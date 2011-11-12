/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.dto;

/**
 *  Thực thể kết quả học tập
 * @author LocNguyen
 */
public class KetQuaHocTap {
    private String mssv;
    /**Mã môn học**/
    private String maMH;
    private String namHoc;
    
    private int hocKy;
    private float diem;

    public KetQuaHocTap() {
    }

    public KetQuaHocTap(String mssv, String maMH, String namHoc, int hocKy, float diem) {
        this.mssv = mssv;
        this.maMH = maMH;
        this.namHoc = namHoc;
        this.hocKy = hocKy;
        this.diem = diem;
    }

    public float getDiem() {
        return diem;
    }

    public void setDiem(float diem) {
        this.diem = diem;
    }

    public int getHocKy() {
        return hocKy;
    }

    public void setHocKy(int hocKy) {
        this.hocKy = hocKy;
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public String getNamHoc() {
        return namHoc;
    }

    public void setNamHoc(String namHoc) {
        this.namHoc = namHoc;
    }
}
