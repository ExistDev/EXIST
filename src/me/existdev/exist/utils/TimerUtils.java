package me.existdev.exist.utils;

public class TimerUtils {

	private long lastMS = -1L;

	public TimerUtils() {
	}

	public void updateLastMS() {
		this.lastMS = System.currentTimeMillis();
	}

	public void updateLastMS(long plusMS) {
		this.lastMS += plusMS;
	}

	public boolean hasTimePassedM(long MS) {
		return System.currentTimeMillis() >= this.lastMS + MS;
	}

	public boolean hasTimePassedS(float speed) {
		return System.currentTimeMillis() >= this.lastMS + (long) (1000.0F / speed);
	}

}
