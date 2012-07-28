package uit.cnpm02.dkhp.model;

/**
 *
 * @author thanh
 */
public class ReportBySemester {
    private int numOfClassOpen;
    private int numOfReg;
    private TrainClass maxRegClass;
    private TrainClass minRegClass;
    private int numOfPassReg;
    private int numOfNotPassReg;
    public ReportBySemester(){
        
    }
    public ReportBySemester(int _numOfClassOpen, int _numOfReg, TrainClass _maxRegClass,
            TrainClass _minRegClass, int _numOfPassReg, int _numOfNotPassReg){
        this.numOfClassOpen = _numOfClassOpen;        
        this.numOfReg = _numOfReg;
        this.maxRegClass = _maxRegClass;
        this.minRegClass = _minRegClass;
        this.numOfPassReg = _numOfPassReg;
        this.numOfNotPassReg = _numOfNotPassReg;
        
    }

    public TrainClass getMaxRegClass() {
        return maxRegClass;
    }

    public void setMaxRegClass(TrainClass _maxRegClass) {
        this.maxRegClass = _maxRegClass;
    }

    public TrainClass getMinRegClass() {
        return minRegClass;
    }

    public void setMinRegClass(TrainClass _minRegClass) {
        this.minRegClass = _minRegClass;
    }

    public int getNumOfClassOpen() {
        return numOfClassOpen;
    }

    public void setNumOfClassOpen(int _numOfClassOpen) {
        this.numOfClassOpen = _numOfClassOpen;
    }

    public int getNumOfNotPassReg() {
        return numOfNotPassReg;
    }

    public void setNumOfNotPassReg(int _numOfNotPassReg) {
        this.numOfNotPassReg = _numOfNotPassReg;
    }

    public int getNumOfPassReg() {
        return numOfPassReg;
    }

    public void setNumOfPassReg(int _numOfPassReg) {
        this.numOfPassReg = _numOfPassReg;
    }

    public int getNumOfReg() {
        return numOfReg;
    }

    public void setNumOfReg(int _numOfReg) {
        this.numOfReg = _numOfReg;
    }
    
}
