/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.controllers.PDT;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import uit.cnpm02.dkhp.DAO.SubjectDAO;
import uit.cnpm02.dkhp.model.Faculty;
import uit.cnpm02.dkhp.model.Class;
import uit.cnpm02.dkhp.model.Student;
import uit.cnpm02.dkhp.model.StudyResult;
import uit.cnpm02.dkhp.model.StudyResultID;
import uit.cnpm02.dkhp.service.IReporter;
import uit.cnpm02.dkhp.service.IStudentService;
import uit.cnpm02.dkhp.service.impl.ReporterImpl;
import uit.cnpm02.dkhp.service.impl.StudentServiceImpl;
import uit.cnpm02.dkhp.utilities.Message;

/**
 *
 * @author thanh
 */
@WebServlet(name = "StudyResultManager", urlPatterns = {"/StudyResultManager"})
public class StudyResultManager extends HttpServlet {

    /**
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    private IReporter reportService = new ReporterImpl();
    private IStudentService studentService = new StudentServiceImpl();
     private String mssv = "";
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String requestAction;
        try {
            requestAction = (String) request.getParameter("action");
            if (requestAction.equals(ReportFunctionSupported.
                                                SEARCH_STUDENT.getValue())) {
                String key = request.getParameter("value");
                if (key != null) {
                    List<Student> students = searchStudent(key);
                    if ((students != null) && !students.isEmpty()) {
                        writeRespond(students, out);
                    } else {
                        out.println(Message.STUDENT_SEARCH_NOTFOUND);
                    }
                } else {
                    out.println(Message.STUDENT_SEARCH_NOTFOUND);
                }
                return;
            } else if (requestAction.equals(ReportFunctionSupported.
                                                DETAIL.getValue())) {
            getStudyResultForStudent(request, response);
            }else if (requestAction.equals(ReportFunctionSupported.
                                                RELOAD.getValue())) {
            reloadData(request, response);
            }else if (requestAction.equals(ReportFunctionSupported.
                                                UPDATE.getValue())) {
                updateMark(request, response);
            }
            
             
        } finally {            
            out.close();
        }
    }
private void updateMark(HttpServletRequest request, HttpServletResponse response){
     try{
         String path="";
            HttpSession session = request.getSession();  
            String studentCode = request.getParameter("studentCode");
            String subjectCode = request.getParameter("subjectCode");
            StudyResultID id = new StudyResultID(studentCode, subjectCode);
            StudyResult studyResult = DAOFactory.getStudyResultDao().findById(id);
            Student student = studentService.getStudent(studentCode);
            Class classes = DAOFactory.getClassDao().findById(student.getClassCode());
            Faculty faculty = DAOFactory.getFacultyDao().findById(student.getFacultyCode());
            studyResult.setSubjectName(DAOFactory.getSubjectDao().findById(subjectCode).getSubjectName());
            session.setAttribute("classes", classes);
            session.setAttribute("faculty", faculty);
            session.setAttribute("result", studyResult);
            session.setAttribute("student", student);
            path = "./jsps/PDT/UpdateMarkForStudent.jsp";
         }catch(Exception ex){
           String path= "./jsps/Message.jsp";
            try {
                response.sendRedirect(path);
            } catch (IOException ex1) {
                Logger.getLogger(StudyResultManager.class.getName()).log(Level.SEVERE, null, ex1);
            }
       }
}    
    
private void reloadData(HttpServletRequest request, HttpServletResponse response) throws IOException{
        try{
            SubjectDAO subjectDao=new SubjectDAO();
            int numTC = 0;
            float SumMark = 0;
            float Average = 0;
            PrintWriter out = response.getWriter();
            String studentCode = request.getParameter("studentCode");
            String year = request.getParameter("year");
            int semester = Integer.parseInt(request.getParameter("semester"));
            List<StudyResult> studyResult= DAOFactory.getStudyResultDao().findAllByYearAndSemester(studentCode, year, semester);
            //List<StudyResult> studyResult=studyResultDao.findAllByYearAndSemester(user, year, semester);
            setSubjectName(studyResult);
            out.println("<tr><th width='100px'>Năm học</th><th width='70px'>Học kỳ</th><th width='100px'>Mã môn</th><th width='300px'>Tên môn học</th><th width='70px'>Số TC</th><th width='80px'>Điểm</th><th width='100px'>Nhân hệ số</th><th>Sửa</th></tr>");
            for (int i = 0; i < studyResult.size(); i++) {
                int numTCSubject=subjectDao.findById(studyResult.get(i).getId().getSubjectCode()).getnumTC();
                float markSubject=(subjectDao.findById(studyResult.get(i).getId().getSubjectCode()).getnumTC() * studyResult.get(i).getMark());
                
                out.println("<tr><td>" + studyResult.get(i).getYear() + "</td><td>" + studyResult.get(i).getSemester() + "</td><td>" + studyResult.get(i).getId().getSubjectCode() + "</td><td>" + studyResult.get(i).getSubjectName() + "</td><td>" + numTCSubject + "</td><td>" + studyResult.get(i).getMark() + "</td><td>" + markSubject + "</td>");
                out.println("<td><a href='../../StudyResultManager?action=update&studentCode="+studentCode+ "&subjectCode="+ studyResult.get(i).getId().getSubjectCode()+"'>Sửa</a></td></tr>");
                numTC += numTCSubject;
                SumMark += markSubject;
                Average = (float) Math.round(SumMark * 100 / numTC) / 100;
        }  
           out.println("<tr><th>Tổng kết</th><th></th><th></th><th>Trung bình: " + Average + "</th><th>" + numTC + "</th><th></th><th>" + SumMark + "</th><th></th></tr>"); 
         }catch(Exception ex){
           String path= "./jsps/Message.jsp";
           response.sendRedirect(path);
       }
    }
    
private void getStudyResultForStudent(HttpServletRequest request, HttpServletResponse response){
    String path=""; 
      try{
           HttpSession session = request.getSession();   
           String studentCode = request.getParameter("studentCode");
           List<StudyResult> studyResult=DAOFactory.getStudyResultDao().findByOther("MSSV", studentCode, "NamHoc, HocKy", "ASC");
           ClassDAO classDao = DAOFactory.getClassDao();
           FacultyDAO facultyDao = DAOFactory.getFacultyDao();
           StudentDAO studentDao=new StudentDAO();
           Student student = studentDao.findById(studentCode);
           Class classes = classDao.findById(student.getClassCode());
           Faculty faculty = facultyDao.findById(student.getFacultyCode());
           setSubjectName(studyResult);
           List<String> yearList=getYear(studyResult);
           session.setAttribute("studyResult", studyResult);
           session.setAttribute("student", student);
           session.setAttribute("classes", classes);
           session.setAttribute("faculty", faculty);
           session.setAttribute("yearList", yearList);
           path = "./jsps/PDT/DetailStudyResult.jsp";
       }catch(Exception ex){
           path= "./jsps/Message.jsp";
       }
        try {
            response.sendRedirect(path);
        } catch (IOException ex) {
            Logger.getLogger(StudyResultManager.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
private void writeRespond(List<Student> datas, PrintWriter out) throws Exception {
        out.println("<tr>"
                + "<th> STT </th>"
                + "<th>MSSV</th>"
                + "<th width = 200px> Họ và tên </th>"
                + "<th width = 150px>Ngày sinh</th>"
                + "<th>lớp</th>"
                + "<th>Khoa</th>"
                + "<th>Khóa</th>"
                + "</tr>");
        for (int i = 0; i < datas.size(); i++) {
            out.println("<tr>");
            out.println("<td> " + (i + 1) + " </td>");
            out.println("<td> " + datas.get(i).getId() + " </td>");
            out.println("<td> <a href='../../StudyResultManager?action=detail&studentCode="+datas.get(i).getId() +"'>"
                    + datas.get(i).getFullName()
                    + "</a> </td>");
            out.println("<td> " + datas.get(i).getBirthday() + " </td>");
            out.println("<td> " + datas.get(i).getClassCode() + " </td>");
            out.println("<td> " + datas.get(i).getFacultyCode() + " </td>");
            out.println("<td> " + datas.get(i).getCourseCode() + " </td>");
            out.println("</tr>");// <a hreft > abc </a>
        }
        out.println("</table>");
    }

    private List<Student> searchStudent(String key) {
        List<Student> results = reportService.searchStudent(key);
        return results;
    }    
    
    
public enum ReportFunctionSupported {
        DEFAULT("default"), // List first page of class opened.
        SEARCH_STUDENT("search_student"),
        STUDENT_REPORT("student-report"),
        UPDATE("update"),
        RELOAD("reload"),
        DETAIL("detail"),
        DOWNLOAD_STUDENT_REPORT("download-student-report");
        
        private String description;
        ReportFunctionSupported(String description) {
            this.description = description;
        }
        
        public String getValue() {
            return description;
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
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(StudyResultManager.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(StudyResultManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
