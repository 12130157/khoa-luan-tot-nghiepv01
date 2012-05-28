package uit.cnpm02.dkhp.service;

import java.util.List;
import uit.cnpm02.dkhp.model.Subject;
import uit.cnpm02.dkhp.utilities.ExecuteResult;

/**
 *
 * @author LocNguyen
 */
public interface ISubjectService {
    List<Subject> getAll() throws Exception;
    Subject addSubject(Subject sub) throws Exception;
    ExecuteResult updateSubject(Subject sub);
    boolean deleteSubject(String sessionId, String subId) throws Exception;
    boolean deleteSubjectByID(String subId) throws Exception;
    List<Subject> sort(String sessionId, final String by, final String type);
    List<Subject> findByFaculty(String facultyCode);
    List<Subject> findSubjects(String sessionId, int currentPage);
    
    int getNumberPage();

    public List<Subject> search(String sessionId, String key);
    Subject findById(String id);
    public List<Subject> getCurrentSubjects(String sessionId);

    public List<Subject> findAll(int recordPerPage, int currentPage, String orderBy, String order);
    public List<Subject> findAll(int recordPerPage, int currentPage , String whereColumn, String whereValue, String orderBy, String order);
}
