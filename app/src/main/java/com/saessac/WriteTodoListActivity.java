package com.saessac;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WriteTodoListActivity extends AppCompatActivity {
    private int yValue = 100;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int theme = sharedPreferences.getInt("Theme", 0);

        switch (theme) {
            case 0:
                setContentView(R.layout.activity_todolist_writing_theme_0);
                break;
            case 1:
                setContentView(R.layout.activity_todolist_writing_theme_1);
                break;
        }

        String today = SaessacUI.date;
        String fileName = SaessacUserData.uuid + "_" + today + ".txt";
        String filePath = new File(getBaseContext().getFilesDir(), fileName).toString();
        FrameLayout container = findViewById(R.id.container);

        // 투두리스트 로드
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        switch (theme) {
            case 0:
                dialog.setContentView(R.layout.dialog_waiting_theme_0);
                break;
            case 1:
                dialog.setContentView(R.layout.dialog_waiting_theme_1);
                break;
        }
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        executorService.execute(() -> {
            try {
                boolean isSuccess = SaessacFTP.downloadFile(SaessacFTP.TODOLIST_DIR + fileName, filePath).get();
                if (!isSuccess) {
                    runOnUiThread(() -> dialog.dismiss());
                    return;
                }

                JsonObject json = SaessacUserData.loadJSON(filePath);
                TypeToken<LinkedHashMap<String, String>> type = new TypeToken<LinkedHashMap<String, String>>() {
                };
                LinkedHashMap<String, String> data = new Gson().fromJson(json, type.getType());

                runOnUiThread(() -> {
                    yValue = 100;
                    for (String task : data.values()) {
                        CheckBox cb = new CheckBox(WriteTodoListActivity.this);
                        cb.setText(task);
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.WRAP_CONTENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT);
                        params.topMargin = yValue;
                        cb.setLayoutParams(params);
                        container.addView(cb);
                        yValue += 100;
                    }
                    Extensions.deleteFile(filePath);
                    dialog.dismiss();
                });
            } catch (Exception e) {
                runOnUiThread(() -> dialog.dismiss());
            }
        });

        // 브금 설정 값 로드
        JsonObject jsonData = SaessacUserData.loadJSON(getBaseContext().getFilesDir().toString() + "/" + "Config.json");
        if (jsonData != null && jsonData.has("bgm_toggle")) {
            boolean isBGM = jsonData.get("bgm_toggle").getAsBoolean();
            if (isBGM) {
                SaessacUI.playMusic(this);
            } else {
                SaessacUI.stopMusic();
            }
        }

        // 버튼 - 뒤로 가기
        ImageButton previousBTN = findViewById(R.id.previous_btn);
        previousBTN.setOnClickListener(v -> {
            SaessacUI.openActivity(this, MainActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // 버튼 - 항목 추가
        ImageButton addBTN = findViewById(R.id.add_btn);
        EditText inputTXT = findViewById(R.id.input_txt);
        addBTN.setOnClickListener(v -> {
            String text = inputTXT.getText().toString();
            if (text.isEmpty()) {
                SaessacUI.showText(this, "내용을 하나 이상 넣어주세요.");
                return;
            }
            if (yValue >= 1300) {
                SaessacUI.showText(this, "항목 개수는 최대 12개까지 추가 가능합니다.");
                return;
            }

            LinkedHashMap<String, String> data = new LinkedHashMap<>();
            for (int i = 0; i < container.getChildCount(); i++) {
                android.view.View view = container.getChildAt(i);
                if (view instanceof CheckBox) {
                    String key = (i + 1) + "번";
                    String value = ((CheckBox) view).getText().toString();
                    data.put(key, value);
                }
            }

            String newKey = (container.getChildCount() + 1) + "번";
            data.put(newKey, text);
            Extensions.createFile(getBaseContext().getFilesDir().toString(), fileName, new Gson().toJson(data));

            executorService.execute(() -> {
                try {
                    boolean isSuccess = SaessacFTP.uploadFile(filePath, SaessacFTP.TODOLIST_DIR + fileName).get();
                    Extensions.deleteFile(filePath);
                    runOnUiThread(() -> {
                        if (isSuccess) {
                            CheckBox checkBox = new CheckBox(WriteTodoListActivity.this);
                            checkBox.setText(text);
                            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                    FrameLayout.LayoutParams.WRAP_CONTENT,
                                    FrameLayout.LayoutParams.WRAP_CONTENT);
                            params.topMargin = yValue;
                            checkBox.setLayoutParams(params);
                            container.addView(checkBox);
                            yValue += 100;
                        } else {
                            SaessacUI.showText(WriteTodoListActivity.this, "변경사항을 저장할 수 없습니다. 문제가 지속되면 관리자에게 문의하세요.");
                        }
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        SaessacUI.showText(WriteTodoListActivity.this, "변경사항을 저장할 수 없습니다. 문제가 지속되면 관리자에게 문의하세요.");
                    });
                }
            });
        });

        // 버튼 - 항목 삭제
        ImageButton deleteBTN = findViewById(R.id.delete_btn);
        deleteBTN.setOnClickListener(v -> {
            int count = container.getChildCount();
            java.util.List<CheckBox> toBeDeleted = new java.util.ArrayList<>();
            for (int i = 0; i < count; i++) {
                android.view.View view = container.getChildAt(i);
                if (view instanceof CheckBox && ((CheckBox) view).isChecked()) {
                    toBeDeleted.add((CheckBox) view);
                }
            }

            for (CheckBox checkBox : toBeDeleted) {
                container.removeView(checkBox);
            }

            yValue = 100;
            LinkedHashMap<String, String> data = new LinkedHashMap<>();
            for (int i = 0; i < container.getChildCount(); i++) {
                android.view.View view = container.getChildAt(i);
                if (view instanceof CheckBox) {
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT);
                    params.topMargin = yValue;
                    view.setLayoutParams(params);
                    String key = (i + 1) + "번";
                    String value = ((CheckBox) view).getText().toString();
                    data.put(key, value);
                    yValue += 100;
                }
            }

            executorService.execute(() -> {
                Extensions.createFile(getBaseContext().getFilesDir().toString(), fileName, new Gson().toJson(data));
                try {
                    boolean isSuccess = SaessacFTP.uploadFile(filePath, SaessacFTP.TODOLIST_DIR + fileName).get();
                    Extensions.deleteFile(filePath);
                    if (!isSuccess) {
                        runOnUiThread(() -> {
                            SaessacUI.showText(WriteTodoListActivity.this, "변경사항을 저장할 수 없습니다. 문제가 지속되면 관리자에게 문의하세요.");
                        });
                    }
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        SaessacUI.showText(WriteTodoListActivity.this, "변경사항을 저장할 수 없습니다. 문제가 지속되면 관리자에게 문의하세요.");
                    });
                }
            });
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SaessacUI.stopMusic();
    }
}
