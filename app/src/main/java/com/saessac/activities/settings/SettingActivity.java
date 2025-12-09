package com.saessac.activities.settings;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.saessac.R;
import com.saessac.utils.SaessacUI;
import com.saessac.utils.SaessacUserData;
import com.saessac.utils.Extensions;
import com.saessac.activities.settings.MyPageActivity;
import com.saessac.activities.settings.CustomerServiceActivity;
import com.saessac.activities.settings.PrivacyPolicyActivity;
import com.saessac.activities.sharing.FamilySharingActivity;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SettingActivity extends AppCompatActivity {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @SuppressLint({ "CutPasteId", "UseSwitchCompatOrMaterialCode" })
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int theme = sharedPreferences.getInt("Theme", 0);

        switch (theme) {
            case 0:
                setContentView(R.layout.activity_setting_theme_0);
                break;
            case 1:
                setContentView(R.layout.activity_setting_theme_1);
                break;
        }

        // [ 하단 테마 창 정의 ]
        SaessacUI.touchBTN(this, R.anim.slide_in_right, R.anim.slide_out_left);

        // [ 창 오픈 - 마이 페이지 ]
        Button mypageBTN = findViewById(R.id.mypage_btn);
        mypageBTN.setOnClickListener(v -> {
            SaessacUI.openActivity(this, MyPageActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // 설정 - BGM ON/OFF
        Switch bgmSwitch = findViewById(R.id.playmusic_toggle);
        JsonObject json = SaessacUserData.loadJSON(getBaseContext().getFilesDir().toString() + "/" + "Config.json");
        boolean isBGM = false;
        if (json != null && json.has("bgm_toggle")) {
            isBGM = json.get("bgm_toggle").getAsBoolean();
        }
        bgmSwitch.setChecked(isBGM);
        bgmSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            executorService.execute(() -> {
                LinkedHashMap<String, String> data = new LinkedHashMap<>();
                data.put("bgm_toggle", String.valueOf(isChecked));
                Extensions.createFile(getBaseContext().getFilesDir().toString(), "Config.json",
                        new Gson().toJson(data));
            });
        });

        // 설정 - 알림 ON/OFF
        Switch alarmSwitch = findViewById(R.id.alarm_switch);
        alarmSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            executorService.execute(() -> {
                LinkedHashMap<String, String> data = new LinkedHashMap<>();
                data.put("alarm_toggle", String.valueOf(isChecked));
                Extensions.createFile(getBaseContext().getFilesDir().toString(), "AlarmConfig.json",
                        new Gson().toJson(data));
            });
        });

        // [ 창 오픈 - 고객센터 ]
        Button serviceBTN = findViewById(R.id.customer_service_btn);
        serviceBTN.setOnClickListener(v -> {
            SaessacUI.openActivity(this, CustomerServiceActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // [ 창 오픈 - 운영정책 ]
        Button policyBTN = findViewById(R.id.policy_btn);
        policyBTN.setOnClickListener(v -> {
            SaessacUI.openActivity(this, PrivacyPolicyActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // [ 창 오픈 - 가족 공유 ]
        Button sharingBTN = findViewById(R.id.sharing_btn);
        sharingBTN.setOnClickListener(v -> {
            SaessacUI.openActivity(this, FamilySharingActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // 뒤로 가기 버튼 처리
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Do nothing - 뒤로 가기 버튼 비활성화
            }
        });
    }
}
