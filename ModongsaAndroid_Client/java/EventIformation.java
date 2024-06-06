package smu.hw_network_team5_chatting_android;

import static smu.hw_network_team5_chatting_android.MoDongSa.neighborhoodEvents;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


public class EventIformation extends AppCompatActivity {
    static int eventIndex = 0;
    Button askBTN;
    Button reservationBTN;
    ImageView eventPoster;
    TextView eventTitle, eventDate, eventInfo;
    RatingBar eventStars;
    String eventWhereURL;

    // 웹뷰는 추가해야함
    private String TAG = EventIformation.class.getSimpleName();
    private WebView webView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_infromation);

        Intent intent = getIntent(); // 전달된 데이터 받을 intent
        // text 키 값으로 데이터를 받는다. int형을 받을거다
        eventIndex = intent.getIntExtra("clickPosition", 0); //클릭한 위치가 movies의 인덱스가 될거임
        NeighborhoodEvents nowNeighborhoodEvents = neighborhoodEvents[eventIndex]; // 현재 보고 있는 창의 영화
        //
        askBTN = findViewById(R.id.askBTN);
        reservationBTN = findViewById(R.id.reservationBTN);
        eventPoster = findViewById(R.id.posterImageView);
        eventTitle = findViewById(R.id.titleTextView);
        eventDate = findViewById(R.id.open_dayTextView);
        eventInfo = findViewById(R.id.movieInfoTextView);
        eventStars = findViewById(R.id.starsRatingBar);

        // 선택된 행사로 정보 세팅
        eventPoster.setImageResource(nowNeighborhoodEvents.getImage_path());
        eventTitle.setText(nowNeighborhoodEvents.getTitle());
        eventDate.setText(nowNeighborhoodEvents.getDate());
        eventInfo.setText(nowNeighborhoodEvents.getInfo());
        //  url은 string으로 변경
        eventWhereURL = nowNeighborhoodEvents.getWhereURL();
        eventStars.setRating(nowNeighborhoodEvents.getStarScore());

        //문의하기 버튼 이벤트
        askBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChattingActivity.class);
                intent.putExtra("portNumber", nowNeighborhoodEvents.getPortNumber()); // 해당 기관 포트넘버 넘겨줌
                //Log.e("ServerIP", Client.SERVERIP);
                startActivity(intent);
            }
        });

        // 신청 버튼 이벤트
        reservationBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast myToast = Toast.makeText(getApplicationContext(), nowNeighborhoodEvents.getTitle() + "\n신청 완료되었습니다", Toast.LENGTH_LONG);
                myToast.show();
            }
        });

        // 웹뷰
        webView = (WebView) findViewById(R.id.webView);

        webView.setWebViewClient(new WebViewClient());  // 새 창 띄우기 않기
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setLoadWithOverviewMode(true);  // WebView 화면크기에 맞추도록 설정 - setUseWideViewPort 와 같이 써야함
        webView.getSettings().setUseWideViewPort(true);  // wide viewport 설정 - setLoadWithOverviewMode 와 같이 써야함
        webView.getSettings().setSupportZoom(false);  // 줌 설정 여부
        webView.getSettings().setBuiltInZoomControls(false);  // 줌 확대/축소 버튼 여부
        webView.getSettings().setJavaScriptEnabled(true); // 자바스크립트 사용여부
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); // javascript가 window.open()을 사용할 수 있도록 설정
        webView.getSettings().setSupportMultipleWindows(true); // 멀티 윈도우 사용 여부
        webView.getSettings().setDomStorageEnabled(true);  // 로컬 스토리지 (localStorage) 사용여부

        //선택한 행사 관련 웹페이지 호출
        webView.loadUrl(eventWhereURL);


    }
}