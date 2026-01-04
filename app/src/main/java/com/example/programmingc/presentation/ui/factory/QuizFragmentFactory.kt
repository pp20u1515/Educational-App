package com.example.programmingc.presentation.ui.factory

import com.example.programmingc.R
import com.example.programmingc.domain.model.QuestionType
import com.example.programmingc.domain.model.QuizQuestion
import com.example.programmingc.presentation.ui.lessons.quiz.BaseQuizFragment
import com.example.programmingc.presentation.ui.lessons.quiz.FragmentQuizInputChoice
import com.example.programmingc.presentation.ui.lessons.quiz.FragmentQuizMultipleChoice
import com.example.programmingc.presentation.ui.lessons.quiz.FragmentQuizSingleChoice

object QuizFragmentFactory {
    private fun getActionForQuestion(question: QuizQuestion): Int {
        return when (question.questionType) {
            QuestionType.SINGLE_CHOICE -> R.id.action_fragment_lesson_to_fragment_quiz_single_choice
            QuestionType.MULTIPLE_CHOICE -> R.id.action_fragment_lesson_to_fragment_quiz_multiple_choice
            QuestionType.INPUT_CHOICE -> R.id.action_fragment_lesson_to_fragment_quiz_input_choice
        }
    }
    fun getActionForFirstQuestion(questions: List<QuizQuestion>, lectureId: String): Int{
        if (questions.isEmpty()){
            throw IllegalArgumentException("Questions list cannot be empty")
        }
        return getActionForQuestion(questions[0])
    }
}