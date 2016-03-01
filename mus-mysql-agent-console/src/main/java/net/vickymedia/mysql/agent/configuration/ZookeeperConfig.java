package net.vickymedia.mysql.agent.configuration;

import net.vickymedia.mysql.agent.config.ZKProperty;
import net.vickymedia.mysql.agent.zookeeper.ZKClient;
import net.vickymedia.mysql.agent.zookeeper.ZKClientFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * User: weijie.song
 * Date: 16/2/14 下午2:16
 */
@Configuration
@EnableConfigurationProperties({ ZKProperty.class })
public class ZookeeperConfig {
	@Resource
	ZKProperty zkProperty;

	@Bean(name = "zkClient")
	ZKClient musicalCache() {
		return ZKClientFactory.builder().property(zkProperty).build().createZkClinet();
	}
}
