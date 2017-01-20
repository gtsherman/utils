package edu.gslis.utils.data.interpreters;

import java.util.Arrays;
import java.util.List;

import edu.gslis.utils.data.sources.DataSource;

/**
 * Handles conversion of raw data into some specific object instance.
 * 
 * <p>Data supplied by a <code>DataSource</code> is converted into the desired
 * Java object representation. The method of conversion and type of Java object
 * is determined by the implementation of the <code>build(Datasource)</code>
 * method.
 * 
 * <p>Although interpreters in this library handle <code>fields</code>
 * internally, some implementations may want to allow client code to specify
 * the <code>fields</code> on instantiation if the fields present in a data
 * source may change. A trivial example may be when two formats exist that
 * present the same data in a different order from one another.
 * 
 * @author Garrick
 * 
 */
public abstract class DataInterpreter {

	private List<String> fields;
	
	/**
	 * Create a <code>DataInterpreter</code> object
	 * 
	 * @param fields The names of the data columns and the order they appear
	 */
	public DataInterpreter(List<String> fields) {
		setFields(fields);
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}
	
	public List<String> getFields() {
		return fields;
	}
	
	/**
	 * Get the name of the field at a given index
	 * 
	 * @param i The index of the field
	 * @return The name of the field
	 */
	public String fieldAtIndex(int i) {
		if (i >= fields.size()) {
			return "Field_" + (i - fields.size() - 1);
		}
		return fields.get(i);
	}
	
	/**
	 * Get the value of the named field in the given tuple
	 * 
	 * @param fieldName The name of the field
	 * @param tuple The tuple containing the required value
	 * @return The value of the field if it exists, otherwise null
	 */
	public String valueOfField(String fieldName, String[] tuple) {
		if (tuple.length > fields.size()) {
			System.err.println("Tuple length does not match field size. You may"
					+ " want to investigate.");
			System.err.println("\t" + Arrays.toString(tuple));
		}

		int index = fields.indexOf(fieldName);
		if (index == -1 || index >= tuple.length) {
			// The field is not recognized or is outside the length of the tuple
			return null;
		}
		
		return tuple[index];
	}
	
	/**
	 * Convert data read from some <code>DataSource</code> into a Java object
	 * 
	 * <p>Implementations of this method are best served by specifying a more
	 * specific Java class to be returned, rather than <code>Object</code>,
	 * since returning an undifferentiated <code>Object</code> is clearly not
	 * very useful. For example:</p>
	 * 
	 * <pre>
	 * {@code public Map<String, String> build(DataSource dataSource) {...}}
	 * </pre>
	 * 
	 * @param dataSource The raw data to be interpreted
	 * @return
	 */
	public abstract Object build(DataSource dataSource);
	
}
