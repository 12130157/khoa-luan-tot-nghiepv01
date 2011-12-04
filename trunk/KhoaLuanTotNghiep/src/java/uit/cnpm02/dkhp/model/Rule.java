package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcModel;
import uit.cnpm02.dkhp.access.mapper.MapperConstant;

/**
 *
 * @author LocNguyen
 */
public class Rule extends  AbstractJdbcModel<String>{
    
    private float value;

    public Rule() {
    }

    public Rule(String id, int value) {
        setId(id);
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
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
            value =Float.parseFloat(values[0].toString());
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
