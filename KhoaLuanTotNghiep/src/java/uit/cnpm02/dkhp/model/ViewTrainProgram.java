/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcModel;

/**
 *
 * @author thanh
 */
public class ViewTrainProgram extends AbstractJdbcModel<String>{
    private String subjectCode;
    private String subjectName;
    private int numTC;
    private int numTCLT;
    private int numTCTH;
    private float mark;
    private int semester;
    public ViewTrainProgram() {
    }

    public ViewTrainProgram(String _subjectCode, String _subjectName, int numtc, int numtclt, int numtcth, float mark, int semester) {
        this.subjectCode = _subjectCode;
        this.subjectName = _subjectName;
        this.numTC = numtc;
        this.numTCLT = numtclt;
        this.numTCTH = numtcth;
        this.mark = mark;
        this.semester = semester;
    }
     public void setSubCode(String _subjectCode) {
        this.subjectCode = _subjectCode;
    }

    public void setSubName(String _subjectName) {
         this.subjectName = _subjectName;
    }

    public void setNumTC(int numtc) {
        this.numTC = numtc;
    }

    public void setNumTCLT(int numtclt) {
        this.numTCLT = numtclt;
    }

    public void setNumTCTH(int numtcth) {
        this.numTCTH = numtcth;
    }

    public void setMark(float mark) {
        this.mark = mark;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getSubCode() {
        return this.subjectCode;
    }

    public String getSubName() {
        return this.subjectName;
    }

    public int getNumTC() {
        return this.numTC;
    }

    public int getNumTCLT() {
        return this.numTCLT;
    }

    public int getNumTCTH() {
        return this.numTCTH;
    }

    public float getMark() {
        return this.mark;
    }

    public int getSemester() {
        return this.semester;
    }

    @Override
    public String[] getColumnNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object[] getColumnValues() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setColumnValues(Object[] values) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
