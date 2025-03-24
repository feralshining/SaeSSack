package com.saessac

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.saessac.SaessacUI
import java.nio.ByteBuffer
import java.security.Key
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.math.abs

class MainActivity : AppCompatActivity()
{
    private var _lastClickTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val theme = sharedPreferences.getInt("Theme", 0)

        when (theme) {
            0 -> setContentView(R.layout.activity_main_theme_0)
            1 -> setContentView(R.layout.activity_main_theme_1)
        }
        //하단 테마 창 정의
        SaessacUI.TouchBTN(this)

        // <editor-fold desc="[     창 오픈 - 투두리스트 & 다이어리     ]">
        val myCalendarView: CalendarView = findViewById(R.id.maincalendar)
        myCalendarView.setOnDateChangeListener{ _: CalendarView, year: Int, month: Int, dayOfMonth: Int ->
            if (IsDoubleClicked())
            {
                SaessacUI.date = year.toString()+(month+1).toString()+dayOfMonth.toString()

                val dialog = Dialog(this)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

                when(theme)
                {
                    0 -> dialog.setContentView(R.layout.dialog_select_window_theme_0)
                    1 -> dialog.setContentView(R.layout.dialog_select_window_theme_1)
                }

                dialog.show()

                val todolistBTN: ImageButton = dialog.findViewById(R.id.todolist_btn)
                val diaryBTN: ImageButton = dialog.findViewById(R.id.diary_btn)
                val exitBTN: ImageButton = dialog.findViewById(R.id.exit_btn)

                //창 오픈 - 투두리스트
                todolistBTN.setOnClickListener {
                    SaessacUI.OpenActivity(this, WriteTodoListActivity::class.java)
                    dialog.dismiss()
                }

                //창 오픈 - 다이어리
                diaryBTN.setOnClickListener {
                    SaessacUI.OpenActivity(this, WriteDiaryActivity::class.java)
                    dialog.dismiss()
                }

                //다이얼로그 종료
                exitBTN.setOnClickListener{
                    dialog.dismiss()
                }
            }
        }
        // </editor-fold>
    }

    /**
     * 더블 클릭을 감지하는 메소드입니다.
     * 300ms 미만일 경우 더블 클릭으로 인식합니다. (1초 = 1000ms)
     */
    private fun IsDoubleClicked(): Boolean
    {
        var clickTime = System.currentTimeMillis()
        if (clickTime - _lastClickTime < 300) return true
        _lastClickTime = clickTime
        return false
    }

    override fun onBackPressed()
    {

    }
}