/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.controllers.PDT;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uit.cnpm02.dkhp.model.Student;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.service.IReporter;
import uit.cnpm02.dkhp.service.IStudentService;
import uit.cnpm02.dkhp.service.impl.ReporterImpl;
import uit.cnpm02.dkhp.service.impl.StudentServiceImpl;
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
     private String sortType = "ASC";
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
           
        } finally {            
            out.close();
        }
    }
     private List<TrainClass> getStudentReport(String mssv, String sessionId) {
        return reportService.getTrainClassRegistered(sessionId, mssv, true);
    }
      private void writeStudentReportDetail(String mssv, List<TrainClass> datas,
                                                        PrintWriter out) {
        String studentName = studentService.getStudent(mssv).getFullName();
        
        out.println("Danh sách các lớp học <b>" + studentName + "</b> đã đăng ký:");
        out.println("<table id = \"student-report\" name = \"student-report\">");

        out.println("<tr>"
                + "<th> STT </th>"
                + "<th> <a href='#'"
                    + " onclick=\"sortTrainClass('MaLopHoc', '" + sortType + "')\">"
                    + " Mã lớp </a></th>"
                + "<th> <a href='#'"
                    + " onclick=\"sortTrainClass('MonHoc', '" + sortType + "')\">"
                    + " Môn học </a></th>"
                + "<th> <a href='#'"
                    + " onclick=\"sortTrainClass('NamHoc', '" + sortType + "')\">"
                    + " Năm học </a></th>"
                + "<th> <a href = '#'"
                    + " onclick=\"sortTrainClass('HocKy', '" + sortType + "')\">"
                    + "Học kỳ </a></th>"
                + "</tr>");
        for (int i = 0; i < datas.size(); i++) {
            out.println("<tr>");
            out.println("<td> " + (i + 1) + " </td>");
            String classId = datas.get(i).getId().getClassCode();
            out.println("<td> <a href=\"../../ReportController?action=class-detail&classid=" 
                    + classId + "\">" 
                    + classId
                    + "</a></td>");
            out.println("<td> " + datas.get(i).getSubjectName() + " </td>");
            out.println("<td> " + datas.get(i).getId().getYear() + " </td>");
            out.println("<td> " + datas.get(i).getId().getSemester() + " </td>");
            out.println("</tr>");// <a hreft > abc </a>
        }
        out.println("</table>");
    }
    private void writeRespond(List<Student> datas, PrintWriter out) {
        out.println("<tr>"
                + "<th> STT </th>"
                + "<th width = 200px> Họ và tên </th>"
                + "</tr>");
        for (int i = 0; i < datas.size(); i++) {
            out.println("<tr>");
            out.println("<td> " + (i + 1) + " </td>");
            String method = String.format(" onclick=getDetailStudentReport('%s')>",
                                                        datas.get(i).getId());
            out.println("<td> <a href=\'#\'" + method
                    + datas.get(i).getFullName()
                    + "</a> </td>");
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
        STUDENT_REPORT_SORT("sort-student-report"),
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
