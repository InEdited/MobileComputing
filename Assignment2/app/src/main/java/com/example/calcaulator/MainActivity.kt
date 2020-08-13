package com.example.calcaulator

import android.annotation.SuppressLint
import android.app.UiModeManager.MODE_NIGHT_YES
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.itis.libs.parserng.android.expressParser.MathExpression
import net.objecthunter.exp4j.ExpressionBuilder


class MainActivity : AppCompatActivity() {

    var stateError: Boolean = false
    lateinit var txtInput: TextView
    lateinit var output: TextView
    var lastNumeric: Boolean = true
    var lastDot: Boolean = false
    var temp: String = ""
    var memory: String = ""
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        println("bruh we boooooooooooooooolin")
        txtInput = findViewById(R.id.inputTextView)
        output = findViewById(R.id.resultTextView)
        txtInput.append("0")
        temp ="0"

    }
    fun onDigit(view: View) {
        if (stateError) {
            // If current state is Error, replace the error message
            txtInput.text = (view as Button).text
            stateError = false
        } else {
            // If not, already there is a valid expression so append to it
            txtInput.append((view as Button).text)
        }
        // Set the flag
        lastNumeric = true
    }
    fun onButtonClick(view: View) {
        var buttonText = (view as Button).text.toString();
        if(buttonText=="*"){
            if (!stateError) {
                txtInput.append((view as Button).text)
                lastNumeric = true
                lastDot = false    // Reset the DOT flag
                temp =  ""
            }
        }else if(buttonText=="+"){
            if (lastNumeric && !stateError) {
                txtInput.append((view as Button).text)
                lastNumeric = false
                lastDot = false    // Reset the DOT flag
                temp = ""
            }
        }else if(buttonText=="-"){
            if (lastNumeric && !stateError) {
                txtInput.append((view as Button).text)
                lastNumeric = false
                lastDot = false    // Reset the DOT flag
                temp =""
            }
        }else if(buttonText=="/"){
            if (!stateError) {
                txtInput.append((view as Button).text)
                lastNumeric = false
                lastDot = false    // Reset the DOT flag
                temp =""
            }
        }else if(buttonText=="."){
            if (lastNumeric && !stateError && !lastDot) {
                txtInput.append(".")
                lastNumeric = false
                lastDot = true
                temp =""
            }
        }else if(buttonText=="="){
            if (lastNumeric && !stateError) {
                evaluate()
                txtInput.text = ""
                lastNumeric = false
                lastDot = false
                txtInput.append("0")
                temp ="0"

            }
        }else if(buttonText=="M"){
            if (!stateError) {
                txtInput.append(memory)
                if(temp.startsWith('0')&&temp.length>1&&!lastDot){
                    temp = temp.drop(1)
                    //temp.removePrefix(0.toString())
                    println(temp)
                    txtInput.append((view as Button).text)
                    txtInput.text=txtInput.text.dropLast(2)
                    txtInput.append((view as Button).text)

                }
                println("memory")
                evaluate()
                lastNumeric = true
                stateError = false
            }
        }else if(buttonText=="M-"){

                memory = ""
                lastNumeric = false
                lastDot = false
                println("memory cleared")

        }else if(buttonText=="M+"){
            if (lastNumeric && !stateError) {
                evaluate()
                var test:Int
                test= output.text.toString().toFloat().toInt()
                if(test.toFloat()==output.text.toString().toFloat()){
                    memory=output.text.toString().toFloat().toInt().toString()
                }else {
                    memory = output.text.toString()
                }
                println(memory)
                lastNumeric = false
                lastDot = false

            }
        }else if(buttonText=="C"){
            txtInput.text = ""
            lastNumeric = false
            stateError = false
            lastDot = false
            txtInput.append("")
            output.text=""
            temp ="0"

        }else{
            if (stateError) {
                // If current state is Error, replace the error message
                txtInput.text = (view as Button).text
                stateError = false

            } else {
                temp+=(view as Button).text
                // If not, already there is a valid expression so append to it
                //println(temp)
                if(temp.startsWith('0')&&temp.length>1&&!lastDot){
                    temp = temp.drop(1)
                    //temp.removePrefix(0.toString())
                    println(temp)
                    txtInput.append((view as Button).text)
                    txtInput.text=txtInput.text.dropLast(2)
                    txtInput.append((view as Button).text)

                }else{
                    txtInput.append((view as Button).text)

                }
            }
            // Set the flag
            lastNumeric = true
            stateError = false

            evaluate()
        }
        //println(buttonText)

    }

    private fun evaluate() {
        val txt = txtInput.text.toString()
        // Create an Expression (A class from exp4j library)
        val expression = ExpressionBuilder(txt).build()
        try {
            // Calculate the result and display
            val result = expression.evaluate()
            val expr = MathExpression(txt)
            println(expr.solve())
            output.text = String.format("%.4g",result)
        } catch (ex: ArithmeticException) {
            // Display an error message
            output.text = "Error"
            Toast.makeText(this.applicationContext, "You should know how to use a calculator by now come on.", Toast.LENGTH_SHORT).show()

            stateError = true
            lastNumeric = false
        }catch (ex:Exception){

        }
    }
}