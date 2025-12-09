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
import androidx.appcompat.app.AppCompatActivity;
import com.example.saessac.Extensions;
import com.example.saessac.SaessacUI;
import com.example.saessac.SaessacUserData;
import com.example.saessac.SaessacFTP;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;
import java.util.List;

public class WriteTodoListActivity extends AppCompatActivity {

    private int y_value = 100;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int theme = sharedPreferences.getInt("Theme", 0);

        if (theme == 0) {
            setContentView(R.layout.activity_todolist_writing_theme_0);
        } else if (theme == 1) {
            setContentView(R.layout.activity_todolist_writing_theme_1);
        }

        String today = SaessacUI.date;
        String fileName = SaessacUserData.uuid + "_" + today + ".txt";
        String filePath = new File(getBaseContext().getFilesDir(), fileName).toString();
        FrameLayout container = findViewById(R.id.container);

        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (theme == 0) {
            dialog.setContentView(R.layout.dialog_waiting_theme_0);
        } else if (theme == 1) {
            dialog.setContentView(R.layout.dialog_waiting_theme_1);
        }

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        executorService.execute(() -> {
            try {
                boolean isSuccess = SaessacFTP.DownloadFile(SaessacFTP.TODOLIST_DIR + fileName, filePath);
                if (!isSuccess) {
                    runOnUiThread(() -> dialog.dismiss());
                    return;
                }
                runOnUiThread(() -> {
                    JsonObject json = SaessacUserData.LoadJSON(filePath);
                    TypeToken<LinkedHashMap<String, String>> type = new TypeToken<LinkedHashMap<String, String>>() {};
                    LinkedHashMap<String, String> data = new Gson().fromJson(json, type.getType());
                    y_value = 100;
                    for (String task : data.values()) {
                        CheckBox cb = new CheckBox(this);
                        cb.setText(task);
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                        params.topMargin = y_value;
                        cb.setLayoutParams(params);
                        container.addView(cb);
                        y_value += 100;
                        Extensions.DeleteFile(filePath);
                    }
                    dialog.dismiss();
                });
            } catch (Exception e) {
                // Do Nothing
            }
        });

        JsonObject jsonData = SaessacUserData.LoadJSON(getBaseContext().getFilesDir().toString() + "/" + "Config.json");
        Boolean isBGM = jsonData != null ? jsonData.get("bgm_toggle").getAsBoolean() : null;
        if (isBGM != null && isBGM) {
            SaessacUI.PlayMusic(this);
        } else {
            SaessacUI.StopMusic();
        }

        ImageButton previousBTN = findViewById(R.id.previous_btn);
        previousBTN.setOnClickListener(v -> {
            SaessacUI.OpenActivity(this, MainActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
        });

        ImageButton addBTN = findViewById(R.id.add_btn);
        EditText inputTXT = findViewById(R.id.input_txt);
        addBTN.setOnClickListener(v -> {
            String text = inputTXT.getText().toString();
            if (text.isEmpty()) {
                SaessacUI.ShowText(this, "할 일을 하나 이상 적어주세요.");
                return;
            }
            if (y_value >= 1300) {
                SaessacUI.ShowText(this, "할 일 개수는 최대 12개까지 추가 가능합니다.");
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
            Extensions.CreateFile(getBaseContext().getFilesDir().toString(), fileName, new Gson().toJson(data));

            executorService.execute(() -> {
                boolean isSuccess = SaessacFTP.UploadFile(filePath, SaessacFTP.TODOLIST_DIR + fileName);
                Extensions.DeleteFile(filePath);
                if (isSuccess) {
                    runOnUiThread(() -> {
                        Extensions.DeleteFile(filePath);
                        CheckBox checkBox = new CheckBox(this);
                        checkBox.setText(text);
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                        params.topMargin = y_value;
                        checkBox.setLayoutParams(params);
                        container.addView(checkBox);
                        y_value += 100;
                    });
                } else {
                    runOnUiThread(() -> {
                        SaessacUI.ShowText(this, "변경사항을 저장할 수 없습니다. 문제가 지속되면 관리자에게 문의하세요.");
                    });
                }
            });
        });

        ImageButton deleteBTN = findViewById(R.id.delete_btn);
        deleteBTN.setOnClickListener(v -> {
            int count = container.getChildCount();
            List<CheckBox> toBeDeleted = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                android.view.View view = container.getChildAt(i);
                if (view instanceof CheckBox && ((CheckBox) view).isChecked()) {
                    toBeDeleted.add((CheckBox) view);
                }
            }
            for (CheckBox checkBox : toBeDeleted) {
                container.removeView(checkBox);
            }
            y_value = 100;
            LinkedHashMap<String, String> data = new LinkedHashMap<>();
            for (int i = 0; i < container.getChildCount(); i++) {
                android.view.View view = container.getChildAt(i);
                if (view instanceof CheckBox) {
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    params.topMargin = y_value;
                    view.setLayoutParams(params);
                    String key = (i + 1) + "번";
                    String value = ((CheckBox) view).getText().toString();
                    data.put(key, value);
                    y_value += 100;
                }
            }
            executorService.execute(() -> {
                Extensions.CreateFile(getBaseContext().getFilesDir().toString(), fileName, new Gson().toJson(data));
                boolean is_success = SaessacFTP.UploadFile(filePath, SaessacFTP.TODOLIST_DIR + fileName);
                Extensions.DeleteFile(filePath);
                if (is_success) {
                    runOnUiThread(() -> {
                        Extensions.DeleteFile(filePath);
                    });
                } else {
                    runOnUiThread(() -> {
                        SaessacUI.ShowText(this, "변경사항을 저장할 수 없습니다. 문제가 지속되면 관리자에게 문의하세요.");
                    });
                }
            });
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SaessacUI.StopMusic();
    }

    @Override
    public void onBackPressed() {
        SaessacUI.OpenActivity(this, MainActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}