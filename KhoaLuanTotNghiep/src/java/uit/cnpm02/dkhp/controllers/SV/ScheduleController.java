/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.controllers.SV;

import com.sun.corba.se.impl.orbutil.closure.Constant;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uit.cnpm02.dkhp.DAO.TrainClassDAO;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.utilities.Constants;

/**
 *
 * @author thanh
 */
@WebServlet(name = "ScheduleController", urlPatterns = {"/ScheduleController"})
public class ScheduleController extends HttpServlet {
TrainClassDAO trainClassDao=new TrainClassDAO();
   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
         HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        try {
            String action=request.getParameter("action");
            if(action.equalsIgnoreCase("view"))
                getSchedule(response, session);
        } finally {            
            out.close();
        }
    }
private void getSchedule(HttpServletResponse response, HttpSession session) throws IOException{
    String path=""; 
      try{
            session.setAttribute("year", Constants.CURRENT_YEAR);
            session.setAttribute("semester", Constants.CURRENT_SEMESTER);
           // List<TrainClass> monday=trainClassDao.findAllByStudyDate(Constants.MONDAY);
            List<TrainClass> monday=trainClassDao.findAll();
            //List<TrainClass> tuesday=trainClassDao.findAllByStudyDate(Constants.TUESDAY);
            //List<TrainClass> wednesday=trainClassDao.findAllByStudyDate(Constants.WEDNESDAY);
           // List<TrainClass> thursday=trainClassDao.findAllByStudyDate(Constants.THURSDAY);
           // List<TrainClass> friday=trainClassDao.findAllByStudyDate(Constants.FRIDAY);
           // List<TrainClass> saturday=trainClassDao.findAllByStudyDate(Constants.SATURDAY);
            session.setAttribute("monday", monday);
            //session.setAttribute("tuesday", tuesday);
            //session.setAttribute("wednesday", wednesday);
            //session.setAttribute("thursday", thursday);
            //session.setAttribute("friday", friday);
            //session.setAttribute("saturday", saturday);
            path= "./jsps/SinhVien/Schedule.jsp";
       }catch(Exception ex){
           path= "./jsps/Message.jsp";
       }
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
