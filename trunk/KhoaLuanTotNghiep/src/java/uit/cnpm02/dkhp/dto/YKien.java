/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.dto;

/**
 *
 * @author thanh
 */
public class YKien {
    String maYKien;
    String noiDung;
    String tacGia;
    String ngayGui;
    String tinhTrang;
    public YKien(){
        
    }
    public YKien(String _maYKien, String _noiDung, String _tacGia, String _ngayGui, String _tinhTrang){
        this.maYKien=_maYKien;
        this.noiDung=_noiDung;
        this.tacGia=_tacGia;
        this.ngayGui=_ngayGui;
        this.tinhTrang=_tinhTrang;
    }
    //************
    //set parameter
    //************
    public void setMaYKien(String _maYKien){
        this.maYKien=_maYKien;
    }
    public void setNoiDung(String _noiDung){
        this.noiDung=_noiDung;
    }
    public void setTacGia(String _tacGia){
        this.tacGia=_tacGia;
    }
    public void setNgayGui(String _ngayGui){
        this.ngayGui=_ngayGui;
    }
    public void setTinhTrang(String _tinhTrang){
        this.tinhTrang=_tinhTrang;
    }
    //************
    //get parameter
    //************
    public String getMaYKien(){
        return this.maYKien;
    }
    public String getNoiDung(){
        return this.noiDung;
    }
    public String getTacGia(){
        return this.tacGia;
    }
    public String getNgayGui(){
        return this.ngayGui;
    }
    public String getTinhTrang(){
        return this.tinhTrang;
    }
}
