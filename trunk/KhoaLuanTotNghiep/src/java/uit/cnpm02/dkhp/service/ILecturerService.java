package uit.cnpm02.dkhp.service;

import java.util.List;
import uit.cnpm02.dkhp.model.Lecturer;
import uit.cnpm02.dkhp.utilities.ExecuteResult;

/**
 *
 * @author LocNguyen
 */
public interface ILecturerService {
    Lecturer getLecturer(String id);
    
    List<Lecturer> getLecturers(int page, String session);
    
    List<Lecturer> getLecturers(String session);
    
    List<Lecturer> getLecturer(String classID, String year, int semeter);
    
    
    ExecuteResult addLecturer(Lecturer l);
    
    ExecuteResult addLecturers(List<Lecturer> lecturers, boolean addIfPossible, String sessionId);

    ExecuteResult validateNewLecturer(Lecturer l);

    ExecuteResult deleteLecturer(String lecturerId, boolean deleteAnyway, String sessionId);
    
    public ExecuteResult deleteLecturers(List<String> lecturerIds,
            boolean deleteAnyway, String sessionId);

    List<Lecturer> search(String key, String session);

    public List<Lecturer> sort(String sessionId, final String by);

    public ExecuteResult updateLecturer(String sessionId, Lecturer l);
    
    int getNumberPage();
}
