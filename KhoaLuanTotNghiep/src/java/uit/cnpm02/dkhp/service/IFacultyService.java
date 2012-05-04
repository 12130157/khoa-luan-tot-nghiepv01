package uit.cnpm02.dkhp.service;

import java.util.List;
import uit.cnpm02.dkhp.model.Faculty;
import uit.cnpm02.dkhp.model.Subject;

/**
 *
 * @author LocNguyen
 */
public interface IFacultyService {
    List<Faculty> getAllFaculty();
    
    /**
     * Retrieve all subject of a faculty
     * and ofcoure the general subject.
     * @param faculty
     * @return 
     */
    List<Subject> getSubject(String faculty);
}
