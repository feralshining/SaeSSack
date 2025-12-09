package com.example.saessac;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.saessac.SaessacUI;

public class EventDiscountActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_notice_theme);
        // [     버튼 - 뒤로 가기     ]
        ImageButton previousBTN = (ImageButton) findViewById(R.id.previous_btn);
        previousBTN.setOnClickListener(v -> {
            SaessacUI.OpenActivity(this, EventActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    @Override
    public void onBackPressed() {
        SaessacUI.OpenActivity(this, EventActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
    }
}