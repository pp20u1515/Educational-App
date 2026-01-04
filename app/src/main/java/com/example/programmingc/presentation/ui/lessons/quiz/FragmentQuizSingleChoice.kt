package com.example.programmingc.presentation.ui.lessons.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.programmingc.R
import com.example.programmingc.databinding.FragmentQuizSingleChoiceBinding
import com.example.programmingc.domain.model.QuizQuestion
import com.example.programmingc.domain.model.ValidationResult
import com.example.programmingc.presentation.ui.menu.BaseMenuBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentQuizSingleChoice : BaseMenuBar() {
    private var _binding: FragmentQuizSingleChoiceBinding? = null
    private val binding: FragmentQuizSingleChoiceBinding get() = _binding!!
    private val quizSingleChoiceViewModel: QuizSingleChoiceViewModel by viewModels()
    private lateinit var radioButtons: List<RadioButton>
    private var currentQuestion: QuizQuestion? = null

    override fun shouldShowMenu(): Boolean {
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizSingleChoiceBinding.inflate(inflater, container, false)
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
        // Загружаем вопросы для лекции
        quizSingleChoiceViewModel.loadQuestionsForLecture(lectureId, questionIndex)

        setupRadioButtons()
        setupClickListener()
        observeViewModel()
    }

    private fun setupRadioButtons() {
        radioButtons = listOf(
            binding.radioButton1,
            binding.radioButton2,
            binding.radioButton3,
            binding.radioButton4
        )

        binding.answersRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButton1 -> onAnswerSelected(0)
                R.id.radioButton2 -> onAnswerSelected(1)
                R.id.radioButton3 -> onAnswerSelected(2)
                R.id.radioButton4 -> onAnswerSelected(3)
            }
        }
    }

    private fun onAnswerSelected(answerIndex: Int) {
        quizSingleChoiceViewModel.selectAnswer(answerIndex)
    }

    private fun setupClickListener() {
        binding.checkAnswerButton.setOnClickListener {
            if (quizSingleChoiceViewModel.isAnswerSelected()) {
                quizSingleChoiceViewModel.validateQuizAnswer()
            } else {
                // Показать сообщение о необходимости выбора ответа
                showMessage("Please select an answer")
            }
        }
    }

    private fun observeViewModel() {
        // Наблюдаем за текущим вопросом
        quizSingleChoiceViewModel.currentQuestion.observe(viewLifecycleOwner) { question ->
            question?.let {
                currentQuestion = it
                displayQuestion(it)
            }
        }

        // Наблюдаем за состоянием квиза
        quizSingleChoiceViewModel.quizState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is QuizSingleChoiceViewModel.QuizState.Loading -> {
                    showLoading(true)
                }
                is QuizSingleChoiceViewModel.QuizState.Ready -> {
                    showLoading(false)
                }
                is QuizSingleChoiceViewModel.QuizState.Error -> {
                    showLoading(false)
                    showMessage(state.message)
                }
                else -> {
                    showLoading(false)
                }
            }
        }

        // Наблюдаем за результатом валидации
        quizSingleChoiceViewModel.validationResult.observe(viewLifecycleOwner) { result ->
            result?.let {
                handleValidationResult(it)
            }
        }

        // Наблюдаем за выбранным ответом
        quizSingleChoiceViewModel.selectedAnswer.observe(viewLifecycleOwner) { selectedIndex ->
            updateRadioButtons(selectedIndex)
        }

        // Наблюдаем за навигационными событиями
        quizSingleChoiceViewModel.navigationEvents.observe(viewLifecycleOwner) { event ->
            event?.let {
                when (it) {
                    is QuizSingleChoiceViewModel.QuizNavigationEvent.ShowResults -> {
                        showResults(it.score, it.totalQuestions)
                    }
                    is QuizSingleChoiceViewModel.QuizNavigationEvent.NavigateToSingleChoice -> {
                        navigateToSingleChoice(it.lessonId, it.questionIndex)
                    }
                    is QuizSingleChoiceViewModel.QuizNavigationEvent.NavigateToMultipleChoice -> {
                        navigateToMultipleChoice(it.lessonId, it.questionIndex)
                    }
                    is QuizSingleChoiceViewModel.QuizNavigationEvent.NavigateToInputChoice -> {
                        navigateToInputChoice(it.lessonId, it.questionIndex)
                    }
                    is QuizSingleChoiceViewModel.QuizNavigationEvent.NavigateToResults -> {
                        // Обработка навигации к результатам если нужно
                        showResults(it.score, it.totalQuestions)
                    }
                }
                // Сбрасываем событие после обработки
                quizSingleChoiceViewModel.clearNavigationEvent()
            }
        }

        // Наблюдаем за ошибками
        quizSingleChoiceViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                showMessage(it)
                quizSingleChoiceViewModel.clearError()
            }
        }
    }

    private fun navigateToSingleChoice(lessonId: String, questionIndex: Int) {
        // Навигируем к тому же фрагменту с новым индексом вопроса
        val action = FragmentQuizSingleChoiceDirections.actionFragmentQuizSingleChoiceSelf(
            lessonId = lessonId,
            questionIndex = questionIndex
        )
        findNavController().navigate(action)
    }

    private fun navigateToMultipleChoice(lessonId: String, questionIndex: Int) {
        // Навигируем к фрагменту множественного выбора
        val action = FragmentQuizSingleChoiceDirections.actionFragmentQuizSingleChoiceToFragmentQuizMultipleChoice(
            lessonId = lessonId,
            questionIndex = questionIndex
        )
        findNavController().navigate(action)
    }

    private fun navigateToInputChoice(lessonId: String, questionIndex: Int) {
        // Навигируем к фрагменту ввода текста
        val action = FragmentQuizSingleChoiceDirections.actionFragmentQuizSingleChoiceToFragmentQuizInputChoice(
            lessonId = lessonId,
            questionIndex = questionIndex
        )
        findNavController().navigate(action)
    }

    private fun displayQuestion(question: QuizQuestion) {
        // Устанавливаем текст вопроса
        binding.questionId.text = question.question

        // Устанавливаем варианты ответов
        question.options.forEachIndexed { index, answer ->
            if (index < radioButtons.size) {
                radioButtons[index].text = answer
                radioButtons[index].visibility = View.VISIBLE
            }
        }

        // Скрываем неиспользуемые RadioButton
        for (i in question.options.size until radioButtons.size) {
            radioButtons[i].visibility = View.GONE
        }

        // Сбрасываем выбор
        binding.answersRadioGroup.clearCheck()
        clearAnswerFeedback()
    }

    private fun updateRadioButtons(selectedIndex: Int?) {
        selectedIndex?.let { index ->
            if (index in radioButtons.indices) {
                binding.answersRadioGroup.check(radioButtons[index].id)
            }
        } ?: run {
            binding.answersRadioGroup.clearCheck()
        }
    }

    private fun handleValidationResult(result: ValidationResult) {
        if (result.isCorrect) {
            showCorrectAnswerFeedback()
            // Кнопка становится неактивной после правильного ответа
            binding.checkAnswerButton.isEnabled = true
        } else {
            showIncorrectAnswerFeedback(result.message)
        }

        // Обновляем текст кнопки в зависимости от того, последний ли это вопрос
        updateCheckButtonText()
    }

    private fun showCorrectAnswerFeedback() {
        // Подсвечиваем правильный ответ зеленым
        /*currentQuestion?.correctAnswers?.firstOrNull()?.let { correctIndex ->
            if (correctIndex in radioButtons.indices) {
                radioButtons[correctIndex.toInt()].setTextColor(resources.getColor(android.R.color.holo_green_dark, null))
            }
        }*/

        binding.checkAnswerButton.text = if (quizSingleChoiceViewModel.isLastQuestion()) {
            "Show Results"
        } else {
            "Next Question"
        }
    }

    private fun showIncorrectAnswerFeedback(message: String) {
        showMessage(message)
        // Можно подсветить неправильный ответ красным
        quizSingleChoiceViewModel.selectedAnswer.value?.let { selectedIndex ->
            if (selectedIndex in radioButtons.indices) {
                radioButtons[selectedIndex].setTextColor(resources.getColor(android.R.color.holo_red_dark, null))
            }
        }
    }

    private fun clearAnswerFeedback() {
        radioButtons.forEach { radioButton ->
            radioButton.setTextColor(resources.getColor(android.R.color.black, null))
        }
        binding.checkAnswerButton.isEnabled = true
        binding.checkAnswerButton.text = "Check Answer"
    }

    private fun updateCheckButtonText() {
        if (quizSingleChoiceViewModel.validationResult.value?.isCorrect == true) {
            binding.checkAnswerButton.text = if (quizSingleChoiceViewModel.isLastQuestion()) {
                "Show Results"
            } else {
                "Next Question"
            }

            val lessonId = arguments?.getString("lessonId") ?: ""

            // Изменяем поведение кнопки после правильного ответа
            binding.checkAnswerButton.setOnClickListener {
                if (quizSingleChoiceViewModel.isLastQuestion()) {
                    quizSingleChoiceViewModel.showResults()
                } else {
                    quizSingleChoiceViewModel.goToNextQuestion(lessonId)
                    clearAnswerFeedback()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.checkAnswerButton.visibility = View.GONE
            // Можно показать ProgressBar
        } else {
            binding.checkAnswerButton.visibility = View.VISIBLE
        }
    }

    private fun showMessage(message: String) {
        // Реализуйте показ сообщений (Toast, Snackbar и т.д.)
        android.widget.Toast.makeText(requireContext(), message, android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun showResults(score: Int, totalQuestions: Int) {
        // Переход к экрану результатов
        val resultMessage = "Your score: $score/$totalQuestions"
        showMessage(resultMessage)

        // Здесь можно реализовать навигацию к экрану результатов
        // findNavController().navigate(R.id.action_quizFragment_to_resultsFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setupQuestion(question: QuizQuestion) {
        quizSingleChoiceViewModel.setQuestion(question)
    }
}