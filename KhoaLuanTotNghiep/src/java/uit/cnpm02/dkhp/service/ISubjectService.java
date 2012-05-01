package uit.cnpm02.dkhp.service;

import java.util.List;
import uit.cnpm02.dkhp.model.Subject;

/**
 *
 * @author LocNguyen
 */
public interface ISubjectService {
    Subject addSubject(Subject sub);
    Subject updateSubject(Subject sub);
    boolean deleteSubject(Subject sub);
    
    List<Subject> sort(String sessionId, final String by, final String type);
    List<Subject> findByFaculty(String facultyCode);
    List<Subject> findSubjects(String sessionId, int currentPage);
    
    int getNumberPage();

    public List<Subject> search(String sessionId, String key);
    
}
