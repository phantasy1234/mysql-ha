package net.vickymedia.mysql.agent.jdbc;

import lombok.Data;

/**
 * User: weijie.song
 * Date: 16/2/4 上午11:02
 */
@Data
public class DBResult {
	boolean exist = Boolean.TRUE;
	long startTime;
	long costTime;
}
