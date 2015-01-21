package pw.elka.wedt.wikifinder.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
	private static final String DEFAULT_CONFIG_PATH = "./wikifinder.properties";
	private Properties properties;
	private String path;
	public ConfigManager() {
		try {
			load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void load() throws IOException {
		String confPath = path == null ? DEFAULT_CONFIG_PATH : path;

		InputStream is = new FileInputStream(confPath);

		if (properties == null) {
			properties = new Properties();
		}
		properties.load(is);
	}
	public String getCategorySourceFile() {
		return properties.getProperty("category-source-file");
	}

	public String getSourceFile() {
		return properties.getProperty("source-file");
	}

	public String getIndexPath() {
		return properties.getProperty("index-path");
	}
}
