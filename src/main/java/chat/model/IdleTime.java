package chat.model;

public class IdleTime {
	
	private static long lastUpdateTime;
	
	public static void update() {
		lastUpdateTime = System.currentTimeMillis();
	}
	
	public static boolean isExceeded(int time) {
		return ((System.currentTimeMillis() - lastUpdateTime) / 1000.0) > time ? true : false;
	}

}
