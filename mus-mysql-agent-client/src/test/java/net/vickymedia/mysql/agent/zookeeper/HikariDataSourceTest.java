package net.vickymedia.mysql.agent.zookeeper;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: weijie.song
 * Date: 16/2/4 下午4:20
 */
public class HikariDataSourceTest {
	HikariDataSource hikariDataSource;
	AtomicInteger count = new AtomicInteger();

	@Before
	public void init() {
		HikariConfig config = new HikariConfig();
		config.setMaximumPoolSize(100);
		config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
		config.addDataSourceProperty("serverName", "172.0.0.64");
		config.addDataSourceProperty("port", "3306");
		config.addDataSourceProperty("databaseName", "sys");
		config.addDataSourceProperty("user", "root");
		config.addDataSourceProperty("password", "123");
		config.setConnectionTimeout(5000);
		hikariDataSource = new HikariDataSource(config);
	}

	@Test
	public void search() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		for (int i = 0; i < 1000; i++) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					Connection connection = null;
					ResultSet rs = null;
					PreparedStatement statement = null;
					try {
						connection = hikariDataSource.getConnection();
						statement = connection.prepareStatement("select * from schema_table_statistics");
						rs = statement.executeQuery();
//						while (rs.next()) {
//							System.out.println(rs.getString(1));
//						}
					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						System.out.println(count.incrementAndGet());
						try {
							if (rs != null) {
								rs.close();
							}
							if (statement != null) {
								statement.close();
							}
							if (connection != null) {
								connection.close();
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			});
			thread.start();
		}
		while (count.get() < 1000) {

		}
		stopWatch.stop();
		System.out.println(stopWatch.getTime());
	}
}
