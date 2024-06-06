// 모여봐요 동네 사람들(모동사) 서비스 관련 시연을 위한 코드 - 5조 네똑똑이 2012140 정유진, 1813935 김유정, 2013075 박근영

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

// 클라이언트가 채팅하는 화면을 관리해주는 부분
public class Team5_ServiceApp_ChatClient {

	// 채팅 클라이언트가 접근하는 부분
	static class ChatAccess extends Observable {
		private Socket socket;
		private OutputStream outputStream;

		@Override
		public void notifyObservers(Object arg) {
			super.setChanged();
			super.notifyObservers(arg);
		}

		// 소켓 생성 및 스레드 수신 부분
		public void InitSocket(String server, int port) throws IOException {
			socket = new Socket(server, port);
			outputStream = socket.getOutputStream();

			Thread receivingThread = new Thread() {
				@Override
				public void run() {
					try {
						BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						String line;
						while ((line = reader.readLine()) != null)
							notifyObservers(line);
					} catch (IOException ex) {
						notifyObservers(ex);
					}
				}
			};
			receivingThread.start();
		}

		private static final String CRLF = "\r\n"; // newline

		// 한줄 텍스트 보내기
		public void send(String text) {
			try {
				outputStream.write((text + CRLF).getBytes());
				outputStream.flush();
			} catch (IOException ex) {
				notifyObservers(ex);
			}
		}

		// 소켓 닫기
		public void close() {
			try {
				socket.close();
			} catch (IOException ex) {
				notifyObservers(ex);
			}
		}
	}

	// 채팅 화면 ui 부분
	static class ChatFrame extends JFrame implements Observer {

		private JTextArea textArea;
		private JTextField inputTextField;
		private JButton sendButton;
		private ChatAccess chatAccess;

		public ChatFrame(ChatAccess chatAccess) {
			this.chatAccess = chatAccess;
			chatAccess.addObserver(this);

			buildGUI();
		}

		// 화면 설계
		private void buildGUI() {

			textArea = new JTextArea(20, 50);
			textArea.setFont(new Font("맑은 고딕", Font.BOLD, 15));
			textArea.setBackground(new Color(208, 247, 255));
			textArea.setBounds(20, 20, 420, 420);
			textArea.setEditable(false);
			textArea.setLineWrap(true);
			add(new JScrollPane(textArea), BorderLayout.CENTER);

			Box box = Box.createHorizontalBox();
			add(box, BorderLayout.SOUTH);
			inputTextField = new JTextField();
			// sendButton = new JButton("Send");
			ImageIcon buttonIcon = new ImageIcon("sendImage.png");
			Image buttonImage = buttonIcon.getImage();
			buttonImage = buttonImage.getScaledInstance(100, 50, Image.SCALE_SMOOTH);
			buttonIcon = new ImageIcon(buttonImage);
			sendButton = new JButton(buttonIcon);
			sendButton.setBackground(new Color(255, 255, 255));
			box.add(inputTextField);
			box.add(sendButton);

			// 버튼 눌렀을 때 입력한 텍스트 보내는 부분
			ActionListener sendListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String str = inputTextField.getText();
					if (str != null && str.trim().length() > 0)
						chatAccess.send(str);
					inputTextField.selectAll();
					inputTextField.requestFocus();
					inputTextField.setText("");
				}
			};
			inputTextField.addActionListener(sendListener);
			sendButton.addActionListener(sendListener);

			this.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					chatAccess.close();
				}
			});
		}

		// Object argument에 따라 UI 업데이트
		public void update(Observable o, Object arg) {
			final Object finalArg = arg;
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					textArea.append(finalArg.toString());
					textArea.append("\n");
				}
			});
		}
	}

	//
	public static void main(String[] args) {
		String server = args[0];
		// int port = 1009;
		String centerName = args[1];
		int port = Integer.parseInt(args[2]);
		ChatAccess access = new ChatAccess();

		JFrame frame = new ChatFrame(access);
		frame.setTitle("5조 네똑똑이: +" + centerName + "- connected to " + server + ":" + port);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);

		try {
			access.InitSocket(server, port);
		} catch (IOException ex) {
			System.out.println("Cannot connect to " + server + ":" + port);
			ex.printStackTrace();
			System.exit(0);
		}

		access.send(centerName); // 기관 이름 보내서 이름으로 설정하게 함
	}
}