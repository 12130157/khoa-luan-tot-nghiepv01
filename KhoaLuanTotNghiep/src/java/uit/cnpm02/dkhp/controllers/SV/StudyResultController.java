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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uit.cnpm02.dkhp.DAO.ClassDAO;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.FacultyDAO;
import uit.cnpm02.dkhp.DAO.StudentDAO;
import uit.cnpm02.dkhp.DAO.StudyResultDAO;
import uit.cnpm02.dkhp.DAO.SubjectDAO;
import uit.cnpm02.dkhp.model.Faculty;
import uit.cnpm02.dkhp.model.Class;
import uit.cnpm02.dkhp.model.Student;
import uit.cnpm02.dkhp.model.StudyResult;
import uit.cnpm02.dkhp.model.StudyResultID;

/**
 *
 * @author thanh
 */
@WebServlet(name = "StudyResultController", urlPatterns = {"/StudyResultController"})
public class StudyResultController extends HttpServlet {
  StudyResultDAO studyResultDao=new StudyResultDAO();
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
         HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        try {
            String action = request.getParameter("action");
           if(action.equalsIgnoreCase("view"))
            getStudyResult(response, session);
           else if(action.equalsIgnoreCase("reload"))
               reloadData(request, response, session);
        } finally {            
            out.close();
        }
    }
    private void reloadData(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException{
        try{
            SubjectDAO subjectDao=new SubjectDAO();
            int numTC = 0;
            float SumMark = 0;
            float Average = 0;
            PrintWriter out = response.getWriter();
            String user = (String) session.getAttribute("username");
            String year = request.getParameter("year");
            int semester = Integer.parseInt(request.getParameter("semester"));
            List<StudyResult> studyResult=studyResultDao.findAllByYearAndSemester(user, year, semester);
            setSubjectName(studyResult);
            out.println("<tr><th width='100px'>Năm học</th><th width='70px'>Học kỳ</th><th width='100px'>Mã môn</th><th width='300px'>Tên môn học</th><th width='70px'>Số TC</th><th width='80px'>Điểm</th><th width='100px'>Nhân hệ số</th></tr>");
            for (int i = 0; i < studyResult.size(); i++) {
                int numTCSubject=subjectDao.findById(studyResult.get(i).getId().getSubjectCode()).getnumTC();
                float markSubject=(subjectDao.findById(studyResult.get(i).getId().getSubjectCode()).getnumTC() * studyResult.get(i).getMark());
                
                out.println("<tr><td>" + studyResult.get(i).getYear() + "</td><td>" + studyResult.get(i).getSemester() + "</td><td>" + studyResult.get(i).getId().getSubjectCode() + "</td><td>" + studyResult.get(i).getSubjectName() + "</td><td>" + numTCSubject + "</td><td>" + studyResult.get(i).getMark() + "</td><td>" + markSubject + "</td></tr>");
                
                numTC += numTCSubject;
                SumMark += markSubject;
                Average = (float) Math.round(SumMark * 100 / numTC) / 100;
        }  
           out.println("<tr><th>Tổng kết</th><th></th><th></th><th>Trung bình: " + Average + "</th><th>" + numTC + "</th><th></th><th>" + SumMark + "</th></tr>"); 
         }catch(Exception ex){
           String path= "./jsps/Message.jsp";
           response.sendRedirect(path);
       }
    }
private void getStudyResult(HttpServletResponse response, HttpSession session) throws IOException{
     String path=""; 
      try{
          String user = (String) session.getAttribute("username");
          StudyResultID studyResultId=new StudyResultID(user, "");
           List<StudyResult> studyResult=studyResultDao.findByOther("MSSV", user, "NamHoc, HocKy", "ASC");
           ClassDAO classDao = DAOFactory.getClassDao();
           FacultyDAO facultyDao = DAOFactory.getFacultyDao();
           StudentDAO studentDao=new StudentDAO();
           Student student = studentDao.findById(user);
           Class classes = classDao.findById(student.getClassCode());
           Faculty faculty = facultyDao.findById(student.getFacultyCode());
           setSubjectName(studyResult);
           List<String> yearList=getYear(studyResult);
           session.setAttribute("studyResult", studyResult);
           session.setAttribute("student", student);
           session.setAttribute("classes", classes);
           session.setAttribute("faculty", faculty);
           session.setAttribute("yearList", yearList);
           path = "./jsps/SinhVien/StudyResult.jsp";
       }catch(Exception ex){
           path= "./jsps/Message.jsp";
       }
        response.sendRedirect(path);
}
private List<String> getYear(List<StudyResult> studyResult){
    ArrayList<String> yearList=new ArrayList<String>();
    for(int i=0; i<studyResult.size();i++){
     if(checkStringExist(studyResult.get(i).getYear(), yearList)==false)
         yearList.add(studyResult.get(i).getYear());
    }

   return yearList;         
}
private boolean checkStringExist(String value, List<String> list){
    boolean result=false;
    for(int i=0;i<list.size();i++){
       if(value.equalsIgnoreCase(list.get(i)))
           result=true;
    }
    return result;
}
private void setSubjectName(List<StudyResult> studyResult) throws Exception{
    SubjectDAO subjectDao=new SubjectDAO();
    for(int i=0;i<studyResult.size();i++){
        studyResult.get(i).setSubjectName(subjectDao.findById(studyResult.get(i).getId().getSubjectCode()).getSubjectName());
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
