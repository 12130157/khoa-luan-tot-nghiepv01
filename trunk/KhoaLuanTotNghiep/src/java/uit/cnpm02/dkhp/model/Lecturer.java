/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.model;

import java.util.Date;
import uit.cnpm02.dkhp.access.mapper.MapperConstant;
import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcModel;

/**
 *
 * @author thanh
 */
public class Lecturer extends AbstractJdbcModel<String> {

    private String lecturerCode;
    private String fullName;
    private String facultyCode;
    private Date birthday;
    private String address;
    private String identityCard;
    private String phone;
    private String email;
    private String gender;
    private String hocHam;
    private String hocVi;
    private String note;

    public Lecturer() {
    }

    public Lecturer(String lecturerCode, String fullName, String facultyCode,
            Date birthday, String address, String identityCard, String phone,
            String email, String gender, String hocHam, String hocVi) {
        
        this.lecturerCode = lecturerCode;
        this.fullName = fullName;
        this.facultyCode = facultyCode;
        this.birthday = birthday;
        this.address = address;
        this.identityCard = identityCard;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.hocHam = hocHam;
        this.hocVi = hocVi;
        
        setId(lecturerCode);
    }

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHocHam() {
        return hocHam;
    }

    public void setHocHam(String hocHam) {
        this.hocHam = hocHam;
    }

    public String getHocVi() {
        return hocVi;
    }

    public void setHocVi(String hocVi) {
        this.hocVi = hocVi;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getLecturerCode() {
        return lecturerCode;
    }

    public void setLecturerCode(String lecturerCode) {
        this.lecturerCode = lecturerCode;
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
            birthday = (Date) values[2];
            address = values[3] == null ? "" : values[3].toString();
            identityCard = values[4].toString();
            phone = values[5] == null ? "" : values[5].toString();
            email = values[6] == null ? "" : values[6].toString();
            gender = values[7].toString();
            hocHam = values[8] == null ? "" : values[8].toString();
            hocVi = values[9] == null ? "" : values[9].toString();
            note = values[10] == null ? "" : values[10].toString();

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

    @Override
    public boolean isIdAutoIncrement() {
        return false;
    }

    public int compare(Lecturer o2, String by) {
        int result = 0;
        if (by.equalsIgnoreCase("MaGV")) {
            result = this.getId()
                    .compareTo(o2.getId());
        } else if (by.equalsIgnoreCase("HoTen")) {
            result = this.getFullName()
                    .compareTo(o2.getFullName());
        } else if (by.equalsIgnoreCase("MaKhoa")) {
            result = this.getFacultyCode().compareTo(o2.getFacultyCode());
        } else if (by.equalsIgnoreCase("NgaySinh")) {
            result = this.getBirthday().compareTo(o2.getBirthday());
        } else if (by.equalsIgnoreCase("GioiTinh")) {
            result = this.getGender().compareTo(o2.getGender());
        } else if (by.equalsIgnoreCase("Email")) {
            result = this.getEmail().compareTo(o2.getEmail());
        } else if (by.equalsIgnoreCase("HocHam")) {
            result = this.getHocHam().compareTo(o2.getHocHam());
        } else if (by.equalsIgnoreCase("HocVi")) {
            result = this.getHocVi().compareTo(o2.getHocVi());
        } else if (by.equalsIgnoreCase("QueQuan")) {
            result = this.getAddress().compareTo(o2.getAddress());
        }
        
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) && (this == null)) {
            return true;
        }
        
        if (! (obj instanceof Lecturer)) {
            return false;
        }
        
        Lecturer other = (Lecturer) obj;
        return this.getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (this.lecturerCode != null ? this.lecturerCode.hashCode() : 0);
        hash = 71 * hash + (this.identityCard != null ? this.identityCard.hashCode() : 0);
        return hash;
    }
}
