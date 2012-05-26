package uit.cnpm02.dkhp.service;

import java.util.List;
import uit.cnpm02.dkhp.model.Course;
import uit.cnpm02.dkhp.model.Faculty;
import uit.cnpm02.dkhp.model.Task;
import uit.cnpm02.dkhp.utilities.ExecuteResult;

/**
 *
 * @author LocNguyen
 */
public interface IPDTService {
    /**
     * List all class existed.
     * @return 
     */
    List<uit.cnpm02.dkhp.model.Class> getAllClass();
    List<uit.cnpm02.dkhp.model.Class> getClassByFaculty(String facultyId);
    
    /**
     * List all course
     * @return 
     */
    List<Course> getAllCourse();
    
    /**
     * List all faculty.
     * @return 
     */
    List<Faculty> getAllFaculty();
    
    /**
     * This for lecturer using to upload final score
     * (exel file.)
     */
    void uploadScoreSheet();
    
    public List<Task> getTasks(String username);

    public ExecuteResult sendTask(Task task);
    
    public ExecuteResult processTask(int taskId);
    
    public ExecuteResult rejectTask(int taskId);

    public ExecuteResult hideTask(int taskId);
}
