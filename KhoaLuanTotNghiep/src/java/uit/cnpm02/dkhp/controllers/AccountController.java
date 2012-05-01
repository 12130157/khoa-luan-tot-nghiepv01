package uit.cnpm02.dkhp.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import uit.cnpm02.dkhp.utilities.DateTimeUtil;
import uit.cnpm02.dkhp.utilities.Log;
import uit.cnpm02.dkhp.utilities.StringUtils;
import uit.cnpm02.dkhp.utilities.password.PasswordProtector;

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
    private int numpage = 0;
    
    private List<Account> accounts = new ArrayList<Account>(10);

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
                session.setAttribute("numpage", getNumberPage());
                listAccount(request, response, session);
            } else if (action.equalsIgnoreCase("createnew")) {
                session.setAttribute("numpage", getNumberPage());
                createNewAccount(request, response, session);
            } else if (action.equalsIgnoreCase("editaccount")) {
                editAccount(request, response, session);
            } else if (action.equalsIgnoreCase("deleteaccount")) {
                session.setAttribute("numpage", getNumberPage());
                deleteAccount(request, response, session);
            } else if (action.equalsIgnoreCase("search")) {
                search(request, response, session);
            } else if (action.equalsIgnoreCase("Filter")) {
                filterAccount(request, response);
            }

            //
        } catch (Exception ex) {
            Logger.getLogger(AccountController.class.getName())
                    .log(Level.SEVERE, null, ex);
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
        Date birthday = DateTimeUtil.parse(request.getParameter("birthday"));
        Date startDate = DateTimeUtil.parse(request.getParameter("startdate"));
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
            String newTxtPass = request.getParameter("newpass");
            String newPass = PasswordProtector.getMD5(newTxtPass);
            account.setPassword(newPass);
            accDao.update(account);
            session.setAttribute("password", newPass);
            session.setAttribute("messageChanPass", "Đổi mật khẩu thành công");
            response.sendRedirect(path);
        } catch (Exception ex) {
            session.setAttribute("messageChanPass", "Đổi mật khẩu thất bại");
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

    private void listAccount(HttpServletRequest request,
                HttpServletResponse response, HttpSession session)
                throws Exception {
        int currentPage = 1;
        try {
            currentPage = Integer.parseInt(request.getParameter("curentPage"));
        } catch (Exception ex) {
            currentPage = 1;
        }

        accounts = accDao.findAll(Constants.ELEMENT_PER_PAGE_DEFAULT,
                currentPage, "TenDangNhap", "DESC");
        
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
        String error = "Tạo tài khoản thành công";

        if ((userName == null) || (userName.isEmpty())
                || (pwd == null)
                || (rePwd == null)
                || (!pwd.equals(rePwd))
                || pwd.isEmpty()) {
            error = "Thông tin không hợp lệ";
        }


        try {
            String safePass = PasswordProtector.getMD5(pwd);
            Account acc = new Account(userName, safePass, fullName,
                    false, AccounType.NORMAL.value(), getType(type));
            accDao.add(acc);

            String editor = (String) session.getAttribute("logineduser");

            Log.getInstance().log(editor, "Tạo mới tài khoản " + userName);
        } catch (Exception ex) {
            error = "Đã có lỗi xảy ra. " + ex.toString();
        }

        session.setAttribute("error", error);
        response.sendRedirect(path);
    }

    private void editAccount(HttpServletRequest request,
            HttpServletResponse response, HttpSession session) throws IOException {
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
            int status =Integer.parseInt(request.getParameter("selectStatus"));

            if ((userName == null) || (userName.isEmpty())
                    || (pwd == null)
                    || (rePwd == null)
                    || (!pwd.equals(rePwd))
                    || pwd.isEmpty()) {

                session.setAttribute("error", "Thông tin không hợp lệ");
            }

            session.setAttribute("error", "Cập nhật thành công");
            try {
                if ((pwd != null) && !pwd.isEmpty()) {
                    pwd = PasswordProtector.getMD5(pwd);
                }
                Account acc = new Account(userName, pwd, fullName, false, status, getType(type));
                accDao.update(acc);

                String editor = (String) session.getAttribute("logineduser");
                Log.getInstance().log(editor, "Cập nhật thông tin tài khoản " + userName);
            } catch (Exception ex) {
                session.setAttribute("error", "Đã có lỗi xảy ra.");
            }
        }
        response.sendRedirect(path);
    }

    private void deleteAccount(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        session.removeAttribute("error");
        String userName = request.getParameter("username");
        if ((userName != null) && (!userName.isEmpty())) {
            try {
                //Prepare data for edit.
                Account acc = accDao.findById(userName);

                if (acc != null) {
                    accDao.delete(acc);
                    //logineduser
                    String editor = (String) session.getAttribute("logineduser");
                    Log.getInstance().log(editor, "Xóa tài khoản " + userName);
                }

            } catch (Exception ex) {
            }
            //session.setAttribute("error", "Xóa thành công.");
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

    private void filterAccount(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        int currentPage = 1;
        try {
            currentPage = Integer.parseInt(request.getParameter("curentPage"));
        } catch (Exception ex) {
        }
        List<Account> accounts = accDao.findAll(Constants.ELEMENT_PER_PAGE_DEFAULT, currentPage, "TenDangNhap", "DESC");

        if ((accounts != null) && (!accounts.isEmpty())) {
            out.println("<tr>"
                    + "<th>STT</th>"
                    + "<th>Tên đăng nhập</th>"
                    + "<th>Họ tên NSD</th>"
                    + "<th>Tình Trạng</th>"
                    + "<th>Loại Tài khoản</th>"
                    + "<th>Sửa</th>"
                    + "<th>Xóa</th>"
                    + "</tr>");

            for (int i = 0; i < accounts.size(); i++) {
                StringBuffer str = new StringBuffer();
                str.append("<tr><td>").append((currentPage - 1) * 10 + 1 + i).append("</td>");
                str.append("<td>").append(accounts.get(i).getId()).append("</td>");
                str.append("<td>").append(accounts.get(i).getFullName()).append("</td>");
                str.append("<td>").append(accounts.get(i).getStatus()).append("</td>");
                String type = StringUtils.getAccountTypeDescription(accounts.get(i).getType());
                str.append("<td> ").append(type).append("</td>");

                str.append("<td><a href='../../AccountController?action=editaccount&username=").append(accounts.get(i).getId()).append("'>Sửa</a></td>");
                str.append("<td><a href='../../AccountController?action=deleteaccount&username=").append(accounts.get(i).getId()).append("'>Xóa</a></td>");
                out.println(str.toString());
            }
        }
    }

    private int getNumberPage() throws Exception {
        int rows = accDao.getRowsCount();

        int rowsPerPage = Constants.ELEMENT_PER_PAGE_DEFAULT;
        if (rows % rowsPerPage == 0) {
            numpage = rows / rowsPerPage;
        } else {
            numpage = rows / rowsPerPage + 1;
        }
        return numpage;
    }
    
    public enum AccounType {
        NORMAL(0, "Bình thường"),
        LOCK(1, "Bị khóa");
        
        private int value;
        private String description;
        
        AccounType(int value, String description) {
            this.value = value;
            this.description = description;
        }
        
        public int value() {
            return this.value;
        }
        
        public String description() {
            return this.description;
        }
    }
}
