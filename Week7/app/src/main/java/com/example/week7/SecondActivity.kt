package com.example.week7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val msg = intent?.getStringExtra("data") ?: "0"

        val viewModel = ViewModelProvider(this, MyViewModel.MyViewModelFactory(msg.toInt()))[MyViewModel::class.java]

        viewModel.countLiveData.observe(this) {
            findViewById<TextView>(R.id.textView).text = "$it"
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val resultIntent = Intent()
                resultIntent.putExtra("resData", viewModel.countLiveData.value.toString())
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        })

        findViewById<Button>(R.id.buttonInc)?.setOnClickListener {
            viewModel.increaseCount()
        }

        findViewById<Button>(R.id.buttonDec)?.setOnClickListener {
            viewModel.decreaseCount()
        }
    }
}