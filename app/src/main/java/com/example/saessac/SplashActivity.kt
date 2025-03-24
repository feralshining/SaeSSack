package com.saessac

import android.os.Bundle
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.saessac.SaessacUI

class SplashActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        SaessacUI.OpenActivity(this, LoginRegisterActivity::class.java)
    }
}