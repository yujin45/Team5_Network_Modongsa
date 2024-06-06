package smu.hw_network_team5_chatting_android;

import android.util.Log;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private String serverMessage;
    public static String SERVERIP; //서버 ip
    //public static final int SERVERPORT = 1009; // 포트번호 (서버와 동일하게 1009)
    public static int SERVERPORT = 8001; // 포트번호 (서버와 동일하게 1009)
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;

    PrintWriter out;
    BufferedReader in;

    // 클래스 생성자. OnMessagedReceived는 서버에서 받은 메시지를 수신
    public Client(OnMessageReceived listener) {
        mMessageListener = listener;

    }

    //@param message  text entered by client
    // 클라이언트가 입력한 메시지를 서버로 전송
    public void sendMessage(String message) {
        if (out != null && !out.checkError()) {
            out.println(message);
            out.flush();
        }
    }

    public void stopClient() {
        mRun = false;
    }

    public void run() {

        mRun = true;

        try {
            // 서버 IP를 넣어줘야 함
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);
            Log.e("serverAddr", serverAddr.toString());
            Log.e("TCP Client", "C: Connecting...");
            // 서버와 연결하기 위한 소켓 생성
            Socket socket = new Socket(serverAddr, SERVERPORT);
            Log.e("TCP Server IP", SERVERIP);

            try {
                // 서버에 메시지 보내기
                out = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())), true);
                Log.e("TCP Client", "C: Sent.");
                Log.e("TCP Client", "C: Done.");

                // 서버가 다시 보내는 메시지 수신
                //in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // 한글 되려면 utf-8필요
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));

                sendMessage("사용자");
                // 클라이언트가 서버에서 보낸 메시지를 수신하는 동안
                while (mRun) {
                    serverMessage = in.readLine();
                    Log.e("=============Client Get DATA: ", serverMessage);
                    // 여기에서 받는 부분
                    if (serverMessage != null && mMessageListener != null) {
                        // MyActivity에서 messageReceived 메서드를 호출
                        Log.e("TCP Client Get DATA: ", serverMessage);
                        mMessageListener.messageReceived(serverMessage);
                        ;
                    }
                    serverMessage = null;
                }
                Log.e("RESPONSE FROM SERVER", "S: Received Message: '"
                        + serverMessage + "'");

            } catch (Exception e) {
                Log.e("TCP", "S: Error", e);
            } finally {
                // 소켓을 닫아야 함 이 소켓이 닫힌 후 이 소켓에 다시 연결할 수 없음 즉, 새 소켓 인스턴스를 만들어야 함
                socket.close();
            }
        } catch (Exception e) {
            Log.e("TCP", "C: Error", e);
        }
    }

    // 인터페이스를 선언
    // messageReceived(String message)는 asynckTask 
    // ChattingActivity의 doInBackground로 구현되어야 함
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}