package uit.cnpm02.dkhp.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import uit.cnpm02.dkhp.model.type.AccountType;
import uit.cnpm02.dkhp.service.IAccountService;
import uit.cnpm02.dkhp.service.impl.AccountServiceImpl;
import uit.cnpm02.dkhp.utilities.Constants;
import uit.cnpm02.dkhp.utilities.ExecuteResult;
import uit.cnpm02.dkhp.utilities.StringUtils;
import uit.cnpm02.dkhp.utilities.password.PasswordProtector;

/**
 *
 * @author LocNguyen
 */
@WebServlet(name = "AccountController", urlPatterns = {"/AccountController"})
public class AccountController extends HttpServlet {

    private IAccountService accountService = new AccountServiceImpl();

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
            session.setAttribute("numpage", getNumberPage());
            if (action.equalsIgnoreCase("changePass")) {
                changePass(request, response, session);
            } else if (action.equalsIgnoreCase("Info")) {
                getInfo(response, session);
            } else if (action.equals("changeinfo")) {
                changeInfo(response, session);
            } else if (action.equalsIgnoreCase(AccountSupport
                                        .DEFAULT.description())) {
                doListAccount(request, response);
            } else if (action.equalsIgnoreCase(AccountSupport
                                        .DELETE.description())) {
                doDeleteAccount(request, response);
            } else if (action.equalsIgnoreCase(AccountSupport
                                        .PRE_EDIT_ACCOUNT.description())) {
                doPreEditAccount(request, response);
            } else if (action.equalsIgnoreCase(AccountSupport
                                        .UPDATE_ACCOUNT.description())) {
                doUpdateAccount(request, response);
            } else if (action.equalsIgnoreCase(AccountSupport
                    .CREATE_NEW_ACC.description())) {
                doCreateNew(request, response);
            } else if (action.equalsIgnoreCase(AccountSupport
                    .SEARCH.description())) {
                doSsearch(request, response);
            } else if (action.equalsIgnoreCase(AccountSupport
                                        .SORT.description())) {
                doSortAccount(request, response);
            }
            //
        } catch (Exception ex) {
            Logger.getLogger(AccountController.class.getName())
                    .log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }
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

    /////////
    private void doListAccount(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        List<Account> accs = accountService.getAccount(
                                1, request.getSession().getId());

        HttpSession session = request.getSession();
        session.setAttribute("account", accs);
        String path = "./jsps/PDT/AccountManager.jsp";
        response.sendRedirect(path);
    }

    private int slideLimit= 15;
    private void writeOutListAccount(PrintWriter out, List<Account> accounts) {
        if (accounts == null) {
            return;
        }
        if (accounts.size() > slideLimit) {
            out.println("<div id=\"sidebar\">");
        }
        out.println("<table id=\"accountdetail\" name=\"accountdetail\" class=\"general-table\">");
        out.println("<tr>"
                    + "<th> STT </th>"
                    + "<th> <span class=\"atag\" onclick=\"sort('TenDangNhap')\"> Tên đăng nhập </span></th>"
                    + "<th> <span class=\"atag\" onclick=\"sort('HoTen')\"> Họ tên NSD </span></th>"
                    + "<th> <span class=\"atag\" onclick=\"sort('TinhTrang')\"> Tình trạng </span></th>"
                    + "<th> <span class=\"atag\" onclick=\"sort('Loai')\"> Loại tài khoản </span></th>"
                    + "<th> Sửa </th>"
                    + "<th> Xóa </th>"
                + "</tr>");

        if (!accounts.isEmpty()) {
            for (int i = 0; i < accounts.size(); i++) {
                Account a = accounts.get(i);
                String accountType =  AccountType.getDescription(a.getType());
                // StringUtils.getAccountTypeDescription(a.getType());
                out.println("<tr>"
                        + "<td>" + (i + 1) + "</td>"
                        + "<td>" + a.getId() + "</td>"
                        + "<td>" + a.getFullName() + "</td>"
                        + "<td>" + a.getStatus() + "</td>"
                        + "<td>" + accountType + "</td>"
                        + "<td> <a href=\"../../AccountController?action=editaccount&username=" + a.getId() + "\">Sửa</a> </td>"
                        + "<td> <span class=\"atag\" onclick=\"deleteUser('" + a.getId() + "')\">Xóa</span></td>"
                        + "</tr>");
            }
        }
        out.println("</table>");
        if (accounts.size() > slideLimit) {
            out.println("</div>");
        }
     }

    private void doSsearch(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String key = request.getParameter("key");
        HttpSession session = request.getSession();
        List<Account> acc = accountService.search(key, session.getId());

        writeOutListAccount(response.getWriter(), acc);

    }

    private void doSortAccount(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String by = request.getParameter("by");
        List<Account> acc = accountService.sort(by,
                        request.getSession().getId());

        writeOutListAccount(response.getWriter(), acc);
    }

    private void doDeleteAccount(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String userName = request.getParameter("user");
        String session = request.getSession().getId();
        ExecuteResult er = accountService.deleteAccount(userName, session);

        PrintWriter out = response.getWriter();
        if (!er.isIsSucces()) {
            out.println("error " + er.getMessage());
        } else {
            List<Account> acc = accountService.getAccount(session);
            writeOutListAccount(out, acc);
        }
    }

    private void doPreEditAccount(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String userName = request.getParameter("username");

        Account acc = accountService.findAccount(userName);
        if (acc != null) {
            HttpSession session = request.getSession();
            session.setAttribute("account", acc);

            String path = "./jsps/PDT/EditAccount.jsp";
            response.sendRedirect(path);
        }
    }

    private void doUpdateAccount(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        
        if (!validatePassword(request)) {
            out.println("Mật khẩu không khớp.");
            return;
        }
        
        Account account = getAccountFromRequest(request);
        String sessionId =  request.getSession().getId();
        ExecuteResult er = accountService.update(
                    account, sessionId);
        if (!er.isIsSucces()) {
            out.println(er.getMessage());
        } else {
            out.println("Cập nhật thông tin thành công.");
        }
    }

    private void doCreateNew(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        
        if (!validatePassword(request)) {
            out.println("Mật khẩu không khớp.");
            return;
        }
        
        Account account = getAccountFromRequest(request);
        String sessionId =  request.getSession().getId();
        ExecuteResult er = accountService.createNew(
                    account, sessionId);
        if (!er.isIsSucces()) {
            out.println(er.getMessage());
        } else {
            out.println("Tạo mới thành công.");
        }
    }
    
    private boolean validatePassword(HttpServletRequest request) {
        String pass = request.getParameter("password");
        String rePass = request.getParameter("repassword");
        if (StringUtils.isEmpty(pass) 
                || (StringUtils.isEmpty(rePass))) {
            return false;
        }
        
        return pass.equals(rePass);
    }
    
    private Account getAccountFromRequest(HttpServletRequest request) {
        String userName = request.getParameter("username");
        String pass = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String status = request.getParameter("type");
        String type = request.getParameter("status");
        
        int int_status = Integer.parseInt(status);
        int int_type = Integer.parseInt(type);
        Account account = new Account(
                userName, pass, fullName, false, int_status, int_type);
        account.setId(userName);
        
        return account;
    }

    //#############################################
    public enum AccountSupport {
        DEFAULT("manager"),
        SEARCH("search"),
        SORT("sort"),
        DELETE("delete"),
        PRE_EDIT_ACCOUNT("editaccount"),
        UPDATE_ACCOUNT("update-account"),
        CREATE_NEW_ACC("create-new");

        private String description;
        AccountSupport(String description) {
            this.description = description;
        }

        public String description() {
            return description;
        }
    }
}
