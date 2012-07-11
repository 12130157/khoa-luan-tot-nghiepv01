/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.advancedJDBC.IID;

/**
 *
 * @author LocNguyen
 */
public class TrainProDetailID implements IID{
    private String trainProgID;
    private String subjectID;
    
    public TrainProDetailID() {
        
    }

    public TrainProDetailID(String trainProgID, String subjectID) {
        this.trainProgID = trainProgID;
        this.subjectID = subjectID;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getTrainProgID() {
        return trainProgID;
    }

    public void setTrainProgID(String trainProgID) {
        this.trainProgID = trainProgID;
    }
    
    @Override
    public String[] getIDNames() {
        return new String[] {
            "MaCTDT",
            "MaMH"
        };
    }

    @Override
    public Object[] getIDValues() {
        return new Object[] {
            trainProgID,
            subjectID
        };
    }

    @Override
    public void setIDValues(Object[] idValues) {
        this.trainProgID = idValues[0].toString();
        this.subjectID = idValues[1].toString();
    }
    
}
