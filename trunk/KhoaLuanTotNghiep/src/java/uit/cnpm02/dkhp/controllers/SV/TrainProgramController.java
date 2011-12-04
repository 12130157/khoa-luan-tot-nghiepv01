/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.controllers.SV;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uit.cnpm02.dkhp.DAO.ClassDAO;
import uit.cnpm02.dkhp.DAO.FacultyDAO;
import uit.cnpm02.dkhp.DAO.StudentDAO;
import uit.cnpm02.dkhp.DAO.ViewTrainProgramDAO;
import uit.cnpm02.dkhp.model.Faculty;
import uit.cnpm02.dkhp.model.Student;
import uit.cnpm02.dkhp.model.ViewTrainProgram;
import uit.cnpm02.dkhp.model.Class;

/**
 *
 * @author thanh
 */
@WebServlet(name = "TrainProgramController", urlPatterns = {"/TrainProgramController"})
public class TrainProgramController extends HttpServlet {

    /** 
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        try {
            String action = request.getParameter("action");
            if(action.equalsIgnoreCase("view"))
               getProgram(response, session); 
        } finally {            
            out.close();
        }
    }
 private void getProgram(HttpServletResponse response, HttpSession session) throws Exception {
       String path="";  
       try{
        String user=(String)session.getAttribute("username");
        StudentDAO studentDao = new StudentDAO();
        FacultyDAO facultyDao=new FacultyDAO();
        ClassDAO classDao=new ClassDAO();
        Student student = new Student();
        student = studentDao.findById(user);
        session.setAttribute("student", student);
        Faculty faculty=new Faculty();
        faculty=facultyDao.findById(student.getFacultyCode());
        session.setAttribute("faculty", faculty);
        Class classes=new Class();
        classes=classDao.findById(student.getClassCode());
        session.setAttribute("classes", classes);
        ViewTrainProgramDAO viewTrainProgramDAO = new ViewTrainProgramDAO();
        ArrayList<ViewTrainProgram> pro = viewTrainProgramDAO.getProgramByCode(user);
        session.setAttribute("pro", pro);
         path = "./jsps/SinhVien/TrainingProgram.jsp";
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
            Logger.getLogger(TrainProgramController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(TrainProgramController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
