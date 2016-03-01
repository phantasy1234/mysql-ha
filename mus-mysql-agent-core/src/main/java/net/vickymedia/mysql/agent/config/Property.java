package net.vickymedia.mysql.agent.config;

import lombok.Data;

/**
 * User: weijie.song
 * Date: 16/2/4 上午11:26
 */
@Data
public class Property {
	ZKProperty zookeeper;
	MysqlProperty mysql;
	long waitTimeMs;
}
