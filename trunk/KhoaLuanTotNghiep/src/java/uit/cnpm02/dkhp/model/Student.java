package uit.cnpm02.dkhp.model;

import java.util.Date;
import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcModel;
import uit.cnpm02.dkhp.access.mapper.MapperConstant;

/**
 *
 * @author thanh
 */
public class Student extends AbstractJdbcModel<String> {

    private String fullName;
    private Date birthday;
    private String gender;
    private String identityNumber;
    private String homeAddr;
    private String address;
    private String phone;
    private String email;
    private String classCode;
    private String facultyCode;
    private String courseCode;
    private String status;
    private String studyLevel;
    private Date dateStart;
    private String studyType;
    private String note;
<<<<<<< .mine
    
    public Student(){
        
=======

    public Student() {
>>>>>>> .r102
    }
<<<<<<< .mine
    /**
     * Constructor with parameter input:
     * @param _studentCode: 
     * @param _fullName
     * @param _birthday
     * @param _gender
     * @param _identityCard
     * @param _home
     * @param _address
     * @param _phone
     * @param _email
     * @param _classCode
     * @param _facultyCode
     * @param _courseCode
     * @param _status
     * @param _studyLevel
     * @param _dateStart
     * @param _loaiHinHoc
     * @param _note 
     */
    
    public Student(String id, String _fullName, Date _birthday, String _gender, String _identityCard, String _home,
            String _address, String _phone, String _email, String _classCode, String _facultyCode, String _courseCode, 
            String _status, String _studyLevel, Date _dateStart, String _loaiHinHoc, String _note)   {
     setId(id);
     this.fullName=_fullName;
     this.birthday=_birthday;
     this.gender=_gender;
     this.identityCard=_identityCard;
     this.home=_home;
     this.address=_address;
     this.phone=_phone;
     this.email=_email;
     this.classCode=_classCode;
     this.facultyCode=_facultyCode;
     this.courseCode=_courseCode;
     this.status=_status;
     this.studyLevel=_studyLevel;
     this.dateStart=_dateStart;
     this.studyType=_loaiHinHoc;
     this.note="";
    }     
    //****
    //get parameter
    //****
=======

    public Student(String studentCode, String fullName, Date birthday, String gender, String identityNumber, String homeAddr, String address, String phone, String email, String classCode, String facultyCode, String courseCode, String status, String studyLevel, Date dateStart, String studyType, String note) {
        this.studentCode = studentCode;
        this.fullName = fullName;
        this.birthday = birthday;
        this.gender = gender;
        this.identityNumber = identityNumber;
        this.homeAddr = homeAddr;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.classCode = classCode;
        this.facultyCode = facultyCode;
        this.courseCode = courseCode;
        this.status = status;
        this.studyLevel = studyLevel;
        this.dateStart = dateStart;
        this.studyType = studyType;
        this.note = note;
    }
>>>>>>> .r102

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacultyCode() {
        return facultyCode;
    }

    public void setFacultyCode(String facultyCode) {
        this.facultyCode = facultyCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
<<<<<<< .mine
    //*****
    // get parameter
    //****
   public String getFullName(){
       return this.fullName;
=======

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
>>>>>>> .r102
    }

    public String getHomeAddr() {
        return homeAddr;
    }

    public void setHomeAddr(String homeAddr) {
        this.homeAddr = homeAddr;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStudentCode() {
        return studentCode;
    }
<<<<<<< .mine
    public String getStudyLevel(){
        return this.studyLevel;
=======

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
>>>>>>> .r102
    }

    public String getStudyLevel() {
        return studyLevel;
    }

    public void setStudyLevel(String studyLevel) {
        this.studyLevel = studyLevel;
    }

    public String getStudyType() {
        return studyType;
    }

    public void setStudyType(String studyType) {
        this.studyType = studyType;
    }

    @Override
    public String getIdColumnName() {
        return "MSSV";
    }

    @Override
    public String getTableName() {
        return MapperConstant.DB_NAME
                + ".SinhVien";
    }

    @Override
    public String[] getColumnNames() {
        return new String[]{
                    "HoTen",
                    "NgaySinh",
                    "GioiTinh",
                    "CMND",
                    "QueQuan",
                    "DiaChi",
                    "DienThoai",
                    "Email",
                    "MaLop",
                    "MaKhoa",
                    "MaKhoaHoc",
                    "TinhTrang",
                    "BacHoc",
                    "NgayNhapHoc",
                    "LoaiHinhHoc",
                    "GhiChu"
                };
    }

    @Override
    public void setColumnValues(Object[] values) {
        try {
            fullName = values[0].toString();
            birthday = (Date) values[1];
            gender = values[2].toString();
            identityNumber = values[3].toString();
            homeAddr = values[4].toString();
            address = values[5].toString();
            phone = values[6].toString();
            email = values[7].toString();
            classCode = values[8].toString();
            facultyCode = values[9].toString();
            courseCode = values[10].toString();
            status = values[11].toString();
            studyLevel = values[12].toString();
            dateStart = (Date) values[13];
            studyType = values[14].toString();
            try{
            note = values[15].toString();
<<<<<<< .mine
            }catch(Exception ex){
                note="";
            }

=======
>>>>>>> .r102
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Object[] getColumnValues() {
        return new Object[]{
                    fullName,
                    birthday,
                    gender,
                    identityNumber,
                    homeAddr,
                    address,
                    phone,
                    email,
                    classCode,
                    facultyCode,
                    courseCode,
                    status,
                    studyLevel,
                    dateStart,
                    studyType,
                    note
                };
    }
}
