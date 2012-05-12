package uit.cnpm02.dkhp.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    /**
     * Backup for import from file case (include
     * students have correct information)
     * 
     * SessionID - List<Student>
     **/
    private Map<String, List<Student>> importStudents
            = new HashMap<String, List<Student>>();
    
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
            er.setData(s);
        } catch (Exception ex) {
            Logger.getLogger(StudentServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            er.setIsSucces(false);
            er.setMessage("[StudentService][Add-One] - " + ex.toString());
        }
        
        return er;
    }

    @Override
    public ExecuteResult validateNewStudent(Student s) {
        ExecuteResult er = new ExecuteResult(true, "");
        try {
            // Check mssv existed.
            if (studentDao.findById(s.getId()) != null) {
                er.setIsSucces(false);
                er.setMessage("Mã số SV bị trùng");
            }
        } catch (Exception ex) {
            Logger.getLogger(StudentServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            er.setIsSucces(false);
            er.setMessage("Đã có lỗi xảy ra: " + ex.toString());
        }
        
        return er;
    }

    @Override
    public ExecuteResult deleteStudent(String mssv) {
        ExecuteResult er = new ExecuteResult(true, "Xóa thành công.");
        try {
            Student s = studentDao.findById(mssv);
            if (s == null) {
                er.setIsSucces(false);
                er.setMessage("Không tìm thấy Sinh viên");
            } else {
                studentDao.delete(s);
                er.setData(s);
            }
        } catch (Exception ex) {
            Logger.getLogger(StudentServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            er.setIsSucces(false);
            er.setMessage(ex.toString());
        }
        
        return er;
    }

    @Override
    public ExecuteResult addStudents(List<Student> students
            , boolean addIfPossible, String sessionId) {
        try {
            if (addIfPossible) {
                List<Student> s_temps = importStudents.get(sessionId);
                if ((s_temps == null) || s_temps.isEmpty()) {
                    return new ExecuteResult(false, "Dữ liệu không tồn tại, "
                            + "có thể do phiên làm việc hết hiệu lực, vui lòng"
                            + "thử lại bằng cách submit lại file.");
                } else {
                    studentDao.addAll(s_temps);
                    importStudents.remove(sessionId);
                    ExecuteResult e = new ExecuteResult(true, "");
                    e.setData(s_temps);
                    
                    return e;
                }
            }
            
            List<Student> existedStudents = new ArrayList<Student>(10);
            for (Student s : students) {
                if ((studentDao.findById(s.getId()) != null)
                        || studentDao.findByIdentifier(s.getIdentityNumber()) != null) {
                    existedStudents.add(s);
                }
            }

            ExecuteResult result = new ExecuteResult(true, "");
            if (existedStudents.size() > 0) {
                students.removeAll(existedStudents);
                // Incase these are some information incorrect
                // please send it back to user
                result.setIsSucces(false);
                result.setMessage("Một số SV có MSSV hoặc số CMND bị trùng");
                result.setData(existedStudents);

                importStudents.put(sessionId, students);

            } else {
                studentDao.addAll(students);
                result.setData(students);
            }
            return result;
        } catch (Exception ex) {
            Logger.getLogger(StudentServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return new ExecuteResult(
                    false, "[Error][StudentService]: " + ex.toString());
        }
    }
}
