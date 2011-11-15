package uit.cnpm02.dkhp.model;

/**
 *
 * @author LocNguyen
 */
public class PreSubject {
    /**subject code**/
    private String subjectCode;
    /**prerequisite subject code**/
    private String preSubjectCode;

    public PreSubject() {
    }

    public PreSubject(String subjectCode, String preSubjectCode) {
        this.subjectCode = subjectCode;
        this.preSubjectCode = preSubjectCode;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getPreSubjectCode() {
        return preSubjectCode;
    }

    public void setPreSubjectCode(String preSubjectCode) {
        this.preSubjectCode = preSubjectCode;
    }
    
}
