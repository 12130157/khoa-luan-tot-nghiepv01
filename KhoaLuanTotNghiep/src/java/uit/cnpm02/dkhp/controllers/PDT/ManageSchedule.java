/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.controllers.PDT;
import java.util.List;
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
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.LecturerDAO;
import uit.cnpm02.dkhp.DAO.SubjectDAO;
import uit.cnpm02.dkhp.DAO.TrainClassDAO;
import uit.cnpm02.dkhp.model.Faculty;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.utilities.Constants;

/**
 *
 * @author thanh
 */
@WebServlet(name = "ManageSchedule", urlPatterns = {"/ManageSchedule"})
public class ManageSchedule extends HttpServlet {

    /** 
     * 
     */
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
    TrainClassDAO trainClassDao=new TrainClassDAO();
    String path="";
     try {
    List<TrainClass> monday = trainClassDao.findAllByStudyDate(Constants.MONDAY);
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
    List<Faculty> facultyList= DAOFactory.getFacultyDao().findAll();
    session.setAttribute("monday", monday);
    session.setAttribute("tuesday", tuesday);
    session.setAttribute("wednesday", wednesday);
    session.setAttribute("thursday", thursday);
    session.setAttribute("friday", friday);
    session.setAttribute("saturday", saturday);
    session.setAttribute("year", Constants.CURRENT_YEAR);
    session.setAttribute("semester", Constants.CURRENT_SEMESTER);
    session.setAttribute("facultyList", facultyList);
    path= "./jsps/PDT/Schedule.jsp";
    } catch (Exception ex) {
    Logger.getLogger(ManageSchedule.class.getName()).log(Level.SEVERE, null, ex);
     path= "./jsps/Message.jsp";
   }
   response.sendRedirect(path);
}
private void setSubjectAndLecturer(List<TrainClass> trainClass) throws Exception{
    SubjectDAO subjectDao=new SubjectDAO();
    LecturerDAO lecturerDao=new LecturerDAO();
    for(int i=0;i<trainClass.size();i++){
        trainClass.get(i).setSubjectName(subjectDao.findById(trainClass.get(i).getSubjectCode()).getSubjectName());
        trainClass.get(i).setLectturerName(lecturerDao.findById(trainClass.get(i).getLecturerCode()).getFullName());
        trainClass.get(i).setNumTC(subjectDao.findById(trainClass.get(i).getSubjectCode()).getnumTC() );
    }
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
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
