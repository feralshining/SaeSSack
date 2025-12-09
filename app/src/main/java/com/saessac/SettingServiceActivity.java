package com.saessac;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class SettingServiceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service_theme);

        // 버튼 - 뒤로 가기
        ImageButton previousBTN = findViewById(R.id.previous_btn);
        previousBTN.setOnClickListener(v -> {
            SaessacUI.openActivity(this, SettingActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    // onBackPressed removed - use OnBackPressedCallback in onCreate instead
}
