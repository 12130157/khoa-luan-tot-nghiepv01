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
import uit.cnpm02.dkhp.model.Student;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.service.IReporter;
import uit.cnpm02.dkhp.service.TrainClassStatus;
import uit.cnpm02.dkhp.service.impl.ReporterImpl;
import uit.cnpm02.dkhp.utilities.Message;

/**
 *
 * @author LocNguyen
 */
@WebServlet(name = "ReportController", urlPatterns = {"/ReportController"})
public class ReportController extends HttpServlet {
    
    private IReporter reportService = new ReporterImpl();

    /** 
     * Processes requests for both HTTP <code>GET</code> and
     * <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request,
                                        HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String requestAction;
        try {
            requestAction = (String) request.getParameter("action");
        } catch (Exception ex) {
             requestAction = ReportFunctionSupported.DEFAULT.getValue();
        }
        try {
            if (requestAction.equals(ReportFunctionSupported.
                                                DEFAULT.getValue())) {
                String path = "./jsps/PDT/Report.jsp";
                response.sendRedirect(path);
                return;
            } else if (requestAction.equals(ReportFunctionSupported.
                                                SEARCH_STUDENT.getValue())) {
                // List existed TrainClass, Pagging setup
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
                                                STUDENT_REPORT.getValue())) {
                String mssv = request.getParameter("value");
                List<TrainClass> trainClassReg = getStudentReport(mssv);
                if ((trainClassReg != null) && !trainClassReg.isEmpty()) {
                    writeStudentReportDetail(mssv, trainClassReg, out);
                } else {
                    out.println(Message.STUDENT_REPORT_NO_REPORT);
                }
                return;
            } else if (requestAction.equals(ReportFunctionSupported.
                                                CLASS_REPORT.getValue())) {
                String year = request.getParameter("year");
                String s = request.getParameter("semeter");
                int semeter = -1;
                if ((s != null) || !s.isEmpty()) {
                    if (s.equals("1"))
                        semeter = 1;
                    else if (s.equals("2"))
                        semeter = 2;
                }
                List<TrainClass> trainClassReg = getTrainClassByYearAndSemeter(
                                                    year, semeter);
                
                if ((trainClassReg == null) || trainClassReg.isEmpty()) {
                    out.println(Message.CLASS_REPORT_NO_REPORT);
                } else {
                    writeTrainClassReport(trainClassReg, out);
                }
                return;
            }
        } catch(Exception ex) {
            Logger.getLogger(ReportController.class.getName()).
                                                log(Level.SEVERE, null, ex);
        } finally {
            out.close();
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

    private List<Student> searchStudent(String key) {
        List<Student> results = reportService.searchStudent(key);
        return results;
    }
    
    /**
     * Data respone throught ajax when call search student from page
     * @param datas list student found
     * @param out output stream
     */
    private void writeRespond(List<Student> datas, PrintWriter out) {
        out.println("<table id = \"list-student\" name = \"list-student\">");
        out.println("<tr>"
                + "<th> STT </th>"
                + "<th> Họ và tên </th>"
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

    private List<TrainClass> getStudentReport(String mssv) {
        return reportService.getTrainClassRegistered(mssv, true);
    }
    
    private void writeStudentReportDetail(String student, List<TrainClass> datas,
                                                        PrintWriter out) {
        out.println("Danh sach cac mon hoc " + student + " da dang ky:");
        out.println("<table id = \"student-report\" name = \"student-report\">");

        out.println("<tr>"
                + "<th> STT </th>"
                + "<th> Ma lop </th>"
                + "<th> Mon hoc </th>"
                + "<th> Nam hoc </th>"
                + "<th> Hoc ky </th>"
                + "</tr>");
        for (int i = 0; i < datas.size(); i++) {
            out.println("<tr>");
            out.println("<td> " + (i + 1) + " </td>");
            out.println("<td>" + datas.get(i).getId().getClassCode() + "</td>");
            out.println("<td> " + datas.get(i).getSubjectName() + " </td>");
            out.println("<td> " + datas.get(i).getId().getYear() + " </td>");
            out.println("<td> " + datas.get(i).getId().getSemester() + " </td>");
            out.println("</tr>");// <a hreft > abc </a>
        }
        out.println("</table>");
        out.println("... Thong tin khac </br>");
        
        out.println("<a href='#'>Tai file excel</a>");
    }

    private List<TrainClass> getTrainClassByYearAndSemeter(
            String year, int semeter) {
        return reportService.getTrainClass(year, semeter, TrainClassStatus.ALL);
    }

    private void writeTrainClassReport(List<TrainClass> trainClassReg, PrintWriter out) {
        
        out.println("<table id = \"trainclass-report\" name = \"trainclass-report\">");

        /**
            Tổng số lớp đã tạo: 	120
            Tổng số lớp hủy do không đủ điều kiện: 	100
            Tổng số lớp chưa kết thúc: 	20
         */
        int total = trainClassReg.size();
        int classClosed = 0;
        int classCanceled = 0;
        int classInProcess = 0;
        for (TrainClass t : trainClassReg) {
            
            if (t.getStatus() == TrainClassStatus.OPEN) {
                classInProcess ++;
            } else if (t.getStatus() == TrainClassStatus.CLOSE) {
                classClosed ++;
            } else if (t.getStatus() == TrainClassStatus.CANCEL) {
                classCanceled ++;
            }
        }
        
        String[][] map = new String[][] {
            {Message.REPORT_TRAINCLASS_TOTAL, Integer.toString(total)},
            {Message.REPORT_TRAINCLASS_CLOSED, Integer.toString(classClosed)},
            {Message.REPORT_TRAINCLASS_CANCELED, Integer.toString(classCanceled)},
            {Message.REPORT_TRAINCLASS_IN_PROGRESS, Integer.toString(classInProcess)},
        };     
        
        for (int i = 0; i < map.length; i++) {
            out.println("<tr>");
            out.println("<td> " + map[i][0] + " </td>");
            out.println("<td> " + map[i][1] + " </td>");
            out.println("</tr>");
        }
        
        out.println("</table>");
        out.println("<a href='#'>Tai file excel</a>");
    }

     /**
     * An enum define all supported function of serverlet
     * .
     */
    public enum ReportFunctionSupported {
        DEFAULT("default"), // List first page of class opened.
        SEARCH_STUDENT("search_student"),
        STUDENT_REPORT("student-report"),
        CLASS_REPORT("class-report");
        
        private String description;
        ReportFunctionSupported(String description) {
            this.description = description;
        }
        
        public String getValue() {
            return description;
        }
        
    }
}
