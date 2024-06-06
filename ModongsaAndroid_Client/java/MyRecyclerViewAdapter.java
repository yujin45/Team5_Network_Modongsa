package smu.hw_network_team5_chatting_android;

import static smu.hw_network_team5_chatting_android.MoDongSa.neighborhoodEvents;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter {
    /*
  어댑터의 동작원리 및 순서
  1.(getItemCount) 데이터 개수를 세어 어댑터가 만들어야 할 총 아이템 개수를 얻는다.
  2.(getItemViewType)[생략가능] 현재 itemview의 viewtype을 판단한다
  3.(onCreateViewHolder)viewtype에 맞는 뷰 홀더를 생성하여 onBindViewHolder에 전달한다.
  4.(onBindViewHolder)뷰홀더와 position을 받아 postion에 맞는 데이터를 뷰홀더의 뷰들에 바인딩한다.
  */
    String TAG = "RecyclerViewAdapter";

    //리사이클러뷰에 넣을 데이터 리스트
    ArrayList<NeighborhoodEvents> dataModels;

    Context context;

    //생성자를 통하여 데이터 리스트 context를 받음
    public MyRecyclerViewAdapter(Context context, ArrayList<NeighborhoodEvents> dataModels) {
        this.dataModels = dataModels;
        this.context = context;
    }


    public int getItemCount() {
        //데이터 리스트의 크기를 전달해주어야 함
        return dataModels.size();
    }


    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");

        //자신이 만든 itemview를 inflate한 다음 뷰홀더 생성
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);


        //생선된 뷰홀더를 리턴하여 onBindViewHolder에 전달한다.
        return viewHolder;
    }


    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Log.d(TAG, "onBindViewHolder");

        MyViewHolder myViewHolder = (MyViewHolder) holder;

        myViewHolder.textViewTitle.setText(dataModels.get(position).getTitle());
        // ▲ Movie에서 Title 가져와서 넣어줌
        myViewHolder.textViewReleaseDate.setText(dataModels.get(position).getDate());
        // ▲ 일시 넣어주기
        myViewHolder.imageViewPoster.setImageResource(dataModels.get(position).image_path);
        // ▲ Movie 해당하는 poster가져와서 그려줌
        myViewHolder.starsRatingBar.setRating(dataModels.get(position).getStarScore());
        //▲ 별점 넣기
        // ▼ 리사이클러 내의 아이템 클릭시 동작하는 부분
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, position+"번째 아이템 클릭", Toast.LENGTH_SHORT).show();
                // 인텐트로 넘겨줘야 하는 부분
                Intent intent = new Intent(myViewHolder.itemView.getContext(), EventIformation.class);
                intent.putExtra("clickPosition", position); // position만 넘겨주면 어떤 영화, 스케줄인지 static 변수에 접근 가능
                //ContextCompat.startActivity(myViewHolder.itemView.getContext(), intent, null);
                ContextCompat.startActivity(myViewHolder.itemView.getContext(), intent, null);
            }
        });

        myViewHolder.askButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myViewHolder.itemView.getContext(), ChattingActivity.class);
                intent.putExtra("portNumber", neighborhoodEvents[position].getPortNumber()); // 해당 기관 포트넘버 넘겨줌
                //Log.e("ServerIP", Client.SERVERIP);
                ContextCompat.startActivity(myViewHolder.itemView.getContext(), intent, null);
            }
        });

        myViewHolder.subscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast myToast = Toast.makeText(myViewHolder.itemView.getContext(), neighborhoodEvents[position].getTitle() + "\n신청 완료되었습니다", Toast.LENGTH_LONG);
                myToast.show();
            }
        });
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        ImageView imageViewPoster;
        TextView textViewReleaseDate;
        RatingBar starsRatingBar;
        Button askButton, subscriptionButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            imageViewPoster = itemView.findViewById(R.id.imageViewPoster);
            textViewReleaseDate = itemView.findViewById(R.id.textViewReleaseDate);
            starsRatingBar = itemView.findViewById(R.id.starsRatingBar);
            askButton = itemView.findViewById(R.id.buttonAsk);
            subscriptionButton = itemView.findViewById(R.id.buttonSubscription);

        }
    }


}
