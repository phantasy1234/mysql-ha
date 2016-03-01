package net.vickymedia.mysql.agent.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * User: weijie.song
 * Date: 16/2/14 下午2:06
 */
@AllArgsConstructor
@Data
public class ShardInfo {
	String masterIp;
	String slaveIp;

	public ShardInfo() {}
}
