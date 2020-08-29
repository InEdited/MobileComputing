package com.example.ahmed_alaa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.*
import kotlinx.android.synthetic.main.activity_main.*

const val EXTRA_MESSAGE = "3ala.allah"
class MainActivity : AppCompatActivity() {
    var radioText: String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setDefaultNightMode(MODE_NIGHT_YES)
        radioButtonGroup.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val radio: RadioButton = findViewById(checkedId)
                radioText = radio.text as String

            })
        button.setOnClickListener{
            if(radioText==""){
                Toast.makeText(applicationContext,"Please Choose an answer!",
                    Toast.LENGTH_SHORT).show()
            }else{
                println(radioText)
                if(radioText.contains("Nougat")){
                    println("e7na ahoooo")
                    sendMessage(true)
                }else {
                    sendMessage(false)
                }
            }
        }
    }

    fun sendMessage(answer:Boolean) {
        val intent = Intent(this, ResultActivity::class.java).apply { putExtra("answer",answer) }
        startActivity(intent)
    }
}