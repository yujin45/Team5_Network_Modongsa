
// 모여봐요 동네 사람들(모동사) 서비스 관련 시연을 위한 코드 - 5조 네똑똑이 2012140 정유진, 1813935 김유정, 2013075 박근영
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;

// 클라이언트가 연결되면 이 class를 호출함
public class Team5_ServiceApp_ClientThread extends Thread {
	private String clientName = null;
	// private DataInputStream is = null;// *point* 이렇게 하면 한글 전송이 안됨
	private BufferedReader is = null;
	private PrintStream os = null;
	private Socket clientSocket = null;
	private final Team5_ServiceApp_ClientThread[] threads;
	private int maxClientsCount;

	public Team5_ServiceApp_ClientThread(Socket clientSocket, Team5_ServiceApp_ClientThread[] threads) {
		this.clientSocket = clientSocket;
		this.threads = threads;
		maxClientsCount = threads.length;
	}

	public void run() {
		int maxClientsCount = this.maxClientsCount;
		Team5_ServiceApp_ClientThread[] threads = this.threads;

		try {
			// 이 클라이언트를 위한 입출력 관련 스트림, 버퍼를 여는 부분
			// is = new DataInputStream(clientSocket.getInputStream()); // *point* 이렇게 하면 한글
			// 전송이 안됨
			is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "utf-8"));
			os = new PrintStream(clientSocket.getOutputStream());
			String name;
			while (true) {
				// os.println("이름을 입력해주세요(Enter your name)");
				name = is.readLine().trim();
				if (name.indexOf('@') == -1) {
					break;
				} else {
					os.println("The name should not contain '@' character.");
				}
			}

			// 새로운 client 입장시 반기는 부분 채팅 프로그램에서는 사용했으나 서비스에서는 사용하지 않아 주석처리
			/*
			 * os.println("어서오십쇼 " + name + "님\n 네트워크 5조 네똑똑이의 채팅방에 오신걸 환영합니다." +
			 * "\n(/quit 혹은 /끝 입력시 채팅방을 떠날 수 있습니다)");
			 */
			synchronized (this) {
				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] != null && threads[i] == this) {
						clientName = "@" + name;
						break;
					}
				}
				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] != null && threads[i] != this) {
						threads[i].os.println("*** 문의하러 오신 " + name + "님이 입장하셨습니다 ! ***");
					}
				}
			}
			// 대화 관련 부분 시작
			while (true) {
				String line = is.readLine();
				if (line.startsWith("/quit") || line.startsWith("/끝")) {
					// "/quit" 혹은 "/끝"입력하면 사용자 나감
					break;
				}
				// @이름 하면 해당 이름한테만 메세지 가는 귓속말 기능 부분
				if (line.startsWith("@")) {
					String[] words = line.split("\\s", 2);
					if (words.length > 1 && words[1] != null) {
						words[1] = words[1].trim();
						if (!words[1].isEmpty()) {
							synchronized (this) {
								for (int i = 0; i < maxClientsCount; i++) {
									if (threads[i] != null && threads[i] != this && threads[i].clientName != null
											&& threads[i].clientName.equals(words[0])) {
										threads[i].os.println("<" + name + "> " + words[1]);
										// 귓속말 보낸 사람은 내가 지금 보낸게 귓속말이라는 표시로 > > 이거 표시 사용
										this.os.println(">" + name + "> " + words[1]);
										break;
									}
								}
							}
						}
					}
				} else {
					// 메세지가 다른 모든 클라이언트에게 브로드캐스트되는 부분
					synchronized (this) {
						for (int i = 0; i < maxClientsCount; i++) {
							if (threads[i] != null && threads[i].clientName != null) {
								threads[i].os.println("<" + name + "> " + line);
							}
						}
					}
				}
			}
			synchronized (this) {
				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] != null && threads[i] != this && threads[i].clientName != null) {
						threads[i].os.println("*** 문의하러 오신 " + name + "님이 문의센터를 떠나십니다 ! ***");
					}
				}
			}
			os.println("*** 잘가요~ " + name + "님 ***");

			// clean up부분. 현재 스레드를 null로 설정해서 새로운 client를 서버에서 받을 수 있음
			synchronized (this) {
				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] == this) {
						threads[i] = null;
					}
				}
			}
			// 입축력 관련 및 소켓 닫는 부분
			is.close();
			os.close();
			clientSocket.close();
		} catch (IOException e) {
		}
	}
}
