package net.vickymedia.mysql.agent.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.vickymedia.mysql.agent.config.MysqlProperty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: weijie.song
 * Date: 16/2/4 下午4:14
 */
public class DataSource {
	HikariDataSource source;
	MysqlProperty property;
	static DataSource dataSource;

	public DataSource(MysqlProperty property) {
		this.property = property;
	}

	public static DataSource getInstance(MysqlProperty property) {
		if (dataSource == null) {
			dataSource = new DataSource(property);
		}
		return dataSource;
	}

	public void reload() {
		HikariConfig config = new HikariConfig();
		config.setMaximumPoolSize(100);
		config.setDataSourceClassName(property.getDataSourceClassName());
		config.addDataSourceProperty("serverName", property.getHost());
		config.addDataSourceProperty("port", property.getPort());
		config.addDataSourceProperty("databaseName", property.getDatabase());
		config.addDataSourceProperty("user", property.getUser());
		config.addDataSourceProperty("password", property.getPassword());
		config.setConnectionTimeout(property.getConnectionTimeoutMs());
		source = new HikariDataSource(config);
	}

	public Connection getConnection() {
		if (source == null) {
			reload();
		}
		try {
			return source.getConnection();
		} catch (SQLException e) {
			reload();
			e.printStackTrace();
		}
		return null;
	}

	public void exec() throws SQLException {
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement statement = null;
		try {
			connection = getConnection();
			statement = connection.prepareStatement(property.getExecSql());
			rs = statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
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
}
