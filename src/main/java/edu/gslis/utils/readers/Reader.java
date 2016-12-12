package edu.gslis.utils.readers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Reader {
	
	public static final String WHITESPACE_DELIMITER = "\\s+";
	public static final String SPACE_DELIMITER = " ";
	public static final String TAB_DELIMITER = "\t";
	public static final String COMMA_DELIMITER = ",";

	protected String delimiter;
	protected List<String> fields;
	
	protected List<String[]> results; // one map per line in the file
	
	public Reader(List<String> fields) {
		this(WHITESPACE_DELIMITER, fields);
	}
	
	public Reader(String delimiter, List<String> fields) {
		setDelimiter(delimiter);
		setFields(fields);
		results = new ArrayList<String[]>();
	}
	
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	
	public void setFields(List<String> fields) {
		this.fields = fields;
	}
	
	/**
	 * Get the index of a field.
	 * @param field The name of the field
	 * @return The index of the field in any well-formed tuple, -1 if field is not known
	 */
	public int fieldIndex(String field) {
		return fields.indexOf(field);
	}
	
	/**
	 * Get the name of the field at a given index.
	 * @param i The index of the field
	 * @return The name of the field
	 */
	public String fieldAtIndex(int i) {
		if (i >= fields.size()) {
			return "Field_" + (i - fields.size() - 1);
		}
		return fields.get(i);
	}
	
	/**
	 * Get the value of the named field in the given tuple.
	 * @param fieldName The name of the field
	 * @param tuple The tuple containing the required value
	 * @return The value of the field if it exists, otherwise null
	 */
	public String valueOfField(String fieldName, String[] tuple) {
		if (tuple.length > fields.size()) {
			System.err.println("Tuple length does not match field size. You may"
					+ " want to investigate.");
			System.err.println("\t" + Arrays.toString(tuple));
		}

		int index = fieldIndex(fieldName);
		if (index == -1 || index >= tuple.length) {
			// The field is not recognized or is outside the length of the tuple
			return null;
		}
		
		return tuple[index];
	}
	
	public void read(File file) {
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] parts = line.split(delimiter);
				for (int i = 0; i < parts.length; i++) {
					parts[i] = parts[i].trim();
				}
				results.add(parts);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.err.println("File not found: "+file.getAbsolutePath());
		}
	}
}