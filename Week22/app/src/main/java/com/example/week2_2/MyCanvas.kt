package com.example.week2_2;

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class MyCanvas : View {
    private val paint = Paint()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    // 레이아웃에서 사용하려면 이 생성자가 반드시 있어야 함
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // draw a line
//        paint.color = Color.RED
//        paint.strokeWidth = 5F
//        canvas.drawLine(100F, 100F, 500F, 200F, paint)
    }
}
