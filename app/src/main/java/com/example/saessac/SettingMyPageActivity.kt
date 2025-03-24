package com.saessac

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.saessac.Extensions
import com.example.saessac.SaessacUI
import com.google.gson.Gson
import com.example.saessac.SaessacUserData

class SettingMyPageActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val theme = sharedPreferences.getInt("Theme", 0)

        when (theme) {
            0 -> setContentView(R.layout.activity_my_page_theme_0)
            1 -> setContentView(R.layout.activity_my_page_theme_1)
        }
        // <editor-fold desc="[     마이페이지 정보 로드     ]">
            val user = SaessacUserData.LoadUserInfo(baseContext.filesDir.toString()+"/"+"USER_MYPAGE.json")
            println(user)
            if (user != null)
            {
                findViewById<EditText>(R.id.name_txt).setText(user.name)
                findViewById<EditText>(R.id.birth_txt).setText(user.birth)
                findViewById<EditText>(R.id.sex_txt).setText(user.sex)
                findViewById<EditText>(R.id.bloodtype_txt).setText(user.bloodtype)
                findViewById<EditText>(R.id.relationship_txt).setText(user.relationship)
            }
        //</editor-fold>
        //<editor-fold desc="[     버튼 - 뒤로 가기     ]">
            val previousBTN: ImageButton = findViewById(R.id.previous_btn)
            previousBTN.setOnClickListener{
                SaessacUI.OpenActivity(this, SettingActivity::class.java)
            }
        //</editor-fold>
        //<editor-fold desc="[     버튼 - 작성 완료     ]">
            val saveBTN: ImageButton = findViewById(R.id.save_btn)
            saveBTN.setOnClickListener{
                val userName = findViewById<EditText>(R.id.name_txt).text.toString()
                val userBirth = findViewById<EditText>(R.id.birth_txt).text.toString()
                val userSex = findViewById<EditText>(R.id.sex_txt).text.toString()
                val userBloodType = findViewById<EditText>(R.id.bloodtype_txt).text.toString()
                val userRelationship = findViewById<EditText>(R.id.relationship_txt).text.toString()

                if(userName.isEmpty() || userBirth.isEmpty() || userSex.isEmpty() || userBloodType.isEmpty() || userRelationship.isEmpty())
                {
                    SaessacUI.ShowText(this, "빈 칸 없이 작성해주세요.")
                    return@setOnClickListener
                }
                val user = linkedMapOf(
                    "name" to userName,
                    "birth" to userBirth,
                    "sex" to userSex,
                    "bloodtype" to userBloodType,
                    "relationship" to userRelationship)
                Extensions.CreateFile(baseContext.filesDir.toString(), "USER_MYPAGE.json", Gson().toJson(user))
                SaessacUI.ShowText(this, "작성 완료!")
            }
        //</editor-fold>
    }

    override fun onBackPressed()
    {
        SaessacUI.OpenActivity(this, SettingActivity::class.java)
    }
}