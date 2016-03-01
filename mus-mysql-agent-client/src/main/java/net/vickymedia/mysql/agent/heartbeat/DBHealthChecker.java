package net.vickymedia.mysql.agent.heartbeat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.vickymedia.mysql.agent.config.MysqlProperty;
import net.vickymedia.mysql.agent.jdbc.DataSource;
import net.vickymedia.mysql.agent.listener.DBHealthListener;
import org.apache.commons.lang3.time.StopWatch;

import java.sql.SQLException;

/**
 * User: weijie.song
 * Date: 16/2/4 上午10:27
 */
@Builder
public class DBHealthChecker {
	private MysqlProperty property;
	@Setter
	@Getter
	private DBHealthListener dbHealthListener;

	public void check() {
		dbHealthListener.start();


		try {
			DataSource dataSource = DataSource.getInstance(property);
			dataSource.exec();
		} catch (Exception e) {
			dbHealthListener.error();
			e.printStackTrace();
			return;
		}
		dbHealthListener.end();
	}
}
