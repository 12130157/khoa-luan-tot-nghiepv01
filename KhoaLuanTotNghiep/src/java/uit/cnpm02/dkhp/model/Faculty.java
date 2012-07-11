package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.mapper.MapperConstant;
import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcModel;

/**
 *
 * @author thanh
 */

   

public class Faculty extends AbstractJdbcModel<String> {

    private String facultyName;
    private String dean;
    private int numOfStudent;
    private String deanName;

    public Faculty() {

    }

    public Faculty(String id, String _facultyName, String _dean, int _numOfStudent){
        setId(id);
        this.facultyName=_facultyName;
        this.dean=_dean;
        this.numOfStudent=_numOfStudent;
        deanName="";
 }

    public String getDeanName() {
        return deanName;
    }

    public void setDeanName(String deanName) {
        this.deanName = deanName;
    }

    //***********
    // set parameter
    //***********
     public void setFacultyName(String _facultyName){
        this.facultyName=_facultyName;
     }

    public String getDean() {
        return dean;
    }

    public void setDean(String dean) {
        this.dean = dean;

    }
 //************
    // get parameter
    //************


    public String getFacultyName() {
        return facultyName;
    }


    public int getNumOfStudent() {
        return numOfStudent;
    }

    public void setNumOfStudent(int numOfStudent) {
        this.numOfStudent = numOfStudent;
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
                    "SoSinhVien",};
    }

    @Override
    public void setColumnValues(Object[] values) {
        try {
            facultyName = values[0].toString();
            dean = values[1].toString();
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
                    numOfStudent,};
    }
    @Override
    public boolean isIdAutoIncrement() {
        return false;
    }
}
