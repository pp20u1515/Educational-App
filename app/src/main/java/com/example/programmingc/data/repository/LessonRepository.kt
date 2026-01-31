package com.example.programmingc.data.repository

import com.example.programmingc.domain.repo.ILessonRepository
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import com.example.programmingc.R
import com.example.programmingc.domain.model.Lesson
import com.example.programmingc.domain.model.LessonWindow
import javax.inject.Inject

class LessonRepository @Inject constructor(
    @param:ApplicationContext private val context: Context
): ILessonRepository {
    private val gson = Gson()

    override suspend fun getLessonById(id: String): Lesson {
        val json = loadJsonFromAssets("lectures/lecture_$id.json")

        return gson.fromJson(json, Lesson::class.java)
    }

    override suspend fun showLessons(): List<LessonWindow> {
        return listOf(
            LessonWindow("1", 1, "", "Introduction to C", listOf(R.drawable.image_study, R.drawable.image_c, R.drawable.baseline_play_arrow)),
            LessonWindow("2", 2, "", "Writing the first C program", listOf(R.drawable.image_study, R.drawable.image_c, R.drawable.baseline_play_arrow)),
            LessonWindow("3", 3, "", "First look at input/output", listOf(R.drawable.image_study, R.drawable.image_c, R.drawable.baseline_play_arrow)),
            LessonWindow("4", 4, "", "Data Structure and Types", listOf(R.drawable.image_study, R.drawable.image_c, R.drawable.baseline_play_arrow)),
            LessonWindow("5", 5, "", "Variables", listOf(R.drawable.image_study, R.drawable.image_c, R.drawable.baseline_play_arrow)),
            LessonWindow("6", 6, "","Pointers: Part 1", listOf(R.drawable.image_study, R.drawable.image_c, R.drawable.baseline_play_arrow)),
            LessonWindow("7", 7, "", "Pointers: Part 2", listOf(R.drawable.image_study, R.drawable.image_c, R.drawable.baseline_play_arrow)),
            LessonWindow("8", 8, "", "Functions", listOf(R.drawable.image_study, R.drawable.image_c, R.drawable.baseline_play_arrow)),
            LessonWindow("9", 9, "", "Conditional loops", listOf(R.drawable.image_study, R.drawable.image_c, R.drawable.baseline_play_arrow)),
            LessonWindow("10", 10, "", "Arrays", listOf(R.drawable.image_study, R.drawable.image_c, R.drawable.baseline_play_arrow)),
            LessonWindow("11", 11, "", "Arithmetic Operations", listOf(R.drawable.image_study, R.drawable.image_c, R.drawable.baseline_play_arrow)),
            LessonWindow("12", 12, "", "Increment (++) and Decrement (--) Operators", listOf(R.drawable.image_study, R.drawable.image_c, R.drawable.baseline_play_arrow)),
            LessonWindow("13", 13, "", "Switch, case and default", listOf(R.drawable.image_study, R.drawable.image_c, R.drawable.baseline_play_arrow))
        )
    }

    private fun loadJsonFromAssets(filePath: String): String{
        return context.assets.open(filePath).bufferedReader().use { it.readText() }
    }
}