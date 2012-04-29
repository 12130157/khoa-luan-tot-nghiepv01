package uit.cnpm02.dkhp.service.impl;

import java.util.logging.Level;
import java.util.logging.Logger;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.StudentDAO;
import uit.cnpm02.dkhp.model.Student;
import uit.cnpm02.dkhp.service.IStudentService;

/**
 *
 * @author LocNguyen
 */
public class StudentServiceImpl implements IStudentService {
    private StudentDAO studentDao;
    
    private Object mutex = new Object();
    
    /**
     * Default constructor
     */
    public StudentServiceImpl() {
        studentDao = DAOFactory.getStudentDao();
    }

    @Override
    public Student getStudent(String mssv) {
        try {
            return studentDao.findById(mssv);
        } catch (Exception ex) {
            Logger.getLogger(StudentServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
