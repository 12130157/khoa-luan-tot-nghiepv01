package uit.cnpm02.dkhp.DAO;

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
public class TrainClassDAO extends AdvancedAbstractJdbcDAO<TrainClass, TrainClassID> {

    @Override
    public TrainClassID createID() {
        return new TrainClassID();
    }

    public List<TrainClass> findAllByStudyDate(int studyDate) throws Exception {
        checkModelWellDefined();
        TrainClass t = new TrainClass();
        if (t == null) {
            throw new Exception("Cannot initialize the " + TrainClass.class.getName()
                    + " class");
        }
        ArrayList<TrainClass> results = new ArrayList<TrainClass>();
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        String selectQuery = "Select * from " + t.getTableName() + " where NgayHoc= ? and HocKy=" + Constants.CURRENT_SEMESTER + " and NamHoc='" + Constants.CURRENT_YEAR + "'";
        try {
            con = getConnection();
            statement = con.prepareStatement(selectQuery);
            statement.setObject(1, studyDate);
            rs = statement.executeQuery();

            while (rs.next()) {
                String classCode = rs.getString("MaLopHoc");
                TrainClassID trainclassID = new TrainClassID(classCode, Constants.CURRENT_YEAR, Constants.CURRENT_SEMESTER);
                //TODO: Improve (Ham findById se mo mot Connection moi --> Not Good)
                // Nen tao ham findById moi, truyen Connection vao
                // Hoac Tao cau query moi + dung Connection vua tao o tren de find
                TrainClass trainClass = findById(trainclassID);
                //
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
    public List<TrainClass> findAllByStudyDateAndFaculty(int studyDate, String FacultyCode) throws Exception {
        checkModelWellDefined();
        TrainClass t = new TrainClass();
        if (t == null) {
            throw new Exception("Cannot initialize the " + TrainClass.class.getName()
                    + " class");
        }
        ArrayList<TrainClass> results = new ArrayList<TrainClass>();
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        String selectQuery = "Select * from KhoaLuanTotNghiep.MonHoc, " + t.getTableName() + " where KhoaLuanTotNghiep.MonHoc.MaMH=KhoaLuanTotNghiep.LopHoc.MaMH and NgayHoc= ? and MaKhoa in ( ?,'ENG','MAT','XH') and HocKy=" + Constants.CURRENT_SEMESTER + " and NamHoc='" + Constants.CURRENT_YEAR + "'";
        try {
            con = getConnection();
            statement = con.prepareStatement(selectQuery);
            statement.setObject(1, studyDate);
            statement.setObject(2, FacultyCode);
            rs = statement.executeQuery();

            while (rs.next()) {
                String classCode = rs.getString("MaLopHoc");
                TrainClassID trainclassID = new TrainClassID(classCode, Constants.CURRENT_YEAR, Constants.CURRENT_SEMESTER);
                //TODO: Improve (Ham findById se mo mot Connection moi --> Not Good)
                // Nen tao ham findById moi, truyen Connection vao
                // Hoac Tao cau query moi + dung Connection vua tao o tren de find
                TrainClass trainClass = findById(trainclassID);
                //
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
        checkModelWellDefined();
        TrainClass t = new TrainClass();
        if (t == null) {
            throw new Exception("Cannot initialize the " + TrainClass.class.getName()
                    + " class");
        }
        ArrayList<TrainClass> results = new ArrayList<TrainClass>();
        String selectQuery = "Select * from " + t.getTableName() + " where HocKy=" + Constants.CURRENT_SEMESTER + " and NamHoc='" + Constants.CURRENT_YEAR + "'";
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            statement = con.prepareStatement(selectQuery);
            rs = statement.executeQuery();

            while (rs.next()) {
                String classCode = rs.getString("MaLopHoc");
                TrainClassID trainclassID = new TrainClassID(classCode, Constants.CURRENT_YEAR, Constants.CURRENT_SEMESTER);
                
                //TODO: Improve (Ham findById se mo mot Connection moi --> Not Good)
                // Nen tao ham findById moi, truyen Connection vao
                // Hoac Tao cau query moi + dung Connection vua tao o tren de find
                TrainClass trainClass = findById(trainclassID);
                //
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
    
    /**
     * Check if a class already existed, A Semeter of an year,
     * There is only on class opened at A DATE, specified SHIFT, ROOM, and LECTURER
     * 
     * @param semeter
     * @param year
     * @param lectureId
     * @param date
     * @param shift
     * @param room
     * @return Class found, Null if class not existed.
     */
    public TrainClass findUnique(int semeter, String year, String lectureId, int date, int shift, String room) throws Exception {
        checkModelWellDefined();
        
        TrainClass clazz = new TrainClass();
        String sqlQuery = "Select * from "
                + clazz.getTableName()
                + " where HocKy = ? And"
                + " NamHoc = ? And"
                + " MaGV = ? And"
                + " NgayHoc = ? And"
                + " CaHoc = ? And"
                + " PhongHoc= ?";
        
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            statement = con.prepareStatement(sqlQuery);
            statement.setInt(1, semeter);
            statement.setString(2, year);
            statement.setString(3, lectureId);
            statement.setInt(4, date);
            statement.setInt(5, shift);
            statement.setString(6, room);
            rs = statement.executeQuery();
            if (rs.next()) {
                clazz = new TrainClass();
                Object[] obj = new Object[clazz.getColumnNames().length];
                for (int i = 0; i < obj.length; i++) {
                    obj[i] = rs.getObject(clazz.getColumnNames()[i]);
                }
                TrainClassID ID = new TrainClassID();
                String idNames[] = ID.getIDNames();
                Object[] idValues = new Object[idNames.length];
                for (int k = 0; k < idNames.length; k ++) {
                    idValues[k] = rs.getObject(idNames[k]);
                }
                ID.setIDValues(idValues);
                clazz.setId(ID);
                clazz.setColumnValues(obj);
            }
        } catch (SQLException ex) {
            throw new Exception(ex);
        } finally {
            close(rs, statement);
            close(con);
        }
        return clazz;
    }

    /**
     * Find TrainClass by status
     * There are 3 status existed:
     *  + Open
     *  + Close
     *  + Cancel
     * 
     * @param status 
     * @return
     * @throws Exception 
     */
    public List<TrainClass> findByStatus(int status) throws Exception {
        checkModelWellDefined();
        
        TrainClass t = new TrainClass();
        String sqlQuery = "Select * from "
                + t.getTableName()
                + " where TrangThai = ?";
        
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            statement = con.prepareStatement(sqlQuery);
            statement.setInt(1, status);
            rs = statement.executeQuery();
            return getDataFromResultSet(rs);
        } catch (SQLException ex) {
            throw new Exception(ex);
        } finally {
            close(rs, statement);
            close(con);
        }
    }

    public List<TrainClass> findByClassRoomAndTime(String room, int date, int shift, int status) throws Exception {
        checkModelWellDefined();
        TrainClass t = new TrainClass();
        String sqlQuery = "Select * from "
                + t.getTableName()
                + " where PhongHoc = ? And"
                + " CaHoc = ? And"
                + " NgayHoc = ? And"
                + " TrangThai = ?";
        
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            statement = con.prepareStatement(sqlQuery);
            statement.setString(1, room);
            statement.setInt(2, shift);
            statement.setInt(3, date);
            statement.setInt(4, status);
            
            rs = statement.executeQuery();
            return getDataFromResultSet(rs);
        } catch (SQLException ex) {
            throw new Exception(ex);
        } finally {
            close(rs, statement);
            close(con);
        }
    }

    public List<TrainClass> findByLecturerAndTime(String lectureCode, int date, int shift, int status) throws Exception {
        checkModelWellDefined();
        TrainClass t = new TrainClass();
        String sqlQuery = "Select * from "
                + t.getTableName()
                + " where MaGV = ? And"
                + " CaHoc = ? And"
                + " NgayHoc = ? And"
                + " TrangThai = ?";
        
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            statement = con.prepareStatement(sqlQuery);
            statement.setString(1, lectureCode);
            statement.setInt(2, shift);
            statement.setInt(3, date);
            statement.setInt(4, status);
            
            rs = statement.executeQuery();
            return getDataFromResultSet(rs);
        } catch (SQLException ex) {
            throw new Exception(ex);
        } finally {
            close(rs, statement);
            close(con);
        }
    }
    
    private List<TrainClass> getDataFromResultSet(ResultSet rs) throws SQLException {
        List<TrainClass> results = new ArrayList<TrainClass>(10);
        while (rs.next()) {
            TrainClass clazz = new TrainClass();
            Object[] obj = new Object[clazz.getColumnNames().length];
            for (int i = 0; i < obj.length; i++) {
                obj[i] = rs.getObject(clazz.getColumnNames()[i]);
            }
            TrainClassID ID = new TrainClassID();
            String idNames[] = ID.getIDNames();
            Object[] idValues = new Object[idNames.length];
            for (int k = 0; k < idNames.length; k++) {
                idValues[k] = rs.getObject(idNames[k]);
            }
            ID.setIDValues(idValues);
            clazz.setId(ID);
            clazz.setColumnValues(obj);

            results.add(clazz);
        }
        return results;
    }
}
