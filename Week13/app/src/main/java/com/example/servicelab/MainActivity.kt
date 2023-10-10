package com.example.servicelab

import android.Manifest
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.week13.R
import kotlinx.coroutines.*
import java.util.Random

class MainActivity : AppCompatActivity() {

    val textView by lazy { findViewById<TextView>(R.id.textView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestSinglePermission(Manifest.permission.POST_NOTIFICATIONS)
        }

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            startCoroutine()
        }

        val buttonService = findViewById<Button>(R.id.buttonService)
        buttonService.setOnClickListener {
            Intent(this, MyService::class.java).putExtra("init", Random().nextInt(100)).also {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // Android 8.0
                    startForegroundService(it) // must call startForground in a few seconds.
                } else {
                    startService(it)
                }
            }
        }

        val buttonGetCount = findViewById<Button>(R.id.buttonGet)
        buttonGetCount.setOnClickListener {
            textView.text = "${myService?.tickCount}"
        }
    }

    private var myService: MyService? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            myService = (service as MyService.LocalBinder).getService() // 바운드됨
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            myService = null
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, MyService::class.java).also {
            bindService(it,
                serviceConnection,
                BIND_AUTO_CREATE) // Bound 서비스 bind, 결과가 serviceConnection으로 전달
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(serviceConnection)
    }

    fun startCoroutine() {
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            for (i in 1..10) {
                delay(1000)
                withContext(Dispatchers.Main) {
                    textView.text = "$i"
                }
            }
        }
    }

    private fun requestSinglePermission(permission: String) { // 한번에 하나의 권한만 요청하는 예제
        if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) // 권한 유무 확인
            return
        val requestPermLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { // 권한 요청 컨트랙트
                if (it == false) { // permission is not granted!
                    AlertDialog.Builder(this).apply {
                        setTitle("Warning")
                        setMessage("no_permission")
                    }.show()
                }
            }
        if (shouldShowRequestPermissionRationale(permission)) { // 권한 설명 필수 여부 확인
            AlertDialog.Builder(this).apply {
                setTitle("Reason")
                setMessage("req_permission_reason")
                setPositiveButton("Allow") { _, _ -> requestPermLauncher.launch(permission) }
                setNegativeButton("Deny") { _, _ -> }
            }.show()
        } else {
            requestPermLauncher.launch(permission) // 권한 요청 시작
        }
    }
}