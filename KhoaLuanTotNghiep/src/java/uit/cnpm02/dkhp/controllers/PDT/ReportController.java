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
import uit.cnpm02.dkhp.model.Student;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.service.IReporter;
import uit.cnpm02.dkhp.service.IStudentService;
import uit.cnpm02.dkhp.service.TrainClassStatus;
import uit.cnpm02.dkhp.service.impl.ReporterImpl;
import uit.cnpm02.dkhp.service.impl.StudentServiceImpl;
import uit.cnpm02.dkhp.utilities.Constants;
import uit.cnpm02.dkhp.utilities.Message;
import uit.cnpm02.dkhp.utilities.filedownload.FileDownloadUtility;

/**
 *
 * @author LocNguyen
 */
@WebServlet(name = "ReportController", urlPatterns = {"/ReportController"})
public class ReportController extends HttpServlet {
    
    private IReporter reportService = new ReporterImpl();
    private IStudentService studentService = new StudentServiceImpl();
    
    private String sortType = "ASC";
    /**Store curren process student**/
    private String mssv = "";

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
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
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
                /**The key should be student's name or student's id**/
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
                                                STUDENT_REPORT_SORT.getValue())) {
                // List existed TrainClass, Pagging setup
                String by = request.getParameter("by");
                String type = request.getParameter("type");
                if (type != null) {
                    if (!type.equalsIgnoreCase("ASC")
                            && !type.equalsIgnoreCase("DES")) {
                        type = "ASC";
                    }
                    List<TrainClass> trainClassReg = reportService.sort(session.getId(), by, type);
                    if ((trainClassReg != null) && !trainClassReg.isEmpty()) {
                        writeStudentReportDetail(mssv, trainClassReg, out);
                    } else {
                        out.println(Message.STUDENT_REPORT_NO_REPORT);
                    }
                    
                    if (sortType.equalsIgnoreCase("ASC"))
                        sortType = "DES";
                    else
                        sortType = "ASC";
                    //103/33 duong dien cao the
                    return;
                } else {
                    out.println(Message.STUDENT_SEARCH_NOTFOUND);
                }
                return;
            } else if (requestAction.equals(ReportFunctionSupported.
                                                STUDENT_REPORT.getValue())) {
                mssv = request.getParameter("value");
                List<TrainClass> trainClassReg = getStudentReport(mssv, session.getId());
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
                if ((year == null)
                        || year.isEmpty()
                        || year.equalsIgnoreCase("All")
                        || year.equals("*")) {
                    
                    year = "";
                }
                
                List<TrainClass> trainClassReg = getTrainClassByYearAndSemeter(
                                                    year, semeter);
                
                if ((trainClassReg == null) || trainClassReg.isEmpty()) {
                    out.println(Message.CLASS_REPORT_NO_REPORT);
                } else {
                    writeTrainClassReport(trainClassReg, out);
                }
                return;
            } else if (requestAction.equals(ReportFunctionSupported.
                                                DOWNLOAD_STUDENT_REPORT.getValue())) {
                //downloadStudentReport(response);
                //return;
            } else if (requestAction.equals(ReportFunctionSupported.
                                                CLASS_DETAIL.getValue())) {
                String classId = request.getParameter("classid");
                updateClassDetailReport(classId, session);
                String path = "./jsps/PDT/TrainClassReport.jsp";
                response.sendRedirect(path);
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

    private List<TrainClass> getStudentReport(String mssv, String sessionId) {
        return reportService.getTrainClassRegistered(sessionId, mssv, true);
    }
    
    private void writeStudentReportDetail(String mssv, List<TrainClass> datas,
                                                        PrintWriter out) {
        String studentName = studentService.getStudent(mssv).getFullName();
        
        out.println("Danh sach cac mon hoc <b>" + studentName + "</b> da dang ky:");
        out.println("<table id = \"student-report\" name = \"student-report\">");

        out.println("<tr>"
                + "<th> STT </th>"
                + "<th> <a href='#'"
                    + " onclick=\"sortTrainClass('MaLopHoc', '" + sortType + "')\">"
                    + " Ma lop </a></th>"
                + "<th> <a href='#'"
                    + " onclick=\"sortTrainClass('MonHoc', '" + sortType + "')\">"
                    + " Mon hoc </a></th>"
                + "<th> <a href='#'"
                    + " onclick=\"sortTrainClass('NamHoc', '" + sortType + "')\">"
                    + " Nam hoc </a></th>"
                + "<th> <a href = '#'"
                    + " onclick=\"sortTrainClass('HocKy', '" + sortType + "')\">"
                    + "Hoc ky </a></th>"
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
        out.println("... Thong tin khac </br>");
        
        out.println("<a href='../../ReportController?action=download-student-report'>Tai file excel</a>");
        //<a href="../DownloadFile?action=studentresult&mssv=<%=student.getCode()%>"> Tải file</a>
    }

    private List<TrainClass> getTrainClassByYearAndSemeter(
            String year, int semeter) {
        return reportService.getTrainClass(year, semeter, TrainClassStatus.ALL);
    }

    private void writeTrainClassReport(List<TrainClass> trainClassReg,
                                                        PrintWriter out) {
        
        out.println("<table>");

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

    private void downloadStudentReport(HttpServletResponse resp, String sessionId) {
        List<TrainClass> trainClasses = reportService
                                  .getTrainClassRegistered(sessionId, mssv, true);
        String fileName = 
                FileDownloadUtility.exportStudentReportFile(mssv, trainClasses);
        if (!fileName.isEmpty()) {
            try {
                FileDownloadUtility.downloadFile(fileName, resp);
            } catch (IOException ex) {
                Logger.getLogger(ReportController.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
        
    }

    /**
     * User, after search some student, the click handler for 
     * link at each student include detail class
     * but, we need detail for each class, so this function
     * will update nedded data.
     * @param classId
     * @param session 
     */
    private void updateClassDetailReport(String classId, HttpSession session) {
        TrainClass clazz = reportService.getTrainClass(classId);
        String clazzId = clazz.getId().getClassCode();
        String year = Constants.CURRENT_YEAR;
        int semeter = Constants.CURRENT_SEMESTER;
        
        List<Student> students = studentService
                .getStudent(clazzId, year, semeter);
        
        session.setAttribute("trainclass", clazz);
        session.setAttribute("students", students);
    }

     /**
     * An enum define all supported function of serverlet
     * .
     */
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
}
