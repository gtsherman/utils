package edu.gslis.utils.readers;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

import edu.gslis.indexes.IndexWrapper;
import edu.gslis.searchhits.IndexBackedSearchHit;
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
	
	private IndexWrapper index;
	
	private SearchHitsBatch resultsBatch;
	
	public SearchResultsReader(File file, IndexWrapper index) {
		super(Arrays.asList(QUERY_FIELD, Q0_FIELD, DOCNO_FIELD, RANK_FIELD,
				SCORE_FIELD, RUN_FIELD));
		this.index = index;
		read(file);
		createSearchHitsBatch();
	}

	public SearchResultsReader(File file) {
		this(file, null);
	}
	
	public SearchHitsBatch getBatchResults() {
		return resultsBatch;
	}
	
	private void createSearchHitsBatch() {
		SearchHitsBatch resultsBatch = new SearchHitsBatch();
		SearchHits queryResults = new SearchHits();
		String currentQuery = valueOfField(QUERY_FIELD, results.get(0));
		
		Iterator<String[]> tupleIt = results.iterator();
		while (tupleIt.hasNext()) {
			SearchHit hit = index == null ?
					new SearchHit() : new IndexBackedSearchHit(index);
			
			String[] tuple = tupleIt.next();
			String query = valueOfField(QUERY_FIELD, tuple);
			
			if (!query.equals(currentQuery)) {
				resultsBatch.setSearchHits(currentQuery, queryResults);
				queryResults = new SearchHits();
				currentQuery = query;
			}
			
			hit.setQueryName(query);
			hit.setDocno(valueOfField(DOCNO_FIELD, tuple));
			hit.setScore(Double.parseDouble(valueOfField(SCORE_FIELD, tuple)));
			
			queryResults.add(hit);
		}
		resultsBatch.setSearchHits(currentQuery, queryResults); // final query
		
		this.resultsBatch = resultsBatch;
	}

}
