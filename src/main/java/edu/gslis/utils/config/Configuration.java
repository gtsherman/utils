package edu.gslis.utils.config;

public interface Configuration {
	
	public static final String INDEX_PATH = "index";
	public static final String QUERIES_PATH = "queries";
	public static final String STOPLIST_PATH = "stoplist";
	public static final String QRELS_PATH = "qrels";
	public static final String COUNT = "num-docs";
	
	public void read(String file);
	
	public String get(String key);
	
	public void set(String key, String value);

}
