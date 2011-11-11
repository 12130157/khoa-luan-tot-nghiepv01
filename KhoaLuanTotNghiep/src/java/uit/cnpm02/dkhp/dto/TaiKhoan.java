/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.dto;

/**
 *
 * @author thanh
 */
public class TaiKhoan {
    String tenDangNhap;
    String matKhau;
    String hoTen;
    int dangDangNhap;//1 dang dang nhap, 0 chua dang nhap
    String tinhTrang;
    int loai;
    public TaiKhoan(){
        
    }
    public TaiKhoan(String _tenDangNhap, String _matKhau, String _hoTen, int _dangDangNhap,
            String _tinhTrang, int _loai){
        this.tenDangNhap=_tenDangNhap;
        this.matKhau=_matKhau;
        this.hoTen=_hoTen;
        this.dangDangNhap=_dangDangNhap;
        this.tinhTrang=_tinhTrang;
        this.loai=_loai;
    }
    //***********
    // set parameter
    //***********
    public void setTenDangNhap(String _tenDangNhap){
        this.tenDangNhap=_tenDangNhap;
    }
    public void setMatKhau(String _matKhau){
        this.matKhau=_matKhau;
    }
    public void setHoTen(String _hoTen){
        this.hoTen=_hoTen;
    }
    public void setDangDangNhap(int _dangDangNhap){
        this.dangDangNhap=_dangDangNhap;
    }
    public void setTinhTrang(String _tinhTrang){
        this.tinhTrang=_tinhTrang;
    }
    public void setLoai(int _loai){
        this.loai=_loai;
    }
    //**************
    // get parameter
    //**************
    public String getTenDangNhap(){
        return this.tenDangNhap;
    }
    public String getMatKhau(){
       return this.matKhau;
    }
    public String getHoTen(){
       return this.hoTen;
    }
    public int getDangDangNhap(){
       return this.dangDangNhap;
    }
    public String getTinhTrang(){
        return this.tinhTrang;
    }
    public int getLoai(){
        return this.loai;
    }
         
}
