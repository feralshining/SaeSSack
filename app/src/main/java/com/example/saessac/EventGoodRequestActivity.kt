package com.saessac

import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.saessac.Extensions
import com.example.saessac.SaessacUI
import com.example.saessac.SaessacUserData
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import com.example.saessac.SaessacFTP

class EventGoodRequestActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val theme = sharedPreferences.getInt("Theme", 0)

        when (theme) {
            0 -> setContentView(R.layout.activity_good_request_theme_0)
            1 -> setContentView(R.layout.activity_good_request_theme_1)
        }
        val uuid = SaessacUserData.uuid

        // <editor-fold desc="[     버튼 - 뒤로 가기     ]">
            val previousBTN = findViewById<ImageButton>(R.id.previous_btn)
            previousBTN.setOnClickListener{
                SaessacUI.OpenActivity(this, EventActivity::class.java)
            }
        //</editor-fold>
        // <editor-fold desc="[     버튼 - 굿즈 신청     ]">
            val requestBTN = findViewById<ImageButton>(R.id.request_btn)
            requestBTN.setOnClickListener{
                val name = findViewById<EditText>(R.id.name_txt).text.toString()
                val contact = findViewById<EditText>(R.id.contact_txt).text.toString()
                val email = findViewById<EditText>(R.id.email_txt).text.toString()
                val address = findViewById<EditText>(R.id.address_txt).text.toString()
                val goodsOptions = mapOf(
                    "굿즈 - 편지지" to findViewById<CheckBox>(R.id.cb_letter).isChecked,
                    "굿즈 - 키링" to findViewById<CheckBox>(R.id.cb_keyring).isChecked,
                    "굿즈 - 다이어리" to findViewById<CheckBox>(R.id.cb_diary).isChecked,
                    "굿즈 - 스티커" to findViewById<CheckBox>(R.id.cb_sticker).isChecked,
                    "굿즈 - 크레파스" to findViewById<CheckBox>(R.id.cb_crayon).isChecked,
                    "굿즈 - 벨크로" to findViewById<CheckBox>(R.id.cb_balcro).isChecked
                )

                if (name.isEmpty() || contact.isEmpty() || email.isEmpty() || address.isEmpty())
                {
                    SaessacUI.ShowText(this, "빈 칸 없이 작성해주세요.")
                    return@setOnClickListener
                }
                if (goodsOptions.values.none { it })
                {
                    SaessacUI.ShowText(this, "굿즈를 하나 이상 선택해주세요.")
                    return@setOnClickListener
                }

                val data = linkedMapOf(
                    "이름" to name,
                    "휴대폰" to contact,
                    "이메일" to email,
                    "배송지" to address
                ) + goodsOptions
                val fileName = uuid+"_request.txt"
                val filePath = File(baseContext.filesDir, fileName).toString()

                Extensions.CreateFile(baseContext.filesDir.toString(), fileName, Gson().toJson(data)) //굿즈 신청 파일 생성

                lifecycleScope.launch(Dispatchers.IO)
                {
                    if (SaessacFTP.GetGoodsCount(uuid) > 0)
                    {
                        withContext(Dispatchers.Main)
                        {
                            SaessacUI.ShowText(this@EventGoodRequestActivity, "이미 굿즈 신청을 하셨습니다.")
                        }
                        return@launch
                    }
                    val is_success = SaessacFTP.UploadFile(filePath, SaessacFTP.GOODSREQUEST_DIR + fileName)
                    if (is_success)
                    {
                        withContext(Dispatchers.Main)
                        {
                            Extensions.DeleteFile(filePath)
                            SaessacUI.ShowText(this@EventGoodRequestActivity, "신청이 완료되었습니다.")
                            SaessacUI.OpenActivity(this@EventGoodRequestActivity,EventActivity::class.java)
                        }
                    }
                    else
                    {
                        withContext(Dispatchers.Main)
                        {
                            SaessacUI.ShowText(this@EventGoodRequestActivity, "정상적으로 굿즈 신청을 할 수 없었습니다. 문제가 지속되면 관리자에게 문의하세요.")
                        }
                    }
                    Extensions.DeleteFile(filePath)
                }
            }
        //</editor-fold>
    }
    override fun onBackPressed()
    {
        SaessacUI.OpenActivity(this, EventActivity::class.java)
    }
}