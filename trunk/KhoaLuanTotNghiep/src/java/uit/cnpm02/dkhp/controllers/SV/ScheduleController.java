/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.controllers.SV;

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
import uit.cnpm02.dkhp.DAO.LecturerDAO;
import uit.cnpm02.dkhp.DAO.StudentDAO;
import uit.cnpm02.dkhp.DAO.SubjectDAO;
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
StudentDAO studentDao = new StudentDAO();
   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
         HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        try {
            String action=request.getParameter("action");
            if(action.equalsIgnoreCase("view"))
                getSchedule(response, session);
            else if(action.equalsIgnoreCase("Ajax"))
                getScheduleForAjax(request, response, session);
        } finally {            
            out.close();
        }
    }
private void getScheduleForAjax(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException, Exception{
   try{
     String type = request.getParameter("type");
    if(type.equalsIgnoreCase("All")){
            getScheduleForAll(request, response);
      }
    else if(type.equalsIgnoreCase("Only")){
        getScheduleForOnly(request, response, session);
    }
   }catch(Exception ex){
           String path= "./jsps/Message.jsp";
           response.sendRedirect(path);       
   }
     
}
private void getScheduleForAll(HttpServletRequest request, HttpServletResponse response) throws Exception{
             PrintWriter out = response.getWriter();
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
           out.println("<tr><th width='50px'></th><th width='50px'>Mã lớp</th><th width='200px'>Môn học</th><th width='50px'>Phòng</th><th width='50px'>Buổi</th><th width='200px'>Giảng viên</th></tr>"); 
           out.println("<tr><th><b><u>Thứ 2:</u></b></th><th></th><th></th><th></th><th></th><th></th></tr> ");
           for(int i=1; i<monday.size();i++){
               StringBuffer output= new StringBuffer();
               output.append("<tr><td></td><td>");
               output.append(monday.get(i).getId().getClassCode());
               output.append("</td><td>");
               output.append(monday.get(i).getSubjectName());
               output.append("</td><td>");
               output.append(monday.get(i).getClassRoom());
               if(monday.get(i).getShift()==1)
                    output.append("</td><td>Sáng</td><td>");
               else 
                    output.append("</td><td>Chiều</td><td>");
               output.append(monday.get(i).getLectturerName());
               output.append("</td></tr>");
               out.println(output.toString());
           }
           out.println("<tr><th><b><u>Thứ 3:</u></b></th><th></th><th></th><th></th><th></th><th></th></tr> ");
           for(int i=1; i<tuesday.size();i++){
               StringBuffer output= new StringBuffer();
               output.append("<tr><td></td><td>");
               output.append(tuesday.get(i).getId().getClassCode());
               output.append("</td><td>");
               output.append(tuesday.get(i).getSubjectName());
               output.append("</td><td>");
               output.append(tuesday.get(i).getClassRoom());
               if(monday.get(i).getShift()==1)
                    output.append("</td><td>Sáng</td><td>");
               else 
                    output.append("</td><td>Chiều</td><td>");
               output.append(tuesday.get(i).getLectturerName());
               output.append("</td></tr>");
               out.println(output.toString());
           }
           out.println("<tr><th><b><u>Thứ 4:</u></b></th><th></th><th></th><th></th><th></th><th></th></tr> ");
           for(int i=1; i<wednesday.size();i++){
               StringBuffer output= new StringBuffer();
               output.append("<tr><td></td><td>");
               output.append(wednesday.get(i).getId().getClassCode());
               output.append("</td><td>");
               output.append(wednesday.get(i).getSubjectName());
               output.append("</td><td>");
               output.append(wednesday.get(i).getClassRoom());
               if(monday.get(i).getShift()==1)
                    output.append("</td><td>Sáng</td><td>");
               else 
                    output.append("</td><td>Chiều</td><td>");
               output.append(wednesday.get(i).getLectturerName());
               output.append("</td></tr>");
               out.println(output.toString());
           }
            out.println("<tr><th><b><u>Thứ 5:</u></b></th><th></th><th></th><th></th><th></th><th></th></tr> ");
           for(int i=1; i<thursday.size();i++){
               StringBuffer output= new StringBuffer();
               output.append("<tr><td></td><td>");
               output.append(thursday.get(i).getId().getClassCode());
               output.append("</td><td>");
               output.append(thursday.get(i).getSubjectName());
               output.append("</td><td>");
               output.append(thursday.get(i).getClassRoom());
               if(monday.get(i).getShift()==1)
                    output.append("</td><td>Sáng</td><td>");
               else 
                    output.append("</td><td>Chiều</td><td>");
               output.append(thursday.get(i).getLectturerName());
               output.append("</td></tr>");
               out.println(output.toString());
           }
           out.println("<tr><th><b><u>Thứ 6:</u></b></th><th></th><th></th><th></th><th></th><th></th></tr> ");
           for(int i=1; i<friday.size();i++){
               StringBuffer output= new StringBuffer();
               output.append("<tr><td></td><td>");
               output.append(friday.get(i).getId().getClassCode());
               output.append("</td><td>");
               output.append(friday.get(i).getSubjectName());
               output.append("</td><td>");
               output.append(friday.get(i).getClassRoom());
               if(monday.get(i).getShift()==1)
                    output.append("</td><td>Sáng</td><td>");
               else 
                    output.append("</td><td>Chiều</td><td>");
               output.append(friday.get(i).getLectturerName());
               output.append("</td></tr>");
               out.println(output.toString());
           }
           out.println("<tr><th><b><u>Thứ 7:</u></b></th><th></th><th></th><th></th><th></th><th></th></tr> ");
           for(int i=1; i<saturday.size();i++){
               StringBuffer output= new StringBuffer();
               output.append("<tr><td></td><td>");
               output.append(saturday.get(i).getId().getClassCode());
               output.append("</td><td>");
               output.append(saturday.get(i).getSubjectName());
               output.append("</td><td>");
               output.append(saturday.get(i).getClassRoom());
               if(monday.get(i).getShift()==1)
                    output.append("</td><td>Sáng</td><td>");
               else 
                    output.append("</td><td>Chiều</td><td>");
               output.append(saturday.get(i).getLectturerName());
               output.append("</td></tr>");
               out.println(output.toString());
           }
}
private void getScheduleForOnly(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception{
            PrintWriter out = response.getWriter();
            String user = (String) session.getAttribute("username");
            String FacultyCode= studentDao.findById(user).getFacultyCode();
            List<TrainClass> monday=trainClassDao.findAllByStudyDateAndFaculty(Constants.MONDAY,FacultyCode);
            setSubjectAndLecturer(monday);
            List<TrainClass> tuesday=trainClassDao.findAllByStudyDateAndFaculty(Constants.TUESDAY,FacultyCode);
            setSubjectAndLecturer(tuesday);
            List<TrainClass> wednesday=trainClassDao.findAllByStudyDateAndFaculty(Constants.WEDNESDAY,FacultyCode);
            setSubjectAndLecturer(wednesday);
            List<TrainClass> thursday=trainClassDao.findAllByStudyDateAndFaculty(Constants.THURSDAY,FacultyCode);
            setSubjectAndLecturer(thursday);
            List<TrainClass> friday=trainClassDao.findAllByStudyDateAndFaculty(Constants.FRIDAY,FacultyCode);
            setSubjectAndLecturer(friday);
            List<TrainClass> saturday=trainClassDao.findAllByStudyDateAndFaculty(Constants.SATURDAY,FacultyCode);
            setSubjectAndLecturer(saturday);
            out.println("<tr><th width='50px'></th><th width='50px'>Mã lớp</th><th width='200px'>Môn học</th><th width='50px'>Phòng</th><th width='50px'>Buổi</th><th width='200px'>Giảng viên</th></tr>"); 
            out.println("<tr><th><b><u>Thứ 2:</u></b></th><th></th><th></th><th></th><th></th><th></th></tr> ");
            for(int i=1; i<monday.size();i++){
               StringBuffer output= new StringBuffer();
               output.append("<tr><td></td><td>");
               output.append(monday.get(i).getId().getClassCode());
               output.append("</td><td>");
               output.append(monday.get(i).getSubjectName());
               output.append("</td><td>");
               output.append(monday.get(i).getClassRoom());
               if(monday.get(i).getShift()==1)
                    output.append("</td><td>Sáng</td><td>");
               else 
                    output.append("</td><td>Chiều</td><td>");
               output.append(monday.get(i).getLectturerName());
               output.append("</td></tr>");
               out.println(output.toString());
           }
          /*  out.println("<tr><th><b><u>Thứ 3:</u></b></th><th></th><th></th><th></th><th></th><th></th></tr> ");
           for(int i=1; i<tuesday.size();i++){
               StringBuffer output= new StringBuffer();
               output.append("<tr><td></td><td>");
               output.append(tuesday.get(i).getId().getClassCode());
               output.append("</td><td>");
               output.append(tuesday.get(i).getSubjectName());
               output.append("</td><td>");
               output.append(tuesday.get(i).getClassRoom());
               if(monday.get(i).getShift()==1)
                    output.append("</td><td>Sáng</td><td>");
               else 
                    output.append("</td><td>Chiều</td><td>");
               output.append(tuesday.get(i).getLectturerName());
               output.append("</td></tr>");
               out.println(output.toString());
           }
          out.println("<tr><th><b><u>Thứ 4:</u></b></th><th></th><th></th><th></th><th></th><th></th></tr> ");
           for(int i=1; i<wednesday.size();i++){
               StringBuffer output= new StringBuffer();
               output.append("<tr><td></td><td>");
               output.append(wednesday.get(i).getId().getClassCode());
               output.append("</td><td>");
               output.append(wednesday.get(i).getSubjectName());
               output.append("</td><td>");
               output.append(wednesday.get(i).getClassRoom());
               if(monday.get(i).getShift()==1)
                    output.append("</td><td>Sáng</td><td>");
               else 
                    output.append("</td><td>Chiều</td><td>");
               output.append(wednesday.get(i).getLectturerName());
               output.append("</td></tr>");
               out.println(output.toString());
           }
            out.println("<tr><th><b><u>Thứ 5:</u></b></th><th></th><th></th><th></th><th></th><th></th></tr> ");
           for(int i=1; i<thursday.size();i++){
               StringBuffer output= new StringBuffer();
               output.append("<tr><td></td><td>");
               output.append(thursday.get(i).getId().getClassCode());
               output.append("</td><td>");
               output.append(thursday.get(i).getSubjectName());
               output.append("</td><td>");
               output.append(thursday.get(i).getClassRoom());
               if(monday.get(i).getShift()==1)
                    output.append("</td><td>Sáng</td><td>");
               else 
                    output.append("</td><td>Chiều</td><td>");
               output.append(thursday.get(i).getLectturerName());
               output.append("</td></tr>");
               out.println(output.toString());
           }
           out.println("<tr><th><b><u>Thứ 6:</u></b></th><th></th><th></th><th></th><th></th><th></th></tr> ");
           for(int i=1; i<friday.size();i++){
               StringBuffer output= new StringBuffer();
               output.append("<tr><td></td><td>");
               output.append(friday.get(i).getId().getClassCode());
               output.append("</td><td>");
               output.append(friday.get(i).getSubjectName());
               output.append("</td><td>");
               output.append(friday.get(i).getClassRoom());
               if(monday.get(i).getShift()==1)
                    output.append("</td><td>Sáng</td><td>");
               else 
                    output.append("</td><td>Chiều</td><td>");
               output.append(friday.get(i).getLectturerName());
               output.append("</td></tr>");
               out.println(output.toString());
           }
           out.println("<tr><th><b><u>Thứ 7:</u></b></th><th></th><th></th><th></th><th></th><th></th></tr> ");
           for(int i=1; i<saturday.size();i++){
               StringBuffer output= new StringBuffer();
               output.append("<tr><td></td><td>");
               output.append(saturday.get(i).getId().getClassCode());
               output.append("</td><td>");
               output.append(saturday.get(i).getSubjectName());
               output.append("</td><td>");
               output.append(saturday.get(i).getClassRoom());
               if(monday.get(i).getShift()==1)
                    output.append("</td><td>Sáng</td><td>");
               else 
                    output.append("</td><td>Chiều</td><td>");
               output.append(saturday.get(i).getLectturerName());
               output.append("</td></tr>");
               out.println(output.toString());
           }*/
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
private void getSchedule(HttpServletResponse response, HttpSession session) throws IOException{
    String path=""; 
      try{
            String user = (String) session.getAttribute("username");
            String FacultyCode= studentDao.findById(user).getFacultyCode();
            session.setAttribute("year", Constants.CURRENT_YEAR);
            session.setAttribute("semester", Constants.CURRENT_SEMESTER);
            List<TrainClass> monday=trainClassDao.findAllByStudyDateAndFaculty(Constants.MONDAY,FacultyCode);
            setSubjectAndLecturer(monday);
            List<TrainClass> tuesday=trainClassDao.findAllByStudyDateAndFaculty(Constants.TUESDAY,FacultyCode);
            setSubjectAndLecturer(tuesday);
            List<TrainClass> wednesday=trainClassDao.findAllByStudyDateAndFaculty(Constants.WEDNESDAY,FacultyCode);
            setSubjectAndLecturer(wednesday);
            List<TrainClass> thursday=trainClassDao.findAllByStudyDateAndFaculty(Constants.THURSDAY,FacultyCode);
            setSubjectAndLecturer(thursday);
            List<TrainClass> friday=trainClassDao.findAllByStudyDateAndFaculty(Constants.FRIDAY,FacultyCode);
            setSubjectAndLecturer(friday);
            List<TrainClass> saturday=trainClassDao.findAllByStudyDateAndFaculty(Constants.SATURDAY,FacultyCode);
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
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(ScheduleController.class.getName()).log(Level.SEVERE, null, ex);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(ScheduleController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
