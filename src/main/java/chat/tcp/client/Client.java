package chat.tcp.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import chat.model.IdleTime;
import chat.model.Packet;
import chat.model.header.Header;
import chat.model.payload.Payload;
import chat.model.payload.login.LoginRequest;
import chat.model.payload.login.LoginResponse;
import chat.model.payload.message.MessageRequest;
import chat.tcp.server.Server;

public class Client {

	public final static char[] USER_ID = "skhan@danal.co.kr".toCharArray();
	private final static char[] USER_PWD = "testpwd".toCharArray();
	private final static char[] VERSION = "DANAL-CHAT-1.0".toCharArray();

	private Socket socket;
	private ObjectOutputStream sender;
	private ObjectInputStream reader;

	// server -> connect
	public void connecting(String ip, int port) throws UnknownHostException, IOException {
		socket = new Socket(ip, port);
		sender = new ObjectOutputStream(socket.getOutputStream());
		reader = new ObjectInputStream(socket.getInputStream());
	}

	public void main_() {

		try {
			
			connecting("127.0.0.1", 8000);

			// 로그인 성공
			if (login(USER_ID, USER_PWD)) {

				@SuppressWarnings("resource")
				Scanner in = new Scanner(System.in);

				// Idle Time 30초 경과 시 Ping 전송 Thread
				new Thread(new AutoPingSender(sender, Client.USER_ID)).start();

				// Server로부터 오는 response
				new Thread(new Reader(reader)).start();

				// 보내고 싶은 메세지 전달
				while (true) {
					MessageRequest mr = new MessageRequest(Server.USER_ID, Client.USER_ID, in.nextLine().toCharArray());
					sender.writeObject(new Packet(new Header(Header.MSG_REQ, null), mr));
					sender.flush();

					// Idle Time Update
					IdleTime.update();
				}

			} else { // 로그인 실패
				System.err.println("로그인 실패");
				throw new RuntimeException();
			}

		} catch (Exception e) {
			System.err.println("Usage: java Client <hostname> <port:4000>");
			e.printStackTrace();
		} finally {
		}

	}

	public static void main(String[] args) {
		new Client().main_();
	}

	public boolean login(char[] userId, char[] userPwd) throws IOException, ClassNotFoundException {

		// 로그인 요청
		Payload request = new LoginRequest(USER_ID, USER_PWD, VERSION);
		sender.writeObject(new Packet(new Header(Header.LOGIN_REQ, null), request));
		sender.flush();

		while (true) {

			// Login 성공 여부 확인
			Packet response = (Packet) reader.readObject();
			Header header = response.getHeader();

			// 받은 response가 login response가 아니면 무시
			if (header.getH_TYPE() != Header.LOGIN_RES)
				continue;

			LoginResponse lResponse = (LoginResponse) response.getPayload();
			lResponse.print("수신한 Login Response");

			// 로그인 성공 여부 반환
			if (lResponse.getResCode() == LoginResponse.LOGIN_SUCCESS)
				return true;
			else
				return false;
		}
	}
}
