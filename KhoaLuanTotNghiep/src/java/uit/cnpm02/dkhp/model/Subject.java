package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.mapper.MapperConstant;
import uit.cnpm02.dkhp.access.util.AbstractJdbcModel;

/**
 *
 * @author LocNguyen
 */
public class Subject extends  AbstractJdbcModel<String>{
    /**Subject code**/
    private String subjectCode;
    /**Subject name**/
    private String subjectName;
    
    /**Số tín chỉ**/
    private int soTC;
    /**Số tín chỉ lý thuyết**/
    private int soTCLT;
    /**Số tín chỉ thực hành**/
    private int soTCTH;

    public Subject() {
    }

    public Subject(String subjectCode, String subjectName, int soTC, int soTCLT) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.soTC = soTC;
        this.soTCLT = soTCLT;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public int getSoTC() {
        return soTC;
    }

    public void setSoTC(int soTC) {
        this.soTC = soTC;
    }

    public int getSoTCLT() {
        return soTCLT;
    }

    public void setSoTCLT(int soTCLT) {
        this.soTCLT = soTCLT;
    }

    public int getSoTCTH() {
        return soTCTH;
    }

    public void setSoTCTH(int soTCTH) {
        this.soTCTH = soTCTH;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
     @Override
    public String getIdColumnName() {
        return "MaMH";
    }
     @Override
    public String getTableName() {
        return MapperConstant.DB_NAME
                + ".MonHoc";
    }
    @Override
    public String[] getColumnNames() {
        return new String[]{
                    "TenMH",
                    "SoTC",
                    "SoTCLT",
                    "SoTCTH"
                };
    }

   @Override
    public void setColumnValues(Object[] values) {
        try {
            subjectName = values[0].toString();
            soTC = Integer.parseInt(values[1].toString());
            soTCLT = Integer.parseInt(values[2].toString());
            soTCTH = Integer.parseInt(values[3].toString());

        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
     @Override
    public Object[] getColumnValues() {
        return new Object[]{
                    subjectName,
                    soTC,
                    soTCLT,
                    soTCTH,
                };
    }
}

