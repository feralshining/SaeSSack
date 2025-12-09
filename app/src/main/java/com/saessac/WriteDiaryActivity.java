package com.saessac;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WriteDiaryActivity extends AppCompatActivity {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int theme = sharedPreferences.getInt("Theme", 0);

        switch (theme) {
            case 0:
                setContentView(R.layout.activity_diary_theme_0);
                break;
            case 1:
                setContentView(R.layout.activity_diary_theme_1);
                break;
        }

        String today = SaessacUI.date;
        String fileName = SaessacUserData.uuid + "_" + today + ".txt";
        String filePath = new File(getBaseContext().getFilesDir(), fileName).toString();

        // 다이어리 로드
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        switch (theme) {
            case 0:
                dialog.setContentView(R.layout.dialog_waiting_theme_0);
                break;
            case 1:
                dialog.setContentView(R.layout.dialog_waiting_theme_1);
                break;
        }
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        EditText diary = findViewById(R.id.write_txt);
        executorService.execute(() -> {
            try {
                boolean isSuccess = SaessacFTP.downloadFile(SaessacFTP.DIARY_DIR + fileName, filePath).get();
                runOnUiThread(() -> {
                    if (isSuccess) {
                        diary.setText(SaessacUserData.loadDiary(filePath));
                        Extensions.deleteFile(filePath);
                    } else {
                        diary.setHint("해당 날짜에 다이어리가 존재하지 않습니다.");
                    }
                    dialog.dismiss();
                });
            } catch (Exception e) {
                runOnUiThread(() -> dialog.dismiss());
            }
        });

        // 브금 설정 값 로드
        JsonObject jsonData = SaessacUserData.loadJSON(getBaseContext().getFilesDir().toString() + "/" + "Config.json");
        if (jsonData != null && jsonData.has("bgm_toggle")) {
            boolean isBGM = jsonData.get("bgm_toggle").getAsBoolean();
            if (isBGM) {
                SaessacUI.playMusic(this);
            } else {
                SaessacUI.stopMusic();
            }
        }

        // 버튼 - 뒤로 가기
        ImageButton previousBTN = findViewById(R.id.previous_btn);
        previousBTN.setOnClickListener(v -> {
            SaessacUI.openActivity(this, MainActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // 버튼 - 작성 완료
        ImageButton saveBTN = findViewById(R.id.save_btn);
        saveBTN.setOnClickListener(v -> {
            String data = ((EditText) findViewById(R.id.write_txt)).getText().toString();
            executorService.execute(() -> {
                Extensions.createFile(getBaseContext().getFilesDir().toString(), fileName, data);
                try {
                    boolean isSuccess = SaessacFTP.uploadFile(filePath, SaessacFTP.DIARY_DIR + fileName).get();
                    Extensions.deleteFile(filePath);
                    runOnUiThread(() -> {
                        if (isSuccess) {
                            SaessacUI.openActivity(WriteDiaryActivity.this, DiaryCountCheckActivity.class,
                                    R.anim.slide_in_right, R.anim.slide_out_left);
                        } else {
                            Toast.makeText(WriteDiaryActivity.this, "일기를 저장할 수 없습니다. 문제가 지속되면 관리자에게 문의하세요.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(WriteDiaryActivity.this, "일기를 저장할 수 없습니다. 문제가 지속되면 관리자에게 문의하세요.",
                                Toast.LENGTH_SHORT).show();
                    });
                }
            });
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SaessacUI.stopMusic();
    }
}
