package chat.tcp.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import chat.model.IdleTime;
import chat.model.Packet;
import chat.model.header.Header;
import chat.model.payload.Payload;
import chat.model.payload.login.LoginRequest;
import chat.model.payload.login.LoginResponse;
import chat.model.payload.message.MessageRequest;
import chat.model.payload.message.MessageResponse;
import chat.model.payload.pingpong.Ping;
import chat.model.payload.pingpong.Pong;

public class ForkServer implements Runnable {

	// 객체 직렬화를 통한 소켓 통신 준비
	private Socket socket;
	private ObjectInputStream reader;
	private ObjectOutputStream sender;

	public ForkServer(Socket socket) {
		try {

			System.out.println("-- Fork Server Start --");
			this.socket = socket;
			reader = new ObjectInputStream(socket.getInputStream());
			sender = new ObjectOutputStream(socket.getOutputStream());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {

			while (true) {

				// 연결 후 첫 패킷
				Packet packet = (Packet) reader.readObject();
				Header header = packet.getHeader();

				// 연결 후 첫 패킷이 Login Request 이면서 Login 성공을 해야 다음으로 진행
				if (header.getH_TYPE() != Header.LOGIN_REQ || !response(header.getH_TYPE(), packet.getPayload()))
					continue;

				// 1분 이상 요청, ping 없을 시 연결 종료
				new Thread(new AutoDisconnect(socket)).start();

				// client msg 대기 - ping -> pong 반환, msg -> response 반환
				while (true) {

					// 항상 서버 측으로 부터 오는 메세지 대기
					packet = (Packet) reader.readObject();
					header = packet.getHeader();

					// request에 대한 반응
					response(header.getH_TYPE(), packet.getPayload());

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean response(byte type, Payload payload) throws IOException {

		try {

			switch (type) {
			case Header.LOGIN_REQ:

				// 패킷 안에 내용 확인
				LoginRequest lRequest = (LoginRequest) payload;

				// 응답할 Header 생성
				Header lHeader = new Header(Header.LOGIN_RES, LoginResponse.size());
				LoginResponse lResponse = new LoginResponse(lRequest);

				// Packet 생성
				Packet newPacket = new Packet(lHeader, lResponse);

				sender.writeObject(newPacket);
				sender.flush();
				IdleTime.update();

				return lResponse.getResCode() == LoginResponse.LOGIN_SUCCESS ? true : false;

			case Header.MSG_REQ:
				MessageRequest mRequest = (MessageRequest) payload;

				mRequest.print("수신한 Message Request");

				// 반환할 MSG_RES
				MessageResponse mResponse = new MessageResponse(mRequest);
				sender.writeObject(new Packet(new Header(Header.MSG_RES, MessageResponse.size()), mResponse));
				sender.flush();

				return mResponse.getResCode() == MessageResponse.SEND_SUCCESS ? true : false;
			case Header.PING:
				Ping ping = (Ping) payload;
				ping.print("수신한 Ping");

				System.out.println("Pong !");

				Pong pong = new Pong(Server.USER_ID);
				sender.writeObject(new Packet(new Header(Header.PONG, Pong.size()), pong));
				sender.flush();

				return true;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;

	}
}
