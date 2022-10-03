package com.example.stopwatch

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.text.DecimalFormat
import java.util.Timer

class MainActivity : AppCompatActivity() {

    lateinit var startAndStopBtn: Button // 시작, 멈춤 버튼
    lateinit var recordAndResetBtn: Button // 기록, 초기화 버튼
    private var timerTask: Timer? = null

    private var isRunning: Boolean = false // 현재 상태
    private var isRecorded: Boolean = false // 기록이 남아있는지

    private var time = 0 // 현재 시간
    private var pauseTime = 0L // 멈췄을때 시간

    private var index: Int = 0
    private lateinit var timerText: TextView

    // 참고 : https://stickode.tistory.com/130

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startAndStopBtn = findViewById(R.id.startAndStopBtn)
        recordAndResetBtn = findViewById(R.id.recordAndResetBtn)
        timerText = findViewById(R.id.timerText)


        // 시작, 중지 이벤트
        startAndStopBtn.setOnClickListener() {
            if (!isRunning) { // 시작
                isRunning = true
                start()
            } else {  // 중지
                isRunning = false
                pause()
            }
        }

        // 기록, 초기화 이벤트
        recordAndResetBtn.setOnClickListener() {
            if (!isRecorded) {
                recordAndResetBtn.isEnabled = false
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun start() {
        recordAndResetBtn.text = "초기화"
        startAndStopBtn.text = "중지"
        startAndStopBtn.setBackgroundResource(R.drawable.round_button_stop)

        timerTask =
            kotlin.concurrent.timer(period = 10) { //반복주기는 peroid 프로퍼티로 설정, 단위는 1000분의 1초 (period = 1000, 1초)
                time++ // 0.01초마다 time 을 1씩 증가 (period=10)
                val minute = DecimalFormat("00").format(time / 100 / 60)
                val second = DecimalFormat("00").format(time / 100)
                val millisecond = DecimalFormat("00").format(time % 100)
                //val text :String = "$minute:$second.$millisecond"

                // UI 조작을 위한 메서드
                runOnUiThread {
                    timerText.text = "$minute:$second.$millisecond"
                }
            }
    }

    private fun pause() {
        recordAndResetBtn.text = "구간 기록"
        startAndStopBtn.text = "계속"
        startAndStopBtn.setBackgroundResource(R.drawable.round_button_start)
        timerTask?.cancel();
    }
}