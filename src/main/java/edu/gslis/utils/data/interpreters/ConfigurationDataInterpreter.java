package edu.gslis.utils.data.interpreters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.gslis.utils.data.sources.DataSource;

public class ConfigurationDataInterpreter extends DataInterpreter {
	
	public static final String DATA_NAME = "configuration";

	public static final String KEY_FIELD = "KEY";
	public static final String VALUE_FIELD = "VALUE";
	public static final List<String> ALL_FIELDS = Arrays.asList(
			KEY_FIELD, VALUE_FIELD);
	
	public ConfigurationDataInterpreter() {
		super(ALL_FIELDS);
	}
	
	@Override
	public Map<String, String> build(DataSource dataSource) {
		Map<String, String> config = new HashMap<String, String>();
		Iterator<String[]> tupleIt = dataSource.iterator();
		while (tupleIt.hasNext()) {
			String[] tuple = tupleIt.next();
			config.put(valueOfField(KEY_FIELD, tuple),
					valueOfField(VALUE_FIELD, tuple));
		}
		return config;
	}
	
}
