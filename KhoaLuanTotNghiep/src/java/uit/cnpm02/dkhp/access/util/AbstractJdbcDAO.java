package uit.cnpm02.dkhp.access.util;

import uit.cnpm02.dkhp.communication.database.ConnectionServer;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;

/**
 * A Base Jdbc DAO contains common Jdbc functionalities.
 * @param <T> The model type.
 * @param <ID> The Id type.
 * 
 */
public abstract class AbstractJdbcDAO<T extends IJdbcModel<ID>, ID extends Serializable> implements IGenericDAO<T, ID> {

    private Class<T> modelClazz;
    private boolean isCheckModelWellDefine = false;
    /**
     * Default id generate query of mysql
     */
    private String idGenerateQuery = "";
    
     private String[][] queryName = new String[][] {
        {"sql.delete", "delete from {0} where {1} = {2}"},
        {"sql.delete.multi", "delete from {0} where {1} in ({2})"},
        {"sql.insert", "insert into {0}({1}) values ({2})"},
        {"sql.insert.multi", "insert into {0} ({1}) values {2}"},
        {"sql.select", "select * from {0} where {1} = {2}"},
        {"sql.select.multi", "select * from {0} where {1} in ({2})"},
        {"sql.select.all", "select * from {0}"},
        {"sql.maxId", "select max({0}) from {1}"},
        {"sql.update", "update {0} set {1} where {2} = {3}"},
        {"sql.update.multi", "update {0} set {1} where {2} in ({3})"},
        {"log.insert", "insert into Log(col1, col2) values (?,?)"},
        {"log.update", "update Log set col1=value1 where id = ?"},
        {"sql.select.rowsCount", "select count(*) from {0}"},
        {"sql.select.rows", "select * from {0} order by {1} {2} limit {3} offset {4}"}
    };
    
    private Map<String, String> sqlQuery;

    /**
     * Default constructor.
     */
    @SuppressWarnings(value = {"unchecked"})
    public AbstractJdbcDAO() {
        loadQuery();
        // isSupportAutoIncrementDB = true;//configurable
        modelClazz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
    
    private void loadQuery() {
        sqlQuery = new HashMap<String, String>();
        for(int i = 0; i < queryName.length; i++) {
            sqlQuery.put(queryName[i][0], queryName[i][1]);
        }
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

    /**
     * @param queryName query name.
     * @return found query or null if not found.
     */
    public String getSql(String queryName) {
        return sqlQuery.get(queryName);
    }

    protected Connection getConnection() throws SQLException {
        Connection conn = null;
        try {
            conn = ConnectionServer.getConnection();
        } catch (NamingException ex) {
            Logger.getLogger(AbstractJdbcDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return conn;
    }

    protected void close(Connection c) {
        try {
            c.close();
        } catch (SQLException ex) {
            Logger.getLogger(AbstractJdbcDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<ID> addAll(Collection<T> entities) throws Exception {
        checkModelWellDefined();

        if ((entities == null) || entities.isEmpty()) {
            throw new NullPointerException("The list model is incorrect.");
        }


        ArrayList<ID> ids = new ArrayList<ID>();
        List<T> lEntities = (List<T>) entities;
        int numberEntities = lEntities.size();
        String[] columnNames = lEntities.get(0).getColumnNames();
        Object[] columnValues;

        StringBuffer sbColumn = new StringBuffer();
        StringBuffer sbValue = new StringBuffer();

        sbColumn.append(lEntities.get(0).getIdColumnName());
        sbValue.append("?");

        for (int i = 0; i < columnNames.length; i++) {
            sbColumn.append("," + columnNames[i]);
            sbValue.append("," + "?");
        }

        int i, j;

        Long firstId = null;
        T t = lEntities.get(0);
        boolean isAutoIncreament = false;
        if (t.isIdAutoIncrement()
                && (t.getIdType() == IModel.INT_ID_TYPE || t.getIdType() == IModel.LONG_ID_TYPE)) {
            isAutoIncreament = true;
            firstId = (Long) _getMaxID(t);

            if (firstId == null) {
                throw new Exception("Unable to get the first id.");
            }
        }

        for (i = 0; i < numberEntities; i++) {
            final T objT = lEntities.get(i);

            if (objT == null) {
                throw new Exception("Model is null.");
            }

            if (!objT.validate()) {
                throw new IllegalArgumentException(
                        "Model is not validated: Model " + i);
            }

            if (isAutoIncreament && (objT.getIdType() != IModel.STRING_ID_TYPE)) {
                objT.setId((ID) firstId);
                firstId++;
            }

            if (objT.getId() == null) {
                throw new NullPointerException("The ID is null.");
            }
        }

        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = getConnection();
            con.setAutoCommit(false);
            String insertQuery = LangUtils.bind(getSql(Queries.SQL_INSERT),
                    new String[]{lEntities.get(0).getTableName(), sbColumn.toString(),
                        sbValue.toString()});
            statement = con.prepareStatement(insertQuery);

            for (i = 0; i < numberEntities; i++) {
                final ID id = lEntities.get(i).getId();

                ids.add(id);

                try {
                    columnValues = lEntities.get(i).getColumnValues();
                } catch (Exception ex) {
                    throw new Exception("Could not get columnValues", ex);
                }
                statement.setObject(1, id);
                for (j = 0; j < columnValues.length; j++) {
                    setObject(statement, j + 2, columnValues[j]);
                    // statement.setObject(j + 2, columnValues[j]);
                }
                statement.addBatch();
            }
            statement.executeBatch();
            con.commit();
        } catch (SQLException e) {
            rollBack(con);
            throw new Exception(e);
        } finally {
            close(statement);
            close(con);
        }
        return ids;
    }

    @Override
    public ID add(T t) throws Exception {
        checkModelWellDefined();

        if (t == null) {
            throw new NullPointerException("The input entity is null.");
        }

        if (t.getId() == null) {
            if (t.isIdAutoIncrement()
                    && t.getIdType() == IModel.STRING_ID_TYPE) {
                throw new Exception(
                        "The T model is not well defined, String cannot be autoIncrement.");
            } else if (!t.isIdAutoIncrement()) {
                throw new NullPointerException("The id is null.");
            }
        }

        if (!t.validate()) {
            throw new IllegalArgumentException("T model is not validated.");
        }


        String[] columnNames = t.getColumnNames();
        Object[] columnValues = t.getColumnValues();
        StringBuffer sbColumn = new StringBuffer();
        StringBuffer sbValue = new StringBuffer();
        Connection con = null;
        PreparedStatement statement = null;

        if (t.isIdAutoIncrement()) {
            t.setId(_getMaxID(t));
        }

        sbColumn.append(t.getIdColumnName());
        sbValue.append("?");
        for (int i = 0; i < columnValues.length; i++) {
            sbColumn.append("," + columnNames[i]);
            sbValue.append("," + "?");
        }

        String insertQuery = LangUtils.bind(getSql(Queries.SQL_INSERT),
                new String[]{t.getTableName(), sbColumn.toString(),
                    sbValue.toString()});

        try {
            con = getConnection();
            con.setAutoCommit(false);
            statement = con.prepareStatement(insertQuery);
            statement.setObject(1, t.getId());
            for (int i = 0; i < columnValues.length; i++) {
                setObject(statement, i + 2, columnValues[i]);
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

        if ((entities == null) || entities.isEmpty()) {
            throw new NullPointerException("List model is incorrect.");
        }

        int i, j;
        int lSize;
        List<T> lEntities = (List<T>) entities;
        lSize = lEntities.size();
        String[] columnNames = lEntities.get(0).getColumnNames();
        Object[] columnValues = new Object[columnNames.length];
        StringBuffer sb = new StringBuffer();

        sb = new StringBuffer();
        sb.append(columnNames[0] + "=" + "?");
        for (j = 1; j < columnNames.length; j++) {
            sb.append(", " + columnNames[j] + "=" + "?");
        }

        for (i = 0; i < lSize; i++) {
            if (lEntities.get(i) == null) {
                throw new Exception("Model is null.");
            }
            if (lEntities.get(i).getId() == null) {
                throw new NullPointerException("Id is null: " + "Model " + i);
            }

            if (!lEntities.get(i).validate()) {
                throw new IllegalArgumentException(
                        "T model is not validated: Model " + i);
            }
        }

        Connection con = null;
        PreparedStatement statement = null;
        try {
            String updateQuery = LangUtils.bind(getSql(Queries.SQL_UPDATE),
                    new String[]{lEntities.get(0).getTableName(),
                        sb.toString(),
                        lEntities.get(0).getIdColumnName(),
                        "?"});
            con = getConnection();
            con.setAutoCommit(false);
            statement = con.prepareStatement(updateQuery);

            for (i = 0; i < lSize; i++) {
                columnValues = lEntities.get(i).getColumnValues();
                for (j = 0; j < columnValues.length; j++) {
                    setObject(statement, j + 1, columnValues[j]);
//					statement.setObject(j + 1, columnValues[j]);
                }
                statement.setObject(j + 1, lEntities.get(i).getId());

                statement.addBatch();
            }
            statement.executeBatch();
            con.commit();
        } catch (SQLException e) {
            rollBack(con);
            throw new Exception(e);
        } finally {
            close(statement);
            close(con);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll() throws Exception {
        checkModelWellDefined();

        ArrayList<T> results = new ArrayList<T>();

        T t = createModel();

        if (t == null) {
            throw new Exception("Cannot initialize the " + modelClazz.getName()
                    + " class");
        }

        String selectQuery = LangUtils.bind(getSql(Queries.SQL_SELECT_ALL), t.getTableName());
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            statement = con.prepareStatement(selectQuery);
            rs = statement.executeQuery();

            while (rs.next()) {
                T ti = createModel();
                Object[] obj = new Object[t.getColumnNames().length];
                for (int i = 0; i < obj.length; i++) {
                    obj[i] = rs.getObject(ti.getColumnNames()[i]);
                }
                ID id = (ID) rs.getObject(ti.getIdColumnName());

                ti.setId(id);
                ti.setColumnValues(obj);
                results.add(ti);
            }
        } catch (SQLException ex) {
            throw new Exception(ex);
        } finally {
            close(rs, statement);
            close(con);
        }
        return results;
    }

    @Override
    public T findById(ID id, boolean lock) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public T findById(ID id) throws Exception {
        checkModelWellDefined();

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
            String selectQuery = LangUtils.bind(getSql(Queries.SQL_SELECT),
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

        if ((ids == null) || (ids.size() == 0)) {
            throw new NullPointerException("Ids list is incorrect.");
        }

        int n = ids.size();
        ArrayList<T> listResult = new ArrayList<T>();
        List<ID> idl = (List<ID>) ids;

        T t = createModel();
        if (t == null) {
            throw new Exception("Cannot initialize the " + modelClazz.getName()
                    + " class");
        }

        String strId = "?";
        for (int i = 1; i < n; i++) {
            strId += ", " + "?";
        }

        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            String strQuery = LangUtils.bind(getSql(Queries.SQL_SELECT_MULTI),
                    new String[]{t.getTableName(), t.getIdColumnName(), strId});

            con = getConnection();
            statement = con.prepareStatement(strQuery);
            for (int i = 0; i < n; i++) {
                statement.setObject(i + 1, idl.get(i));
            }

            rs = statement.executeQuery();
            while (rs.next()) {
                Object[] obj = new Object[t.getColumnNames().length];
                for (int j = 0; j < obj.length; j++) {
                    obj[j] = rs.getObject(t.getColumnNames()[j]);
                }
                T ti = createModel();
                ID id = (ID) rs.getObject(ti.getIdColumnName());

                ti.setId(id);
                ti.setColumnValues(obj);
                listResult.add(ti);
            }

            return listResult;

        } catch (SQLException e) {
            throw new Exception(e);
        } finally {
            close(rs, statement);
            close(con);
        }
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
    public ID getMaxID() throws Exception {
        return _getMaxID(createModel());
    }

    @SuppressWarnings("unchecked")
    protected ID _getMaxID(T t) throws Exception {
        if (t.getIdType() == IModel.STRING_ID_TYPE) {
            throw new IllegalArgumentException(
                    "Cannot get maxId for ID string type");
        }
        String selectQuery = LangUtils.bind(getSql(Queries.SQL_MAX_ID), t.getIdColumnName(), t.getTableName());
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            statement = con.prepareStatement(selectQuery);
            rs = statement.executeQuery();
            if (t.getIdType() == IModel.INT_ID_TYPE) {
                if (rs.next()) {
                    return (ID) new Integer(rs.getInt(1) + 1);
                } else {
                    return (ID) new Integer(1);
                }
            }
            // else if (t.getIdType() == IModel.LONG_ID_TYPE)
            if (rs.next()) {
                return (ID) new Long(rs.getInt(1) + 1);
            } else {
                return (ID) new Long(1);
            }
        } catch (SQLException e) {
            throw new Exception(e);
        } finally {
            close(rs, statement);
            close(con);
        }
    }

    @Override
    public T update(T t) throws Exception {
        checkModelWellDefined();

        if (t == null) {
            throw new NullPointerException("Model is null.");
        }
        if (t.getId() == null) {
            throw new NullPointerException("Id is null.");
        }

        if (!t.validate()) {
            throw new IllegalArgumentException("T model is not validated.");
        }

        String[] columnNames = t.getColumnNames();
        Object[] columnValues = t.getColumnValues();

        StringBuffer sb = new StringBuffer();
        sb.append(columnNames[0] + "=" + "?");
        for (int i = 1; i < columnValues.length; i++) {
            sb.append(", " + columnNames[i] + "=" + "?");
        }

        Connection con = null;
        PreparedStatement statement = null;
        try {
            String updateQuery = LangUtils.bind(getSql(Queries.SQL_UPDATE),
                    new String[]{t.getTableName(), sb.toString(), t.getIdColumnName(), "?"});

            con = getConnection();
            con.setAutoCommit(false);
            statement = con.prepareStatement(updateQuery);
            int i;
            for (i = 0; i < columnNames.length; i++) {
                setObject(statement, i + 1, columnValues[i]);
//				statement.setObject(i + 1, columnValues[i]);
            }
            statement.setObject(i + 1, t.getId());
            statement.execute();
            con.commit();
        } catch (SQLException e) {
            rollBack(con);
            throw new Exception(e);
        } finally {
            close(statement);
            close(con);
        }

        return t;
    }

    @Override
    public void delete(T t) throws Exception {
        checkModelWellDefined();

        if (t == null) {
            throw new NullPointerException("The input entity is null.");
        }

        if (t.getId() == null) {
            throw new NullPointerException("The id is null.");
        }
        if (!t.validate()) {
            throw new IllegalArgumentException("T model is not validated.");
        }

        Connection con = null;
        PreparedStatement statement = null;
        try {
            String delQuery = LangUtils.bind(getSql(Queries.SQL_DELETE), new String[]{
                        t.getTableName(), t.getIdColumnName(), "?"});

            con = getConnection();
            con.setAutoCommit(false);
            statement = con.prepareStatement(delQuery);
            statement.setObject(1, t.getId());
            statement.execute();
            con.commit();
        } catch (SQLException e) {
            rollBack(con);
            throw new Exception(e);
        } finally {
            close(statement);
            close(con);
        }
    }

    @Override
    public void delete(Collection<T> t) throws Exception {
        checkModelWellDefined();

        if ((t == null) || (t.isEmpty())) {
            throw new NullPointerException("List model is incorrect.");
        }

        List<T> listEntities = (List<T>) t;
        int numEntities = listEntities.size();
        String strMark = "";

        for (int i = 0; i < numEntities; i++) {
            final T obj = listEntities.get(i);

            if (obj == null) {
                throw new NullPointerException("Model is null");
            }
            if (obj.getId() == null) {
                throw new NullPointerException("ID is null");
            }

            if (!obj.validate()) {
                throw new IllegalArgumentException("T model is not validated.");
            }

            strMark += "?";
            if (i < numEntities - 1) {
                strMark += ", ";
            }
        }

        final T ot = listEntities.get(0);

        String delQuery = LangUtils.bind(getSql(Queries.SQL_DELETE_MULTI), new String[]{
                    ot.getTableName(), ot.getIdColumnName(), strMark});
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = getConnection();
            con.setAutoCommit(false);
            statement = con.prepareStatement(delQuery);
            for (int i = 0; i < numEntities; i++) {
                statement.setObject(i + 1, listEntities.get(i).getId());
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
        String query = LangUtils.bind(getSql(Queries.SQL_SELECT_ROWS_COUNT), t.getTableName());
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
        List<T> lstResult = new ArrayList<T>();
        T t = createModel();
        String asc = sortOrder ? "ASC" : "DESC";
        String query = LangUtils.bind(getSql(Queries.SQL_SELECT_ROWS),
                new String[]{t.getTableName(),
                    t.getColumnNames()[sortColumn], asc,
                    String.valueOf(rowsCount), String.valueOf(startRow)});
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            statement = con.prepareStatement(query);
            rs = statement.executeQuery();
            while (rs.next()) {
                Object[] obj = new Object[t.getColumnNames().length];
                for (int j = 0; j < obj.length; j++) {
                    obj[j] = rs.getObject(t.getColumnNames()[j]);
                }
                T ti = createModel();
                ID id = (ID) rs.getObject(ti.getIdColumnName());

                ti.setId(id);
                ti.setColumnValues(obj);
                lstResult.add(ti);
            }
        } catch (SQLException e) {
            throw new Exception(e);
        } finally {
            close(rs, statement);
            close(con);
        }
        return lstResult;
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