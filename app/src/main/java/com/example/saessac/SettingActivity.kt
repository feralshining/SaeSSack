package com.saessac

import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.example.saessac.Extensions
import com.example.saessac.SaessacUI
import com.example.saessac.SaessacUserData
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val theme = sharedPreferences.getInt("Theme", 0)

        when (theme) {
            0 -> setContentView(R.layout.activity_setting_theme_0)
            1 -> setContentView(R.layout.activity_setting_theme_1)
        }

        //하단 테마 창 정의
        SaessacUI.TouchBTN(this)
        //<editor-fold desc="[     창 오픈 - 마이 페이지     ]">
            val mypageBTN: Button = findViewById(R.id.mypage_btn)
            mypageBTN.setOnClickListener{
                SaessacUI.OpenActivity(this, SettingMyPageActivity::class.java)
            }
        //</editor-fold>
        //<editor-fold desc="[     토글 - BGM ON/OFF     ]">
            val bgmSwitch: Switch = findViewById(R.id.playmusic_toggle)
            val json = SaessacUserData.LoadJSON(baseContext.filesDir.toString() + "/" + "Config.json")
            val isBGM = json?.get("bgm_toggle")?.asBoolean ?: false
            bgmSwitch.isChecked = isBGM
            bgmSwitch.setOnCheckedChangeListener { _, isChecked ->
                CoroutineScope(Dispatchers.IO).launch {
                    val data = linkedMapOf("bgm_toggle" to isChecked.toString())
                    Extensions.CreateFile(baseContext.filesDir.toString(), "Config.json", Gson().toJson(data))
                }
            }
        //</editor-fold>
        //<editor-fold desc="[     토글 - 알림 ON/OFF     ]">
        val noticeSwitch: Switch = findViewById(R.id.playmusic_toggle)
        noticeSwitch.setOnCheckedChangeListener { _, isChecked ->
            CoroutineScope(Dispatchers.IO).launch {
                val data = linkedMapOf("bgm_toggle" to isChecked.toString())
                Extensions.CreateFile(baseContext.filesDir.toString(), "Config.json", Gson().toJson(data))
            }
        }
        //</editor-fold>
        //<editor-fold desc="[     창 오픈 - 고객센터     ]">
            val serviceBTN: Button = findViewById(R.id.customer_service_btn)
            serviceBTN.setOnClickListener{
                SaessacUI.OpenActivity(this, SettingServiceActivity::class.java)
            }
        //</editor-fold>
        //<editor-fold desc="[     창 오픈 - 운영정책     ]">
            val policyBTN: Button = findViewById(R.id.policy_btn)
            policyBTN.setOnClickListener{
                SaessacUI.OpenActivity(this, SettingPolicyActivity::class.java)
            }
        //</editor-fold>
        // <editor-fold desc="[     창 오픈 - 가족 공유     ]">
            val sharingBTN: Button = findViewById(R.id.sharing_btn)
            sharingBTN.setOnClickListener{
                SaessacUI.OpenActivity(this, SharingUUIDActivity::class.java)
            }
        //</editor-fold>
    }

    override fun onBackPressed()
    {

    }
}