package com.example.programmingc.presentation.ui.lessons.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programmingc.domain.model.QuestionType
import com.example.programmingc.domain.model.QuizQuestion
import com.example.programmingc.domain.model.ValidationResult
import com.example.programmingc.domain.usecase.GetQuestionsUseCase
import com.example.programmingc.domain.usecase.ValidateQuizAnswerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizSingleChoiceViewModel @Inject constructor(
    private val validateQuizAnswerUseCase: ValidateQuizAnswerUseCase,
    private val getQuestionsUseCase: GetQuestionsUseCase
) : ViewModel() {

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

    private val _selectedAnswer = MutableLiveData<Int?>()
    val selectedAnswer: LiveData<Int?> = _selectedAnswer

    private val _quizState = MutableLiveData<QuizState>(QuizState.Idle)
    val quizState: LiveData<QuizState> = _quizState

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _navigateToNextQuestion = MutableLiveData<NavigationData?>()
    val navigateToNextQuestion: LiveData<NavigationData?> = _navigateToNextQuestion

    private var userAnswers: MutableMap<String, Int> = mutableMapOf() // key: questionId (String)

    // Загрузка вопросов для лекции
    fun loadQuestionsForLecture(lectureId: Int, questionIndex: Int) {
        _quizState.value = QuizState.Loading
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val questionsList = getQuestionsUseCase(lectureId)

                _questions.value = questionsList
                _currentQuestionIndex.value = 0
                _quizState.value = QuizState.Ready

                if (questionsList.isNotEmpty() && questionIndex < questionsList.size) {
                    setCurrentQuestion(questionIndex)
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

        if (index in questionsList.indices) {
            val question = questionsList[index]
            _currentQuestion.value = question
            _currentQuestionIndex.value = index

            // Восстанавливаем выбранный ответ если он есть
            userAnswers[question.id]?.let { selectedIndex ->
                _selectedAnswer.value = selectedIndex
            } ?: run {
                _selectedAnswer.value = null
            }
            clearValidation()
        }
    }

    fun setQuestion(question: QuizQuestion) {
        _currentQuestion.value = question
        clearSelection()
        clearValidation()
    }

    fun clearSelection() {
        _selectedAnswer.value = null
    }

    fun clearValidation() {
        _validationResult.value = null
    }

    fun selectAnswer(answerIndex: Int) {
        _selectedAnswer.value = answerIndex
        _currentQuestion.value?.let { question ->
            userAnswers[question.id] = answerIndex
        }
    }

    fun validateQuizAnswer() {
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
                    userAnswers = listOf(selectedIndex.toString())
                )
                _validationResult.value = result

                // Автоматически переходим к следующему вопросу после правильного ответа
                if (result.isCorrect) {
                    // Задержка для показа feedback
                    kotlinx.coroutines.delay(1000)
                    //goToNextQuestion()
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

    data class NavigationData(
        val question: QuizQuestion,
        val lectureId: Int,
        val questionIndex: Int
    )

    fun goToPreviousQuestion() {
        val currentIndex = _currentQuestionIndex.value ?: 0
        val questionsList = _questions.value ?: emptyList()

        if (currentIndex > 0) {
            setCurrentQuestion(currentIndex - 1)
        }
    }

    fun showResults() {
        val questionsList = _questions.value ?: emptyList()
        val score = calculateScore()
        _navigationEvents.value = QuizNavigationEvent.ShowResults(score, questionsList.size)
    }

    private fun calculateScore(): Int {
        val questionsList = _questions.value ?: return 0
        var score = 0

        /*questionsList.forEach { question ->
            val userAnswer = userAnswers[question.id]
            if (userAnswer != null && question.correctAnswers.contains(userAnswer)) {
                score++
            }
        }*/

        return score
    }

    fun isAnswerSelected(): Boolean {
        return _selectedAnswer.value != null
    }

    fun getTotalQuestions(): Int {
        return _questions.value?.size ?: 0
    }

    fun isLastQuestion(): Boolean {
        val currentIndex = _currentQuestionIndex.value ?: 0
        val totalQuestions = getTotalQuestions()
        return currentIndex == totalQuestions - 1
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun clearNavigationEvent() {
        _navigationEvents.value = null
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