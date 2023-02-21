package Chatting;

import java.net.*;
import java.io.*;
import java.util.*;

public class Server_Back extends Thread {
	Vector<ReceiveInfo> ClientList = new Vector<ReceiveInfo>(); // Ŭ���̾�Ʈ�� �����带 �������ݴϴ�.
	ArrayList<String> NickNameList = new ArrayList<String>(); // Ŭ���̾�Ʈ�� �г����� �������ݴϴ�.
	ServerSocket serversocket;
	Socket socket;
	private Server_ChatGUI serverchatgui;

	public void setGUI(Server_ChatGUI serverchatgui) {
		this.serverchatgui = serverchatgui;
	}

	public void Start_Server(int Port) {
		try {
			Collections.synchronizedList(ClientList); // ���������� ���ش�.( clientList�� ��Ʈ��ũ ó�����ִ°� )
			serversocket = new ServerSocket(Port); // ������ �Էµ� Ư�� Port�� ������ �㰡�ϱ� ���� ����߽��ϴ�.
			System.out.println("���� �����ǿ� ��Ʈ�ѹ��� [" + InetAddress.getLocalHost() + "], [" + Port + "] �Դϴ�.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void run() {
		try {
			NickNameList.add("Admin"); // ��������� ù ��° ����(Admin)�� �߰��մϴ�.
			while (true) {
				System.out.println("�� ������ ����մϴ�...");
				socket = serversocket.accept(); // ��Ʈ ��ȣ�� ��ġ�� Ŭ���̾�Ʈ�� ������ �޽��ϴ�.
				System.out.println("[" + socket.getInetAddress() + "]���� �����ϼ̽��ϴ�.");
				ReceiveInfo receive = new ReceiveInfo(socket);
				ClientList.add(receive);
				receive.start();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void Transmitall(String Message) {
		// ��� Ŭ���̾�Ʈ�鿡�� �޼����� �������ݴϴ�.
		for (int i = 0; i < ClientList.size(); i++) {
			try {
				ReceiveInfo ri = ClientList.elementAt(i);
				ri.Transmit(Message);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public void removeClient(ReceiveInfo Client, String NickName) {
		// ������ ���� �߻��� ��Ͽ��� �����ϴ� ������ �մϴ�.
		ClientList.removeElement(Client);
		NickNameList.remove(NickName);
		System.out.println(NickName + "�� ���� �Ϸ��߽��ϴ�.");
		serverchatgui.UserList.setText(null);
		serverchatgui.AppendUserList(NickNameList);
	}

	class ReceiveInfo extends Thread { 
		// �� ��Ʈ��ũ(Ŭ���̾�Ʈ)�κ��� ������ �޾� �ٽ� �������� ������ �մϴ�.
		private DataInputStream in;
		private DataOutputStream out;
		String NickName;
		String Message;

		public ReceiveInfo(Socket socket) {
			try {
				out = new DataOutputStream(socket.getOutputStream()); // Output
				in = new DataInputStream(socket.getInputStream()); // Input
				NickName = in.readUTF();
				NickNameList.add(NickName);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}

		public void run() {
			try {
				// ���ο� ���� �߻��� ��������� �ʱ�ȭ �մϴ�.
				// �Ŀ� ���Ӱ� ��������� �Է����ݴϴ�.
				// ����, ���ο� ������ ���������� ��� Ŭ���̾�Ʈ���� �����մϴ�.
				serverchatgui.UserList.setText(null); 
				serverchatgui.AppendUserList(NickNameList);
				Transmitall(NickName + "���� �����ϼ̽��ϴ�.\n");
				for (int i = 0; i < NickNameList.size(); i++) {
					// +++�г����ǽ���+++�� �ش� ���� �г������� �˰����ִ� �ĺ����̸�
					// ���������� ���� ��ȣȭ�� ���� ������� ȥ�� �߻��� �����ݴϴ�.
					Transmitall("+++�г����ǽ���+++" + NickNameList.get(i));
				}
				serverchatgui.AppendMessage(NickName + "���� �����ϼ̽��ϴ�.\n");
				while (true) {
					Message = in.readUTF();
					serverchatgui.AppendMessage(Message);
					Transmitall(Message);
				}
			} catch (Exception e) {
				// ������ ������ �����ϸ� ���⼭ ������ �߻��մϴ�.
				// ���� �߻��� ���� �ٽ� ��� Ŭ���̾�Ʈ �鿡�� ���۽����ݴϴ�.
				System.out.println(NickName + "���� �����ϼ̽��ϴ�.");
				removeClient(this, NickName);
				Transmitall(NickName + "���� �����ϼ̽��ϴ�.\n");
				for (int i = 0; i < NickNameList.size(); i++) {
					Transmitall("+++�г����ǽ���+++" + NickNameList.get(i));
				}
				serverchatgui.AppendMessage(NickName + "���� �����ϼ̽��ϴ�.\n");
			}
		}

		public void Transmit(String Message) {
			// ���޹��� ��(Message)�� �� Ŭ���̾�Ʈ�� �����忡 ���� �����մϴ�.
			try {
				out.writeUTF(Message);
				out.flush();
			} catch (Exception e) {
				e.getStackTrace();
			}

		}
	}
}
