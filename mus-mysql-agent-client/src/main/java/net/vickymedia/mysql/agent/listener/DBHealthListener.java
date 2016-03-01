package net.vickymedia.mysql.agent.listener;

import lombok.Getter;
import net.vickymedia.mysql.agent.jdbc.DBResult;
import org.apache.commons.lang3.time.StopWatch;

/**
 * User: weijie.song
 * Date: 16/2/4 下午5:45
 */

public class DBHealthListener {
	@Getter
	DBResult dbResult = new DBResult();
	StopWatch stopWatch = new StopWatch();

	public void start() {

		stopWatch.start();
		Collector.run();
		dbResult.setStartTime(System.currentTimeMillis());
	}

	public void end() {
		stopWatch.stop();
		Collector.success();
		dbResult.setCostTime(stopWatch.getTime());
	}

	public void error() {
		Collector.failed();
		dbResult.setExist(Boolean.FALSE);
	}
}
