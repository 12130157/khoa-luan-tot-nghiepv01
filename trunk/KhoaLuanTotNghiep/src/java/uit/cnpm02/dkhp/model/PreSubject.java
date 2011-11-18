package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.advancedJDBC.AdvancedAbstractJdbcModel;
import uit.cnpm02.dkhp.access.mapper.MapperConstant;

/**
 *
 * @author LocNguyen
 */
public class PreSubject extends AdvancedAbstractJdbcModel<PreSubID> {
    /**subject code**/
    private String subId;
    /**prerequisite subject code**/
    private String preSubId;

    public PreSubject() {
    }

    public PreSubject(String subId, String preSubId) {
        this.subId = subId;
        this.preSubId = preSubId;
    }

    public String getPreSubId() {
        return preSubId;
    }

    public void setPreSubId(String preSubId) {
        this.preSubId = preSubId;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    @Override
    public String[] getColumnNames() {
        return new String[] {};
    }

    @Override
    public Object[] getColumnValues() {
        return new Object[] {};
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
        return new String[] {
            "MaMH",
            "MaMHTQ"
        };
    }

    @Override
    public Object[] getIdColumnValues() {
        return new Object[] {
            subId,
            preSubId
        };
    }
    
}
