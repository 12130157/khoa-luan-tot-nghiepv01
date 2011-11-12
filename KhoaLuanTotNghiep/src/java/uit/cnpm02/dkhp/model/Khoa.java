/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.model;

/**
 *
 * @author thanh
 */
public class Khoa {
    String maKhoa;
    String tenKhoa;
    String truongKhoa;
    int soSinhVien;
    public Khoa(){
        
    }
    public Khoa(String _maKhoa, String _tenKhoa, String _truongKhoa, int _soSinhVien){
        this.maKhoa=_maKhoa;
        this.tenKhoa=_tenKhoa;
        this.truongKhoa=_truongKhoa;
        this.soSinhVien=_soSinhVien;
    }
    //***********
    // set parameter
    //***********
    public void setMaKhoa(String _maKhoa){
        this.maKhoa=_maKhoa;
    }
    public void setTenKhoa(String _tenKhoa){
        this.tenKhoa=_tenKhoa;
    }
    public void setTruongKhoa(String _truongKhoa){
        this.truongKhoa=_truongKhoa;
    }
    public void setSoSinhVien(int _soSinhVien){
        this.soSinhVien=_soSinhVien;
    }
    //************
    // get parameter
    //************
    public String getMaKhoa(){
        return this.maKhoa;
    }
    public String getTenKhoa(){
        return this.tenKhoa;
    }
    public String getTruongKhoa(){
        return this.truongKhoa;
    }
    public int getSoSinhVien(){
        return this.soSinhVien;
    }
}
