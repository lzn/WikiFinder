package pw.elka.wedt.wikifinder.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class ConfigManager {
	private static final String DEFAULT_CONFIG_PATH = "./wikifinder.properties";
	HashMap<String, String> config = new HashMap<String, String>();
	Properties properties;
	String path;

	public void load() throws IOException {
		String confPath = this.path == null ? DEFAULT_CONFIG_PATH : this.path;

		InputStream is = new FileInputStream(confPath);

		if (this.properties == null) {
			this.properties = new Properties();
		}
		this.properties.load(is);
	}

	public String getSourceFile() {
		return this.properties.getProperty("source-file");
	}

	public String getIndexPath() {
		return this.properties.getProperty("index-path");
	}
}
