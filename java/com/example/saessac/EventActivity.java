package com.example.saessac;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.saessac.SaessacUI;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int theme = sharedPreferences.getInt("Theme", 0);

        if (theme == 0) {
            setContentView(R.layout.activity_event_theme_0);
        } else if (theme == 1) {
            setContentView(R.layout.activity_event_theme_1);
        }

        // [     이용자 참여 이벤트 및 트렌드 공지    ]
        ImageButton eventBTN1 = (ImageButton) findViewById(R.id.event_btn_1);
        ImageButton eventBTN2 = (ImageButton) findViewById(R.id.event_btn_2);
        ImageButton eventBTN3 = (ImageButton) findViewById(R.id.event_btn_3);
        ImageButton eventBTN4 = (ImageButton) findViewById(R.id.event_btn_4);

        eventBTN1.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cafe.naver.com/purplebzbe2"));
            startActivity(intent);
        });
        eventBTN2.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/saessac__oo/"));
            startActivity(intent);
        });
        eventBTN3.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://artpuzzle.creatorlink.net"));
            startActivity(intent);
        });
        eventBTN4.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/tupl_kr/"));
            startActivity(intent);
        });

        SetButton(Arrays.asList(R.id.trend_btn_1, R.id.trend_btn_2, R.id.trend_btn_3),
                "https://cafe.naver.com/ArticleList.nhn?search.clubid=31209713&search.menuid=3");

        // 하단 테마 창 정의
        SaessacUI.TouchBTN(this, R.anim.slide_in_right, R.anim.slide_out_left);
        // 창 오픈 - 굿즈 신청
        ImageButton goodsBTN = (ImageButton) findViewById(R.id.goodrequest_btn);
        goodsBTN.setOnClickListener(v -> {
            SaessacUI.OpenActivity(this, EventGoodRequestActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
        });
        // 창 오픈 - 할인 공지
        ImageButton discountBTN = (ImageButton) findViewById(R.id.discount_notice_btn);
        discountBTN.setOnClickListener(v -> {
            SaessacUI.OpenActivity(this, EventDiscountActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    /**
     * 각 버튼 별로 파싱한 제목과 url을 설정합니다. 상위 3개만 가져옵니다.
     * @param buttonIds => 제목과 url을 설정할 버튼 리스트
     * @param url => 게시글 고유번호가 포함된 네이버 카페 주소
     */
    private void SetButton(List<Integer> buttonIds, String url) {
        new Thread(() -> {
            String html = GetHTML(url);
            if (html == null) return;
            List<String> titles = GetTitle(html);
            List<String> urls = GetUrl(html);

            runOnUiThread(() -> {
                for (int index = 0; index < buttonIds.size(); index++) {
                    Button button = (Button) findViewById(buttonIds.get(index));
                    if (index < titles.size() && index < urls.size()) {
                        button.setText(titles.get(index));
                        final int finalIndex = index;
                        button.setOnClickListener(v -> {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cafe.naver.com" + urls.get(finalIndex)));
                            startActivity(intent);
                        });
                    } else {
                        button.setText("");
                        button.setOnClickListener(v -> {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cafe.naver.com/ArticleList.nhn?search.clubid=31209713"));
                            startActivity(intent);
                        });
                    }
                }
            });
        }).start();
    }

    /**
     * 해당 주소의 html 페이지 소스를 가져옵니다.
     * @param url => 페이지 소스를 가져올 주소
     */
    private String GetHTML(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .timeout(10000)
                    .get();
            String text = doc.select("a.article").toString();
            return text;
        } catch (SocketException e) {
            Log.e("Network Error", "Connection reset: " + e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Network Error", "IOException: " + e.getMessage());
            return null;
        } catch (Exception e) {
            Log.e("Network Error", "Exception: " + e.getMessage());
            return null;
        }
    }

    /**
     * html 형식의 문자열을 입력받아 네이버 카페 게시글 제목을 파싱해서 반환합니다.
     * @param htmlContent => 게시글 제목이 포함된 html 데이터
     */
    private List<String> GetTitle(String htmlContent) {
        Document document = Jsoup.parse(htmlContent);
        List<String> titles = new ArrayList<>();
        Elements links = document.select("a.article");

        for (int i = 0; i < links.size(); i++) {
            titles.add(links.get(i).text().trim());
        }
        return titles;
    }

    /**
     * html 형식의 문자열을 입력받아 네이버 카페 게시글 주소를 파싱해서 반환합니다.
     * @param htmlContent => 게시글 주소가 포함된 html 데이터
     */
    private List<String> GetUrl(String htmlContent) {
        Document document = Jsoup.parse(htmlContent);
        List<String> hrefs = new ArrayList<>();
        Elements links = document.select("a.article");

        for (int i = 0; i < links.size(); i++) {
            hrefs.add(links.get(i).attr("href").trim());
        }

        return hrefs;
    }

    @Override
    public void onBackPressed() {
    }
}