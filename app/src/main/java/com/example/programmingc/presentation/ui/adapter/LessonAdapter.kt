package com.example.programmingc.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.programmingc.R
import com.example.programmingc.domain.model.Lesson

class LessonAdapter(
    private var lesson: List<Lesson>,
    private val onPracticeClick: (String) -> Unit
): RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {
    inner class LessonViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val lecture: TextView = itemView.findViewById(R.id.lectureID)
        val content: TextView = itemView.findViewById(R.id.contentID)
        val practicing: Button = itemView.findViewById(R.id.startPracticingId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonAdapter.LessonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lesson, parent, false)
        return LessonViewHolder(view)
    }

    override fun onBindViewHolder(holder: LessonAdapter.LessonViewHolder, position: Int) {
        val _lesson = lesson[position]

        holder.lecture.text = _lesson.title
        holder.content.text = _lesson.content
        holder.practicing.setOnClickListener {
            onPracticeClick(_lesson.id)
        }
    }

    override fun getItemCount(): Int = lesson.size

    fun updateData(newLessons: List<Lesson>){
        lesson = newLessons
        notifyDataSetChanged()
    }
}