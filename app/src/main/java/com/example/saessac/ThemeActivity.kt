package com.saessac

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.saessac.SaessacUI

class ThemeActivity : AppCompatActivity()
{
    private var _theme = 0
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val theme = sharedPreferences.getInt("Theme", 0)

        when (theme) {
            0 -> setContentView(R.layout.activity_settheme_theme_0)
            1 -> setContentView(R.layout.activity_settheme_theme_1)
        }

        val basicBTN: ImageButton = findViewById(R.id.basic_btn)
        val modernBTN: ImageButton = findViewById(R.id.modern_btn)
        val preview1 : ImageView = findViewById(R.id.preview_1)
        val preview2 : ImageView = findViewById(R.id.preview_2)

        if(theme == 0)
        {
            preview1.setImageResource(R.drawable.preview_picture_3) //기본 새싹 미리보기 1
            preview2.setImageResource(R.drawable.preview_picture_2) //기본 새싹 미리보기 2
            basicBTN.alpha = 1f
            modernBTN.alpha = 0.2f
            _theme = 0
        }
        else
        {
            preview1.setImageResource(R.drawable.dad_preview_picture_3) //모던 새싹 미리보기 1
            preview2.setImageResource(R.drawable.dad_preview_picture_2) //모던 새싹 미리보기 2
            basicBTN.alpha = 0.2f
            modernBTN.alpha = 1f
            _theme = 1
        }

        //<editor-fold desc="[     버튼 - 테마 선택    ]">
            basicBTN.setOnClickListener {
                preview1.setImageResource(R.drawable.preview_picture_3) //기본 새싹 미리보기 1
                preview2.setImageResource(R.drawable.preview_picture_2) //기본 새싹 미리보기 2
                basicBTN.alpha = 1f
                modernBTN.alpha = 0.2f
                _theme = 0
            }
            modernBTN.setOnClickListener {
                preview1.setImageResource(R.drawable.dad_preview_picture_3) //모던 새싹 미리보기 1
                preview2.setImageResource(R.drawable.dad_preview_picture_2) //모던 새싹 미리보기 2
                basicBTN.alpha = 0.2f
                modernBTN.alpha = 1f
                _theme = 1
            }
            //</editor-fold>
        // <editor-fold desc="[     버튼 - 테마 적용    ]">
            val applyBTN: ImageButton = findViewById(R.id.apply_btn)
            applyBTN.setOnClickListener {
                val dialog = Dialog(this)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                when(theme)
                {
                    0 -> dialog.setContentView(R.layout.dialog_apply_theme_0)
                    1 -> dialog.setContentView(R.layout.dialog_apply_theme_1)
                }
                dialog.setCancelable(false)
                dialog.setCanceledOnTouchOutside(false)
                dialog.show()

                val yesBTN: ImageButton = dialog.findViewById(R.id.yes_btn)
                val noBTN: ImageButton = dialog.findViewById(R.id.no_btn)

                yesBTN.setOnClickListener{
                    val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putInt("Theme", _theme)
                    editor.apply()
                    SaessacUI.OpenActivity(this, MainActivity::class.java)
                }

                noBTN.setOnClickListener{
                    dialog.dismiss()
                }
            }
        //</editor-fold>

        //하단 테마 창 정의
        SaessacUI.TouchBTN(this)
    }

    override fun onBackPressed()
    {

    }
}