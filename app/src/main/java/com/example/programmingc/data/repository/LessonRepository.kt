package com.example.programmingc.data.repository

import com.example.programmingc.domain.model.Practice
import com.example.programmingc.domain.repo.ILessonRepository
import com.example.programmingc.presentation.ui.objects.visiable_objects.LessonFragment
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import com.example.programmingc.domain.model.Lesson
import javax.inject.Inject

class LessonRepository @Inject constructor(
    @ApplicationContext private val context: Context
): ILessonRepository {
    private val gson = Gson()

    override suspend fun getLessonById(id: String): Lesson {
        val json = loadJsonFromAssets("lectures/lecture_$id.json")

        return gson.fromJson(json, Lesson::class.java)
    }

    override suspend fun getPracticeByLessonId(lessonId: String): Practice? {
        TODO("Not yet implemented")
    }

    override suspend fun markLessonCompleted(lessonId: String) {
        TODO("Not yet implemented")
    }

    private fun loadJsonFromAssets(filePath: String): String{
        return context.assets.open(filePath).bufferedReader().use { it.readText() }
    }
}