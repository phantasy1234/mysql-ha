package net.vickymedia.mysql.agent.service;

import com.google.common.collect.Lists;
import net.vickymedia.mysql.agent.config.ZKProperty;
import net.vickymedia.mysql.agent.model.Biz;
import net.vickymedia.mysql.agent.model.ShardInfo;
import net.vickymedia.mysql.agent.zookeeper.ZKClient;
import net.vickymedia.mysql.agent.zookeeper.ZKClientFactory;
import net.vickymedia.mysql.agent.zookeeper.ZKWatchProcesser;
import org.apache.curator.utils.ZKPaths;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * User: weijie.song
 * Date: 16/2/14 下午4:17
 */
public class ZKClientTest {
	private ZKClient zkClient;
	private Biz biz;

	@Before
	public void init() {
		ZKProperty zkProperty = new ZKProperty();
		zkProperty.setSessionTimeoutMs(10000);
		zkProperty.setConnectionTimeoutMs(10000);
		zkProperty.setBaseSleepTimeMs(1000);
		zkProperty.setMaxRetries(3);
		zkProperty.setZkNode("biz");
		zkProperty.setZkServer("localhost:2181");
		zkClient = ZKClientFactory.builder().property(zkProperty).build().createZkClinet();

		biz = new Biz();
		biz.setName("musical");
		biz.setShardCount(128);

		List<ShardInfo> list = Lists.newArrayList();
		list.add(new ShardInfo("127.0.0.1", "127.0.0.2"));
		list.add(new ShardInfo("127.0.0.3", "127.0.0.4"));
		biz.setShardInfoList(list);
	}

	@Test
	public void create() {
		zkClient.pushData(biz.getName(), biz);
	}

	@Test
	public void get() {
		Biz biz1 = zkClient.getData(biz.getName(), Biz.class);
		System.out.println(biz1.getShardCount());
	}

	@Test
	public void getChildren() {
		zkClient.getChildren("", 1, 10, Biz.class).forEach(System.out::println);
	}

	@Test
	public void register() {
		try {
			zkClient.register("", new ZKWatchProcesser() {
				@Override
				public void add(String node) {
					System.out.println("Node added: " + node);
				}

				@Override
				public void update(String node) {
					System.out.println("Node update: " + node);
				}

				@Override
				public void delete(String node) {
					System.out.println("Node delete: " + node);
				}
			});
			Thread.sleep(Long.MAX_VALUE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
