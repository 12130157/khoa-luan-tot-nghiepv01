package uit.cnpm02.dkhp.service;

import java.util.List;
import uit.cnpm02.dkhp.model.Student;
import uit.cnpm02.dkhp.model.TrainClass;

/**
 *
 * @author LocNguyen
 */
public interface IReporter {
    
    //
    // TrainClass report
    //
    List<TrainClass> getTrainClass(TrainClassStatus status);
    List<TrainClass> getOpened(String year);
    List<TrainClass> getOpened(String year, int semeter);
    List<TrainClass> getTrainClass(String year, int semeter,
            TrainClassStatus status);
    /**
     * Retrieve TrainClass at current year and semeter.
     * @param classId class ID
     * @return 
     */
    TrainClass getTrainClass(String classId);
    /**
     * 
     * @param ti
     * @param type default ASC
     * @return 
     */
    List<TrainClass> sort(String sessionId, String by, String type);
    
    //
    // Student report
    //
    List<Student> searchStudent(String key);
    List<TrainClass> getTrainClassRegistered(String sessionId, String mssv, boolean fullInfor);
    List<TrainClass> getTrainClassRegistered(String mssv, String year, int semeter);
    
}
