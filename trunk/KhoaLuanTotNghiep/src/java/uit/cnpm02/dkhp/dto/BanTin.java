/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.dto;

import java.util.Date;

/**
 *
 * @author thanh
 */
public class BanTin {
    private String maTin;
    private String tieuDe;
    private String noiDung;
    private String nguoiDang;
    private Date ngayTao;
    private int loai;
    
    public BanTin(){
        
    }

    public BanTin(String maTin, String tieuDe, String noiDung, String nguoiDang, Date ngayTao, int loai) {
        this.maTin = maTin;
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.nguoiDang = nguoiDang;
        this.ngayTao = ngayTao;
        this.loai = loai;
    }

    public int getLoai() {
        return loai;
    }

    public void setLoai(int loai) {
        this.loai = loai;
    }

    public String getMaTin() {
        return maTin;
    }

    public void setMaTin(String maTin) {
        this.maTin = maTin;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public String getNguoiDang() {
        return nguoiDang;
    }

    public void setNguoiDang(String nguoiDang) {
        this.nguoiDang = nguoiDang;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }
}
