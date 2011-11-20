/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcModel;
import uit.cnpm02.dkhp.access.mapper.MapperConstant;
/**
 * Information about registry of student
 * @author LocNguyen
 */
public class Registration  extends  AbstractJdbcModel<String>{
    private String studentCode;    
    private String classCode;
    private String year;
    
    private int semester;
    /**No registry**/
    private int noRegisty;
    private float mark;

    public Registration() {
    }

    public Registration(String studentCode, String classCode, String year, int semester, int noRegisty, float mark) {
        this.studentCode = studentCode;
        this.classCode = classCode;
        this.year = year;
        this.semester = semester;
        this.noRegisty = noRegisty;
        this.mark = mark;
    }

    public float getMark() {
        return mark;
    }

    public void setMark(float mark) {
        this.mark = mark;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    /**
     *  get no of registry
     * @return noRegisty
     */
    public int getNoRegisty() {
        return noRegisty;
    }

    /**
     * Set No of registry
     * @param noRegisty noRegisty
     */
    public void setNoRegisty(int noRegisty) {
        this.noRegisty = noRegisty;
    }
     @Override
    public String getIdColumnName() {
        return "MSSV";
    }
     @Override
    public String getTableName() {
        return MapperConstant.DB_NAME
                + ".DangKyHocPhan";
    }
    @Override
    public String[] getColumnNames() {
        return new String[]{
                    "MaLopHoc",
                    "HocKy",
                    "NamHoc",
                    "STTDK",
                    "Diem"
                };
    }

   @Override
    public void setColumnValues(Object[] values) {
        try {
            classCode = values[0].toString();
            semester =Integer.parseInt(values[1].toString());
            year = values[2].toString();
            noRegisty =Integer.parseInt(values[3].toString());
            mark=Float.parseFloat(values[0].toString());

        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
     @Override
    public Object[] getColumnValues() {
        return new Object[]{
                    classCode,
                    semester,
                    year,
                    noRegisty,
                    mark
                };
    }
}
