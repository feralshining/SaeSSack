package com.saessac.activities.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.saessac.R;
import com.saessac.activities.main.MainActivity;
import com.saessac.utils.SaessacUI;
import com.saessac.utils.SaessacUserData;

import java.util.UUID;

public class AuthActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("com.saessac.preferences", Context.MODE_PRIVATE);

        UUID uuid = loadUUID();
        if (uuid == null) { // 앱을 처음 실행하는 경우
            uuid = UUID.randomUUID();
            saveUUID(uuid);
        }
        SaessacUserData.uuid = uuid.toString();
        SaessacUI.openActivity(this, MainActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * sharedPreferences에 저장된 uuid 값을 로드하여 UUID 객체로 반환합니다.
     */
    private UUID loadUUID() {
        String uuidString = sharedPreferences.getString("uuid", null);
        if (uuidString != null) {
            return UUID.fromString(uuidString);
        }
        return null;
    }

    /**
     * sharedPreferences에 새로운 생성된 UUID 값을 저장하고 저장합니다.
     */
    private void saveUUID(UUID uuid) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("uuid", uuid.toString());
        editor.apply();
    }
}
