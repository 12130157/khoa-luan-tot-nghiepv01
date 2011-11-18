package uit.cnpm02.dkhp.access.advancedJDBC;

/**
 *
 * @author LocNguyen
 */
public interface IAdvancedJdbcModel<ID extends IID> extends IAdvancedModel<ID> {
	/**
	 * @return database table name.
	 */
	String getTableName();

	/**
	 * @return id column name.
	 */
	String[] getIdColumnName();

        /**
         * 
         * @return 
         */
        Object[] getIdColumnValues();
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
