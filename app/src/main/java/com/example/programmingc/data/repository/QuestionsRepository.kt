package com.example.programmingc.data.repository

import android.content.Context
import com.example.programmingc.domain.model.QuestionType
import com.example.programmingc.domain.model.QuizQuestion
import com.example.programmingc.domain.repo.IQuestionsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import javax.inject.Inject

class QuestionsRepository @Inject constructor(
    @ApplicationContext private val context: Context
): IQuestionsRepository {
    override suspend fun getAvailableLectureIds(): List<Int> {
        return withContext(Dispatchers.IO){
            try {
                val files = context.assets.list("questions") ?: emptyArray()
                files.mapNotNull { fileName ->
                    if (fileName.startsWith("lecture_")){
                        fileName.removePrefix("lecture_").toIntOrNull()
                    }
                    else{
                        null
                    }
                }.sorted()
            }catch (e: Exception){
                emptyList()
            }
        }
    }
    override suspend fun getQuestionsForLecture(lectureId: Int): List<QuizQuestion>? {
        return withContext(Dispatchers.IO){
            try {
                val fileName = "questions/lecture_${lectureId}/questions.json"
                val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }

                parseQuestionsJson(jsonString)
            }
            catch (e: Exception){
                emptyList() // Error loading questions
            }
        }
    }

    private fun parseQuestionsJson(jsonString: String): List<QuizQuestion>? {
        return try {
            val jsonArray = JSONArray(jsonString)

            (0 until jsonArray.length()).map { index ->
                val jsonObject = jsonArray.getJSONObject(index)
                val options = parseOptions(jsonObject.getJSONArray("options"))

                // Парсим тип вопроса с значением по умолчанию
                val questionType = parseQuestionType(jsonObject.optString("questionType", "SINGLE_CHOICE"))

                val result = QuizQuestion(
                    id = jsonObject.getInt("id").toString(),
                    question = jsonObject.getString("questionText"),
                    options = options,
                    correctAnswers = parseCorrectAnswers(jsonObject.getJSONArray("correctAnswers")),
                    questionType = questionType
                )
                result
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun parseOptions(optionsArray: JSONArray): List<String>{
        val options = mutableListOf<String>()

        for (i in 0 until optionsArray.length()){
            options.add(optionsArray.getString(i))
        }

        return options
    }

    private fun parseCorrectAnswers(answersArray: JSONArray): List<String>{
        val answers = mutableListOf<String>()

        for (i in 0 until answersArray.length()){
            val value = answersArray.get(i)
            answers.add(value.toString())
        }

        return answers
    }

    private fun parseQuestionType(typeString: String): QuestionType {
        return when (typeString.uppercase()) {
            "SINGLE_CHOICE" -> QuestionType.SINGLE_CHOICE
            "MULTIPLE_CHOICE" -> QuestionType.MULTIPLE_CHOICE
            "INPUT_CHOICE" -> QuestionType.INPUT_CHOICE
            else -> {
                // Значение по умолчанию, если тип не распознан
                QuestionType.SINGLE_CHOICE
            }
        }
    }
}