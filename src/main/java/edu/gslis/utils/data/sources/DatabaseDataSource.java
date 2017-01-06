package edu.gslis.utils.data.sources;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;


public class DatabaseDataSource extends DataSource {
	
	public static final String ANYTHING = "*";
	
	private Connection connection;
	private String table;
	
	public DatabaseDataSource(Connection connection, String table) {
		this.connection = connection;
		setTable(table);
	}
	
	public void setTable(String table) {
		this.table = table;
	}
	
	public String getTable() {
		return table;
	}
	
	public void read(List<String> fields, String... conditions) {
		read(-1, fields, conditions);
	}
	
	public void read(int limit, List<String> fields, String... conditions) {
		// Flush any current data so this data source may be reused
		flush();
		
		// Set up the query to read the required fields
		String dbQuery = "SELECT " + StringUtils.join(fields, ", ") +
				" FROM " + getTable();
		
		// If there are conditions, include them in the WHERE
		if (conditions.length > 0) {
			dbQuery += " WHERE " + StringUtils.join(conditions, " AND ");
		}
		
		// Assume a limit of 0 or less is nonsense (why query for 0 rows?)
		if (limit > 0) {
			dbQuery += " LIMIT " + limit;
		}

		dbQuery += ";";
		
		// Read in the data
		try {
			ResultSet sqlResults = connection.createStatement().executeQuery(dbQuery);
			while (sqlResults.next()) {
				String[] tuple = new String[fields.size()];
				for (int i = 0; i < fields.size(); i++) {
					tuple[i] = sqlResults.getString(fields.get(i));
				}
				addTuple(tuple);
			}
		} catch (SQLException e) {
			System.err.println("Error reading from database. ");
			System.err.println("Faulty query: " + dbQuery);
			System.err.println(e.getStackTrace());
		} 
	}

}
