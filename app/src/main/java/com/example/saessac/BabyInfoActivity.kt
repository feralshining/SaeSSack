package com.saessac

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.saessac.SaessacUserData
import com.example.saessac.Extensions
import com.example.saessac.SaessacUI
import com.google.gson.Gson

class BabyInfoActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val theme = sharedPreferences.getInt("Theme", 0)

        when (theme) {
            0 -> setContentView(R.layout.activity_baby_information_theme_0)
            1 -> setContentView(R.layout.activity_baby_information_theme_1)
        }

        //하단 테마 창 정의
        SaessacUI.TouchBTN(this)

        // <editor-fold desc="[     창 진입 시 아이 프로필 정보 로드     ]">
            val filePath = baseContext.filesDir.toString()+"/"+"USER_BABYINFO.json"
            val data = SaessacUserData.LoadBabyInfo(filePath)
            if(data != null)
            {
                findViewById<EditText>(R.id.name_txt).setText(data.name)
                findViewById<EditText>(R.id.birth_txt).setText(data.birth)
                findViewById<EditText>(R.id.sex_txt).setText(data.sex)
                findViewById<EditText>(R.id.bloodtype_txt).setText(data.bloodtype)
                findViewById<EditText>(R.id.hypocorism_txt).setText(data.hypocorism)
                findViewById<EditText>(R.id.headcircum_txt).setText(data.headcircum)
                findViewById<EditText>(R.id.height_txt).setText(data.height)
                findViewById<EditText>(R.id.weight_txt).setText(data.weight)
            }
        // </editor-fold>
        // <editor-fold desc="[     버튼 - 작성 완료     ]">
            val saveBTN: ImageButton = findViewById(R.id.save_btn)
            saveBTN.setOnClickListener{
                val babyName = findViewById<EditText>(R.id.name_txt).text.toString()
                val babyBirth = findViewById<EditText>(R.id.birth_txt).text.toString()
                val babySex = findViewById<EditText>(R.id.sex_txt).text.toString()
                val babyBloodType = findViewById<EditText>(R.id.bloodtype_txt).text.toString()
                val babyHypocorism = findViewById<EditText>(R.id.hypocorism_txt).text.toString()
                val babyHeadcircum = findViewById<EditText>(R.id.headcircum_txt).text.toString()
                val babyHeight = findViewById<EditText>(R.id.height_txt).text.toString()
                val babyWeight = findViewById<EditText>(R.id.weight_txt).text.toString()

                if(babyName.isEmpty() || babyBirth.isEmpty() || babySex.isEmpty() || babyBloodType.isEmpty()
                    || babyHypocorism.isEmpty() || babyHeadcircum.isEmpty() || babyHeight.isEmpty() || babyWeight.isEmpty())
                {
                    SaessacUI.ShowText(this,"빈 칸 없이 작성해주세요.")
                    return@setOnClickListener
                }
                val user = linkedMapOf(
                    "name" to babyName,
                    "birth" to babyBirth,
                    "sex" to babySex,
                    "bloodtype" to babyBloodType,
                    "hypocorism" to babyHypocorism,
                    "headcircum" to babyHeadcircum,
                    "height" to babyHeight,
                    "weight" to babyWeight
                    )
                Extensions.CreateFile(baseContext.filesDir.toString(), "USER_BABYINFO.json", Gson().toJson(user))
                SaessacUI.ShowText(this, "작성 완료!")
            }
        // </editor-fold>
    }

    override fun onBackPressed()
    {

    }
}