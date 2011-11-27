package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.mapper.MapperConstant;
import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcModel;

/**
 *
 * @author thanh
 */
<<<<<<< .mine
public class Faculty extends  AbstractJdbcModel<String>{
    String facultyName;
    String dean;
    int numOfStudent;
    public Faculty(){
        
=======
public class Faculty extends AbstractJdbcModel<String> {

    private String facultyCode;
    private String facultyName;
    private String dean;
    private int numOfStudent;

    public Faculty() {
>>>>>>> .r103
    }
<<<<<<< .mine
    public Faculty(String id, String _facultyName, String _dean, int _numOfStudent){
        setId(id);
        this.facultyName=_facultyName;
        this.dean=_dean;
        this.numOfStudent=_numOfStudent;
=======

    public Faculty(String facultyCode, String facultyName, String dean, int numOfStudent) {
        this.facultyCode = facultyCode;
        this.facultyName = facultyName;
        this.dean = dean;
        this.numOfStudent = numOfStudent;
>>>>>>> .r103
    }
<<<<<<< .mine
    //***********
    // set parameter
    //***********
     public void setFacultyName(String _facultyName){
        this.facultyName=_facultyName;
=======

    public String getDean() {
        return dean;
    }

    public void setDean(String dean) {
        this.dean = dean;
>>>>>>> .r103
    }

    public String getFacultyCode() {
        return facultyCode;
    }

    public void setFacultyCode(String facultyCode) {
        this.facultyCode = facultyCode;
    }
<<<<<<< .mine
    //************
    // get parameter
    //************
=======

    public String getFacultyName() {
        return facultyName;
    }
>>>>>>> .r103

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
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
}
