package com.example.programmingc.presentation.model

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.style.ReplacementSpan
class RoundedBackgroundSpan(
    private val backgroundColor: Int,
    private val cornerRadius: Float
) : ReplacementSpan() {

    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        // Возвращаем ширину родителя (MATCH_PARENT)
        // Для этого нужно знать ширину родительского View
        // Вместо этого будем использовать другой подход
        return (paint.measureText(text, start, end) + 64).toInt()
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        val textWidth = paint.measureText(text, start, end)
        val textHeight = paint.descent() - paint.ascent()

        // Получаем ширину родительского View
        val parentWidth = canvas.width.toFloat()

        val rect = RectF(
            x,
            top.toFloat(),
            parentWidth, // Занимаем всю ширину родителя
            top + textHeight + 32
        )

        val backgroundPaint = Paint(paint)
        backgroundPaint.color = backgroundColor
        backgroundPaint.style = Paint.Style.FILL
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, backgroundPaint)

        val textPaint = Paint(paint)
        textPaint.color = Color.BLACK
        canvas.drawText(text, start, end, x + 32, y.toFloat(), textPaint)
    }
}
