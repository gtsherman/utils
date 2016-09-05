package edu.gslis.utils.readers;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import edu.gslis.textrepresentation.FeatureVector;

public class RelevanceModelReader extends Reader {
	
	public static final String TERM_FIELD = "TERM";
	public static final String SCORE_FIELD = "SCORE";
	
	private FeatureVector vector;
	
	public RelevanceModelReader(File file) {
		super(Arrays.asList(SCORE_FIELD, TERM_FIELD));
		read(file);
		createFeatureVector();
	}
	
	public FeatureVector getFeatureVector() {
		return vector;
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
