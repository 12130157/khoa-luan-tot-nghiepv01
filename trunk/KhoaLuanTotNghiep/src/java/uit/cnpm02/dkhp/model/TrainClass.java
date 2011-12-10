/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.model;

import java.util.Date;
import uit.cnpm02.dkhp.access.advancedJDBC.AdvancedAbstractJdbcModel;
import uit.cnpm02.dkhp.access.mapper.MapperConstant;

/**
 *
 * @author LocNguyen
 */
public class TrainClass extends AdvancedAbstractJdbcModel<TrainClassID>{
    private String subjectCode;
    private String lecturerCode;
    private int numOfStudent;
    private int numOfStudentReg;
    private String studyDate;
    private int shift;
    private String classRoom;
    private Date testDate;
    private String testHours;
    private String testRoom;
    private String subjectName;
    private String lecturerName;
    int numTC;

    public TrainClass() {
    }

    public TrainClass(String classCode, String year, int semester, String subjectCode, String lecturerCode,
            String classRoom, int numOfStudent, int numOfStudentReg, String studyDate, int shift,
            Date testDate, String testRoom, String testHours) {
        TrainClassID trainClassID=new TrainClassID(classCode, year, semester);
        setId(trainClassID);
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
        this.subjectName="";
        this.lecturerName="";
        numTC=0;
    }

    public int getShift() {
        return shift;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }
    public String getLecturerCode() {
        return lecturerCode;
    }

    public void setLecturerCode(String lecturerCode) {
        this.lecturerCode = lecturerCode;
    }
    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }
    public String getStudyDate() {
        return studyDate;
    }

    public void setStudyDate(String studyDate) {
        this.studyDate = studyDate;
    }

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date testDate) {
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
    public String getTestHours(){
     return this.testHours;   
    }
    public void setTestRoom(String testRoom){
        this.testRoom=testRoom;
    }
    public String getTestRoom(){
     return this.testRoom;   
    }
    public void setSubjectName(String _subjectName){
        this.subjectName=_subjectName;
    }
    public String getSubjectName(){
        return this.subjectName;
    }
     public void setLectturerName(String _lectturerNam){
        this.lecturerName=_lectturerNam;
    }
    public String getLectturerName(){
        return this.lecturerName;
    }
    public void setNumTC(int _numTC){
        this.numTC=_numTC;
    }
    public int getNumTC(){
        return this.numTC;
    }
    @Override
    public String getTableName() {
        return MapperConstant.DB_NAME
                + ".lophoc";
    }
     @Override
    public String[] getIdColumnName() {
        return new String[]{
                    "MaLopHoc",
                    "HocKy",
                    "NamHoc"
                };
    }
    @Override
    public String[] getColumnNames() {
        return new String[]{
                    "MaMH",
                    "MaGV",
                    "SLSV",
                    "SLDK",
                    "NgayHoc",
                    "CaHoc",
                    "PhongHoc",
                    "NgayThi",
                    "CaThi",
                    "PhongThi"
              };
    }

    @Override
    public Object[] getColumnValues() {
         return new Object[]{
                    subjectCode,
                    lecturerCode,
                    numOfStudent,
                    numOfStudentReg,
                    studyDate,
                    shift,
                    classRoom,
                    testDate,
                    testHours,
                    testRoom
                };
    }

    @Override
    public void setColumnValues(Object[] values) {
        try {
                    subjectCode=values[0].toString();
                    lecturerCode=values[1].toString();
                    numOfStudent=Integer.parseInt(values[2].toString());
                    numOfStudentReg=Integer.parseInt(values[3].toString());
                    studyDate=values[4].toString();
                    shift=Integer.parseInt(values[5].toString());
                    classRoom=values[6].toString();
                    testDate=(Date) (values[7] == null ? null : values[7]);
                    testHours=values[8] == null ? "" : values[8].toString();
                    testRoom =values[9] == null ? "" : values[9].toString();
            

        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Object[] getIdColumnValues() {
        return getId().getIDValues();
    }
}
