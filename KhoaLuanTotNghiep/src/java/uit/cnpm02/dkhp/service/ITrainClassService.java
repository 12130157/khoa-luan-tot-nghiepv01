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
    
    List<TrainClass> getTrainClass(int currentPage);
    
    List<TrainClass> getTrainClass();
    
    ExecuteResult addNewTrainClass(TrainClass obj);
    
    TrainClass getClassInfomation(TrainClassID obj);
    
    ExecuteResult updateTrainClass(TrainClass obj);
    
    boolean isExisted(TrainClass obj);
    
    boolean validate(TrainClass obj);
}