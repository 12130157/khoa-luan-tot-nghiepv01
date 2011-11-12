package uit.cnpm02.dkhp.dto;

/**
 *
 * @author LocNguyen
 */
public class MonHocTienQuyet {
    /**Mã môn học**/
    private String maMH;
    /**Mã môn học tiên quyết**/
    private String maMHTQ;

    public MonHocTienQuyet() {
    }

    public MonHocTienQuyet(String maMH, String maMHTQ) {
        this.maMH = maMH;
        this.maMHTQ = maMHTQ;
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public String getMaMHTQ() {
        return maMHTQ;
    }

    public void setMaMHTQ(String maMHTQ) {
        this.maMHTQ = maMHTQ;
    }
    
}
