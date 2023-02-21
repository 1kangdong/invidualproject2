package Chatting;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client_GUI {
	public static void main(String[] args) {
		LoginGUI LG = new LoginGUI();
	}
}

class LoginGUI extends JFrame implements ActionListener {
	// 유저의 로그인 창
	private JPanel Login_GUIPanel = new JPanel();
	private JTextField NickName_Text = new JTextField(20);
	private JTextField Port_Text = new JTextField("####", 20);
	private JTextField IPAddress_Text = new JTextField("###.###.###.###", 20);
	private JLabel NickName_Label = new JLabel("아이디 입력");
	private JLabel Port_Label = new JLabel("포트 입력");
	private JLabel IPAddress_Label = new JLabel("IP 주소 입력");
	private JButton Login_GUI_Button = new JButton("접속!");

	public LoginGUI() {
		setTitle("로그인 화면");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setSize(300, 600);
		setResizable(false);
		setVisible(true);
		Login_GUI_Button.setPreferredSize(new Dimension(260, 300));
		Login_GUI_Button.addActionListener(this);
		Login_GUIPanel.add(NickName_Label);
		Login_GUIPanel.add(NickName_Text);
		Login_GUIPanel.add(Port_Label);
		Login_GUIPanel.add(Port_Text);
		Login_GUIPanel.add(IPAddress_Label);
		Login_GUIPanel.add(IPAddress_Text);
		Login_GUIPanel.add(Login_GUI_Button);
		add(Login_GUIPanel);
	}

	public void actionPerformed(ActionEvent e) {
		// 닉네임, 주소, 포트값을 버튼을 통해 입력받습니다.
		try {
			if (e.getSource() == Login_GUI_Button) {
				String NickName = NickName_Text.getText().trim();
				String IPAddress = IPAddress_Text.getText().trim();
				int Port = Integer.parseInt(Port_Text.getText().trim());
				new Client_ChatGUI(NickName, IPAddress, Port);
				setVisible(false);
			}
		} catch (Exception a) {
			// 만약 올바르지 않는 값이 입력되면 팝업창을 띄워줍니다.
			JOptionPane.showMessageDialog(null, "올바르지 않은 로그인입니다!");
		}
	}
}

class Client_ChatGUI extends JFrame implements ActionListener, KeyListener {
	//클라이언트용 채팅창
	String NickName;
	Client_Back CB = new Client_Back();
	JPanel ClientGUIPanel = new JPanel();
	JLabel UserLabel = new JLabel("유저 목록");
	JLabel User = new JLabel(NickName);
	JTextField Chat = new JTextField(50);
	JButton Enter = new JButton("전송");
	TextArea ChatList = new TextArea(30, 50);
	TextArea UserList = new TextArea(30, 15);

	public Client_ChatGUI(String NickName, String IPAddress, int Port) {
		this.NickName = NickName;

		setTitle("고객 창");
		setVisible(true);
		setLocationRelativeTo(null);
		setSize(750, 600);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ChatList.setEditable(false);
		UserList.setEditable(false);
		Chat.addKeyListener(this);
		Enter.addActionListener(this);

		ClientGUIPanel.add(User);
		ClientGUIPanel.add(ChatList);
		ClientGUIPanel.add(UserLabel);
		ClientGUIPanel.add(UserList);
		ClientGUIPanel.add(Chat);
		ClientGUIPanel.add(Enter);
		add(ClientGUIPanel);
		CB.setGui(this);
		CB.getUserInfo(NickName, IPAddress, Port);
		CB.start(); // 채팅창이 켜짐과 동시에 접속을 실행해줍니다.
	}

	public void actionPerformed(ActionEvent e) { 
		String Message = Chat.getText().trim();
		if (e.getSource() == Enter && Message.length() > 0) {
			CB.Transmit(NickName + " : " + Message + "\n");
			Chat.setText(null);
		}
	}

	public void keyPressed(KeyEvent e) { 
		String Message = Chat.getText().trim();
		if (e.getKeyCode() == KeyEvent.VK_ENTER && Message.length() > 0) {
			CB.Transmit(NickName + " : " + Message + "\n");
			Chat.setText(null);
		}
	}

	public void AppendMessage(String Message) {
		ChatList.append(Message);
	}

	public void AppendUserList(ArrayList NickName) {
		// 유저목록을 유저리스트에 띄워줍니다.
		String name;
		UserList.setText(null);
		for (int i = 0; i < NickName.size(); i++) {
			name = (String) NickName.get(i);
			UserList.append(name + "\n");
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}
}
