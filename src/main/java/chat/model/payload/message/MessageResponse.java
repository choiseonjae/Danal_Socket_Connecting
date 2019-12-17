package chat.model.payload.message;

import java.io.Serializable;

import chat.model.Packet;
import chat.model.payload.Payload;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse implements Serializable, Payload {

	private static final long serialVersionUID = 1L;

	public static final byte SEND_SUCCESS = (byte) 0xF0;
	public static final byte WRONG_RECEIVE_ID = (byte) 0xF1;
	public static final byte WRONG_SEND_ID = (byte) 0xF2;
	public static final byte MSG_ORMAT_ERROR = (byte) 0xF3;
	public static final byte MSG_ETC_ERROR = (byte) 0xFE;

	private char[] msgSeq;
	private byte resCode;
	private char[] reserved;

	public MessageResponse(Payload payload) {
		create((MessageRequest) payload);
	}

	public void create(MessageRequest mRequest) {
		// 잘못된 수신, 발신, 포맷, 기타 에러는 지금 고려할 수 없는 조건
		setMsgSeq(mRequest.getMSG_SEQ());
		setReserved(Packet.createField("MSG_RES: 서버의 메세지 수신 성공", 50));
		setResCode(SEND_SUCCESS);

		print("생성된 Message Request");
	}

	public static char[] size() {
		return Packet.createField(String.valueOf(71), 10);
	}

	public void print(String comment) {
		System.out.println();
		System.out.println("------------------- " + comment + " -------------------");
		Packet.print("Message Response - ", msgSeq);
		Packet.print("Message Response Code - ", resCode);
		Packet.print("Message reserved - ", reserved);
		System.out.println("---------------------------------------------------");
		System.out.println();
	}

}
