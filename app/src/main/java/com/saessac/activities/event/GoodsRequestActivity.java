package com.saessac.activities.event;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.saessac.R;
import com.saessac.utils.SaessacUI;
import com.saessac.utils.SaessacUserData;
import com.saessac.utils.SaessacFTP;
import com.saessac.utils.Extensions;
import com.saessac.activities.main.MainActivity;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GoodsRequestActivity extends AppCompatActivity {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int theme = sharedPreferences.getInt("Theme", 0);

        switch (theme) {
            case 0:
                setContentView(R.layout.activity_good_request_theme_0);
                break;
            case 1:
                setContentView(R.layout.activity_good_request_theme_1);
                break;
        }

        String uuid = SaessacUserData.uuid;

        // [ 버튼 - 뒤로 가기 ]
        ImageButton previousBTN = findViewById(R.id.previous_btn);
        previousBTN.setOnClickListener(v -> {
            SaessacUI.openActivity(this, EventActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // [ 버튼 - 굿즈 신청 ]
        ImageButton requestBTN = findViewById(R.id.request_btn);
        requestBTN.setOnClickListener(v -> {
            String name = ((EditText) findViewById(R.id.name_txt)).getText().toString();
            String contact = ((EditText) findViewById(R.id.contact_txt)).getText().toString();
            String email = ((EditText) findViewById(R.id.email_txt)).getText().toString();
            String address = ((EditText) findViewById(R.id.address_txt)).getText().toString();

            if (name.isEmpty() || contact.isEmpty() || email.isEmpty() || address.isEmpty()) {
                SaessacUI.showText(this, "빈 칸 없이 작성해주세요.");
                return;
            }

            boolean hasSelectedGoods = ((CheckBox) findViewById(R.id.cb_letter)).isChecked() ||
                    ((CheckBox) findViewById(R.id.cb_keyring)).isChecked() ||
                    ((CheckBox) findViewById(R.id.cb_diary)).isChecked() ||
                    ((CheckBox) findViewById(R.id.cb_sticker)).isChecked() ||
                    ((CheckBox) findViewById(R.id.cb_crayon)).isChecked() ||
                    ((CheckBox) findViewById(R.id.cb_balcro)).isChecked();

            if (!hasSelectedGoods) {
                SaessacUI.showText(this, "굿즈를 하나 이상 선택해주세요.");
                return;
            }

            LinkedHashMap<String, Object> data = new LinkedHashMap<>();
            data.put("이름", name);
            data.put("연락처", contact);
            data.put("이메일", email);
            data.put("배송지", address);
            data.put("굿즈 - 편지지", ((CheckBox) findViewById(R.id.cb_letter)).isChecked());
            data.put("굿즈 - 키링", ((CheckBox) findViewById(R.id.cb_keyring)).isChecked());
            data.put("굿즈 - 다이어리", ((CheckBox) findViewById(R.id.cb_diary)).isChecked());
            data.put("굿즈 - 스티커", ((CheckBox) findViewById(R.id.cb_sticker)).isChecked());
            data.put("굿즈 - 크레파스", ((CheckBox) findViewById(R.id.cb_crayon)).isChecked());
            data.put("굿즈 - 벨크로", ((CheckBox) findViewById(R.id.cb_balcro)).isChecked());

            String fileName = uuid + "_request.txt";
            String filePath = new File(getBaseContext().getFilesDir(), fileName).toString();

            Extensions.createFile(getBaseContext().getFilesDir().toString(), fileName, new Gson().toJson(data));

            executorService.execute(() -> {
                try {
                    int count = SaessacFTP.getGoodsCount(uuid).get();
                    if (count > 0) {
                        runOnUiThread(() -> {
                            SaessacUI.showText(GoodsRequestActivity.this, "이미 굿즈 신청하셨습니다.");
                        });
                        return;
                    }

                    boolean isSuccess = SaessacFTP.uploadFile(filePath, SaessacFTP.GOODSREQUEST_DIR + fileName).get();
                    runOnUiThread(() -> {
                        if (isSuccess) {
                            Extensions.deleteFile(filePath);
                            SaessacUI.showText(GoodsRequestActivity.this, "신청이 완료되었습니다.");
                            SaessacUI.openActivity(GoodsRequestActivity.this, EventActivity.class,
                                    R.anim.slide_in_right, R.anim.slide_out_left);
                        } else {
                            SaessacUI.showText(GoodsRequestActivity.this,
                                    "비정상적으로 굿즈 신청에 실패했습니다. 문제가 지속되면 관리자에게 문의하세요.");
                        }
                    });
                    Extensions.deleteFile(filePath);
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        SaessacUI.showText(GoodsRequestActivity.this,
                                "비정상적으로 굿즈 신청에 실패했습니다. 문제가 지속되면 관리자에게 문의하세요.");
                    });
                }
            });
        });
    }

    // onBackPressed 메서드는 제거되었습니다 - onCreate에서 OnBackPressedCallback을 사용하세요
}