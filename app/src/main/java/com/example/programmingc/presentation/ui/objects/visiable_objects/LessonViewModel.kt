package com.example.programmingc.presentation.ui.objects.visiable_objects

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.programmingc.domain.model.Lesson
import com.example.programmingc.domain.model.QuizQuestion
import com.example.programmingc.domain.usecase.CompleteLessonUseCase
import com.example.programmingc.domain.usecase.GetLessonUseCase
import com.example.programmingc.domain.usecase.GetQuestionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonViewModel @Inject constructor(
    private val getLessonUseCase: GetLessonUseCase,
    private val getQuestionsUseCase: GetQuestionsUseCase
): ViewModel() {

    private val _currentLesson = MutableLiveData<Lesson?>(null)
    val currentLesson: LiveData<Lesson?> = _currentLesson

    private val _questions = MutableLiveData<List<QuizQuestion>?>()
    val questions: LiveData<List<QuizQuestion>?> = _questions

    fun loadLesson(lessonId: String){
        viewModelScope.launch {
            try {
                val lesson = getLessonUseCase(lessonId)
                _currentLesson.value = lesson
            }
            catch (e: Exception){
                // Обработка ошибок
            }
        }
    }

    fun loadQuestionsForLesson(lessonId: String){

        viewModelScope.launch {
            try {
                val questionsList = getQuestionsUseCase(lessonId.toInt())

                _questions.value = questionsList
            } catch (e: Exception) {
                // Обработка ошибок
                //println("\n\nerror_lessonId = $lessonId\n")
            }
        }
    }
}