/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.model;

/**
 * Training program
 * @author LocNguyen
 */
public class TrainProgram {

    /**Training program**/
    private String programCode;
    /**Faculty Code**/
    private String facultyCode;
    /**Course Code**/
    private String courseCode;

    public TrainProgram(String programCode, String facultyCode, String courseCode) {
        this.programCode = programCode;
        this.facultyCode= facultyCode;
        this.courseCode = courseCode;
    }

    public String getProgramCode() {
        return programCode;
    }

    public void setProgramCode(String programCode) {
        this.programCode = programCode;
    }

    public String getFacultyCode() {
        return facultyCode;
    }

    public void setFacultyCode(String facultyCode) {
        this.facultyCode= facultyCode;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
}
