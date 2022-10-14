package com.example.stopwatch

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import java.text.DecimalFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var startAndStopBtn: Button // 시작, 멈춤 버튼
    private lateinit var recordAndResetBtn: Button // 기록, 초기화 버튼
    private lateinit var popupMenu: ImageButton // 팝업 메뉴 버튼
    private var timerTask: Timer? = null

    private var isRunning: Boolean = false // 현재 상태

    private var startTime = 0
    private var pauseTime = 0
    private var time = 0
    private var sectionTime = 0

    private var recordIndex: Int = 0
    private lateinit var timerText: TextView

    private lateinit var recordList: LinearLayout
    private lateinit var recordListText: LinearLayout
    private lateinit var recordListBar: ImageView

    private val mainTag = "LifeCycle[main]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(mainTag, "onCreate()")

        startAndStopBtn = findViewById(R.id.startAndStopBtn)
        recordAndResetBtn = findViewById(R.id.recordAndResetBtn)
        timerText = findViewById(R.id.timerText)
        recordList = findViewById(R.id.recordList)

        popupMenu = findViewById(R.id.popupMenu)

        // 기록 없을땐 숨기기
        recordListText = findViewById(R.id.recordListText)
        recordListText.visibility = View.INVISIBLE
        recordListBar = findViewById(R.id.recordListBar)
        recordListBar.visibility = View.INVISIBLE

        // 시작, 중지 이벤트
        startAndStopBtn.setOnClickListener {
            if (!isRunning) { // 시작
                isRunning = true
                start()
            } else {  // 중지
                isRunning = false
                pause()
            }
            recordAndResetBtn.isEnabled = true
        }

        // 기록, 초기화 이벤트
        recordAndResetBtn.setOnClickListener {
            if (isRunning) { // 기록 이벤트
                record()
            } else { // 초기화 이벤트
                reset()
            }
        }

        // 팝업 메뉴
        popupMenu.setOnClickListener { it ->
            val popupMenu = PopupMenu(applicationContext, it)
            menuInflater.inflate(R.menu.popup, popupMenu.menu)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.settings -> {
                        Log.d(mainTag, "open SettingActivity")
                        openSettings();
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }
    }

    private fun openSettings() {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        val fragment = SettingsFragment()
//        https://todaycode.tistory.com/131 참조
        transaction.replace(R.id.frame, fragment).addToBackStack(null).commit()
    }

    override fun onStart() {
        super.onStart()
        Log.d(mainTag, "onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.d(mainTag, "onResume()")
    }

    override fun onPause() {
        if (loadSettings()) pause()
        super.onPause()
        Log.d(mainTag, "onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.d(mainTag, "onStop()")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(mainTag, "onRestart()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(mainTag, "onDestroy()")
    }

    private fun timeTxt(): String {
        val millisecond = DecimalFormat("00").format(time / 10 % 100)
        val second = DecimalFormat("00").format(time / 1000 % 60)
        val minute = DecimalFormat("00").format(time / 1000 / 60)
        return "$minute:$second.$millisecond"
    }

    private fun sectionTimeTxt(): String {
        val millisecond = DecimalFormat("00").format(sectionTime / 10 % 100)
        val second = DecimalFormat("00").format(sectionTime / 1000 % 60)
        val minute = DecimalFormat("00").format(sectionTime / 1000 / 60)
        return "$minute:$second.$millisecond"
    }

    private fun recordIndexText(): String {
        return DecimalFormat("00").format(recordIndex)
    }

    private fun recordTxt(): String {
        return "   ${recordIndexText()}                 ${sectionTimeTxt()}               ${timeTxt()}"
    }

    private fun loadSettings(): Boolean {
        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        return sp.getBoolean("background", false)
    }

    @SuppressLint("SetTextI18n")
    private fun start() {
        Log.d(mainTag, "StopWatch start")
        recordAndResetBtn.text = "구간 기록"
        startAndStopBtn.text = "중지"
        startAndStopBtn.setBackgroundResource(R.drawable.round_button_stop)
        recordAndResetBtn.isEnabled = true
        timerTask =
            kotlin.concurrent.timer(period = 10) {
                if (startTime == 0) startTime = SystemClock.uptimeMillis().toInt()
                //반복주기는 period 프로퍼티로 설정, 단위는 1000분의 1초 (period = 1000, 1초)
                time = SystemClock.uptimeMillis().toInt() - startTime + pauseTime
                runOnUiThread { timerText.text = timeTxt() }
            }
    }

    private fun pause() {
        Log.d(mainTag, "StopWatch pause")
        pauseTime = time
        startTime = SystemClock.uptimeMillis().toInt()
        recordAndResetBtn.text = "초기화"
        startAndStopBtn.text = "계속"
        startAndStopBtn.setBackgroundResource(R.drawable.round_button_start)
        timerTask?.cancel()
    }

    private fun reset() {
        Log.d(mainTag, "StopWatch reset")
        recordListText.visibility = View.INVISIBLE
        recordListBar.visibility = View.INVISIBLE

        recordAndResetBtn.text = "구간 기록"
        startAndStopBtn.text = "시작"
        recordAndResetBtn.isEnabled = false
        timerText.text = "00:00.00"
        time = 0
        pauseTime = 0
        sectionTime = 0
        startTime = SystemClock.uptimeMillis().toInt()
        recordIndex = 0
        recordList.removeAllViews()
    }

    private fun record() {
        Log.d(mainTag, "StopWatch record")
        sectionTime = time - sectionTime

        recordListText.visibility = View.VISIBLE
        recordListBar.visibility = View.VISIBLE
        recordIndex++

        val param: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        param.marginStart = 10
        param.topMargin = 50

        val textViewNm = TextView(applicationContext)
        textViewNm.textSize = 16f
        textViewNm.id = 0
        textViewNm.layoutParams = param

        textViewNm.text = recordTxt()
        recordList.addView(textViewNm)
        sectionTime = time
    }
}