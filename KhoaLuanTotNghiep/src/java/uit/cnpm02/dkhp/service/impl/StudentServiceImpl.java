package uit.cnpm02.dkhp.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import uit.cnpm02.dkhp.utilities.Constants;
import uit.cnpm02.dkhp.utilities.ExecuteResult;
import uit.cnpm02.dkhp.utilities.StringUtils;

/**
 *
 * @author LocNguyen
 */
public class StudentServiceImpl implements IStudentService {
    private int rowPerPage = Constants.ELEMENT_PER_PAGE_DEFAULT;
    
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
    
    private Map<String, List<Student>> currentStudents
            = new HashMap<String, List<Student>>();
    /**SORT TYPE**/
    private Map<String, String> sortTypes
            = new HashMap<String, String>();
    
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
            Logger.getLogger(StudentServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            return new ExecuteResult(
                    false, "[Error][StudentService]: " + ex.toString());
        }
    }

    @Override
    public List<Student> search(String key, String session) {
        
        List<Student> results = new ArrayList<Student>(10);
        List<Student> results_temp = new ArrayList<Student>(10);
        try {
            // Search by FullName
            results_temp = studentDao.findByColumName("HoTen", key);
            addList(results, results_temp);
            
            // Search by MSSV
            results_temp = studentDao.findByColumName("MSSV", key);
            addList(results, results_temp);
            
            if ((results != null) && !results.isEmpty()) {
                try {
                    currentStudents.remove(session);
                } catch (Exception ex) {
                    // new session, maybe...
                }
                currentStudents.put(session, results);
            }
        } catch (Exception ex) {
            Logger.getLogger(StudentServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return results;
    }
    
    private void addList(List<Student> parent, List<Student> child) {
        if ((child == null) || child.isEmpty()) {
            return;
        }
        
        if (parent == null) { // first time
            parent = new ArrayList<Student>(10);
            parent.addAll(child);
            return;
        }
        
        for (Student s : child) {
            if (!parent.contains(s)) {
                parent.add(s);
            }
        }
    }

    @Override
    public List<Student> getStudents(int page, String session) {
        List<Student> results = null;
        try {
            results = studentDao.findAll(rowPerPage, page, "HoTen", null);
            if ((results != null) && !results.isEmpty()) {
                try {
                    currentStudents.remove(session);
                } catch (Exception ex) {
                    // new session, maybe...
                }
                currentStudents.put(session, results);
            }
        } catch (Exception ex) {
            Logger.getLogger(StudentServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return results;
    }

    @Override
    public List<Student> sort(String sessionId, final String by) {
        String sortType = "ASC";
        try {
            sortType = sortTypes.get(sessionId);
            sortTypes.remove(sessionId);
        } catch (Exception ex) {
            sortType = "ASC";
        }
        if (StringUtils.isEmpty(sortType)) {
            sortType = "ASC";
        }
        
        if (sortType.equalsIgnoreCase("ASC")) {
            sortTypes.put(sessionId, "DES");
        } else {
            sortTypes.put(sessionId, "ASC");
        }

        final String sortType_final = sortType;
        List<Student> students = currentStudents.get(sessionId);
        if ((students == null) || students.isEmpty()) {
            students = getStudents(1, sessionId);
        }
        
        if ((students != null) && (!students.isEmpty())) {
            Collections.sort(students, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                if (sortType_final.equals("ASC"))
                    return o1.compare(o2, by);
                else
                    return o2.compare(o1, by);
            }
        });
        }
        
        return students;
    }

    @Override
    public List<Student> getStudents(String session) {
        return currentStudents.get(session);
    }

    @Override
    public ExecuteResult addUpdateStudent(String sessionId, Student s) {
        ExecuteResult er = new ExecuteResult(true, "Update thành công.");
        try {
            Student persistObj = studentDao.findById(s.getId());
            if (persistObj == null) {
                er.setIsSucces(false);
                er.setMessage("[Error][StudentService] Không tìm thấy SV cần update");
            } else {
                studentDao.update(s);
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
    public ExecuteResult deleteStudent(String mssv,
                                boolean deleteAnyway, String sessionId) {
        ExecuteResult er = new ExecuteResult(true, "Xóa thành công.");
        boolean deleted = false;
        try {
            Student s = studentDao.findById(mssv);
            if (s == null) {
                er.setIsSucces(false);
                er.setMessage("Không tìm thấy Sinh viên");
            } else {
                // Validate: Just delete student,
                //if he/she hasn't registerd any subject
                List<Registration> regs = regDao.findByColumName("MSSV", mssv);
                if ((regs == null) || regs.isEmpty()) {
                    // It's good time for deleting...
                    studentDao.delete(s);
                    deleted = true;
                } else {
                    if (deleteAnyway) {
                        regDao.delete(regs);
                        studentDao.delete(s);
                        deleted = true;
                    } else {
                        er.setIsSucces(false);
                        er.setMessage("Không thể xóa SV đã đk môn học");
                    }
                }
            }
            if (deleted) {
                List<Student> students = currentStudents.get(sessionId);
                if ((students != null) && students.contains(s)) {
                    students.remove(s);
                }
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
    public ExecuteResult deleteStudents(List<String> mssv,
                                boolean deleteAnyway, String sessionId) {
        ExecuteResult result = new ExecuteResult(true, "");

        try {
            List<Student> students = studentDao.findByIds(mssv);
            if ((students == null) || students.isEmpty()) {
                return new ExecuteResult(false, "Không tìm thấy sinh viên cần xóa.");
            }
            //
            // TODO: check registered student
            // >>>>
            //
            // Không thể xóa danh sách gồm có SV đã đăng ký môn học
            List<Student> registeredStudents = new ArrayList<Student>(10);
            for (Student s : students) {
                if (!regDao.findByColumName("MSSV", s.getId()).isEmpty()) {
                    registeredStudents.add(s);
                }
            }
            
            if (!registeredStudents.isEmpty()) {
                String msg = "";
                for (Student s : registeredStudents) {
                    msg += "</br>- " + s.getFullName() + " (" + s.getId() + ")";
                }
                result.setIsSucces(false);
                result.setMessage(msg);
                
                // Doen't support delete any way
                return result;
            }
            
            studentDao.delete(students);
            currentStudents.remove(sessionId);
            students = getStudents(1, sessionId);
            currentStudents.put(sessionId, students);
        } catch (Exception ex) {
            Logger.getLogger(StudentServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            result.setIsSucces(false);
            result.setMessage("[StudentService][DeleteStudents]: " + ex.toString());
        }
        
        return result;
    }

    @Override
    public int getNumberPage() {
        try {
            int rows = studentDao.getRowsCount();

            int numPage = 1;
            if (rows % rowPerPage == 0) {
                numPage = rows / rowPerPage;
            } else {
                numPage = rows / rowPerPage + 1;
            }
            return numPage;
        } catch (Exception ex) {
            Logger.getLogger(StudentServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return 1;
    }
}
