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
import uit.cnpm02.dkhp.DAO.LecturerDAO;
import uit.cnpm02.dkhp.DAO.SubjectDAO;
import uit.cnpm02.dkhp.DAO.TrainClassDAO;
import uit.cnpm02.dkhp.model.Lecturer;
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
private void setSubjectAndLecturer(List<TrainClass> trainClass) throws Exception{
    SubjectDAO subjectDao=new SubjectDAO();
    LecturerDAO lecturerDao=new LecturerDAO();
    for(int i=0;i<trainClass.size();i++){
        trainClass.get(i).setSubjectName(subjectDao.findById(trainClass.get(i).getSubjectCode()).getSubjectName());
        trainClass.get(i).setLectturerName(lecturerDao.findById(trainClass.get(i).getLecturerCode()).getFullName());
    }
}
private void getSchedule(HttpServletResponse response, HttpSession session) throws IOException{
    String path=""; 
      try{
            session.setAttribute("year", Constants.CURRENT_YEAR);
            session.setAttribute("semester", Constants.CURRENT_SEMESTER);
            List<TrainClass> monday=trainClassDao.findAllByStudyDate(Constants.MONDAY);
            setSubjectAndLecturer(monday);
            List<TrainClass> tuesday=trainClassDao.findAllByStudyDate(Constants.TUESDAY);
            setSubjectAndLecturer(tuesday);
            List<TrainClass> wednesday=trainClassDao.findAllByStudyDate(Constants.WEDNESDAY);
            setSubjectAndLecturer(wednesday);
            List<TrainClass> thursday=trainClassDao.findAllByStudyDate(Constants.THURSDAY);
            setSubjectAndLecturer(thursday);
            List<TrainClass> friday=trainClassDao.findAllByStudyDate(Constants.FRIDAY);
            setSubjectAndLecturer(friday);
            List<TrainClass> saturday=trainClassDao.findAllByStudyDate(Constants.SATURDAY);
            setSubjectAndLecturer(saturday);
            session.setAttribute("monday", monday);
            session.setAttribute("tuesday", tuesday);
            session.setAttribute("wednesday", wednesday);
            session.setAttribute("thursday", thursday);
            session.setAttribute("friday", friday);
            session.setAttribute("saturday", saturday);
            path= "./jsps/SinhVien/Schedule.jsp";
       }catch(Exception ex){
           path= "./jsps/Message.jsp";
       }
        response.sendRedirect(path);
}

 /**
 * 
 * @param request
 * @param response
 * @throws ServletException
 * @throws IOException 
 */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * 
     * @return 
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
