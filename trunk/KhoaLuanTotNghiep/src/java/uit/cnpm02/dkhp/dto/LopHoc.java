/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.dto;

import java.util.Date;

/**
 *
 * @author LocNguyen
 */
public class LopHoc {
    private String maLopHoc;
    private String namHoc;
    private int hocKy;
    /**Mã Môn học**/
    private String maMH;
    /**Mã giảng viên chủ nhiệm**/
    private String maGVCN;
    /**Tên phòng học**/
    private String phongHoc;
    
    /**Số lượng sinh viên**/
    private int slsv;
    /**Số lượng SV đã đăng ký**/
    private int slsvDK;
    
    private Date ngayHoc;
    private Date ngayThi;
    private int caHoc;

    public LopHoc() {
    }

    public LopHoc(String maLopHoc, String namHoc, int hocKy, String maMH, String maGVCN, String phongHoc, int slsv, int slsvDK, Date ngayHoc, Date ngayThi, int caHoc) {
        this.maLopHoc = maLopHoc;
        this.namHoc = namHoc;
        this.hocKy = hocKy;
        this.maMH = maMH;
        this.maGVCN = maGVCN;
        this.phongHoc = phongHoc;
        this.slsv = slsv;
        this.slsvDK = slsvDK;
        this.ngayHoc = ngayHoc;
        this.ngayThi = ngayThi;
        this.caHoc = caHoc;
    }

    public int getCaHoc() {
        return caHoc;
    }

    public void setCaHoc(int caHoc) {
        this.caHoc = caHoc;
    }

    public int getHocKy() {
        return hocKy;
    }

    public void setHocKy(int hocKy) {
        this.hocKy = hocKy;
    }

    public String getMaGVCN() {
        return maGVCN;
    }

    public void setMaGVCN(String maGVCN) {
        this.maGVCN = maGVCN;
    }

    public String getMaLopHoc() {
        return maLopHoc;
    }

    public void setMaLopHoc(String maLopHoc) {
        this.maLopHoc = maLopHoc;
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public String getNamHoc() {
        return namHoc;
    }

    public void setNamHoc(String namHoc) {
        this.namHoc = namHoc;
    }

    public Date getNgayHoc() {
        return ngayHoc;
    }

    public void setNgayHoc(Date ngayHoc) {
        this.ngayHoc = ngayHoc;
    }

    public Date getNgayThi() {
        return ngayThi;
    }

    public void setNgayThi(Date ngayThi) {
        this.ngayThi = ngayThi;
    }

    public String getPhongHoc() {
        return phongHoc;
    }

    public void setPhongHoc(String phongHoc) {
        this.phongHoc = phongHoc;
    }

    public int getSlsv() {
        return slsv;
    }

    public void setSlsv(int slsv) {
        this.slsv = slsv;
    }

    public int getSlsvDK() {
        return slsvDK;
    }

    public void setSlsvDK(int slsvDK) {
        this.slsvDK = slsvDK;
    }
    
}
