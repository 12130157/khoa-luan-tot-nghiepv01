/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.dto;

/**
 * Chương trình đào tạo
 * @author LocNguyen
 */
public class CTDT {

    /**Mã chương trình đào tạo**/
    private String maCTDT;
    /**Mã khoa**/
    private String maKhoa;
    /**Mã khóa học**/
    private String maKhoaHoc;

    public CTDT(String maCTDT, String maKhoa, String maKhoaHoc) {
        this.maCTDT = maCTDT;
        this.maKhoa = maKhoa;
        this.maKhoaHoc = maKhoaHoc;
    }

    public String getMaCTDT() {
        return maCTDT;
    }

    public void setMaCTDT(String maCTDT) {
        this.maCTDT = maCTDT;
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
}
