package uit.cnpm02.dkhp.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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
import uit.cnpm02.dkhp.DAO.NewsDAO;
import uit.cnpm02.dkhp.DAO.TaskDAO;
import uit.cnpm02.dkhp.model.Account;
import uit.cnpm02.dkhp.model.News;
import uit.cnpm02.dkhp.model.Task;
import uit.cnpm02.dkhp.model.type.AccountType;
import uit.cnpm02.dkhp.model.type.NewsType;
import uit.cnpm02.dkhp.model.type.TaskStatus;
import uit.cnpm02.dkhp.service.IPDTService;
import uit.cnpm02.dkhp.service.impl.PDTServiceImpl;
import uit.cnpm02.dkhp.utilities.Constants;
import uit.cnpm02.dkhp.utilities.ExecuteResult;
import uit.cnpm02.dkhp.utilities.StringUtils;
import uit.cnpm02.dkhp.utilities.password.PasswordProtector;

/**
 *
 * @author LocNguyen
 */
@WebServlet(name = "HomepageController", urlPatterns = {"/HomepageController"})
public class HomepageController extends HttpServlet {
    
    private IPDTService pdtService = new PDTServiceImpl();
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        /**
         * Check if !Login
         * return ...
         */
        PrintWriter out = response.getWriter();
        String path = "./jsps/StartUp.jsp";
        String actor = request.getParameter("actor");

        //updatePassword();
        
        if (session.isNew()) {
            session.setMaxInactiveInterval(1200);
        }
        
        try {
            NewsDAO newsDao = new NewsDAO();
            //List<News> news = newsDao.findAll();
            List<News> news = newsDao.findByColumName("Loai", NewsType.IMPORTANT.value());
            news.addAll(newsDao.findByColumName("Loai", NewsType.OPEN.value()));
            
            if((news != null) && (news.size() > 0)) {
                session.setAttribute("news", news);
            }
            if (actor.equalsIgnoreCase("All")) {
                path = "./jsps/StartUp.jsp";
            } else if (actor.equalsIgnoreCase("Student")) {
                path = "./jsps/SinhVien/SVStart.jsp";
            } else if (actor.equalsIgnoreCase("PDT")) {
                path = "./jsps/PDT/PDTStart.jsp";
            } else if (actor.equalsIgnoreCase("Lecturer")) {
                path = "./jsps/GiangVien/GVStart.jsp";
            } else if (actor.equalsIgnoreCase("notify-task-to-student")) {
                processTask(request, response);
                return;
            } else if (actor.equalsIgnoreCase("hide-task")) {
                processHideTask(request, response);
                return;
            }
            response.sendRedirect(path);
        } catch (Exception ex) {
            Logger.getLogger(HomepageController.class.getName())
                    .log(Level.SEVERE, null, ex);

        } finally {
            out.close();
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
        return "This servlet controller data to send to Homepage of website";
    }// </editor-fold>

    private void updatePassword() {
        AccountDAO accountDao = DAOFactory.getAccountDao();
        try {
            List<Account> accounts = accountDao.findAll();
            if ((accounts != null) && !accounts.isEmpty()) {
                for (Account a : accounts) {
                    String pwd = a.getPassword();
                    if ((pwd != null)
                            && !pwd.isEmpty()
                            && (pwd.length() < 30)) {
                        String safePass = PasswordProtector.getMD5(pwd);
                        a.setPassword(safePass);
                        accountDao.update(a);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(HomepageController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    private void processTask(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String task = request.getParameter("taskid");
        PrintWriter out = response.getWriter();
        if (StringUtils.isEmpty(task)) {
            out.println("error - : Không tìm thấy thông báo");
            return;
        }
        int taskId = 0;
        try {
            taskId = Integer.parseInt(task);
        } catch (Exception ex) {
            out.println("error - : Lỗi server " + ex.toString());
            return;
        }
        
        ExecuteResult er = pdtService.processTask(taskId);
        if (!er.isIsSucces()) {
            out.println(er.getMessage());
            return;
        }
        
        List<Task> tasks = getRemindTask(request);
        writeOutListTasks(out, tasks);
    }

    private List<Task> getRemindTask(HttpServletRequest request) {
        TaskDAO taskDao = DAOFactory.getTaskDAO();
        HttpSession session = request.getSession();
        session.removeAttribute("tasks");
        String username = "";
        String generalUsername = "";
        try {
            username = (String) session.getAttribute("username");
            AccountDAO accDao = DAOFactory.getAccountDao();
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
            return tasks;
        } catch (Exception ex) {
            Logger.getLogger(HomepageController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void writeOutListTasks(PrintWriter out, List<Task> tasks) {
        if ((tasks != null) && !tasks.isEmpty()) {
            return;
        }
        out.println("<u><b>Tin quan trọng:</b></u>");
        out.println("<table class=\"general-table important-table\">");
        out.println("<tr>"
                        + "<th> STT </th><th> Nội dung </th><th> Người gửi </th><th> Ngày gửi </th>"
                    + "</tr>");
        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_PARTERM_DEFAULT);
            out.println("<tr>"
                        + "<td>" + (i + 1) + "</td>"
                        + "<td>" + t.getContent()
                            + "<div class=\"pop-up\">"
                                + "<span class=\"atag\" onclick=\"accept(" + t.getId() + ")\"> Chấp nhận </span>"
                                + "<span class=\"atag\" onclick=\"reject(" + t.getId() + ")\"> Không chấp nhận </span>"
                            + "</div>"
                        + "</td>"
                        + "<td>" + t.getSender() + "</td>"
                        + "<td>" + sdf.format(t.getCreated()) + "</td>"
                    + "</tr>");
        }
        out.println("</table>");
    }

    private void processHideTask(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String task = request.getParameter("taskid");
        PrintWriter out = response.getWriter();
        if (StringUtils.isEmpty(task)) {
            out.println("error - : Không tìm thấy thông báo");
            return;
        }
        int taskId = 0;
        try {
            taskId = Integer.parseInt(task);
        } catch (Exception ex) {
            out.println("error - : Lỗi server " + ex.toString());
            return;
        }
        
        ExecuteResult er = pdtService.hideTask(taskId);
        if (!er.isIsSucces()) {
            out.println(er.getMessage());
            return;
        }
        
        List<Task> tasks = getRemindTask(request);
        writeOutListTasks(out, tasks);
    }
}
