package net.vickymedia.mysql.agent.model;

import lombok.Data;

import java.util.List;

/**
 * User: weijie.song
 * Date: 16/2/14 下午2:04
 */
@Data
public class Biz {
	String name;
	int shardCount;
	List<ShardInfo> shardInfoList;
}
