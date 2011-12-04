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
import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcDAO;
import uit.cnpm02.dkhp.model.Student;
import uit.cnpm02.dkhp.model.ViewTrainProgram;

/**
 *
 * @author thanh
 */
public class ViewTrainProgramDAO extends AbstractJdbcDAO<ViewTrainProgram, String>{
    public void IniViewProgramfromRs(ViewTrainProgram program, ResultSet rs, String mssv) throws SQLException, Exception {
        if ((rs != null) && (program != null)) {
            program.setSubCode(rs.getString("MaMH"));
            program.setSubName(rs.getString("TenMH"));
            program.setNumTC(Integer.parseInt(rs.getString("SoTC")));
            program.setNumTCLT(Integer.parseInt(rs.getString("SoTCLT")));
            program.setNumTCTH(Integer.parseInt(rs.getString("SoTCLT")));
            program.setSemester(Integer.parseInt(rs.getString("HocKy")));
            program.setMark(getMark(mssv,rs.getString("MaMH") ));
        }
    }
    public float getMark(String mssv, String subjectCode) throws Exception {
        float result = 0;
             Connection con = null;
             PreparedStatement statement = null;
             ResultSet rs = null;
         try {
            StringBuffer sql = new StringBuffer();
            sql.append("select Diem from khoaluantotnghiep.ketquahoctap where MSSV='").append(mssv).append("' and MaMH='").append(subjectCode).append("'");
            con=getConnection();
            statement=con.prepareStatement(sql.toString());
            rs=statement.executeQuery();
            while ((rs != null) && (rs.next())) {
                try
                {
                result = Float.parseFloat(rs.getString("Diem"));
                } catch (Exception ex) {
                result=0;
         }
           }
        } catch (Exception ex) {
            throw ex;
         } finally {
            close(rs, statement);
            close(con);
        }
        return result;
    }
    public String getTrainProCode(String courseCode, String facultyCode) throws Exception {
        String result = "";
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("select MaCTDT from khoaluantotnghiep.chuongtrinhdaotao where MaKhoa='").append(facultyCode).append("' and MaKhoaHoc='").append(courseCode).append("'");
            con=getConnection();
            statement=con.prepareStatement(sql.toString());
            rs = statement.executeQuery();
            while ((rs != null) && (rs.next())) {
                result = rs.getString("MaCTDT");
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            close(rs, statement);
            close(con);
        }
        return result;
    }
     public ArrayList<ViewTrainProgram> getProgramByCode(String mssv) throws Exception {
         StudentDAO studentDao=new StudentDAO();
         Student student=studentDao.findById(mssv);
         String proCode=getTrainProCode(student.getCourseCode(), student.getFacultyCode());
         ArrayList<ViewTrainProgram> pro = new ArrayList<ViewTrainProgram>();
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("select DISTINCT HocKy, khoaluantotnghiep.monhoc.MaMH, TenMH, SoTC, SoTCLT, SoTCTH from khoaluantotnghiep.chitietctdt, khoaluantotnghiep.monhoc ");
            sql.append("where chitietctdt.MaMH=monhoc.MaMH and chitietctdt.MaCTDT='").append(proCode).append("' ");
            sql.append("order by chitietctdt.HocKy ASC");
            con=getConnection();
            statement = con.prepareStatement(sql.toString());
            rs = statement.executeQuery();
            while ((rs != null) && rs.next()) {
                ViewTrainProgram classTemp = new ViewTrainProgram();
                IniViewProgramfromRs(classTemp, rs, mssv);
                pro.add(classTemp);

           }
        } catch (Exception ex) {
            throw ex;
       } finally {
            close(rs, statement);
            close(con);
        }
        return pro;
    }
}
