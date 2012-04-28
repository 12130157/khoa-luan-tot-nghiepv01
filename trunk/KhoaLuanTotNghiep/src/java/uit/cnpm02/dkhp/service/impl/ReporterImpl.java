package uit.cnpm02.dkhp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.RegistrationDAO;
import uit.cnpm02.dkhp.DAO.StudentDAO;
import uit.cnpm02.dkhp.DAO.TrainClassDAO;
import uit.cnpm02.dkhp.model.Registration;
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
    private RegistrationDAO regDao = DAOFactory.getRegistrationDAO();
    private TrainClassDAO classDao = DAOFactory.getTrainClassDAO();
    
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
                    
            List<Student> student_1 = studentDao.findByColumName("MSSV", key);
            
            if ((student_1 != null) && !student_1.isEmpty()) {
                for (Student s : student_1) {
                    if (!students.contains(s)) {
                        students.add(s);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(
                    ReporterImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return students;
    }

    @Override
    public List<TrainClass> getTrainClassRegistered(String mssv) {
        List<TrainClass> results = new ArrayList<TrainClass>(10);

        try {
            List<Registration> regs = regDao.findByColumName("MSSV", mssv);
            if ((regs == null) || regs.isEmpty()) {
                return null;
            }

            for (Registration r : regs) {
                List<TrainClass> clazzTemp = classDao.findByColumName(
                                        "MaLopHoc", r.getId().getClassCode());
                if ((clazzTemp != null) && !clazzTemp.isEmpty()) {
                    results.addAll(clazzTemp);
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(ReporterImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return results;
        //TODO: tobe tested.
    }

    @Override
    public List<TrainClass> getTrainClassRegistered(
                                            String mssv,
                                            String year,
                                            int semeter) {
        // TODO: implementation
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<TrainClass> getTrainClass(String year, int semeter,
                                                TrainClassStatus status) {
        return null;
    }
    
}
