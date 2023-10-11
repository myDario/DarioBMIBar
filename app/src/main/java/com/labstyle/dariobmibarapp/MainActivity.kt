package com.labstyle.dariobmibarapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.labstyle.dariobmibar.DarioBMIBar
import com.labstyle.dariobmibar.DarioBMIDotIndicator
import com.labstyle.dariobmibar.DarioBMISections

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<DarioBMIBar>(R.id.bmiBar).setBMIValue(0f)
        findViewById<DarioBMISections>(R.id.bmiSections).setBMIValue(30.5f)
        findViewById<DarioBMIDotIndicator>(R.id.bmiDotIndicator).setBMIValue(24.9f)
    }
}
//0.26545453