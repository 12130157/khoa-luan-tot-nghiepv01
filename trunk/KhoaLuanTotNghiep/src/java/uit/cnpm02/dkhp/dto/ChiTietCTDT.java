/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.dto;

/**
 *
 * @author LocNguyen
 */
public class ChiTietCTDT {
    /**Mã chương trình đào tạo**/
    private String maCTDT;
    
    /**Mã môn học**/
    private String maMH;
    
    private int hocKy;

    public ChiTietCTDT() {
    }

    public ChiTietCTDT(String maCTDT, String maMH, int hocKy) {
        this.maCTDT = maCTDT;
        this.maMH = maMH;
        this.hocKy = hocKy;
    }

    public int getHocKy() {
        return hocKy;
    }

    public void setHocKy(int hocKy) {
        this.hocKy = hocKy;
    }

    /**
     * Get Mã Chương trình đào tạo
     * @return ma CTDT
     */
    public String getMaCTDT() {
        return maCTDT;
    }

    /**
     * Set mã chương trình đào tạo
     * @param maCTDT ma CTDT
     */
    public void setMaCTDT(String maCTDT) {
        this.maCTDT = maCTDT;
    }

    /**
     * Get mã môn học
     * @return maMH
     */
    public String getMaMH() {
        return maMH;
    }

    /**
     * Set mã môn học
     * @param maMH maMH
     */
    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }
}
