package com.saessac

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.CheckBox
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.saessac.Extensions
import com.example.saessac.SaessacUI
import com.example.saessac.SaessacUserData
import com.example.saessac.SaessacFTP
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.Exception

class WriteTodoListActivity : AppCompatActivity()
{
    private var y_value = 100
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val theme = sharedPreferences.getInt("Theme", 0)
        when(theme)
        {
            0 -> setContentView(R.layout.activity_todolist_writing_theme_0)
            1 -> setContentView(R.layout.activity_todolist_writing_theme_1)
        }

        val today = SaessacUI.date
        val fileName = SaessacUserData.uuid + "_" + today + ".txt"
        val filePath = File(baseContext.filesDir, fileName).toString()
        val container: FrameLayout = findViewById(R.id.container)

        // <editor-fold desc="[     투두리스트 로드     ]">
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
                try
                {
                    val isSuccess = SaessacFTP.DownloadFile(SaessacFTP.TODOLIST_DIR+fileName, filePath)
                    if(!isSuccess)
                    {
                        withContext(Dispatchers.Main)
                        {
                            dialog.dismiss()
                        }
                        return@launch
                    }
                    withContext(Dispatchers.Main)
                    {
                        val json: JsonObject? = SaessacUserData.LoadJSON(filePath)
                        val type = object : TypeToken<LinkedHashMap<String, String>>() {}.type
                        val data: LinkedHashMap<String, String> = Gson().fromJson(json, type)
                        withContext(Dispatchers.Main)
                        {
                            y_value = 100
                            data.values.forEach { task ->
                                val cb = CheckBox(this@WriteTodoListActivity)
                                cb.text = task
                                val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT)
                                params.topMargin = y_value
                                cb.layoutParams = params
                                container.addView(cb)
                                y_value += 100
                                Extensions.DeleteFile(filePath)
                            }
                            dialog.dismiss()
                        }
                    }
                }
                catch (e: Exception)
                {
                    //Do Nothing
                }
            }
        // </editor-fold>
        // <editor-fold desc="[     브금 설정 값 로드     ]">
            val jsonData = SaessacUserData.LoadJSON(baseContext.filesDir.toString()+"/"+"Config.json")
            val isBGM = jsonData?.get("bgm_toggle")?.asBoolean
            if(isBGM == true) SaessacUI.PlayMusic(this)
            else SaessacUI.StopMusic()
        //< </editor-fold>
        // <editor-fold desc="[     버튼 - 뒤로 가기     ]">
            val previousBTN = findViewById<ImageButton>(R.id.previous_btn)
            previousBTN.setOnClickListener {
                SaessacUI.OpenActivity(this, MainActivity::class.java)
            }
        //</editor-fold>
        // <editor-fold desc="[     버튼 - 일정 추가     ]">
            val addBTN = findViewById<ImageButton>(R.id.add_btn)
            val inputTXT = findViewById<EditText>(R.id.input_txt)
            addBTN.setOnClickListener {
                val text = inputTXT.text.toString()
                if (text.isEmpty())
                {
                    SaessacUI.ShowText(this, "할 일을 하나 이상 적어주세요.")
                    return@setOnClickListener
                }
                if (y_value >= 1300)
                {
                    SaessacUI.ShowText(this, "할 일 개수는 최대 12개까지 추가 가능합니다.")
                    return@setOnClickListener
                }
                // 현존하는 모든 체크박스의 텍스트를 가져와서 가공
                val data = linkedMapOf<String, String>()
                for (i in 0 until container.childCount)
                {
                    val view = container.getChildAt(i)
                    if (view is CheckBox)
                    {
                        val key = (i + 1).toString() + "번"
                        val value = view.text.toString()
                        data[key] = value
                    }
                }
                // 새로운 체크박스 텍스트 추가
                val newKey = (container.childCount + 1).toString() + "번"
                data[newKey] = text
                Extensions.CreateFile(baseContext.filesDir.toString(), fileName, Gson().toJson(data))

                lifecycleScope.launch(Dispatchers.IO)
                {
                    val isSuccess = SaessacFTP.UploadFile(filePath, SaessacFTP.TODOLIST_DIR+fileName)
                    Extensions.DeleteFile(filePath)
                    if (isSuccess)
                    {
                        withContext(Dispatchers.Main)
                        {
                            Extensions.DeleteFile(filePath)
                            val checkBox = CheckBox(this@WriteTodoListActivity)
                            checkBox.text = text
                            val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT)
                            params.topMargin = y_value
                            checkBox.layoutParams = params
                            container.addView(checkBox)
                            y_value += 100
                        }
                    }
                    else
                    {
                        withContext(Dispatchers.Main)
                        {
                            SaessacUI.ShowText(this@WriteTodoListActivity, "변경사항을 저장할 수 없습니다. 문제가 지속되면 관리자에게 문의하세요.")
                        }
                    }
                }
            }
        //</editor-fold>
        // <editor-fold desc="[     버튼 - 일정 삭제     ]">
            val deleteBTN = findViewById<ImageButton>(R.id.delete_btn)
            deleteBTN.setOnClickListener {
                val count = container.childCount
                val toBeDeleted = mutableListOf<CheckBox>()
                for (i in 0 until count)
                {
                    val view = container.getChildAt(i)
                    if (view is CheckBox && view.isChecked)
                    {
                        toBeDeleted.add(view)
                    }
                }
                for (checkBox in toBeDeleted)
                {
                    container.removeView(checkBox)
                }
                y_value = 100
                val data = linkedMapOf<String, String>()
                for (i in 0 until container.childCount)
                {
                    val view = container.getChildAt(i)
                    if (view is CheckBox)
                    {
                        val params = FrameLayout.LayoutParams( FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT)
                        params.topMargin = y_value
                        view.layoutParams = params
                        val key = (i + 1).toString() + "번"
                        val value = view.text.toString()
                        data[key] = value
                        y_value += 100
                    }
                }
                lifecycleScope.launch(Dispatchers.IO)
                {
                    Extensions.CreateFile(baseContext.filesDir.toString(), fileName, Gson().toJson(data))
                    val is_success = SaessacFTP.UploadFile(filePath, SaessacFTP.TODOLIST_DIR+fileName)
                    Extensions.DeleteFile(filePath)
                    if (is_success)
                    {
                        withContext(Dispatchers.Main)
                        {
                            Extensions.DeleteFile(filePath)
                        }
                    }
                    else
                    {
                        withContext(Dispatchers.Main)
                        {
                            SaessacUI.ShowText(this@WriteTodoListActivity, "변경사항을 저장할 수 없습니다. 문제가 지속되면 관리자에게 문의하세요.")
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