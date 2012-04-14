package uit.cnpm02.dkhp.service;

import java.util.List;
import uit.cnpm02.dkhp.model.TrainClass;

/**
 *
 * @author LocNguyen
 */
public interface ITrainClassService {
    
    List<TrainClass> getTrainClass(int currentPage);
    
    List<TrainClass> getTrainClass();
    
    TrainClass addNewTrainClass(TrainClass obj);
    
    boolean updateTrainClass(TrainClass obj);
    
    boolean isExisted(TrainClass obj);
    
    boolean validate(TrainClass obj);
}
