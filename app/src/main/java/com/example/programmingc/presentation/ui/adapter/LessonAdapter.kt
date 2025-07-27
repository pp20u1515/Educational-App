package com.example.programmingc.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.programmingc.R
import com.example.programmingc.presentation.ui.objects.visiable_objects.Lesson

class LessonAdapter(
    private val lessons: List<Lesson>
): RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {
    inner class LessonViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.lessonTitle)
        val description: TextView = itemView.findViewById(R.id.lessonDescription)
        val firstImage: ImageView = itemView.findViewById(R.id.image1)
        val secondImage: ImageView = itemView.findViewById(R.id.image2)
        val thirdImage: ImageView = itemView.findViewById(R.id.image3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lesson, parent, false)
        return LessonViewHolder(view)
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        val lesson = lessons[position]
        holder.title.text = "Lesson ${lesson.number}: ${lesson.title}"
        holder.description.text = lesson.text

        val images = lesson.imageResIds
        holder.firstImage.setImageResource(images.getOrNull(0) ?: R.drawable.image_study)
        holder.secondImage.setImageResource(images.getOrNull(1) ?: R.drawable.image_c)
        holder.thirdImage.setImageResource(images.getOrNull(2) ?: R.drawable.baseline_play_arrow)
    }

    override fun getItemCount(): Int = lessons.size
}