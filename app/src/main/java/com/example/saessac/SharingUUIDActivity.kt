package com.saessac

import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.saessac.Extensions
import com.example.saessac.SaessacFTP
import com.example.saessac.SaessacUI
import com.example.saessac.SaessacUserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class SharingUUIDActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val theme = sharedPreferences.getInt("Theme", 0)

        when (theme)
        {
            0 -> setContentView(R.layout.activity_sharing_theme_0)
            1 -> setContentView(R.layout.activity_sharing_theme_1)
        }
        //<editor-fold desc="[     버튼 - 뒤로 가기     ]">
            val previousBTN: ImageButton = findViewById(R.id.previous_btn)
            previousBTN.setOnClickListener{
                SaessacUI.OpenActivity(this, SettingActivity::class.java)
            }
        //</editor-fold>
        //<editor-fold desc="[     내 UUID 표시     ]">
            val myuuidTXT: EditText = findViewById(R.id.my_uuid)
            myuuidTXT.setText(SaessacUserData.uuid)
        //</editor-fold>
        //<editor-fold desc="[     버튼 - 공유하기     ]">
            val shareBTN: ImageButton = findViewById(R.id.sharing_btn)
            shareBTN.setOnClickListener {
                val shareuuidTXT : EditText = findViewById(R.id.share_uuid)
                val shareUUID = shareuuidTXT.text.toString()
                if (shareUUID.isEmpty()) return@setOnClickListener
                lifecycleScope.launch(Dispatchers.IO)
                {
                    if (!SaessacFTP.Isvalid(shareUUID))
                    {
                        SaessacUI.ShowText(this@SharingUUIDActivity, "유효하지 않은 UUID입니다.")
                        return@launch
                    }
                    withContext(Dispatchers.Main)
                    {
                        SaveUUID(UUID.fromString(shareUUID))
                        SaessacUserData.uuid = shareUUID
                        SaessacUI.ShowText(this@SharingUUIDActivity, "공유가 완료되었습니다!")
                        SaessacUI.OpenActivity(this@SharingUUIDActivity, MainActivity::class.java)
                    }
                }
            }
        //</editor-fold>
    }

    /**
     ** sharedPreferences에 본인의 UUID 값을 공유받을 UUID 값으로 작성하고 저장합니다.
     **/
    private fun SaveUUID(uuid: UUID)
    {
        val sharedPreferences = getSharedPreferences("com.saessac.preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("uuid", uuid.toString())
        editor.apply()
    }

    override fun onBackPressed()
    {
        SaessacUI.OpenActivity(this,SettingActivity::class.java)
    }
}