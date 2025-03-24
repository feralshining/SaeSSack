package com.saessac

import android.os.Bundle
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.saessac.SaessacUI
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import kotlinx.coroutines.*
import org.jsoup.select.Elements
import java.io.IOException
import java.net.SocketException

class EventActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val theme = sharedPreferences.getInt("Theme", 0)

        when (theme)
        {
            0 -> setContentView(R.layout.activity_event_theme_0)
            1 -> setContentView(R.layout.activity_event_theme_1)
        }

        //<editor-fold desc="[     이용자 참여 이벤트 및 트렌드 공지    ]"
            val eventBTN1 : ImageButton = findViewById(R.id.event_btn_1)
            val eventBTN2 : ImageButton = findViewById(R.id.event_btn_2)
            val eventBTN3 : ImageButton = findViewById(R.id.event_btn_3)
            val eventBTN4 : ImageButton = findViewById(R.id.event_btn_4)

            eventBTN1.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://cafe.naver.com/purplebzbe2"))
                startActivity(intent)
            }
            eventBTN2.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/saessac__oo/"))
                startActivity(intent)
            }
            eventBTN3.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://artpuzzle.creatorlink.net"))
                startActivity(intent)
            }
            eventBTN4.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/tupl_kr/"))
                startActivity(intent)
            }

            SetButton(
                listOf(R.id.trend_btn_1, R.id.trend_btn_2, R.id.trend_btn_3),
                "https://cafe.naver.com/ArticleList.nhn?search.clubid=31209713&search.menuid=3"
            )
        //</editor-fold>

        //하단 테마 창 정의
        SaessacUI.TouchBTN(this)
        //창 오픈 - 굿즈 신청
        val goodsBTN = findViewById<ImageButton>(R.id.goodrequest_btn)
        goodsBTN.setOnClickListener{
            SaessacUI.OpenActivity(this, EventGoodRequestActivity::class.java)
        }
        //창 오픈 - 할인 공지
        val discountBTN = findViewById<ImageButton>(R.id.discount_notice_btn)
        discountBTN.setOnClickListener{
            SaessacUI.OpenActivity(this, EventDiscountActivity::class.java)
        }
    }

    /**
     * 각 버튼 별로 파싱한 제목과 url을 설정합니다. 상위 3개만 가져옵니다.
     * @param buttonIds => 제목과 url을 설정할 버튼 리스트
     * @param url => 게시글 고유번호가 포함된 네이버 카페 주소
     */
    private fun SetButton(buttonIds: List<Int>, url: String)
    {
        lifecycleScope.launch(Dispatchers.IO)
        {
            val html = GetHTML(url)?.trimIndent() ?: return@launch
            val titles = GetTitle(html)
            val urls = GetUrl(html)

            withContext(Dispatchers.Main)
            {
                buttonIds.forEachIndexed { index, buttonId ->
                    val button = findViewById<Button>(buttonId)
                    if (index < titles.size && index < urls.size)
                    {
                        button.text = titles[index]
                        button.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://cafe.naver.com${urls[index]}"))
                            startActivity(intent)
                        }
                    }
                    else
                    {
                        button.text = ""
                        button.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://cafe.naver.com/ArticleList.nhn?search.clubid=31209713"))
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    /**
     * 해당 주소의 html 페이지 소스를 가져옵니다.
     * @param url => 페이지 소스를 가져올 주소
     */
    private suspend fun GetHTML(url: String): String? = withContext(Dispatchers.IO)
    {
        try
        {
            val doc = Jsoup.connect(url)
                .timeout(10_000)
                .get()
            val text = doc.select("a.article").toString()
            text
        }
        catch (e: SocketException)
        {
            Log.e("Network Error", "Connection reset: ${e.message}")
            null
        }
        catch (e: IOException)
        {
            Log.e("Network Error", "IOException: ${e.message}")
            null
        }
        catch (e: Exception)
        {
            Log.e("Network Error", "Exception: ${e.message}")
            null
        }
    }
    /**
     * html 형식의 문자열을 입력받아 네이버 카페 게시글 제목을 파싱해서 반환합니다.
     * @param htmlContent => 게시글 제목이 포함된 html 데이터
     */
    private fun GetTitle(htmlContent: String): List<String>
    {
        val document: Document = Jsoup.parse(htmlContent)
        val titles = mutableListOf<String>()
        val links: Elements = document.select("a.article")

        for (i in 0 until links.size)
        {
            titles.add(links[i].text().trim())
        }
        return titles
    }

    /**
     * html 형식의 문자열을 입력받아 네이버 카페 게시글 주소를 파싱해서 반환합니다.
     * @param htmlContent => 게시글 주소가 포함된 html 데이터
     */
    private fun GetUrl(htmlContent: String): List<String>
    {
        val document: Document = Jsoup.parse(htmlContent)
        val hrefs = mutableListOf<String>()
        val links: Elements = document.select("a.article")

        for (i in 0 until links.size)
        {
            hrefs.add(links[i].attr("href").trim())
        }

        return hrefs
    }

    override fun onBackPressed()
    {

    }
}