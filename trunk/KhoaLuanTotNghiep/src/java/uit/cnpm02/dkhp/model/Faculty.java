/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.mapper.MapperConstant;
import uit.cnpm02.dkhp.access.util.AbstractJdbcModel;

/**
 *
 * @author thanh
 */
public class Faculty extends  AbstractJdbcModel<String>{
    String facultyCode;
    String facultyName;
    String dean;
    int numOfStudent;
    public Faculty(){
        
    }
    public Faculty(String _facultyCode, String _facultyName, String _dean, int _numOfStudent){
        this.facultyCode=_facultyCode;
        this.facultyName=_facultyName;
        this.dean=_dean;
        this.numOfStudent=_numOfStudent;
    }
    //***********
    // set parameter
    //***********
    public void setFacultyCode(String _facultyCode){
        this.facultyCode=_facultyCode;
    }
    public void setFacultyName(String _facultyName){
        this.facultyName=_facultyName;
    }
    public void setDean(String _dean){
        this.dean=_dean;
    }
    public void setNumOfStudent(int _numOfStudent){
        this.numOfStudent=_numOfStudent;
    }
    //************
    // get parameter
    //************
    public String getFacultyCode(){
        return this.facultyCode;
    }
    public String getFacultyName(){
        return this.facultyName;
    }
    public String getDean(){
        return this.dean;
    }
    public int getNumOfStudent(){
        return this.numOfStudent;
    }
     @Override
    public String getIdColumnName() {
        return "MaKhoa";
    }
     @Override
    public String getTableName() {
        return MapperConstant.DB_NAME
                + ".Khoa";
    }
    @Override
    public String[] getColumnNames() {
        return new String[]{
                    "TenKhoa",
                    "TruongKhoa",
                    "SoSinhVien",
               };
    }

   @Override
    public void setColumnValues(Object[] values) {
        try {
             facultyName= values[0].toString();
             dean= values[1].toString();
             numOfStudent = Integer.parseInt(values[2].toString());
        
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
     @Override
    public Object[] getColumnValues() {
        return new Object[]{
                    facultyName,
                    dean,
                    numOfStudent,
                };
    }
}
