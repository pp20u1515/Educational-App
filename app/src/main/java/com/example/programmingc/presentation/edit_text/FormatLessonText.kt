package com.example.programmingc.presentation.edit_text

import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan

fun parseContentWithCodeBlocks(content: String): List<Any> {
    val result = mutableListOf<Any>()
    val lines = content.split("\n")
    var inCodeBlock = false
    var codeBuffer = StringBuilder()
    var textBuffer = StringBuilder()

    for (line in lines) {
        val trimmedLine = line.trim()

        when {
            // Начало блока кода (отдельная строка только с "c")
            !inCodeBlock && trimmedLine == "c" && line == "c" -> {
                if (textBuffer.isNotEmpty()) {
                    result.add(TextContent(formatTextWithBold(textBuffer.toString())))
                    textBuffer.clear()
                }
                inCodeBlock = true
            }

            // Конец блока кода (отдельная строка только с "c")
            inCodeBlock && trimmedLine == "c" && line == "c" -> {
                if (codeBuffer.isNotEmpty()) {
                    result.add(CodeContent(codeBuffer.toString()))
                    codeBuffer.clear()
                }
                inCodeBlock = false
            }

            // Внутри блока кода
            inCodeBlock -> {
                codeBuffer.append(line).append("\n")
            }

            // Обычный текст
            else -> {
                textBuffer.append(line).append("\n")
            }
        }
    }

    // Добавляем остатки
    if (textBuffer.isNotEmpty()) {
        result.add(TextContent(formatTextWithBold(textBuffer.toString())))
    }
    if (codeBuffer.isNotEmpty()) {
        result.add(CodeContent(codeBuffer.toString()))
    }

    return result
}

// Классы для разных типов контента
sealed class Content
data class TextContent(val text: SpannableStringBuilder) : Content()
data class CodeContent(val code: String) : Content()

private fun formatTextWithBold(text: String): SpannableStringBuilder {
    val builder = SpannableStringBuilder()
    val parts = text.split("\\bold") // Экранируем обратный слэш

    for (i in parts.indices) {
        if (i == 0) {
            // Первая часть всегда обычный текст
            val normalText = SpannableString(parts[i])
            normalText.setSpan(
                ForegroundColorSpan(Color.parseColor("#000000")), // Темно-серый
                0,
                normalText.length,
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            builder.append(normalText)
        } else {
            // Находим конец заголовка (первый двойной перевод строки)
            val part = parts[i]
            val headerEndIndex = part.indexOf("\n\n")

            if (headerEndIndex != -1) {
                // Выделяем заголовок (жирный)
                val headerText = part.substring(0, headerEndIndex)
                val boldText = SpannableString(headerText)
                boldText.setSpan(
                    StyleSpan(Typeface.BOLD),
                    0,
                    headerText.length,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                boldText.setSpan(
                    ForegroundColorSpan(Color.parseColor("#000000")), // Еще темнее
                    0,
                    headerText.length,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                builder.append(boldText)

                // Добавляем оставшийся текст (обычный)
                val normalText = part.substring(headerEndIndex)
                val normalSpannable = SpannableString(normalText)
                normalSpannable.setSpan(
                    ForegroundColorSpan(Color.parseColor("#333333")),
                    0,
                    normalText.length,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                builder.append(normalSpannable)
            } else {
                // Если нет двойного перевода строки, весь текст жирный
                val boldText = SpannableString(part)
                boldText.setSpan(
                    StyleSpan(Typeface.BOLD),
                    0,
                    part.length,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                boldText.setSpan(
                    ForegroundColorSpan(Color.parseColor("#1a1a1a")),
                    0,
                    part.length,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                builder.append(boldText)
            }
        }
    }

    return builder
}