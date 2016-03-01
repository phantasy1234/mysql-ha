package net.vickymedia.mysql.agent;

import net.vickymedia.mysql.agent.config.Config;
import net.vickymedia.mysql.agent.job.Processer;
import net.vickymedia.mysql.agent.job.Worker;
import org.apache.commons.lang3.StringUtils;

/**
 * User: weijie.song
 * Date: 16/2/4 上午10:59
 */
public class Application {

	public static void main(String[] args) {
		String configFilePath = args[0];
		Config config = Config.builder().configFilePath(configFilePath).build().load();
		Worker worker = new Worker(config);
		worker.setProcesser(Processer.builder().config(config).build());
		worker.run();
	}
}
