/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.controllers.PDT;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.model.RegistrationTime;
import uit.cnpm02.dkhp.model.RegistrationTimeID;
import uit.cnpm02.dkhp.utilities.Constants;
import uit.cnpm02.dkhp.utilities.DateTimeUtil;

/**
 *
 * @author thanh
 */
@WebServlet(name = "TimeController", urlPatterns = {"/TimeController"})
public class TimeController extends HttpServlet {

    /** 
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
           String action = request.getParameter("action");
           if(action.equalsIgnoreCase("default"))
               showTimeRegistration(request, response);
           else if(action.equalsIgnoreCase("pre_update"))
              preUpdateRegistrationTime(request, response);
           else if(action.equalsIgnoreCase("update"))
              updateRegistrationTime(request, response);
        } finally {            
            out.close();
        }
    }
    private void updateRegistrationTime(HttpServletRequest request, HttpServletResponse response) throws IOException{
       String path="";
        try {
        Date startDate = DateTimeUtil.parse(request.getParameter("startDate"));
        Date endDate = DateTimeUtil.parse(request.getParameter("endDate"));
        RegistrationTimeID id = new RegistrationTimeID(Constants.CURRENT_SEMESTER, Constants.CURRENT_YEAR);
        RegistrationTime regisTime = new RegistrationTime(id, startDate, endDate);
        regisTime = DAOFactory.getRegistrationTimeDAO().update(regisTime);
        HttpSession session = request.getSession();
        session.setAttribute("registrationTime", regisTime);
        path="./jsps/PDT/RegistrationTime.jsp";
        response.sendRedirect(path);
        } catch (Exception ex) {
            path= "./jsps/Message.jsp";
           response.sendRedirect(path);
        }
    }
    private void preUpdateRegistrationTime(HttpServletRequest request, HttpServletResponse response) throws IOException{
        RegistrationTimeID id = new RegistrationTimeID(Constants.CURRENT_SEMESTER, Constants.CURRENT_YEAR);
        String path="";
        try {
            RegistrationTime registrationTime = DAOFactory.getRegistrationTimeDAO().findById(id);
            HttpSession session = request.getSession();
            session.setAttribute("registrationTime", registrationTime);
            path="./jsps/PDT/ChangeRegistrationTime.jsp";
            response.sendRedirect(path);
        } catch (Exception ex) {
            path= "./jsps/Message.jsp";
           response.sendRedirect(path);
        }
    }
    private void showTimeRegistration(HttpServletRequest request, HttpServletResponse response) throws IOException{
        RegistrationTimeID id = new RegistrationTimeID(Constants.CURRENT_SEMESTER, Constants.CURRENT_YEAR);
        String path="";
        try {
            RegistrationTime registrationTime = DAOFactory.getRegistrationTimeDAO().findById(id);
            HttpSession session = request.getSession();
            session.setAttribute("registrationTime", registrationTime);
            path="./jsps/PDT/RegistrationTime.jsp";
            response.sendRedirect(path);
        } catch (Exception ex) {
            path= "./jsps/Message.jsp";
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
}
