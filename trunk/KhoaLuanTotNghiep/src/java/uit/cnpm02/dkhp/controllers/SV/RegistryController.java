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
import uit.cnpm02.dkhp.DAO.TrainClassDAO;
import uit.cnpm02.dkhp.model.Student;
import uit.cnpm02.dkhp.model.Class;
import uit.cnpm02.dkhp.model.Faculty;
import uit.cnpm02.dkhp.model.RegistrationTime;
import uit.cnpm02.dkhp.model.RegistrationTimeID;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.model.TrainClassID;
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
    
    /*private void completeRegistration(HttpServletResponse response,
            HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        String path = "";
        try {
            String studentCode = (String) session.getAttribute("username");
            List<TrainClass> registried = (List<TrainClass>) session.getAttribute("registriedClass");
            ArrayList<String> message = new ArrayList<String>();
            
            // DAO initial ++
            StudentDAO studentDao = DAOFactory.getStudentDao();
            ClassDAO classDao = DAOFactory.getClassDao();
            FacultyDAO facultyDao = DAOFactory.getFacultyDao();
            RuleDAO ruleDao = DAOFactory.getRuleDao();
            // DAO initial --
            
            if (isNotEnoughTC(registried)) {
                session.setAttribute("error", "Số tín chỉ đăng ký chưa đủ quy định,"
                        + " tối thiểu là "
                        + (int) ruleDao.findById("SoTinChiToiThieu").getValue()
                        + " TC. Xem thêm quy định.");
                Student student = studentDao.findById(studentCode);
                Class classes = classDao.findById(student.getClassCode());
                Faculty faculty = facultyDao.findById(student.getFacultyCode());
                session.setAttribute("student", student);
                session.setAttribute("classes", classes);
                session.setAttribute("faculty", faculty);
                session.setAttribute("registriedClass", registried);
                session.setAttribute("semester", Constants.CURRENT_SEMESTER);
                session.setAttribute("year", Constants.CURRENT_YEAR);
                path = "./jsps/SinhVien/PreviewRegistration.jsp";
                response.sendRedirect(path);
            } else if (isOverTC(registried)) {
                session.setAttribute("error",
                        "Số tín chỉ đăng ký quá quy định, tối đa là "
                        + (int) ruleDao.findById("SoTinChiToiDa").getValue()
                        + " TC. Xem thêm quy định.");
                Student student = studentDao.findById(studentCode);
                Class classes = classDao.findById(student.getClassCode());
                Faculty faculty = facultyDao.findById(student.getFacultyCode());
                session.setAttribute("student", student);
                session.setAttribute("classes", classes);
                session.setAttribute("faculty", faculty);
                session.setAttribute("registriedClass", registried);
                session.setAttribute("semester", Constants.CURRENT_SEMESTER);
                session.setAttribute("year", Constants.CURRENT_YEAR);
                path = "./jsps/SinhVien/PreviewRegistration.jsp";
                response.sendRedirect(path);
            } else if (!isEnoughRequiredTC(registried)) {
                session.setAttribute("error",
                        "Số tín chỉ bắt buộc chưa đủ quy định, tối thiểu là "
                        + (int) ruleDao.findById("SoTinChiBatBuocToiThieu").getValue()
                        + " TC. Xem thêm quy định.");
                Student student = studentDao.findById(studentCode);
                Class classes = classDao.findById(student.getClassCode());
                Faculty faculty = facultyDao.findById(student.getFacultyCode());
                session.setAttribute("student", student);
                session.setAttribute("classes", classes);
                session.setAttribute("faculty", faculty);
                session.setAttribute("registriedClass", registried);
                session.setAttribute("semester", Constants.CURRENT_SEMESTER);
                session.setAttribute("year", Constants.CURRENT_YEAR);
                path = "./jsps/SinhVien/PreviewRegistration.jsp";
                response.sendRedirect(path);
            } else if (isRegTwoClassTrainASubject(registried)) {
                session.setAttribute("error",
                        "Không thể đăng ký 2 lớp học cùng dạy một môn học trong cùng một học kỳ."
                        + " Vui lòng kiểm tra lại");
                Student student = studentDao.findById(studentCode);
                Class classes = classDao.findById(student.getClassCode());
                Faculty faculty = facultyDao.findById(student.getFacultyCode());
                session.setAttribute("student", student);
                session.setAttribute("classes", classes);
                session.setAttribute("faculty", faculty);
                session.setAttribute("registriedClass", registried);
                session.setAttribute("semester", Constants.CURRENT_SEMESTER);
                session.setAttribute("year", Constants.CURRENT_YEAR);
                path = "./jsps/SinhVien/PreviewRegistration.jsp";
                response.sendRedirect(path);
            } else if (isRegTwoClassInADay(registried)) {
                session.setAttribute("error",
                        "Không thể đăng ký học 2 lớp học được dạy cùng 1 thời điểm."
                        + " Vui lòng kiểm tra lại");
                Student student = studentDao.findById(studentCode);
                Class classes = classDao.findById(student.getClassCode());
                Faculty faculty = facultyDao.findById(student.getFacultyCode());
                session.setAttribute("student", student);
                session.setAttribute("classes", classes);
                session.setAttribute("faculty", faculty);
                session.setAttribute("registriedClass", registried);
                session.setAttribute("semester", Constants.CURRENT_SEMESTER);
                session.setAttribute("year", Constants.CURRENT_YEAR);
                path = "./jsps/SinhVien/PreviewRegistration.jsp";
                response.sendRedirect(path);
            } else {
                RegistrationDAO regDao = DAOFactory.getRegistrationDAO();
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
        } catch (Exception ex) {
            path = "./jsps/Message.jsp";
            response.sendRedirect(path);
        }

    }*/
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
            
            // Danh sach mon hoc SV da dk trc (co the null)
            List<Registration> regsSub = regDao.findAllByStudentCode(user);
            /*if (isInTimeRegistry()) {
                if (regsSub.isEmpty()) {
                    getAllClass(response, session, user);
                } else {
                    showRegitration(regsSub, response, session, user);
                }
            } else {
                showRegitration(regsSub, response, session, user);
            }*/
            if (isInTimeRegistry() && regsSub.isEmpty()) {
                getAllClass(response, session, user);
            }
            
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
            /*if (registrationTime.getEndDate().getYear() < today.getYear()) {
                return false;
            } else if (registrationTime.getEndDate().getMonth() < today.getMonth()) {
                return false;
            } else if (registrationTime.getEndDate().getDate() < today.getDate()) {
                return false;
            } else {
                return true;
            }*/
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
}
