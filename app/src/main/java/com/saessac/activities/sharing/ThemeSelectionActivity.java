package com.saessac.activities.sharing;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import com.saessac.R;
import com.saessac.utils.SaessacUI;
import com.saessac.activities.main.MainActivity;

public class ThemeSelectionActivity extends AppCompatActivity {
    private int selectedTheme = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int theme = sharedPreferences.getInt("Theme", 0);

        switch (theme) {
            case 0:
                setContentView(R.layout.activity_settheme_theme_0);
                break;
            case 1:
                setContentView(R.layout.activity_settheme_theme_1);
                break;
        }

        ImageButton basicBTN = findViewById(R.id.basic_btn);
        ImageButton modernBTN = findViewById(R.id.modern_btn);
        ImageView preview1 = findViewById(R.id.preview_1);
        ImageView preview2 = findViewById(R.id.preview_2);

        if (theme == 0) {
            preview1.setImageResource(R.drawable.preview_picture_3);
            preview2.setImageResource(R.drawable.preview_picture_2);
            basicBTN.setAlpha(1f);
            modernBTN.setAlpha(0.2f);
            selectedTheme = 0;
        } else {
            preview1.setImageResource(R.drawable.dad_preview_picture_3);
            preview2.setImageResource(R.drawable.dad_preview_picture_2);
            basicBTN.setAlpha(0.2f);
            modernBTN.setAlpha(1f);
            selectedTheme = 1;
        }

        // [ 버튼 - 테마 선택 ]
        basicBTN.setOnClickListener(v -> {
            preview1.setImageResource(R.drawable.preview_picture_3);
            preview2.setImageResource(R.drawable.preview_picture_2);
            basicBTN.setAlpha(1f);
            modernBTN.setAlpha(0.2f);
            selectedTheme = 0;
        });

        modernBTN.setOnClickListener(v -> {
            preview1.setImageResource(R.drawable.dad_preview_picture_3);
            preview2.setImageResource(R.drawable.dad_preview_picture_2);
            basicBTN.setAlpha(0.2f);
            modernBTN.setAlpha(1f);
            selectedTheme = 1;
        });

        // [ 버튼 - 테마 적용 ]
        ImageButton applyBTN = findViewById(R.id.apply_btn);
        applyBTN.setOnClickListener(v -> {
            Dialog dialog = new Dialog(this);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            switch (theme) {
                case 0:
                    dialog.setContentView(R.layout.dialog_apply_theme_0);
                    break;
                case 1:
                    dialog.setContentView(R.layout.dialog_apply_theme_1);
                    break;
            }
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            ImageButton yesBTN = dialog.findViewById(R.id.yes_btn);
            ImageButton noBTN = dialog.findViewById(R.id.no_btn);

            yesBTN.setOnClickListener(view -> {
                android.content.SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                android.content.SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("Theme", selectedTheme);
                editor.apply();
                SaessacUI.openActivity(this, MainActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
            });

            noBTN.setOnClickListener(view -> dialog.dismiss());
        });

        // 하단 테마 창 정의
        SaessacUI.touchBTN(this, R.anim.slide_in_right, R.anim.slide_out_left);

        // 뒤로 가기 버튼 처리
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Do nothing - 뒤로 가기 버튼 비활성화
            }
        });
    }
}
