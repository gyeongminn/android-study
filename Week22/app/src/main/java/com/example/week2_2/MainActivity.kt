package com.example.week2_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioGroup

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myView: MyView = findViewById(R.id.view)
        val radioGroup: RadioGroup = findViewById(R.id.radioGroup)
        myView.setRadioGroup(radioGroup)
    }
}