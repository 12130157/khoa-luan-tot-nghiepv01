package uit.cnpm02.dkhp.dto;

/**
 *
 * @author LocNguyen
 */
public class MonHoc {
    /**Mã môn học**/
    private String maMH;
    /**Tên môn học**/
    private String tenMH;
    
    /**Số tín chỉ**/
    private int soTC;
    /**Số tín chỉ lý thuyết**/
    private int soTCLT;
    /**Số tín chỉ thực hành**/
    private int soTCTH;

    public MonHoc() {
    }

    public MonHoc(String maMH, String tenMH, int soTC, int soTCLT) {
        this.maMH = maMH;
        this.tenMH = tenMH;
        this.soTC = soTC;
        this.soTCLT = soTCLT;
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
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

    public String getTenMH() {
        return tenMH;
    }

    public void setTenMH(String tenMH) {
        this.tenMH = tenMH;
    }
    
}

