package edu.gslis.utils.data.sources;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileDataSource extends DataSource {
	
	public static final String WHITESPACE_DELIMITER = "\\s+";
	public static final String SPACE_DELIMITER = " ";
	public static final String TAB_DELIMITER = "\t";
	public static final String COMMA_DELIMITER = ",";

	private String delimiter;

	public FileDataSource(File file) {
		this(file, WHITESPACE_DELIMITER);
	}
	
	public FileDataSource(File file, int limit) {
		this(file, WHITESPACE_DELIMITER, limit);
	}
	
	public FileDataSource(File file, String delimiter) {
		this(file, delimiter, Integer.MAX_VALUE);
	}
	
	public FileDataSource(File file, String delimiter, int limit) {
		setDelimiter(delimiter);
		read(file, limit);
	}
	
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	
	public String getDelimiter() {
		return delimiter;
	}
	
	public void read(File file) {
		read(file, Integer.MAX_VALUE);
	}
	
	public void read(File file, int count) {
		// Flush any current data so this data source may be reused
		flush();
		
		// Read data from file
		try {
			int l = 0;
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine() && l < count) {
				l++;
				String line = scanner.nextLine();
				String[] parts = line.split(delimiter);
				for (int i = 0; i < parts.length; i++) {
					parts[i] = parts[i].trim();
				}
				addTuple(parts);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.err.println("File not found: "+file.getAbsolutePath());
		}
	}
	
}