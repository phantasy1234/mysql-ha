package net.vickymedia.mysql.agent.zookeeper;

import net.vickymedia.mysql.agent.config.ZKProperty;
import org.junit.Before;
import org.junit.Test;

/**
 * User: weijie.song
 * Date: 16/2/4 下午1:34
 */
public class ZKClientTest {
	private ZKClient zkClient;

	@Before
	public void init() {
		ZKProperty zkProperty = new ZKProperty();
		zkProperty.setSessionTimeoutMs(10000);
		zkProperty.setConnectionTimeoutMs(10000);
		zkProperty.setBaseSleepTimeMs(1000);
		zkProperty.setMaxRetries(3);
		zkProperty.setZkNode("mysql/health");
		zkProperty.setZkServer("localhost:2181");
		zkClient = ZKClientFactory.builder().property(zkProperty).build().createZkClinet();
	}

	@Test
	public void setData() {
		zkClient.pushString("/isStop", "true");
	}
}
