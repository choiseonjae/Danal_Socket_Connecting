package chat.tcp.server;

import java.io.IOException;
import java.net.Socket;

import chat.model.IdleTime;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AutoDisconnect implements Runnable {

	private Socket socket;

	@Override
	public void run() {

		try {

			System.out.println("Auto Disconnect On");

			IdleTime.update();

			while (true) {

				// 거의 Semaphore 처럼 사용
				while (!IdleTime.isExceeded(60)) {
				}

				System.out.println("Idle Time is Exceeded -> Disconnect !");

				socket.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
