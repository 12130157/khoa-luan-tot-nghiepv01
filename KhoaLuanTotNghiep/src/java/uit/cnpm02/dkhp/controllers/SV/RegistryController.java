/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.controllers.SV;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uit.cnpm02.dkhp.model.Registration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.LecturerDAO;
import uit.cnpm02.dkhp.DAO.SubjectDAO;
import uit.cnpm02.dkhp.model.Student;
import uit.cnpm02.dkhp.model.Class;
import uit.cnpm02.dkhp.model.Faculty;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.model.TrainClassID;
import uit.cnpm02.dkhp.utilities.Constants;

/**
 *
 * @author thanh
 */
@WebServlet(name = "RegistryController", urlPatterns = {"/RegistryController"})
public class RegistryController extends HttpServlet {

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
                forward(response, session);
            else if(action.equalsIgnoreCase("reRegistry"))
                reRegistry(response, session);
            else if(action.equalsIgnoreCase("registry"))
                preRegistry(response, request, session);
            else if(action.equalsIgnoreCase("completeRegistry"))
                completeRegistration(response, request, session);
            else if(action.equals("detail"))
                detailTrainClass(response, request, session);
        } finally {           
            out.close();
        }
    }
 private void detailTrainClass(HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception{
    String path="";
     try{
     String classCode= request.getParameter("classCode");
     List<Registration> registration=DAOFactory.getRegistrationDAO().findAllByClassCode(classCode);
     List<Student> studentList= new ArrayList<Student>();
     for(int i=0; i<registration.size(); i++){
         Student student =DAOFactory.getStudentDao().findById(registration.get(i).getId().getStudentCode());
         studentList.add(student);
     }
     TrainClassID trainClassId=new TrainClassID(classCode, Constants.CURRENT_YEAR, Constants.CURRENT_SEMESTER);
     TrainClass trainClass=DAOFactory.getTrainClassDAO().findById(trainClassId);
     trainClass.setLectturerName(DAOFactory.getLecturerDao().findById(trainClass.getLecturerCode()).getFullName());
     trainClass.setSubjectName(DAOFactory.getSubjectDao().findById(trainClass.getSubjectCode()).getSubjectName());
     session.setAttribute("studentList", studentList);
     session.setAttribute("classInfo", trainClass);
      path= "./jsps/SinhVien/ClassDetail.jsp";
      response.sendRedirect(path);
    }catch(Exception e){
        path= "./jsps/Message.jsp";
        response.sendRedirect(path);
    }
    
 }
 private int getNumTCRegistry(List<TrainClass> registried){
       int numTC=0;
        if(registried.isEmpty()==false){
         for(int i=0; i<registried.size();i++){
         numTC+=registried.get(i).getNumTC();
        }
        }
      return numTC;
    }
private boolean checkNumOfStudentReg(String classCode) throws Exception{
    boolean result=false;
    TrainClassID trainClassId=new TrainClassID(classCode, Constants.CURRENT_YEAR, Constants.CURRENT_SEMESTER);
    TrainClass trainClass=DAOFactory.getTrainClassDAO().findById(trainClassId);
    if(trainClass.getNumOfStudentReg()>=trainClass.getNumOfStudent())
       result=true;
    return result;
}
private boolean isNotEnoughTC(List<TrainClass> registried) throws Exception{
    boolean result=false;
    int minNumTC= (int)DAOFactory.getRuleDao().findById("SoTinChiToiThieu").getValue();
    if(getNumTCRegistry(registried)<minNumTC)
        result=true;
    return result;
}
private boolean isOverTC(List<TrainClass> registried) throws Exception{
    boolean result=false;
    int maxNumTC= (int)DAOFactory.getRuleDao().findById("SoTinChiToiDa").getValue();
    if(getNumTCRegistry(registried)>maxNumTC)
        result=true;
    return result;
}
private boolean isPresubNotComplete(String subjectCode, String studentCode) throws Exception{
    boolean result=false;
    float markPass= DAOFactory.getRuleDao().findById("DiemQuaMon").getValue();
    List<String> preSub=DAOFactory.getPreSubDao().findAllPreSubBySubCode(subjectCode);
    if(preSub.isEmpty()==false){
        for(int i=0;i<preSub.size();i++){
            if(DAOFactory.getStudyResultDao().getMarkByStudentAndSub(preSub.get(i), studentCode)<markPass)
                result=true;
        }
    }
    return result;
}

 private void completeRegistration(HttpServletResponse response, HttpServletRequest request, HttpSession session) throws IOException{
     String path="";
     try{
         String studentCode = (String) session.getAttribute("username");
         List<TrainClass> registried=(List<TrainClass>) session.getAttribute("registriedClass");
         ArrayList<String> message=new ArrayList<String>();
         if(isNotEnoughTC(registried))
         {
          session.setAttribute("error", "Số tín chỉ đăng ký chưa đủ quy định, tối thiểu là "+(int)DAOFactory.getRuleDao().findById("SoTinChiToiThieu").getValue()+ " TC. Xem thêm quy định.");
          Student student =DAOFactory.getStudentDao().findById(studentCode);
             Class classes = DAOFactory.getClassDao().findById(student.getClassCode());
             Faculty faculty =DAOFactory.getFacultyDao().findById(student.getFacultyCode());
             session.setAttribute("student", student);
             session.setAttribute("classes", classes);
             session.setAttribute("faculty", faculty);
             session.setAttribute("registriedClass", registried);
             session.setAttribute("semester", Constants.CURRENT_SEMESTER);
             session.setAttribute("year", Constants.CURRENT_YEAR);
             path= "./jsps/SinhVien/PreviewRegistration.jsp";
              response.sendRedirect(path);
         }
         else if(isOverTC(registried)){
             session.setAttribute("error", "Số tín chỉ đăng ký quá quy định, tối đa là "+(int)DAOFactory.getRuleDao().findById("SoTinChiToiDa").getValue()+ " TC. Xem thêm quy định.");
             Student student =DAOFactory.getStudentDao().findById(studentCode);
             Class classes = DAOFactory.getClassDao().findById(student.getClassCode());
             Faculty faculty =DAOFactory.getFacultyDao().findById(student.getFacultyCode());
             session.setAttribute("student", student);
             session.setAttribute("classes", classes);
             session.setAttribute("faculty", faculty);
             session.setAttribute("registriedClass", registried);
             session.setAttribute("semester", Constants.CURRENT_SEMESTER);
             session.setAttribute("year", Constants.CURRENT_YEAR);
             path= "./jsps/SinhVien/PreviewRegistration.jsp";
              response.sendRedirect(path);
         }
         else{
             DAOFactory.getRegistrationDAO().deleteAllByByStudentCode(studentCode);
             for(int i=0; i<registried.size();i++){
                 if(checkNumOfStudentReg(registried.get(i).getId().getClassCode())){
                     String mes="Lớp học "+registried.get(i).getId().getClassCode()+" ("+registried.get(i).getSubjectName() +") đã đủ số lượng nên không thẻ đăng ký thêm";
                     message.add(mes);
                 }
                 else if(isPresubNotComplete(registried.get(i).getSubjectCode(), studentCode)){
                    String mes="Lớp học "+registried.get(i).getId().getClassCode()+" ("+registried.get(i).getSubjectName() +") không thể đăng ký do chưa hoàn thành môn học tiên quyết của nó. Xem thêm quy định môn học tiên quyết.";
                     message.add(mes); 
                 }
                 else {
                     Registration reg=new Registration(studentCode, registried.get(i).getId().getClassCode(), Constants.CURRENT_YEAR, Constants.CURRENT_SEMESTER, registried.get(i).getNumOfStudentReg()+1, 0);
                     DAOFactory.getRegistrationDAO().add(reg);
                 }

             }
             if(message.isEmpty())
                 forward(response, session);
             else{
             session.setAttribute("message", message);
             path="./jsps/SinhVien/ErrorMessage.jsp";
             response.sendRedirect(path);
             }
         }
     }catch(Exception ex){
           path= "./jsps/Message.jsp";
           response.sendRedirect(path);
     }
      
 }
 private void preRegistry(HttpServletResponse response, HttpServletRequest request, HttpSession session) throws IOException, Exception{
     String path="";
     try{
         String studentCode=(String)session.getAttribute("username");
         String[] registry = request.getParameterValues("check");
         if(registry==null)
             getAllClass(response, session, studentCode);
         else{
             List<TrainClass> registriedClass= new ArrayList<TrainClass>();
             for(int i=0; i<registry.length; i++){
                 TrainClassID trainClassId= new TrainClassID(registry[i], Constants.CURRENT_YEAR, Constants.CURRENT_SEMESTER);
                 registriedClass.add(DAOFactory.getTrainClassDAO().findById(trainClassId));
             }
             setSubjectAndLecturer(registriedClass);
             Student student =DAOFactory.getStudentDao().findById(studentCode);
             Class classes = DAOFactory.getClassDao().findById(student.getClassCode());
             Faculty faculty =DAOFactory.getFacultyDao().findById(student.getFacultyCode());
             session.setAttribute("student", student);
             session.setAttribute("classes", classes);
             session.setAttribute("faculty", faculty);
             session.setAttribute("registriedClass", registriedClass);
             session.setAttribute("semester", Constants.CURRENT_SEMESTER);
             session.setAttribute("year", Constants.CURRENT_YEAR);
             path= "./jsps/SinhVien/PreviewRegistration.jsp";
         }
     }catch(Exception ex){
           path= "./jsps/Message.jsp";
     }
      response.sendRedirect(path);
 }
 private void reRegistry(HttpServletResponse response, HttpSession session) throws Exception{
     String path="";
     try{
      String studentCode=(String)session.getAttribute("username");
      Student student =DAOFactory.getStudentDao().findById(studentCode);
      List<TrainClass> trainClass=DAOFactory.getTrainClassDAO().findAllBySemesterAndYear();
      List<String> registried=new ArrayList<String>();
      setSubjectAndLecturer(trainClass);
      Class classes = DAOFactory.getClassDao().findById(student.getClassCode());
      Faculty faculty =DAOFactory.getFacultyDao().findById(student.getFacultyCode());
      session.setAttribute("student", student);
      session.setAttribute("classes", classes);
      session.setAttribute("faculty", faculty);
      session.setAttribute("trainClass", trainClass);
      session.setAttribute("semester", Constants.CURRENT_SEMESTER);
      session.setAttribute("year", Constants.CURRENT_YEAR);
      registried=registried(studentCode);
      session.setAttribute("registried", registried);
      path="./jsps/SinhVien/Registration.jsp";
      }catch(Exception ex){
           path= "./jsps/Message.jsp";
      }
      response.sendRedirect(path);
      
 }
 private List<String> registried(String studentCode) throws Exception{
     ArrayList<String> result=new ArrayList<String>();
     List<Registration> registration=DAOFactory.getRegistrationDAO().findAllByStudentCode(studentCode); 
     for(int i=0; i < registration.size();i++){
         result.add(registration.get(i).getId().getClassCode());
     }
     return result;
 }
 private void forward(HttpServletResponse response, HttpSession session) throws Exception {
     String path="";  
     try{
          String user=(String)session.getAttribute("username");
        List<Registration> registration=DAOFactory.getRegistrationDAO().findAllByStudentCode(user);
        if(Constants.INTIME_REGISTRY){
        if (registration.isEmpty()) {
            getAllClass(response, session, user);
        } else {
            showRegitration(registration, response, session, user);
        }
        }else {
            showRegitration(registration, response, session, user);
        }
         }catch(Exception ex){
           path= "./jsps/Message.jsp";
       }
       response.sendRedirect(path);
}
 private void getAllClass(HttpServletResponse response, HttpSession session, String studentCode) throws Exception{
      Student student =DAOFactory.getStudentDao().findById(studentCode);
      List<TrainClass> trainClass=DAOFactory.getTrainClassDAO().findAllBySemesterAndYear();
      ArrayList<String> registried=new ArrayList<String>();
      setSubjectAndLecturer(trainClass);
      Class classes = DAOFactory.getClassDao().findById(student.getClassCode());
      Faculty faculty =DAOFactory.getFacultyDao().findById(student.getFacultyCode());
      session.setAttribute("student", student);
      session.setAttribute("classes", classes);
      session.setAttribute("faculty", faculty);
      session.setAttribute("trainClass", trainClass);
      session.setAttribute("semester", Constants.CURRENT_SEMESTER);
      session.setAttribute("year", Constants.CURRENT_YEAR);
      session.setAttribute("registried", registried);
      String path="./jsps/SinhVien/Registration.jsp";
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
 private void showRegitration(List<Registration> registration, HttpServletResponse response, HttpSession session, String studentCode) throws Exception{
      Student student =DAOFactory.getStudentDao().findById(studentCode);
      Class classes = DAOFactory.getClassDao().findById(student.getClassCode());
      Faculty faculty =DAOFactory.getFacultyDao().findById(student.getFacultyCode());
      session.setAttribute("student", student);
      session.setAttribute("classes", classes);
      session.setAttribute("faculty", faculty);
      session.setAttribute("semester", Constants.CURRENT_SEMESTER);
      session.setAttribute("year", Constants.CURRENT_YEAR);
      session.setAttribute("inTimeRegistry", Constants.INTIME_REGISTRY);
      ArrayList<TrainClass> registried=new ArrayList<TrainClass>();
      for(int i=0; i<registration.size();i++){
          String classCode=registration.get(i).getId().getClassCode();
          TrainClassID trainClassId=new TrainClassID(classCode, Constants.CURRENT_YEAR, Constants.CURRENT_SEMESTER);
          TrainClass trainClass=DAOFactory.getTrainClassDAO().findById(trainClassId);
          registried.add(trainClass);
      }
      setSubjectAndLecturer(registried);
      session.setAttribute("registried", registried);
       String path="./jsps/SinhVien/ShowRegistry.jsp";
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
            Logger.getLogger(RegistryController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(RegistryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
