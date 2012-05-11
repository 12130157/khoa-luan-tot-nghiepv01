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
public class RegistrationTimeID implements IID{
private int semester;
private String year;
public RegistrationTimeID(){
    super();
}
public RegistrationTimeID(int _semester, String _year){
    this.semester=_semester;
    this.year=_year;
}
    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String[] getIDNames() {
         return new String[] {
          "HocKy",
          "NamHoc"
        };
    }

    @Override
    public Object[] getIDValues() {
        return new Object[] {
            semester,
            year
        };
    }

    @Override
    public void setIDValues(Object[] idValues) {
        this.semester=Integer.parseInt(idValues[0].toString());
        this.year=idValues[1].toString();
    }
    
}
