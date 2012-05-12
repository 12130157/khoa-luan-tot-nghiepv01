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
 * @author thanh
 */
public class RegistrationTime extends AdvancedAbstractJdbcModel<RegistrationTimeID>{
private Date startDate;
private Date endDate;

public RegistrationTime(){
    
}
public RegistrationTime(RegistrationTimeID id, Date _startDate, Date _enđate){
    setId(id);
    this.startDate=_startDate;
    this.endDate=_enđate;
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
 @Override
    public String getTableName() {
        return MapperConstant.DB_NAME
                + ".ThoiGianDangKy";
    }
     @Override
    public String[] getColumnNames() {
        return new String[]{
                    "NgayBatDau",
                    "NgayKetThuc"
              };
    }

    @Override
    public Object[] getColumnValues() {
         return new Object[]{
                    startDate,
                    endDate
                };
    }
     @Override
    public void setColumnValues(Object[] values) {
        try {
            startDate = (Date) (values[0] == null ? null : values[0]);
            endDate = (Date) (values[1] == null ? null : values[1]);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
     @Override
    public String[] getIdColumnName() {
        return new String[]{
                   "HocKy",
                   "NamHoc"
                };
    }
@Override
    public Object[] getIdColumnValues() {
        return getId().getIDValues();
    }
}
