package uit.cnpm02.dkhp.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcDAO;
import uit.cnpm02.dkhp.model.Account;

/**
 *
 * @author LocNguyen
 */
public class AccountDAO extends AbstractJdbcDAO<Account, String> {

    public List<Account> searchById(String idstr) throws Exception {
        checkModelWellDefined();
        Account t = new Account();
        if (t == null) {
            throw new Exception("Cannot initialize the " + Account.class.getName()
                    + " class");
        }

        List<Account> results = new ArrayList<Account>();
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            String selectQuery = "Select * from " + t.getTableName() + " where TenDangNhap like '%" + idstr + "%'";
            con = getConnection();
            statement = con.prepareStatement(selectQuery);
            //statement.setObject(1, idstr);
            rs = statement.executeQuery();
            while (rs.next()) {
                Account ti = new Account();
                Object[] obj = new Object[t.getColumnNames().length];
                for (int i = 0; i < obj.length; i++) {
                    obj[i] = rs.getObject(ti.getColumnNames()[i]);
                }
                String id = (String) rs.getObject(ti.getIdColumnName());

                ti.setId(id);
                ti.setColumnValues(obj);
                results.add(ti);
            }
        } catch (SQLException e) {
            throw new Exception(e);
        } finally {
            close(rs, statement);
            close(con);
        }
        return null;
    }

    public List<Account> searchByName(String name) throws Exception {
        checkModelWellDefined();
        Account t = new Account();
        if (t == null) {
            throw new Exception("Cannot initialize the " + Account.class.getName()
                    + " class");
        }

        List<Account> results = new ArrayList<Account>();
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            String selectQuery = "Select * from " + t.getTableName() + " where HoTen like '%" + name + "%'";
            con = getConnection();
            statement = con.prepareStatement(selectQuery);
            //statement.setObject(1, name);
            rs = statement.executeQuery();
            while (rs.next()) {
                Account ti = new Account();
                Object[] obj = new Object[t.getColumnNames().length];
                for (int i = 0; i < obj.length; i++) {
                    obj[i] = rs.getObject(ti.getColumnNames()[i]);
                }
                String id = (String) rs.getObject(ti.getIdColumnName());

                ti.setId(id);
                ti.setColumnValues(obj);
                results.add(ti);
            }
        } catch (SQLException e) {
            throw new Exception(e);
        } finally {
            close(rs, statement);
            close(con);
        }

        return null;
    }
    
    public List<Account> search(String search) throws Exception {
        checkModelWellDefined();
        Account t = new Account();
        if (t == null) {
            throw new Exception("Cannot initialize the " + Account.class.getName()
                    + " class");
        }

        List<Account> results = new ArrayList<Account>();
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            
            String selectQuery = "Select * from " + t.getTableName() + " where TenDangNhap like '%" + search+ "%' or HoTen like '%" + search + "%'";
            con = getConnection();
            statement = con.prepareStatement(selectQuery);
            //statement.setObject(1, search);
            //statement.setObject(2, search);
            rs = statement.executeQuery();
            while (rs.next()) {
                Account ti = new Account();
                Object[] obj = new Object[t.getColumnNames().length];
                for (int i = 0; i < obj.length; i++) {
                    obj[i] = rs.getObject(ti.getColumnNames()[i]);
                }
                String id = (String) rs.getObject(ti.getIdColumnName());

                ti.setId(id);
                ti.setColumnValues(obj);
                results.add(ti);
            }
        } catch (SQLException e) {
            throw new Exception(e);
        } finally {
            close(rs, statement);
            close(con);
        }

        return results;
    }
}
