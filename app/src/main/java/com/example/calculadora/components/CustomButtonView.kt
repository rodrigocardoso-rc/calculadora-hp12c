package com.example.calculadora.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import com.example.calculadora.R

class CustomButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : LinearLayout(context, attrs) {

    private val button: Button

    init {
        // Inflate the layout from the 'components' subdirectory
        LayoutInflater.from(context).inflate(R.layout.view_custom_button, this, true)

        button = findViewById(R.id.button)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomButtonView, 0, 0)
            val text = typedArray.getString(R.styleable.CustomButtonView_android_text)

            button.text = text

            typedArray.recycle()
        }
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        button.setOnClickListener(listener)
    }
}
