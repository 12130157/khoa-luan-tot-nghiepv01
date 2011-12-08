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
public class TrainClassID implements IID{
    private String classCode;
    private String year;
    private int semester;
    public TrainClassID(){
        super();
    }
     public TrainClassID(String _classCode, String _year, int _semester){
       this.classCode=_classCode;
       this.year=_year;
       this.semester=_semester;
    }
     public void setClassCode(String _classCode){
         this.classCode=_classCode;
     }
     public void setYear(String _year){
           this.year=_year;
     }
     public void setSemester(int _semester){
         this.semester=_semester;
     }
     public String getClassCode(){
         return this.classCode;
     }
     public String getYear(){
           return this.year;
     }
     public int getSemester(){
         return this.semester;
     }
      @Override
    public String[] getIDNames() {
        return new String[] {
          "MaLopHoc",
          "HocKy",
          "NamHoc"
        };
    }

    @Override
    public Object[] getIDValues() {
       return new Object[] {
            classCode,
            semester,
            year
        };
    }

    @Override
    public void setIDValues(Object[] idValues) {
        this.classCode=idValues[0].toString();
        this.semester=Integer.parseInt(idValues[1].toString());
        this.year=idValues[2].toString();
    }
}
