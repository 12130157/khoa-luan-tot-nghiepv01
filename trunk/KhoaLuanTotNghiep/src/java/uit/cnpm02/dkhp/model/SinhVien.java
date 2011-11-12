/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.model;

/**
 *
 * @author thanh
 */
public class SinhVien {
    private String mssv;
    private String hoTen;
    private String ngaySinh;
    private String gioiTinh;
    private String cmnd;
    private String queQuan;
    private String diaChi;
    private String dienThoai;
    private String email;
    private String maLop;
    private String maKhoa;
    private String maKhoaHoc;
    private String tinhTrang;
    private String bacHoc;
    private String ngayNHapHoc;
    private String loaiHinhHoc;
    private String ghiChu;
    public SinhVien(){
        
    }
    /**
     * Constructor with parameter input:
     * @param _mssv: 
     * @param _hoTen
     * @param _ngaySinh
     * @param _gioiTinh
     * @param _cmnd
     * @param _queQuan
     * @param _diaChi
     * @param _dienThoai
     * @param _email
     * @param _maLop
     * @param _maKhoa
     * @param _maKhoaHoc
     * @param _tinhTrang
     * @param _bacHoc
     * @param _ngayNhapHoc
     * @param _loaiHinHoc
     * @param _ghiChu 
     */
    
    public SinhVien(String _mssv, String _hoTen, String _ngaySinh, String _gioiTinh, String _cmnd, String _queQuan,
            String _diaChi, String _dienThoai, String _email, String _maLop, String _maKhoa, String _maKhoaHoc, 
            String _tinhTrang, String _bacHoc, String _ngayNhapHoc, String _loaiHinHoc, String _ghiChu)   {
     this.mssv=_mssv;
     this.hoTen=_hoTen;
     this.ngaySinh=_ngaySinh;
     this.gioiTinh=_gioiTinh;
     this.cmnd=_cmnd;
     this.queQuan=_queQuan;
     this.diaChi=_diaChi;
     this.dienThoai=_dienThoai;
     this.email=_email;
     this.maLop=_maLop;
     this.maKhoa=_maKhoa;
     this.maKhoaHoc=_maKhoaHoc;
     this.tinhTrang=_tinhTrang;
     this.bacHoc=_bacHoc;
     this.ngayNHapHoc=_ngayNhapHoc;
     this.loaiHinhHoc=_loaiHinHoc;
     this.ghiChu="";
    }     
    //****
    //get parameter
    //****
    public void setMSSV(String _mssv){
        this.mssv=_mssv;
    }
   public void setHoTen(String _hoTen){
        this.hoTen=_hoTen;
    }
   public void setNgaySinh(String _ngaySinh){
        this.ngaySinh=_ngaySinh;
    }
    public void setGioiTinh(String _gioiTinh){
        this.gioiTinh=_gioiTinh;
    }
   public void setCMND(String _cmnd){
        this.cmnd=_cmnd;
    }
    public void setQueQuan(String _queQuan){
        this.queQuan=_queQuan;
    }
   public void setDiaChi(String _diaChi){
        this.diaChi=_diaChi;
    }
    public void setDienThoai(String _dienThoai){
        this.dienThoai=_dienThoai;
    }
    public void setEmail(String _email){
        this.email=_email;
    }
    public void setMaLop(String _maLop){
        this.maLop=_maLop;
    }
    public void setMaKhoa(String _maKhoa){
        this.maKhoa=_maKhoa;
    }
    public void setMaKhoaHoc(String _maKhoaHoc){
        this.maKhoaHoc=_maKhoaHoc;
    }
    public void setTinhTrang(String _tinhTrang){
        this.tinhTrang=_tinhTrang;
    }
    public void setBacHoc(String _bacHoc){
        this.bacHoc=_bacHoc;
    }
    public void setNgayNhapHoc(String _ngayNhapHoc){
        this.ngayNHapHoc=_ngayNhapHoc;
    }
    public void setLoaiHinhHoc(String _loaiHinhHoc){
        this.loaiHinhHoc=_loaiHinhHoc;
    }
    public void setGhiChu(String _ghiChu){
        this.ghiChu=_ghiChu;
    }
    //*****
    // get parameter
    //****
   public String getMSSV(){
        return this.mssv;
    }
    public String getHoTen(){
       return this.hoTen;
    }
   public String getNgaySinh(){
       return  this.ngaySinh;
    }
   public String getGioiTinh(){
        return this.gioiTinh;
    }
     public String getCMND(){
        return this.cmnd;
    }
   public String getQueQuan(){
        return this.queQuan;
    }
     public String getDiaChi(){
        return this.diaChi;
    }
    public String getDienThoai(){
        return this.dienThoai;
    }
    public String getEmail(){
        return this.email;
    }
    public String getMaLop(){
        return this.maLop;
    }
    public String getMaKhoa(){
        return this.maKhoa;
    }
    public String getMaKhoaHoc(){
        return this.maKhoaHoc;
    }
    public String getTinhTrang(){
        return this.tinhTrang;
    }
    public String getBacHoc(){
        return this.bacHoc;
    }
    public String getNgayNhapHoc(){
        return this.ngayNHapHoc;
    }
    public String getLoaiHinhHoc(){
       return this.loaiHinhHoc;
    }
    public String getGhiChu(){
       return this.ghiChu;
    }
    
}
