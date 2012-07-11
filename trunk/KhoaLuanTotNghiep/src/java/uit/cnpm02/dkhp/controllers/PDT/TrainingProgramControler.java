/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.controllers.PDT;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uit.cnpm02.dkhp.DAO.CourseDAO;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.FacultyDAO;
import uit.cnpm02.dkhp.DAO.SubjectDAO;
import uit.cnpm02.dkhp.DAO.TrainProgDetailDAO;
import uit.cnpm02.dkhp.DAO.TrainProgramDAO;
import uit.cnpm02.dkhp.model.Course;
import uit.cnpm02.dkhp.model.Faculty;
import uit.cnpm02.dkhp.model.TrainProDetail;
import uit.cnpm02.dkhp.model.TrainProgram;
import uit.cnpm02.dkhp.utilities.StringUtils;

/**
 *
 * @author LocNguyen
 */
@WebServlet(name = "TrainingProgramControler", urlPatterns = {"/TrainingProgramControler"})
public class TrainingProgramControler extends HttpServlet {

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
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        try {
            String action = request.getParameter("action");
            if (action.equalsIgnoreCase("default")) {
                loadDefault(session, response);
            } else if (action.equalsIgnoreCase("get-train-prog-detail")) {
                getTrainProgDetail(request, response);
            } else if(action.equalsIgnoreCase("create-new-train-prog")) {
                createNewTrainProg(request, response);
            } else if (action.equalsIgnoreCase("pre-update-train-prog")) {
                preUpdateTrainProg(request, response);
            }
            
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

    private void loadDefault(HttpSession session,
            HttpServletResponse response) throws IOException {
        TrainProgramDAO tpDao = DAOFactory.getTrainProgramDAO();
        try {
            List<TrainProgram> tps = tpDao.findAll();
            session.setAttribute("train-programs", tps);
            
            FacultyDAO facDao = DAOFactory.getFacultyDao();
            List<Faculty> faculties = facDao.findAll();
            session.setAttribute("faculties", faculties);
            
            CourseDAO courseDao = DAOFactory.getCourseDao();
            List<Course> courses = courseDao.findAll();
            session.setAttribute("courses", courses);
        } catch (Exception ex) {
            Logger.getLogger(TrainingProgramControler.
                    class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String path = "./jsps/PDT/TrainingProgram.jsp";
        response.sendRedirect(path);
    }

    private void getTrainProgDetail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String trainProgID = request.getParameter("train-program-ID");
        PrintWriter out = response.getWriter();
        if (StringUtils.isEmpty(trainProgID)) {
            out.println("Không tìm thấy chi tiết chương trình đào tạo, vui lòng thử lại.");
            return;
        }
        
        TrainProgDetailDAO tpdDao = DAOFactory.getTrainProgDetailDAO();
        try {
            List<TrainProDetail> tps = tpdDao.findByColumName("MaCTDT", trainProgID);
            Collections.sort(tps, new Comparator<TrainProDetail>() {

                @Override
                public int compare(TrainProDetail o1, TrainProDetail o2) {
                    return o1.getSemester() - o2.getSemester();
                }
            });
            writeOutTrainProDetail(out, tps, trainProgID);
        } catch (Exception ex) {
            Logger.getLogger(TrainingProgramControler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private void writeOutTrainProDetail(PrintWriter out,
            List<TrainProDetail> tps, String trainProgId) {
        out.println("Chương trình Đào tạo: <b>" + trainProgId + "</b>");
        if ((tps != null) && !tps.isEmpty()){
            SubjectDAO subDao = DAOFactory.getSubjectDao();

            //out.println("<b>Môn học trong chương trình đào tạo: " + tps.get(0).getId().getTrainProgID() +"</b>");
            out.println("<table class=\"general-table\" style=\"width: 400px; margin-left: 12px;\">");
            out.println("<tr><th>Môn học</th><th>Học kỳ</th></tr>");
            for (TrainProDetail tpd : tps) {
                String subName = tpd.getId().getSubjectID();
                try {
                    subName = subDao.findById(tpd.getId().getSubjectID()).getSubjectName();
                } catch(Exception ex) {
                    //
                }
                out.println("<tr>"
                        + "<td>" + subName + "</td>"
                        + "<td>" + tpd.getSemester() + "</td>"
                        + "</tr>");
            }
            out.println("</table>");
        }
        //<form action="" name="formstudent"  id="formstudent">
        out.println("<form id=\"form-update\" name=\"form-update\" action=\"../../TrainingProgramControler?action=pre-update-train-prog\" method=\"post\">"
                        /*+ "<div class=\"button-1\">"
                        + "<input type= \"submit\" ><img src=\"../../imgs/check.png\"/>Cập nhật</input>"
                        + "</div>"*/
                + "</form>");
        
        
        out.println("<div class=\"button-1\">"
                    + "<span class=\"atag\" onclick=\"updateTrainPro('" + trainProgId + "')\" ><img src=\"../../imgs/check.png\"/>Cập nhật</span>"
                    + "</div>");
    }

    private void createNewTrainProg(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        String trainProgID = request.getParameter("train-program-ID");
        String facultyID = request.getParameter("faculty-ID");
        String courseID = request.getParameter("course-ID");
        if (trainProgID.contains(" ")) {
            out.println("Vui lòng nhập mã CTĐT không có khoảng trắng.");
            return;
        }
        TrainProgramDAO tpDao = DAOFactory.getTrainProgramDAO();
        TrainProgram tp = null;
        try {
            tp = tpDao.findById(trainProgID);
        } catch (Exception ex) {
            Logger.getLogger(TrainingProgramControler.class.getName())
                    .log(Level.SEVERE, null, ex);
            out.println("Lỗi: " + ex.toString());
            return;
        }
        if (tp != null) {
            out.println("<br />Mã chương trình đào tạo đã tồn tại:");
            out.println("<br />- Mã CTDT: " + tp.getId());
            out.println("<br />- Mã Khoa: " + tp.getFacultyCode());
            out.println("<br />- Mã Khóa học: " + tp.getCourseCode());
            return;
        }
        TrainProgram addTrainProg = new TrainProgram(trainProgID, facultyID, courseID);
        addTrainProg.setId(trainProgID);
        try {
            tpDao.add(addTrainProg);
            List<TrainProgram> tps = tpDao.findAll();
            HttpSession session = request.getSession();
            session.setAttribute("train-programs", tps);
            out.println("Tạo thành công CTĐT:<br />");
            out.println("- Mã CTĐT: " + addTrainProg.getId()
                    + " - Mã khoa: " + addTrainProg.getFacultyCode()
                    + " - Khóa: " + addTrainProg.getCourseCode());
        } catch(Exception ex) {
            out.println("Lỗi: " + ex.toString());
            return;
        }
    }

    private void preUpdateTrainProg(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String path = "./jsps/PDT/UpdateTrainProgram.jsp";
        response.sendRedirect(path);
    }

}
