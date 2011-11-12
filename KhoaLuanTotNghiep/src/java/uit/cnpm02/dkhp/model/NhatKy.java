/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.model;

import java.util.Date;

/**
 *
 * @author LocNguyen
 */
public class NhatKy {
    
    private String maNK;
    private String taiKhoan;
    private String noiDung;
    private Date ngayDang;

    public NhatKy() {
    }

    public NhatKy(String maNK, String taiKhoan, String noiDung, Date ngayDang) {
        this.maNK = maNK;
        this.taiKhoan = taiKhoan;
        this.noiDung = noiDung;
        this.ngayDang = ngayDang;
    }

    public String getMaNK() {
        return maNK;
    }

    public void setMaNK(String maNK) {
        this.maNK = maNK;
    }

    public Date getNgayDang() {
        return ngayDang;
    }

    public void setNgayDang(Date ngayDang) {
        this.ngayDang = ngayDang;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(String taiKhoan) {
        this.taiKhoan = taiKhoan;
    }
}
