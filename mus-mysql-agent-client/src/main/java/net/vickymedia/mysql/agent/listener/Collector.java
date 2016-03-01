package net.vickymedia.mysql.agent.listener;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: weijie.song
 * Date: 16/2/4 下午5:49
 */
public class Collector {
	static AtomicInteger successTimes = new AtomicInteger(0);
	static AtomicInteger errorTimes = new AtomicInteger(0);
	static AtomicInteger runTimes = new AtomicInteger(0);

	public static void run() {
		runTimes.incrementAndGet();
	}

	public static void success() {
		successTimes.incrementAndGet();
	}

	public static void failed() {
		errorTimes.incrementAndGet();
	}
}
