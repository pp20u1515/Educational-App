package com.example.programmingc.presentation.ui.lessons.quiz

sealed class QuizNavigationEvent {
    data class NavigateToSingleChoice(val lessonId: String, val questionIndex: Int) : QuizNavigationEvent()
    data class NavigateToMultipleChoice(val lessonId: String, val questionIndex: Int) : QuizNavigationEvent()
    data class NavigateToInputChoice(val lessonId: String, val questionIndex: Int) : QuizNavigationEvent()
    data class NavigateToResults(val score: Int, val totalQuestions: Int) : QuizNavigationEvent()
    data class ShowResults(val score: Int, val totalQuestions: Int) : QuizNavigationEvent()
}