package smu.hw_network_team5_chatting_android;

import static smu.hw_network_team5_chatting_android.ChattingActivity.myChat;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyCustomAdapter extends BaseAdapter {
    private ArrayList<String> mListItems;
    private LayoutInflater mLayoutInflater;

    public MyCustomAdapter(Context context, ArrayList<String> arrayList) {
        mListItems = arrayList;
        //레이아웃 인플레이터 가져오기
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        //getCount()는 목록에 있는 항목 수를 나타냄
        return mListItems.size();
    }

    @Override
    //특정 위치에서 항목의 데이터 가져오기
    //i가 position을 의미
    public Object getItem(int i) {
        return null;
    }

    @Override
    //목록에서 항목의 위치 ID를 가져옴
    public long getItemId(int i) {
        return 0;
    }

    @Override

    public View getView(int position, View view, ViewGroup viewGroup) {
        //재사용된 뷰가 null인지 여부를 확인하고, null이 아니면 재사용
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.list_item, null);
        }
        //배열 목록의 "position" 위치에서 문자열 항목을 가져와 TextView에 넣기
        String stringItem = mListItems.get(position);
        if (stringItem != null) {
            TextView itemName = (TextView) view.findViewById(R.id.list_item_text_view);
            if (myChat) {
                // 제일 최근 chat이 마젠타 색상으로 표시되게 함
                itemName.setTextColor(Color.MAGENTA);
                myChat = false;
            }
            if (itemName != null) {
                //TextView에 채팅 글 설정
                itemName.setText(stringItem);
            }
        }
        // 지정된 위치의 데이터에 해당하는 뷰를 반환
        return view;

    }
}