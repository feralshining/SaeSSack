package com.saessac.activities.main;

import android.os.Bundle;
import androidx.activity.ComponentActivity;
import androidx.core.splashscreen.SplashScreen;
import com.saessac.R;
import com.saessac.activities.auth.AuthActivity;
import com.saessac.utils.SaessacUI;

public class SplashActivity extends ComponentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        SaessacUI.openActivity(this, AuthActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
