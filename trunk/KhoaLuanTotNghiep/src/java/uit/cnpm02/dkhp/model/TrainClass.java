package uit.cnpm02.dkhp.model;

import java.util.Date;
import uit.cnpm02.dkhp.access.advancedJDBC.AdvancedAbstractJdbcModel;
import uit.cnpm02.dkhp.access.mapper.MapperConstant;
import uit.cnpm02.dkhp.service.TrainClassStatus;

/**
 *
 * @author LocNguyen
 */
public class TrainClass extends AdvancedAbstractJdbcModel<TrainClassID>{
    private String subjectCode;
    private String lecturerCode;
    private int numOfStudent;
    private int numOfStudentReg;
    private int studyDate;
    private int shift;
    private String classRoom;
    private Date testDate;
    private String testHours;
    private String testRoom;
    private String subjectName;
    private String lecturerName;
    private Date startDate;
    private Date endDate;
    /**
     * Give current status of train class
     * There are 3 status for each TrainClass
     *  + Opened (1)
     *  + Canceled (2)
     *  + Closed (0)
     **/
    private TrainClassStatus status;
    private int numTC;
    private int updateScore;
    
    public static final int SCORE_NOT_UPDATED = 0;
    public static final int SCORE_UPDATED = 1;

    public TrainClass() {
    }
    
    public TrainClass(String classCode, String year, int semester, String subjectCode, String lecturerCode,
            String classRoom, int numOfStudent, int numOfStudentReg, int studyDate, int shift,
            Date testDate, String testRoom, String testHours) {
        TrainClassID trainClassID=new TrainClassID(classCode, year, semester);
        setId(trainClassID);
        this.subjectCode = subjectCode;
        this.lecturerCode = lecturerCode;
        this.classRoom = classRoom;
        this.numOfStudent = numOfStudent;
        this.numOfStudentReg = numOfStudentReg;
        this.studyDate = studyDate;
        this.shift = shift;
        this.testDate = testDate;
        this.testRoom = testRoom;
        this.testHours = testHours;
        this.subjectName = "";
        this.lecturerName = "";
        this.numTC = 0;
        
        this.status = TrainClassStatus.OPEN;
        updateScore=0;
    }

    public int getUpdateScore() {
        return updateScore;
    }

    public void setUpdateScore(int updateScore) {
        this.updateScore = updateScore;
    }

    public TrainClass(String classCode, String year, int semester, String subjectCode, String lecturerCode,
            String classRoom, int numOfStudent, int numOfStudentReg, int studyDate, int shift,
            Date testDate, String testRoom, String testHours, Date startDate, Date endDate) {

        this(classCode, year, semester, subjectCode, lecturerCode,
            classRoom, numOfStudent, numOfStudentReg, studyDate, shift,
            testDate, testRoom, testHours);
        
        this.startDate = startDate;
        this.endDate = endDate;
        
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
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
    public int getStudyDate() {
        return studyDate;
    }

    public void setStudyDate(int studyDate) {
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
        this.testHours = testHours;
    }
    public String getTestHours(){
     return this.testHours;   
    }
    public void setTestRoom(String testRoom){
        this.testRoom = testRoom;
    }
    public String getTestRoom(){
     return this.testRoom;   
    }
    public void setSubjectName(String subjectName){
        this.subjectName = subjectName;
    }
    public String getSubjectName(){
        return this.subjectName;
    }
     public void setLectturerName(String lectturerName){
         this.lecturerName = lectturerName;
    }
    public String getLectturerName(){
        return this.lecturerName;
    }
    public void setNumTC(int numTC){
        this.numTC = numTC;
    }
    public int getNumTC(){
        return this.numTC;
    }
    
    public void setStatus(TrainClassStatus status) {
        this.status = status;
    }
    
    public TrainClassStatus getStatus() {
        return status;
    }
    
    public int compare(TrainClass obj, String by) {
        /**
         * MaLopHoc
         * MonHoc
         * NamHoc
         * HocKy
         */
        int result = 0;
        if (by.equalsIgnoreCase("MaLopHoc")) {
            result = this.getId().getClassCode()
                    .compareTo(obj.getId().getClassCode());
        } else if (by.equalsIgnoreCase("MonHoc")) {
            result = this.getSubjectName()
                    .compareTo(obj.getSubjectName());
        } else if (by.equalsIgnoreCase("NamHoc")) {
            result = this.getId().getYear()
                    .compareTo(obj.getId().getYear());
        } else if (by.equalsIgnoreCase("HocKy")) {
            result = this.getId().getSemester() - obj.getId().getSemester();
        }
        
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof TrainClass))
            return false;
        
        TrainClass other = (TrainClass) obj;
        TrainClassID thisId = this.getId();
        TrainClassID otherId = other.getId();
        
        return (thisId.getClassCode().equals(otherId.getClassCode())
                && (thisId.getSemester() == otherId.getSemester())
                && thisId.getYear().equals(otherId.getYear()));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
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
                    "PhongThi",
                    "TrangThai",
                    "NgayBatDau",
                    "NgayKetThuc",
                    "CapNhatDiem"
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
                    testRoom,
                    (status == null ? null : status.getValue()),
                    startDate,
                    endDate,
                    updateScore
                };
    }

    @Override
    public void setColumnValues(Object[] values) {
        try {
            subjectCode = values[0].toString();
            lecturerCode = values[1].toString();
            numOfStudent = Integer.parseInt(values[2].toString());
            numOfStudentReg = Integer.parseInt(values[3].toString());
            studyDate = Integer.parseInt(values[4].toString());
            shift = Integer.parseInt(values[5].toString());
            classRoom = values[6].toString();
            testDate = (Date) (values[7] == null ? null : values[7]);
            testHours = values[8] == null ? "" : values[8].toString();
            testRoom = values[9] == null ? "" : values[9].toString();

            int statusValue = Integer.parseInt(values[10].toString());
            if (statusValue == TrainClassStatus.CLOSE.getValue()) {
                this.status = TrainClassStatus.CLOSE;
            } else if (statusValue == TrainClassStatus.OPEN.getValue()) {
                this.status = TrainClassStatus.OPEN;
            }
            startDate = (Date) (values[11] == null ? null : values[11]);
            endDate = (Date) (values[12] == null ? null : values[12]);
            updateScore = Integer.parseInt(values[13].toString());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Object[] getIdColumnValues() {
        return getId().getIDValues();
    }
}
