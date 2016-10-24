package edu.gslis.utils.readers;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SimpleConfigurationReader extends Reader {

	public static final String KEY_FIELD = "KEY";
	public static final String VALUE_FIELD = "VALUE";
	
	private Map<String, String> config;
	
	public SimpleConfigurationReader(String separator, File file) {
		super(separator, Arrays.asList(KEY_FIELD, VALUE_FIELD));
		read(file);
		createConfigMap();
	}
	
	public Map<String, String> getConfigMap() {
		return config;
	}
	
	private void createConfigMap() {
		config = new HashMap<String, String>();
		Iterator<Map<String, String>> tupleIt = results.iterator();
		while (tupleIt.hasNext()) {
			Map<String, String> tuple = tupleIt.next();
			config.put(tuple.get(KEY_FIELD), tuple.get(VALUE_FIELD));
		}
	}
	
}
