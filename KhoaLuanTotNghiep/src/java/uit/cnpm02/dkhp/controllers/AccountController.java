package uit.cnpm02.dkhp.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uit.cnpm02.dkhp.DAO.AccountDAO;
import uit.cnpm02.dkhp.bo.AccountBO;
import uit.cnpm02.dkhp.model.Account;

/**
 *
 * @author LocNguyen
 */
@WebServlet(name = "AccountController", urlPatterns = {"/AccountController"})
public class AccountController extends HttpServlet {
    private AccountDAO accDao = new AccountDAO();

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
                    Login(session, request, response);
                } else if (function.equals("logout")) {
                    LogOut(session, response);
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
    private void Login(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String user = request.getParameter("txtUsername");
        String pass = request.getParameter("txtPassword");
        AccountBO accBo = new AccountBO();
        
        String path = "./HomepageController";
        if (accBo.Login(user, pass)) {
            if (accBo.isLogined(user)) {
                session.setAttribute("error", "Tài khoản của bạn đang được đăng nhập ở một máy khác!");
                path = "./jsps/Login.jsp";
            } else if (accBo.isLock(user)) {
                session.setAttribute("error", "Tài khoản của bạn đang bị khóa vui lòng liên hệ quản lý khoa để giải quyết!");
                path = "./jsps/Login.jsp";
            } else {
                session.setAttribute("username", user);
                session.setAttribute("password", pass);
                
                //Set to logined.
                Account acc = accDao.findById(user);
                acc.setIsLogined(true);
                accDao.update(acc);
            }
        } else {
            session.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không hợp lệ");
            path = "./jsps/Login.jsp";
        }

        response.sendRedirect(path);
    }

    private void LogOut(HttpSession session, HttpServletResponse response) throws IOException, Exception {
        String user=(String) session.getAttribute("username");
        Account acc = accDao.findById(user);
        
        if(acc != null) {
            acc.setIsLogined(false);
            accDao.update(acc);
        }

        session.removeAttribute("username");
        session.removeAttribute("student");
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
}
