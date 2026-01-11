package com.example.programmingc.presentation.ui.courses

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.programmingc.R
import com.example.programmingc.databinding.LessonBinding
import com.example.programmingc.domain.model.Lesson
import com.example.programmingc.domain.model.QuestionType
import com.example.programmingc.presentation.edit_text.CodeContent
import com.example.programmingc.presentation.edit_text.TextContent
import com.example.programmingc.presentation.edit_text.parseContentWithCodeBlocks
import com.example.programmingc.presentation.model.RoundedBackgroundSpan
import com.example.programmingc.presentation.ui.adapter.LessonAdapter
import com.example.programmingc.presentation.ui.menu.BaseMenuBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LessonFragment: BaseMenuBar() {
    private var _binding: LessonBinding? = null
    private val binding: LessonBinding get() = _binding!!
    private val lessonViewModel: LessonViewModel by viewModels()
    private lateinit var adapter: LessonAdapter

    override fun shouldShowMenu(): Boolean {
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LessonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lessonId = arguments?.getString("lessonId") ?: ""

        lessonViewModel.loadLesson(lessonId)
        lessonViewModel.loadQuestionsForLesson(lessonId)

        setupObservers(lessonId)
        //setupClickListeners()
    }

    private fun setupObservers(lessonId: String){
        lessonViewModel.currentLesson.observe(viewLifecycleOwner) { lesson ->
            lesson?.let{
                displayLesson(it)
            }
        }
        lessonViewModel.questions.observe(viewLifecycleOwner) { questions ->
            binding.startPracticingId.isEnabled = !questions.isNullOrEmpty()
            if (questions.isNullOrEmpty()) {
                binding.startPracticingId.text = "No questions available"
            } else {
                binding.startPracticingId.text = "Start Practicing"
            }
        }
    }

    private fun displayLesson(lesson: Lesson){
        binding.lectureID.text = lesson.title
        binding.contentLayout.removeAllViews()

        val parsedContent = parseContentWithCodeBlocks(lesson.content)

        parsedContent.forEach { content ->
            when (content){
                is TextContent -> {
                    addTextView(binding.contentLayout, requireContext(), content.text)
                }
                is CodeContent -> {
                    addCodeView(binding.contentLayout, requireContext(), content.code)
                }
            }
        }
        // ✅ Установите обработчик кнопки один раз
        setupPracticeButton(lesson.id)
    }

    private fun setupPracticeButton(lessonId: String) {
        binding.startPracticingId.setOnClickListener {
            startPractice(lessonId) // ✅ Передаем lessonId
        }
    }

    private fun startPractice(lessonId: String){ // ✅ Принимаем lessonId как параметр
        val questions = lessonViewModel.questions.value

        if (questions.isNullOrEmpty()) {
            return
        }

        try {
            val firstQuestion = questions[0]
            //println("\n\nfirstQuestion = $firstQuestion\n\n")
            val actionId = when (firstQuestion.questionType) {
                QuestionType.SINGLE_CHOICE -> R.id.action_fragment_lesson_to_fragment_quiz_single_choice
                QuestionType.MULTIPLE_CHOICE -> R.id.action_fragment_lesson_to_fragment_quiz_multiple_choice
                QuestionType.INPUT_CHOICE -> R.id.action_fragment_lesson_to_fragment_quiz_input_choice
            }

            val bundle = Bundle().apply {
                putString("lessonId", lessonId)
            }
            findNavController().navigate(actionId, bundle)

        } catch (e: IllegalArgumentException) {
            //showErrorMessage("No questions available for this lesson")
        } catch (e: Exception) {
            //showErrorMessage("Failed to start quiz: ${e.message}")
        }
    }

    private fun addTextView(container: LinearLayout, context: Context, text: SpannableStringBuilder) {
        val textView = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setText(text)
            setTextIsSelectable(true)
            textSize = 16f
            setLineSpacing(4f, 1f)
        }
        container.addView(textView)
    }

    private fun addCodeView(container: LinearLayout, context: Context, code: String) {
        // HorizontalScrollView для кода
        val scrollView = HorizontalScrollView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            isHorizontalScrollBarEnabled = true
        }

        val codeTextView = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            text = createCodeSpannable(code, context)
            setTextIsSelectable(true)
            textSize = 14f
            setTypeface(typeface, Typeface.ITALIC)
            setPadding(32, 16, 32, 16)
        }

        scrollView.addView(codeTextView)
        container.addView(scrollView)
    }

    private fun createCodeSpannable(code: String, context: Context): SpannableString {
        val spannable = SpannableString(code.trim())

        // Добавляем фон
        spannable.setSpan(
            RoundedBackgroundSpan(Color.parseColor("#E3F2FD"), 16f),
            0,
            spannable.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannable
    }
}