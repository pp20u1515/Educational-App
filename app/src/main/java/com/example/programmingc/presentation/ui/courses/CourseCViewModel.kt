package com.example.programmingc.presentation.ui.courses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programmingc.domain.model.LessonWindow
import com.example.programmingc.domain.usecase.ShowLessonsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseCViewModel @Inject constructor(
    private val showLessonsUseCase: ShowLessonsUseCase
): ViewModel() {
    private val _lessons = MutableSharedFlow<List<LessonWindow>>(replay = 1)
    val lessons: SharedFlow<List<LessonWindow>> = _lessons.asSharedFlow()

    private val _errorMessage = MutableSharedFlow<String?>()
    val errorMessage: SharedFlow<String?> = _errorMessage.asSharedFlow()

    fun loadLessons(){
        viewModelScope.launch {
            _errorMessage.emit(null)
            try {
                val lessonsList = showLessonsUseCase.invoke()
                _lessons.emit(lessonsList)
            } catch (e: Exception){
                _errorMessage.emit("Error in loading lectures")
            }
        }
    }

    suspend fun clearError(){
        _errorMessage.emit(null)
    }
}