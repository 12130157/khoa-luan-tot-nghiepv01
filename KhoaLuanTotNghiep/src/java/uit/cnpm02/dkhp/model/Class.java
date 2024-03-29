package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.mapper.MapperConstant;
import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcModel;

/**
 *
 * @author thanh
 */
public class Class extends  AbstractJdbcModel<String>{
    private String className;
    private String facultyCode;
    
    /**course code**/
    private String courseCode;
    
    /**homeroom lecturerCode**/
    private String homeroom;
    
    /**homeroom lecturerName**/
    private String homeroomName;

    
    
    /**number of student**/
    private int numOfStudent;

    public Class() {
    }

    public Class(String id, String className, String facultyCode, String courseCode, String homeroom, int numOfStudent) {
        setId(id);
        this.className = className;
        this.facultyCode = facultyCode;
        this.courseCode = courseCode;
        this.homeroom = homeroom;
        this.numOfStudent = numOfStudent;
        homeroomName="";
    }

    public int getNumOfStudent() {
        return numOfStudent;
    }

    public void setNumOfStudent(int numOfStudent) {
        this.numOfStudent = numOfStudent;
    }

    public String getHomeroom() {
        return homeroom;
    }

    public void setHomeroom(String homeroom) {
        this.homeroom = homeroom;
    }

    public String getFacultyCode() {
        return facultyCode;
    }

    public void setFacultyCode(String facultyCode) {
        this.facultyCode = facultyCode;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCodeoc(String courseCodeoc) {
        this.courseCode = courseCodeoc;
    }

   public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
    public String getHomeroomName() {
        return homeroomName;
    }

    public void setHomeroomName(String homeroomName) {
        this.homeroomName = homeroomName;
    }
    @Override
    public String getIdColumnName() {
        return "MaLop";
    }
     @Override
    public String getTableName() {
        return MapperConstant.DB_NAME
                + ".Lop";
    }
    @Override
    public String[] getColumnNames() {
        return new String[]{
                    "TenLop",
                    "MaKhoa",
                    "MaKhoaHoc",
                    "GVCN",
                    "SoSinhVien"
                };
    }

   @Override
    public void setColumnValues(Object[] values) {
        try {
            className = values[0].toString();
            facultyCode = values[1].toString();
            courseCode = values[2].toString();
            homeroom = values[3].toString();
            numOfStudent = Integer.parseInt(values[4].toString());

        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
     @Override
    public Object[] getColumnValues() {
        return new Object[]{
                    className,
                    facultyCode,
                    courseCode,
                    homeroom,
                    numOfStudent
                };
    }

    @Override
    public boolean isIdAutoIncrement() {
        return false;
    }
     
}
