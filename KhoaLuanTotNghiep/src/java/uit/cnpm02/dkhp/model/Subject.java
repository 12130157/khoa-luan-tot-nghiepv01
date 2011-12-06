package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcModel;
import uit.cnpm02.dkhp.access.mapper.MapperConstant;
/**
 *
 * @author LocNguyen
 */
public class Subject extends  AbstractJdbcModel<String>{
    /**Subject name**/
    private String subjectName;
    
    /**Số tín chỉ**/
    private int numTC;
    /**Số tín chỉ lý thuyết**/
    private int numTCLT;
    /**Số tín chỉ thực hành**/
    private int numTCTH;

    public Subject() {
    }

    public Subject(String id, String subjectName, int numTC, int numTCLT) {
        setId(id);
        this.subjectName = subjectName;
        this.numTC = numTC;
        this.numTCLT = numTCLT;
    }

    public int getnumTC() {
        return numTC;
    }

    public void setnumTC(int numTC) {
        this.numTC = numTC;
    }

    public int getnumTCLT() {
        return numTCLT;
    }

    public void setnumTCLT(int numTCLT) {
        this.numTCLT = numTCLT;
    }

    public int getnumTCTH() {
        return numTCTH;
    }

    public void setnumTCTH(int numTCTH) {
        this.numTCTH = numTCTH;
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
            numTC = Integer.parseInt(values[1].toString());
            numTCLT = Integer.parseInt(values[2].toString());
            numTCTH = Integer.parseInt(values[3].toString());

        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
     @Override
    public Object[] getColumnValues() {
        return new Object[]{
                    subjectName,
                    numTC,
                    numTCLT,
                    numTCTH,
                };
    }
}

