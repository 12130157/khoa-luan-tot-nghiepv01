package uit.cnpm02.dkhp.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import uit.cnpm02.dkhp.DAO.StudentDAO;
import uit.cnpm02.dkhp.model.Account;
import uit.cnpm02.dkhp.model.Student;
import uit.cnpm02.dkhp.DAO.ClassDAO;
import uit.cnpm02.dkhp.DAO.CourseDAO;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.FacultyDAO;
import uit.cnpm02.dkhp.model.Class;
import uit.cnpm02.dkhp.model.Course;
import uit.cnpm02.dkhp.model.Faculty;
import uit.cnpm02.dkhp.utilities.Constants;

/**
 *
 * @author LocNguyen
 */
@WebServlet(name = "AccountController", urlPatterns = {"/AccountController"})
public class AccountController extends HttpServlet {

    private AccountDAO accDao = DAOFactory.getAccountDao();
    private StudentDAO studentDao = DAOFactory.getStudentDao();
    private ClassDAO classDao = DAOFactory.getClassDao();
    private FacultyDAO facultyDao = DAOFactory.getFacultyDao();
    private CourseDAO courseDao = DAOFactory.getCourseDao();

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
        String action = request.getParameter("action");
        try {
            if (action.equalsIgnoreCase("changePass")) {
                changePass(request, response, session);
            } else if (action.equalsIgnoreCase("Info")) {
                getInfo(response, session);
            } else if (action.equals("changeinfo")) {
                changeInfo(response, session);
            } else if (action.equalsIgnoreCase("update")) {
                updateInfo(request, response, session);
            } else if (action.equalsIgnoreCase("manager")) {
                listAccount(request, response, session);
            } else if (action.equalsIgnoreCase("createnew")) {
                createNewAccount(request, response, session);
            } else if (action.equalsIgnoreCase("editaccount")) {
                editAccount(request, response, session);
            } else if (action.equalsIgnoreCase("deleteaccount")) {
                deleteAccount(request, response, session);
            } else if (action.equalsIgnoreCase("search")) {
                search(request, response, session);
            }

        } catch (Exception ex) {
            Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }

    }

    private void updateInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        String user = (String) session.getAttribute("username");
        Student student = studentDao.findById(user);
        String IndentityCard = request.getParameter("IdentityCard");
        String gender = request.getParameter("gender");
        String home = request.getParameter("home");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date birthday = df.parse(request.getParameter("birthday"));
        Date startDate = df.parse(request.getParameter("startdate"));
        student.setIdentityNumber(IndentityCard);
        student.setGender(gender);
        student.setHomeAddr(home);
        student.setAddress(address);
        student.setPhone(phone);
        student.setBirthday(birthday);
        student.setDateStart(startDate);
        studentDao.update(student);
        getInfo(response, session);
    }

    /**
     * 
     * @param response
     * @param session
     * @throws Exception 
     */
    private void changeInfo(HttpServletResponse response, HttpSession session) throws Exception {
        String path = "./jsps/SinhVien/UpdateInfo.jsp";
        String user = (String) session.getAttribute("username");
        Student student = studentDao.findById(user);
        session.setAttribute("student", student);
        response.sendRedirect(path);
    }

    /**
     * 
     * @param response
     * @param session
     * @throws Exception 
     */
    private void getInfo(HttpServletResponse response, HttpSession session) throws Exception {
        String path = "./jsps/SinhVien/Info.jsp";
        String user = (String) session.getAttribute("username");
        Student student = studentDao.findById(user);
        Class classes = classDao.findById(student.getClassCode());
        Faculty faculty = facultyDao.findById(student.getFacultyCode());
        Course course = courseDao.findById(student.getCourseCode());
        session.setAttribute("student", student);
        session.setAttribute("classes", classes);
        session.setAttribute("faculty", faculty);
        session.setAttribute("course", course);
        response.sendRedirect(path);
    }

    /**
     * 
     * @param request
     * @param response
     * @param session
     * @throws Exception 
     */
    private void changePass(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        String path = "./jsps/SinhVien/ChangePass.jsp";
        try {
            String user = (String) session.getAttribute("username");
            Account account = accDao.findById(user);
            String newPass = request.getParameter("newpass");
            account.setPassword(newPass);
            accDao.update(account);
            session.setAttribute("password", newPass);
            session.setAttribute("message", "Đổi mật khẩu thành công");
            response.sendRedirect(path);
        } catch (Exception ex) {
            session.setAttribute("message", "Đổi mật khẩu thất bại");
            response.sendRedirect(path);
        }
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

    private void listAccount(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        List<Account> accounts = accDao.findAll();
        session.setAttribute("account", accounts);

        String path = "./jsps/PDT/AccountManager.jsp";
        response.sendRedirect(path);
    }

    private void createNewAccount(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String userName = request.getParameter("txtUsername");
        String pwd = request.getParameter("txtPassword");
        String rePwd = request.getParameter("txtRePassword");
        String fullName = request.getParameter("txtFullName");
        String type = request.getParameter("selectType");

        String path = "./jsps/PDT/CreateNewAccount.jsp";
        session.removeAttribute("error");

        if ((userName == null) || (userName.isEmpty())
                || (pwd == null)
                || (rePwd == null)
                || (!pwd.equals(rePwd))
                || pwd.isEmpty()) {
            session.setAttribute("error", "Thông tin không hợp lệ");
        }

        session.setAttribute("error", "Tạo tài khoản thành công");
        try {
            Account acc = new Account(userName, pwd, fullName, false, "Bình Thường", getType(type));

            accDao.add(acc);
        } catch (Exception ex) {
            session.setAttribute("error", "Đã có lỗi xảy ra.");
        }

        response.sendRedirect(path);
    }

    private void editAccount(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        String path = "./jsps/PDT/EditAccount.jsp";
        session.removeAttribute("error");
        String userName = request.getParameter("username");
        if ((userName != null) && (!userName.isEmpty())) {
            try {
                //Prepare data for edit.
                Account acc = accDao.findById(userName);
                if (acc != null) {
                    session.setAttribute("account", acc);
                }
            } catch (Exception ex) {
                session.setAttribute("error", "Đã có lỗi xảy ra.");
            }
        } else {

            userName = request.getParameter("txtUsername");
            String pwd = request.getParameter("txtPassword");
            String rePwd = request.getParameter("txtRePassword");
            String fullName = request.getParameter("txtFullName");
            String type = request.getParameter("selectType");
            String status = request.getParameter("selectStatus");

            if ((userName == null) || (userName.isEmpty())
                    || (pwd == null)
                    || (rePwd == null)
                    || (!pwd.equals(rePwd))
                    || pwd.isEmpty()) {

                session.setAttribute("error", "Thông tin không hợp lệ");
            }

            session.setAttribute("error", "Cập nhật thành công");
            try {
                Account acc = new Account(userName, pwd, fullName, false, status, getType(type));
                accDao.update(acc);
            } catch (Exception ex) {
                session.setAttribute("error", "Đã có lỗi xảy ra.");
            }
        }
        response.sendRedirect(path);
    }

    private void deleteAccount(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        String path = "./jsps/PDT/AccountManager.jsp";
        session.removeAttribute("error");
        String userName = request.getParameter("username");
        if ((userName != null) && (!userName.isEmpty())) {
            try {
                //Prepare data for edit.
                Account acc = accDao.findById(userName);

                if (acc != null) {
                    accDao.delete(acc);
                }

            } catch (Exception ex) {
                session.setAttribute("error", "Đã có lỗi xảy ra.");
            }
            session.setAttribute("error", "Xóa thành công.");

            listAccount(request, response, session);
        }
    }

    private void search(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        String path = "./jsps/PDT/AccountManager.jsp";
        session.removeAttribute("error");
        String search = request.getParameter("txtSearch");

        if ((search == null) || (search.isEmpty()) || (search.equals("*")) || (search.equalsIgnoreCase("all"))) {
            listAccount(request, response, session);
        } else {
            if ((search != null) && (!search.isEmpty())) {
                try {
                    List<Account> acc = accDao.search(search);
                    if (acc != null) {
                        session.setAttribute("account", acc);
                        response.sendRedirect(path);
                    }
                } catch (Exception ex) {
                }
            }
        }
        response.sendRedirect(path);
    }

    private int getType(String typeDescription) {
        if ((typeDescription.equals("PDT")) || (typeDescription.equals("PĐT"))) {
            return Constants.ACCOUNT_TYPE_PDT;
        } else if ((typeDescription.equals("Giang Vien")) || (typeDescription.equals("Giảng Viên"))) {
            return Constants.ACCOUNT_TYPE_LECTURE;
        } else if ((typeDescription.equals("Sinh Vien")) || (typeDescription.equals("Sinh Viên"))) {
            return Constants.ACCOUNT_TYPE_STUDENT;
        }

        return -1;
    }
}
