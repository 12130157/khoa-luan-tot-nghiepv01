package uit.cnpm02.dkhp.controllers.GV;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uit.cnpm02.dkhp.model.Lecturer;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.service.ILecturerService;
import uit.cnpm02.dkhp.service.ITrainClassService;
import uit.cnpm02.dkhp.service.impl.LecturerServiceImpl;
import uit.cnpm02.dkhp.service.impl.TrainClassServiceImpl;

/**
 *
 * @author LocNguyen
 */
@WebServlet(name = "LecturerPrivateController", urlPatterns = {"/LecturerPrivateController"})
public class LecturerPrivateController extends HttpServlet {
    private ILecturerService lecturereService = new LecturerServiceImpl();
    private ITrainClassService trainClassSeervice = new TrainClassServiceImpl();

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
        request.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        session.removeAttribute("error");
        
        try {
            String action = request.getParameter("function");
            if (action.equalsIgnoreCase(LecturerPrivateSupport
                    .DEFAULT.getValue())) {
                doDefaultAction(request, response);
            } else if (action.equalsIgnoreCase(LecturerPrivateSupport
                    .GET_TRAIN_CLASS.getValue())) {
                doLoadTrainClassForLecturer(request, response);
            }
        } catch (Exception ex) {
            out.println("Đã xảy ra sự cố: </br>" + ex);
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

    private void doDefaultAction(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        String path = "./jsps/GiangVien/PersionalInformation.jsp";
        Lecturer lecturer = lecturereService.getLecturer(username);
        if (lecturer == null) {
            path = "../../Login.jsp";
        } else {
            List<TrainClass> includeTrainClass =
                    trainClassSeervice.getCurrentTrainClass(username);
            if (trainClassSeervice != null)
                session.setAttribute("trainclass", includeTrainClass);
            session.setAttribute("lecturer", lecturer);
        }
        
        response.sendRedirect(path);
    }

    /**
     * Lecturer want to upload file score
     * he want to known what are classes he's teaching
     * because the file format is the class code.
     * @param request
     * @param response
     * @throws IOException 
     */
    private void doLoadTrainClassForLecturer(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String lecturerId = (String) request.getParameter("lecturer");
        
        List<TrainClass> trainClasses =
                trainClassSeervice.getCurrentTrainClass(lecturerId);
        
        writeOutListTrainClass(response.getWriter(), trainClasses);
    }
    
    private void writeOutListTrainClass(PrintWriter out,
            List<TrainClass> trainClasses) {
        if ((trainClasses == null) || trainClasses.isEmpty()) {
            out.println("Không tìm thấy lớp nào");
        } else {
            out.println("<table class=\"general-table\" style=\"width: 600px;\">");
            out.println("<tr>"
                        + "<th>STT</th>"
                        + "<th>Mã Lớp</th>"
                        + "<th>Môn học</th>"
                    + "</tr>");
            for (int i = 0; i < trainClasses.size(); i++) {
                out.println("<tr>"
                            + "<td>" + (i + 1) + "</td>"
                            + "<td>" + trainClasses.get(i).getId().getClassCode() + "</td>"
                            + "<td>" + trainClasses.get(i).getSubjectName() + "</td>"
                        + "</tr>");
            }
            out.println("</table>");
        }
    }
//#############################################
    public enum LecturerPrivateSupport {
        DEFAULT("default"),
        GET_TRAIN_CLASS("get-train-class");
        
        
        private String description;
        LecturerPrivateSupport(String description) {
            this.description = description;
        }
        
        public String getValue() {
            return description;
        }
    }

}
