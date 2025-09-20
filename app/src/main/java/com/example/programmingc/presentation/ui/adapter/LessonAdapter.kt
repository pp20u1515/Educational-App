package com.example.programmingc.presentation.ui.adapter

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.programmingc.R
import com.example.programmingc.domain.model.Lesson
import com.example.programmingc.presentation.edit_text.TextContent
import com.example.programmingc.presentation.edit_text.CodeContent
import com.example.programmingc.presentation.edit_text.parseContentWithCodeBlocks
import com.example.programmingc.presentation.model.RoundedBackgroundSpan

class LessonAdapter(
    private var lesson: List<Lesson>,
    private val onPracticeClick: (String) -> Unit
): RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {

    inner class LessonViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val lecture: TextView = itemView.findViewById(R.id.lectureID)
        val contentLayout: LinearLayout = itemView.findViewById(R.id.contentLayout)
        val practicing: Button = itemView.findViewById(R.id.startPracticingId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lesson, parent, false)
        return LessonViewHolder(view)
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        val _lesson = lesson[position]
        val parsedContent = parseContentWithCodeBlocks(_lesson.content)

        holder.lecture.text = _lesson.title

        // Очищаем контейнер перед добавлением новых элементов
        holder.contentLayout.removeAllViews()

        // Добавляем элементы контента
        parsedContent.forEach { content ->
            when (content) {
                is TextContent -> {
                    addTextView(holder.contentLayout, holder.itemView.context, content.text)
                }
                is CodeContent -> {
                    addCodeView(holder.contentLayout, holder.itemView.context, content.code)
                }
            }
        }

        holder.practicing.setOnClickListener {
            onPracticeClick(_lesson.id)
        }
    }

    override fun getItemCount(): Int = lesson.size

    fun updateData(newLessons: List<Lesson>){
        lesson = newLessons
        notifyDataSetChanged()
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
            setTypeface(typeface, android.graphics.Typeface.ITALIC)
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