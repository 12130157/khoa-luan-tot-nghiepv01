package uit.cnpm02.dkhp.service;

import java.util.List;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.model.TrainClassID;
import uit.cnpm02.dkhp.utilities.ExecuteResult;

/**
 *
 * @author LocNguyen
 */
public interface ITrainClassService {
    
    List<TrainClass> getAllClassOpenByYearAndSemester(String year, int semester);
    
    List<TrainClass> getAllClassOpen();
    
    List<TrainClass> getAllClassClose();
    
    List<TrainClass> getAllClassCancel();
   
    List<TrainClass> getTrainClass(String year, int semester);
    
    List<TrainClass> getTrainClass();
    
    ExecuteResult checkOpenClassCondition(TrainClass obj);
    
    ExecuteResult addNewTrainClass(TrainClass obj);
    
    TrainClass getClassInfomation(TrainClassID obj);
        
    ExecuteResult updateTrainClass(TrainClass obj);
    
    boolean isExisted(TrainClass obj);
    
    boolean validate(TrainClass obj);

    /**
     * Return all train class of specified lecturer.
     * @param lecturer
     * @return 
     */
    public List<TrainClass> getCurrentTrainClass(String lecturer);
    
    
}
