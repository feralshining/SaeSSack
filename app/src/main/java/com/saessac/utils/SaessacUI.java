package com.saessac.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import com.saessac.R;
import com.saessac.activities.main.MainActivity;
import com.saessac.activities.sharing.BabyInfoActivity;
import com.saessac.activities.settings.SettingActivity;
import com.saessac.activities.sharing.ThemeSelectionActivity;
import com.saessac.activities.event.EventActivity;
import java.util.HashMap;
import java.util.Map;

public class SaessacUI {
    public static String date;
    public static int theme = 0;

    /**
     * 입력받은 액티비티를 실행하는 메소드입니다.
     */
    public static void openActivity(Context context, Class<?> activityClass, int enterAnim, int exitAnim) {
        Intent intent = new Intent(context, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(context, enterAnim, exitAnim);
        context.startActivity(intent, options.toBundle());
    }

    /**
     * 하단 테마 창의 버튼을 정의하고 클릭 시 창을 띄우는 메소드입니다.
     */
    public static void touchBTN(AppCompatActivity activity, int enterAnim, int exitAnim) {
        Map<Integer, Class<?>> btnMap = new HashMap<>();
        btnMap.put(R.id.home_btn, MainActivity.class);
        btnMap.put(R.id.babyinfo_btn, BabyInfoActivity.class);
        btnMap.put(R.id.setting_btn, SettingActivity.class);
        btnMap.put(R.id.theme_btn, ThemeSelectionActivity.class);
        btnMap.put(R.id.event_btn, EventActivity.class);

        for (Map.Entry<Integer, Class<?>> entry : btnMap.entrySet()) {
            ImageButton button = activity.findViewById(entry.getKey());
            button.setOnClickListener(v -> {
                Intent intent = new Intent(activity, entry.getValue());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(activity, enterAnim,
                        exitAnim);
                activity.startActivity(intent, options.toBundle());
            });
        }
    }

    /**
     * 입력받은 메세지를 토스트 안내창으로 표시합니다.
     */
    public static void showText(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 음악을 백그라운드에서 재생합니다.
     */
    public static void playMusic(Context context) {
        if (MediaPlayerManager.player == null) {
            MediaPlayerManager.player = MediaPlayer.create(context, R.raw.background_bgm);
            MediaPlayerManager.player.setLooping(true);
        }
        MediaPlayerManager.player.start();
    }

    /**
     * 재생하고 있는 음악을 중지하고 리소스를 해제합니다.
     */
    public static void stopMusic() {
        if (MediaPlayerManager.player != null) {
            MediaPlayerManager.player.release();
            MediaPlayerManager.player = null;
        }
    }
}
