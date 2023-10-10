package com.example.servicelab

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.Manifest
import android.os.Binder
import androidx.annotation.RequiresApi
import kotlinx.coroutines.*


class MyService : Service() {

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService() = this@MyService
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    var tickCount: Int = 0
        private set

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(notificationID, createNotification())
        if (intent != null) {
            tickCount = intent.getIntExtra("init", 0)
        }
        println(tickCount)
        CoroutineScope(Dispatchers.IO + Job()).apply { // 코루틴 범위 생성
            launch { // 코루틴 생성
                for (i in 1..10) { // 알림을 업데이트
                    tickCount++
                    delay(1000) // 1초 멈춤
                }
                stopSelf(startId) // 서비스를 종료함. 서비스 사용하는 다른 컴포넌트가 없다면 onDestroy 됨
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private val channelID = "default"
    private val notificationID = 1

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(channelID,
            "default channel",
            NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = "description text of this channel."
        NotificationManagerCompat.from(this).createNotificationChannel(channel)
    }

    private fun updateNotification(id: Int, notification: Notification) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        )
            NotificationManagerCompat.from(this).notify(id, notification)
    }

    private fun createNotification(progress: Int = 0) = NotificationCompat.Builder(this, channelID)
        .setContentTitle("Downloading")
        .setContentText("Downloading a file from a cloud")
        .setSmallIcon(R.drawable.ic_android_black_24dp)
        .setOnlyAlertOnce(true) // importance 에 따라 알림 소리가 날 때, 처음에만 소리나게 함
        .setProgress(100, progress, false)
        .build()

}