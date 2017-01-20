package edu.gslis.utils.data.sources;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * A {@link DataSource} created by reading a flat file.
 * 
 * <p>Although a <code>FileDataSource</code> must be initialized with a
 * {@link File}, it may be reused if desired. However, as noted in the
 * {@link DataSource} documentation, reusing a <code>DataSource</code> may be
 * dangerous. It is wisest to only change the file of a given instance when
 * the new file represents the same type of information as the original
 * file, e.g. if you have two files representing relevance model data
 * computed from different sources.
 * 
 * @author Garrick
 *
 */
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
	
	/**
	 * Read all data from a file
	 * 
	 * @param file The file to read from
	 * @return <code>true</code> if the file is successfully read, <code>false</code> otherwise
	 * @see #read(File file, int count)
	 */
	public boolean read(File file) {
		return read(file, Integer.MAX_VALUE);
	}
	
	/**
	 * Read some data from a file
	 * 
	 * @param file The file to read from
	 * @param count The number of lines to read from the file
	 * @return <code>true</code> if the file is successfully read, <code>false</code> otherwise
	 * @see #read(File file)
	 */
	public boolean read(File file, int count) {
		// Flush any current data so this data source may be reused
		flush();
		
		boolean success = true;
		
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
			success = false;
		}
		
		return success;
	}
	
}