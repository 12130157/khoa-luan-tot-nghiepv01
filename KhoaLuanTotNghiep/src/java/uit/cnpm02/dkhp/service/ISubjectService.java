package uit.cnpm02.dkhp.service;

import java.util.List;
import uit.cnpm02.dkhp.model.Subject;
import uit.cnpm02.dkhp.utilities.ExecuteResult;

/**
 *
 * @author LocNguyen
 */
public interface ISubjectService {
    List<Subject> getAll(String facultyId) throws Exception;
    Subject addSubject(Subject sub) throws Exception;
    ExecuteResult updateSubject(Subject sub);
    boolean deleteSubject(String sessionId, String subId) throws Exception;
    
    List<Subject> sort(String sessionId, final String by, final String type);
    List<Subject> findByFaculty(String facultyCode);
    List<Subject> findSubjects(String sessionId, int currentPage);
    
    int getNumberPage();

    public List<Subject> search(String sessionId, String key);
    Subject findById(String id);
    public List<Subject> getCurrentSubjects(String sessionId);
}
