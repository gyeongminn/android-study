package com.example.week2_2;

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup

class MyView : View {
    private var rect = Rect(10, 10, 110, 110)
    private var cx = 10F
    private var cy = 10F
    private var color = Color.BLUE
    private var paint = Paint()
    private lateinit var radioGroup: RadioGroup

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun setRadioGroup(radioGroup: RadioGroup) {
        this.radioGroup = radioGroup
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = color
        if (radioGroup.checkedRadioButtonId == R.id.radioRect)
            canvas.drawRect(rect, paint)
        else
            canvas.drawCircle(cx, cy, 50F, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN ||
            event.action == MotionEvent.ACTION_MOVE
        ) {
            cx = event.x
            cy = event.y
            rect.left = event.x.toInt()
            rect.top = event.y.toInt()
            rect.right = rect.left + 100
            rect.bottom = rect.top + 100
            invalidate()
            performClick()
            return true
        }
        return super.onTouchEvent(event)
    }
}