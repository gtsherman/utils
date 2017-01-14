package edu.gslis.utils.data.sources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * A {@link DataSource} created by reading from a database. 
 * 
 * <p>Although a <code>DatabaseDataSource</code> must be initialized with a
 * specific table, it may be reused if desired. However, as noted in the
 * {@link DataSource} documentation, reusing a <code>DataSource</code> may be
 * dangerous. It is wisest to only change the table of a given instance when
 * the new table represents the same type of information as the original
 * table, e.g. if you have two tables representing relevance model data
 * computed from different sources.
 * 
 * @author Garrick
 *
 */
public class DatabaseDataSource extends DataSource {
	
	public static final String ANYTHING = "*";
	
	private Connection connection;
	private String table;
	
	public DatabaseDataSource(Connection connection, String table) {
		this.connection = connection;
		setTable(table);
	}
	
	/**
	 * Get a database connection
	 * 
	 * <p>Currently only works with SQLite, but should be fleshed out in the
	 * future.
	 * 
	 * @param dbName The name of the database you want to connect to
	 * @return The JDBC Connection object
	 */
	public static Connection getConnection(String dbName) {
		Connection con;
		try {
			con = DriverManager.getConnection("jdbc:sqlite://" + dbName);
		} catch (SQLException e) {
			System.err.println("Error connecting to database " + dbName);
			e.printStackTrace(System.err);
			con = null;
		}
		return con;
	}
	
	/**
	 * Set the table you plan to query
	 * 
	 * If the table does not exist, the previous table is retained (or null if
	 * no previous table exists).
	 * 
	 * @param table The name of the table in the database
	 * @return <code>true</code> if table successfully set, <code>false</code> otherwise
	 */
	public boolean setTable(String table) {
		boolean success = true;
		
		if (connection == null) {
			System.err.println("No connection to database");
			return false;
		}
		
		// Make sure the table exists
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet sqlResults = statement.executeQuery(
					"SELECT 1 FROM sqlite_master WHERE type='table' AND name='" +
					table + "';");
			if (!sqlResults.next()) {
				System.err.println("Table " + table + " does not exist.");
				success = false;
			}

			this.table = table;
		
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			System.err.println("Error checking database tables.");
			e.printStackTrace(System.err);
		}
		
		return success;
	}
	
	public String getTable() {
		return table;
	}
	
	public void read(String... conditions) {
		read(Arrays.asList(ANYTHING), conditions);
	}
	
	public void read(List<String> fields, String... conditions) {
		read(-1, fields, conditions);
	}
	
	public void read(int limit, List<String> fields, String... conditions) {
		// Flush any current data so this data source may be reused
		flush();
		
		if (connection == null) {
			System.err.println("No connection to database");
			return;
		}
		
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
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet sqlResults = statement.executeQuery(dbQuery);

			// If we have run select *, we need to adjust it
			if (fields.size() == 1 && fields.get(0).equals(ANYTHING)) {
				fields = new ArrayList<String>();
				ResultSetMetaData rsm = sqlResults.getMetaData();
				for (int i = 1; i <= rsm.getColumnCount(); i++) {
					fields.add(i-1, rsm.getColumnName(i));
				}
			}
		
			while (sqlResults.next()) {
				String[] tuple = new String[fields.size()];
				for (int i = 0; i < fields.size(); i++) {
					tuple[i] = sqlResults.getString(fields.get(i));
				}
				addTuple(tuple);
			}
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			System.err.println("Error reading from database.");
			System.err.println("Faulty query: " + dbQuery);
			e.printStackTrace(System.err);
		}
	}

}
