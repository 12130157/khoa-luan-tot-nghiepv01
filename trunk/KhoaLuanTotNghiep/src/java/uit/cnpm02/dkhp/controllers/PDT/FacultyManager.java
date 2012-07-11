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
import uit.cnpm02.dkhp.DAO.FacultyDAO;
import uit.cnpm02.dkhp.DAO.LecturerDAO;
import uit.cnpm02.dkhp.DAO.StudentDAO;
import uit.cnpm02.dkhp.model.Faculty;
import uit.cnpm02.dkhp.model.Lecturer;
import uit.cnpm02.dkhp.model.Student;

/**
 *
 * @author thanh
 */
@WebServlet(name = "FacultyManager", urlPatterns = {"/FacultyManager"})
public class FacultyManager extends HttpServlet {

    /** 
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
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
            } else if (action.equalsIgnoreCase("create-faculty")) {
                createNewFaculty(request, response);
            } else if (action.equalsIgnoreCase("delete-faculty")) {
                deleteFaculty(request, response);
            } else if (action.equalsIgnoreCase("edit-faculty")) {
                doPreEditClass(request, response);
            } else if (action.equalsIgnoreCase("update")) {
                doUpdateFaculty(request, response);
            }
        } finally {
            out.close();
        }
    }
     private void doUpdateFaculty(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        try {
            PrintWriter out = response.getWriter();
            FacultyDAO facultyDao = DAOFactory.getFacultyDao();
            String facultyID  = request.getParameter("facultyID");
            String facultyName = request.getParameter("facultyName");
            String deanCode = request.getParameter("deanCode");
            Faculty faculty = facultyDao.findById(facultyID);
            faculty.setFacultyName(facultyName);
            faculty.setDean(deanCode);
            
            List<Faculty> existedFacultys = facultyDao.findByColumName("TruongKhoa", deanCode);
            if (existedFacultys != null && !existedFacultys.isEmpty()) {
                String lecturerName = DAOFactory.getLecturerDao().findById(deanCode).getFullName();
                out.println("Lỗi không thể cập nhật thông tin khoa: <br />"
                        + "- Giảng viên <b>" + lecturerName
                        + "</b> hiện đang là trưởng khoa của khoa <b>" + existedFacultys.get(0).getFacultyName() + "</b>");
                return;
            }
            
            facultyDao.update(faculty);
            out.println("Cập nhật thông tin thành công.");
         } catch (Exception ex) {
            Logger.getLogger(StudentClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     private void writeEditAccountForm(PrintWriter out, Faculty faculty) {
        try {
            LecturerDAO lecturerDao = DAOFactory.getLecturerDao();
            List<Lecturer> lecturer = lecturerDao.findAll();
            out.print("<table style=\"width: 450px;\">");
            out.print("<tr>"
                        + "<td> Mã khoa </td>"
                        + "<td> <input type=\"text\" name=\"txtFacultyID_edit\" id=\"txtFacultyID_edit\" readonly=\"readonly\" value=\""+ faculty.getId() + "\" /></td>"
                    + "</tr>");
             out.print("<tr>"
                        + "<td> Tên khoa </td>"
                        + "<td> <input type=\"text\" name=\"txtFacultyName_edit\" id=\"txtFacultyName_edit\" value=\""+ faculty.getFacultyName() + "\" /></td>"
                    + "</tr>");
            out.print("<tr>"
                + "<td> Trưởng khoa </td>"
                + "<td>");
            out.print("<select name=\"dean\" id=\"dean\" class=\"input-minwidth\">");
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
            out.print("<div id=\"respone-edit-area\"></div>");
        } catch (Exception ex) {
            Logger.getLogger(StudentClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void doPreEditClass(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        try {
            String facultyID = request.getParameter("facultyID");
            FacultyDAO facultyDao = DAOFactory.getFacultyDao();
            Faculty faculty = facultyDao.findById(facultyID);
            PrintWriter out = response.getWriter();
            writeEditAccountForm(out, faculty);
        } catch (Exception ex) {
            Logger.getLogger(StudentClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void deleteFaculty(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String facultyID = request.getParameter("facultyID");
        PrintWriter out = response.getWriter();
        if (facultyID == null) {
            out.println("- Lỗi: Phiên làm việc hết hiệu lực, vui lòng thử lại.");
            return;
        }

        try {
            FacultyDAO facultyDao = DAOFactory.getFacultyDao();
            Faculty faculty = facultyDao.findById(facultyID);
            if (faculty == null) {
                out.println("- Khoa không tồn tại");
                return;
            }
            StudentDAO studentDao = DAOFactory.getStudentDao();
            List<Student> students = studentDao.findByColumName("MaKhoa", facultyID);
            if ((students != null) && !students.isEmpty()) {
                out.println("- Không thể xóa khoa đã có SV.");
                return;
            }

            facultyDao.delete(faculty);
            // Reset data for next F5...
            List<Faculty> allFaculty = facultyDao.findAll();
            HttpSession session = request.getSession();
            session.setAttribute("faculties", allFaculty);
            out.println("Xóa thành công");
        } catch (Exception ex) {
            out.println("- Lỗi xóa khoa: " + ex.toString());
            return;
        }
    }
    private void createNewFaculty(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        String id = request.getParameter("id");
        if (id != null && id.contains(" ")) {
            out.println("- Lỗi: Vui lòng nhập mã khoa không có khoảng trắng.");
            return;
        }
        String name = request.getParameter("name");
        String dean = request.getParameter("dean");
        Faculty faculty = new Faculty(id, name, dean, 0);
        try {
            FacultyDAO facultyDAO = DAOFactory.getFacultyDao();
            Faculty existedFaculty = facultyDAO.findById(id);
            if (existedFaculty != null) {
                out.println("Mã khoa đã tồn tại.");
                return;
            }

            List<Faculty> existedFacultys = facultyDAO.findByColumName("TruongKhoa", dean);
            if (existedFacultys != null && !existedFacultys.isEmpty()) {
                String lecturerName = DAOFactory.getLecturerDao().findById(dean).getFullName();
                out.println("Lỗi không thể tạo khoa: <br />"
                        + "- Giảng viên <b>" + lecturerName
                        + "</b> hiện đang là trưởng khoa của khoa <b>" + existedFacultys.get(0).getFacultyName() + "</b>");
                return;
            }

            facultyDAO.add(faculty);
            List<Faculty> faculties = facultyDAO.findAll();
            HttpSession session = request.getSession();
            session.setAttribute("faculties", faculties);

            out.println("Thêm thành công Khoa:<br />");
            out.println("<br />- Mã khoa: " + id);
            out.println("<br />- Tên khoa: " + name);
            out.println("<br />- Trưởng khoa: " + dean);
        } catch (Exception ex) {
            Logger.getLogger(StudentClassController.class.getName()).log(Level.SEVERE, null, ex);
            out.println("Lỗi: " + ex.toString());
        }
    }

    private void setLecturerName(List<Faculty> facultyList) {
        try {
            LecturerDAO lecturerDao = DAOFactory.getLecturerDao();
            if (facultyList != null && facultyList.size() > 0) {
                for (int i = 0; i < facultyList.size(); i++) {
                    String lecturerName = lecturerDao.findById(facultyList.get(i).getDean()).getFullName();
                    facultyList.get(i).setDeanName(lecturerName);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(StudentClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadDefault(HttpSession session,
            HttpServletResponse response) throws IOException {

        try {
            FacultyDAO facDao = DAOFactory.getFacultyDao();
            List<Faculty> faculties = facDao.findAll();
            setLecturerName(faculties);
            session.setAttribute("faculties", faculties);

            LecturerDAO lecturerDao = DAOFactory.getLecturerDao();
            List<Lecturer> lecturer = lecturerDao.findAll();
            session.setAttribute("lecturers", lecturer);
        } catch (Exception ex) {
            Logger.getLogger(StudentClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
        String path = "./jsps/PDT/ManageFaculty.jsp";
        response.sendRedirect(path);
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
        processRequest(request, response);
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
        processRequest(request, response);
    }

    /** 
     * 
     * @return 
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
