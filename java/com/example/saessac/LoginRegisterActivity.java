package com.example.saessac;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.saessac.SaessacUserData;
import com.example.saessac.SaessacUI;
import java.util.UUID;

public class LoginRegisterActivity extends AppCompatActivity {
    private SharedPreferences _sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _sharedPreferences = getSharedPreferences("com.saessac.preferences", Context.MODE_PRIVATE);

        UUID uuid = LoadUUID();
        if (uuid == null) { // 앱을 처음 실행한 유저
            uuid = UUID.randomUUID();
            SaveUUID(uuid);
        }
        SaessacUserData.uuid = uuid.toString();
        SaessacUI.OpenActivity(this, MainActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * sharedPreferences에 저장된 uuid 값을 로드해서 UUID 객체로 반환합니다.
     */
    private UUID LoadUUID() {
        String uuidString = _sharedPreferences.getString("uuid", null);
        return uuidString != null ? UUID.fromString(uuidString) : null;
    }

    /**
     * sharedPreferences에 랜덤으로 생성한 UUID 값을 작성하고 저장합니다.
     */
    private void SaveUUID(UUID uuid) {
        SharedPreferences.Editor editor = _sharedPreferences.edit();
        editor.putString("uuid", uuid.toString());
        editor.apply();
    }
}