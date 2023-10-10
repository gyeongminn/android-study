package com.example.week12

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.preference.PreferenceManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val tv = findViewById<TextView>(R.id.textView)

        tv.text = pref.getString("Name", "Hello World!")

        when (pref.getString("Size", "small")) {
            "big" ->
                tv.textSize = 20F
            "medium" ->
                tv.textSize = 14F
            else ->
                tv.textSize = 10F
        }

        if (pref.getBoolean("Italic", false)) {
            tv.setTypeface(null, Typeface.ITALIC)
        }
    }
}