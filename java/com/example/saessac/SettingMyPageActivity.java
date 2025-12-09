package com.example.saessac;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.saessac.Extensions;
import com.example.saessac.SaessacUI;
import com.example.saessac.SaessacUserData;
import com.google.gson.Gson;
import java.util.LinkedHashMap;
import java.util.Map;

public class SettingMyPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int theme = sharedPreferences.getInt("Theme", 0);

        if (theme == 0) {
            setContentView(R.layout.activity_my_page_theme_0);
        } else if (theme == 1) {
            setContentView(R.layout.activity_my_page_theme_1);
        }

        Object user = SaessacUserData.LoadUserInfo(getBaseContext().getFilesDir().toString() + "/" + "USER_MYPAGE.json");
        System.out.println(user);
        if (user != null) {
            ((EditText) findViewById(R.id.name_txt)).setText(((SaessacUserData.UserInfo) user).name);
            ((EditText) findViewById(R.id.birth_txt)).setText(((SaessacUserData.UserInfo) user).birth);
            ((EditText) findViewById(R.id.sex_txt)).setText(((SaessacUserData.UserInfo) user).sex);
            ((EditText) findViewById(R.id.bloodtype_txt)).setText(((SaessacUserData.UserInfo) user).bloodtype);
            ((EditText) findViewById(R.id.relationship_txt)).setText(((SaessacUserData.UserInfo) user).relationship);
        }

        ImageButton previousBTN = findViewById(R.id.previous_btn);
        previousBTN.setOnClickListener(v -> {
            SaessacUI.OpenActivity(this, SettingActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
        });

        ImageButton saveBTN = findViewById(R.id.save_btn);
        saveBTN.setOnClickListener(v -> {
            String userName = ((EditText) findViewById(R.id.name_txt)).getText().toString();
            String userBirth = ((EditText) findViewById(R.id.birth_txt)).getText().toString();
            String userSex = ((EditText) findViewById(R.id.sex_txt)).getText().toString();
            String userBloodType = ((EditText) findViewById(R.id.bloodtype_txt)).getText().toString();
            String userRelationship = ((EditText) findViewById(R.id.relationship_txt)).getText().toString();

            if (userName.isEmpty() || userBirth.isEmpty() || userSex.isEmpty() || userBloodType.isEmpty() || userRelationship.isEmpty()) {
                SaessacUI.ShowText(this, "빈 칸 없이 작성해주세요.");
                return;
            }

            Map<String, String> userMap = new LinkedHashMap<>();
            userMap.put("name", userName);
            userMap.put("birth", userBirth);
            userMap.put("sex", userSex);
            userMap.put("bloodtype", userBloodType);
            userMap.put("relationship", userRelationship);

            Extensions.CreateFile(getBaseContext().getFilesDir().toString(), "USER_MYPAGE.json", new Gson().toJson(userMap));
            SaessacUI.ShowText(this, "작성 완료!");
        });
    }

    @Override
    public void onBackPressed() {
        SaessacUI.OpenActivity(this, SettingActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
    }
}