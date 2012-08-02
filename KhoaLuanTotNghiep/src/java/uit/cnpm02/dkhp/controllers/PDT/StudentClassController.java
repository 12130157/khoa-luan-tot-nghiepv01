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
import uit.cnpm02.dkhp.DAO.ClassDAO;
import uit.cnpm02.dkhp.DAO.CourseDAO;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.FacultyDAO;
import uit.cnpm02.dkhp.DAO.LecturerDAO;
import uit.cnpm02.dkhp.DAO.StudentDAO;
import uit.cnpm02.dkhp.model.Course;
import uit.cnpm02.dkhp.model.Faculty;
import uit.cnpm02.dkhp.model.Lecturer;
import uit.cnpm02.dkhp.model.Student;

/**
 *
 * @author LocNguyen
 */
@WebServlet(name = "StudentClassController", urlPatterns = {"/StudentClassController"})
public class StudentClassController extends HttpServlet {

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
        try {
            String action = request.getParameter("action");
            if (action.equalsIgnoreCase("default")) {
                loadDefault(session, response);
            } else if (action.equalsIgnoreCase("change-faculty")) {
                updateChangeFaculty(request, response);
            } else if (action.equalsIgnoreCase("create-class")) {
                createNewClass(request, response);
            } else if (action.equalsIgnoreCase("delete-student-class")) {
                deleteStudentClass(request, response);
            }else if(action.equalsIgnoreCase("edit-student-class")){
                doPreEditClass(request, response);
            }else if(action.equalsIgnoreCase("update")){
                doUpdateAccount(request, response);
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

    private void doUpdateAccount(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        try {
            PrintWriter out = response.getWriter();
            ClassDAO classDao = DAOFactory.getClassDao();
            String classID  = request.getParameter("classID");
            String newLectererCode = request.getParameter("newLecturerCode");
            uit.cnpm02.dkhp.model.Class clazz = classDao.findById(classID);
            clazz.setHomeroom(newLectererCode);
            classDao.update(clazz);
            out.println("Cập nhật thông tin thành công.");
         } catch (Exception ex) {
            Logger.getLogger(StudentClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void doPreEditClass(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        try {
            String classID = request.getParameter("clazzid");
            ClassDAO classDao = DAOFactory.getClassDao();
            uit.cnpm02.dkhp.model.Class clazz = classDao.findById(classID);
            PrintWriter out = response.getWriter();
            writeEditAccountForm(out, clazz);
        } catch (Exception ex) {
            Logger.getLogger(StudentClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void writeEditAccountForm(PrintWriter out, uit.cnpm02.dkhp.model.Class clazz) {
        try {
            LecturerDAO lecturerDao = DAOFactory.getLecturerDao();
            List<Lecturer> lecturer = lecturerDao.findByColumName("MaKhoa", clazz.getFacultyCode());
            out.print("<table style=\"width: 450px;\">");
            out.print("<tr>"
                        + "<td> Mã lớp </td>"
                        + "<td> <input type=\"text\" name=\"txtClassID_edit\" id=\"txtClassID_edit\" readonly=\"readonly\" value=\""+ clazz.getId() + "\" /></td>"
                    + "</tr>");
             out.print("<tr>"
                        + "<td> Tên lớp </td>"
                        + "<td> <input type=\"text\" name=\"txtClassName_edit\" id=\"txtClassName_edit\" readonly=\"readonly\" value=\""+ clazz.getClassName() + "\" /></td>"
                    + "</tr>");
             out.print("<tr>"
                        + "<td> Khoa </td>"
                        + "<td> <input type=\"text\" name=\"txtFaculty_edit\" id=\"txtFaculty_edit\" readonly=\"readonly\" value=\""+ clazz.getFacultyCode() + "\" /></td>"
                    + "</tr>");
             out.print("<tr>"
                        + "<td> Khóa học </td>"
                        + "<td> <input type=\"text\" name=\"txtCourse_edit\" id=\"txtCourse_edit\" readonly=\"readonly\" value=\""+ clazz.getCourseCode() + "\" /></td>"
                    + "</tr>");
            out.print("<tr>"
                + "<td> Giảng viên chủ nhiệm </td>"
                + "<td>");
            out.print("<select name=\"GVCN\" id=\"GVCN\" class=\"input-minwidth\">");
            for(int i=0; i<lecturer.size(); i++){
                out.print("<option value='"+lecturer.get(i).getId()+"'>"+lecturer.get(i).getFullName()+"</option>");
            }
            out.print("</select>");
            out.print("</td>"
            + "</tr>");
           out.print("</table>");
            out.print("<br/>");
            out.print("<div class=\"button-1\"><span class=\"atag\" onclick=\"update()\" ><img src=\"../../imgs/check.png\" />Hoàn thành</span></div>");
            //out.print("<div style=\"float: left; padding-left: 220px;\"><input type=\"button\" onclick=\"update()\" value=\"Hoàn thành\" />");
            out.print("</div>");
            out.print("<div class=\"clear\"></div>");
            out.print("<br/>");
            out.print("<div id=\"respone-edit-area\" class=\"msg-response\"></div>");
        } catch (Exception ex) {
            Logger.getLogger(StudentClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void setLecturerName(List<uit.cnpm02.dkhp.model.Class> classList) {
        try {
            LecturerDAO lecturerDao = DAOFactory.getLecturerDao();
            if (classList != null && classList.size() > 0) {
                for (int i = 0; i < classList.size(); i++) {
                    String lecturerName = lecturerDao.findById(classList.get(i).getHomeroom()).getFullName();
                    classList.get(i).setHomeroomName(lecturerName);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(StudentClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadDefault(HttpSession session,
            HttpServletResponse response) throws IOException {

        try {
            ClassDAO classDao = DAOFactory.getClassDao();
            List<uit.cnpm02.dkhp.model.Class> allClasses = classDao.findAll();
            setLecturerName(allClasses);
            session.setAttribute("classes", allClasses);

            FacultyDAO facDao = DAOFactory.getFacultyDao();
            List<Faculty> faculties = facDao.findAll();
            session.setAttribute("faculties", faculties);

            CourseDAO courseDao = DAOFactory.getCourseDao();
            List<Course> courses = courseDao.findAll();
            session.setAttribute("courses", courses);

            LecturerDAO lecturerDao = DAOFactory.getLecturerDao();
            List<Lecturer> lecturer = lecturerDao.findByColumName("MaKhoa", faculties.get(0).getId());
            session.setAttribute("lecturers", lecturer);
        } catch (Exception ex) {
            Logger.getLogger(StudentClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
        String path = "./jsps/PDT/ManageStudentClass.jsp";
        response.sendRedirect(path);
    }

    private void updateChangeFaculty(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            PrintWriter out = response.getWriter();
            String facultyId = request.getParameter("faculty");
            LecturerDAO lecturerDao = DAOFactory.getLecturerDao();
            List<Lecturer> lecturers = lecturerDao.findByColumName("MaKhoa", facultyId);
            if (lecturers != null && !lecturers.isEmpty()) {
                updateListLecturer(out, lecturers);
            }
        } catch (Exception ex) {
            Logger.getLogger(StudentClassController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void updateListLecturer(PrintWriter out, List<Lecturer> lecturers) {
        for (Lecturer l : lecturers) {
            out.print("<option value=\"" + l.getId() + "\">" + l.getFullName() + "</option>");
            //<option value="<%=l.getId()%>"> <%= l.getFullName()%> </option>
        }
    }

    private void createNewClass(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        String id = request.getParameter("id");
        if (id != null && id.contains(" ")) {
            out.println("- Lỗi: Vui lòng nhập mã lớp không có khoảng trắng.");
            return;
        }
        String name = request.getParameter("name");
        String faculty = request.getParameter("faculty");
        String course = request.getParameter("course");
        String lecturer = request.getParameter("lecturer");
        String numberStudent = request.getParameter("numberStudent");

        int numOfStudent = Integer.parseInt(numberStudent);
        uit.cnpm02.dkhp.model.Class clazz = new uit.cnpm02.dkhp.model.Class(
                id, name, faculty, course, lecturer, numOfStudent);
        try {
            ClassDAO clazzDao = DAOFactory.getClassDao();
            uit.cnpm02.dkhp.model.Class existedClazz = clazzDao.findById(id);
            if (existedClazz != null) {
                out.println("Mã lớp đã tồn tại.");
                return;
            }

            List<uit.cnpm02.dkhp.model.Class> existedClasses = clazzDao.findByColumName("GVCN", lecturer);
            if (existedClasses != null && !existedClasses.isEmpty()) {
                String lecturerName = DAOFactory.getLecturerDao().findById(lecturer).getFullName();
                out.println("Lỗi không thể tạo lớp: <br />"
                        + "- Giảng viên <b>" + lecturerName
                        + "</b> hiện đang là GVCN của lớp <b>" + existedClasses.get(0).getClassName() + "</b>");
                return;
            }

            clazzDao.add(clazz);
            ClassDAO classDao = DAOFactory.getClassDao();
            List<uit.cnpm02.dkhp.model.Class> allClasses = classDao.findAll();
            HttpSession session = request.getSession();
            session.setAttribute("classes", allClasses);

            out.println("Thêm thành công lớp:<br />");
            out.println("<br />- Mã lớp: " + id);
            out.println("<br />- Tên lớp: " + name);
            out.println("<br />- Khoa lớp: " + faculty);
        } catch (Exception ex) {
            Logger.getLogger(StudentClassController.class.getName()).log(Level.SEVERE, null, ex);
            out.println("Lỗi: " + ex.toString());
        }
    }

    private void deleteStudentClass(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String clazzID = request.getParameter("clazzid");
        PrintWriter out = response.getWriter();
        if (clazzID == null) {
            out.println("- Lỗi: Phiên làm việc hết hiệu lực, vui lòng thử lại.");
            return;
        }

        try {
            ClassDAO clazzDao = DAOFactory.getClassDao();
            uit.cnpm02.dkhp.model.Class clazz = clazzDao.findById(clazzID);
            if (clazz == null) {
                out.println("- Lớp không tồn tại");
                return;
            }
            StudentDAO studentDao = DAOFactory.getStudentDao();
            List<Student> students = studentDao.findByColumName("MaLop", clazzID);
            if ((students != null) && !students.isEmpty()) {
                out.println("- Không thể xóa lớp đã có SV.");
                return;
            }

            clazzDao.delete(clazz);
            ClassDAO classDao = DAOFactory.getClassDao();

            // Reset data for next F5...
            List<uit.cnpm02.dkhp.model.Class> allClasses = classDao.findAll();
            HttpSession session = request.getSession();
            session.setAttribute("classes", allClasses);
            out.println("Xóa thành công");
        } catch (Exception ex) {
            out.println("- Lỗi xóa lớp: " + ex.toString());
            return;
        }
    }
}
