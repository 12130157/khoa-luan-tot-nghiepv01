/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.model;

import java.sql.Time;
import java.util.Date;
import uit.cnpm02.dkhp.access.mapper.MapperConstant;
import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcModel;

/**
 *
 * @author LocNguyen
 */
public class Logs extends  AbstractJdbcModel<String> {
    
    private String logCode;
    private String account;
    private String content;
    private Date createDate;
    private Time createHours;   
    public Logs() {
    }

    public Logs(String logCode, String account, String content, Date createDate, Time createHours) {
        this.logCode = logCode;
        this.account = account;
        this.content = content;
        this.createDate = createDate;
        this.createHours=createHours;
    }

    public String getLogCode() {
        return logCode;
    }

    public void setLogCode(String logCode) {
        this.logCode = logCode;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
     public Time getCreateHours() {
        return createHours;
    }

    public void setCreateHours(Time createHours) {
        this.createHours = createHours;
    }
     @Override
    public String getIdColumnName() {
        return "Ma";
    }
     @Override
    public String getTableName() {
        return MapperConstant.DB_NAME
                + ".NhatKy";
    }
    @Override
    public String[] getColumnNames() {
        return new String[]{
                    "TaiKhoan",
                    "NoiDung",
                    "Ngay",
                    "Gio"
                };
    }

   @Override
    public void setColumnValues(Object[] values) {
        try {
            account = values[0].toString();
            content = values[1].toString();
            createDate =(Date)values[2];
            createHours =Time.valueOf(values[3].toString());

        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
     @Override
    public Object[] getColumnValues() {
        return new Object[]{
                    account,
                    content,
                    createDate,
                    createHours
                };
    }
}
