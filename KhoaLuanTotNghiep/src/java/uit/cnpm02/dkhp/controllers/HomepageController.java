package uit.cnpm02.dkhp.controllers;

import java.io.IOException;
import java.io.PrintWriter;
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
import uit.cnpm02.dkhp.model.Account;
import uit.cnpm02.dkhp.model.News;
import uit.cnpm02.dkhp.service.SessionManager;
import uit.cnpm02.dkhp.utilities.password.PasswordProtector;

/**
 *
 * @author LocNguyen
 */
@WebServlet(name = "HomepageController", urlPatterns = {"/HomepageController"})
public class HomepageController extends HttpServlet {

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
        String path="";
        String actor=request.getParameter("actor") ;

        //updatePassword();
        
        try {
            NewsDAO newsDao = new NewsDAO();
            List<News> news = newsDao.findAll();
            
            if((news != null) && (news.size() > 0)) {
                session.setAttribute("news", news);
            }
            if(actor.equalsIgnoreCase("All"))
            path = "./jsps/StartUp.jsp";
            else if(actor.equalsIgnoreCase("Student"))
                path = "./jsps/SinhVien/SVStart.jsp";
            else if(actor.equalsIgnoreCase("PDT"))
                path = "./jsps/PDT/PDTStart.jsp";
            else if(actor.equalsIgnoreCase("Lecturer"))
                 path = "./jsps/GiangVien/GVStart.jsp";
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

}
