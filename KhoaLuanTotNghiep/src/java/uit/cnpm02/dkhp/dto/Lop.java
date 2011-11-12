/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.dto;

/**
 *
 * @author thanh
 */
public class Lop {
    private String maLop;
    private String tenLop;
    private String maKhoa;
    
    /**Mã khóa học**/
    private String maKhoaHoc;
    
    /**Ma giang vien chu nhiem**/
    private String maGVCN;
    
    /**So luong sinh vien**/
    private String SLSV;

    public Lop() {
    }

    public Lop(String maLop, String tenLop, String maKhoa, String maKhoaHoc, String maGVCN, String SLSV) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.maKhoa = maKhoa;
        this.maKhoaHoc = maKhoaHoc;
        this.maGVCN = maGVCN;
        this.SLSV = SLSV;
    }

    public String getSLSV() {
        return SLSV;
    }

    public void setSLSV(String SLSV) {
        this.SLSV = SLSV;
    }

    public String getMaGVCN() {
        return maGVCN;
    }

    public void setMaGVCN(String maGVCN) {
        this.maGVCN = maGVCN;
    }

    public String getMaKhoa() {
        return maKhoa;
    }

    public void setMaKhoa(String maKhoa) {
        this.maKhoa = maKhoa;
    }

    public String getMaKhoaHoc() {
        return maKhoaHoc;
    }

    public void setMaKhoaHoc(String maKhoaHoc) {
        this.maKhoaHoc = maKhoaHoc;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public String getTenLop() {
        return tenLop;
    }

    public void setTenLop(String tenLop) {
        this.tenLop = tenLop;
    }
}
