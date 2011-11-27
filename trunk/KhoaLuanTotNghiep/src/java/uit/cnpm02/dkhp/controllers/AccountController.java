package uit.cnpm02.dkhp.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uit.cnpm02.dkhp.DAO.AccountDAO;
import uit.cnpm02.dkhp.DAO.StudentDAO;
import uit.cnpm02.dkhp.model.Account;
import uit.cnpm02.dkhp.model.Student;
import uit.cnpm02.dkhp.DAO.ClassDAO;
import uit.cnpm02.dkhp.DAO.CourseDAO;
import uit.cnpm02.dkhp.DAO.FacultyDAO;
import uit.cnpm02.dkhp.model.Class;
import uit.cnpm02.dkhp.model.Course;
import uit.cnpm02.dkhp.model.Faculty;


/**
 *
 * @author LocNguyen
 */
@WebServlet(name = "AccountController", urlPatterns = {"/AccountController"})
public class AccountController extends HttpServlet {

    private AccountDAO accDao = new AccountDAO();
    private StudentDAO studentDao=new StudentDAO();
    private ClassDAO classDao= new ClassDAO();
    private FacultyDAO facultyDao=new FacultyDAO();
    private CourseDAO courseDao=new CourseDAO();
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        String action=request.getParameter("action");
        try {
            if(action.equalsIgnoreCase("changePass"))
                changePass(request, response, session);
            else if(action.equalsIgnoreCase("Info"))
                getInfo(response, session);
            else if(action.equals("changeinfo"))
                changeInfo(response, session);
            else if(action.equalsIgnoreCase("update"))
                updateInfo(request, response, session);
        } catch (Exception ex) {
            Logger.getLogger(AccountController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }

    }
    private void updateInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception{
         String user=(String)session.getAttribute("username");
         Student student=studentDao.findById(user);
         String IndentityCard=request.getParameter("IdentityCard");
         String gender=request.getParameter("gender");
         String home=request.getParameter("home");
         String address=request.getParameter("address");
         String  phone =request.getParameter("phone");
         String email=request.getParameter("email");
         student.setIdentityCard(IndentityCard);
         student.setGender(gender);
         student.setHome(home);
         student.setAddress(address);
         student.setPhone(phone);
         studentDao.update(student);
         getInfo(response, session);
    }
    /**
     * 
     * @param response
     * @param session
     * @throws Exception 
     */
    private void changeInfo(HttpServletResponse response, HttpSession session) throws Exception{
        String path="./jsps/SinhVien/UpdateInfo.jsp";
        String user=(String)session.getAttribute("username");
        Student student=studentDao.findById(user);
        session.setAttribute("student", student);
        response.sendRedirect(path);
    }
    /**
     * 
     * @param response
     * @param session
     * @throws Exception 
     */
    private void getInfo(HttpServletResponse response, HttpSession session) throws Exception{
        String path="./jsps/SinhVien/Info.jsp";
        String user=(String)session.getAttribute("username");
        Student student=studentDao.findById(user);
        Class classes=classDao.findById(student.getClassCode());
        Faculty faculty=facultyDao.findById(student.getFacultyCode());
        Course course=courseDao.findById(student.getCourseCode());
        session.setAttribute("student", student);
        session.setAttribute("classes", classes);
        session.setAttribute("faculty", faculty);
        session.setAttribute("course", course);
        response.sendRedirect(path);
    }
    /**
     * 
     * @param request
     * @param response
     * @param session
     * @throws Exception 
     */
private void changePass(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception{
   String path="./jsps/SinhVien/ChangePass.jsp";
    try{
    String user=(String) session.getAttribute("username");
    Account account=accDao.findById(user);
    String newPass=request.getParameter("newpass");
    account.setPassword(newPass);
    accDao.update(account);
     session.setAttribute("password", newPass);
     session.setAttribute("message", "Đổi mật khẩu thành công");
     response.sendRedirect(path);
    }catch(Exception ex){
        session.setAttribute("message", "Đổi mật khẩu thất bại");
        response.sendRedirect(path);
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
