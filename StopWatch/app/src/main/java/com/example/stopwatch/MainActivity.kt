package com.example.stopwatch

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit
import java.util.*

import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.Rotation
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Size
import nl.dionsegijn.konfetti.core.Spread


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

    private lateinit var viewKonfetti: KonfettiView
    private lateinit var easterEgg1: Button
    private lateinit var easterEgg2: Button

    private val mainTag = "LifeCycle[main]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(mainTag, "onCreate()")

        startAndStopBtn = findViewById(R.id.startAndStopBtn)
        recordAndResetBtn = findViewById(R.id.recordAndResetBtn)
        timerText = findViewById(R.id.timerText)
        recordList = findViewById(R.id.recordList)

        viewKonfetti = findViewById(R.id.konfettiView)
        popupMenu = findViewById(R.id.popupMenu)

        // 기록 없을땐 숨기기
        recordListText = findViewById(R.id.recordListText)
        recordListText.visibility = View.INVISIBLE
        recordListBar = findViewById(R.id.recordListBar)
        recordListBar.visibility = View.INVISIBLE

        easterEgg1 = findViewById(R.id.easterEgg1)
        easterEgg2 = findViewById(R.id.easterEgg2)

        easterEgg1.setOnClickListener {
            if (loadEasterEggMode()) {
                viewKonfetti.start(festive())
            }
        }
        
        easterEgg2.setOnClickListener {
            if (loadEasterEggMode()) {
                viewKonfetti.start(parade())
            }
        }

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
//        val fragment = SettingsFragment()
//        https://todaycode.tistory.com/131 참조
//        transaction.replace(R.id.frame, fragment).addToBackStack(null).commit()
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

    private fun loadEasterEggMode(): Boolean {
        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        return sp.getBoolean("easterEggSwitch", false)
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

    private fun festive(): List<Party> {
        val party = Party(
            speed = 30f,
            maxSpeed = 50f,
            damping = 0.9f,
            angle = Angle.TOP,
            spread = 45,
            size = listOf(Size.SMALL, Size.LARGE),
            timeToLive = 3000L,
            rotation = Rotation(),
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(30),
            position = Position.Relative(0.5, 1.0)
        )

        return listOf(
            party,
            party.copy(
                speed = 55f,
                maxSpeed = 65f,
                spread = 10,
                emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(10),
            ),
            party.copy(
                speed = 50f,
                maxSpeed = 60f,
                spread = 120,
                emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(40),
            ),
            party.copy(
                speed = 65f,
                maxSpeed = 80f,
                spread = 10,
                emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(10),
            )
        )
    }

    private fun parade(): List<Party> {
        val party = Party(
            speed = 10f,
            maxSpeed = 30f,
            damping = 0.9f,
            angle = Angle.RIGHT - 45,
            spread = Spread.SMALL,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            emitter = Emitter(duration = 5, TimeUnit.SECONDS).perSecond(30),
            position = Position.Relative(0.0, 0.5)
        )

        return listOf(
            party,
            party.copy(
                angle = party.angle - 90, // flip angle from right to left
                position = Position.Relative(1.0, 0.5)
            ),
        )
    }
}