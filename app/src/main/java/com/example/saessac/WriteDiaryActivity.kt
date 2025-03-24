package com.saessac

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.saessac.Extensions
import com.example.saessac.SaessacUserData
import com.example.saessac.SaessacFTP
import com.example.saessac.SaessacUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class WriteDiaryActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val theme = sharedPreferences.getInt("Theme", 0)
        when(theme)
        {
            0 -> setContentView(R.layout.activity_diary_theme_0)
            1 -> setContentView(R.layout.activity_diary_theme_1)
        }

        val today = SaessacUI.date
        val fileName = SaessacUserData.uuid + "_" + today + ".txt"
        val filePath = File(baseContext.filesDir, fileName).toString()

        //<editor-fold desc="[     다이어리 로드     ]">
            val dialog = Dialog(this)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            when(theme)
            {
                0 -> dialog.setContentView(R.layout.dialog_waiting_theme_0)
                1 -> dialog.setContentView(R.layout.dialog_waiting_theme_1)
            }
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
            lifecycleScope.launch(Dispatchers.IO)
            {
                val diary = findViewById<EditText>(R.id.write_txt)
                val isSuccess = SaessacFTP.DownloadFile(SaessacFTP.DIARY_DIR+fileName, filePath)
                if (isSuccess)
                {
                    withContext(Dispatchers.Main)
                    {
                        diary.setText(SaessacUserData.LoadDiary(filePath))
                        Extensions.DeleteFile(filePath)
                        dialog.dismiss()
                    }
                }
                else
                {
                    withContext(Dispatchers.Main)
                    {
                        diary.hint = "해당 날짜의 다이어리가 존재하지 않습니다."
                        dialog.dismiss()
                    }
                }
            }
        //</editor-fold>
        //<editor-fold desc="[     브금 설정 값 로드     ]">
            val jsonData = SaessacUserData.LoadJSON(baseContext.filesDir.toString()+"/"+"Config.json")
            val isBGM = jsonData?.get("bgm_toggle")?.asBoolean
            if(isBGM == true) SaessacUI.PlayMusic(this)
            else SaessacUI.StopMusic()
        //</editor-fold>
        //<editor-fold desc="[     버튼 - 뒤로 가기     ]">
            val previousBTN = findViewById<ImageButton>(R.id.previous_btn)
            previousBTN.setOnClickListener{
                SaessacUI.OpenActivity(this, MainActivity::class.java)
            }
        //</editor-fold>
        //<editor-fold desc="[     버튼 - 작성 완료     ]">
            val saveBTN = findViewById<ImageButton>(R.id.save_btn)
            saveBTN.setOnClickListener {
                val data = findViewById<EditText>(R.id.write_txt).text.toString()
                lifecycleScope.launch(Dispatchers.IO)
                {
                    Extensions.CreateFile(baseContext.filesDir.toString(), fileName, data)
                    val isSuccess = SaessacFTP.UploadFile(filePath, SaessacFTP.DIARY_DIR + fileName)
                    Extensions.DeleteFile(filePath)
                    if (isSuccess)
                    {
                        withContext(Dispatchers.Main)
                        {
                            SaessacUI.OpenActivity(this@WriteDiaryActivity, DiaryCountCheckActivity::class.java)
                        }
                    }
                    else
                    {
                        withContext(Dispatchers.Main)
                        {
                            Toast.makeText(this@WriteDiaryActivity, "일기를 저장할 수 없습니다. 문제가 지속되면 관리자에게 문의하세요.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        //</editor-fold>
    }
    override fun onPause()
    {
        super.onPause()
        SaessacUI.StopMusic()
    }

    override fun onBackPressed()
    {
        SaessacUI.OpenActivity(this, MainActivity::class.java)
    }
}