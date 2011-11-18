package uit.cnpm02.dkhp.access.advancedJDBC;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import uit.cnpm02.dkhp.access.mapper.Queries;
import uit.cnpm02.dkhp.access.mapper.SQLUtils;
import uit.cnpm02.dkhp.communication.database.ConnectionServer;
import uit.cnpm02.dkhp.utilities.LangUtils;

/**
 *
 * @author LocNguyen
 */
public abstract class AdvancedAbstractJdbcDAO<T extends IAdvancedJdbcModel<ID>, ID extends IID> implements IAdvancedGenericDAO<T, ID> {
    private Class<T> modelClazz;
    private boolean isCheckModelWellDefine = false;
    /**
     * Default id generate query of mysql
     */
    private String idGenerateQuery = "";
    
    /**
     * Default constructor.
     */
    @SuppressWarnings(value = {"unchecked"})
    public AdvancedAbstractJdbcDAO() {
        // isSupportAutoIncrementDB = true;//configurable
        modelClazz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    

    protected PreparedStatement getStatement(String queryName, Connection conn,
            boolean generateId) throws SQLException {
        PreparedStatement p = null;

        return p;
    }

    protected PreparedStatement getStatement(String query, boolean generateId)
            throws SQLException {
        return this.getStatement(query, getConnection(), generateId);
    }

    protected PreparedStatement getStatement(String query) throws SQLException {
        return this.getStatement(query, getConnection(), false);
    }

    protected int executeGenerateIdQuery(PreparedStatement p)
            throws SQLException {
        return this.executeGenerateIdQuery(p, getConnection());
    }

    protected int executeGenerateIdQuery(PreparedStatement p, Connection conn)
            throws SQLException {
        int id = -1;
        p.executeUpdate();

        ResultSet rs = null;

        try {
            // if (isSupportAutoIncrementDB) {
            rs = p.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
            rs.close();
            // } else {
            p = conn.prepareStatement(getIdGenerationQuery());
            rs = p.executeQuery();

            if (rs.next()) {
                id = rs.getInt(1);
            }

            rs.close();
            // }
        } finally {
            close(rs);
        }

        boolean valid = true;
        if (id == -1) {
            valid = false;
        }

        if (!valid) {
            throw new SQLException(this.getErrorMessage());
        }

        return id;
    }

    protected String getErrorMessage() {
        return "Could not obtain the latest generated key. This error may be associated"
                + " to some invalid database driver operation or server failure."
                + " Please check the database configurations and code logic.";
    }

    /**
     * Close the result set & statement
     * 
     * @param rs resultset
     * @param st statement
     */
    public static void close(ResultSet rs, Statement st) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }

        if (st != null) {
            try {
                st.clearWarnings();
                st.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * Close the result set
     * 
     * @param rs ResultSet
     */
    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * Close the statement
     * 
     * @param st Statement
     */
    private void close(Statement st) {
        if (st != null) {
            try {
                st.clearWarnings();
                st.close();
            } catch (SQLException e) {
            }
        }
    }

    /**
     * Rollback commit
     * 
     * @param con Connection
     */
    private void rollBack(Connection con) {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException e) {
            }
        }
    }

    /**
     * @return Id generation query.
     */
    public String getIdGenerationQuery() {
        return idGenerateQuery;
    }

    /**
     * @param idGenerateQuery Id generation query.
     */
    public void setIdGenerationQuery(String idGenerateQuery) {
        this.idGenerateQuery = idGenerateQuery;
    }

    protected Connection getConnection() throws SQLException {
        Connection conn = null;
        try {
            conn = ConnectionServer.getConnection();
        } catch (NamingException ex) {
            Logger.getLogger(AdvancedAbstractJdbcDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return conn;
    }

    protected void close(Connection c) {
        try {
            c.close();
        } catch (SQLException ex) {
            Logger.getLogger(AdvancedAbstractJdbcDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<ID> addAll(Collection<T> entities) throws Exception {
        checkModelWellDefined();
        // TODO
        return null;
    }

    @Override
    public ID add(T t) throws Exception {
        checkModelWellDefined();

        if (t == null) {
            throw new NullPointerException("The input entity is null.");
        }

        ID id = t.getId();
        if (id == null) {
            throw new NullPointerException("The entity's id is null.");
        }

        if (!t.validate()) {
            throw new IllegalArgumentException("T model is not validated.");
        }

        String[] columnNames = t.getColumnNames();
        Object[] columnValues = t.getColumnValues();
        String[] idNames = t.getIdColumnName();
        Object[] idValues = id.getIDValues();
        
        StringBuffer sbColumn = new StringBuffer();
        StringBuffer sbValue = new StringBuffer();
        Connection con = null;
        PreparedStatement statement = null;
        
        for(int i = 0; i < idNames.length; i++) {
            if (i > 0) {
                sbColumn.append("," + idNames[i]);
                sbValue.append("," + "?");
            } else {
                sbColumn.append(idNames[i]);
                sbValue.append("?");
            }
        }
        
        for (int i = 0; i < columnValues.length; i++) {
            sbColumn.append("," + columnNames[i]);
            sbValue.append("," + "?");
        }

        String insertQuery = LangUtils.bind(SQLUtils.getSql(Queries.SQL_INSERT),
                new String[]{t.getTableName(), sbColumn.toString(),
                    sbValue.toString()});

        try {
            con = getConnection();
            con.setAutoCommit(false);
            statement = con.prepareStatement(insertQuery);
            //statement.setObject(1, t.getId());
            int k = 0;
            for(; k < idValues.length; k++) {
                statement.setObject(k + 1, idValues[k]);
            }
            
            for (int i = 0; i < columnValues.length; i++) {
                setObject(statement, i + k, columnValues[i]);
                // statement.setObject(i + 2, columnValues[i]);
            }

            statement.execute();
            con.commit();
        } catch (SQLException e) {
            rollBack(con);
            throw new Exception(e);
        } finally {
            close(statement);
            close(con);
        }

        return t.getId();
    }

    @Override
    public void update(Collection<T> entities) throws Exception {
        checkModelWellDefined();

        //TODO
    }

    @Override
    public List<T> findAll(int recordPerPage, int currentPage, String orderBy, String order) throws Exception {
        checkModelWellDefined();

        //TODO:
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll() throws Exception {
        checkModelWellDefined();

       //TODO:
        return null;
    }

    @Override
    public T findById(ID id, boolean lock) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public T findById(ID id) throws Exception {
        checkModelWellDefined();
        
        //TODO
        
        return null;
/*
        if (id == null) {
            throw new NullPointerException("Id is null.");
        }
        T t = createModel();
        if (t == null) {
            throw new Exception("Cannot initialize the " + modelClazz.getName()
                    + " class");
        }

        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            String selectQuery = LangUtils.bind(SQLUtils.getSql(Queries.SQL_SELECT),
                    new String[]{t.getTableName(), t.getIdColumnName(), "?"});

            con = getConnection();
            statement = con.prepareStatement(selectQuery);
            statement.setObject(1, id);
            rs = statement.executeQuery();
            if (rs.next()) {
                Object[] obj = new Object[t.getColumnNames().length];
                for (int i = 0; i < obj.length; i++) {
                    //obj[i] = rs.getObject(i + 2); 
                    obj[i] = rs.getObject(t.getColumnNames()[i]);
                }
                t.setId(id);
                t.setColumnValues(obj);
                return t;
            }
            return null;
        } catch (SQLException e) {
            throw new Exception(e);
        } finally {
            close(rs, statement);
            close(con);
        }
 */       
    }

    @Override
    public List<T> findByIds(Collection<ID> ids, boolean lock)
            throws Exception {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findByIds(Collection<ID> ids) throws Exception {
        checkModelWellDefined();

        //TODO: 
        
        return null;
    }

    @Override
    public List<T> findByIds(ID[] ids, boolean lock) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> findByIds(ID[] ids) throws Exception {
        return findByIds(Arrays.asList(ids));
    }

    @Override
    public T update(T t) throws Exception {
        checkModelWellDefined();

        //TODO:
        
        return null;
    }

    @Override
    public void delete(T t) throws Exception {
        checkModelWellDefined();

        // TODO: 
    }

    @Override
    public void delete(Collection<T> t) throws Exception {
        checkModelWellDefined();

        //TODO: 
    }

    @SuppressWarnings("unchecked")
    private T createModel() {
        Exception ex = null;
        try {
            Object t = modelClazz.newInstance();
            return (T) t;
        } catch (InstantiationException e) {
            // Throw Exception
            ex = e;
        } catch (IllegalAccessException e) {
            // Throw Exception
            ex = e;
        }
        throw new IllegalArgumentException("Cannot initialize the "
                + modelClazz.getName() + " class", ex);
    }

    @Override
    public int getRowsCount() throws Exception {
        T t = createModel();
        String query = LangUtils.bind(SQLUtils.getSql(Queries.SQL_SELECT_ROWS_COUNT), t.getTableName());
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            statement = con.prepareStatement(query);
            rs = statement.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new Exception(e);
        } finally {
            close(rs, statement);
            close(con);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> getRows(int startRow, int rowsCount, int sortColumn,
            boolean sortOrder) throws Exception {
        // TODO:
        return null;
    }

    protected void checkModelWellDefined() throws Exception {
        if (isCheckModelWellDefine) {
            return;
        }

        T t = createModel();

        String[] columnNames;
        Object[] columnValues;
        try {
            columnNames = t.getColumnNames();
        } catch (Exception ex) {
            throw new Exception("T model is not well define - Problem: Can't get columnName", ex);
        }

        try {
            columnValues = t.getColumnValues();
        } catch (Exception ex) {
            throw new Exception("T model is not well define - Problem: Can't get columnValues", ex);
        }

        if (columnNames == null) {
            throw new Exception("T model is not well define - Problem: columnNames is null");
        }

        if (columnValues == null) {
            throw new Exception("T model is not well define - Problem: columnValues is null");
        }

        // Check size of column name and column value must be equal
        if ((columnNames.length != columnValues.length)) {
            throw new Exception("T model is not well define - Problem: size of columnValues and columnNames is not equal.");
        }

        for (int i = 0; i < columnNames.length; i++) {
            if (columnNames[i] == null) {
                throw new Exception("T model is not well define - Problem: column's name is null");
            }
        }
        isCheckModelWellDefine = true;
    }

    /**
     * This utility function to set object value for 
     * preparestatement object at specify index.
     * @param statement prepare statement object.
     * @param index set at index.
     * @param obj object value.
     * @throws SQLException 
     */
    private void setObject(PreparedStatement statement, int index, Object obj) throws SQLException {
        if (obj instanceof java.util.Date) {
            //some database type (i.e. postgresql, ...) 
            //not allow set object type Timestamp as object.
            //so, this will convert java.util.Date (its accept java.sql.Date)
            //to java.sql.Timestamp and call setTimestamp
            java.util.Date utlDate = (java.util.Date) obj;
            java.sql.Timestamp sqlTime = new java.sql.Timestamp(utlDate.getTime());
            statement.setTimestamp(index, sqlTime);
        } else {
            statement.setObject(index, obj);
        }
    }    
}
