/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.model;

/**
 *  study result of student
 * @author LocNguyen
 */
public class StudyResult {
    private String studentCode;
    /**subject code**/
    private String subjectCode;
    private String year;
    
    private int semester;
    private float mark;

    public StudyResult() {
    }

    public StudyResult(String studentCode, String subjectCode, String year, int semester, float mark) {
        this.studentCode = studentCode;
        this.subjectCode = subjectCode;
        this.year = year;
        this.semester = semester;
        this.mark = mark;
    }

    public float getMark() {
        return mark;
    }

    public void setMark(float mark) {
        this.mark = mark;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
