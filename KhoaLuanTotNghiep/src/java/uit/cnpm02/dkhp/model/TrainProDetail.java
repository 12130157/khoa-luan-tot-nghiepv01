package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.advancedJDBC.AdvancedAbstractJdbcModel;
import uit.cnpm02.dkhp.access.mapper.MapperConstant;

/**
 *
 * @author LocNguyen
 */
public class TrainProDetail extends AdvancedAbstractJdbcModel<TrainProDetailID> {
    /**Training Program code**/
    private String programCode;
    
    /**Subject code**/
    private String subjectCode;
    private String subjectName;

    private int semester;

    public TrainProDetail() {
    }

    public TrainProDetail(String programCode, String subjectCode, int semester) {
        setId(new TrainProDetailID(programCode, subjectCode));
        this.programCode = programCode;
        this.subjectCode = subjectCode;
        this.semester = semester;
    }
    
    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    /**
     * Get training program code
     * @return ma CTDT
     */
    public String getProgramCode() {
        return programCode;
    }

    /**
     * Set training program code
     * @param programCode 
     */
    public void setProgramCode(String programCode) {
        this.programCode = programCode;
    }

    /**
     * Get subject code
     * @return subjectCode
     */
    public String getSubjectCode() {
        return subjectCode;
    }

    /**
     * Set subject code
     * @param subjectCode 
     */
    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    @Override
    public String[] getColumnNames() {
        return new String[] {"HocKy"};
    }

    @Override
    public Object[] getColumnValues() {
        return new Object[] {semester};
    }

    @Override
    public void setColumnValues(Object[] values) {
        try {
            this.semester = Integer.parseInt(values[0].toString());
        } catch(Exception ex) {
            //
        }
    }
    
    @Override
    public String getTableName() {
        return MapperConstant.DB_NAME
                + ".chitietctdt";
    }

    @Override
    public String[] getIdColumnName() {
        return new String[]{
                   "MaCTDT",
                   "MaMH",
                };
    }

    @Override
    public Object[] getIdColumnValues() {
        return getId().getIDValues();
    }

    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof TrainProDetail)) {
            return false;
        }
        TrainProDetailID a;
        TrainProDetail other = (TrainProDetail) obj;
        
        return this.getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.programCode != null ? this.programCode.hashCode() : 0);
        hash = 59 * hash + (this.subjectCode != null ? this.subjectCode.hashCode() : 0);
        hash = 59 * hash + (this.subjectName != null ? this.subjectName.hashCode() : 0);
        hash = 59 * hash + this.semester;
        return hash;
    }
}
