package com.labstyle.dariobmibar

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.ShapeDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import java.util.*

class DarioBMIBar @JvmOverloads constructor(
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

        inflate(context, R.layout.dario_bmi_bar, this)

        setBMIUnitLabel(unitLabel)
        setBMIValue(bmi)
    }

    fun setBMIUnitLabel(label: String) {
        findViewById<TextView>(R.id.bmiValueUnitText)?.text = label
    }

    fun setBMIValue(value: Float) {
        bmi = value

        findViewById<TextView>(R.id.bmiValueText)?.text =
            "%.1f".format(Locale.getDefault(), bmi)

        val parent = findViewById<ConstraintLayout>(R.id.topBarsLayout)
        val constraintSet = ConstraintSet()
        constraintSet.clone(parent)

        val factor =
            if (bmi < 18.5) 0.265454545f / 18.5f
            else if (bmi >= 18.5 && bmi < 25.0) 0.367272727f / (25.0f - 18.5f)
            else if (bmi >= 25.0 && bmi < 30.0) 0.236363636f / (30.0f - 25.0f)
            else 0.130909091f / (40f - 30f)
        var hBias =
            if (bmi < 18.5) factor * bmi
            else if (bmi >= 18.5 && bmi < 25.0) 0.265454545f + (factor * (bmi - 18.5f))
            else if (bmi >= 25.0 && bmi < 30.0) 0.265454545f + 0.367272727f + (factor * (bmi - 25f))
            else 0.265454545f + 0.367272727f + 0.236363636f + (factor * (bmi - 30f))

        if (hBias > 1.0f) {
            hBias = 1.0f
        }

        constraintSet.setHorizontalBias(R.id.selectionLineIndicator, hBias)
        constraintSet.applyTo(parent)

        val circleBg =
            if (bmi < 18.5) R.drawable.bmi_circle_indicator_0
            else if (bmi >= 18.5 && bmi < 25.0) R.drawable.bmi_circle_indicator_1
            else if (bmi >= 25.0 && bmi < 30.0) R.drawable.bmi_circle_indicator_2
            else R.drawable.bmi_circle_indicator_3
        findViewById<View>(R.id.circleIndicator)?.let { circle ->
            circle.background = ContextCompat.getDrawable(context, circleBg)
        }
    }
}