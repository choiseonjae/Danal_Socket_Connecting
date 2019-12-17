package chat.tcp.client;

import java.io.IOException;
import java.io.ObjectInputStream;

import chat.model.IdleTime;
import chat.model.Packet;
import chat.model.header.Header;
import chat.model.payload.Payload;
import chat.model.payload.message.MessageResponse;
import chat.model.payload.pingpong.Pong;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Reader implements Runnable {

	private ObjectInputStream reader;

	@Override
	public void run() {
		try {

			while (true) {

				// 항상 서버 측으로 부터 오는 메세지 대기
				Packet message = (Packet) reader.readObject();

				// Idle Time Update
				IdleTime.update();

				Header header = message.getHeader();
				Payload payload = (Payload) message.getPayload();

				print(header.getH_TYPE(), payload);
			}

		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	public void print(byte type, Payload response) {

		switch (type) {
		case Header.MSG_RES:

			// MSG 발송 성공 여부
			MessageResponse mRes = (MessageResponse) response;
			mRes.print("수신한 Message Response");

			break;
		case Header.PONG:
			Pong pong = (Pong) response;
			pong.print("수신한 Pong");

			break;
		}

	}

}
