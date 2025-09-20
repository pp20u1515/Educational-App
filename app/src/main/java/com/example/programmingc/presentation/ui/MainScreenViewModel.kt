package com.example.programmingc.presentation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programmingc.domain.model.LessonWindow
import com.example.programmingc.domain.usecase.ShowLessonsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val showLessonsUseCase: ShowLessonsUseCase
): ViewModel() {
    private val _lessons = MutableLiveData<List<LessonWindow>>()
    val lessons: LiveData<List<LessonWindow>> = _lessons

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun loadLessons(){
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val lessonsList = showLessonsUseCase.invoke()
                _lessons.value = lessonsList
            } catch (e: Exception){
                _errorMessage.value = "Error in loading lectures"
            }
        }
    }

    fun clearError(){
        _errorMessage.value = null
    }
}