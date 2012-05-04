package uit.cnpm02.dkhp.service;

import java.util.List;
import uit.cnpm02.dkhp.model.Subject;

/**
 *
 * @author LocNguyen
 */
public interface ISubjectService {
    Subject addSubject(Subject sub) throws Exception;
    Subject updateSubject(Subject sub);
    boolean deleteSubject(String sessionId, String subId) throws Exception;
    
    List<Subject> sort(String sessionId, final String by, final String type);
    List<Subject> findByFaculty(String facultyCode);
    List<Subject> findSubjects(String sessionId, int currentPage);
    
    int getNumberPage();

    public List<Subject> search(String sessionId, String key);
    public List<Subject> getCurrentSubjects(String sessionId);
}
