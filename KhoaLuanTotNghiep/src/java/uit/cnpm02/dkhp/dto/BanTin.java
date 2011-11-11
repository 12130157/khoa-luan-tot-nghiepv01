/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.dto;

/**
 *
 * @author thanh
 */
public class BanTin {
    String maTin;
    String tieuDe;
    String noiDung;
    String nguoiDang;
    String ngayTao;
    int loai;
    public BanTin(){
        
    }
    public BanTin(String _maTin, String _tieuDe, String _noiDung, String _nguoiDang, String _ngayTao, int _loai){
        this.maTin=_maTin;
        this.tieuDe=_tieuDe;
        this.noiDung=_noiDung;
        this.nguoiDang=_nguoiDang;
        this.ngayTao=_ngayTao;
        this.loai=_loai;
    }
    //******
    //set parameter
    //******
    public void setMaTin(String _maTin){
        this.maTin=_maTin;
    }
     public void setTieuDe(String _tieuDe){
        this.tieuDe=_tieuDe;
    }
      public void setNoiDung(String _noiDung){
        this.noiDung=_noiDung;
    }
       public void setNguoiDang(String _nguoiDang){
        this.nguoiDang=_nguoiDang;
    }
        public void setNgayTao(String _ngayTao){
        this.ngayTao=_ngayTao;
    }
        public void setLoai(int _Loai){
        this.loai=_Loai;
    }
        //******
    //get parameter
    //******
    public String getMaTin(){
        return this.maTin;
    }
     public String getTieuDe(){
        return this.tieuDe;
    }
      public String getNoiDung(){
        return this.noiDung;
    }
       public String getNguoiDang(){
        return this.nguoiDang;
    }
        public String getNgayTao(){
        return this.ngayTao;
    }
        public int getLoai(){
       return this.loai;
    }
    
}
