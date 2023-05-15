package com.example.uitest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.uitest.databinding.ActivityMainBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var number = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.upButton.setOnLongClickListener{
//            binding.number2.text
//        }
    }
}