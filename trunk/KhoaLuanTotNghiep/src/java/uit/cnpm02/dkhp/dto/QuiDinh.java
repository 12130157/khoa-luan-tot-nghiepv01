/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.dto;

/**
 *
 * @author LocNguyen
 */
public class QuiDinh {
    
    private String maQD;
    private int giaTri;

    public QuiDinh() {
    }

    public QuiDinh(String maQD, int giaTri) {
        this.maQD = maQD;
        this.giaTri = giaTri;
    }

    public int getGiaTri() {
        return giaTri;
    }

    public void setGiaTri(int giaTri) {
        this.giaTri = giaTri;
    }

    public String getMaQD() {
        return maQD;
    }

    public void setMaQD(String maQD) {
        this.maQD = maQD;
    }
    
}
