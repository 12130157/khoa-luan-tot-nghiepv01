/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.model;

/**
 *
 * @author LocNguyen
 */
public class TrainProDetail {
    /**Training Program code**/
    private String programCode;
    
    /**Subject code**/
    private String subjectCode;
    
    private int semester;

    public TrainProDetail() {
    }

    public TrainProDetail(String programCode, String subjectCode, int semester) {
        this.programCode = programCode;
        this.subjectCode = subjectCode;
        this.semester = semester;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    /**
     * Get training program code
     * @return ma CTDT
     */
    public String getProgramCode() {
        return programCode;
    }

    /**
     * Set training program code
     * @param programCode 
     */
    public void setProgramCode(String programCode) {
        this.programCode = programCode;
    }

    /**
     * Get subject code
     * @return subjectCode
     */
    public String getSubjectCode() {
        return subjectCode;
    }

    /**
     * Set subject code
     * @param subjectCode 
     */
    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }
}
