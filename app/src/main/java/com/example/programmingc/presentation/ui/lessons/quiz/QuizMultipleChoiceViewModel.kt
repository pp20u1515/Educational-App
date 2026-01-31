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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizMultipleChoiceViewModel @Inject constructor(
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

    private val _selectedAnswers = MutableLiveData<List<String>>(emptyList())
    val selectedAnswers: LiveData<List<String>> = _selectedAnswers

    private val _quizState = MutableLiveData<QuizState>(QuizState.Idle)
    val quizState: LiveData<QuizState> = _quizState

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private var userAnswers: MutableMap<String, List<String>> = mutableMapOf()

    // Загрузка вопросов для лекции
    fun loadQuestionsForLecture(lectureId: Int, questionIndex: Int) {
        _quizState.value = QuizState.Loading
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val questionsList = getQuestionsUseCase(lectureId)
                _questions.value = questionsList
                _currentQuestionIndex.value = questionIndex
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

            // Восстанавливаем выбранные ответы если они есть
            userAnswers[question.id]?.let { selectedIndices ->
                _selectedAnswers.value = selectedIndices
            } ?: run {
                _selectedAnswers.value = emptyList()
            }
            clearValidation()
        }
    }

    fun selectAnswer(answerIndex: Int) {
        val currentSelections = _selectedAnswers.value ?: emptyList()
        val currentQuestion = _currentQuestion.value

        if (currentQuestion!= null && !currentSelections.contains(answerIndex.toString())) {
            val selectedOptionText = currentQuestion.options.getOrNull(answerIndex)
            selectedOptionText?.let { optionText ->
                _selectedAnswers.value = currentSelections + optionText
                saveUserAnswer()
            }
        }
    }

    fun deselectAnswer(answerIndex: Int) {
        val currentSelections = _selectedAnswers.value ?: emptyList()
        val currentQuestion = _currentQuestion.value

        if (currentQuestion != null) {
            val optionText = currentQuestion.options.getOrNull(answerIndex)
            optionText?.let {
                _selectedAnswers.value = currentSelections - it
                saveUserAnswer()
            }
        }
    }

    fun clearSelections() {
        _selectedAnswers.value = emptyList()
    }

    private fun saveUserAnswer() {
        _currentQuestion.value?.let { question ->
            userAnswers[question.id] = _selectedAnswers.value ?: emptyList()
        }
    }

    fun clearValidation() {
        _validationResult.value = null
    }

    fun validateQuizAnswer() {
        val question = _currentQuestion.value
        val selectedOptions = _selectedAnswers.value // Теперь это List<String>

        if (question == null || selectedOptions.isNullOrEmpty()) {
            _validationResult.value = ValidationResult(
                isCorrect = false,
                message = "Please select at least one answer"
            )
            return
        }

        viewModelScope.launch {
            try {
                val result =
                    validateQuizAnswerUseCase(
                        question = question,
                        userAnswers = selectedOptions
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
            }
        } else {
            showResults()
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

        questionsList.forEach { question ->
            val userAnswer = userAnswers[question.id] // List<String>

            // Сравниваем выбранные тексты с правильными ответами
            if (userAnswer != null && userAnswer.sorted() == question.correctAnswers.sorted()) {
                score++
            }
        }

        return score
    }

    fun isAnyAnswerSelected(): Boolean {
        return _selectedAnswers.value?.isNotEmpty() == true
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
        data class ShowResults(val score: Int, val totalQuestions: Int) : QuizNavigationEvent()
    }
}