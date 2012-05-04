package uit.cnpm02.dkhp.model;

import java.util.List;
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
    private String facultyCode;
    private int type;
    /**Hold back requred subject**/
    private List<Subject> preSub;

    public List<Subject> getPreSub() {
        return preSub;
    }

    public void setPreSub(List<Subject> preSub) {
        this.preSub = preSub;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Subject() {
    }

    public Subject(String id, String subjectName, int numTC, int numTCLT, String facultyCode, int Type) {
        setId(id);
        this.subjectName = subjectName;
        this.numTC = numTC;
        this.numTCLT = numTCLT;
        this.numTCTH = numTC - numTCLT;
    }
    public String getFacultyCode() {
        return facultyCode;
    }

    public void setFacultyCode(String FacultyCode) {
        this.facultyCode = FacultyCode;
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
                    "SoTCTH",
                    "MaKhoa",
                    "Loai"
                };
    }

   @Override
    public void setColumnValues(Object[] values) {
        try {
            subjectName = values[0].toString();
            numTC = Integer.parseInt(values[1].toString());
            numTCLT = Integer.parseInt(values[2].toString());
            numTCTH = Integer.parseInt(values[3].toString());
            facultyCode= values[4].toString();
            type = Integer.parseInt(values[5].toString());

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
                    facultyCode,
                    type
                };
    }

    @Override
    public boolean isIdAutoIncrement() {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Subject))
            return false;
        if (obj == null)
            return false;
        Subject s = (Subject) obj;
        return this.getId().equals(s.getId());
    }

    public int compare(Subject o1, String by) {
        int result = 0;
        if (by.equalsIgnoreCase("MaMH")) {
            result = this.getId()
                    .compareTo(o1.getId());
        } else if (by.equalsIgnoreCase("TenMH")) {
            result = this.getSubjectName()
                    .compareTo(o1.getSubjectName());
        } else if (by.equalsIgnoreCase("SoTC")) {
            result = this.getnumTC() - o1.getnumTC();
        } else if (by.equalsIgnoreCase("SoTCLT")) {
            result = this.getnumTCLT() - o1.getnumTCLT();
        } else if (by.equalsIgnoreCase("SoTCTH")) {
            result = this.getnumTCTH() - o1.getnumTCTH();
        }
        
        return result;
    }
}
