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
import uit.cnpm02.dkhp.access.advancedJDBC.AdvancedAbstractJdbcDAO;
import uit.cnpm02.dkhp.model.StudyResult;
import uit.cnpm02.dkhp.model.StudyResultID;

/**
 *
 * @author thanh
 */
public class StudyResultDAO extends AdvancedAbstractJdbcDAO<StudyResult, StudyResultID>{

    @Override
    public StudyResultID createID() {
        return new StudyResultID();
    }
    public List<StudyResult> findAllByYearAndSemester(String user, String year, int semester) throws Exception {
        ArrayList<StudyResult> results = new ArrayList<StudyResult>();
        String selectQuery = "Select * from KhoaLuanTotNghiep.KetQuaHocTap";
         if (year.equalsIgnoreCase("All") && semester == 0) {
                selectQuery=selectQuery+"  where MSSV='"+user+"' order by NamHoc, HocKy ASC";
            } else if (year.equalsIgnoreCase("All") && semester != 0) {
                selectQuery=selectQuery+"  where MSSV='"+user+"' and HocKy="+semester +" order by NamHoc, HocKy ASC";
            } else if (year.equalsIgnoreCase("All") == false && semester == 0) {
                selectQuery=selectQuery+"  where MSSV='"+user+"' and NamHoc='"+year+"' order by NamHoc, HocKy ASC";
            } else {
                selectQuery=selectQuery+"  where MSSV='"+user+"' and NamHoc='"+year+"' and HocKy="+semester +" order by NamHoc, HocKy ASC";
            }
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            statement = con.prepareStatement(selectQuery);
            rs = statement.executeQuery();

            while (rs.next()) {
                StudyResult studyResult=new StudyResult(rs.getString("MSSV"), rs.getString("MaMH"), rs.getString("NamHoc"), Integer.parseInt(rs.getString("HocKy")), Float.parseFloat(rs.getString("Diem")));
                results.add(studyResult);
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
