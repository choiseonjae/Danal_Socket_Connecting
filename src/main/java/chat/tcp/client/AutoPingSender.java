package chat.tcp.client;

import java.io.IOException;
import java.io.ObjectOutputStream;

import chat.model.Packet;
import chat.model.IdleTime;
import chat.model.header.Header;
import chat.model.payload.pingpong.Ping;

public class AutoPingSender implements Runnable {

	private ObjectOutputStream sender;
	private char[] userId;

	public AutoPingSender(ObjectOutputStream sender, char[] userId) {
		this.sender = sender;
		this.userId = userId;
	}

	@Override
	public void run() {
		try {

			System.out.println("Auto Ping On");

			IdleTime.update();

			while (true) {

				// 거의 Semaphore 처럼 사용
				while (!IdleTime.isExceeded(30)) {
				}

				// Ping 전송
				sender.writeObject(new Packet(new Header(Header.PING, null), new Ping(userId)));
				System.out.println("Ping !");

				// Idle Time Update
				IdleTime.update();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
