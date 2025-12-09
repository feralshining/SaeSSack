package com.saessac;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventActivity extends AppCompatActivity {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.content.SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int theme = sharedPreferences.getInt("Theme", 0);

        switch (theme) {
            case 0:
                setContentView(R.layout.activity_event_theme_0);
                break;
            case 1:
                setContentView(R.layout.activity_event_theme_1);
                break;
        }

        // 이용자 참여 이벤트 - 프렌즈비 공유
        ImageButton eventBTN1 = findViewById(R.id.event_btn_1);
        ImageButton eventBTN2 = findViewById(R.id.event_btn_2);
        ImageButton eventBTN3 = findViewById(R.id.event_btn_3);
        ImageButton eventBTN4 = findViewById(R.id.event_btn_4);

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

        List<Integer> buttonIds = new ArrayList<>();
        buttonIds.add(R.id.trend_btn_1);
        buttonIds.add(R.id.trend_btn_2);
        buttonIds.add(R.id.trend_btn_3);
        setButton(buttonIds, "https://cafe.naver.com/ArticleList.nhn?search.clubid=31209713&search.menuid=3");

        // 하단 테마 창 정의
        SaessacUI.touchBTN(this, R.anim.slide_in_right, R.anim.slide_out_left);

        // 창 오픈 - 굿즈 신청
        ImageButton goodsBTN = findViewById(R.id.goodrequest_btn);
        goodsBTN.setOnClickListener(v -> {
            SaessacUI.openActivity(this, EventGoodRequestActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // 창 오픈 - 할인 공지
        ImageButton discountBTN = findViewById(R.id.discount_notice_btn);
        discountBTN.setOnClickListener(v -> {
            SaessacUI.openActivity(this, EventDiscountActivity.class, R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    /**
     * 각 버튼 별로 최상위 항목과 url을 설정합니다. 상위 3개만 가져옵니다.
     */
    private void setButton(List<Integer> buttonIds, String url) {
        executorService.execute(() -> {
            String html = getHTML(url);
            if (html == null)
                return;

            List<String> titles = getTitle(html);
            List<String> urls = getUrl(html);

            runOnUiThread(() -> {
                for (int i = 0; i < buttonIds.size(); i++) {
                    Button button = findViewById(buttonIds.get(i));
                    if (i < titles.size() && i < urls.size()) {
                        final int index = i;
                        button.setText(titles.get(index));
                        button.setOnClickListener(v -> {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://cafe.naver.com" + urls.get(index)));
                            startActivity(intent);
                        });
                    } else {
                        button.setText("");
                        button.setOnClickListener(v -> {
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://cafe.naver.com/ArticleList.nhn?search.clubid=31209713"));
                            startActivity(intent);
                        });
                    }
                }
            });
        });
    }

    /**
     * ?�당 주소??html ?�이지 ?�스�?가?�옵?�다.
     */
    private String getHTML(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .timeout(10000)
                    .get();
            return doc.select("a.article").toString();
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
     * html ?�식??문자?�을 ?�력받아 ?�이�?카페 게시글 ?�목???�싱?�서 반환?�니??
     */
    private List<String> getTitle(String htmlContent) {
        Document document = Jsoup.parse(htmlContent);
        List<String> titles = new ArrayList<>();
        Elements links = document.select("a.article");

        for (int i = 0; i < links.size(); i++) {
            titles.add(links.get(i).text().trim());
        }
        return titles;
    }

    /**
     * html ?�식??문자?�을 ?�력받아 ?�이�?카페 게시글 주소�??�싱?�서 반환?�니??
     */
    private List<String> getUrl(String htmlContent) {
        Document document = Jsoup.parse(htmlContent);
        List<String> hrefs = new ArrayList<>();
        Elements links = document.select("a.article");

        for (int i = 0; i < links.size(); i++) {
            hrefs.add(links.get(i).attr("href").trim());
        }
        return hrefs;
    }
}
