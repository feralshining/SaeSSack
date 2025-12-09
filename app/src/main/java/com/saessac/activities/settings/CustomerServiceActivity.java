package com.saessac.activities.settings;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import com.saessac.R;
import com.saessac.utils.SaessacUI;

public class CustomerServiceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service_theme);

        // [ 버튼 - 뒤로 가기 ]
        ImageButton previousBTN = findViewById(R.id.previous_btn);
        previousBTN.setOnClickListener(v -> {
            SaessacUI.openActivity(this, SettingActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    // onBackPressed 메서드는 제거되었습니다 - onCreate에서 OnBackPressedCallback을 사용하세요
}
