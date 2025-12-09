package com.saessac.activities.sharing;

import android.content.Context;
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
import com.saessac.activities.settings.SettingActivity;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FamilySharingActivity extends AppCompatActivity {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int theme = sharedPreferences.getInt("Theme", 0);

        switch (theme) {
            case 0:
                setContentView(R.layout.activity_sharing_theme_0);
                break;
            case 1:
                setContentView(R.layout.activity_sharing_theme_1);
                break;
        }

        // [ 버튼 - 뒤로 가기 ]
        ImageButton previousBTN = findViewById(R.id.previous_btn);
        previousBTN.setOnClickListener(v -> {
            SaessacUI.openActivity(this, SettingActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // 내 UUID 표시
        EditText myuuidTXT = findViewById(R.id.my_uuid);
        myuuidTXT.setText(SaessacUserData.uuid);

        // [ 버튼 - 공유하기 ]
        ImageButton shareBTN = findViewById(R.id.sharing_btn);
        shareBTN.setOnClickListener(v -> {
            EditText shareuuidTXT = findViewById(R.id.share_uuid);
            String shareUUID = shareuuidTXT.getText().toString();
            if (shareUUID.isEmpty()) {
                return;
            }

            executorService.execute(() -> {
                try {
                    boolean isValid = SaessacFTP.isValid(shareUUID).get();
                    if (!isValid) {
                        runOnUiThread(() -> {
                            SaessacUI.showText(FamilySharingActivity.this, "유효하지 않은 UUID입니다.");
                        });
                        return;
                    }

                    runOnUiThread(() -> {
                        saveUUID(UUID.fromString(shareUUID));
                        SaessacUserData.uuid = shareUUID;
                        SaessacUI.showText(FamilySharingActivity.this, "공유가 완료되었습니다.");
                        SaessacUI.openActivity(FamilySharingActivity.this, MainActivity.class, R.anim.slide_in_right,
                                R.anim.slide_out_left);
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        SaessacUI.showText(FamilySharingActivity.this, "오류가 발생했습니다.");
                    });
                }
            });
        });
    }

    /**
     * sharedPreferences에 본인의 UUID 값을 공유받을 UUID 값으로 변경하고 저장합니다.
     */
    private void saveUUID(UUID uuid) {
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("com.saessac.preferences",
                Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uuid", uuid.toString());
        editor.apply();
    }

    // onBackPressed 메서드는 제거되었습니다 - onCreate에서 OnBackPressedCallback을 사용하세요
}
