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
import uit.cnpm02.dkhp.model.Registration;
import uit.cnpm02.dkhp.access.advancedJDBC.AdvancedAbstractJdbcDAO;
import uit.cnpm02.dkhp.model.RegistrationID;
import java.util.List;
import uit.cnpm02.dkhp.utilities.Constants;

/**
 *
 * @author thanh
 */
public class RegistrationDAO extends AdvancedAbstractJdbcDAO<Registration, RegistrationID>{

    @Override
    public RegistrationID createID() {
        return new RegistrationID();
    }
  public List<Registration> findAllByStudentCode(String studentCode) throws Exception {
        checkModelWellDefined();
        Registration t = new Registration();
        if (t == null) {
            throw new Exception("Cannot initialize the " + Registration.class.getName()
                    + " class");
        }
        List<Registration> results = new ArrayList<Registration>();
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        String selectQuery = "Select * from "+t.getTableName()+" where MSSV= ? and HocKy="+Constants.CURRENT_SEMESTER+" and NamHoc='"+Constants.CURRENT_YEAR+"'";
        try {
            con = getConnection();
            statement = con.prepareStatement(selectQuery);
            statement.setObject(1, studentCode);
            rs = statement.executeQuery();

            while (rs.next()) {
                String classCode=rs.getString("MaLopHoc");
                RegistrationID registrationID=new RegistrationID(studentCode, classCode, Constants.CURRENT_SEMESTER, Constants.CURRENT_YEAR);
                Registration registration=findById(registrationID);
                results.add(registration);
            }
        } catch (SQLException ex) {
            throw new Exception(ex);
        } finally {
            close(rs, statement);
            close(con);
        }
        return results;
    }
   public void deleteAllByByStudentCode(String studentCode) throws Exception {
        checkModelWellDefined();
        Registration t = new Registration();
        if (t == null) {
            throw new Exception("Cannot initialize the " + Registration.class.getName()
                    + " class");
        }
        Connection con = null;
        PreparedStatement statement = null;
        String selectQuery = "Delete from "+t.getTableName()+" where MSSV= ? and HocKy="+Constants.CURRENT_SEMESTER+" and NamHoc='"+Constants.CURRENT_YEAR+"'";
        try {
            con = getConnection();
            statement = con.prepareStatement(selectQuery);
            statement.setObject(1, studentCode);
            statement.execute();

        } catch (SQLException ex) {
            throw new Exception(ex);
        } finally {
           close(con);
        }
    }
 
}
