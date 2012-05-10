package uit.cnpm02.dkhp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.RegistrationDAO;
import uit.cnpm02.dkhp.DAO.StudentDAO;
import uit.cnpm02.dkhp.model.Registration;
import uit.cnpm02.dkhp.model.Student;
import uit.cnpm02.dkhp.service.IStudentService;
import uit.cnpm02.dkhp.utilities.ExecuteResult;

/**
 *
 * @author LocNguyen
 */
public class StudentServiceImpl implements IStudentService {
    private StudentDAO studentDao;
    private RegistrationDAO regDao;
    
    private Object mutex = new Object();
    
    /**
     * Default constructor
     */
    public StudentServiceImpl() {
        studentDao = DAOFactory.getStudentDao();
        regDao = DAOFactory.getRegistrationDAO();
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

    @Override
    public List<Student> getStudent(String classID, String year, int semeter) {
        List<Student> students = new ArrayList<Student>(10);
        try {
            //
            // Retrieve Registration data
            //
            List<String> columnName = new ArrayList<String>(10);
            List<Object> values = new ArrayList<Object>(10);
            
            columnName.add("MaLopHoc");
            values.add(classID);
            if (semeter > 0) {
                columnName.add("HocKy");
                values.add(semeter);
            }
             if ((year != null)
                    && !year.isEmpty()
                    && !year.equalsIgnoreCase("all")) {
                columnName.add("NamHoc");
                values.add(year);
            }
            String[] strColumnNames = (String[])columnName.toArray(
                                                new String[columnName.size()]);
            List<Registration> regs = new ArrayList<Registration>(10);
            if ((strColumnNames == null) || (strColumnNames.length <= 0)) {
                regs = regDao.findAll();
            } else {
                regs = regDao.findByColumNames(
                                        strColumnNames, values.toArray());
            }
            
            //
            // Retrieve Student list
            //
            if ((regs == null) || regs.isEmpty()) {
                return null;
            }
            
            List<String> studentId = new ArrayList<String>(10);
            for (Registration r : regs) {
                studentId.add(r.getId().getStudentCode());
            }
            students = studentDao.findByIds(studentId);
        } catch (Exception ex) {
            Logger.getLogger(
                    ReporterImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return students;
    }

    @Override
    public ExecuteResult addStudent(Student s) {
        ExecuteResult er = new ExecuteResult(true, "");
        try {
            studentDao.add(s);
        } catch (Exception ex) {
            Logger.getLogger(StudentServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            er.setIsSucces(false);
            er.setMessage("Thêm SV không thành công: " + ex.toString());
        }
        
        return er;
    }
    
}
