// 모여봐요 동네 사람들(모동사) 서비스 관련 시연을 위한 코드 - 5조 네똑똑이 2012140 정유진, 1813935 김유정, 2013075 박근영

import java.io.PrintStream;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.*;

import java.net.InetSocketAddress;
import java.net.ServerSocket;

// 서버 관련 클래스
public class Team5_ServiceApp_ThreadChatSync {
	// 서버 소켓
	private static ServerSocket serverSocket = null;
	// 클라이언트 소켓
	private static Socket clientSocket = null;
	// 최대 클라이언트를 10명까지 가능하도록 설정해둠
	private static final int maxClientsCount = 10;
	private static final Team5_ServiceApp_ClientThread[] threads = new Team5_ServiceApp_ClientThread[maxClientsCount];
	// 포트넘버와 ip지정.
	static int portNumber = 8001; // 기본값은 8001이나 개방 포트에 따라 변경됨
	static String ipAdress = "{현재 서버의 IP주소 넣기}}";

	public static void main(String args[]) {
		if (args.length < 1) {
			System.out.println(
					"Usage: java MultiThreadChatServerSync <portNumber>\n" + "Now using port number=" + portNumber);
		} else {
			portNumber = Integer.valueOf(args[0]).intValue();
		}
		JFrame serverFrame = new JFrame("서버 실행중 : ip :" + ipAdress + " : portNumber : " + portNumber);
		serverFrame.setSize(500, 1);
		JLabel serverInfo = new JLabel("서버 ip: " + ipAdress + "\n포트번호: " + portNumber);
		serverFrame.add("Center", serverInfo);
		serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		serverFrame.setVisible(true);

		try {
			// 알아서 IP잡아서 실행시킨다면 ▼ 로컬
			// serverSocket = new ServerSocket(portNumber);

			// ip 번호를 지정해주기 위해서 아래 방식으로 사용
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(ipAdress, portNumber));
			System.out.println("ip: " + serverSocket.getInetAddress());

		} catch (IOException e) {
			System.out.println(e);
		}

		// 각 연결에 대한 클라이언트 소켓을 만들고 새 클라이언트 스레드에 전달
		while (true) {
			try {
				clientSocket = serverSocket.accept();
				if (serverSocket.isClosed()) {
					throw new SocketException("Socket is closed");
				}
				if (!serverSocket.isBound()) {
					throw new SocketException("Socket is not bound yet");
				}

				int i = 0;
				for (i = 0; i < maxClientsCount; i++) {
					if (threads[i] == null) {
						(threads[i] = new Team5_ServiceApp_ClientThread(clientSocket, threads)).start();
						break;
					}
				}
				if (i == maxClientsCount) {
					PrintStream os = new PrintStream(clientSocket.getOutputStream());
					os.println("Server too busy. Try later.");
					os.close();
					clientSocket.close();
				}

			} catch (IOException e) {
				System.out.println(e);
				System.out.println("확인 프린트--------------------");
				try {
					serverSocket.close();
					// serverSocket2.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				break;
			}
		}
	}
}
