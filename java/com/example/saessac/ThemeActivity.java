package com.example.saessac;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.saessac.SaessacUI;

public class ThemeActivity extends AppCompatActivity {
    private int _theme = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int theme = sharedPreferences.getInt("Theme", 0);

        if (theme == 0) {
            setContentView(R.layout.activity_settheme_theme_0);
        } else if (theme == 1) {
            setContentView(R.layout.activity_settheme_theme_1);
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
            _theme = 0;
        } else {
            preview1.setImageResource(R.drawable.dad_preview_picture_3);
            preview2.setImageResource(R.drawable.dad_preview_picture_2);
            basicBTN.setAlpha(0.2f);
            modernBTN.setAlpha(1f);
            _theme = 1;
        }

        basicBTN.setOnClickListener(v -> {
            preview1.setImageResource(R.drawable.preview_picture_3);
            preview2.setImageResource(R.drawable.preview_picture_2);
            basicBTN.setAlpha(1f);
            modernBTN.setAlpha(0.2f);
            _theme = 0;
        });

        modernBTN.setOnClickListener(v -> {
            preview1.setImageResource(R.drawable.dad_preview_picture_3);
            preview2.setImageResource(R.drawable.dad_preview_picture_2);
            basicBTN.setAlpha(0.2f);
            modernBTN.setAlpha(1f);
            _theme = 1;
        });

        ImageButton applyBTN = findViewById(R.id.apply_btn);
        applyBTN.setOnClickListener(v -> {
            Dialog dialog = new Dialog(this);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (theme == 0) {
                dialog.setContentView(R.layout.dialog_apply_theme_0);
            } else if (theme == 1) {
                dialog.setContentView(R.layout.dialog_apply_theme_1);
            }
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            ImageButton yesBTN = dialog.findViewById(R.id.yes_btn);
            ImageButton noBTN = dialog.findViewById(R.id.no_btn);

            yesBTN.setOnClickListener(vv -> {
                android.content.SharedPreferences sharedPreferences1 = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                android.content.SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putInt("Theme", _theme);
                editor.apply();
                SaessacUI.OpenActivity(this, MainActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
            });

            noBTN.setOnClickListener(vv -> dialog.dismiss());
        });

        SaessacUI.TouchBTN(this, R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onBackPressed() {
    }
}