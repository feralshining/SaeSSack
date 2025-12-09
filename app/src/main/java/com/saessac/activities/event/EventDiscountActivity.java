package com.saessac.activities.event;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import com.saessac.R;
import com.saessac.utils.SaessacUI;

public class EventDiscountActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_notice_theme);

        // [ 버튼 - 뒤로 가기 ]
        ImageButton previousBTN = findViewById(R.id.previous_btn);
        previousBTN.setOnClickListener(v -> {
            SaessacUI.openActivity(this, EventActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // 뒤로 가기 버튼 처리
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                SaessacUI.openActivity(EventDiscountActivity.this, EventActivity.class, R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });
    }
}
