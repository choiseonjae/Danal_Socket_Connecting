package chat.model;

import lombok.Getter;

@Getter
public class DataBase {

	// Server 에서 확인할 정보들
	private final static char[] USER_ID = "skhan@danal.co.kr".toCharArray();
	private final static char[] USER_PWD = "testpwd".toCharArray();
	private final static char[] VERSION = "DANAL-CHAT-1.0".toCharArray();

	public static boolean isLogin(char[] userId, char[] userPwd) {
		return match(userId, USER_ID) && match(userPwd, USER_PWD); 
	}

	public static boolean isVersion(char[] version) {
		return match(version, VERSION);
	}

	private static boolean match(char[] a, char[] b) {
		if (a.length != b.length)
			return false;

		for (int idx = 0; idx < a.length; idx++)
			if (a[idx] != b[idx])
				return false;

		return true;
	}

}
