package uit.cnpm02.dkhp.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uit.cnpm02.dkhp.DAO.AccountDAO;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.RegistrationDAO;
import uit.cnpm02.dkhp.DAO.SubjectDAO;
import uit.cnpm02.dkhp.DAO.TaskDAO;
import uit.cnpm02.dkhp.DAO.TrainClassDAO;
import uit.cnpm02.dkhp.bo.AccountBO;
import uit.cnpm02.dkhp.model.Account;
import uit.cnpm02.dkhp.model.Registration;
import uit.cnpm02.dkhp.model.Task;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.model.TrainClassID;
import uit.cnpm02.dkhp.model.type.AccountType;
import uit.cnpm02.dkhp.model.type.TaskStatus;
import uit.cnpm02.dkhp.utilities.Constants;
import uit.cnpm02.dkhp.utilities.password.PasswordProtector;

/**
 *
 * @author thanh
 */
@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {

    private AccountDAO accDao = new AccountDAO();
    private TrainClassDAO tcDao = DAOFactory.getTrainClassDAO();
    
    private final String SCHEDULE_CK = "private_schedult";

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        String function = (String) request.getParameter("function");
        try {
            if (function != null) {
                if (function.equals("login")) {
                    login(request, response);
                } else if (function.equals("logout")) {
                    logOut(session, response);
                }
            } else {
                session.setAttribute("error", "Lỗi hệ thống. Vui lòng quay lại sau.");
                String path = "./jsps/Login.jsp";
                response.sendRedirect(path);
            }

        } catch (Exception ex) {
            Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }

    }

    /**
     *
     * @param session
     * @param request
     * @param response
     * @throws Exception
     */
    private void login(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String user = request.getParameter("txtUsername");
        String textPass = request.getParameter("txtPassword");
        String pass = PasswordProtector.getMD5(textPass);
        AccountBO accBo = new AccountBO();
        String path = "";
        if (accBo.Login(user, pass)) {
            if (accBo.isLogined(user)) {
                session.setAttribute("error",
                        "Tài khoản của bạn đang được đăng nhập ở một máy khác!");
                path = "./jsps/Login.jsp";
            } else if (accBo.isLock(user)) {
                session.setAttribute("error",
                        "Tài khoản của bạn đang bị khóa vui lòng liên hệ quản lý khoa để giải quyết!");
                path = "./jsps/Login.jsp";
            } else {
                session.setAttribute("username", user);
                session.setAttribute("logineduser", user);
                session.setAttribute("password", pass);

                //Set to logined.
                Account acc = accDao.findById(user);
                //acc.setIsLogined(true);
                //accDao.update(acc);
                
                if (acc.getType() == AccountType.ADMIN.value()) {
                    getRemindTask(request);
                    path = "./jsps/PDT/PDTStart.jsp";
                } else if (acc.getType() == AccountType.STUDENT.value()) {
                    getRemindTask(request);
                    List<TrainClass> tcs = getStudentSchedule(user);
                    if ((tcs != null) && !tcs.isEmpty()) {
                        session.setAttribute(SCHEDULE_CK, tcs);
                    }
                    path = "./jsps/SinhVien/SVStart.jsp";
                } else if (acc.getType() == AccountType.LECTUTER.value()) {
                    getRemindTask(request);
                    List<TrainClass> tcs = getLecturerSchedule(user);
                    if ((tcs != null) && !tcs.isEmpty()) {
                        session.setAttribute(SCHEDULE_CK, tcs);
                    }
                    path = "./jsps/GiangVien/GVStart.jsp";
                }
            }
        } else {
            session.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không hợp lệ");
            path = "./jsps/Login.jsp";
        }

        response.sendRedirect(path);
    }

    private void logOut(HttpSession session, HttpServletResponse response)
            throws IOException, Exception {
        session.removeAttribute(SCHEDULE_CK);
        String user = (String) session.getAttribute("username");
        Account acc = accDao.findById(user);

        if (acc != null) {
            //acc.setIsLogined(false);
            //accDao.update(acc);
        }

        session.removeAttribute("username");
        session.removeAttribute("logineduser");
        session.removeAttribute("pass");
        String path = "./index.jsp";
        response.sendRedirect(path);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void getRemindTask(HttpServletRequest request) {
        TaskDAO taskDao = DAOFactory.getTaskDAO();
        HttpSession session = request.getSession();
        session.removeAttribute("tasks");
        String username = "";
        String generalUsername = "";
        try {
            username = (String) session.getAttribute("username");
            Account acc = accDao.findById(username);
            if (acc.getType() == AccountType.STUDENT.value()) {
                generalUsername = "student";
            } else if (acc.getType() == AccountType.LECTUTER.value()) {
                generalUsername = "lecuturer";
            } else if (acc.getType() == AccountType.ADMIN.value()) {
                generalUsername = "admin";
            }
        } catch (Exception ex) {
            //
        }
        try {
            List<Task> tasks = taskDao.findByColumNames(
                    new String[] {"NguoiNhan", "TrangThai"},
                    new Object[] {username, TaskStatus.TOBE_PROCESS.value()});
            if (!username.equals(generalUsername))
                tasks.addAll(taskDao.findByColumNames(
                    new String[] {"NguoiNhan", "TrangThai"},
                    new Object[] {generalUsername, TaskStatus.TOBE_PROCESS.value()}));
            
            if ((tasks != null) && !tasks.isEmpty()) {
                session.setAttribute("tasks", tasks);
            }
        } catch (Exception ex) {
            Logger.getLogger(HomepageController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    
     private List<TrainClass> getLecturerSchedule(String lecturerId) {
        try {
            int semeter = Constants.CURRENT_SEMESTER;
            String year = Constants.CURRENT_YEAR;
            List<TrainClass> trainClasses = tcDao.findByColumNames(
                    new String[] {"MaGV", "HocKy", "NamHoc"},
                    new Object[] {lecturerId, semeter, year});
            SubjectDAO subDao = DAOFactory.getSubjectDao();
            for (TrainClass tc : trainClasses) {
                String subName = subDao.findById(tc.getSubjectCode()).getSubjectName();
                tc.setSubjectName(subName);
            }
            sortListTrainClassByStudyDate(trainClasses);
            return trainClasses;
        } catch (Exception ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
     }
    
    private List<TrainClass> getStudentSchedule(String studentId) {
        RegistrationDAO regDao = DAOFactory.getRegistrationDAO();
        try {
            int semeter = Constants.CURRENT_SEMESTER;
            String year = Constants.CURRENT_YEAR;
            // Cac lop SV da dk (trong hoc ki hien tai)
            List<Registration> regs = regDao.findByColumNames(
                    new String[] {"MSSV", "HocKy", "NamHoc"},
                    new Object[] {studentId, semeter, year});
            List<TrainClassID> tcIDs = new ArrayList<TrainClassID>(10);
            if ((regs == null) || regs.isEmpty()) {
                return null;
            }
            
            for (Registration reg : regs) {
                TrainClassID tcID = new TrainClassID(reg.getId().getClassCode(),
                        year, semeter);
                tcIDs.add(tcID);
            }
            List<TrainClass> trainClasses = tcDao.findByIds(tcIDs);
            SubjectDAO subDao = DAOFactory.getSubjectDao();
            for (TrainClass tc : trainClasses) {
                String subName = subDao.findById(tc.getSubjectCode()).getSubjectName();
                tc.setSubjectName(subName);
            }
            sortListTrainClassByStudyDate(trainClasses);
            
            return trainClasses;
        } catch (Exception ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return null;
    }
    
    private void sortListTrainClassByStudyDate(List<TrainClass> trainClasses) {
        Collections.sort(trainClasses, new Comparator<TrainClass>() {

            @Override
            public int compare(TrainClass o1, TrainClass o2) {
                return (o1.getStudyDate() - o2.getStudyDate());
            }
        });
    }
}
