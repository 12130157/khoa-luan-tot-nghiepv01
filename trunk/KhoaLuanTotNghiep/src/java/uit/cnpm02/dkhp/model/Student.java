/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.model;

/**
 *
 * @author thanh
 */
public class Student {
    private String studentCode;
    private String fullName;
    private String birthday;
    private String gender;
    private String identityCard;
    private String home;
    private String address;
    private String phone;
    private String email;
    private String classCode;
    private String facultyCode;
    private String courseCode;
    private String status;
    private String studyLevel;
    private String dateStart;
    private String studyType;
    private String note;
    public Student(){
        
    }
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
    
    public Student(String _studentCode, String _fullName, String _birthday, String _gender, String _identityCard, String _home,
            String _address, String _phone, String _email, String _classCode, String _facultyCode, String _courseCode, 
            String _status, String _studyLevel, String _dateStart, String _loaiHinHoc, String _note)   {
     this.studentCode=_studentCode;
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
    public void setStudentCode(String _studentCode){
        this.studentCode=_studentCode;
    }
   public void setFullName(String _fullName){
        this.fullName=_fullName;
    }
   public void setBirthday(String _birthday){
        this.birthday=_birthday;
    }
    public void setGender(String _gender){
        this.gender=_gender;
    }
   public void setIdentityCard(String _identityCard){
        this.identityCard=_identityCard;
    }
    public void setHome(String _home){
        this.home=_home;
    }
   public void setAddress(String _address){
        this.address=_address;
    }
    public void setPhone(String _phone){
        this.phone=_phone;
    }
    public void setEmail(String _email){
        this.email=_email;
    }
    public void setClassCode(String _classCode){
        this.classCode=_classCode;
    }
    public void setFacultyCode(String _facultyCode){
        this.facultyCode=_facultyCode;
    }
    public void setCourseCode(String _courseCode){
        this.courseCode=_courseCode;
    }
    public void setStatus(String _status){
        this.status=_status;
    }
    public void setStudyLevel(String _studyLevel){
        this.studyLevel=_studyLevel;
    }
    public void setDateStart(String _dateStart){
        this.dateStart=_dateStart;
    }
    public void setStudyType(String _studyType){
        this.studyType=_studyType;
    }
    public void setNote(String _note){
        this.note=_note;
    }
    //*****
    // get parameter
    //****
   public String getStudentCode(){
        return this.studentCode;
    }
    public String getFullName(){
       return this.fullName;
    }
   public String getBirthday(){
       return  this.birthday;
    }
   public String getGender(){
        return this.gender;
    }
     public String getIdentityCard(){
        return this.identityCard;
    }
   public String getHome(){
        return this.home;
    }
     public String getAddress(){
        return this.address;
    }
    public String getPhone(){
        return this.phone;
    }
    public String getEmail(){
        return this.email;
    }
    public String getClassCode(){
        return this.classCode;
    }
    public String getFacultyCode(){
        return this.facultyCode;
    }
    public String getCourseCode(){
        return this.courseCode;
    }
    public String getStatus(){
        return this.status;
    }
    public String getstudyLevel(){
        return this.studyLevel;
    }
    public String getDateStart(){
        return this.dateStart;
    }
    public String getStudyType(){
       return this.studyType;
    }
    public String getNote(){
       return this.note;
    }
    
}