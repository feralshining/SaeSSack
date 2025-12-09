package com.saessac;

import android.os.Bundle;
import androidx.activity.ComponentActivity;
import androidx.core.splashscreen.SplashScreen;


public class SplashActivity extends ComponentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        SaessacUI.openActivity(this, LoginRegisterActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
