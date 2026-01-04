package com.example.programmingc.presentation.ui.lessons.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.programmingc.databinding.FragmentQuizMultipleChoiceBinding
import com.example.programmingc.domain.model.QuizQuestion
import com.example.programmingc.domain.model.ValidationResult
import com.example.programmingc.presentation.ui.menu.BaseMenuBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentQuizMultipleChoice : BaseMenuBar() {
    private var _binding: FragmentQuizMultipleChoiceBinding? = null
    private val binding: FragmentQuizMultipleChoiceBinding get() = _binding!!
    private val quizMultipleChoiceViewModel: QuizMultipleChoiceViewModel by viewModels()
    private lateinit var checkBoxes: List<CheckBox>
    private var currentQuestion: QuizQuestion? = null

    override fun shouldShowMenu(): Boolean {
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizMultipleChoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получаем lectureId из аргументов
        val questionIndex = arguments?.getInt("questionIndex", 0) ?: 0
        val lectureIdString = arguments?.getString("lessonId") ?: ""
        val lectureId = try {
            lectureIdString.toInt()
        } catch (e: NumberFormatException) {
            0
        }

        // Загружаем вопросы для лекции
        quizMultipleChoiceViewModel.loadQuestionsForLecture(lectureId, questionIndex)

        setupCheckBoxButtons()
        setupClickListener()
        observeViewModel()
    }

    private fun setupCheckBoxButtons() {
        checkBoxes = listOf(
            binding.option1,
            binding.option2,
            binding.option3,
            binding.option4
        )

        // Устанавливаем слушатели для каждого CheckBox
        checkBoxes.forEachIndexed { index, checkBox ->
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    quizMultipleChoiceViewModel.selectAnswer(index)
                } else {
                    quizMultipleChoiceViewModel.deselectAnswer(index)
                }
            }
        }
    }

    private fun setupClickListener() {
        binding.checkMultipleAnswersButton.setOnClickListener {
            if (quizMultipleChoiceViewModel.isAnyAnswerSelected()) {
                quizMultipleChoiceViewModel.validateQuizAnswer()
            } else {
                showMessage("Please select at least one answer")
            }
        }
    }

    private fun observeViewModel() {
        // Наблюдаем за текущим вопросом
        quizMultipleChoiceViewModel.currentQuestion.observe(viewLifecycleOwner) { question ->
            question?.let {
                currentQuestion = it
                displayQuestion(it)
            }
        }

        // Наблюдаем за состоянием квиза
        quizMultipleChoiceViewModel.quizState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is QuizMultipleChoiceViewModel.QuizState.Loading -> {
                    showLoading(true)
                }
                is QuizMultipleChoiceViewModel.QuizState.Ready -> {
                    showLoading(false)
                }
                is QuizMultipleChoiceViewModel.QuizState.Error -> {
                    showLoading(false)
                    showMessage(state.message)
                }
                else -> {
                    showLoading(false)
                }
            }
        }

        // Наблюдаем за результатом валидации
        quizMultipleChoiceViewModel.validationResult.observe(viewLifecycleOwner) { result ->
            result?.let {
                handleValidationResult(it)
            }
        }

        // Наблюдаем за выбранными ответами
        quizMultipleChoiceViewModel.selectedAnswers.observe(viewLifecycleOwner) { selectedIndices ->
            updateCheckBoxes(selectedIndices)
        }

        // Наблюдаем за навигационными событиями
        quizMultipleChoiceViewModel.navigationEvents.observe(viewLifecycleOwner) { event ->
            event?.let {
                when (it) {
                    is QuizMultipleChoiceViewModel.QuizNavigationEvent.ShowResults -> {
                        showResults(it.score, it.totalQuestions)
                    }
                    is QuizMultipleChoiceViewModel.QuizNavigationEvent.NavigateToSingleChoice -> {
                        navigateToSingleChoice(it.lessonId, it.questionIndex)
                    }
                    is QuizMultipleChoiceViewModel.QuizNavigationEvent.NavigateToMultipleChoice -> {
                        navigateToMultipleChoice(it.lessonId, it.questionIndex)
                    }
                    is QuizMultipleChoiceViewModel.QuizNavigationEvent.NavigateToInputChoice -> {
                        navigateToInputChoice(it.lessonId, it.questionIndex)
                    }
                }
                quizMultipleChoiceViewModel.clearNavigationEvent()
            }
        }

        // Наблюдаем за ошибками
        quizMultipleChoiceViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                showMessage(it)
                quizMultipleChoiceViewModel.clearError()
            }
        }
    }

    private fun navigateToSingleChoice(lessonId: String, questionIndex: Int) {
        val action = FragmentQuizMultipleChoiceDirections.actionFragmentQuizMultipleChoiceToFragmentQuizSingleChoice(
            lessonId = lessonId,
            questionIndex = questionIndex
        )
        findNavController().navigate(action)
    }

    private fun navigateToMultipleChoice(lessonId: String, questionIndex: Int) {
        val action = FragmentQuizMultipleChoiceDirections.actionFragmentQuizMultipleChoiceSelf(
            lessonId = lessonId,
            questionIndex = questionIndex
        )
        findNavController().navigate(action)
    }

    private fun navigateToInputChoice(lessonId: String, questionIndex: Int) {
        val action = FragmentQuizMultipleChoiceDirections.actionFragmentQuizMultipleChoiceToFragmentQuizInputChoice(
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
            if (index < checkBoxes.size) {
                checkBoxes[index].text = answer
                checkBoxes[index].visibility = View.VISIBLE
            }
        }

        // Скрываем неиспользуемые CheckBox
        for (i in question.options.size until checkBoxes.size) {
            checkBoxes[i].visibility = View.GONE
        }

        // Сбрасываем выбор
        clearSelections()
        clearAnswerFeedback()
    }

    private fun updateCheckBoxes(selectedIndices: List<String>) {
        checkBoxes.forEachIndexed { index, checkBox ->
            // Проверяем, совпадает ли текст текущего CheckBox с выбранным текстом
            val optionText = checkBox.text.toString()
            checkBox.isChecked = selectedIndices.contains(optionText) }
    }

    private fun clearSelections() {
        checkBoxes.forEach { it.isChecked = false }
        quizMultipleChoiceViewModel.clearSelections()
    }

    private fun handleValidationResult(result: ValidationResult) {
        if (result.isCorrect) {
            showCorrectAnswerFeedback()
            binding.checkMultipleAnswersButton.isEnabled = true
        } else {
            showIncorrectAnswerFeedback(result.message)
        }

        updateCheckButtonText()
    }

    private fun showCorrectAnswerFeedback() {
        // Подсвечиваем правильные ответы зеленым
        /*currentQuestion?.correctAnswers?.forEach { correctIndex ->
            if (correctIndex in checkBoxes.indices) {
                checkBoxes[correctIndex.toInt()].setTextColor(resources.getColor(android.R.color.holo_green_dark, null))
            }
        }*/

        binding.checkMultipleAnswersButton.text = if (quizMultipleChoiceViewModel.isLastQuestion()) {
            "Show Results"
        } else {
            "Next Question"
        }
    }

    private fun showIncorrectAnswerFeedback(message: String) {
        showMessage(message)

        /*quizMultipleChoiceViewModel.selectedAnswers.value?.let { selectedAnswers ->
            currentQuestion?.correctAnswers?.let { correctAnswers ->
                selectedAnswers.forEach { selectedIndex ->
                    if (selectedIndex in checkBoxes.indices &&
                        !correctAnswers.contains(selectedIndex)) {
                        checkBoxes[selectedIndex.toInt()].setTextColor(resources.getColor(android.R.color.holo_red_dark, null))
                    }
                }
            }
        }*/
    }

    private fun clearAnswerFeedback() {
        checkBoxes.forEach { checkBox ->
            checkBox.setTextColor(resources.getColor(android.R.color.black, null))
        }
        binding.checkMultipleAnswersButton.isEnabled = true
        binding.checkMultipleAnswersButton.text = "Check Answer"
    }

    private fun updateCheckButtonText() {
        if (quizMultipleChoiceViewModel.validationResult.value?.isCorrect == true) {
            binding.checkMultipleAnswersButton.text = if (quizMultipleChoiceViewModel.isLastQuestion()) {
                "Show Results"
            } else {
                "Next Question"
            }

            val lessonId = arguments?.getString("lessonId") ?: ""

            binding.checkMultipleAnswersButton.setOnClickListener {
                if (quizMultipleChoiceViewModel.isLastQuestion()) {
                    quizMultipleChoiceViewModel.showResults()
                } else {
                    quizMultipleChoiceViewModel.goToNextQuestion(lessonId)
                    clearAnswerFeedback()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.checkMultipleAnswersButton.visibility = View.GONE
        } else {
            binding.checkMultipleAnswersButton.visibility = View.VISIBLE
        }
    }

    private fun showMessage(message: String) {
        android.widget.Toast.makeText(requireContext(), message, android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun showResults(score: Int, totalQuestions: Int) {
        val resultMessage = "Your score: $score/$totalQuestions"
        showMessage(resultMessage)
        // Здесь можно реализовать навигацию к экрану результатов
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}