package uit.cnpm02.dkhp.model;

/**
 *
 * @author LocNguyen
 */
public class Subject {
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
    
}

