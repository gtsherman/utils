package edu.gslis.utils.data.interpreters;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import edu.gslis.indexes.IndexWrapper;
import edu.gslis.searchhits.IndexBackedSearchHit;
import edu.gslis.searchhits.SearchHit;
import edu.gslis.searchhits.SearchHits;
import edu.gslis.searchhits.SearchHitsBatch;
import edu.gslis.utils.data.sources.DataSource;

public class SearchResultsDataInterpreter extends DataInterpreter {
	
	public static final String DATA_NAME = "search_results";

	public static final String QUERY_FIELD = "QUERY";
	public static final String Q0_FIELD = "Q0";
	public static final String DOCNO_FIELD = "DOCNO";
	public static final String RANK_FIELD = "RANK";
	public static final String SCORE_FIELD = "SCORE";
	public static final String RUN_FIELD = "RUN";
	public static final List<String> ALL_FIELDS = Arrays.asList(
			QUERY_FIELD, Q0_FIELD, DOCNO_FIELD, RANK_FIELD, SCORE_FIELD, RUN_FIELD);
	
	private IndexWrapper index;
	
	public SearchResultsDataInterpreter() {
		this(null);
	}
	
	public SearchResultsDataInterpreter(IndexWrapper index) {
		super(ALL_FIELDS);
		this.index = index;
	}

	@Override
	public SearchHitsBatch build(DataSource dataSource) {
		SearchHitsBatch resultsBatch = new SearchHitsBatch();
		SearchHits queryResults = new SearchHits();
		String currentQuery = valueOfField(QUERY_FIELD,
				dataSource.get(0));
		
		Iterator<String[]> tupleIt = dataSource.iterator();
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
		
		return resultsBatch;
	}

}
