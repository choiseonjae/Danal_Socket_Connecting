package chat.model.header;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Header implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final byte LOGIN_REQ = (byte) 0xA1;
	public static final byte LOGIN_RES = (byte) 0xA2;
	public static final byte MSG_REQ = (byte) 0xB1;
	public static final byte MSG_RES = (byte) 0xB2;
	public static final byte PING = (byte) 0xC1;
	public static final byte PONG = (byte) 0xC2;

	private byte H_TYPE;
	private char[] P_SIZE = new char[10];

}
