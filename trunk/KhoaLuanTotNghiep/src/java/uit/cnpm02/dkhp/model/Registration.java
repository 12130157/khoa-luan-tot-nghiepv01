/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.advancedJDBC.AdvancedAbstractJdbcModel;
import uit.cnpm02.dkhp.access.mapper.MapperConstant;

/**
 * Information about registry of student
 * @author LocNguyen
 */
public class Registration extends AdvancedAbstractJdbcModel<RegistrationID>{
    /**No registry**/
    private int noRegisty;
    private float mark;
    private String studentName;

    public Registration() {
    }

    public Registration(String _studentCode, String _classCode, String _year, int _semester, int _noRegisty, float _mark) {
        RegistrationID registrationId=new RegistrationID(_studentCode, _classCode, _semester, _year);
        setId(registrationId);
        this.noRegisty = _noRegisty;
        this.mark = _mark;
        studentName="";
    }
    public float getMark() {
        return mark;
    }
    public void setMark(float mark) {
        this.mark = mark;
    }
    public int getNoRegisty() {
        return noRegisty;
    }
    public void setNoRegisty(int noRegisty) {
        this.noRegisty = noRegisty;
    }
    public void setStudentName(String _studentName){
        this.studentName=_studentName;
    }
    public String getStudentName(){
        return this.studentName;
    }
   @Override
    public String[] getColumnNames() {
        return new String[]{
                    "STTDK",
                    "Diem"
              };
    }

    @Override
    public Object[] getColumnValues() {
         return new Object[]{
                    noRegisty,
                    mark
                };
    }

    @Override
    public void setColumnValues(Object[] values) {
        try {
            noRegisty = Integer.parseInt(values[0].toString());
            mark = values[1] == null ? 0 : Float.parseFloat(values[1].toString());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String getTableName() {
        return MapperConstant.DB_NAME
                + ".dangkyhocphan";
    }

    @Override
    public String[] getIdColumnName() {
        return new String[]{
                   "MSSV",
                   "MaLopHoc",
                   "HocKy",
                   "NamHoc"
                };
    }

    @Override
    public Object[] getIdColumnValues() {
        return getId().getIDValues();
    }
    
}
