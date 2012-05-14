package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.mapper.MapperConstant;
import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcModel;

/**
 *
 * @author thanh
 */
public class Account extends AbstractJdbcModel<String> {
    private String userName;
    private String password;
    private String fullName;
    private boolean   isLogined;//1 dang dang nhap, 0 chua dang nhap
    private int status;//1 dang khoa, 0 binh thuong
    private int type;

    public Account() {
    }

    public Account(String userName, String password, String fullName, boolean  isLogined, int status, int type) {
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.isLogined = isLogined;
        this.status = status;
        this.type = type;
        setId(userName);
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean  getIsLogined() {
        return isLogined;
    }

    public void setIsLogined(boolean  isLogined) {
        this.isLogined = isLogined;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getIdColumnName() {
        return "TenDangNhap";
    }

    @Override
    public String getTableName() {
        return MapperConstant.DB_NAME
                + ".TaiKhoan";
    }

    @Override
    public boolean isIdAutoIncrement() {
        return false;
    }

    @Override
    public String[] getColumnNames() {
        return new String[]{
                    "MatKhau",
                    "HoTen",
                    "DangDangNhap",
                    "TinhTrang",
                    "Loai"
                };
    }

    @Override
    public Object[] getColumnValues() {
        return new Object[]{
                    password,
                    fullName,
                    isLogined,
                    status,
                    type
                };
    }

    @Override
    public void setColumnValues(Object[] values) {
        try {
            password = values[0].toString();
            fullName = values[1].toString();
            isLogined =Boolean.parseBoolean(values[2].toString());
            //status =Integer.parseInt(values[3].toString());
            status = Integer.parseInt(values[3].toString());
            type = Integer.parseInt(values[4].toString());
            
            userName = getId();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null && this == null)
            return true;
        
        if (!(obj instanceof Account)) {
            return false;
        }
        
        Account other = (Account) obj;
        return this.getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.userName != null ? this.userName.hashCode() : 0);
        return hash;
    }

    public int compare(Account o2, String by) {
        int result = 0;
        if (by.equalsIgnoreCase("TenDangNhap")) {
            result = this.getId()
                    .compareTo(o2.getId());
        } else if (by.equalsIgnoreCase("HoTen")) {
            result = this.getFullName()
                    .compareTo(o2.getFullName());
        } else if (by.equalsIgnoreCase("TinhTrang")) {
            result = this.getStatus() -  o2.getStatus();
        } else if (by.equalsIgnoreCase("Loai")) {
            result = this.getType() - o2.getType();
        }
        
        return result;
    }
    
}
