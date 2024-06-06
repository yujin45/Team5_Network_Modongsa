package smu.hw_network_team5_chatting_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MoDongSa extends AppCompatActivity {

    MyRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    public static int eventCount = 6;
    public static NeighborhoodEvents neighborhoodEvents[] = new NeighborhoodEvents[eventCount]; //  행사

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mo_dong_sa);
        // 동네 행사 관련한 것들
        // 제목
        String title_array[] = getResources().getStringArray(R.array.title); // 제목
        // 행사 일시
        String date_array[] = getResources().getStringArray(R.array.date);
        // 행사 정보
        String info_array[] = getResources().getStringArray(R.array.info); // 정보
        // 행사 어디서 하는지 추가
        String url_array[] = getResources().getStringArray(R.array.url);
        // 평점
        int starScore_array[] = getResources().getIntArray(R.array.starScore);
        // 이미지 추가하기
        int[] poster_array = {R.drawable.event1, R.drawable.event2, R.drawable.event3, R.drawable.event4, R.drawable.event5, R.drawable.event6};
        // 각 기관별 포트번호 있는 것 추가
        int portNumber_array[] = getResources().getIntArray(R.array.portNumber);

        // 영화 객체를 리스트로 만들기
        for (int i = 0; i < eventCount; i++) {
            neighborhoodEvents[i] = new NeighborhoodEvents(title_array[i], date_array[i], info_array[i], url_array[i], poster_array[i], starScore_array[i], portNumber_array[i]);
        }
        // 데이터 영화 리스트
        ArrayList<NeighborhoodEvents> movies_list = new ArrayList<>();
        for (int i = 0; i < eventCount; i++) {
            movies_list.add(neighborhoodEvents[i]);
        }

        recyclerView = findViewById(R.id.recyclerview);
        adapter = new MyRecyclerViewAdapter(this, movies_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));


    }
}