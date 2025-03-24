package com.saessac

import android.content.Context
import android.os.Bundle
import java.util.UUID
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.saessac.SaessacUserData
import com.example.saessac.SaessacUI

class LoginRegisterActivity : AppCompatActivity()
{
    private lateinit var _sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        _sharedPreferences = getSharedPreferences("com.saessac.preferences", Context.MODE_PRIVATE)

        var uuid: UUID? = LoadUUID()
        if (uuid == null) //앱을 처음 실행한 유저
        {
            uuid = UUID.randomUUID()
            SaveUUID(uuid)
        }
        SaessacUserData.uuid = uuid.toString()
        SaessacUI.OpenActivity(this, MainActivity::class.java)
    }

    /**
     ** sharedPreferences에 저장된 uuid 값을 로드해서 UUID 객체로 반환합니다.
     **/
    private fun LoadUUID(): UUID?
    {
        val uuidString = _sharedPreferences.getString("uuid", null)
        return uuidString?.let { UUID.fromString(it) }
    }

    /**
     ** sharedPreferences에 랜덤으로 생성한 UUID 값을 작성하고 저장합니다.
     **/
    private fun SaveUUID(uuid: UUID) {
        val editor = _sharedPreferences.edit()
        editor.putString("uuid", uuid.toString())
        editor.apply()
    }
}