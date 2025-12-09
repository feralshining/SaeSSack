package com.example.saessac;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.saessac.Extensions;
import com.example.saessac.SaessacUserData;
import com.example.saessac.SaessacFTP;
import com.example.saessac.SaessacUI;
import java.io.File;

public class WriteDiaryActivity extends AppCompatActivity {
    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int theme = sharedPreferences.getInt("Theme", 0);
        if (theme == 0) {
            setContentView(R.layout.activity_diary_theme_0);
        } else if (theme == 1) {
            setContentView(R.layout.activity_diary_theme_1);
        }

        String today = SaessacUI.date;
        String fileName = SaessacUserData.uuid + "_" + today + ".txt";
        String filePath = new File(getBaseContext().getFilesDir(), fileName).toString();

        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (theme == 0) {
            dialog.setContentView(R.layout.dialog_waiting_theme_0);
        } else if (theme == 1) {
            dialog.setContentView(R.layout.dialog_waiting_theme_1);
        }
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        new Thread(() -> {
            EditText diary = findViewById(R.id.write_txt);
            boolean isSuccess = SaessacFTP.DownloadFile(SaessacFTP.DIARY_DIR + fileName, filePath);
            if (isSuccess) {
                runOnUiThread(() -> {
                    diary.setText(SaessacUserData.LoadDiary(filePath));
                    Extensions.DeleteFile(filePath);
                    dialog.dismiss();
                });
            } else {
                runOnUiThread(() -> {
                    diary.setHint("해당 날짜의 다이어리가 존재하지 않습니다.");
                    dialog.dismiss();
                });
            }
        }).start();

        com.google.gson.JsonObject jsonData = SaessacUserData.LoadJSON(getBaseContext().getFilesDir().toString() + "/" + "Config.json");
        Boolean isBGM = jsonData != null ? jsonData.get("bgm_toggle").getAsBoolean() : null;
        if (isBGM != null && isBGM) {
            SaessacUI.PlayMusic(this);
        } else {
            SaessacUI.StopMusic();
        }

        ImageButton previousBTN = findViewById(R.id.previous_btn);
        previousBTN.setOnClickListener(v -> {
            SaessacUI.OpenActivity(this, MainActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
        });

        ImageButton saveBTN = findViewById(R.id.save_btn);
        saveBTN.setOnClickListener(v -> {
            String data = ((EditText) findViewById(R.id.write_txt)).getText().toString();
            new Thread(() -> {
                Extensions.CreateFile(getBaseContext().getFilesDir().toString(), fileName, data);
                boolean isSuccess = SaessacFTP.UploadFile(filePath, SaessacFTP.DIARY_DIR + fileName);
                Extensions.DeleteFile(filePath);
                if (isSuccess) {
                    runOnUiThread(() -> {
                        SaessacUI.OpenActivity(WriteDiaryActivity.this, DiaryCountCheckActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(WriteDiaryActivity.this, "일기를 저장할 수 없습니다. 문제가 지속되면 관리자에게 문의하세요.", Toast.LENGTH_SHORT).show();
                    });
                }
            }).start();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SaessacUI.StopMusic();
    }

    @Override
    public void onBackPressed() {
        SaessacUI.OpenActivity(this, MainActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
    }
}