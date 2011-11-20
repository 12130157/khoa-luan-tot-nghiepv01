package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcModel;
import uit.cnpm02.dkhp.access.mapper.MapperConstant;

/**
 *
 * @author LocNguyen
 */
public class Rule extends  AbstractJdbcModel<String>{
    
    private String ruleCode;
    private int value;

    public Rule() {
    }

    public Rule(String ruleCode, int value) {
        this.ruleCode = ruleCode;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }
     @Override
    public String getIdColumnName() {
        return "Ma";
    }
     @Override
    public String getTableName() {
        return MapperConstant.DB_NAME
                + ".QuyDinh";
    }
    @Override
    public String[] getColumnNames() {
        return new String[]{
                    "GiaTri"
                };
    }

   @Override
    public void setColumnValues(Object[] values) {
        try {
            value =Integer.parseInt(values[0].toString());
          } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
     @Override
    public Object[] getColumnValues() {
        return new Object[]{
                  value
        };
    }
    
}
