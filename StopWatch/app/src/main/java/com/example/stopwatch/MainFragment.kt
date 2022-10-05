package com.example.stopwatch

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.preference.PreferenceManager
import java.text.DecimalFormat
import java.util.*

class MainFragment : Fragment() {

    private lateinit var startAndStopBtn: Button // 시작, 멈춤 버튼
    private lateinit var recordAndResetBtn: Button // 기록, 초기화 버튼
    private lateinit var popupMenu: ImageButton // 팝업 메뉴 버튼
    private var timerTask: Timer? = null

    private var isRunning: Boolean = false // 현재 상태
    private var isRecorded: Boolean = false // 기록이 남아있는지

    private var time = 0 // 현재 시간
    private var sectionTime = 0 // 현재 시간

    private var recordIndex: Int = 0
    private lateinit var timerText: TextView

    private lateinit var recordList: LinearLayout
    private lateinit var recordListText: LinearLayout
    private lateinit var recordListBar: ImageView

    private val mainTag = "LifeCycle[main]"

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(mainTag, "onCreateView")
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(mainTag, "onViewCreated")

        startAndStopBtn = view.findViewById(R.id.startAndStopBtn)
        recordAndResetBtn = view.findViewById(R.id.recordAndResetBtn)
        timerText = view.findViewById(R.id.timerText)
        recordList = view.findViewById(R.id.recordList)
        popupMenu = view.findViewById(R.id.popupMenu)

        // 기록 없을땐 숨기기
        recordListText = view.findViewById(R.id.recordListText)
        recordListText.visibility = View.INVISIBLE
        recordListBar = view.findViewById(R.id.recordListBar)
        recordListBar.visibility = View.INVISIBLE

        // 시작, 중지 이벤트
        startAndStopBtn.setOnClickListener {
            if (!isRunning) { // 시작
                isRunning = true
                isRecorded = true
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
    }

    private fun timeTxt(): String {
        val minute = DecimalFormat("00").format(time / 100 / 60)
        val second = DecimalFormat("00").format(time / 100)
        val millisecond = DecimalFormat("00").format(time % 100)
        return "$minute:$second.$millisecond"
    }

    private fun sectionTimeTxt(): String {
        val minute = DecimalFormat("00").format(sectionTime / 100 / 60)
        val second = DecimalFormat("00").format(sectionTime / 100)
        val millisecond = DecimalFormat("00").format(sectionTime % 100)
        return "$minute:$second.$millisecond"
    }

    private fun recordIndexText(): String {
        return DecimalFormat("00").format(recordIndex)
    }

    private fun recordTxt(): String {
        return "   ${recordIndexText()}                  ${sectionTimeTxt()}               ${timeTxt()}"
    }

    private fun Fragment?.runOnUiThread(action: () -> Unit) {
        this ?: return
        if (!isAdded) return
        activity?.runOnUiThread(action)
    }

    private fun loadSettings(): Boolean {
        //val sp = PreferenceManager.getDefaultSharedPreferences(context)
        //val backgroundOpt:Boolean = sp.getBoolean("background", false)
        //return sp.getBoolean("background", false)
        return false
    }

    @SuppressLint("SetTextI18n")
    private fun start() {
        recordAndResetBtn.text = "구간 기록"
        startAndStopBtn.text = "중지"
        startAndStopBtn.setBackgroundResource(R.drawable.round_button_stop)
        recordAndResetBtn.isEnabled = true
        //반복주기는 period 프로퍼티로 설정, 단위는 1000분의 1초 (period = 1000, 1초)
        timerTask = kotlin.concurrent.timer(period = 10) {
            time++ // 0.01초마다 time 을 1씩 증가 (period=10)
            runOnUiThread { timerText.text = timeTxt() }
        }
    }

    private fun pause() {
        recordAndResetBtn.text = "초기화"
        startAndStopBtn.text = "계속"
        isRecorded = true
        startAndStopBtn.setBackgroundResource(R.drawable.round_button_start)
        timerTask?.cancel()
    }

    private fun reset() {
        recordListText.visibility = View.INVISIBLE
        recordListBar.visibility = View.INVISIBLE

        recordAndResetBtn.text = "구간 기록"
        recordAndResetBtn.isEnabled = false
        timerText.text = "00:00.00"
        recordIndex = 0
        sectionTime = 0
        time = 0
        recordList.removeAllViews()
    }

    private fun record() {
        recordListText.visibility = View.VISIBLE
        recordListBar.visibility = View.VISIBLE

        sectionTime = time - sectionTime
        recordIndex++

        val textViewNm = TextView(requireContext().applicationContext)
        textViewNm.text = recordTxt()
        textViewNm.textSize = 16f
        textViewNm.id = 0
        val param: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        param.marginStart = 10
        param.topMargin = 50
        textViewNm.layoutParams = param
        recordList.addView(textViewNm)
    }
}