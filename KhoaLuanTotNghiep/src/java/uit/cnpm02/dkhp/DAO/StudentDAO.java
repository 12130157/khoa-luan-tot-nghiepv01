/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcDAO;
import uit.cnpm02.dkhp.model.Student;

/**
 *
 * @author thanh
 */
public class StudentDAO extends AbstractJdbcDAO<Student, String>{
    public List<Student> findByClassName(String className) throws Exception {
        checkModelWellDefined();

        if ((className == null) || (className.isEmpty())) {
            throw new NullPointerException("Classname is null.");
        }
        
        Student t = new Student();
        ArrayList<Student> listResult = new ArrayList<Student>();

        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            String selectQuery = "Select * from " + t.getTableName() + " where MaLop = ?";
            
            con = getConnection();
            statement = con.prepareStatement(selectQuery);
            statement.setObject(1, className);
            rs = statement.executeQuery();
            while (rs.next()) {
                Object[] obj = new Object[t.getColumnNames().length];
                for (int j = 0; j < obj.length; j++) {
                    obj[j] = rs.getObject(t.getColumnNames()[j]);
                }
                Student ti = new Student();
                String id = (String) rs.getString(ti.getIdColumnName());

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
}

