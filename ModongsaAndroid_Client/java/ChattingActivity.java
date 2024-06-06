package smu.hw_network_team5_chatting_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChattingActivity extends AppCompatActivity {
    private ListView mList;
    private ArrayList<String> arrayList;
    private MyCustomAdapter mAdapter;
    //public static MyCustomAdapter mAdapter;
    private Client mClient;
    public static boolean myChat = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        Intent intent = getIntent();
        int portNumber = intent.getIntExtra("portNumber", 0);

        Client.SERVERPORT = portNumber;


        // 네트워크 연결 위해 필요 부분 ▼
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        arrayList = new ArrayList<String>();
        final EditText editText = (EditText) findViewById(R.id.editText);
        Button send = (Button) findViewById(R.id.send_button);

        mList = (ListView) findViewById(R.id.list);
        mAdapter = new MyCustomAdapter(this, arrayList);
        mList.setAdapter(mAdapter);


        // 서버와 연결
        new connectTask().execute("");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editText.getText().toString();
                // arrayList에 텍스트를 추가
                //arrayList.add("c: " + message);

                // 서버에 메세지를 보냄
                if (mClient != null) {
                    myChat = true;
                    mClient.sendMessage(message);
                }

                // 리스트를 refresh
                mAdapter.notifyDataSetChanged();
                editText.setText("");
            }
        });

    }

    public class connectTask extends AsyncTask<String, String, Client> {

        @Override
        protected Client doInBackground(String... message) {
            Log.e("=============doInBackground: ", " doInBackground 들어옴");
            //we create a Client object and
            mClient = new Client(new Client.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    Log.e("============ publishProgress: ", message);
                    publishProgress(message);
                }
            });
            mClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Log.e("============ onProgressUpdate: ", values[0]);
            //in the arrayList we add the messaged received from server
            arrayList.add(values[0]);
            // notify the adapter that the data set has changed. This means that new message received
            // from server was added to the list
            mAdapter.notifyDataSetChanged();
        }
    }

    //뒤로가기 버튼 눌렀을 때
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(getApplicationContext(), "문의센터를 나갑니다", Toast.LENGTH_SHORT).show();   //토스트 메시지
        Intent intent = new Intent(getApplicationContext(), MoDongSa.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // 서버에 나간다고 메세지를 보냄
        String done = "/quit";
        if (mClient != null) {
            myChat = true;
            mClient.sendMessage(done);
        }
        startActivity(intent);  //인텐트 이동
        finish();   //현재 액티비티 종료
    }
}