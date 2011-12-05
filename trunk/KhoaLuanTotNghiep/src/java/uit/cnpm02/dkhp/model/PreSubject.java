package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.advancedJDBC.AdvancedAbstractJdbcModel;
import uit.cnpm02.dkhp.access.mapper.MapperConstant;

/**
 *
 * @author LocNguyen
 */
public class PreSubject extends AdvancedAbstractJdbcModel<PreSubID> {

    private String preSubjectName;
    private String subjectName;
    public PreSubject() {
    }
    public String getPreSubjectName(){
        return this.preSubjectName;
    }
    public void setPreSubjectName(String _preSubjectName){
        this.preSubjectName=_preSubjectName;
    }
     public String getSubjectName(){
        return this.subjectName;
    }
    public void setSubjectName(String _subjectName){
        this.subjectName=_subjectName;
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
