package com.saessac

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.saessac.SaessacFTP
import com.example.saessac.SaessacUI
import com.example.saessac.SaessacUserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiaryCountCheckActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val theme = sharedPreferences.getInt("Theme", 0)

        when (theme) {
            0 -> setContentView(R.layout.activity_number_of_diaries_theme_0)
            1 -> setContentView(R.layout.activity_number_of_diaries_theme_1)
        }

        val today = SaessacUI.date
        val month : String = today.substring(4, 5)
        val uuid : String = SaessacUserData.uuid
        // <editor-fold desc="[     다이어리 작성 횟수     ]">
            lifecycleScope.launch(Dispatchers.IO)
            {
                val diaryNum : IntArray = SaessacFTP.GetDiaryCount(month, uuid)
                withContext(Dispatchers.Main)
                {
                    findViewById<EditText>(R.id.diarytotal_txt).setText(diaryNum[0].toString()) //전체 작성 횟수
                    findViewById<EditText>(R.id.diarymonth_txt).setText(diaryNum[1].toString()) //전체 중 해당 월 작성 횟수
                }
            }
        //</editor-fold>
        // <editor-fold desc="[     버튼 - 확인     ]">
            val checkBTN = findViewById<ImageButton>(R.id.check_btn)
            checkBTN.setOnClickListener {
                SaessacUI.OpenActivity(this, MainActivity::class.java)
            }
        //</editor-fold>
    }
    override fun onBackPressed()
    {

    }
}




