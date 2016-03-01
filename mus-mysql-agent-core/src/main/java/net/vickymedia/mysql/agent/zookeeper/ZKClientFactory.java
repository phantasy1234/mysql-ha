package net.vickymedia.mysql.agent.zookeeper;

import lombok.Builder;
import lombok.Getter;
import net.vickymedia.mysql.agent.config.ZKProperty;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * User: weijie.song
 * Date: 16/2/3 下午6:11
 */
@Builder
@Getter
public class ZKClientFactory {
	private ZKProperty property;

	public CuratorFramework createCuratorClinet() {
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString(property.getZkServer())
				.connectionTimeoutMs(property.getConnectionTimeoutMs())
				.sessionTimeoutMs(property.getSessionTimeoutMs())
				.retryPolicy( new ExponentialBackoffRetry(property.getBaseSleepTimeMs(), property.getMaxRetries()))
				.namespace(property.getZkNode())
				.build();
		client.start();
		return client;
	}

	public ZKClient createZkClinet() {
		return new ZKClient(createCuratorClinet());
	}
}
