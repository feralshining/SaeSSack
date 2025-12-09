package com.example.saessac;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.saessac.Extensions;
import com.example.saessac.SaessacUI;
import com.example.saessac.SaessacUserData;
import com.google.gson.Gson;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class EventGoodRequestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int theme = sharedPreferences.getInt("Theme", 0);

        if (theme == 0) {
            setContentView(R.layout.activity_good_request_theme_0);
        } else if (theme == 1) {
            setContentView(R.layout.activity_good_request_theme_1);
        }
        String uuid = SaessacUserData.uuid;

        // [     버튼 - 뒤로 가기     ]
        ImageButton previousBTN = (ImageButton) findViewById(R.id.previous_btn);
        previousBTN.setOnClickListener(v -> {
            SaessacUI.OpenActivity(this, EventActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // [     버튼 - 굿즈 신청     ]
        ImageButton requestBTN = (ImageButton) findViewById(R.id.request_btn);
        requestBTN.setOnClickListener(v -> {
            String name = ((EditText) findViewById(R.id.name_txt)).getText().toString();
            String contact = ((EditText) findViewById(R.id.contact_txt)).getText().toString();
            String email = ((EditText) findViewById(R.id.email_txt)).getText().toString();
            String address = ((EditText) findViewById(R.id.address_txt)).getText().toString();
            Map<String, Boolean> goodsOptions = new LinkedHashMap<>();
            goodsOptions.put("굿즈 - 편지지", ((CheckBox) findViewById(R.id.cb_letter)).isChecked());
            goodsOptions.put("굿즈 - 키링", ((CheckBox) findViewById(R.id.cb_keyring)).isChecked());
            goodsOptions.put("굿즈 - 다이어리", ((CheckBox) findViewById(R.id.cb_diary)).isChecked());
            goodsOptions.put("굿즈 - 스티커", ((CheckBox) findViewById(R.id.cb_sticker)).isChecked());
            goodsOptions.put("굿즈 - 크레파스", ((CheckBox) findViewById(R.id.cb_crayon)).isChecked());
            goodsOptions.put("굿즈 - 벨크로", ((CheckBox) findViewById(R.id.cb_balcro)).isChecked());

            if (name.isEmpty() || contact.isEmpty() || email.isEmpty() || address.isEmpty()) {
                SaessacUI.ShowText(this, "빈 칸 없이 작성해주세요.");
                return;
            }
            boolean anySelected = false;
            for (Boolean value : goodsOptions.values()) {
                if (value) {
                    anySelected = true;
                    break;
                }
            }
            if (!anySelected) {
                SaessacUI.ShowText(this, "굿즈를 하나 이상 선택해주세요.");
                return;
            }

            LinkedHashMap<String, Object> data = new LinkedHashMap<>();
            data.put("이름", name);
            data.put("휴대폰", contact);
            data.put("이메일", email);
            data.put("배송지", address);
            data.putAll(goodsOptions);
            String fileName = uuid + "_request.txt";
            String filePath = new File(getBaseContext().getFilesDir(), fileName).toString();

            Extensions.CreateFile(getBaseContext().getFilesDir().toString(), fileName, new Gson().toJson(data)); // 굿즈 신청 파일 생성

            new Thread(() -> {
                if (SaessacFTP.GetGoodsCount(uuid) > 0) {
                    runOnUiThread(() -> {
                        SaessacUI.ShowText(EventGoodRequestActivity.this, "이미 굿즈 신청을 하셨습니다.");
                    });
                    return;
                }
                boolean is_success = SaessacFTP.UploadFile(filePath, SaessacFTP.GOODSREQUEST_DIR + fileName);
                if (is_success) {
                    runOnUiThread(() -> {
                        Extensions.DeleteFile(filePath);
                        SaessacUI.ShowText(EventGoodRequestActivity.this, "신청이 완료되었습니다.");
                        SaessacUI.OpenActivity(EventGoodRequestActivity.this, EventActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
                    });
                } else {
                    runOnUiThread(() -> {
                        SaessacUI.ShowText(EventGoodRequestActivity.this, "정상적으로 굿즈 신청을 할 수 없었습니다. 문제가 지속되면 관리자에게 문의하세요.");
                    });
                }
                Extensions.DeleteFile(filePath);
            }).start();
        });
    }

    @Override
    public void onBackPressed() {
        SaessacUI.OpenActivity(this, EventActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
    }
}