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
    
    //
    // Student report
    //
    List<Student> searchStudent(String key);
    List<TrainClass> getTrainClassRegistered(String mssv);
    List<TrainClass> getTrainClassRegistered(String mssv, String year, int semeter);
    
}