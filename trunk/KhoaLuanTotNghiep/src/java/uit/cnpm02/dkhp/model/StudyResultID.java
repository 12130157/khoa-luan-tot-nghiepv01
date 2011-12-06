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
public class StudyResultID implements IID{
    private String studentCode;
    private String subjectCode;
    public StudyResultID(){
        super();
    }
    public StudyResultID(String _studentCode, String _subjectCode){
        this.studentCode=_studentCode;
        this.subjectCode=_subjectCode;
    }
    public void setStudentCode(String _studentCode){
        this.studentCode=_studentCode;
    }
    public String getStudentCode(){
        return this.studentCode;
    }
    public void setSubjectCode(String _subjectCode){
        this.subjectCode=_subjectCode;
    }
    public String getSubjectCode(){
        return this.subjectCode;
    }
    @Override
    public String[] getIDNames() {
        return new String[] {
          "MSSV",
          "MaMH"
        };
    }

    @Override
    public Object[] getIDValues() {
       return new Object[] {
            studentCode,
            subjectCode
        };
    }

    @Override
    public void setIDValues(Object[] idValues) {
        this.studentCode=idValues[0].toString();
        this.subjectCode=idValues[1].toString();
    }
    
}
