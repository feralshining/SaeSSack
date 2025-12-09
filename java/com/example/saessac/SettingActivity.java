package com.example.saessac;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import com.example.saessac.Extensions;
import com.example.saessac.SaessacUI;
import com.example.saessac.SaessacUserData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.LinkedHashMap;
import java.util.Map;

public class SettingActivity extends AppCompatActivity {
    @SuppressLint({"CutPasteId", "UseSwitchCompatOrMaterialCode"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int theme = sharedPreferences.getInt("Theme", 0);

        if (theme == 0) {
            setContentView(R.layout.activity_setting_theme_0);
        } else if (theme == 1) {
            setContentView(R.layout.activity_setting_theme_1);
        }

        SaessacUI.TouchBTN(this, R.anim.slide_in_right, R.anim.slide_out_left);

        Button mypageBTN = findViewById(R.id.mypage_btn);
        mypageBTN.setOnClickListener(v -> {
            SaessacUI.OpenActivity(this, SettingMyPageActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
        });

        Switch bgmSwitch = findViewById(R.id.playmusic_toggle);
        JsonObject json = SaessacUserData.LoadJSON(getBaseContext().getFilesDir().toString() + "/" + "Config.json");
        boolean isBGM = json != null && json.get("bgm_toggle") != null ? json.get("bgm_toggle").getAsBoolean() : false;
        bgmSwitch.setChecked(isBGM);
        bgmSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            new Thread(() -> {
                Map<String, String> data = new LinkedHashMap<>();
                data.put("bgm_toggle", String.valueOf(isChecked));
                Extensions.CreateFile(getBaseContext().getFilesDir().toString(), "Config.json", new Gson().toJson(data));
            }).start();
        });

        Switch noticeSwitch = findViewById(R.id.playmusic_toggle);
        noticeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            new Thread(() -> {
                Map<String, String> data = new LinkedHashMap<>();
                data.put("bgm_toggle", String.valueOf(isChecked));
                Extensions.CreateFile(getBaseContext().getFilesDir().toString(), "Config.json", new Gson().toJson(data));
            }).start();
        });

        Button serviceBTN = findViewById(R.id.customer_service_btn);
        serviceBTN.setOnClickListener(v -> {
            SaessacUI.OpenActivity(this, SettingServiceActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
        });

        Button policyBTN = findViewById(R.id.policy_btn);
        policyBTN.setOnClickListener(v -> {
            SaessacUI.OpenActivity(this, SettingPolicyActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
        });

        Button sharingBTN = findViewById(R.id.sharing_btn);
        sharingBTN.setOnClickListener(v -> {
            SaessacUI.OpenActivity(this, SharingUUIDActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    @Override
    public void onBackPressed() {
    }
}