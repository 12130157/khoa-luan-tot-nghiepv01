/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.advancedJDBC.IID;

/**
 *
 * @author thanh
 */
public class DetailTrainID implements IID{
    private String lecturerCode;
    private String SubjectCode;

    public String getSubjectCode() {
        return SubjectCode;
    }

    public void setSubjectCode(String SubjectCode) {
        this.SubjectCode = SubjectCode;
    }

    public String getLecturerCode() {
        return lecturerCode;
    }

    public void setLecturerCode(String lecturerCode) {
        this.lecturerCode = lecturerCode;
    }
    public DetailTrainID() {
            super();
        }
    public DetailTrainID(String _lecturerCode, String _subjectCode) {
            this.lecturerCode=_lecturerCode;
            this.SubjectCode=_subjectCode;
        }
    @Override
    public String[] getIDNames() {
        return new String[] {
          "MaGV",
          "MaMH"
        };
    }

    @Override
    public Object[] getIDValues() {
        return new Object[] {
            lecturerCode,
            SubjectCode
        };
    }

    @Override
    public void setIDValues(Object[] idValues) {
        lecturerCode = idValues[0].toString();
        SubjectCode = idValues[1].toString();
    }
    
}
