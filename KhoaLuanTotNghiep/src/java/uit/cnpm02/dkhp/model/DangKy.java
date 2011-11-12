/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.model;

/**
 * Thông tin về sinh viên đăng ký học phần.
 * @author LocNguyen
 */
public class DangKy {
    private String mssv;    
    private String maLopHoc;
    private String namHoc;
    
    private int hocKy;
    /**Số thứ tự đăng ký**/
    private int sttDK;
    private float diem;

    public DangKy() {
    }

    public DangKy(String mssv, String maLopHoc, String namHoc, int hocKy, int sttDK, float diem) {
        this.mssv = mssv;
        this.maLopHoc = maLopHoc;
        this.namHoc = namHoc;
        this.hocKy = hocKy;
        this.sttDK = sttDK;
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

    public String getMaLopHoc() {
        return maLopHoc;
    }

    public void setMaLopHoc(String maLopHoc) {
        this.maLopHoc = maLopHoc;
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

    /**
     * Lấy số thứ tự đăng ký
     * @return sttDK
     */
    public int getSttDK() {
        return sttDK;
    }

    /**
     * Set số thứ tự đăng ký.
     * @param sttDK sttDK
     */
    public void setSttDK(int sttDK) {
        this.sttDK = sttDK;
    }
}
