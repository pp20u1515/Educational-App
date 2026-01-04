package com.example.programmingc.presentation.ui.lessons.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.programmingc.databinding.FragmentQuizInputChoiceBinding
import com.example.programmingc.domain.model.QuizQuestion
import com.example.programmingc.domain.model.ValidationResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentQuizInputChoice: BaseQuizFragment() {
    private var _binding: FragmentQuizInputChoiceBinding? = null
    private val binding: FragmentQuizInputChoiceBinding get() = _binding!!
    private val quizInputChoiceViewModel: QuizInputChoiceViewModel by viewModels()
    private var currentQuestion: QuizQuestion? = null

    override fun shouldShowMenu(): Boolean {
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizInputChoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получаем lectureId из аргументов
        val questionIndex = arguments?.getInt("questionIndex", 0) ?: 0
        val lectureIdString = arguments?.getString("lessonId") ?: ""
        val lectureId = try {
            lectureIdString!!.toInt()
        } catch (e: NumberFormatException){
            0
        }

        quizInputChoiceViewModel.loadQuestionsForLecture(lectureId, questionIndex)

        setupClickListener()
        observeViewModel()
    }

    private fun setupClickListener(){
        binding.checkAnswerButton.setOnClickListener {
            val answer = binding.answerField.text?.toString()?.trim()

            if (!answer.isNullOrEmpty()){
                quizInputChoiceViewModel.saveUserAnswer(answer)
                quizInputChoiceViewModel.validateQuizAnswer()
            }
            else{
                showMessage("Please write your answer")
            }
        }
    }

    private fun showMessage(message: String) {
        // Реализуйте показ сообщений (Toast, Snackbar и т.д.)
        android.widget.Toast.makeText(requireContext(), message, android.widget.Toast.LENGTH_SHORT).show()
    }

    override fun getViewModel(): BaseQuizViewModel = quizInputChoiceViewModel

    override fun observeViewModel() {
        // Наблюдаем за текущим вопросом
        quizInputChoiceViewModel.currentQuestion.observe(viewLifecycleOwner) { question ->
            question?.let {
                currentQuestion = it
                displayQuestion(it)
            }
        }
        // Наблюдаем за результатом валидации
        quizInputChoiceViewModel.validationResult.observe(viewLifecycleOwner) { result ->
            result?.let {
                handleValidationResult(it)
            }
        }

        // Наблюдаем за навигационными событиями
        quizInputChoiceViewModel.navigationEvents.observe(viewLifecycleOwner) { event ->
            event?.let {
                when (it) {
                    is QuizInputChoiceViewModel.QuizNavigationEvent.ShowResults -> {
                        showResults(it.score, it.totalQuestions)
                    }
                    is QuizInputChoiceViewModel.QuizNavigationEvent.NavigateToSingleChoice -> {
                        navigateToSingleChoice(it.lessonId, it.questionIndex)
                    }
                    is QuizInputChoiceViewModel.QuizNavigationEvent.NavigateToMultipleChoice -> {
                        navigateToMultipleChoice(it.lessonId, it.questionIndex)
                    }
                    is QuizInputChoiceViewModel.QuizNavigationEvent.NavigateToInputChoice -> {
                        navigateToInputChoice(it.lessonId, it.questionIndex)
                    }
                    is QuizInputChoiceViewModel.QuizNavigationEvent.NavigateToResults -> {
                        // Обработка навигации к результатам если нужно
                        showResults(it.score, it.totalQuestions)
                    }
                }
                // Сбрасываем событие после обработки
                quizInputChoiceViewModel.clearNavigationEvent()
            }
        }
        // Наблюдаем за ошибками
        quizInputChoiceViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                showMessage(it)
                quizInputChoiceViewModel.clearError()
            }
        }
    }

    private fun showResults(score: Int, totalQuestions: Int) {
        // Переход к экрану результатов
        val resultMessage = "Your score: $score/$totalQuestions"
        showMessage(resultMessage)

        // Здесь можно реализовать навигацию к экрану результатов
        // findNavController().navigate(R.id.action_quizFragment_to_resultsFragment)
    }

    private fun navigateToSingleChoice(lessonId: String, questionIndex: Int) {
        // Навигируем к тому же фрагменту с новым индексом вопроса
        val action = FragmentQuizInputChoiceDirections.actionFragmentQuizInputChoiceToFragmentQuizSingleChoice(
            lessonId = lessonId,
            questionIndex = questionIndex
        )
        findNavController().navigate(action)
    }

    private fun navigateToMultipleChoice(lessonId: String, questionIndex: Int) {
        // Навигируем к фрагменту множественного выбора
        val action = FragmentQuizInputChoiceDirections.actionFragmentQuizInputChoiceToFragmentQuizMultipleChoice(
            lessonId = lessonId,
            questionIndex = questionIndex
        )
        findNavController().navigate(action)
    }

    private fun navigateToInputChoice(lessonId: String, questionIndex: Int) {
        // Навигируем к фрагменту ввода текста
        val action = FragmentQuizInputChoiceDirections.actionFragmentQuizInputChoiceSelf(
            lessonId = lessonId,
            questionIndex = questionIndex
        )
        findNavController().navigate(action)
    }

    private fun handleValidationResult(result: ValidationResult){
        if (result.isCorrect){
            showCorrectAnswerFeedback()
            // Кнопка становится неактивной после правильного ответа
            binding.checkAnswerButton.isEnabled = true
        }
        else{
            showIncorrectAnswerFeedback(result.message)
        }
        updateCheckButtonText()
    }

    private fun updateCheckButtonText() {
        if (quizInputChoiceViewModel.validationResult.value?.isCorrect == true) {
            binding.checkAnswerButton.text = if (quizInputChoiceViewModel.isLastQuestion()) {
                "Show Results"
            } else {
                "Next Question"
            }

            val lessonId = arguments?.getString("lessonId") ?: ""

            // Изменяем поведение кнопки после правильного ответа
            binding.checkAnswerButton.setOnClickListener {
                if (quizInputChoiceViewModel.isLastQuestion()) {
                    quizInputChoiceViewModel.showResults()
                } else {
                    quizInputChoiceViewModel.goToNextQuestion(lessonId)
                }
            }
        }
    }

    private fun showCorrectAnswerFeedback(){
        binding.checkAnswerButton.text = if (quizInputChoiceViewModel.isLastQuestion()){
            "Show Results"
        } else {
            "Next Question"
        }
    }

    private fun showIncorrectAnswerFeedback(message: String){
        showMessage(message)
    }

    private fun displayQuestion(question: QuizQuestion){
        // Устанавливаем текст вопроса
        binding.questionId.text = question.question


    }

    override fun setupQuestion(question: QuizQuestion) {
        TODO("Not yet implemented")
    }
}