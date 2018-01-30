package me.existdev.exist.utils.helper;

import java.util.concurrent.ThreadLocalRandom;

public class TimeHelper {

	public int getRandomInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max);
	}

	private long lastMS = 0L;

	public long getCurrentMS() {
		return System.nanoTime() / 10000000L;
	}

	public boolean hasReached(long milliseconds) {
		return getCurrentMS() - lastMS >= milliseconds;
	}

	public void reset() {
		lastMS = getCurrentMS();
	}

}
