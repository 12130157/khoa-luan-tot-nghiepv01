/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.dto;

/**
 *
 * @author thanh
 */
public class GiangVien {
   String maGV;
   String hoTen;
   String maKhoa;
   String ngaySinh;
   String queQuan;
   String cmnd;
   String dienThoai;
   String email;
   String gioiTinh;
   String hocHam;
   String hocVi;
   String ghiChu;
   public GiangVien(){
       
   }
   public GiangVien(String _maGV, String _hoTen, String _maKhoa, String _ngaySinh, String _queQuan, 
           String _cmnd, String _dienThoai, String _email, String _gioiTinh, String _hocHam, String _hocVi){
       this.maGV=_maGV;
       this.hoTen=_hoTen;
       this.maKhoa=_maKhoa;
       this.ngaySinh=_ngaySinh;
       this.queQuan=_queQuan;
       this.cmnd=_cmnd;
       this.dienThoai=_dienThoai;
       this.email=_email;
       this.gioiTinh=_gioiTinh;
       this.hocHam=_hocHam;
       this.hocVi=_hocVi;
       this.ghiChu="";
   }
   //**********
   //set parameter
   //**********
   public void setMaGV(String _maGV){
       this.maGV=_maGV;
   }
   public void setHoTen(String _hoTen){
       this.hoTen=_hoTen;
   }
   public void setMaKhoa(String _maKhoa){
       this.maKhoa=_maKhoa;
   }
   public void setNgaySinh(String _NgaySinh){
       this.ngaySinh=_NgaySinh;
   }
   public void setQueQuan(String _queQuan){
       this.queQuan=_queQuan;
   }
   public void setCMND(String _cmnd){
       this.cmnd=_cmnd;
   }
   public void setDienThoai(String _dienThoai){
       this.dienThoai=_dienThoai;
   }
    public void setEmail(String _email){
       this.email=_email;
   }
   public void setGioiTinh(String _gioiTinh){
       this.gioiTinh=_gioiTinh;
   }
   public void setHocHam(String _hocHam){
       this.hocHam=_hocHam;
   }
   public void setHocVi(String _hocVi){
       this.hocVi=_hocVi;
   }
   public void setGhiChu(String _ghiChu){
       this.ghiChu=_ghiChu;
   }
   //*********
   //get parameter
   //*********
   public String getMaGV(){
       return this.maGV;
   }
   public String getHoTen(){
       return this.hoTen;
   }
   public String getMaKhoa(){
       return this.maKhoa;
   }
   public String getNgaySinh(){
       return this.ngaySinh;
   }
   public String getQueQuan(){
      return  this.queQuan;
   }
   public String getCMND(){
       return this.cmnd;
   }
   public String getDienThoai(){
       return this.dienThoai;
   }
    public String getEmail(){
      return  this.email;
   }
   public String getGioiTinh(){
       return this.gioiTinh;
   }
   public String getHocHam(){
       return this.hocHam;
   }
   public String getHocVi(){
       return this.hocVi;
   }
   public String getGhiChu(){
       return this.ghiChu;
   }
}
