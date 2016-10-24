package edu.gslis.utils.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import edu.gslis.utils.readers.SimpleConfigurationReader;

public class SimpleConfiguration implements Configuration {
	
	public static final String SEPARATOR_COLON = ":";
	public static final String SEPARATOR_PIPE = "|";
	
	private Map<String, String> config;
	private String separator;
	
	public SimpleConfiguration() {
		this(SEPARATOR_COLON);
	}
	
	public SimpleConfiguration(String separator) {
		this.config = new HashMap<String, String>();
		this.separator = separator;
	}
	
	public void read(String file) {
		SimpleConfigurationReader reader = new SimpleConfigurationReader(separator, new File(file));
		config = reader.getConfigMap();
	}

	public String get(String key) {
		return this.config.keySet().contains(key) ? this.config.get(key) : null;
	}

	public void set(String key, String value) {
		this.config.put(key, value);
	}

}
