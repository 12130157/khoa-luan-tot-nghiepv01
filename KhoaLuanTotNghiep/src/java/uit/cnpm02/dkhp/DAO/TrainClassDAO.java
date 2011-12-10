/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.DAO;

import com.sun.corba.se.impl.orbutil.closure.Constant;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import uit.cnpm02.dkhp.access.advancedJDBC.AdvancedAbstractJdbcDAO;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.model.TrainClassID;
import uit.cnpm02.dkhp.utilities.Constants;

/**
 *
 * @author thanh
 */
public class TrainClassDAO extends AdvancedAbstractJdbcDAO<TrainClass, TrainClassID>{

    @Override
    public TrainClassID createID() {
        return new TrainClassID();
    }
     public List<TrainClass> findAllByStudyDate(int studyDate) throws Exception {
        ArrayList<TrainClass> results = new ArrayList<TrainClass>();
        String selectQuery = "Select * from KhoaLuanTotNghiep.LopHoc where NgayHoc="+studyDate+" and HocKy="+Constants.CURRENT_SEMESTER+" and NamHoc='"+Constants.CURRENT_YEAR+"'";
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            statement = con.prepareStatement(selectQuery);
            rs = statement.executeQuery();

            while (rs.next()) {
                String classCode=rs.getString("MaLopHoc");
                TrainClassID trainclassID=new TrainClassID(classCode, Constants.CURRENT_YEAR, Constants.CURRENT_SEMESTER);
                TrainClass trainClass=findById(trainclassID);
                results.add(trainClass);
            }
        } catch (SQLException ex) {
            throw new Exception(ex);
        } finally {
            close(rs, statement);
            close(con);
        }
        return results;
    }
      public List<TrainClass> findAllBySemesterAndYear() throws Exception {
        ArrayList<TrainClass> results = new ArrayList<TrainClass>();
        String selectQuery = "Select * from KhoaLuanTotNghiep.LopHoc where HocKy="+Constants.CURRENT_SEMESTER+" and NamHoc='"+Constants.CURRENT_YEAR+"'";
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            statement = con.prepareStatement(selectQuery);
            rs = statement.executeQuery();

            while (rs.next()) {
                String classCode=rs.getString("MaLopHoc");
                TrainClassID trainclassID=new TrainClassID(classCode, Constants.CURRENT_YEAR, Constants.CURRENT_SEMESTER);
                TrainClass trainClass=findById(trainclassID);
                results.add(trainClass);
            }
        } catch (SQLException ex) {
            throw new Exception(ex);
        } finally {
            close(rs, statement);
            close(con);
        }
        return results;
    }
}
