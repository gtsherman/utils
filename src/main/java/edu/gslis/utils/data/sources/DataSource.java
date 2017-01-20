package edu.gslis.utils.data.sources;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A container for tuples of data, as might be read from a file or database.
 * 
 * <p>While it is reasonable to instantiate a <code>DataSource</code> directly
 * and fill it with data manually, this class is designed to be subclassed by
 * classes that handle the reading in of data themselves.
 * 
 * <p>It is easiest to conceive of each <code>DataSource</code> object as 
 * accessing only one type of data. This is because it will introduce bugs if
 * a {@link DataInterpreter} retains a reference to a <code>DataSource</code>
 * that has been reused to store a different type of data.
 * 
 * @author Garrick
 *
 */
public class DataSource implements Iterable<String[]> {

	private List<String[]> results; // one array per tuple of data

	public DataSource() {
		flush();
	}
	
	public void addTuple(String[] tuple) {
		results.add(tuple);
	}
	
	public String[] get(int i) {
		return results.get(i);
	}
	
	/**
	 * Remove the currently stored data
	 * 
	 * <p>This method may be useful when you want to reuse a 
	 * <code>DataSource</code>, perhaps for performance reasons.
	 */
	public void flush() {
		results = new ArrayList<String[]>();
	}
	
	@Override
	public Iterator<String[]> iterator() {
		return results.iterator();
	}
	
}
