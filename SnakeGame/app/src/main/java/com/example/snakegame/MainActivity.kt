package com.example.snakegame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.snakegame.databinding.ActivityMainBinding
import com.example.snakegame.game.GameActivity

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // binding.startBtn.setOnClickListener {
        startActivity(Intent(this@MainActivity, GameActivity::class.java))
        //}
    }
}