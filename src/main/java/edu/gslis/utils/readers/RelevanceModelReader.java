package edu.gslis.utils.readers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Iterator;
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
			while (scanner.hasNextLine() && l < count) {
				l++;
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
	
	private void createFeatureVector() {
		vector = new FeatureVector(null);
		Iterator<String[]> tupleIt = results.iterator();
		while (tupleIt.hasNext()) {
			String[] termTuple = tupleIt.next();
			vector.addTerm(valueOfField(TERM_FIELD, termTuple),
					Double.parseDouble(valueOfField(SCORE_FIELD, termTuple)));
		}
	}

}
