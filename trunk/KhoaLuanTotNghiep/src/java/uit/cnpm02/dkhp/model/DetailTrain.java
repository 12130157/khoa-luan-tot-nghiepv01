/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.advancedJDBC.AdvancedAbstractJdbcModel;
import uit.cnpm02.dkhp.access.mapper.MapperConstant;

/**
 *
 * @author thanh
 */
public class DetailTrain extends AdvancedAbstractJdbcModel<DetailTrainID> {
    private String lecturerName;
    private String subjectName;

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
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
    public String getTableName() {
        return MapperConstant.DB_NAME
                + ".chitietgiangday";
    }
     @Override
    public String[] getIdColumnName() {
        return new String[]{
                    "MaGV",
                    "MaMH"
                };
    }
    @Override
    public Object[] getIdColumnValues() {
        return getId().getIDValues();
    }
    
    @Override
    public void setColumnValues(Object[] values) {
        
    }
    
}
