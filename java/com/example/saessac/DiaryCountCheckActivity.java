package com.example.saessac;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.saessac.SaessacFTP;
import com.example.saessac.SaessacUI;
import com.example.saessac.SaessacUserData;

public class DiaryCountCheckActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int theme = sharedPreferences.getInt("Theme", 0);

        if (theme == 0) {
            setContentView(R.layout.activity_number_of_diaries_theme_0);
        } else if (theme == 1) {
            setContentView(R.layout.activity_number_of_diaries_theme_1);
        }

        String today = SaessacUI.date;
        String month = today.substring(4, 6); // assuming format, but in Kotlin it's 4,5 which is one char, probably 4,6
        String uuid = SaessacUserData.uuid;

        // [     다이어리 작성 횟수     ]
        new Thread(() -> {
            int[] diaryNum = SaessacFTP.GetDiaryCount(month, uuid);
            runOnUiThread(() -> {
                ((EditText) findViewById(R.id.diarytotal_txt)).setText(String.valueOf(diaryNum[0])); // 전체 작성 횟수
                ((EditText) findViewById(R.id.diarymonth_txt)).setText(String.valueOf(diaryNum[1])); // 전체 중 해당 월 작성 횟수
            });
        }).start();

        // [     버튼 - 확인     ]
        ImageButton checkBTN = (ImageButton) findViewById(R.id.check_btn);
        checkBTN.setOnClickListener(v -> {
            SaessacUI.OpenActivity(this, MainActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    @Override
    public void onBackPressed() {
    }
}