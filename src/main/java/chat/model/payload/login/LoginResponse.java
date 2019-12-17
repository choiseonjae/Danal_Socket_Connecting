package chat.model.payload.login;

import java.io.Serializable;
import java.util.function.Consumer;

import chat.model.DataBase;
import chat.model.Packet;
import chat.model.payload.Payload;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse implements Serializable, Payload {

	private static final long serialVersionUID = 1L;

	public static final byte LOGIN_SUCCESS = (byte) 0xE0;
	public static final String LOGIN_SUCCESS_RESERVED = "로그인에 성공했습니다. 1분간 아무런 응답이 없으면 연결이 종료됩니다.";
	public static final byte LOGIN_FAIL = (byte) 0xE1;
	public static final String LOGIN_FAIL_RESERVED = "ID 혹은 PW가 일치하지 않습니다. 다시 시도해 주세요.";
	public static final byte FORMAT_ERROR = (byte) 0xE2;
	public static final String FORMAT_ERROR_RESERVED = "형식에 맞지 않습니다. 형식에 맞게 다시 시도해 주세요.";
	public static final byte ETC_ERROR = (byte) 0xEE;
	public static final String ETC_ERROR_RESERVED = "에러가 발생했습니다. 다시 시도해 주세요.";
	public static final int RESERVED_SIZE = 50;

	private byte resCode;
	private char[] reserved = new char[50];

	public LoginResponse(Payload payload) {
		create((LoginRequest) payload);
		
		print("생성된 Login Response");
	}

	private void create(LoginRequest request) {

		// 응답 코드 별 다른 응답
		Consumer<Byte> runLoginResponse = type -> {
			// response 생성
			setResCode(type);
			setReserved(LoginResponse.getReserved(type));
		};

		if (!DataBase.isLogin(request.getUserId(), request.getUserPwd())) // ID, PW 불일치 -> 로그인 실패 응답
			runLoginResponse.accept(LoginResponse.LOGIN_FAIL);
		else if (!DataBase.isVersion(request.getVersion())) // 버전 불일치 -> 기타에러 응답
			runLoginResponse.accept(LoginResponse.ETC_ERROR);
		else // 로그인 성공 응답
			runLoginResponse.accept(LoginResponse.LOGIN_SUCCESS);

	}

	public static char[] getReserved(byte type) {
		if (type == LOGIN_SUCCESS)
			return Packet.createField(LOGIN_SUCCESS_RESERVED, RESERVED_SIZE);
		else if (type == LOGIN_FAIL)
			return Packet.createField(LOGIN_FAIL_RESERVED, RESERVED_SIZE);
		else if (type == FORMAT_ERROR)
			return Packet.createField(FORMAT_ERROR_RESERVED, RESERVED_SIZE);
		else if (type == ETC_ERROR)
			return Packet.createField(ETC_ERROR_RESERVED, RESERVED_SIZE);
		else
			return null;
	}

	public static char[] size() {
		return Packet.createField(String.valueOf(51), 10);
	}
	
	public void print(String comment) {
		System.out.println();
		System.out.println("------------------ " + comment + " -------------------");
		Packet.print("Login Response Code - ", resCode);
		Packet.print("Login Response reserved - ", reserved);
		System.out.println("---------------------------------------------------");
		System.out.println();
	}
}
