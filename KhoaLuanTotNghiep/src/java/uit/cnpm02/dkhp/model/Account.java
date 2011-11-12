package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.mapper.MapperConstant;
import uit.cnpm02.dkhp.access.util.AbstractJdbcModel;

/**
 *
 * @author thanh
 */
public class Account extends AbstractJdbcModel<String> {
    private String userName;
    private String password;
    private String fullName;
    private int isLogined;//1 dang dang nhap, 0 chua dang nhap
    private String status;
    private int type;

    public Account() {
    }

    public Account(String userName, String password, String fullName, int isLogined, String status, int type) {
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.isLogined = isLogined;
        this.status = status;
        this.type = type;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getIsLogined() {
        return isLogined;
    }

    public void setIsLogined(int isLogined) {
        this.isLogined = isLogined;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
                    "TenDangNhap",
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
                    userName,
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
            userName = values[0].toString();
            password = values[1].toString();
            fullName = values[2].toString();
            isLogined = Integer.parseInt(values[3].toString());
            status = values[4].toString();
            type = Integer.parseInt(values[4].toString());

        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
