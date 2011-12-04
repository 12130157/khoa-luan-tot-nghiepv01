package uit.cnpm02.dkhp.access.JDBC;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @param <T> model type.
 * @param <ID> model id type.
 */
public interface IGenericDAO<T extends IModel<ID>, ID extends Serializable> {
	
	/**
	 * Find a model object by id.
	 * 
	 * @param id
	 * @param lock
	 * @return found object or null if not found.
	 * @throws DBException
	 */
	T findById(ID id, boolean lock) throws Exception;
	
	/**
	 * Find a list of model objects by ids.
	 * 
	 * @param ids
	 * @param lock
	 * @return found objects or empty list.
	 * @throws DBException
	 */
	List<T> findByIds(ID[] ids, boolean lock) throws Exception;
	
	/**
	 * Find a list of model objects by ids.
	 * @param ids
	 * @param lock
	 * @return found objects or empty list.
	 * @throws DBException
	 */
	List<T> findByIds(Collection<ID> ids, boolean lock) throws Exception;

	/**
	 * Find a model object by id.
	 * @param id
	 * @return found object or null if not found.
	 * @throws DBException
	 */
	T findById(ID id) throws Exception;

	/**
	 * Find a list of model objects by ids.
	 * 
	 * @param ids
	 * @return found objects or empty list.
	 * @throws DBException
	 */
	List<T> findByIds(ID[] ids) throws Exception;
	
	/**
	 * @param ids
	 * @return found objects or empty list.
	 * @throws DBException
	 */
	List<T> findByIds(Collection<ID> ids) throws Exception;

        /**
         * Get all record on a page.
         * @param recordPerPage number record per page.
         * @param currentPage current page.
         * @return list entities on the current page.
         * @throws Exception occur while execute query.
         */
        List<T> findAll(int recordPerPage, int currentPage, String orderBy, String order) throws Exception;
        
        List<T> findAll(int recordPerPage, int currentPage, String whereStrColumn, String whereValue, String orderBy, String order) throws Exception;
        
	/**
	 * Select all available model objects in database.
	 * @return found objects or empty list.
	 * @throws DBException
	 */
	List<T> findAll() throws Exception;

	/**
	 * Update a modified model object.
	 * 
	 * @param entity
	 * @return updated model object.
	 * @throws DBException
	 */
	T update(T entity) throws Exception;
	
	/**
	 * Update a set of model objects.
	 * 
	 * @param entities
	 * @throws DBException
	 */
	void update(Collection<T> entities) throws Exception;
	
	/**
	 * Save a new model object to database.
	 * @param entity
	 * @return generated id.
	 * @throws DBException
	 */
    ID add(T entity) throws Exception;

	/**
	 * Save a new collection of model objects to database.
	 * @param entities
	 * @return generated id.
	 * @throws DBException
	 */
    Collection<ID> addAll(final Collection<T> entities) throws Exception;
    
    /**
	 * Delete a model object.
	 * 
	 * @param t the object.
	 * @throws DBException
	 */
	void delete(T t) throws Exception;
	
	/**
	 * Delete a set of model objects.
	 * 
	 * @param t objects to be deleted.
	 * 
	 * @throws DBException
	 */
	void delete(Collection<T> t) throws Exception;
	
	/**
	 * Get the max Id in the database.
	 * 
	 * @return found max id.
	 * @throws DBException
	 */
	ID getMaxID() throws Exception;
	
	/**
	 * Get the number of rows in table
	 * @return the number of rows
	 * @throws DBException
	 */
	int getRowsCount() throws Exception;
	
	/**
	 * 
	 * @param startRow
	 *            the first row
	 * @param rowsCount
	 *            the number of rows
	 * @param sortColumn
	 *            the column to sort
	 * @param sortOrder
	 *            increase/decrease
	 * @return List of rows
	 */
	List<T> getRows(int startRow, int rowsCount, int sortColumn,
			boolean sortOrder) throws Exception;

	/**
	 * The definition for the number of records that allowed in the FlustMode.Commit
	 * else we have to use FlushMode.Auto
	 */
	public static final int DERBY_FLUSH_COMMIT_MAX = 100;
	
	
}
