package com.example.programmingc.presentation.ui.lessons.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programmingc.domain.model.QuestionType
import com.example.programmingc.domain.model.QuizQuestion
import com.example.programmingc.domain.model.ValidationResult
import com.example.programmingc.domain.usecase.GetQuestionsUseCase
import com.example.programmingc.domain.usecase.UseLiveForWrongAnswerUseCase
import com.example.programmingc.domain.usecase.ValidateQuizAnswerUseCase
import com.example.programmingc.presentation.ui.lessons.quiz.QuizSingleChoiceViewModel.NavigationData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizInputChoiceViewModel @Inject constructor(
    private val validateQuizAnswerUseCase: ValidateQuizAnswerUseCase,
    private val getQuestionsUseCase: GetQuestionsUseCase,
    private val useLiveForWrongAnswerUseCase: UseLiveForWrongAnswerUseCase
): ViewModel() {
    private val _questions = MutableLiveData<List<QuizQuestion>>()
    val questions: LiveData<List<QuizQuestion>> = _questions

    private val _currentQuestion = MutableLiveData<QuizQuestion?>()
    val currentQuestion: LiveData<QuizQuestion?> = _currentQuestion

    private val _currentQuestionIndex = MutableLiveData<Int>(0)
    val currentQuestionIndex: LiveData<Int> = _currentQuestionIndex

    private val _validationResult = MutableLiveData<ValidationResult?>()
    val validationResult: LiveData<ValidationResult?> = _validationResult

    private val _navigationEvents = MutableLiveData<QuizNavigationEvent?>()
    val navigationEvents: LiveData<QuizNavigationEvent?> = _navigationEvents

    private val _selectedAnswer = MutableLiveData<String?>()
    val selectedAnswer: LiveData<String?> = _selectedAnswer

    private val _quizState = MutableLiveData<QuizState>(QuizState.Idle)
    val quizState: LiveData<QuizState> = _quizState

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _navigateToNextQuestion = MutableLiveData<NavigationData?>()
    val navigateToNextQuestion: LiveData<NavigationData?> = _navigateToNextQuestion

    private var userAnswers = mutableListOf<String?>() // key: questionId (String)

    fun loadQuestionsForLecture(lectureId: Int, questionIndex: Int) {
        _quizState.value = QuizState.Loading
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val questionsList = getQuestionsUseCase(lectureId)

                // Инициализируем userAnswers null'ами для всех вопросов
                userAnswers.clear()
                repeat(questionsList.size) { userAnswers.add(null) }

                _questions.value = questionsList
                _currentQuestionIndex.value = 0
                _quizState.value = QuizState.Ready

                if (questionsList.isNotEmpty() && questionIndex < questionsList.size) {
                    setCurrentQuestion(questionIndex)
                    println("test")
                } else {
                    _quizState.value = QuizState.Error("No questions available")
                    _errorMessage.value = "No questions available"
                }
            } catch (e: Exception) {
                _quizState.value = QuizState.Error(e.message ?: "Unknown error")
                _errorMessage.value = e.message ?: "Failed to load questions"
            }
        }
    }

    private fun setCurrentQuestion(index: Int) {
        val questionsList = _questions.value ?: return
        //println("index = $index")
        if (index in questionsList.indices) {
            val question = questionsList[index]
            //println("new test of question = $question")
            _currentQuestion.value = question
            _currentQuestionIndex.value = index
            //println("userAnswer.size = ${userAnswers.size}")
            // Устанавливаем сохраненный ответ
            _selectedAnswer.value = if (index < userAnswers.size) {
                userAnswers[index] // String присваивается String? - корректно
            } else {
                null // или "" если хотите пустую строку вместо null
            }
            //println("userAnswers[index] = ${userAnswers[index]}")
            //println("_selectedAnswer = ${_selectedAnswer.value}")
            clearValidation()
        }
    }

    fun clearSelection() {
        _selectedAnswer.value = null
    }

    fun clearValidation() {
        _validationResult.value = null
    }

    // Функция для сохранения ОТВЕТА ПОЛЬЗОВАТЕЛЯ
    fun saveUserAnswer(answer: String) {
        val currentIndex = _currentQuestionIndex.value ?: return
        val trimmedAnswer = answer.trim()

        // Гарантируем, что список достаточно большой
        while (userAnswers.size <= currentIndex) {
            userAnswers.add(null)
        }

        // Сохраняем ответ пользователя
        userAnswers[currentIndex] = trimmedAnswer

        // Обновляем LiveData
        _selectedAnswer.value = trimmedAnswer

        println("DEBUG: User answer saved: '$trimmedAnswer' at index $currentIndex")
        println("DEBUG: All user answers: $userAnswers")
    }

    fun validateQuizAnswer(){
        val question = _currentQuestion.value
        val selectedIndex = _selectedAnswer.value

        if (question == null || selectedIndex == null) {
            _validationResult.value = ValidationResult(
                isCorrect = false,
                message = "Please select an answer"
            )
            return
        }

        viewModelScope.launch {
            try {
                val result = validateQuizAnswerUseCase(
                    question = question,
                    userAnswers = listOf(selectedIndex)
                )
                _validationResult.value = result

                if (!result.isCorrect){
                    useLiveForWrongAnswerUseCase.invoke()
                }
            } catch (e: Exception) {
                _validationResult.value = ValidationResult(
                    isCorrect = false,
                    message = "Validation error: ${e.message}"
                )
            }
        }
    }

    fun goToNextQuestion(lessonId: String) {
        val currentIndex = _currentQuestionIndex.value ?: 0
        val questionsList = _questions.value ?: emptyList()

        if (currentIndex < questionsList.size - 1) {
            val nextIndex = currentIndex + 1
            val nextQuestion = questionsList[nextIndex]

            _navigationEvents.value = when (nextQuestion.questionType) {
                QuestionType.SINGLE_CHOICE ->
                    QuizNavigationEvent.NavigateToSingleChoice(lessonId, nextIndex)

                QuestionType.MULTIPLE_CHOICE ->
                    QuizNavigationEvent.NavigateToMultipleChoice(lessonId, nextIndex)

                QuestionType.INPUT_CHOICE ->
                    QuizNavigationEvent.NavigateToInputChoice(lessonId, nextIndex)
            } /*else {
                showResults()
            }*/
        }
    }

    fun clearNavigationEvent() {
        _navigationEvents.value = null
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun getTotalQuestions(): Int {
        return _questions.value?.size ?: 0
    }

    fun isLastQuestion(): Boolean {
        val currentIndex = _currentQuestionIndex.value ?: 0
        val totalQuestions = getTotalQuestions()
        return currentIndex == totalQuestions - 1
    }

    sealed class QuizState {
        object Idle : QuizState()
        object Loading : QuizState()
        object Ready : QuizState()
        data class Error(val message: String) : QuizState()
    }
    sealed class QuizNavigationEvent {
        data class NavigateToSingleChoice(val lessonId: String, val questionIndex: Int) : QuizNavigationEvent()
        data class NavigateToMultipleChoice(val lessonId: String, val questionIndex: Int) : QuizNavigationEvent()
        data class NavigateToInputChoice(val lessonId: String, val questionIndex: Int) : QuizNavigationEvent()
        data class NavigateToResults(val score: Int, val totalQuestions: Int) : QuizNavigationEvent()
        data class ShowResults(val score: Int, val totalQuestions: Int) : QuizNavigationEvent()
    }
}