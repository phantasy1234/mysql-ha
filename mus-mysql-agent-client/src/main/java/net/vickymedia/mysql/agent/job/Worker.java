package net.vickymedia.mysql.agent.job;

import net.vickymedia.mysql.agent.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * User: weijie.song
 * Date: 16/2/4 上午11:00
 */
public class Worker implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(Worker.class);
	Processer processer;
	Config config;
	private static final int ISNUNNING = 1;
	private static final int STOP = 0;

	private static int runStatus = STOP;

	public Worker(Config config) {
		this.config = config;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(config.getProperty().getWaitTimeMs());
				if (runStatus != ISNUNNING) {
					runStatus = ISNUNNING;
					logger.info("the job {} running at {}", Thread.currentThread().getName(), new Date());
					processer.run();
					logger.info("the job {} stop at {}", Thread.currentThread().getName(), new Date());
					runStatus = STOP;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void setProcesser(Processer processer) {
		this.processer = processer;
	}
}
