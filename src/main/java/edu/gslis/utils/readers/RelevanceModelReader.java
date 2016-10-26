package edu.gslis.utils.readers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import edu.gslis.textrepresentation.FeatureVector;

public class RelevanceModelReader extends Reader {
	
	public static final String TERM_FIELD = "TERM";
	public static final String SCORE_FIELD = "SCORE";
	
	private static final int DEFAULT_TERM_COUNT = 20;
	
	private FeatureVector vector;
	
	public RelevanceModelReader(File file) {
		this(file, DEFAULT_TERM_COUNT);
	}
	
	public RelevanceModelReader(File file, int termCount) {
		super(Arrays.asList(SCORE_FIELD, TERM_FIELD));
		read(file, termCount);
		createFeatureVector();
	}
	
	public FeatureVector getFeatureVector() {
		return vector;
	}
	
	public void read(File file, int count) {
		try {
			int l = 0;
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				l++;
				if (l > count) {
					break;
				}

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
	
	private void createFeatureVector() {
		vector = new FeatureVector(null);
		Iterator<Map<String, String>> tupleIt = results.iterator();
		while (tupleIt.hasNext()) {
			Map<String, String> termTuple = tupleIt.next();
			vector.addTerm(termTuple.get(TERM_FIELD), Double.parseDouble(termTuple.get(SCORE_FIELD)));
		}
	}

}
