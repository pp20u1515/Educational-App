package com.example.programmingc.presentation.ui.objects.visiable_objects

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.programmingc.domain.model.Lesson
import com.example.programmingc.domain.usecase.CompleteLessonUseCase
import com.example.programmingc.domain.usecase.GetLessonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonViewModel @Inject constructor(
    private val getLessonUseCase: GetLessonUseCase,
    private val completeLessonUseCase: CompleteLessonUseCase
): ViewModel() {

    private val _currentLesson = MutableStateFlow<Lesson?>(null)
    val currentLesson: LiveData<Lesson?> = _currentLesson.asLiveData()

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

    fun completeLesson(lessonId: String){
        viewModelScope.launch {
            completeLessonUseCase(lessonId)
        }
    }
}