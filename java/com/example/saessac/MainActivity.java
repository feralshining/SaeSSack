package com.example.saessac;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.saessac.SaessacUI;

public class MainActivity extends AppCompatActivity {
    private long _lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.content.SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int theme = sharedPreferences.getInt("Theme", 0);

        if (theme == 0) {
            setContentView(R.layout.activity_main_theme_0);
        } else if (theme == 1) {
            setContentView(R.layout.activity_main_theme_1);
        }
        // 하단 테마 창 정의
        SaessacUI.TouchBTN(this, R.anim.slide_in_right, R.anim.slide_out_left);

        // [     창 오픈 - 투두리스트 & 다이어리     ]
        CalendarView myCalendarView = (CalendarView) findViewById(R.id.maincalendar);
        myCalendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            if (IsDoubleClicked()) {
                SaessacUI.date = String.valueOf(year) + String.valueOf(month + 1) + String.valueOf(dayOfMonth);

                Dialog dialog = new Dialog(this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                if (theme == 0) {
                    dialog.setContentView(R.layout.dialog_select_window_theme_0);
                } else if (theme == 1) {
                    dialog.setContentView(R.layout.dialog_select_window_theme_1);
                }

                dialog.show();

                ImageButton todolistBTN = (ImageButton) dialog.findViewById(R.id.todolist_btn);
                ImageButton diaryBTN = (ImageButton) dialog.findViewById(R.id.diary_btn);
                ImageButton exitBTN = (ImageButton) dialog.findViewById(R.id.exit_btn);

                // 창 오픈 - 투두리스트
                todolistBTN.setOnClickListener(v -> {
                    SaessacUI.OpenActivity(this, WriteTodoListActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
                    dialog.dismiss();
                });

                // 창 오픈 - 다이어리
                diaryBTN.setOnClickListener(v -> {
                    SaessacUI.OpenActivity(this, WriteDiaryActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
                    dialog.dismiss();
                });

                // 다이얼로그 종료
                exitBTN.setOnClickListener(v -> {
                    dialog.dismiss();
                });
            }
        });
    }

    /**
     * 더블 클릭을 감지하는 메소드입니다.
     * 300ms 미만일 경우 더블 클릭으로 인식합니다. (1초 = 1000ms)
     */
    private boolean IsDoubleClicked() {
        long clickTime = System.currentTimeMillis();
        if (clickTime - _lastClickTime < 300) return true;
        _lastClickTime = clickTime;
        return false;
    }

    @Override
    public void onBackPressed() {
    }
}