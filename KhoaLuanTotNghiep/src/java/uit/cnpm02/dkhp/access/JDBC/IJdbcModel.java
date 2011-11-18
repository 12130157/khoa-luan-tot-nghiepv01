package uit.cnpm02.dkhp.access.JDBC;

import java.io.Serializable;

/**
 * @param <ID> model id type.
 */
public interface IJdbcModel<ID extends Serializable> extends IModel<ID> {

	/**
	 * @return true: id is auto-increment else false.
	 */
	boolean isIdAutoIncrement();

	
	/**
	 * @return database table name.
	 */
	String getTableName();

	/**
	 * @return id column name.
	 */
	String getIdColumnName();

	/**
	 * 
	 * @return name of all table columns except the ID column.
	 */
	String[] getColumnNames();

	/**
	 * 
	 * @return values of all columns except the ID column. Be careful with the
	 *         order - must be exactly as {@link #getColumnNames()}
	 */
	Object[] getColumnValues();
	
	/**
	 * set value for each column. Be careful with the order - must be exactly as
	 * {@link #getColumnNames()}
	 * 
	 * @param values
	 */
	void setColumnValues(Object[] values);
}
