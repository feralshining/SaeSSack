package com.example.saessac

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.saessac.BabyInfoActivity
import com.saessac.EventActivity
import com.saessac.MainActivity
import com.saessac.R
import com.saessac.SettingActivity
import com.saessac.SharingUUIDActivity
import com.saessac.ThemeActivity

object SaessacUI
{
    lateinit var date : String
    var theme: Int = 0

    /**
     * 입력받은 액티비티를 실행하는 메소드입니다.
     */
    fun OpenActivity(context: Context, activityName: Class<*>)
    {
        val intent = Intent(context, activityName)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    /**
     * 하단 테마 창의 버튼을 정의하고 클릭 시 창을 띄우는 메소드입니다.
     */
    fun TouchBTN(activity: AppCompatActivity)
    {
        val btnMAP = mapOf(
            R.id.home_btn to MainActivity::class.java,
            R.id.babyinfo_btn to BabyInfoActivity::class.java,
            R.id.setting_btn to SettingActivity::class.java,
            R.id.theme_btn to ThemeActivity::class.java,
            R.id.event_btn to EventActivity::class.java
        )

        for ((buttonId, activityClass) in btnMAP)
        {
            val button: ImageButton = activity.findViewById(buttonId)
            button.setOnClickListener {
                val intent = Intent(activity, activityClass)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                activity.startActivity(intent)
            }
        }
    }

    /**
     * 입력받은 메세지를 토스트 안내창으로 표시합니다.
     */
    fun ShowText(context: Context, message: String)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * 음악을 백그라운드에서 재생합니다.
     */
    fun PlayMusic(context: Context)
    {
        if (MediaPlayerManager.player == null)
        {
            MediaPlayerManager.player = MediaPlayer.create(context, R.raw.background_bgm)
            MediaPlayerManager.player?.isLooping = true
        }
        MediaPlayerManager.player?.start()
    }

    /**
     * 재생하고 있는 음악을 중지하고 리소스를 해제합니다.
     */
    fun StopMusic()
    {
        if (MediaPlayerManager.player != null)
        {
            MediaPlayerManager.player?.release()
            MediaPlayerManager.player = null
        }
    }
}