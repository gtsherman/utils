package edu.gslis.utils.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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
		try {
			Scanner scanner = new Scanner(new File(file));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String key = line.split(separator)[0].trim();
				String value = line.substring(line.indexOf(key)+key.length()+1).trim();
				
				this.config.put(key, value);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.err.println("Configuration file not found. A configuration file is required. Exiting...");
			System.exit(-1);
		}
	}

	public String get(String key) {
		return this.config.keySet().contains(key) ? this.config.get(key) : null;
	}

	public void set(String key, String value) {
		this.config.put(key, value);
	}

}
