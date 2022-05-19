package com.labstyle.dariobmibar

import android.content.Context
import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout

class DarioBMISections @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
): RelativeLayout(context, attrs, defStyle, defStyleRes) {
    private var bmi = 21f

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.DarioBMIBar)
        bmi = attributes.getFloat(R.styleable.DarioBMIBar_bmi, bmi)
        attributes.recycle()

        inflate(context, R.layout.dario_bmi_sections, this)

        setBMIValue(bmi)
    }

    fun setBMIValue(value: Float) {
        bmi = value

        val dividers = listOf<View>(
            findViewById(R.id.divider0),
            findViewById(R.id.divider1),
            findViewById(R.id.divider2),
            findViewById(R.id.divider3),
            findViewById(R.id.divider4),
            findViewById(R.id.divider5),
            findViewById(R.id.divider6),
            findViewById(R.id.divider7)
        )
        dividers.forEach { it.visibility = View.GONE }

        if (bmi < 18.5) {
            dividers[0].visibility = View.VISIBLE
            dividers[1].visibility = View.VISIBLE
        } else if (bmi >= 18.5 && bmi < 25.0) {
            dividers[2].visibility = View.VISIBLE
            dividers[3].visibility = View.VISIBLE
        } else if (bmi >= 25.0 && bmi < 30.0) {
            dividers[4].visibility = View.VISIBLE
            dividers[5].visibility = View.VISIBLE
        } else {
            dividers[6].visibility = View.VISIBLE
            dividers[7].visibility = View.VISIBLE
        }
    }
}