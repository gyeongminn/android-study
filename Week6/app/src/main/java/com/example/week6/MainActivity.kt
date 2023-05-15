package com.example.week6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView)
        val button = findViewById<Button>(R.id.button)
        val radioDog = findViewById<RadioButton>(R.id.radioDog)
        val radioCat = findViewById<RadioButton>(R.id.radioCat)


        button.setOnClickListener {
            val pet = "Dog:${radioDog.isChecked}, Cat:${radioCat.isChecked}"
            Snackbar.make(it, pet, Snackbar.LENGTH_SHORT).show()
            var text = if (radioDog.isChecked) radioDog.text else radioCat.text
            textView.text = text
        }
    }
}