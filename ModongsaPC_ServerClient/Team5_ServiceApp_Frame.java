
// 모여봐요 동네 사람들(모동사) 서비스 관련 시연을 위한 코드 - 5조 네똑똑이 2012140 정유진, 1813935 김유정, 2013075 박근영
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Team5_ServiceApp_Frame extends JFrame implements ActionListener {
	JPanel imagePanel, buttonPanel;
	ImageIcon mainImage;
	Image team5MainImage;
	JLabel mainLabel;
	JButton serverBTN, clientBTN;

	public Team5_ServiceApp_Frame() {
		setTitle("네트워크 5조 네똑똑이 모동사 서비스 - 정유진/김유정/박근영");
		setLayout(new BorderLayout());
		setSize(520, 550);
		// 이미지CENTER에 넣을 패널 만들어주기
		imagePanel = new JPanel(new BorderLayout());
		mainImage = new ImageIcon("team5AppMainImage.png");
		team5MainImage = mainImage.getImage();
		team5MainImage = team5MainImage.getScaledInstance(500, 500, Image.SCALE_SMOOTH);
		mainImage = new ImageIcon(team5MainImage);
		mainLabel = new JLabel(mainImage);
		imagePanel.add(mainLabel);

		// 버튼 넣을 패널 만들어주기
		buttonPanel = new JPanel(new GridLayout(1, 2));
		serverBTN = new JButton("Server - 포트 열기");
		clientBTN = new JButton("Client - 문의 센터 열기");
		serverBTN.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		clientBTN.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		serverBTN.setBackground(new Color(220, 191, 255));
		clientBTN.setBackground(new Color(255, 191, 222));
		serverBTN.addActionListener(this);
		clientBTN.addActionListener(this);
		buttonPanel.add(serverBTN);
		buttonPanel.add(clientBTN);

		add("Center", imagePanel);
		add("South", buttonPanel);
		setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == serverBTN) {

			String portNumber = JOptionPane.showInputDialog("개방할 포트 번호를 입력하세요");
			if (portNumber != null) {
				setVisible(false);// 보여주는건 사라지게 하고 돌기는 돌아
				String[] arguments = new String[] { portNumber };
				new Team5_ServiceApp_ThreadChatSync().main(arguments);
			}

			setVisible(false);
		} else if (e.getSource() == clientBTN) {
			String IPServer = JOptionPane.showInputDialog("모동사 서버 IP를 입력하세요");
			String centerName = JOptionPane.showInputDialog("기관명을 입력해주세요");
			String portNumber = JOptionPane.showInputDialog("모동사에서 부여받은 portNumber를 입력하세요");

			if (IPServer != null && portNumber != null && centerName != null) {
				setVisible(false);
				String[] arguments = new String[] { IPServer, centerName, portNumber };
				new Team5_ServiceApp_ChatClient().main(arguments);
			}

		}

	}
}
