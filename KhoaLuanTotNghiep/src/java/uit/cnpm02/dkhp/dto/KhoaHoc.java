/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.dto;

/**
 *
 * @author thanh
 */
public class KhoaHoc {
    String maKhoaHoc;
    int namVao;
    int namRa;
    float hocKy;
    int soSinhVien;
    public KhoaHoc(){
        
    }
    public KhoaHoc(String _maKhoaHoc, int _namVao, int _namRa, float _hocKy, int _soSinhVien){
        this.maKhoaHoc=_maKhoaHoc;
        this.namVao=_namVao;
        this.namRa=_namRa;
        this.hocKy=_hocKy;
        this.soSinhVien=_soSinhVien;
    }
    //************
    //set parameter
    //************
    public void setMaKhoa(String _maKhoa){
        this.maKhoaHoc=_maKhoa;
    }
    public void setNamVao(int _namVao){
        this.namVao=_namVao;
    }
    public void setNamRa(int _namRa){
        this.namRa=_namRa;
    }
    public void setHocKy(float _hocKy){
        this.hocKy=_hocKy;
    }
    public void setSoSinhVien(int _soSinhVien){
        this.soSinhVien=_soSinhVien;
    }
    //************
    //get parameter
    //************
    public String getMaKhoa(){
        return this.maKhoaHoc;
    }
    public int getNamVao(){
        return this.namVao;
    }
    public int getNamRa(){
        return this.namRa;
    }
    public float getHocKy(){
       return this.hocKy;
    }
    public int getSoSinhVien(){
        return this.soSinhVien;
    }
    
}
