package com.example.programmingc.presentation.ui.lessons.quiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.programmingc.domain.model.QuizQuestion
import com.example.programmingc.presentation.ui.menu.BaseMenuBar

abstract class BaseQuizFragment: BaseMenuBar() {
    abstract fun setupQuestion(question: QuizQuestion)
    abstract fun observeViewModel()
    abstract fun getViewModel(): BaseQuizViewModel

    companion object{
        private const val LECTURE_ID_ARG = "lessonId"
        private const val QUESTION_INDEX_ARG = "questionIndex"

        fun createBundle(lectureId: String, questionIndex: Int = 0): Bundle{
            return Bundle().apply{
                putString(LECTURE_ID_ARG, lectureId)
                putInt(QUESTION_INDEX_ARG, questionIndex)
            }
        }

        fun getLectureIdFromArgs(arguments: Bundle?): String?{
            return arguments?.getString(LECTURE_ID_ARG) ?: null
        }

        fun getQuestionIndexFromArgs(arguments: Bundle?): Int{
            return arguments?.getInt(QUESTION_INDEX_ARG) ?: 0
        }
    }
}