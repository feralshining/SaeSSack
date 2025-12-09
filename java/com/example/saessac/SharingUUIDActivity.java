package com.saessac;

import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.saessac.Extensions;
import com.example.saessac.SaessacFTP;
import com.example.saessac.SaessacUI;
import com.example.saessac.SaessacUserData;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SharingUUIDActivity extends AppCompatActivity {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int theme = sharedPreferences.getInt("Theme", 0);

        if (theme == 0) {
            setContentView(R.layout.activity_sharing_theme_0);
        } else if (theme == 1) {
            setContentView(R.layout.activity_sharing_theme_1);
        }

        ImageButton previousBTN = findViewById(R.id.previous_btn);
        previousBTN.setOnClickListener(v -> {
            SaessacUI.OpenActivity(this, SettingActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
        });

        EditText myuuidTXT = findViewById(R.id.my_uuid);
        myuuidTXT.setText(SaessacUserData.uuid);

        ImageButton shareBTN = findViewById(R.id.sharing_btn);
        shareBTN.setOnClickListener(v -> {
            EditText shareuuidTXT = findViewById(R.id.share_uuid);
            String shareUUID = shareuuidTXT.getText().toString();
            if (shareUUID.isEmpty()) return;
            executorService.execute(() -> {
                if (!SaessacFTP.Isvalid(shareUUID)) {
                    runOnUiThread(() -> SaessacUI.ShowText(this, "유효하지 않은 UUID입니다."));
                    return;
                }
                runOnUiThread(() -> {
                    SaveUUID(UUID.fromString(shareUUID));
                    SaessacUserData.uuid = shareUUID;
                    SaessacUI.ShowText(this, "공유가 완료되었습니다!");
                    SaessacUI.OpenActivity(this, MainActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
                });
            });
        });
    }

    private void SaveUUID(UUID uuid) {
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("com.saessac.preferences", Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uuid", uuid.toString());
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        SaessacUI.OpenActivity(this, SettingActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}