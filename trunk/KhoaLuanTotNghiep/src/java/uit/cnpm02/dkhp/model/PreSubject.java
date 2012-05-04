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

    public PreSubject(String preSubjectName, String subjectName) {
        this.preSubjectName = preSubjectName;
        this.subjectName = subjectName;
    }

    public String getPreSubjectName() {
        return preSubjectName;
    }

    public void setPreSubjectName(String preSubjectName) {
        this.preSubjectName = preSubjectName;
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PreSubject)) {
            return false;
        }
        if ((this == null) && (obj == null)) {
            return true;
        }
        
        PreSubject other = (PreSubject) obj;
        return this.getId().getSudId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (this.preSubjectName != null ? 
                                            this.preSubjectName.hashCode() : 0);
        hash = 79 * hash + (this.subjectName != null ? 
                                                this.subjectName.hashCode() : 0);
        return hash;
    }

   
}
