package Chatting;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server_GUI {

	public static void main(String[] args) {
		new ManagerLogin();
	}
}

class ManagerLogin extends JFrame implements ActionListener, KeyListener { 
	// 로그인 창
	Server_ChatGUI Server_chat = null;
	JPanel Port_Log = new JPanel();
	JLabel Port_Label = new JLabel("입력을 허용할 포트 번호를 입력하세요.");
	JLabel Port_Warning = new JLabel("(단, 포트 번호는 0 ~ 65535까지)");
	JTextField Port_Text = new JTextField(20);
	JButton Port_Enter = new JButton("접속!");

	public ManagerLogin() {
		setTitle("서버 메니저 창");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 닫았을 때 메모리에서 제거되도록 설정합니다.
		setSize(300, 600);
		setVisible(true);
		setResizable(false);
		Port_Enter.addActionListener(this);
		Port_Text.addKeyListener(this);
		Port_Log.add(Port_Label);
		Port_Log.add(Port_Warning);
		Port_Log.add(Port_Text);
		Port_Log.add(Port_Enter);
		add(Port_Log);
	}

	public void actionPerformed(ActionEvent e) { 
		// "접속!" 버튼을 누르면 작동이 됩니다.
		try {
			int Port = Integer.parseInt(Port_Text.getText().trim());
			if (e.getSource() == Port_Enter) {
				Server_chat = new Server_ChatGUI(Port);
				setVisible(false);
			}
		} catch (Exception a) {
			JOptionPane.showMessageDialog(null, "올바르지 않은 입력입니다!");
		}
	}

	public void keyPressed(KeyEvent e) { 
		// 텍스트필드에 값을 입력한 후 엔터키를 누르면 작동이 됩니다.
		try {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				int Port = Integer.parseInt(Port_Text.getText().trim());
				Server_chat = new Server_ChatGUI(Port);
				setVisible(false);
			}
		} catch (Exception a) {
			JOptionPane.showMessageDialog(null, "올바르지 않은 입력입니다!");
		}

	}

	public void keyTyped(KeyEvent e) { // 불필요
	}

	public void keyReleased(KeyEvent e) { // 불필요
	}

}

class Server_ChatGUI extends JFrame implements ActionListener, KeyListener {
	// 서버용 채팅창
	JPanel ServerGUI_Panel = new JPanel();
	JLabel ServerLabel = new JLabel("Main Server");
	JLabel UserLabel = new JLabel("유저 목록");
	JTextField Chat = new JTextField(45);
	JButton Enter = new JButton("전송");
	TextArea ServerChatList = new TextArea(30, 50);
	TextArea UserList = new TextArea(30, 15);
	Server_Back SB = new Server_Back();

	public Server_ChatGUI(int Port) {
		setTitle("메인 서버");
		setVisible(true);
		setLocationRelativeTo(null);
		setSize(750, 650);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 닫았을 때 메모리에서 제거되도록 설정합니다.

		ServerChatList.setEditable(false);
		UserList.setEditable(false);
		Chat.addKeyListener(this);
		Enter.addActionListener(this);

		ServerGUI_Panel.add(ServerLabel);
		ServerGUI_Panel.add(ServerChatList);
		ServerGUI_Panel.add(UserLabel);
		ServerGUI_Panel.add(UserList);
		ServerGUI_Panel.add(Chat);
		ServerGUI_Panel.add(Enter);
		add(ServerGUI_Panel);

		UserList.append("Admin\n");
		SB.setGUI(this);
		SB.Start_Server(Port);
		SB.start();
	}

	public void actionPerformed(ActionEvent e) {
		String Message = Chat.getText().trim();
		if (e.getSource() == Enter && Message.length() > 0) {
			AppendMessage("서버 : " + Message + "\n");
			SB.Transmitall("서버 : " + Message + "\n");
			Chat.setText(null);
		}
	}

	public void keyPressed(KeyEvent e) {
		String Message = Chat.getText().trim();
		if (e.getKeyCode() == KeyEvent.VK_ENTER && Message.length() > 0) {
			AppendMessage("서버 : " + Message + "\n");
			SB.Transmitall("서버 : " + Message + "\n");
			Chat.setText(null);
		}
	}

	public void AppendMessage(String Message) {
		ServerChatList.append(Message);
	}

	public void AppendUserList(ArrayList NickName) {
		String name;
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
