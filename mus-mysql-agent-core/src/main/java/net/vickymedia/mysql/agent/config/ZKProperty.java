package net.vickymedia.mysql.agent.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * User: weijie.song
 * Date: 16/2/3 下午6:17
 */
@Data
@ConfigurationProperties(prefix = "zookeeper")
public class ZKProperty {
	String zkServer;
	int sessionTimeoutMs;
	int connectionTimeoutMs;
	int baseSleepTimeMs;
	int maxRetries;
	String zkNode;
}
