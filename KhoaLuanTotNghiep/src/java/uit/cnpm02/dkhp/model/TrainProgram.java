package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcModel;
import uit.cnpm02.dkhp.access.mapper.MapperConstant;
/**
 * Training program
 * @author LocNguyen
 */
public class TrainProgram extends  AbstractJdbcModel<String>{

    /**Training program**/
    private String programCode;
    /**Faculty Code**/
    private String facultyCode;
    /**Course Code**/
    private String courseCode;

    public TrainProgram(String programCode, String facultyCode, String courseCode) {
        this.programCode = programCode;
        this.facultyCode= facultyCode;
        this.courseCode = courseCode;
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
                    "MaKhoaHoc"
               };
    }

   @Override
    public void setColumnValues(Object[] values) {
        try {
            facultyCode = values[0].toString();
            courseCode = values[1].toString();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
     @Override
    public Object[] getColumnValues() {
        return new Object[]{
                    facultyCode,
                    courseCode
        };
    }
}
