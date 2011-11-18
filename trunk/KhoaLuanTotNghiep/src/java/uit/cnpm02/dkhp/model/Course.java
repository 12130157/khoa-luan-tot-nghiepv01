/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.mapper.MapperConstant;
import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcModel;

/**
 *
 * @author thanh
 */
public class Course extends  AbstractJdbcModel<String>{
    String courseCode;
    int yearIn;
    int yearOut;
    float numOfSemester;
    int numOfStudent;
    public Course(){
        
    }
    public Course(String _courseCode, int _yearIn, int _yearOut, float _numOfSemester, int _numOfStudent){
        this.courseCode=_courseCode;
        this.yearIn=_yearIn;
        this.yearOut=_yearOut;
        this.numOfSemester=_numOfSemester;
        this.numOfStudent=_numOfStudent;
    }
    //************
    //set parameter
    //************
    public void setCourseCode(String _maKhoa){
        this.courseCode=_maKhoa;
    }
    public void setYearIn(int _yearIn){
        this.yearIn=_yearIn;
    }
    public void setYearOut(int _yearOut){
        this.yearOut=_yearOut;
    }
    public void setNoOfSemester(float _noOfSemester){
        this.numOfSemester=_noOfSemester;
    }
    public void setNoOfStudent(int _noOfStudent){
        this.numOfStudent=_noOfStudent;
    }
    //************
    //get parameter
    //************
    public String getMaKhoa(){
        return this.courseCode;
    }
    public int getYearIn(){
        return this.yearIn;
    }
    public int getYearOut(){
        return this.yearOut;
    }
    public float getNumOfSemester(){
       return this.numOfSemester;
    }
    public int getNumOfStudent(){
        return this.numOfStudent;
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
                    "SoSinhVien",
                };
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
                    numOfStudent,
                };
    }
}
