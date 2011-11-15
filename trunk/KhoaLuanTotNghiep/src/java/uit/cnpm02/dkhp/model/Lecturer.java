/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.mapper.MapperConstant;
import uit.cnpm02.dkhp.access.util.AbstractJdbcModel;

/**
 *
 * @author thanh
 */
public class Lecturer extends  AbstractJdbcModel<String>{
   String lecturerCode;
   String fullName;
   String facultyCode;
   String birthday;
   String address;
   String identityCard;
   String phone;
   String email;
   String gender;
   String hocHam;
   String hocVi;
   String note;
   public Lecturer(){
       
   }
   public Lecturer(String _lecturerCode, String _fullName, String _facultyCode, String _birthday, String _address, 
           String _identityCard, String _phone, String _email, String _gender, String _hocHam, String _hocVi){
       this.lecturerCode=_lecturerCode;
       this.fullName=_fullName;
       this.facultyCode=_facultyCode;
       this.birthday=_birthday;
       this.address=_address;
       this.identityCard=_identityCard;
       this.phone=_phone;
       this.email=_email;
       this.gender=_gender;
       this.hocHam=_hocHam;
       this.hocVi=_hocVi;
       this.note="";
   }
   //**********
   //set parameter
   //**********
   public void setLecturerCode(String _lecturerCode){
       this.lecturerCode=_lecturerCode;
   }
   public void setFullName(String _fullName){
       this.fullName=_fullName;
   }
   public void setFacultyCode(String _facultyCode){
       this.facultyCode=_facultyCode;
   }
   public void setBirthday(String _birthday){
       this.birthday=_birthday;
   }
   public void setAddress(String _address){
       this.address=_address;
   }
   public void setIdentityCard(String _identityCard){
       this.identityCard=_identityCard;
   }
   public void setPhone(String _phone){
       this.phone=_phone;
   }
    public void setEmail(String _email){
       this.email=_email;
   }
   public void setGender(String _gender){
       this.gender=_gender;
   }
   public void setHocHam(String _hocHam){
       this.hocHam=_hocHam;
   }
   public void setHocVi(String _hocVi){
       this.hocVi=_hocVi;
   }
   public void setNote(String _note){
       this.note=_note;
   }
   //*********
   //get parameter
   //*********
   public String getLecturerCode(){
       return this.lecturerCode;
   }
   public String getFullName(){
       return this.fullName;
   }
   public String getFacultyCode(){
       return this.facultyCode;
   }
   public String getBirthday(){
       return this.birthday;
   }
   public String getAddress(){
      return  this.address;
   }
   public String getIdentityCard(){
       return this.identityCard;
   }
   public String getPhone(){
       return this.phone;
   }
    public String getEmail(){
      return  this.email;
   }
   public String getGender(){
       return this.gender;
   }
   public String getHocHam(){
       return this.hocHam;
   }
   public String getHocVi(){
       return this.hocVi;
   }
   public String getNote(){
       return this.note;
   }
     @Override
    public String getIdColumnName() {
        return "MaGV";
    }
     @Override
    public String getTableName() {
        return MapperConstant.DB_NAME
                + ".GiangVien";
    }
    @Override
    public String[] getColumnNames() {
        return new String[]{
                    "HoTen",
                    "MaKhoa",
                    "NgaySinh",
                    "QueQuan",
                    "CMND",
                    "DienThoai",
                    "Email",
                    "GioiTinh",
                    "HocHam",
                    "HocVi",
                    "GhiChu"
                
                };
    }

   @Override
    public void setColumnValues(Object[] values) {
        try {
            fullName = values[0].toString();
            facultyCode = values[1].toString();
            birthday = values[2].toString();
            address = values[3].toString();
            identityCard=values[4].toString();
            phone=values[5].toString();
            email=values[6].toString();
            gender=values[7].toString();
            hocHam=values[8].toString();
            hocVi=values[9].toString();
            note=values[10].toString();

        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
     @Override
    public Object[] getColumnValues() {
        return new Object[]{
                    fullName,
                    facultyCode,
                    birthday,
                    address,
                    identityCard,
                    phone,
                    email,
                    gender,
                    hocHam,
                    hocVi,
                    note
                };
    }
}
