package edu.gslis.utils.config;

import edu.gslis.utils.ParameterBroker;

/**
 * Simple adapter to make the ir-tools ParameterBroker class work with the Configuration interface.
 * 
 * @author Garrick
 *
 */
public class ParameterBrokerConfiguration implements Configuration {

	ParameterBroker params;
	
	@Override
	public void read(String file) {
		params = new ParameterBroker(file);
	}

	@Override
	public String get(String key) {
		return params.getParamValue(key);
	}

	@Override
	public void set(String key, String value) {
		params.setParam(key, value);
	}

}