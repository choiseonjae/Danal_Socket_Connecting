package chat.model.payload.pingpong;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import chat.model.Packet;
import chat.model.payload.Payload;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pong implements Serializable, Payload {

	private static final long serialVersionUID = 1L;
	
	public static final byte PONG_SUCCESS = (byte) 0xD1;
	
	private char[] time;
	private char[] userId;

	public Pong(char[] userId) {
		SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
		time = date.format(new Date()).toCharArray();
		this.userId = userId;
		print("생성한 Pong");
	}
	
	public static char[] size() {
		return Packet.createField(String.valueOf(34), 10);
	}
	
	public void print(String comment) {
		System.out.println();
		System.out.println("------------------- " + comment + " -------------------");
		Packet.print("Pong Time - ", time);
		Packet.print("Pong User ID - ", userId);
		System.out.println("---------------------------------------------------");
		System.out.println();
	}

}
