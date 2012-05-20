package uit.cnpm02.dkhp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uit.cnpm02.dkhp.DAO.ClassDAO;
import uit.cnpm02.dkhp.DAO.CourseDAO;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.FacultyDAO;
import uit.cnpm02.dkhp.model.Class;
import uit.cnpm02.dkhp.model.Course;
import uit.cnpm02.dkhp.model.Faculty;
import uit.cnpm02.dkhp.service.IPDTService;

/**
 *
 * @author LocNguyen
 */
public class PDTServiceImpl implements IPDTService {
    private ClassDAO classDao;
    private CourseDAO courseDao;
    private FacultyDAO facultyDao;
    
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
    
}
