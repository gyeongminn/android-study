package com.example.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class BlankActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blank)

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        val fragment = SettingsFragment()
        transaction.replace(R.id.frame, fragment).addToBackStack(null).commit()
    }
}