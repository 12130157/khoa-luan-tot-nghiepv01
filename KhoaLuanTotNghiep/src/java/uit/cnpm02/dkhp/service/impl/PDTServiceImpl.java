package uit.cnpm02.dkhp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uit.cnpm02.dkhp.DAO.ClassDAO;
import uit.cnpm02.dkhp.DAO.CourseDAO;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.FacultyDAO;
import uit.cnpm02.dkhp.DAO.TaskDAO;
import uit.cnpm02.dkhp.model.Class;
import uit.cnpm02.dkhp.model.Course;
import uit.cnpm02.dkhp.model.Faculty;
import uit.cnpm02.dkhp.model.News;
import uit.cnpm02.dkhp.model.Task;
import uit.cnpm02.dkhp.model.type.NewsType;
import uit.cnpm02.dkhp.model.type.TaskStatus;
import uit.cnpm02.dkhp.model.type.TaskType;
import uit.cnpm02.dkhp.service.IPDTService;
import uit.cnpm02.dkhp.utilities.ExecuteResult;
/**
 *
 * @author LocNguyen
 */
public class PDTServiceImpl implements IPDTService {
    private ClassDAO classDao;
    private CourseDAO courseDao;
    private FacultyDAO facultyDao;
    private TaskDAO taskDao;
    
    private List<Class> clazzes = new ArrayList<Class>(50);
    private List<Course> courses = new ArrayList<Course>(50);
    private List<Faculty> faculties = new ArrayList<Faculty>(10);
    
    /**
     * Default constructor
     */
    public PDTServiceImpl() {
        classDao = DAOFactory.getClassDao();
        courseDao = DAOFactory.getCourseDao();
        facultyDao = DAOFactory.getFacultyDao();
        taskDao = DAOFactory.getTaskDAO();
    }

    @Override
    public List<Class> getAllClass() {
        if ((clazzes.isEmpty())) {
            try {
                clazzes = classDao.findAll();
            } catch (Exception ex) {
                Logger.getLogger(PDTServiceImpl.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
        return clazzes;
    }

    @Override
    public List<Course> getAllCourse() {
        if ((courses.isEmpty())) {
            try {
                courses = courseDao.findAll();
            } catch (Exception ex) {
                Logger.getLogger(PDTServiceImpl.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
        return courses;
    }

    @Override
    public List<Faculty> getAllFaculty() {
        if ((faculties.isEmpty())) {
            try {
                faculties = facultyDao.findAll();
            } catch (Exception ex) {
                Logger.getLogger(PDTServiceImpl.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
        return faculties;
    }

    @Override
    public List<Class> getClassByFaculty(String facultyId) {
        try {
            return classDao.findByColumName("MaKhoa", facultyId);
        } catch (Exception ex) {
            Logger.getLogger(PDTServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void uploadScoreSheet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public List<Task> getTasks(String username) {
        try {
            return DAOFactory.getTaskDAO().findByColumName("NguoiNhan", username);
        } catch (Exception ex) {
            Logger.getLogger(LecturerServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public ExecuteResult sendTask(Task task) {
        try {
            DAOFactory.getTaskDAO().add(task);
            return new ExecuteResult(true, "");
        } catch (Exception ex) {
            Logger.getLogger(PDTServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            return new ExecuteResult(false, "Không thể cập nhật yêu cầu xuống"
                    + " database. Vui lòng thử lại sau");
        }
    }

    @Override
    public ExecuteResult processTask(int taskId) {
        try {
            Task task = taskDao.findById(taskId);
            if (task == null) {
                return new ExecuteResult(false, "Yêu cầu không tồn tại");
            }
            
            if (task.getTaskType() == TaskType.GV_TB_NGHI_DAY) {
                //
                // Create a news
                //
                News n = new News(-1, "Thông báo nghỉ học",task.getContent(),
                         "Phòng Đào Tạo",task.getCreated().toString(),
                        NewsType.IMPORTANT.value());
                DAOFactory.getNewsDao().add(n);
                
                task.setStatus(TaskStatus.PROCESSED);
                taskDao.update(task);
              /*Task taskToStudent = new Task(task);
              taskToStudent.setSender("admin");
              taskToStudent.setReciever("student");
              taskDao.add(taskToStudent);
              
              task.setReciever(task.getSender());
              task.setSender("admin");
              task.setContent("\"" + task.getContent() + "\" - duoc chap nhan.");
              //task.setStatus(TaskStatus.PROCESSED);
              task.setTaskType(TaskType.UNKNOWN);
              taskDao.update(task);*/
            } // else other case...
            
            return new ExecuteResult(true, "Xử lý thành công");
        } catch (Exception ex) {
            Logger.getLogger(PDTServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            return new ExecuteResult(false, "error - Lỗi server: " + ex.toString());
        }
    }

    @Override
    public ExecuteResult rejectTask(int taskId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ExecuteResult hideTask(int taskId) {
        try {
            Task task = taskDao.findById(taskId);
            if (task == null) {
                return new ExecuteResult(false, "Yêu cầu không tồn tại");
            }

            task.setStatus(TaskStatus.PROCESSED);
            //task.setTaskType(TaskType.UNKNOWN);
            taskDao.update(task);

            return new ExecuteResult(true, "Xử lý thành công");
        } catch (Exception ex) {
            Logger.getLogger(PDTServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return new ExecuteResult(false, "error - Lỗi server: " + ex.toString());
        }
    }
    
}
