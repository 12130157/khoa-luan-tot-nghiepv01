package uit.cnpm02.dkhp.controllers.SV;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uit.cnpm02.dkhp.model.Registration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uit.cnpm02.dkhp.DAO.ClassDAO;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.FacultyDAO;
import uit.cnpm02.dkhp.DAO.LecturerDAO;
import uit.cnpm02.dkhp.DAO.RegistrationDAO;
import uit.cnpm02.dkhp.DAO.RuleDAO;
import uit.cnpm02.dkhp.DAO.StudentDAO;
import uit.cnpm02.dkhp.DAO.SubjectDAO;
import uit.cnpm02.dkhp.DAO.TaskDAO;
import uit.cnpm02.dkhp.DAO.TrainClassDAO;
import uit.cnpm02.dkhp.model.Student;
import uit.cnpm02.dkhp.model.Class;
import uit.cnpm02.dkhp.model.Faculty;
import uit.cnpm02.dkhp.model.RegistrationID;
import uit.cnpm02.dkhp.model.RegistrationTime;
import uit.cnpm02.dkhp.model.RegistrationTimeID;
import uit.cnpm02.dkhp.model.Task;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.model.TrainClassID;
import uit.cnpm02.dkhp.model.type.StudentStatus;
import uit.cnpm02.dkhp.model.type.TaskStatus;
import uit.cnpm02.dkhp.model.type.TaskType;
import uit.cnpm02.dkhp.service.TrainClassStatus;
import uit.cnpm02.dkhp.utilities.Constants;
import uit.cnpm02.dkhp.utilities.ExecuteResult;

/**
 *
 * @author thanh
 */
@WebServlet(name = "RegistryController", urlPatterns = {"/RegistryController"})
public class RegistryController extends HttpServlet {
    private final String LIST_STRING_REGISTRIED_CK = "registriedID";
    private final Object mutex = new Object();
    
    // DAO initial ++
    private StudentDAO studentDao = DAOFactory.getStudentDao();
    private ClassDAO classDao = DAOFactory.getClassDao();
    private FacultyDAO facultyDao = DAOFactory.getFacultyDao();
    private RuleDAO ruleDao = DAOFactory.getRuleDao();
    private RegistrationDAO regDao = DAOFactory.getRegistrationDAO();
    private TrainClassDAO tcDao = DAOFactory.getTrainClassDAO();
    private LecturerDAO lecturerDao = DAOFactory.getLecturerDao();
    private SubjectDAO subjectDao = DAOFactory.getSubjectDao();
    // DAO initial --
    /** 
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        try {
            String action = request.getParameter("action");
            if (action.equalsIgnoreCase("view")) {
                /**
                 * Ktra SV da dki trc chua?
                 * Neu da dki, load trang chua thong
                 * tin cac lop SV da dang ki, neu chua
                 * load trang dang ki hoc phan
                 */
                forward(response, session);
            } else if (action.equalsIgnoreCase("reRegistry")) {
                reRegistry(response, session);
            } else if (action.equalsIgnoreCase("registry")) {
                preRegistry(response, request);
            } else if (action.equalsIgnoreCase("completeRegistry")) {
                completeRegistration(response, request);
            } else if (action.equals("detail")) {
                detailTrainClass(response, request);
            } else if (action.equalsIgnoreCase("get-reged-students")) {
                getListStudentRegedOnTrainClass(response, request);
            } else if(action.equalsIgnoreCase("student-process-task")) {
                showTaskProcessPopup(request, response);
            } else if (action.equalsIgnoreCase("student-confirm-process-task")) {
                processTaskConfirm(request, response);
            }
        } finally {
            out.close();
        }
    }
    /**
     * 
     * @param response
     * @param request
     * @param session
     * @throws Exception 
     */
    private void detailTrainClass(HttpServletResponse response,
            HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        String path = "";
        try {
            String classCode = request.getParameter("classCode");
            int semester = Integer.parseInt(request.getParameter("semester"));
            String year = request.getParameter("year");
            TrainClassID id = new TrainClassID(classCode, year, semester);
            List<Registration> registration = regDao.findAllByClassCode(id);
            List<Student> studentList = new ArrayList<Student>();
            for (int i = 0; i < registration.size(); i++) {
                Student student = studentDao.findById(registration.get(i).getId().getStudentCode());
                studentList.add(student);
            }
            TrainClassID trainClassId = new TrainClassID(classCode, Constants.CURRENT_YEAR, Constants.CURRENT_SEMESTER);
            TrainClass trainClass = tcDao.findById(trainClassId);
            trainClass.setLectturerName(lecturerDao.findById(trainClass.getLecturerCode()).getFullName());
            trainClass.setSubjectName(subjectDao.findById(trainClass.getSubjectCode()).getSubjectName());
            session.setAttribute("studentList", studentList);
            session.setAttribute("classInfo", trainClass);
            path = "./jsps/SinhVien/ClassDetail.jsp";
            response.sendRedirect(path);
        } catch (Exception e) {
            path = "./jsps/Message.jsp";
            response.sendRedirect(path);
        }

    }
 /**
  * Count total TC of list trainclass.
  * @param registried
  * @return 
  */
    private int getNumTCRegistry(List<TrainClass> registried) {
        int numTC = 0;
        if (!registried.isEmpty()) {
            for (int i = 0; i < registried.size(); i++) {
                numTC += registried.get(i).getNumTC();
            }
        }
        return numTC;
    }
     /**
      * 
      * @param classCode
      * @return
      * @throws Exception 
      */
    private boolean checkNumOfStudentReg(String classCode) throws Exception {
        TrainClassID trainClassId = new TrainClassID(
                classCode, Constants.CURRENT_YEAR, Constants.CURRENT_SEMESTER);
        TrainClass trainClass = tcDao.findById(trainClassId);
        
        return (trainClass.getNumOfStudentReg()
                >= trainClass.getNumOfStudent());
    }
    /**
     * 
     * @param registried
     * @return
     * @throws Exception 
     */
    private boolean isNotEnoughTC(List<TrainClass> registried) throws Exception {
        int minNumTC = (int) ruleDao.findById("SoTinChiToiThieu").getValue();
        return (getNumTCRegistry(registried) < minNumTC);
    }
    
    private float getRule(String key) {
        try {
            return ruleDao.findById(key).getValue();
        } catch (Exception ex) {
            Logger.getLogger(RegistryController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    private boolean isRegTwoClassTrainASubject(List<TrainClass> registries) {
        for (int i = 0; i < registries.size()-1; i++) {
            for (int j = i + 1; j < registries.size(); j++) {
                if (registries.get(i).getSubjectCode()
                        .equalsIgnoreCase(registries.get(j).getSubjectCode())) {
                    //result = true;
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean isRegTwoClassInADay(List<TrainClass> registried) {
        for (int i = 0; i < registried.size()-1; i++) {
            for (int j = i + 1; j < registried.size(); j++) {
                if (registried.get(i).getStudyDate() == registried.get(j).getStudyDate()
                        && registried.get(i).getShift() == registried.get(j).getShift()) {
                    //result = true;
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 
     * @param registried
     * @return
     * @throws Exception 
     */
    private boolean isOverTC(List<TrainClass> registried) throws Exception {
        int maxNumTC = (int) ruleDao.findById("SoTinChiToiDa").getValue();
        
        return (getNumTCRegistry(registried) > maxNumTC);
    }
    /**
     * 
     * @param registried
     * @return 
     */
    private boolean isEnoughRequiredTC(List<TrainClass> registried) {
        try {
            int maxNumTC = (int) ruleDao
                    .findById("SoTinChiBatBuocToiThieu").getValue();
            return (getNumRequireTC(registried) > maxNumTC);
        } catch (Exception ex) {
            Logger.getLogger(RegistryController.
                    class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    /**
     * 
     * @param registried
     * @return 
     */
    private int getNumRequireTC(List<TrainClass> registried) {
        int numTC = 0;
        if (registried.isEmpty() == false) {
            for (int i = 0; i < registried.size(); i++) {
                try {
                    if (subjectDao.findById(registried.get(i).getSubjectCode()).getType() == 0) {
                        numTC += registried.get(i).getNumTC();
                    }
                } catch (Exception ex) {
                    Logger.getLogger(RegistryController.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            }
        }
        return numTC;
    }
    /**
     * 
     * @param subjectCode
     * @param studentCode
     * @return
     * @throws Exception 
     */
    private boolean isPresubNotComplete(String subjectCode, String studentCode)
            throws Exception {
        //boolean result = false;
        float markPass = ruleDao.findById("DiemQuaMon").getValue();
        List<String> preSub = DAOFactory.getPreSubDao().findAllPreSubBySubCode(subjectCode);
        if ((preSub != null) && !preSub.isEmpty()) {
            for (int i = 0; i < preSub.size(); i++) {
                float mark = DAOFactory.getStudyResultDao()
                        .getMarkByStudentAndSub(preSub.get(i), studentCode);
                if (mark < markPass) {
                    //result = true;
                    return true;
                }
            }
        }
        //return result;
        return false;
    }
    /**
     * 
     * @param response
     * @param request
     * @param session
     * @throws IOException 
     */
    private void completeRegistration(HttpServletResponse response,
            HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        String path = "";
        try {
            synchronized(mutex) {
                doRegister(response, session);
            }
        } catch (Exception ex) {
            path = "./jsps/Message.jsp";
            response.sendRedirect(path);
        }

    }
    
    private void doRegister(HttpServletResponse response,
            HttpSession session) throws Exception {
        String path = "";
        String studentCode = (String) session.getAttribute("username");
        List<TrainClass> registried = (List<TrainClass>) session.getAttribute("registriedClass");
        ArrayList<String> message = new ArrayList<String>();

        // Kiem tra dieu kien dang ki mon hoc
        // + Ktra so luong tin chi toi da, toi thieu
        // + Ktra Trung lich hoc
        // ...
        ExecuteResult regOK = validateRegister(registried);

        if (!regOK.isIsSucces()) {
            Student student = studentDao.findById(studentCode);
            Class classes = classDao.findById(student.getClassCode());
            Faculty faculty = facultyDao.findById(student.getFacultyCode());
            session.setAttribute("error", regOK.getMessage());
            session.setAttribute("student", student);
            session.setAttribute("classes", classes);
            session.setAttribute("faculty", faculty);
            session.setAttribute("registriedClass", registried);
            session.setAttribute("semester", Constants.CURRENT_SEMESTER);
            session.setAttribute("year", Constants.CURRENT_YEAR);
            path = "./jsps/SinhVien/PreviewRegistration.jsp";
            response.sendRedirect(path);
        } else { // Kiem tra dieu kien hop le ==> tien hanh dang ky
            regDao.deleteAllByByStudentCode(studentCode);
            for (int i = 0; i < registried.size(); i++) {
                if (checkNumOfStudentReg(registried.get(i).getId().getClassCode())) {
                    String mes = "Lớp học "
                            + registried.get(i).getId().getClassCode()
                            + " (" + registried.get(i).getSubjectName()
                            + ") đã đủ số lượng nên không thẻ đăng ký thêm";
                    message.add(mes);
                } else if (isPresubNotComplete(registried.get(i).getSubjectCode(), studentCode)) {
                    String mes = "Lớp học "
                            + registried.get(i).getId().getClassCode()
                            + " (" + registried.get(i).getSubjectName()
                            + ") không thể đăng ký do chưa hoàn thành môn học tiên quyết của nó."
                            + " Xem thêm quy định môn học tiên quyết.";
                    message.add(mes);
                } else {
                    Registration reg = new Registration(
                            studentCode,
                            registried.get(i).getId().getClassCode(),
                            Constants.CURRENT_YEAR,
                            Constants.CURRENT_SEMESTER,
                            registried.get(i).getNumOfStudentReg() + 1, 0);
                    regDao.add(reg);
                }
            
                }
            if (message.isEmpty()) {
                forward(response, session);
            } else {
                session.setAttribute("message", message);
                path = "./jsps/SinhVien/ErrorMessage.jsp";
                response.sendRedirect(path);
            }
        }
    }
    
    /**
     * Validate condition for register new trainclasses
     * @param registried
     * @return
     * @throws Exception 
     */
    private ExecuteResult validateRegister(List<TrainClass> registried)
                                                        throws Exception {
        boolean regOK = false;
        String errorMessage = "";
        if (isNotEnoughTC(registried)) {
            errorMessage = "Số tín chỉ đăng ký chưa đủ quy định,"
                    + " tối thiểu là "
                    + (int) ruleDao.findById("SoTinChiToiThieu").getValue()
                    + " TC. Xem thêm quy định.";
        } else if (isOverTC(registried)) {
            errorMessage = "Số tín chỉ đăng ký quá quy định, tối đa là "
                    + (int) ruleDao.findById("SoTinChiToiDa").getValue()
                    + " TC. Xem thêm quy định.";
        } else if (!isEnoughRequiredTC(registried)) {
            errorMessage = "Số tín chỉ bắt buộc chưa đủ quy định, tối thiểu là "
                    + (int) ruleDao.findById("SoTinChiBatBuocToiThieu").getValue()
                    + " TC. Xem thêm quy định.";
        } else if (isRegTwoClassTrainASubject(registried)) {
            errorMessage = "Không thể đăng ký 2 lớp học cùng dạy một môn học trong cùng một học kỳ."
                    + " Vui lòng kiểm tra lại";
        } else if (isRegTwoClassInADay(registried)) {
            errorMessage = "Không thể đăng ký học 2 lớp học được dạy cùng 1 thời điểm."
                    + " Vui lòng kiểm tra lại";
        }
        // Else - 
        else {
            regOK = true;
        }
        
        return new ExecuteResult(regOK, errorMessage);
    }
    
    /**
     * 
     * @param response
     * @param request
     * @param session
     * @throws IOException
     * @throws Exception 
     */
    private void preRegistry(HttpServletResponse response,
            HttpServletRequest request) throws IOException, Exception {
        String path = "";
        HttpSession session = request.getSession();
        try {
            String studentCode = (String) session.getAttribute("username");
            String[] registries = request.getParameterValues("check");
            if ((registries == null) || (registries.length == 0)) {
                getAllClass(response, session, studentCode);
            } else {
                List<TrainClass> registriedClass = new ArrayList<TrainClass>();
                for (int i = 0; i < registries.length; i++) {
                    TrainClassID trainClassId = new TrainClassID(registries[i],
                            Constants.CURRENT_YEAR, Constants.CURRENT_SEMESTER);
                    registriedClass.add(tcDao.findById(trainClassId));
                }
                setSubjectAndLecturer(registriedClass);
                sortListTrainClassByStudyDate(registriedClass);
                Student student = studentDao.findById(studentCode);
                Class classes = DAOFactory.getClassDao().findById(student.getClassCode());
                Faculty faculty = DAOFactory.getFacultyDao().findById(student.getFacultyCode());
                session.setAttribute("student", student);
                session.setAttribute("classes", classes);
                session.setAttribute("faculty", faculty);
                session.setAttribute("registriedClass", registriedClass);
                session.setAttribute("semester", Constants.CURRENT_SEMESTER);
                session.setAttribute("year", Constants.CURRENT_YEAR);
                //List<String> regs_Temp = (List<String>) session.getAttribute(LIST_STRING_REGISTRIED_CK);
                //if ((regs_Temp == null) || regs_Temp.isEmpty())
                session.setAttribute(LIST_STRING_REGISTRIED_CK, Arrays.asList(registries));
                path = "./jsps/SinhVien/PreviewRegistration.jsp";
            }
        } catch (Exception ex) {
            path = "./jsps/Message.jsp";
        }
        response.sendRedirect(path);
    }

    /**
     * 
     * @param response
     * @param session
     * @throws Exception 
     */
    private void reRegistry(HttpServletResponse response, HttpSession session) throws Exception {
        String path = "";
        try {
            String studentCode = (String) session.getAttribute("username");
            Student student = studentDao.findById(studentCode);
            String facultyCode = student.getFacultyCode();
            List<TrainClass> trainClass = tcDao.findAllByFacultyCodeAndTime(facultyCode);
            setSubjectAndLecturer(trainClass);
            for (int i = trainClass.size() - 1; i >= 0; i--) {
                if (isPresubNotComplete(trainClass.get(i).getSubjectCode(), studentCode)) {
                    trainClass.remove(i);
                }
            }
            // Retrive external trainclass
            // Kiem tra mon hoc tien quyet cho danh sach lop mo rong ?
            List<TrainClass> extTrainClass = tcDao.findByColumName(
                    "TrangThai", TrainClassStatus.OPEN.getValue());
            if ((extTrainClass != null) && !extTrainClass.isEmpty()) {
                extTrainClass.removeAll(trainClass);
            }
            for (int i = extTrainClass.size() - 1; i >= 0; i--) {
                if (isPresubNotComplete(extTrainClass.get(i).getSubjectCode(), studentCode)) {
                    extTrainClass.remove(i);
                }
            }
            setSubjectAndLecturer(extTrainClass);
            Class classes = DAOFactory.getClassDao().findById(student.getClassCode());
            Faculty faculty = DAOFactory.getFacultyDao().findById(student.getFacultyCode());
            session.setAttribute("student", student);
            session.setAttribute("classes", classes);
            session.setAttribute("faculty", faculty);
            session.setAttribute("trainClass", trainClass);
            session.setAttribute("ExtTrainClass", extTrainClass);
            session.setAttribute("semester", Constants.CURRENT_SEMESTER);
            session.setAttribute("year", Constants.CURRENT_YEAR);
            //session.removeAttribute(LIST_STRING_REGISTRIED_CK);
            List<String> regs_Temp = (List<String>) session.getAttribute(LIST_STRING_REGISTRIED_CK);
            if ((regs_Temp == null) || regs_Temp.isEmpty()){
                List<String> registried = getRegistried(studentCode);
                session.setAttribute(LIST_STRING_REGISTRIED_CK, registried);
            }
            //session.setAttribute(LIST_STRING_REGISTRIED_CK, registried);

            path = "./jsps/SinhVien/Registration.jsp";
        } catch (Exception ex) {
            path = "./jsps/Message.jsp";
        }
        response.sendRedirect(path);

    }

    /**
     * 
     * @param studentCode
     * @return
     * @throws Exception 
     */
    private List<String> getRegistried(String studentCode) throws Exception {
        ArrayList<String> result = new ArrayList<String>();
        List<Registration> regs = regDao.findAllByStudentCode(studentCode);
        if ((regs != null) && !regs.isEmpty()) {
            for (Registration r : regs) {
                result.add(r.getId().getClassCode());
            }
        }
        return result;
    }
    /**
     * 
     * @param response
     * @param session
     * @throws Exception 
     */
    private void forward(HttpServletResponse response,
            HttpSession session) throws Exception {
        String path = "";
        session.removeAttribute(LIST_STRING_REGISTRIED_CK);
        try {
            String user = (String) session.getAttribute("username");
            if (regDao == null) {
                throw new Exception("Can't not create RegistrationDAO object.");
            }
            
            Student s = studentDao.findById(user);
            boolean nonStudy = false;
            if ((s != null) &&
                    (s.getStatus() == StudentStatus.NON_STUDING.value())) {
                nonStudy = true;
            }
            session.setAttribute("non-study-student", nonStudy);
            
            // Danh sach mon hoc SV da dk trc (co the null)
            List<Registration> regsSub = regDao.findAllByStudentCode(user);
            if (isInTimeRegistry() && regsSub.isEmpty()) {
                getAllClass(response, session, user);
            }
            
            // Set max and min tc
            int min_tc = (int) getRule("SoTinChiToiThieu");
            int max_tc = (int) getRule("SoTinChiToiDa");
            session.setAttribute("min-tc", min_tc);
            session.setAttribute("max-tc", max_tc);
            
            showRegitration(regsSub, response, session, user);
        } catch (Exception ex) {
            path = "./jsps/Message.jsp";
        }
        response.sendRedirect(path);
    }

    private boolean isInTimeRegistry() {
        try {
            RegistrationTimeID id = new RegistrationTimeID(
                    Constants.CURRENT_SEMESTER, Constants.CURRENT_YEAR);
            RegistrationTime registrationTime = 
                    DAOFactory.getRegistrationTimeDAO().findById(id);
            Date today = new Date();
            
            return today.before(registrationTime.getEndDate());
        } catch (Exception ex) {
            Logger.getLogger(RegistryController.class.getName())
                    .log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * 
     * @param resp
     * @param session
     * @param mssv
     * @throws Exception 
     */
    private void getAllClass(HttpServletResponse resp,
            HttpSession session, String mssv) throws Exception {
        if (studentDao == null) {
            throw new Exception("Can't create Student DAO object.");
        }
        Student student = studentDao.findById(mssv);
        String facultyCode = student.getFacultyCode();
        
        List<TrainClass> trainClass = tcDao.findAllByFacultyCodeAndTime(facultyCode);
        for (int i = trainClass.size() - 1; i >= 0; i--) {
            if (isPresubNotComplete(trainClass.get(i).getSubjectCode(), mssv)) {
                trainClass.remove(i);
            }
        }
        setSubjectAndLecturer(trainClass);
        
        // Retrive external trainclass
        // Kiem tra mon hoc tien quyet cho danh sach lop mo rong ?
        List<TrainClass> extTrainClass = tcDao.findByColumName(
                "TrangThai", TrainClassStatus.OPEN.getValue());
        if ((extTrainClass != null) && !extTrainClass.isEmpty()) {
            extTrainClass.removeAll(trainClass);
        }
        for (int i = extTrainClass.size()-1; i >= 0; i--) {
            if (isPresubNotComplete(extTrainClass.get(i).getSubjectCode(), mssv)) {
                extTrainClass.remove(i);
            }
        }
        setSubjectAndLecturer(extTrainClass);
        //List<String> registried = new ArrayList<String>();
        session.removeAttribute(LIST_STRING_REGISTRIED_CK);
        Class classes = DAOFactory.getClassDao().findById(student.getClassCode());
        Faculty faculty = DAOFactory.getFacultyDao().findById(student.getFacultyCode());
        session.setAttribute("student", student);
        session.setAttribute("classes", classes);
        session.setAttribute("faculty", faculty);
        session.setAttribute("trainClass", trainClass);
        session.setAttribute("ExtTrainClass", extTrainClass);
        session.setAttribute("semester", Constants.CURRENT_SEMESTER);
        session.setAttribute("year", Constants.CURRENT_YEAR);
        //session.setAttribute(LIST_STRING_REGISTRIED_CK, registried);
        String path = "./jsps/SinhVien/Registration.jsp";
        resp.sendRedirect(path);

    }

    /**
     * 
     * @param trainClass
     * @throws Exception 
     */
    private void setSubjectAndLecturer(List<TrainClass> trainClass) throws Exception {
        for (int i = 0; i < trainClass.size(); i++) {
            trainClass.get(i).setSubjectName(subjectDao.findById(trainClass.get(i).getSubjectCode()).getSubjectName());
            trainClass.get(i).setLectturerName(lecturerDao.findById(trainClass.get(i).getLecturerCode()).getFullName());
            trainClass.get(i).setNumTC(subjectDao.findById(trainClass.get(i).getSubjectCode()).getnumTC());
        }
    }

    /**
     * 
     * @param registration
     * @param response
     * @param session
     * @param studentCode
     * @throws Exception 
     */
    private void showRegitration(List<Registration> registration,
            HttpServletResponse response, HttpSession session, String studentCode) throws Exception {
        Student student = DAOFactory.getStudentDao().findById(studentCode);
        Class classes = DAOFactory.getClassDao().findById(student.getClassCode());
        Faculty faculty = DAOFactory.getFacultyDao().findById(student.getFacultyCode());
        session.setAttribute("student", student);
        session.setAttribute("classes", classes);
        session.setAttribute("faculty", faculty);
        session.setAttribute("semester", Constants.CURRENT_SEMESTER);
        session.setAttribute("year", Constants.CURRENT_YEAR);
        session.setAttribute("inTimeRegistry", isInTimeRegistry());
        List<TrainClass> registried = new ArrayList<TrainClass>();
        for (int i = 0; i < registration.size(); i++) {
            String classCode = registration.get(i).getId().getClassCode();
            TrainClassID trainClassId = new TrainClassID(classCode,
                    Constants.CURRENT_YEAR, Constants.CURRENT_SEMESTER);
            TrainClass trainClass = DAOFactory.getTrainClassDAO().findById(trainClassId);
            registried.add(trainClass);
        }
        setSubjectAndLecturer(registried);
        sortListTrainClassByStudyDate(registried);
        
        session.setAttribute("registried", registried);
        String path = "./jsps/SinhVien/ShowRegistry.jsp";
        response.sendRedirect(path);

    }
    
    private void sortListTrainClassByStudyDate(List<TrainClass> registried) {
        Collections.sort(registried, new Comparator<TrainClass>() {

            @Override
            public int compare(TrainClass o1, TrainClass o2) {
                return (o1.getStudyDate() - o2.getStudyDate());
            }
        });
    }

    /** 
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(RegistryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** 
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(RegistryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** 
     * 
     * @return 
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private void getListStudentRegedOnTrainClass(HttpServletResponse response,
            HttpServletRequest request) throws IOException {
        
        PrintWriter out = response.getWriter();
        String trainClassId = (String) request.getParameter("trainclassid");
        String semeterStr = (String) request.getParameter("semeter");
        String year = (String) request.getParameter("year");
        int semeter = -1;
        try {
            semeter = Integer.parseInt(semeterStr);
        } catch (Exception ex) {
            out.println("Lỗi, không lấy được thông tin");
            return;
        }
        
        TrainClassID id = new TrainClassID(trainClassId, year, semeter);
        List<Registration> registration;
        try {
            registration = regDao.findAllByClassCode(id);
            List<Student> studentList = new ArrayList<Student>();
            for (int i = 0; i < registration.size(); i++) {
                Student student = studentDao.findById(registration.get(i).getId().getStudentCode());
                studentList.add(student);
            }
            
            if (studentList != null) {
                writeOutListStudent(out, studentList);
            }
        } catch (Exception ex) {
            Logger.getLogger(RegistryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private int slideLimit = 50;
    private void writeOutListStudent(PrintWriter out, List<Student> students) {
        if (students == null) {
            return;
        }
        out.println("<div id=\"popup-title\">DS SV đã đăng ký</div> <br />");
        if (students.size() > slideLimit) {
            out.println("<div id=\"sidebar\">");
        }
        out.println("<table class=\"general-table\">");
        out.println("<tr>"
                + "<th> STT </th>"
                + "<th> MSSV </th>"
                + "<th> Họ tên </th>"
                + "<th> Lớp </th>"
                + "<th> Khoa </th>"
                + "</tr>");

        if (!students.isEmpty()) {
            for (int i = 0; i < students.size(); i++) {
                out.println("<tr>"
                        + "<td>" + (i + 1) + "</td>"
                        + "<td>" + students.get(i).getId() + "</td>"
                        + "<td>" + students.get(i).getFullName() + "</td>"
                        + "<td>" + students.get(i).getClassCode() + "</td>"
                        + "<td>" + students.get(i).getFacultyCode() + "</td>"
                        + "</tr>");
            }
        }
        out.println("</table>");
        if (students.size() > slideLimit) {
            out.println("</div>");
        }
    }

    // Show task process popup
    // Task id get from request object
    private void showTaskProcessPopup(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        String taskId = (String) request.getParameter("taskid");
        int id = Integer.parseInt(taskId);
        TaskDAO taskDao = DAOFactory.getTaskDAO();
        Task task = null;
        try {
            task = taskDao.findById(id);
        } catch (Exception ex) {
            Logger.getLogger(RegistryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (task == null) {
            out.println("Không có yêu cầu nào tương ứng");
            return;
        }
        boolean yes_no = false;
        if (task.getTaskType() == TaskType.MOVE_STUDENT_TO_TC) {
            yes_no = true;
        }
        
        out.println("<div id=\"popup-title\">Thông tin phản hồi</div> <br />");
        out.println("Nội dung thông báo: " + task.getContent() + "<br /> <br />");
        
        if (yes_no) {
            out.println("<br />SV click vào nút <b>Đồng ý</b> để đồng ý với quyết định của PĐT hoặc "
                    + "click vào nut <b>Hủy</b> để yêu cầu hủy bỏ thao tác của PĐT <br /> <br />");
        }

        // Button Yes - No
        out.println("<div class=\"button-1\" style=\"padding: 2px !important; margin-top: 13px; float: left; margin-left: 25px;\">");
        out.println("<span class=\"atag\" onclick=\"confirm("+ task.getId()+", 'OK'" +")\" ><img src=\"../../imgs/check.png\" />Đồng ý</span>");
        out.println("</div>");
        
        // Is cancel button displayed ?
        if (yes_no) { /** or some else allow user cancel **/
            out.println("<div class=\"button-1\" style=\"padding: 2px !important; margin-top: 13px; float: left; margin-left: 25px;\">");
            out.println("<span class=\"atag\" onclick=\"confirm("+ task.getId()+", 'NOK'" +")\" ><img src=\"../../imgs/check.png\" />Hủy</span>");
            out.println("</div>");
        }
    }

    private void processTaskConfirm(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        String taskId = (String) request.getParameter("taskid");
        String YES_NO = (String) request.getParameter("yesno");
        
        if (YES_NO.equals("OK")) { // OK, task accepted, no need to do anything else
            out.println("Xác nhận thành công");
        } else {
            int id = Integer.parseInt(taskId);
            TaskDAO taskDao = DAOFactory.getTaskDAO();
            try {
                Task task = taskDao.findById(id);
                if (task.getTaskType() == TaskType.MOVE_STUDENT_TO_TC) { //OH, that student cancel admin decicion
                                                                    // OK, I'll rollback his registration.
                    // Task processed
                    task.setStatus(TaskStatus.PROCESSED);
                    taskDao.update(task);
                    
                    // Rollback registration
                    String content = task.getContent();
                    String data = content.substring(1, content.indexOf("]"));
                    String[] regIdData = data.split(";");
                    
                    String mssv = regIdData[0];
                    String trainClassId = regIdData[1];
                    String year = regIdData[2];
                    String semeter = regIdData[3];
                    RegistrationID regId = new RegistrationID(mssv, trainClassId, Integer.parseInt(semeter), year);
                    Registration reg = regDao.findById(regId);
                    regDao.delete(reg);
                    
                    out.println("Hoàn tất, việc chuyển lớp đã được hủy bỏ.");
                }
            } catch (Exception ex) {
                Logger.getLogger(RegistryController.class.getName())
                        .log(Level.SEVERE, null, ex);
                out.println("Trong quá trình xử lý đã có lỗi xảy ra, PĐT sẽ xem xét lại trường hợp này");
            }
        }
    }
}
