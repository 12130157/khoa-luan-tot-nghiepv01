package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcModel;
import uit.cnpm02.dkhp.access.mapper.MapperConstant;
/**
 * Training program
 * @author LocNguyen
 */
public class TrainProgram extends AbstractJdbcModel<String>{

    /**Training program**/
    private String programCode;
    /**Faculty Code**/
    private String facultyCode;
    private String facultyName;

    /**Course Code**/
    private String courseCode;
    
    private boolean isStarted = false;
    
    public TrainProgram() {
        
    }

    public TrainProgram(String programCode, String facultyCode, String courseCode) {
        setId(programCode);
        this.programCode = programCode;
        this.facultyCode= facultyCode;
        this.courseCode = courseCode;
        this.isStarted = false;
    }
    
    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getProgramCode() {
        return programCode;
    }

    public void setProgramCode(String programCode) {
        this.programCode = programCode;
    }

    public String getFacultyCode() {
        return facultyCode;
    }

    public void setFacultyCode(String facultyCode) {
        this.facultyCode= facultyCode;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    
    public boolean isIsStarted() {
        return isStarted;
    }

    public void setIsStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }
    
    @Override
    public String getIdColumnName() {
        return "MaCTDT";
    }
    @Override
    public String getTableName() {
        return MapperConstant.DB_NAME
                + ".ChuongTrinhDaoTao";
    }
    @Override
    public String[] getColumnNames() {
        return new String[]{
                    "MaKhoa",
                    "MaKhoaHoc",
                    "DaBatDau"
               };
    }

   @Override
    public void setColumnValues(Object[] values) {
        try {
            facultyCode = values[0].toString();
            courseCode = values[1].toString();
            isStarted = Boolean.parseBoolean(values[2].toString());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
     @Override
    public Object[] getColumnValues() {
        return new Object[]{
                    facultyCode,
                    courseCode,
                    isStarted
        };
    }

    @Override
    public boolean isIdAutoIncrement() {
        return false;
    }
}
