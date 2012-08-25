package uit.cnpm02.dkhp.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.EnumSet;
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
import uit.cnpm02.dkhp.model.type.AccountStatus;
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
    
    private final int numPerPage = Constants.ELEMENT_PER_PAGE_DEFAULT;
    

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
            } else if (action.equals("update")) {
                changeInfo(request, response);
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
                doSearch(request, response);
            } else if (action.equalsIgnoreCase(AccountSupport
                                        .SORT.description())) {
                doSortAccount(request, response);
            }else if(action.equalsIgnoreCase(AccountSupport
                                        .FILTER.description())){
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

    private void filterAccount(HttpServletRequest request,
            HttpServletResponse response) {
        PrintWriter out = null;
        int numpage = 0;
        int currentPage = Integer.parseInt(request.getParameter("curentPage"));
        String key = request.getParameter("key");
        List<Account> result = new ArrayList<Account>(numPerPage);
        HttpSession session = request.getSession();
        try {
            out = response.getWriter();
            if (key.isEmpty()) {
                result = accDao.findAll(numPerPage, currentPage, "TenDangNhap", "DESC");
            } else {
                List<Account> acc = accountService.search(key, session.getId());
                if ((acc.size() % numPerPage) == 0) {
                    numpage = acc.size() / numPerPage;
                } else {
                    numpage = acc.size() / numPerPage + 1;
                }
                if (currentPage > numpage) {
                    currentPage = numpage;
                }
                if (acc.size() <= numPerPage) {
                    result = acc;
                } else {
                    int beginIndex = (currentPage - 1) * numPerPage;
                    int endIndex = (currentPage - 1) * numPerPage + numPerPage;
                    for (int i = 0; i < acc.size(); i++) {
                        if (i >= beginIndex && i < endIndex) {
                            result.add(acc.get(i));
                        }
                    }
                }
            }
            writeOutListAccount(out, result, currentPage);
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
    private void changeInfo(HttpServletRequest request,
            HttpServletResponse response) {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            //String path = "./jsps/SinhVien/UpdateInfo.jsp";
            HttpSession session = request.getSession();
            String user = (String) session.getAttribute("username");
            Student student = studentDao.findById(user);
            if (student == null) {
                out.println("Cập nhật không thành công, login user null");
                return;
            }
            String cmnd = request.getParameter("IdentityCard");
            String home = request.getParameter("home");
            String address = request.getParameter("address");
            String phone = request.getParameter("phone");
            if (!StringUtils.isEmpty(cmnd)) {
                student.setIdentityNumber(cmnd);
            }
            if (!StringUtils.isEmpty(home)) {
                student.setHomeAddr(home);
            }
            if (!StringUtils.isEmpty(address)) {
                student.setAddress(address);
            }
            if (!StringUtils.isEmpty(phone)) {
                student.setPhone(phone);
            }
            studentDao.update(student);
            out.println("Cập nhật thông tin thành công");
            //session.setAttribute("student", student);
            //response.sendRedirect(path);
        } catch (Exception ex) {
            Logger.getLogger(AccountController.class.getName())
                    .log(Level.SEVERE, null, ex);
            out.println("Đã có lỗi xảy ra, vui lòng thử lại sau.");
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
    private void getInfo(HttpServletResponse response,
            HttpSession session) throws Exception {
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
    private void changePass(HttpServletRequest request,
            HttpServletResponse response, HttpSession session) throws Exception {
        //String path = "./jsps/SinhVien/ChangePass.jsp";
        PrintWriter out = response.getWriter();
        try {
            String user = (String) session.getAttribute("username");
            Account account = accDao.findById(user);
            if (account == null) {
                out.println("Đôi mật khẩu không thành công, login user null");
                return;
            }
                    
            String newTxtPass = request.getParameter("new_pwd");
            String oldTxtPass = request.getParameter("old_pwd");
            String newPass = PasswordProtector.getMD5(newTxtPass);
            String oldPass = PasswordProtector.getMD5(oldTxtPass);
            if (!oldPass.equals(account.getPassword())) {
                out.println("Mật khẩu hiện tại không đúng.");
                return;
            }
            
            account.setPassword(newPass);
            accDao.update(account);
            //session.setAttribute("password", newPass);
            //session.setAttribute("messageChanPass", "Đổi mật khẩu thành công");
            //response.sendRedirect(path);
            out.println("Đổi mật khẩu thành công");
        } catch (Exception ex) {
            //session.setAttribute("messageChanPass", "Đổi mật khẩu thất bại");
            //response.sendRedirect(path);
            out.println("Đôi mật khẩu không thành công. Vui lòng thử lại sau");
            Logger.getLogger(AccountController.class.getName())
                    .log(Level.SEVERE, null, ex);
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

    /////////
    private void doListAccount(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        int numpage = 1;
        List<Account> accs = accDao.findAll();
        int rows = accs.size();
        if ((rows % numPerPage) == 0) {
            numpage = rows / numPerPage;
        } else {
            numpage = rows / numPerPage + 1;
        }
        List<Account> result = accDao.findAll(numPerPage, 1, "TenDangNhap", "DESC");
        HttpSession session = request.getSession();
        session.setAttribute("accountList", result);
        session.setAttribute("numpage", numpage);
        String path = "./jsps/PDT/AccountManager.jsp";
        response.sendRedirect(path);
    }

    private int slideLimit= 15;
    private void writeOutListAccount(PrintWriter out, List<Account> accounts,
                                                            int currentPage) {
        if (accounts == null) {
            return;
        }
        //if (accounts.size() > slideLimit) {
           // out.println("<div id=\"sidebar\">");
        //}
        out.println("<table id=\"accountdetail\" name=\"accountdetail\" class=\"general-table\">");
        out.println("<tr>"
                    + "<th> STT </th>"
                    + "<th>  Tên đăng nhập </th>"
                    + "<th>  Họ tên </th>"
                    + "<th>  Tình trạng </th>"
                    + "<th>  Loại tài khoản </th>"
                    + "<th> Sửa </th>"
                    + "<th> Xóa </th>"
                + "</tr>");

        if (!accounts.isEmpty()) {
            for (int i = 0; i < accounts.size(); i++) {
                Account acc = accounts.get(i);
                String accountType =  AccountType.getDescription(acc.getType());
                String statusImg = "";
                if(acc.getStatus()== AccountStatus.NORMAL.value()){
                    statusImg = "<img src=\"../../imgs/icon/unlock.png\" title=\"Đang hoạt động\" alt=\"Bình thường\"/>";
                } else if(acc.getStatus()== AccountStatus.LOCKED.value()) {
                    statusImg = "<img src=\"../../imgs/icon/lock.png\" title=\"Đang bị khóa\" alt=\"Đang bị khóa\"/>";
                }
                
                String editImg = "<img src=\"../../imgs/icon/edit.png\" title=\"Sửa\" alt=\"Sửa\"/>";
                String deleteImg = "<img src=\"../../imgs/icon/delete.png\" title=\"Xóa\" alt=\"Xóa\"/>";
                         
                out.println("<tr>"
                        + "<td>" + ((currentPage-1)*numPerPage + 1 + i) + "</td>"
                        + "<td>" + acc.getId() + "</td>"
                        + "<td>" + acc.getFullName() + "</td>"
                        + "<td>" + statusImg + "</td>"
                        + "<td>" + accountType + "</td>"
                        + "<td>"
                            + " <a href=\"#edit-account-range\" onclick=\"loadDataToEdit('" + acc.getId() + "')\">" + editImg + "</span>"
                        + "</td>"
                        + "<td>"
                            + " <span class=\"atag\" onclick=\"deleteUser('" + acc.getId() + "')\">" + deleteImg + "</span>"
                        + "</td>"
                        + "</tr>");
            }
        }
        out.println("</table>");
        //if (accounts.size() > slideLimit) {
        // //   out.println("</div>");
        //}
     }

    private void doSearch(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        List<Account> result = new ArrayList<Account>(numPerPage);
        String key = request.getParameter("key");
        HttpSession session = request.getSession();
        List<Account> acc = accountService.search(key, session.getId());
        if (acc.size() > numPerPage) {
            for (int i = 0; i < numPerPage; i++) {
                result.add(acc.get(i));
            }
        } else {
            result = acc;
        }
        writeOutListAccount(response.getWriter(), result, 1);

    }

    private void doSortAccount(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String by = request.getParameter("by");
        List<Account> acc = accountService.sort(by,
                request.getSession().getId());

        writeOutListAccount(response.getWriter(), acc, 1);
    }

    private void doDeleteAccount(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String userName = request.getParameter("user");
        String key = request.getParameter("key");
        String session = request.getSession().getId();
        List<Account> result = new ArrayList<Account>(numPerPage);
        int currentPage = Integer.parseInt(request.getParameter("curentPage"));
        int numpage = 0;
        PrintWriter out = null;
        try {
            if (accountService.deleteAccountByID(userName)) {
                out = response.getWriter();
                if (StringUtils.isEmpty(key)) {
                    result = accDao.findAll(numPerPage,
                            currentPage, "TenDangNhap", "DESC");
                } else {
                    List<Account> acc = accountService.search(key, session);
                    if (acc.size() % numPerPage == 0) {
                        numpage = acc.size() / numPerPage;
                    } else {
                        numpage = acc.size() / numPerPage + 1;
                    }
                    if (currentPage > numpage) {
                        currentPage = numpage;
                    }
                    if (acc.size() <= numPerPage) {
                        result = acc;
                    } else {
                        int beginIndex = (currentPage - 1) * numPerPage;
                        int endIndex = (currentPage - 1) * numPerPage + numPerPage;
                        for (int i = 0; i < acc.size(); i++) {
                            if (i >= beginIndex && i < endIndex) {
                                result.add(acc.get(i));
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            result = null;
        }
        writeOutListAccount(out, result, 1);
    }

    private void doPreEditAccount(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String userName = request.getParameter("username");

        Account acc = accountService.findAccount(userName);
        /*if (acc != null) {
            HttpSession session = request.getSession();
            session.setAttribute("account", acc);

            String path = "./jsps/PDT/EditAccount.jsp";
            response.sendRedirect(path);
        }*/
        PrintWriter out = response.getWriter();
        writeEditAccountForm(out, acc);
    }
    
    private void writeEditAccountForm(PrintWriter out, Account acc) {
        out.print("<table style=\"width: 450px;\">");
        out.print("<tr>"
                    + "<td> Tên đăng nhập </td>"
                    + "<td> <input type=\"text\" name=\"txtUsername_edit\" id=\"txtUsername_edit\" readonly=\"readonly\" value=\""+ acc.getId() + "\" /></td>"
                + "</tr>");
        out.print("<tr>"
                    + "<td> Mật khẩu </td>"
                    + "<td><input type=\"password\" name=\"txtPassword_edit\" id=\"txtPassword_edit\" value=\"" +acc.getPassword() +"\" /> </td>"
                + "</tr>");
        out.print("<tr>"
                    + "<td> Xác nhận mật khẩu </td>"
                    + "<td><input type=\"password\" name=\"txtRePassword_edit\" id=\"txtRePassword_edit\" value=\"" + acc.getPassword() + "\" /></td>"
                + "</tr>");
        out.print("<tr>"
            + "<td> Họ tên </td>"
            + "<td><input type=\"text\" name=\"txtFullName_edit\" id=\"txtFullName_edit\" value=\""+ acc.getFullName() +"\" /></td>"
        + "</tr>");
        out.print("<tr>"
            + "<td> Loại tài khoản </td>"
            + "<td>");
        out.print("<select name=\"selectType_edit\" id=\"selectType_edit\" class=\"input-minwidth\">");
        for(AccountType at : EnumSet.allOf(AccountType.class)) {
            String selected = "";
            if (acc.getType() == at.value()) {
                selected="selected=\"selected\"";
            }
            int value = at.value();
            String description = at.description();
            out.print("<option "+ selected +" value=" + value + ">");
            out.print(description);
            out.print("</option>");
        }
        out.print("</select>");
        out.print("</td>"
        + "</tr>");
        out.print("<tr>"
            + "<td> Tình trạng </td>"
            + "<td>");
        out.print("<select name=\"selectStatus_edit\" id=\"selectStatus_edit\" class=\"input-minwidth\">");
        for(AccountStatus at : EnumSet.allOf(AccountStatus.class)) {
            String selected = "";
            if (acc.getType() == at.value()) {
                selected="selected=\"selected\"";
            }
            int value = at.value();
            String description = at.description();
            out.print("<option "+ selected +" value=" + value + ">");
            out.print(description);
            out.print("</option>");
        }
        out.print("</select>");
        out.print("</td>"
        + "</tr>");

        out.print("</table>");
        out.print("<br/>");
        out.print("<div class=\"button-1\"><span class=\"atag\" onclick=\"update()\" ><img src=\"../../imgs/check.png\" />Hoàn thành</span></div>");
        //out.print("<div style=\"float: left; padding-left: 220px;\"><input type=\"button\" onclick=\"update()\" value=\"Hoàn thành\" />");
        out.print("</div>");
	out.print("<div class=\"clear\"></div>");
	out.print("<br/>");
	out.print("<div id=\"respone-edit-area\" class=\"msg-response\"></div>");
    }

    private void doUpdateAccount(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        if (!validatePassword(request)) {
            out.println("Mật khẩu không khớp.");
            return;
        }

        Account account = getAccountFromRequest(request);
        String sessionId = request.getSession().getId();
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
        String sessionId = request.getSession().getId();
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
        String status = request.getParameter("status");
        String type = request.getParameter("type");

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
        FILTER("Filter"),
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
