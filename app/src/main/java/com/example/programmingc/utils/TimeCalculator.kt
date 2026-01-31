package com.example.programmingc.utils

import java.util.Calendar
import javax.inject.Inject

class TimeCalculator @Inject constructor(){
    fun calculateTimeUntilMidnight(): Long {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_YEAR, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.timeInMillis - currentTime
    }
}