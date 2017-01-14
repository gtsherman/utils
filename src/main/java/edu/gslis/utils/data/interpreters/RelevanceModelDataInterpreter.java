package edu.gslis.utils.data.interpreters;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import edu.gslis.textrepresentation.FeatureVector;
import edu.gslis.utils.data.sources.DataSource;

public class RelevanceModelDataInterpreter extends DataInterpreter implements FeatureVectorBuilder {

	public static final String DATA_NAME = "relevance_model";

	public static final String TERM_FIELD = "TERM";
	public static final String SCORE_FIELD = "SCORE";
	public static final List<String> ALL_FIELDS = Arrays.asList(
			SCORE_FIELD, TERM_FIELD);
	
	// Default parameters
	public static final int DEFAULT_TERM_COUNT = 20;
	
	public RelevanceModelDataInterpreter(List<String> fields) {
		super(fields);
	}
	
	public RelevanceModelDataInterpreter(String... fields) {
		this(Arrays.asList(fields));
	}
	
	public RelevanceModelDataInterpreter() {
		this(ALL_FIELDS);
	}
	
	@Override
	public FeatureVector build(DataSource dataSource) {
		FeatureVector vector = new FeatureVector(null);
		Iterator<String[]> tupleIt = dataSource.iterator();
		while (tupleIt.hasNext()) {
			String[] termTuple = tupleIt.next();
			vector.addTerm(valueOfField(TERM_FIELD, termTuple),
					Double.parseDouble(valueOfField(SCORE_FIELD, termTuple)));
		}
		return vector;
	}

}
