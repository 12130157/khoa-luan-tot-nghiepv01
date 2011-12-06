/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.advancedJDBC.AdvancedAbstractJdbcModel;
import uit.cnpm02.dkhp.access.mapper.MapperConstant;

/**
 *  study result of student
 * @author LocNguyen
 */
public class StudyResult extends AdvancedAbstractJdbcModel<StudyResultID>{
    
    private String year;
    private int semester;
    private float mark;
    private String SubjectName;
    public StudyResult() {
    }

    public StudyResult(String studentCode, String subjectCode, String year, int semester, float mark) {
        StudyResultID studyID=new StudyResultID(studentCode, subjectCode);
        this.setId(studyID);
        this.year = year;
        this.semester = semester;
        this.mark = mark;
        this.SubjectName="";
    }

    public float getMark() {
        return mark;
    }

    public void setMark(float mark) {
        this.mark = mark;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getSubjectName() {
        return SubjectName;
    }

    public void setSubjectName(String subjectName) {
        this.SubjectName = subjectName;
    }
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

     @Override
    public String[] getColumnNames() {
        return new String[]{
                    "Diem",
                    "HocKy",
                    "NamHoc"
              };
    }

    @Override
    public Object[] getColumnValues() {
         return new Object[]{
                    mark,
                    semester,
                    year
                };
    }

    @Override
    public void setColumnValues(Object[] values) {
        try {
            mark = Float.parseFloat(values[0].toString());
            semester =Integer.parseInt(values[1].toString());
            year =values[2].toString();
            

        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String getTableName() {
        return MapperConstant.DB_NAME
                + ".ketquahoctap";
    }

    @Override
    public String[] getIdColumnName() {
        return new String[]{
                    "MSSV",
                    "MaMH"
                };
    }

    @Override
    public Object[] getIdColumnValues() {
        return getId().getIDValues();
    }
}
