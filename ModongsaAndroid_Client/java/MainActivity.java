package smu.hw_network_team5_chatting_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private Button connect;
    private EditText ipAdress;
    public static String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipAdress = (EditText) findViewById(R.id.editText1);
        connect = (Button) findViewById(R.id.button1);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String ip = ipAdress.getText().toString();
                Client.SERVERIP = ip;

                // Intent intent = new Intent(getApplicationContext(), ChattingActivity.class);
                Intent intent = new Intent(getApplicationContext(), MoDongSa.class);
                //Log.e("ServerIP", Client.SERVERIP);
                startActivity(intent);
            }
        });
    }
}