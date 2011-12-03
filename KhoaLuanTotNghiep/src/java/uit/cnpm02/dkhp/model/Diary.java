package uit.cnpm02.dkhp.model;

import java.sql.Time;
import java.util.Date;
import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcModel;
import uit.cnpm02.dkhp.access.mapper.MapperConstant;

/**
 *
 * @author LocNguyen
 */
public class Diary extends AbstractJdbcModel<Integer> {

    private String userName;
    private String content;
    private Date date;
    private Time time;

    public Diary() {
    }

    public Diary(String userName, String content, Date date, Time time) {
        this.userName = userName;
        this.content = content;
        this.date = date;
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
    public String getIdColumnName() {
        return "Ma";
    }

    @Override
    public Object[] getColumnValues() {
        return new Object[]{
                    userName,
                    content,
                    date,
                    time
        };
    }

    @Override
    public void setColumnValues(Object[] values) {
        try {
            userName = values[0].toString();
            content = values[1].toString();
            date = (Date)values[2];
            time = (Time)values[3];
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
