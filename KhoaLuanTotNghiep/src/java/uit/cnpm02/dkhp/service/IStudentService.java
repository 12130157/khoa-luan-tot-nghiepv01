package uit.cnpm02.dkhp.service;

import java.util.List;
import uit.cnpm02.dkhp.model.Student;

/**
 *
 * @author LocNguyen
 */
public interface IStudentService {

    Student getStudent(String mssv);
    
    /**
     * Retrieve all student registered at a class
     * @param classID
     * @param year
     * @param semeter
     * @return 
     */
    List<Student> getStudent(String classID, String year, int semeter);
}
