package edu.gslis.utils.readers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Reader {
	
	public static final String WHITESPACE_DELIMITER = "\\s+";
	public static final String SPACE_DELIMITER = " ";
	public static final String TAB_DELIMITER = "\t";
	public static final String COMMA_DELIMITER = ",";

	protected String delimiter;
	protected List<String> fields;
	
	protected List<Map<String, String>> results; // one map per line in the file
	
	public Reader(List<String> fields) {
		this(WHITESPACE_DELIMITER, fields);
	}
	
	public Reader(String delimiter, List<String> fields) {
		setDelimiter(delimiter);
		setFields(fields);
		results = new ArrayList<Map<String, String>>();
	}
	
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	
	public void setFields(List<String> fields) {
		this.fields = fields;
	}
	
	public void read(File file) {
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				
				Map<String, String> field = new HashMap<String, String>();

				String[] parts = line.split(delimiter);
				for (int i = 0; i < parts.length; i++) {
					String fieldValue = parts[i].trim();
					String fieldName;
					try {
						fieldName = fields.get(i);
					} catch (IndexOutOfBoundsException e) {
						fieldName = "Field"+i;
					}
					field.put(fieldName, fieldValue);
				}
				
				results.add(field);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.err.println("File not found: "+file.getAbsolutePath());
		}
	}
}