package uit.cnpm02.dkhp.controllers.GV;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uit.cnpm02.dkhp.model.Lecturer;
import uit.cnpm02.dkhp.model.Task;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.model.type.TaskStatus;
import uit.cnpm02.dkhp.model.type.TaskType;
import uit.cnpm02.dkhp.service.ILecturerService;
import uit.cnpm02.dkhp.service.IPDTService;
import uit.cnpm02.dkhp.service.ITrainClassService;
import uit.cnpm02.dkhp.service.impl.LecturerServiceImpl;
import uit.cnpm02.dkhp.service.impl.PDTServiceImpl;
import uit.cnpm02.dkhp.service.impl.TrainClassServiceImpl;
import uit.cnpm02.dkhp.utilities.Constants;
import uit.cnpm02.dkhp.utilities.ExecuteResult;

/**
 *
 * @author LocNguyen
 */
@WebServlet(name = "LecturerPrivateController", urlPatterns = {"/LecturerPrivateController"})
public class LecturerPrivateController extends HttpServlet {
    private ILecturerService lecturereService = new LecturerServiceImpl();
    private ITrainClassService trainClassSeervice = new TrainClassServiceImpl();
    private IPDTService pdtService = new PDTServiceImpl();
    
    private static SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_PARTERM_DEFAULT);

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
            /*if (action.equalsIgnoreCase(LecturerPrivateSupport
                    .DEFAULT.getValue())) {
                doDefaultAction(request, response);
            }*/if (action.equalsIgnoreCase(LecturerPrivateSupport
                    .LOAD_PERSIONAL_INFO.getValue())) {
                doDefaultAction(request, response);
            } else if (action.equalsIgnoreCase(LecturerPrivateSupport
                    .GET_TRAIN_CLASS.getValue())) {
                doLoadTrainClassForLecturer(request, response);
            } else if (action.equalsIgnoreCase(LecturerPrivateSupport
                    .UPDATE.getValue())) {
                doUpdateInformaton(request, response);
            } else if (action.equalsIgnoreCase(LecturerPrivateSupport
                    .SEND_REQUEST.getValue())) {
                doSendRequest(request, response);
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
        String path = "./jsps/GiangVien/PersionnalInformation.jsp";
        Lecturer lecturer = lecturereService.getLecturer(username);
        if (lecturer == null) {
            path = "../../Login.jsp";
        } else {
            session.setAttribute("lecturer", lecturer);
            
            /*List<Task> tasks = pdtService.getTasks(username);
            if ((tasks != null) && !tasks.isEmpty()) {
                session.setAttribute("tasks", tasks);
            }*/
            /*List<TrainClass> includeTrainClass =
                    trainClassSeervice.getCurrentTrainClass(username);
            if (trainClassSeervice != null)
                session.setAttribute("trainclass", includeTrainClass);
            session.setAttribute("lecturer", lecturer);
             */
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
            out.println("<table class=\"general-table\" style=\"width: 615px;\">");
            out.println("<tr>"
                    + "<th>STT</th>"
                    + "<th>Mã Lớp</th>"
                    + "<th>Môn học</th>"
                    + "<th>Download</th>"
                    + "</tr>");
            for (int i = 0; i < trainClasses.size(); i++) {
                String key = trainClasses.get(i).getId().getClassCode() + ";"
                        + trainClasses.get(i).getId().getYear() + ";"
                        + trainClasses.get(i).getId().getSemester();
                out.println("<tr>"
                        + "<td>" + (i + 1) + "</td>"
                        + "<td>" + trainClasses.get(i).getId().getClassCode() + "</td>"
                        + "<td>" + trainClasses.get(i).getSubjectName() + "</td>"
                        + "<td><a href=\"../../DownloadController?action=download-empty-file-score&key="
                        + key + "\"><img src=\"../../imgs/download.png\" alt=\"Download\" title=\"Tải bảng điểm\" /></a></td>"
                        + "</tr>");
            }
            out.println("</table>");
        }
    }

    /**
     * Update lecturer's information.
     * @param request
     * @param response 
     */
    private void doUpdateInformaton(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String lecturerId = (String) request.getParameter("lecturer");
        String fullname = (String) request.getParameter("fullname");
        String cmnd = (String) request.getParameter("cmnd");
        String email = (String) request.getParameter("email");
        String birthday = (String) request.getParameter("birthday");
        String phone = (String) request.getParameter("phone");
        
        PrintWriter out = response.getWriter();
        Date birth = null;
        try {
            
            birth = sdf.parse(birthday);
        } catch (Exception ex) {
            out.append("error - Lỗi parse ngày sinh: " + ex.toString());
            return;
        }
        
        ExecuteResult er = lecturereService.updateLecturer(
                request.getSession().getId(), lecturerId,
                    fullname, cmnd, email, birth, phone);
        if (!er.isIsSucces()) {
            out.println(er.getMessage());
        } else {
            Lecturer l = (Lecturer) er.getData();
            writeOutLecturerInformation(out, l);
        }
    }

    private void writeOutLecturerInformation(PrintWriter out, Lecturer l) {
        out.println("<table style=\"width: 600px;\">");
        out.println("<tr class=\"odd-row\">"
                        + "<td> Mã GV </td>"
                        + "<td>"
                            + "<input type=\"text\" readonly=\"readonly\" id=\"txt-lecturer-id\" value=" + l.getId() + " />"
                        + "</td>"
                        + "<td> Quên quán </td>"
                        + "<td>" + l.getAddress() + "</td>"
                    + "</tr>");
        
        out.println("<tr class=\"even-row\">"
                        + "<td> Họ và tên </td>"
                        + "<td>" + l.getFullName() + "</td>"
                        + "<td> Khoa </td>"
                        + "<td>" + l.getFacultyCode() + "</td>"
                        + "</tr>"
                        + "<tr class=\"odd-row\">"
                        + "<td> Ngày sinh </td>"
                        + "<td>" + sdf.format(l.getBirthday()) + "</td>"
                        + "<td> CMND </td>"
                        + "<td>" + l.getIdentityCard() + "</td>"
                        + "</tr>");
        out.println("<tr class=\"even-row\">"
                        + "<td> ĐT liên lạc </td>"
                        + "<td>" + l.getPhone() + "</td>"
                        + "<td> Học Hàm </td>"
                        + "<td>" + l.getHocHam() + "</td>"
                    + "</tr>"
                    + "<tr class=\"odd-row\">"
                        + "<td> Email </td>"
                        + "<td>" + l.getEmail() + "</td>"
                        + "<td> Học Vị </td>"
                        + "<td>" + l.getHocVi() + "</td>"
                    + "</tr>");
        out.println("</table>");
    }

    /**
     * Sen request to PDT
     * @param request
     * @param response
     * @throws IOException d requ
     */
    private void doSendRequest(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        
        String trainClass = (String) request.getParameter("trainclass");
        String dayoff = (String) request.getParameter("dayoff");
        String dayon = (String) request.getParameter("dayon");
        String shift = (String) request.getParameter("shift");
        
        String sender = (String) request.getSession().getAttribute("username");
        
        String content = "GV " + sender + " TB nghi lop " + trainClass
                + " ca " + shift + " , ngay " + dayoff
                + " day bu vao ngay " + dayon;
        Task task = new Task(sender, "admin", content, new Date(),
                TaskStatus.TOBE_PROCESS, TaskType.GV_TB_NGHI_DAY);
        ExecuteResult er = pdtService.sendTask(task);
        
        if (!er.isIsSucces()) {
            out.println("Lỗi: " + er.getMessage());
        } else {
            out.println("Yêu cầu được gửi thành công");
        }
    }
//#############################################
    public enum LecturerPrivateSupport {
        DEFAULT("default"),
        GET_TRAIN_CLASS("get-train-class"),
        LOAD_PERSIONAL_INFO("load-persional-infor"),
        UPDATE("update"),
        SEND_REQUEST("send-request");        
        
        
        private String description;
        LecturerPrivateSupport(String description) {
            this.description = description;
        }
        
        public String getValue() {
            return description;
        }
    }

}
