package net.vickymedia.mysql.agent.config;

import lombok.Builder;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * User: weijie.song
 * Date: 16/2/3 下午6:32
 */
@Builder(toBuilder = true)
public class Config {
	private String configFilePath;
	@Getter
	private Property property;

	private static final Logger logger = LoggerFactory.getLogger(Config.class);

	public Config load() {
		logger.info("load file with name :" + configFilePath);

		InputStream configIS = null;
		try {
			configIS = open(configFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (configIS == null) {
			logger.error("can't find zk properties file : " + configFilePath);
		}
		property = new Yaml().loadAs(configIS, Property.class);
		return this;
	}

	public InputStream open(String path) throws IOException {
		final File file = new File(path);
		if (!file.exists()) {
			throw new FileNotFoundException("File " + file + " not found");
		}

		return new FileInputStream(file);
	}

	public ZKProperty getZKProperty() {
		return property.getZookeeper();
	}

	public MysqlProperty getMysqlProperty() {
		return property.getMysql();
	}
}
