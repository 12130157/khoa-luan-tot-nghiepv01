package uit.cnpm02.dkhp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.StudentDAO;
import uit.cnpm02.dkhp.model.Student;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.service.IReporter;
import uit.cnpm02.dkhp.service.TrainClassStatus;

/**
 *
 * @author LocNguyen
 */
public class ReporterImpl implements IReporter {
    private StudentDAO studentDao = DAOFactory.getStudentDao();
    
    private static Object mutex = new Object();
    
    public ReporterImpl() {
        super();
    }

    @Override
    public List<TrainClass> getTrainClass(TrainClassStatus status) {
        // TODO: implementation
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<TrainClass> getOpened(String year) {
        // TODO: implementation
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<TrainClass> getOpened(String year, int semeter) {
        // TODO: implementation
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Student> searchStudent(String key) {
        List<Student> students = new ArrayList<Student>(10);
        try {
            students = studentDao.findByColumName("HoTen", key);
            Student student = studentDao.findById(key);
            
            if ((student != null) && !students.contains(student)) {
                students.add(student);
            }
        } catch (Exception ex) {
            Logger.getLogger(ReporterImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return students;
    }

    @Override
    public List<TrainClass> getTrainClassRegistered(String mssv) {
        // TODO: implementation
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<TrainClass> getTrainClassRegistered(String mssv, String year, int semeter) {
        // TODO: implementation
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
