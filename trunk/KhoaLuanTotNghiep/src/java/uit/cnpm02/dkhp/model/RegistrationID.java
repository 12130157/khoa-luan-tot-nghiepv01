/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.advancedJDBC.IID;

/**
 *
 * @author thanh
 */
public class RegistrationID implements IID{
    private String studentCode;
    private String classCode;
    private int semester;
    private String year;
    
    public RegistrationID(){
        super();
    }
    public RegistrationID(String _studentCode, String _classCode, int _semester, String _year){
        this.studentCode=_studentCode;
        this.classCode=_classCode;
        this.semester=_semester;
        this.year=_year;
    }
    public void setStudentCode(String _studentCode){
        this.studentCode=_studentCode;
    }
    public void setClassCode(String _classCode){
        this.classCode=_classCode;
    }
    public void setSemester(int _semester){
        this.semester=_semester;
    }
    public void setYear(String _year){
        this.year=_year;
    }
    //
    //get methods
    //
    public String getStudentCode(){
        return this.studentCode;
    }
    public String getClassCode(){
        return this.classCode;
    }
    public int getSemester(){
        return this.semester;
    }
    public String getYear(){
        return this.year;
    }
    @Override
    public String[] getIDNames() {
        return new String[] {
          "MSSV",
          "MaLopHoc",
          "HocKy",
          "NamHoc"
        };
    }

    @Override
    public Object[] getIDValues() {
       return new Object[] {
            studentCode,
            classCode,
            semester,
            year
        };
    }

    @Override
    public void setIDValues(Object[] idValues) {
        this.studentCode=idValues[0].toString();
        this.classCode=idValues[1].toString();
        this.semester=Integer.parseInt(idValues[2].toString());
        this.year=idValues[3].toString();
    }
    
}
