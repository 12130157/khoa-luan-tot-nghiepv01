package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.mapper.MapperConstant;
import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcModel;

/**
 *
 * @author thanh
 */
public class Course extends AbstractJdbcModel<String> {

    private String courseCode;
    private int yearIn;
    private int yearOut;
    private float numOfSemester;
    private int numOfStudent;

    public Course() {
    }

    public Course(String courseCode, int yearIn, int yearOut, float numOfSemester, int numOfStudent) {
        this.courseCode = courseCode;
        this.yearIn = yearIn;
        this.yearOut = yearOut;
        this.numOfSemester = numOfSemester;
        this.numOfStudent = numOfStudent;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public float getNumOfSemester() {
        return numOfSemester;
    }

    public void setNumOfSemester(float numOfSemester) {
        this.numOfSemester = numOfSemester;
    }

    public int getNumOfStudent() {
        return numOfStudent;
    }

    public void setNumOfStudent(int numOfStudent) {
        this.numOfStudent = numOfStudent;
    }

    public int getYearIn() {
        return yearIn;
    }

    public void setYearIn(int yearIn) {
        this.yearIn = yearIn;
    }

    public int getYearOut() {
        return yearOut;
    }

    public void setYearOut(int yearOut) {
        this.yearOut = yearOut;
    }
    
    @Override
    public String getIdColumnName() {
        return "MaKhoaHoc";
    }

    @Override
    public String getTableName() {
        return MapperConstant.DB_NAME
                + ".KhoaHoc";
    }

    @Override
    public String[] getColumnNames() {
        return new String[]{
                    "NamVao",
                    "NamRa",
                    "HocKy",
                    "SoSinhVien",};
    }

    @Override
    public void setColumnValues(Object[] values) {
        try {
            yearIn = Integer.parseInt(values[0].toString());
            yearOut = Integer.parseInt(values[1].toString());
            numOfSemester = Integer.parseInt(values[2].toString());
            numOfStudent = Integer.parseInt(values[3].toString());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Object[] getColumnValues() {
        return new Object[]{
                    yearIn,
                    yearOut,
                    numOfSemester,
                    numOfStudent,};
    }
}
