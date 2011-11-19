package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.advancedJDBC.AdvancedAbstractJdbcModel;
import uit.cnpm02.dkhp.access.mapper.MapperConstant;

/**
 *
 * @author LocNguyen
 */
public class PreSubject extends AdvancedAbstractJdbcModel<PreSubID> {

    public PreSubject() {
    }

    @Override
    public String[] getColumnNames() {
        return new String[]{};
    }

    @Override
    public Object[] getColumnValues() {
        return new Object[]{};
    }

    @Override
    public void setColumnValues(Object[] values) {
        //
    }

    @Override
    public String getTableName() {
        return MapperConstant.DB_NAME
                + ".monhoctienquyet";
    }

    @Override
    public String[] getIdColumnName() {
        return new String[]{
                    "MaMH",
                    "MaMHTQ"
                };
    }

    @Override
    public Object[] getIdColumnValues() {
        return getId().getIDValues();
    }

}
