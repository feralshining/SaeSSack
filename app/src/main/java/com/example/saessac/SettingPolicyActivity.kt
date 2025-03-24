package com.saessac

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.saessac.SaessacUI

class SettingPolicyActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_policy_theme)
        //<editor-fold desc="[     버튼 - 뒤로 가기     ]">
            val previousBTN: ImageButton = findViewById(R.id.previous_btn)
            previousBTN.setOnClickListener{
                SaessacUI.OpenActivity(this,SettingActivity::class.java)
            }
        //</editor-fold>
    }

    override fun onBackPressed()
    {
        SaessacUI.OpenActivity(this,SettingActivity::class.java)
    }
}