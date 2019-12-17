package chat.model.payload.login;

import java.io.Serializable;

import chat.model.Packet;
import chat.model.payload.Payload;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest implements Serializable, Payload {

	private static final long serialVersionUID = 1L;

	private char[] userId = new char[20];
	private char[] userPwd = new char[10];
	private char[] version = new char[50];

	public LoginRequest(char[] userId, char[] userPwd, char[] version) {
		this.userId = userId;
		this.userPwd = userPwd;
		this.version = version;
		
		print("생성된 Login Request");
	}

	public static char[] size() {
		return Packet.createField(String.valueOf(80), 10);
	}

	public void print(String comment) {
		System.out.println();
		System.out.println("------------------- " + comment + " -------------------");
		Packet.print("Login User ID - ", userId);
		Packet.print("Login Reqeust User Pwd - ", userPwd);
		Packet.print("Login Reqeust Version - ", version);
		System.out.println("---------------------------------------------------");
		System.out.println();
	}

}
