/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.controllers.PDT;

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
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.model.Registration;
import uit.cnpm02.dkhp.model.RegistrationID;
import uit.cnpm02.dkhp.model.Student;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.service.IReporter;
import uit.cnpm02.dkhp.service.IStudentService;
import uit.cnpm02.dkhp.service.impl.ReporterImpl;
import uit.cnpm02.dkhp.service.impl.StudentServiceImpl;
import uit.cnpm02.dkhp.utilities.Constants;
import uit.cnpm02.dkhp.utilities.Message;

/**
 *
 * @author thanh
 */
@WebServlet(name = "RegistrationManager", urlPatterns = {"/RegistrationManager"})
public class RegistrationManager extends HttpServlet {

    /** 
     * 
     */
    private IReporter reportService = new ReporterImpl();
    private IStudentService studentService = new StudentServiceImpl();
     private String mssv = "";
         
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
         HttpSession session = request.getSession();
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
            }else if (requestAction.equals(ReportFunctionSupported.
                                                STUDENT_REPORT.getValue())) {
                mssv = request.getParameter("value");
                List<TrainClass> trainClassReg = getStudentReport(mssv, session.getId());
                if ((trainClassReg != null) && !trainClassReg.isEmpty()) {
                    writeStudentReportDetail(mssv, trainClassReg, out);
                } else {
                    out.println(Message.STUDENT_REPORT_NO_REPORT);
                }
                return;
            } 
            else if (requestAction.equals(ReportFunctionSupported.
                                                DELETE.getValue())) {
                mssv = request.getParameter("studentCode");
                String classCode=request.getParameter("classCode");
                deleteRegistration(mssv, classCode);
                List<TrainClass> trainClassReg = getStudentReport(mssv, session.getId());
                if ((trainClassReg != null) && !trainClassReg.isEmpty()) {
                    writeStudentReportDetail(mssv, trainClassReg, out);
                } else {
                    out.println(Message.STUDENT_REPORT_NO_REPORT);
                }
                return;
            } 
           
        } finally {            
            out.close();
        }
    }
    
    private void deleteRegistration(String studentCode, String classCode){
        try {
             RegistrationID id = new RegistrationID(studentCode, classCode, Constants.CURRENT_SEMESTER, Constants.CURRENT_YEAR);
             Registration reg = DAOFactory.getRegistrationDAO().findById(id);
             DAOFactory.getRegistrationDAO().delete(reg);
        } catch (Exception ex) {
            Logger.getLogger(RegistrationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     private List<TrainClass> getStudentReport(String mssv, String sessionId) {
        return reportService.getTrainClassRegistered(sessionId, mssv, true);
    }
      private void writeStudentReportDetail(String mssv, List<TrainClass> datas,
                                                        PrintWriter out) {
          int sumTC=0;
        String studentName = studentService.getStudent(mssv).getFullName();
        
        out.println("Danh sách các lớp học <b>" + studentName + "</b> đã đăng ký:");
        out.println("<table id = \"student-report\" name = \"student-report\" class=\"general-table\" style=\"float: right; width: 350;\">");

        out.println("<tr><th>STT</th><th>Mã lớp</th><th>Môn học</th><th>Số TC</th><th>Xóa</th</tr>");
        for (int i = 0; i < datas.size(); i++) {
            try {
            int numTC = DAOFactory.getSubjectDao().findById(datas.get(i).getSubjectCode()).getnumTC();
            out.println("<tr>");
            out.println("<td> " + (i + 1) + " </td>");
            String classId = datas.get(i).getId().getClassCode();
            out.println("<td> " 
                    + classId
                    + "</td>");
            out.println("<td> " + datas.get(i).getSubjectName() + " </td>");
            String method = String.format(" onclick=deleteTrainClassRegistration('%s','%s')>",
                                                      mssv, datas.get(i).getId().getClassCode());
            out.println("<td>"+numTC+"</td>");
            out.println("<td><a href=\'#\' " + method + "Xóa </a> </td>");
            out.println("</tr>");
            sumTC+= numTC;
            } catch (Exception ex) {
                Logger.getLogger(RegistrationManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        out.println("<tr><th></th><th>Tổng TC</th><th></th><th>"+sumTC+"</th><th></th></tr>");
        out.println("</table>");
    }
    private void writeRespond(List<Student> datas, PrintWriter out) throws Exception {
        out.println("<tr>"
                + "<th> STT </th>"
                + "<th>MSSV</th>"
                + "<th width = 200px> Họ và tên </th>"
                + "<th>Lớp</th>"
                + "</tr>");
        for (int i = 0; i < datas.size(); i++) {
            out.println("<tr>");
            out.println("<td> " + (i + 1) + " </td>");
            out.println("<td> " + datas.get(i).getId() + " </td>");
            String method = String.format(" onclick=getDetailStudentReport('%s')>",
                                                        datas.get(i).getId());
            out.println("<td> <a href=\'#\'" + method
                    + datas.get(i).getFullName()
                    + "</a> </td>");
            out.println("<td> " + datas.get(i).getClassCode() + " </td>");
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
        DELETE("delete"),
        CLASS_REPORT("class-report"),
        CLASS_DETAIL("class-detail"),
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
            Logger.getLogger(RegistrationManager.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(RegistrationManager.class.getName()).log(Level.SEVERE, null, ex);
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
