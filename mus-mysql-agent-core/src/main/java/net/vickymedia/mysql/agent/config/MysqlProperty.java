package net.vickymedia.mysql.agent.config;

import lombok.Data;

/**
 * User: weijie.song
 * Date: 16/2/4 上午10:24
 */
@Data
public class MysqlProperty {
	private final static String HEALTH_CHECK_SQL = "select * from schema_table_statistics";
	String host;
	int port;
	String user;
	String password;
	String dataSourceClassName;
	String database;
	String execSql = HEALTH_CHECK_SQL;
	long connectionTimeoutMs;
}
