package com.example.currencyconverter

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.view.View.generateViewId
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var fromValue: Float = 0.0f
    var toValue = 0.0f

    fun convertButtonPressed() {

        if(fromSpinner.selectedItem == toSpinner.selectedItem) {
            yoWtf("Please choose different currencies")
            return
        }

        if(editTextAmount.text.isEmpty()) {
            yoWtf("Please enter an amount to convert")
            return
        }

        try {
            fromValue = editTextAmount.text.toString().toFloat()
        } catch (_: NumberFormatException) {
            yoWtf("Please Enter A valid Number.")
            return
        }

        resultTextView.visibility = VISIBLE

        hideKeyboard(window.decorView.findViewById(android.R.id.content))//I have no idea why this
        //works but it works on my phone so idk

        if(fromSpinner.selectedItem.toString() == "EGP"
            && toSpinner.selectedItem.toString() == "USD"
        ) {
            toValue = fromValue * 15.9f
        } else if (fromSpinner.selectedItem.toString() == "USD"
            && toSpinner.selectedItem.toString() == "EGP"
        ) {
            toValue = fromValue / 15.9f
        } else {
            yoWtf("huh")
        }

        resultTextView.text = "%.2f".format(toValue)
        toValue = 0.0f

    }

    private fun hideKeyboard(view: View) {
        view?.apply {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    fun yoWtf(msg: String) {
        Toast.makeText(this.applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES) //because reasons.
        setContentView(R.layout.activity_main)
        TehButton.setOnClickListener {convertButtonPressed()}
    }


}