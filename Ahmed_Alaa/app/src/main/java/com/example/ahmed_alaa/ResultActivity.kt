package com.example.ahmed_alaa

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        setDefaultNightMode(MODE_NIGHT_YES)
        val message = intent.getBooleanExtra("answer",true)
        println(message)
        if(message) {
            answerText.text = "Correct Answer"
            answerText.setBackgroundColor(Color.parseColor("#FF009688"))
        }else{
            answerText.text = "Wrong Answer"
            answerText.setBackgroundColor(Color.parseColor("#FFE91E63"))
        }

        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}