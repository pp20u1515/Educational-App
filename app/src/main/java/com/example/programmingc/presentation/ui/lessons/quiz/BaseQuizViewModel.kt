package com.example.programmingc.presentation.ui.lessons.quiz

import androidx.lifecycle.ViewModel
import com.example.programmingc.domain.model.QuizQuestion

abstract class BaseQuizViewModel: ViewModel() {
    abstract fun loadQuestionsForLecture(lectureId: Int, questionIndex: Int)
    abstract fun goToNextQuestion(lessonId: String)
    abstract fun getCurrentQuestion(): QuizQuestion?
    abstract fun getTotalQuestions(): Int
    abstract fun getCurrentQuestionIndex(): Int

    abstract fun validateQuizAnswer()
}