package com.example.assignment3

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.core.view.setMargins
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.schedule


class MainActivity : AppCompatActivity(), View.OnClickListener {
    var dpRatio: Float =0f
    public val images: HashMap<Int,String> = HashMap<Int,String>()
    public val sounds: HashMap<Int,String> = HashMap<Int,String>()
    val pressedButtons : ArrayList<ImageButton> = arrayListOf()
    lateinit var keys  : MutableList<Int>
    lateinit var params :TableRow.LayoutParams
    var width : Int =0
    var removedButtons : Int =0
    var semaphore: Boolean= false
    var gameStarted: Boolean= false
    var counter = 0
    var highScore = Int.MAX_VALUE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        setContentView(R.layout.activity_main)
        dpRatio = this.getResources().getDisplayMetrics().density

        images.put(R.drawable.sanic, "sanic")
        images.put(R.drawable.shadow, "shadow")
        images.put(R.drawable.mario, "mario")
        images.put(R.drawable.dog, "dog")
        images.put(R.drawable.lion, "lion")
        images.put(R.drawable.eminem, "eminem")
        sounds.put(R.raw.eminem, "eminem")
        sounds.put(R.raw.shadow, "shadow")
        sounds.put(R.raw.mario, "mario")
        sounds.put(R.raw.sanic, "sanic")
        sounds.put(R.raw.lion, "lion")
        sounds.put(R.raw.dog, "dog")
        keys = ArrayList(images.keys)
        keys.addAll(images.keys)
        println(keys)
        buttonStart.setOnClickListener {
            if (!gameStarted) {
                counter == 0

                gameStarted = true
                val handler = Handler()
                handler.postDelayed(object : Runnable {
                    override fun run() {
                        counter++
                        println(counter)
                        handler.postDelayed(this, 1000)//1 sec delay
                    }
                }, 0)
            } else {
                finish();
                startActivity(getIntent());
            }
        }
        buttonRestart.setOnClickListener {
            counter == 0
            finish();
            startActivity(getIntent());
            gameStarted = true
            val handler = Handler()
            handler.postDelayed(object : Runnable {
                override fun run() {
                    counter++
                    println(counter)
                    handler.postDelayed(this, 1000)//1 sec delay
                }
            }, 0)
        }
        buttonScore.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val sharedPref = this?.getPreferences(Context.MODE_PRIVATE)
            highScore = sharedPref.getInt(getString(R.string.saved_high_score_key), 0)

            builder.setTitle("High Score")
            builder.setMessage("Your Score is : " + highScore)

//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

            builder.setNeutralButton("Okay") { dialog, which ->}
            builder.show()
        }
    }


    override fun onStart() {
        super.onStart()
        tablerow1.post(Runnable(){
            tablerow1.removeAllViewsInLayout()
            tablerow2.removeAllViewsInLayout()
            tablerow3.removeAllViewsInLayout()
            tablerow1.minimumHeight = tablerow1.measuredHeight
            tablerow2.minimumHeight = tablerow1.measuredHeight
            tablerow3.minimumHeight = tablerow1.measuredHeight
            //println("size is : " + tablerow1.measuredWidth)
            //button.setBackgroundResource(R.drawable.shadow)

            params = TableRow.LayoutParams(((tablerow1.measuredWidth/4)).toInt(),TableRow.LayoutParams.FILL_PARENT ,1f )

            keys.shuffle()
            for (o  in 0..keys.size) {
                // Access keys/values in a random order
                when {
                    o<4 -> {
                        createButton(tablerow1,images.get(keys[o]).toString(),o)

                    }
                    o in 4..7 -> {
                        createButton(tablerow2,images.get(keys[o]).toString(),o)
                    }
                    o in 8..11 -> {
                        createButton(tablerow3,images.get(keys[o]).toString(),o)

                    }
                }

            }

        })
    }
    fun createButton(row : TableRow, id : String, pic : Int){
        //println(dpRatio)
        val buttonNew =  ImageButton(this)
        //tablerow1.weightSum = 4F

        buttonNew.setBackgroundColor(Color.parseColor("#ff00c771"))
        params.setMargins((5*dpRatio).toInt())
        buttonNew.setPadding(0,0,0,0)
        buttonNew.layoutParams = params
        buttonNew.tag = id
        buttonNew.setOnClickListener(this)
        println("Created button with tag " + id )
        row.addView(buttonNew)
        buttonNew.post(Runnable(){
            width = buttonNew.measuredWidth
            params = TableRow.LayoutParams(width,width ,1f )
            params.setMargins((5*dpRatio).toInt())
            buttonNew.layoutParams = params

        }
        )
    }



    override fun onClick(view: View?) {
        if(semaphore){
            println("semaphore still down")
            Toast.makeText(this@MainActivity, "Please wait till the sound stops playing.", Toast.LENGTH_SHORT).show();

            return
        }
        if(!gameStarted){
            Toast.makeText(this@MainActivity, "Please start the game first.", Toast.LENGTH_SHORT).show();

            return
        }

        var pressedButton = view as ImageButton
        pressedButtons.add(pressedButton)
        var buttonText = (view as ImageButton).tag.toString();
        println(images.filterValues { it == pressedButton.tag }.keys.first())
        var mediaPlayer: MediaPlayer? = MediaPlayer.create(this, sounds.filterValues { it ==pressedButton.tag }.keys.first())
        mediaPlayer?.setOnCompletionListener {
            semaphore = false
            println("mediaplayerdone")
            checkBois()
            mediaPlayer.release()
        }
        semaphore = true
        mediaPlayer?.start()
        params = TableRow.LayoutParams(width,width ,1f )
        params.setMargins((5*dpRatio).toInt())

        pressedButton.setImageResource(images.filterValues { it ==pressedButton.tag }.keys.first())
        pressedButton.scaleType = ImageView.ScaleType.FIT_XY
        pressedButton.layoutParams = params



        //Toast.makeText(this@MainActivity, buttonText, Toast.LENGTH_SHORT).show();
    }

    private fun removeButtons() {

        runOnUiThread {
            for (button in pressedButtons){
                button.setImageResource(android.R.color.transparent)
                button.setBackgroundColor(Color.parseColor("#0000c771"))
                button.setOnClickListener(null)
            }
            pressedButtons.clear()
            removedButtons++
            println("removed buttons : " +removedButtons)
            if(removedButtons==6){
                //won
                Toast.makeText(this@MainActivity, "3azama fash5 kesebt", Toast.LENGTH_SHORT).show();
                val builder = AlertDialog.Builder(this)
                builder.setTitle("You won!")
                builder.setMessage("Your Score is : " + counter)
                if(counter<highScore) {
                    highScore = counter
                    val sharedPref = this?.getPreferences(Context.MODE_PRIVATE)
                    with (sharedPref.edit()) {
                        putInt(getString(R.string.saved_high_score_key), highScore)
                        commit()
                    }                }

                builder.setNeutralButton("Okay") { dialog, which ->
                    Toast.makeText(applicationContext,
                        "Maybe", Toast.LENGTH_SHORT).show()
                }
                builder.show()
            }
        }
    }
    private fun clearButtons() {

        runOnUiThread {
            for (button in pressedButtons){
                button.setImageResource(android.R.color.transparent)
                button.setBackgroundColor(Color.parseColor("#ff00c771"))
            }
            pressedButtons.clear()        }
    }

    fun checkBois(){
        if(pressedButtons.size>1){
            if(pressedButtons[0].tag==pressedButtons[1].tag){
                Toast.makeText(this@MainActivity, "Gamed", Toast.LENGTH_SHORT).show();
                Timer("SettingUp", false).schedule(200) {
                    removeButtons()
                }
            }
            else{
                Timer("SettingUp", false).schedule(200) {
                    clearButtons()
                    semaphore=false
                }
            }
        }
    }

}
