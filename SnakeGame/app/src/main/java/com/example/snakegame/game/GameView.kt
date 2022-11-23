package com.example.snakegame.game

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Handler
import android.os.Message
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.snakegame.R

class GameView(context: Context) : View(context) {

    private val paint: Paint = Paint()
    private val displayMetrics: DisplayMetrics = DisplayMetrics()
    private var screenHeight: Int
    private var screenWidth: Int
    private var screenGameTop = 0F

    private val snake: Snake
    private val snakeBody: Bitmap

    init {
        (context.getSystemService(  AppCompatActivity.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(displayMetrics)
        screenHeight = displayMetrics.heightPixels
        screenWidth= displayMetrics.widthPixels
        snake = Snake(600F, 200F, screenWidth/16, screenHeight/32, Direction.UP, 1)
        snakeBody = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.sqare),
            snake.width,
            snake.height,
            true
        )
        SnakeThread(snake).start()



        val mHandler: Handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                invalidate()
                sendEmptyMessageDelayed(0, 10)
            }
        }

        mHandler.sendEmptyMessageDelayed(0, 10)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0F, 0F, screenWidth.toFloat(), screenGameTop, paint)
        canvas.drawBitmap(snakeBody, snake.x, snake.y, null)
    }

    inner class SnakeThread(private val snake: Snake): Thread() {

        override fun run() {
            super.run()

            while(true) {
                snake.x += 10
                sleep(10)
                when (snake.direction) {
                    Direction.UP -> snake.y--
                    Direction.DOWN -> snake.y++
                    Direction.LEFT -> snake.x--
                    Direction.RIGHT -> snake.x++
                }

//                if (snake.x < snake.width / 2) {                           // 왼쪽 벽
//                    snake.x = snake.width / 2.toFloat()
//                    snake.speedX = -snake.speedX
//                } else if (snake.x > screenWidth - snake.width / 2) {         // 오른쪽 벽
//                    snake.x = (screenWidth - snake.width / 2).toFloat()
//                    snake.speedX = -snake.speedX
//                } else if (snake.y < screenGameTop ) {                    // 천장
//                    snake.y = screenGameTop
//                    snake.speedY = -snake.speedY
//                } else if (snake.y > screenHeight - snake.height){ // 바닥
//                    snake.y = screenHeight.toFloat() - 100f - snake.height/2
//                    snake.speedY = -snake.speedY
//                }
                sleep(10)
            }
        }
    }
}