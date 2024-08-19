package com.example.calculadora

import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.calculadora.Calculator.MODO_EXIBINDO
import com.example.calculadora.components.CustomButtonView
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    private lateinit var visorText: EditText
    private var calculator: Calculator = Calculator()
    private var hasComma = false

    private var DEFAULT_VALUE = "0"
    private var COMMA_VALUE = "."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        visorText = findViewById(R.id.editText)
        visorText.showSoftInputOnFocus = false

        findViewById<CustomButtonView?>(R.id.btnSubmit).setOnClickListener { onPressSubmit() }
        findViewById<CustomButtonView?>(R.id.btnClear).setOnClickListener { onPressClear() }
        findViewById<CustomButtonView?>(R.id.btnDelete).setOnClickListener { onPressDelete() }
        findViewById<CustomButtonView?>(R.id.btnComma).setOnClickListener { onPressComma() }

        findViewById<CustomButtonView?>(R.id.btnPlus).setOnClickListener { onPressPlus() }
        findViewById<CustomButtonView?>(R.id.btnMinus).setOnClickListener { onPressMinus() }
        findViewById<CustomButtonView?>(R.id.btnPow).setOnClickListener { onPressPow() }
        findViewById<CustomButtonView?>(R.id.btnDivisor).setOnClickListener { onPressDivisor() }
        findViewById<CustomButtonView?>(R.id.btnPv).setOnClickListener { onPressPv() }
        findViewById<CustomButtonView?>(R.id.btnFv).setOnClickListener { onPressFv() }
        findViewById<CustomButtonView?>(R.id.btnPmt).setOnClickListener { onPressPmt() }
        findViewById<CustomButtonView?>(R.id.btnN).setOnClickListener { onPressN() }
        findViewById<CustomButtonView?>(R.id.btnI).setOnClickListener { onPressI() }

        findViewById<CustomButtonView?>(R.id.btnZero).setOnClickListener { changeVisorText("0") }
        findViewById<CustomButtonView?>(R.id.btnOne).setOnClickListener { changeVisorText("1") }
        findViewById<CustomButtonView?>(R.id.btnTwo).setOnClickListener { changeVisorText("2") }
        findViewById<CustomButtonView?>(R.id.btnThree).setOnClickListener { changeVisorText("3") }
        findViewById<CustomButtonView?>(R.id.btnFour).setOnClickListener { changeVisorText("4") }
        findViewById<CustomButtonView?>(R.id.btnFive).setOnClickListener { changeVisorText("5") }
        findViewById<CustomButtonView?>(R.id.btnSix).setOnClickListener { changeVisorText("6") }
        findViewById<CustomButtonView?>(R.id.btnSeven).setOnClickListener { changeVisorText("7") }
        findViewById<CustomButtonView?>(R.id.btnEight).setOnClickListener { changeVisorText("8") }
        findViewById<CustomButtonView?>(R.id.btnNine).setOnClickListener { changeVisorText("9") }
    }

    private fun changeVisorText(newChar: String) {
        if (calculator.modo == MODO_EXIBINDO) {
            clearVisor()
        }

        var newValue = newChar
        val startCursor = visorText.selectionStart
        val endCursor = visorText.selectionEnd
        val prevLength = visorText.text.length

        if (newValue == COMMA_VALUE && startCursor == prevLength) {
            hasComma = true
        } else {
            if (hasComma) {
                newValue = COMMA_VALUE + newChar
            }

            if (visorText.text.startsWith(DEFAULT_VALUE) && hasComma) {
                visorText.setText("0$newValue")
            } else if (
                visorText.text.startsWith(DEFAULT_VALUE) &&
                !visorText.text.startsWith("$DEFAULT_VALUE$COMMA_VALUE")) {
                visorText.setText(newValue)
            } else {
                visorText.text = visorText.text.replace(startCursor, endCursor, newValue)
            }

            hasComma = false
            visorText.setSelection(visorText.text.length)
            changeNumberCalculator(visorText.text.toString().toDouble())
        }
    }

    private fun changeNumberCalculator(number: Double) {
        calculator.numero = number
    }

    private fun onPressPv() {
        calculator.pressPv()
        updateVisorValue()
    }

    private fun onPressFv() {
        calculator.pressFv()
        updateVisorValue()
    }

    private fun onPressPmt() {
        calculator.pressPmt()
        updateVisorValue()
    }

    private fun onPressI() {
        calculator.pressI()
        updateVisorValue()
    }

    private fun onPressN() {
        calculator.pressN()
        updateVisorValue()
    }

    private fun onPressPlus() {
        calculator.soma()
        updateVisorValue()
    }

    private fun onPressMinus() {
        calculator.subtracao()
        updateVisorValue()
    }

    private fun onPressPow() {
        calculator.multiplicacao()
        updateVisorValue()
    }

    private fun onPressDivisor() {
        calculator.divisao()
        updateVisorValue()
    }

    private fun onPressSubmit() {
        calculator.enter()
    }

    private fun onPressComma() {
        val currentValue = visorText.text.toString()

        if (!currentValue.contains(".")) {
            changeVisorText(COMMA_VALUE)
        }
    }

    private fun onPressClear() {
        clearVisor()
        calculator.reset()
    }

    private fun onPressDelete() {
        if (visorText.text.length > 1) {
            val startCursor = visorText.selectionStart
            val endCursor = visorText.selectionEnd

            var newText = visorText.text.replace(startCursor, endCursor, "").toString()

            if (newText.endsWith(COMMA_VALUE)) {
                newText = newText.dropLast(1)
            }

            if (newText.isEmpty()) {
                clearVisor()
            } else {
                visorText.setText(newText)
                changeNumberCalculator(newText.replace(",",".").toDouble())
                visorText.setSelection(newText.length)
            }
        } else
            clearVisor()
    }

    private fun updateVisorValue() {
        val newValue = calculator.numero
        val format: DecimalFormat = DecimalFormat("#,##0.##")

        visorText.setText(format.format(newValue).toString())
    }

    private fun clearVisor() {
        hasComma = false
        visorText.setSelection(1)
        visorText.setText(DEFAULT_VALUE)
    }
}