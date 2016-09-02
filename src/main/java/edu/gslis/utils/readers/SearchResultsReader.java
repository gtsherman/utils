package edu.gslis.utils.readers;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import edu.gslis.searchhits.SearchHit;
import edu.gslis.searchhits.SearchHits;
import edu.gslis.searchhits.SearchHitsBatch;

public class SearchResultsReader extends Reader {
	
	public static final String QUERY_FIELD = "QUERY";
	public static final String Q0_FIELD = "Q0";
	public static final String DOCNO_FIELD = "DOCNO";
	public static final String RANK_FIELD = "RANK";
	public static final String SCORE_FIELD = "SCORE";
	public static final String RUN_FIELD = "RUN";
	
	private SearchHitsBatch resultsBatch;
	
	public SearchResultsReader(File file) {
		super(Arrays.asList(QUERY_FIELD, Q0_FIELD, DOCNO_FIELD, RANK_FIELD, SCORE_FIELD, RUN_FIELD));
		read(file);
		createSearchHitsBatch();
	}
	
	public SearchHitsBatch getBatchResults() {
		return resultsBatch;
	}
	
	private void createSearchHitsBatch() {
		SearchHitsBatch resultsBatch = new SearchHitsBatch();
		SearchHits queryResults = new SearchHits();
		String currentQuery = results.get(0).get(QUERY_FIELD);
		
		Iterator<Map<String, String>> tupleIt = results.iterator();
		while (tupleIt.hasNext()) {
			SearchHit hit = new SearchHit();
			
			Map<String, String> tuple = tupleIt.next();
			String query = tuple.get(QUERY_FIELD);
			
			if (!query.equals(currentQuery)) {
				resultsBatch.setSearchHits(currentQuery, queryResults);
				queryResults = new SearchHits();
				currentQuery = query;
			}
			
			hit.setQueryName(query);
			hit.setDocno(tuple.get(DOCNO_FIELD));
			hit.setScore(Double.parseDouble(tuple.get(SCORE_FIELD)));
			
			queryResults.add(hit);
		}
		resultsBatch.setSearchHits(currentQuery, queryResults); // for final query
		
		this.resultsBatch = resultsBatch;
	}

}
