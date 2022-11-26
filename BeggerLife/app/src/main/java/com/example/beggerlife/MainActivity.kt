package com.example.beggerlife

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.beggerlife.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var coin: Int = 0
    private var coinPerClick: Int = 100
    private var coinPerSec: Int = 1

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        coin = MyApplication.preferenceUtil.getString("coin", "0").toInt()
        coinPerClick = MyApplication.preferenceUtil.getString("coinPerClick", "100").toInt()
        coinPerSec = MyApplication.preferenceUtil.getString("coinPerSec", "1").toInt()

        binding.clickBtn.setOnClickListener {
            Log.d("MAIN_TAG", "click")
            coin += coinPerClick
            binding.coin.text = "${coin}G"
        }

        binding.resetBtn.setOnClickListener {
            coin = 0
            coinPerClick = 100
            coinPerSec = 0
        }

        GameThread().start()
    }

    override fun onPause() {
        super.onPause()

        MyApplication.preferenceUtil.setString("coin", coin.toString())
        MyApplication.preferenceUtil.setString("coinPerClick", coinPerClick.toString())
        MyApplication.preferenceUtil.setString("coinPerSec", coinPerSec.toString())
    }

    inner class GameThread : Thread() {
        override fun run() {
            super.run()
            while (true) {
                coin += coinPerSec
                runOnUiThread {
                    binding.coin.text = "${coin}G"
                }
                sleep(1000)
            }
        }
    }

}