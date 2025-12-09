package com.saessac;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.LinkedHashMap;

public class SettingMyPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int theme = sharedPreferences.getInt("Theme", 0);

        switch (theme) {
            case 0:
                setContentView(R.layout.activity_my_page_theme_0);
                break;
            case 1:
                setContentView(R.layout.activity_my_page_theme_1);
                break;
        }

        // 마이페이지 정보 로드
        SaessacUserData.UserInfo user = SaessacUserData
                .loadUserInfo(getBaseContext().getFilesDir().toString() + "/" + "USER_MYPAGE.json");
        if (user != null) {
            ((EditText) findViewById(R.id.name_txt)).setText(user.name);
            ((EditText) findViewById(R.id.birth_txt)).setText(user.birth);
            ((EditText) findViewById(R.id.sex_txt)).setText(user.sex);
            ((EditText) findViewById(R.id.bloodtype_txt)).setText(user.bloodtype);
            ((EditText) findViewById(R.id.relationship_txt)).setText(user.relationship);
        }

        // 버튼 - 뒤로 가기
        ImageButton previousBTN = findViewById(R.id.previous_btn);
        previousBTN.setOnClickListener(v -> {
            SaessacUI.openActivity(this, SettingActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // 버튼 - 작성 완료
        ImageButton saveBTN = findViewById(R.id.save_btn);
        saveBTN.setOnClickListener(v -> {
            String userName = ((EditText) findViewById(R.id.name_txt)).getText().toString();
            String userBirth = ((EditText) findViewById(R.id.birth_txt)).getText().toString();
            String userSex = ((EditText) findViewById(R.id.sex_txt)).getText().toString();
            String userBloodType = ((EditText) findViewById(R.id.bloodtype_txt)).getText().toString();
            String userRelationship = ((EditText) findViewById(R.id.relationship_txt)).getText().toString();

            if (userName.isEmpty() || userBirth.isEmpty() || userSex.isEmpty() || userBloodType.isEmpty()
                    || userRelationship.isEmpty()) {
                SaessacUI.showText(this, "빈 칸 없이 작성해주세요.");
                return;
            }

            LinkedHashMap<String, String> userData = new LinkedHashMap<>();
            userData.put("name", userName);
            userData.put("birth", userBirth);
            userData.put("sex", userSex);
            userData.put("bloodtype", userBloodType);
            userData.put("relationship", userRelationship);

            Extensions.createFile(getBaseContext().getFilesDir().toString(), "USER_MYPAGE.json",
                    new Gson().toJson(userData));
            SaessacUI.showText(this, "작성 완료!");
        });
    }

    // onBackPressed removed - use OnBackPressedCallback in onCreate instead
}
