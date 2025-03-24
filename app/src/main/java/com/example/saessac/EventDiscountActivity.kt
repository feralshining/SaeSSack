package com.saessac

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.saessac.SaessacUI

class EventDiscountActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discount_notice_theme)
        // <editor-fold desc="[     버튼 - 뒤로 가기     ]">
            val previousBTN = findViewById<ImageButton>(R.id.previous_btn)
            previousBTN.setOnClickListener{
                SaessacUI.OpenActivity(this, EventActivity::class.java)
            }
        //</editor-fold>
    }
    override fun onBackPressed()
    {
        SaessacUI.OpenActivity(this, EventActivity::class.java)
    }
}