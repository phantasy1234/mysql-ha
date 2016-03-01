package net.vickymedia.mysql.agent.job;

import lombok.Builder;
import net.vickymedia.mysql.agent.config.Config;
import net.vickymedia.mysql.agent.heartbeat.DBHealthChecker;
import net.vickymedia.mysql.agent.listener.DBHealthListener;
import net.vickymedia.mysql.agent.zookeeper.ZKClient;
import net.vickymedia.mysql.agent.zookeeper.ZKClientFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User: weijie.song
 * Date: 16/2/4 上午11:03
 */
@Builder
public class Processer {
	private static volatile AtomicBoolean isFirst = new AtomicBoolean(Boolean.TRUE);
	private ZKClient zkClient;
	private DBHealthChecker healthChecker;
	private Config config;

	public void run() {
		if (isFirst == null || isFirst.get() == Boolean.TRUE) {
			init();
		}
		try {
			DBHealthListener dbHealthListener = new DBHealthListener();
			healthChecker.setDbHealthListener(dbHealthListener);
			healthChecker.check();
			String path = "/" + config.getMysqlProperty().getHost();
			zkClient.pushString(path, config.getMysqlProperty().getHost());
			zkClient.push(path, healthChecker.getDbHealthListener().getDbResult());
		} catch (Exception e) {
			e.printStackTrace();
			isFirst.compareAndSet(Boolean.FALSE, Boolean.TRUE);
		}

	}

	private void init() {
		zkClient = ZKClientFactory.builder().property(config.getZKProperty()).build().createZkClinet();
		healthChecker = DBHealthChecker.builder().property(config.getMysqlProperty()).build();
		isFirst.compareAndSet(Boolean.TRUE, Boolean.FALSE);
	}
}
