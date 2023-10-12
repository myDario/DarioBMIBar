package com.labstyle.dariobmibar

import android.content.Context
import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import java.util.Locale

class DarioBMIDotIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
): RelativeLayout(context, attrs, defStyle, defStyleRes) {
    private var bmi = 21f
    private var unitLabel = "BMI"

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.DarioBMIBar)
        bmi = attributes.getFloat(R.styleable.DarioBMIBar_bmi, bmi)
        unitLabel = attributes.getString(R.styleable.DarioBMIBar_unitLabel) ?: unitLabel
        attributes.recycle()

        inflate(context, R.layout.dario_bmi_dot_indicator, this)

        setBMIUnitLabel(unitLabel)
        setBMIValue(bmi)
    }

    fun setBMIUnitLabel(label: String) {
        findViewById<TextView>(R.id.bmiText)?.text = label
    }

    fun setBMIValue(value: Float) {
        bmi = value


        val parent = findViewById<ConstraintLayout>(R.id.topBarsLayout)
        val constraintSet = ConstraintSet()
        constraintSet.clone(parent)

        var hBias = calculateHBias(bmi)

        if (hBias > 0.985f) {
            hBias = 0.985f
        }
        if (hBias < 0.01f) {
            hBias = 0.01f
        }

        constraintSet.setHorizontalBias(R.id.selectionLineIndicator, hBias)
        constraintSet.clear(R.id.bmiText, ConstraintSet.START)
        constraintSet.clear(R.id.bmiText, ConstraintSet.END)

        if (hBias > 0.85f) {
            constraintSet.connect(
                R.id.bmiText,
                ConstraintSet.END,
                R.id.selectionLineIndicator,
                ConstraintSet.START
            )
        } else if (hBias < 0.08f) {
            // Position bmiText to the left
            constraintSet.connect(
                R.id.bmiText,
                ConstraintSet.START,
                R.id.selectionLineIndicator,
                ConstraintSet.END
            )
        } else {
            constraintSet.connect(
                R.id.bmiText,
                ConstraintSet.START,
                R.id.selectionLineIndicator,
                ConstraintSet.START
            )
            constraintSet.connect(
                R.id.bmiText,
                ConstraintSet.END,
                R.id.selectionLineIndicator,
                ConstraintSet.END
            )
        }

        constraintSet.applyTo(parent)

        val circleBg =
            if (bmi < 18.5) R.drawable.bmi_dot_indicator
            else if (bmi >= 18.5 && bmi < 25.0) R.drawable.bmi_dot_indicator
            else if (bmi >= 25.0 && bmi < 30.0) R.drawable.bmi_dot_indicator
            else R.drawable.bmi_dot_indicator
        findViewById<View>(R.id.circleIndicator)?.let { circle ->
            circle.background = ContextCompat.getDrawable(context, circleBg)
        }

    }

    private fun calculateHBias(bmi: Float): Float {
        return when {
            bmi < 18.5f -> {
                val factor = bmi / 18.5f
                0.01f + factor * (0.12f - 0.01f)
            }

            bmi >= 18.5f && bmi < 25.0f -> {
                val factor = (bmi - 18.5f) / (25.0f - 18.5f)
                0.17f + factor * (0.50f - 0.17f)
            }

            bmi >= 25.0f && bmi < 30.0f -> {
                val factor = (bmi - 25.0f) / (30.0f - 25.0f)
                0.54f + factor * (0.71f - 0.54f)
            }

            else -> {
                val factor = (bmi - 30.0f) / (40.0f - 30.0f)
                0.76f + factor * (0.985f - 0.76f)
            }
        }
    }
}