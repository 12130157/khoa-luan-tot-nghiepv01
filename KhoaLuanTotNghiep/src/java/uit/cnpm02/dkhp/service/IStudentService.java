package uit.cnpm02.dkhp.service;

import java.util.List;
import uit.cnpm02.dkhp.model.Student;
import uit.cnpm02.dkhp.utilities.ExecuteResult;

/**
 *
 * @author LocNguyen
 */
public interface IStudentService {

    Student getStudent(String mssv);
    
    List<Student> getStudents(int page, String session);
    
    List<Student> getStudents(String session);
    
    /**
     * Retrieve all student registered at a class
     * @param classID
     * @param year
     * @param semeter
     * @return 
     */
    List<Student> getStudent(String classID, String year, int semeter);
    
    
    ExecuteResult addStudent(Student s);
    /**
     * Add a list of student
     * 
     * @param addIfPossible if true: any student is correct are added althought
     * there are some other not.
     * @return add information
     *  if successful: result will include list of students added
     *  if fail: result will include all student could not be added
     */
    ExecuteResult addStudents(List<Student> students, boolean addIfPossible, String sessionId);

    ExecuteResult validateNewStudent(Student s);

    ExecuteResult deleteStudent(String mssv, boolean deleteAnyway, String sessionId);

    List<Student> search(String key, String session);

    public List<Student> sort(String sessionId, final String by);
}
