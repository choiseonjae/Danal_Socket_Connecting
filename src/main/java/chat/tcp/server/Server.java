package chat.tcp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public final static char[] USER_ID = "server@danal.co.kr".toCharArray();
	private final static int SERVER_PORT = 8000;

	public static void main(String[] args) throws IOException {
		new Server().on();
	}

	public void on() throws IOException {

		// 서버 소켓 생성
		@SuppressWarnings("resource")
		ServerSocket serverSocket = new ServerSocket(SERVER_PORT);

		while (true) {

			System.out.println("-- Client 대기 중 --");
			Socket socket = serverSocket.accept();
			System.out.println("Client Connecting Success");

			// 연결되면 해당 클라이언트와 통신
			Thread fork = new Thread(new ForkServer(socket));
			fork.start();

		}

	}

}