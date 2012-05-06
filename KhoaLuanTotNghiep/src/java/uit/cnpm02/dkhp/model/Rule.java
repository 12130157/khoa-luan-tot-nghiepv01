package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcModel;
import uit.cnpm02.dkhp.access.mapper.MapperConstant;

/**
 *
 * @author LocNguyen
 */
public class Rule extends  AbstractJdbcModel<String>{
    
    private float value;
    private String description;

    public Rule() {
        this.description = "";
    }

    public Rule(String id, int value) {
        setId(id);
        this.value = value;
        this.description = "";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
                    "GiaTri",
                    "MoTa"
                };
    }

   @Override
    public void setColumnValues(Object[] values) {
        try {
            value = Float.parseFloat(values[0].toString());
            if (values[1] != null) {
                description = values[1].toString();
            }
          } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
     @Override
    public Object[] getColumnValues() {
         return new Object[]{
                     value,
                     description
                 };
    }

    public int compare(Rule o2, String by) {
        int result = 0;
        if (by.equalsIgnoreCase("Ma")) {
            result = this.getId()
                    .compareTo(o2.getId());
        } else if (by.equalsIgnoreCase("GiaTri")) {
            result = (int) (this.getValue() - o2.getValue());
        } else if (by.equalsIgnoreCase("MoTa")) {
            result = this.getDescription()
                    .compareTo(o2.getDescription());
        }
        
        return result;
    }
    
}
