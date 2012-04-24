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
import uit.cnpm02.dkhp.service.IReporter;
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
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
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
            if (requestAction.equals(ReportFunctionSupported.DEFAULT.getValue())) {
                String path = "./jsps/PDT/Report.jsp";
                response.sendRedirect(path);
                return;
            } else if (requestAction.equals(ReportFunctionSupported.SEARCH_STUDENT.getValue())) {
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
            }
        } catch(Exception ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
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
    
    private void writeRespond(List<Student> datas, PrintWriter out) {
        out.println("<table id = \"list-student\" name = \"list-student\">");

        out.println("<tr>"
                + "<th> STT </th>"
                + "<th> Họ và tên </th>"
                + "</tr>");
        for (int i = 0; i < datas.size(); i++) {
            out.println("<tr>");
            out.println("<td> " + (i + 1) + " </td>");
            out.println("<td> " + datas.get(i).getFullName() + " </td>");
            out.println("</tr>");
        }
        out.println("</table>");
    }
    

     /**
     * An enum define all supported function of serverlet
     * .
     */
    public enum ReportFunctionSupported {
        DEFAULT("default"), // List first page of class opened.
        SEARCH_STUDENT("search_student");
        //CREATE("create"),   // Create new class form support
        //DELETE("delete"),   // Remove class
        //UPDATE("update");   // Update
        
        private String description;
        ReportFunctionSupported(String description) {
            this.description = description;
        }
        
        public String getValue() {
            return description;
        }
        
    }
}
