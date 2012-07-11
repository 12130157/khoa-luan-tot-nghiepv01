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
import uit.cnpm02.dkhp.DAO.CourseDAO;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.StudentDAO;
import uit.cnpm02.dkhp.model.Course;
import uit.cnpm02.dkhp.model.Student;

/**
 *
 * @author thanh
 */
@WebServlet(name = "CourseManager", urlPatterns = {"/CourseManager"})
public class CourseManager extends HttpServlet {

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
            } else if (action.equalsIgnoreCase("create-course")) {
                createNewCourse(request, response);
            } else if (action.equalsIgnoreCase("delete-course")) {
                deleteCourse(request, response);
            } 
        } finally {
            out.close();
        }
    }
     private void deleteCourse(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String courseID = request.getParameter("courseID");
        PrintWriter out = response.getWriter();
        if (courseID == null) {
            out.println("- Lỗi: Phiên làm việc hết hiệu lực, vui lòng thử lại.");
            return;
        }

        try {
            CourseDAO courseDao = DAOFactory.getCourseDao();
            Course course = courseDao.findById(courseID);
            if (course == null) {
                out.println("- Khóa học không tồn tại");
                return;
            }
            StudentDAO studentDao = DAOFactory.getStudentDao();
            List<Student> students = studentDao.findByColumName("MaKhoaHoc", courseID);
            if ((students != null) && !students.isEmpty()) {
                out.println("- Không thể xóa lớp đã có SV.");
                return;
            }

            courseDao.delete(course);
           
            // Reset data for next F5...
            List<Course> allCourse = courseDao.findAll();
            HttpSession session = request.getSession();
            session.setAttribute("courses", allCourse);
            out.println("Xóa thành công");
        } catch (Exception ex) {
            out.println("- Lỗi xóa lớp: " + ex.toString());
            return;
        }
    }
    private void createNewCourse(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        String id = request.getParameter("id");
        if (id != null && id.contains(" ")) {
            out.println("- Lỗi: Vui lòng nhập mã lớp không có khoảng trắng.");
            return;
        }
        int yearIn = Integer.parseInt(request.getParameter("yearIn"));
        int yearOut = Integer.parseInt(request.getParameter("yearOut"));
        int numOfSemester = (yearOut - yearIn)*2;
        Course course = new Course(id, yearIn, yearOut, numOfSemester, 0);
        try {
            CourseDAO courseDao = DAOFactory.getCourseDao();
            Course existedCourse = courseDao.findById(id);
            if (existedCourse != null) {
                out.println("Mã khóa đã tồn tại.");
                return;
            }
            courseDao.add(course);    
            out.println("Thêm thành công lớp:<br />");
            out.println("<br />- Mã khóa: " + id);
            out.println("<br />- Năm bắt đầu: " + yearIn);
            out.println("<br />- Năm kết thúc: " + yearOut);
        } catch (Exception ex) {
            Logger.getLogger(StudentClassController.class.getName()).log(Level.SEVERE, null, ex);
            out.println("Lỗi: " + ex.toString());
        }
    }

    private void loadDefault(HttpSession session,
            HttpServletResponse response) throws IOException {

        try {
            CourseDAO courseDao = DAOFactory.getCourseDao();
            List<Course> courses = courseDao.findAll();
            session.setAttribute("courses", courses);

        } catch (Exception ex) {
            Logger.getLogger(StudentClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
        String path = "./jsps/PDT/ManageCourse.jsp";
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
