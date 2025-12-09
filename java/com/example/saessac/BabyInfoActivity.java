package com.example.saessac;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.saessac.SaessacUserData;
import com.example.saessac.Extensions;
import com.example.saessac.SaessacUI;
import com.google.gson.Gson;

public class BabyInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int theme = sharedPreferences.getInt("Theme", 0);

        if (theme == 0) {
            setContentView(R.layout.activity_baby_information_theme_0);
        } else if (theme == 1) {
            setContentView(R.layout.activity_baby_information_theme_1);
        }

        // 하단 테마 창 정의
        SaessacUI.TouchBTN(this, R.anim.slide_in_right, R.anim.slide_out_left);

        // [     창 진입 시 아이 프로필 정보 로드     ]
        String filePath = getBaseContext().getFilesDir().toString() + "/" + "USER_BABYINFO.json";
        SaessacUserData.BabyInfo data = SaessacUserData.LoadBabyInfo(filePath);
        if (data != null) {
            ((EditText) findViewById(R.id.name_txt)).setText(data.name);
            ((EditText) findViewById(R.id.birth_txt)).setText(data.birth);
            ((EditText) findViewById(R.id.sex_txt)).setText(data.sex);
            ((EditText) findViewById(R.id.bloodtype_txt)).setText(data.bloodtype);
            ((EditText) findViewById(R.id.hypocorism_txt)).setText(data.hypocorism);
            ((EditText) findViewById(R.id.headcircum_txt)).setText(data.headcircum);
            ((EditText) findViewById(R.id.height_txt)).setText(data.height);
            ((EditText) findViewById(R.id.weight_txt)).setText(data.weight);
        }

        // [     버튼 - 작성 완료     ]
        ImageButton saveBTN = (ImageButton) findViewById(R.id.save_btn);
        saveBTN.setOnClickListener(v -> {
            String babyName = ((EditText) findViewById(R.id.name_txt)).getText().toString();
            String babyBirth = ((EditText) findViewById(R.id.birth_txt)).getText().toString();
            String babySex = ((EditText) findViewById(R.id.sex_txt)).getText().toString();
            String babyBloodType = ((EditText) findViewById(R.id.bloodtype_txt)).getText().toString();
            String babyHypocorism = ((EditText) findViewById(R.id.hypocorism_txt)).getText().toString();
            String babyHeadcircum = ((EditText) findViewById(R.id.headcircum_txt)).getText().toString();
            String babyHeight = ((EditText) findViewById(R.id.height_txt)).getText().toString();
            String babyWeight = ((EditText) findViewById(R.id.weight_txt)).getText().toString();

            if (babyName.isEmpty() || babyBirth.isEmpty() || babySex.isEmpty() || babyBloodType.isEmpty()
                    || babyHypocorism.isEmpty() || babyHeadcircum.isEmpty() || babyHeight.isEmpty() || babyWeight.isEmpty()) {
                SaessacUI.ShowText(this, "빈 칸 없이 작성해주세요.");
                return;
            }
            java.util.LinkedHashMap<String, String> user = new java.util.LinkedHashMap<>();
            user.put("name", babyName);
            user.put("birth", babyBirth);
            user.put("sex", babySex);
            user.put("bloodtype", babyBloodType);
            user.put("hypocorism", babyHypocorism);
            user.put("headcircum", babyHeadcircum);
            user.put("height", babyHeight);
            user.put("weight", babyWeight);
            Extensions.CreateFile(getBaseContext().getFilesDir().toString(), "USER_BABYINFO.json", new Gson().toJson(user));
            SaessacUI.ShowText(this, "작성 완료!");
        });
    }

    @Override
    public void onBackPressed() {
    }
}