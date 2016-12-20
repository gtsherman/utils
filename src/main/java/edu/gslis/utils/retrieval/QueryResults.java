package edu.gslis.utils.retrieval;

import edu.gslis.queries.GQuery;
import edu.gslis.searchhits.SearchHits;

public class QueryResults {
	
	private GQuery query;
	private SearchHits results;

	public QueryResults(GQuery query, SearchHits results) {
		this.query = query;
		this.results = results;
	}
	
	public GQuery getQuery() {
		return query;
	}
	
	public SearchHits getResults() {
		return results;
	}

}
