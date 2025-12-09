package com.saessac;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.ImageButton;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.content.SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int theme = sharedPreferences.getInt("Theme", 0);

        switch (theme) {
            case 0:
                setContentView(R.layout.activity_main_theme_0);
                break;
            case 1:
                setContentView(R.layout.activity_main_theme_1);
                break;
        }

        // 하단 테마 창 정의
        SaessacUI.touchBTN(this, R.anim.slide_in_right, R.anim.slide_out_left);

        // 창 오픈 - 투두리스트 & 다이어리
        CalendarView myCalendarView = findViewById(R.id.maincalendar);
        myCalendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            if (isDoubleClicked()) {
                SaessacUI.date = year + "" + (month + 1) + dayOfMonth;

                Dialog dialog = new Dialog(this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                switch (theme) {
                    case 0:
                        dialog.setContentView(R.layout.dialog_select_window_theme_0);
                        break;
                    case 1:
                        dialog.setContentView(R.layout.dialog_select_window_theme_1);
                        break;
                }

                dialog.show();

                ImageButton todolistBTN = dialog.findViewById(R.id.todolist_btn);
                ImageButton diaryBTN = dialog.findViewById(R.id.diary_btn);
                ImageButton exitBTN = dialog.findViewById(R.id.exit_btn);

                // 창 오픈 - 투두리스트
                todolistBTN.setOnClickListener(v -> {
                    SaessacUI.openActivity(this, WriteTodoListActivity.class, R.anim.slide_in_right,
                            R.anim.slide_out_left);
                    dialog.dismiss();
                });

                // 창 오픈 - 다이어리
                diaryBTN.setOnClickListener(v -> {
                    SaessacUI.openActivity(this, WriteDiaryActivity.class, R.anim.slide_in_right,
                            R.anim.slide_out_left);
                    dialog.dismiss();
                });

                // 다이얼로그 종료
                exitBTN.setOnClickListener(v -> dialog.dismiss());
            }
        });

        // Back press handling
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Do nothing - disable back button
            }
        });
    }

    /**
     * 더블 클릭을 감지하는 메소드입니다.
     * 300ms 미만일 경우 더블 클릭으로 인식합니다. (1초 = 1000ms)
     */
    private boolean isDoubleClicked() {
        long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime < 300) {
            return true;
        }
        lastClickTime = clickTime;
        return false;
    }
}
