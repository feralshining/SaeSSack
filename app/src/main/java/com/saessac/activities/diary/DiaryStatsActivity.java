package com.saessac.activities.diary;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import com.saessac.R;
import com.saessac.utils.SaessacUI;
import com.saessac.utils.SaessacUserData;
import com.saessac.utils.SaessacFTP;
import com.saessac.activities.main.MainActivity;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiaryStatsActivity extends AppCompatActivity {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int theme = sharedPreferences.getInt("Theme", 0);

        switch (theme) {
            case 0:
                setContentView(R.layout.activity_number_of_diaries_theme_0);
                break;
            case 1:
                setContentView(R.layout.activity_number_of_diaries_theme_1);
                break;
        }

        String today = SaessacUI.date;
        String month = today.substring(4, 5);
        String uuid = SaessacUserData.uuid;

        // 다이어리 작성 개수
        executorService.execute(() -> {
            try {
                int[] diaryNum = SaessacFTP.getDiaryCount(month, uuid).get();
                runOnUiThread(() -> {
                    ((EditText) findViewById(R.id.diarytotal_txt)).setText(String.valueOf(diaryNum[0]));
                    ((EditText) findViewById(R.id.diarymonth_txt)).setText(String.valueOf(diaryNum[1]));
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // [ 버튼 - 확인 ]
        ImageButton checkBTN = findViewById(R.id.check_btn);
        checkBTN.setOnClickListener(v -> {
            SaessacUI.openActivity(this, MainActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
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
