package chat.model.payload.message;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import chat.model.Packet;
import chat.model.payload.Payload;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequest implements Serializable, Payload {

	private static final long serialVersionUID = 1L;

	private char[] MSG_SEQ;
	private char[] TO_USER_ID;
	private char[] FROM_USER_ID;
	// 프린터에는 버전 정보를 담는다고 적혀있지만 Client가 보내고자하는 메세지를 담겠습니다.
	private char[] MSG_DATA;

	public MessageRequest(char[] toUserId, char[] fromUserId, char[] msgData) {
		// userId 와 현재 날짜 조합 -> 일련번호
		MSG_SEQ = createSerialNumber(toUserId);
		TO_USER_ID = toUserId;
		FROM_USER_ID = fromUserId;
		MSG_DATA = msgData;
		
		print("생성된 Message Request");


	}

	private char[] createSerialNumber(char[] toUserId) {
		SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
		return (String.valueOf(toUserId) + date.format(new Date())).toCharArray();
	}
	
	public static char[] size() {
		return Packet.createField(String.valueOf(2050), 10);
	}
	
	public void print(String comment) {
		System.out.println();
		System.out.println("------------------- " + comment + " -------------------");
		Packet.print("Message Reqeust Serial Number - ", MSG_SEQ);
		Packet.print("Message Reqeust To User ID - ", TO_USER_ID);
		Packet.print("Message Reqeust From User ID - ", FROM_USER_ID);
		Packet.print("Message Reqeust Data- ", MSG_DATA);
		System.out.println("---------------------------------------------------");
		System.out.println();
	}

}
