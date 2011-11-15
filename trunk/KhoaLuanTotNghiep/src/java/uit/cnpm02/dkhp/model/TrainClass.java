/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.model;

import java.util.Date;

/**
 *
 * @author LocNguyen
 */
public class TrainClass {
    private String classCode;
    private String year;
    private int semester;
    private String subjectCode;
    private String lecturerCode;
    private int numOfStudent;
    private int numOfStudentReg;
    private String studyDate;
    private int shift;
    private String classRoom;
    private String testDate;
    private String testHours;
    private String testRoom;

    public TrainClass() {
    }

    public TrainClass(String classCode, String year, int semester, String subjectCode, String lecturerCode,
            String classRoom, int numOfStudent, int numOfStudentReg, String studyDate, int shift,
            String testDate, String testRoom, String testHours) {
        this.classCode = classCode;
        this.year = year;
        this.semester = semester;
        this.subjectCode = subjectCode;
        this.lecturerCode = lecturerCode;
        this.classRoom = classRoom;
        this.numOfStudent = numOfStudent;
        this.numOfStudentReg = numOfStudentReg;
        this.studyDate = studyDate;
        this.shift=shift;
        this.testDate=testDate;
        this.testRoom=testRoom;
        this.testHours=testHours;
    }

    public int getShift() {
        return shift;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getLecturerCode() {
        return lecturerCode;
    }

    public void setLecturerCode(String lecturerCode) {
        this.lecturerCode = lecturerCode;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getStudyDate() {
        return studyDate;
    }

    public void setStudyDate(String studyDate) {
        this.studyDate = studyDate;
    }

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public int getNumOfStudent() {
        return numOfStudent;
    }

    public void setNumOfStudent(int numOfStudent) {
        this.numOfStudent = numOfStudent;
    }

    public int getNumOfStudentReg() {
        return numOfStudentReg;
    }

    public void setNumOfStudentReg(int numOfStudentReg) {
        this.numOfStudentReg = numOfStudentReg;
    }
    public void setTestHours(String testHours){
        this.testHours=testHours;
    }
    public String setTestHours(){
     return this.testHours;   
    }
    public void setTestRoom(String testRoom){
        this.testRoom=testRoom;
    }
    public String setTestRoom(){
     return this.testRoom;   
    }
    
}
