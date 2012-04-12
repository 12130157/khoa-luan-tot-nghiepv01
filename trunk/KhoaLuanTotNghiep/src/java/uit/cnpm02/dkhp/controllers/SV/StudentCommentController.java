/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.controllers.SV;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uit.cnpm02.dkhp.model.Comment;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.model.Student;

/**
 *
 * @author thanh
 */
@WebServlet(name = "StudentCommentController", urlPatterns = {"/StudentCommentController"})
public class StudentCommentController extends HttpServlet {
     /** 
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        try {
          String action = request.getParameter("action");
            if(action.equalsIgnoreCase("send"))
              comment(response, session);
            else if(action.equalsIgnoreCase("complete"))
                sendComment(request, response);
        } finally {            
            out.close();
        }
    }
private void comment(HttpServletResponse response, HttpSession session) throws IOException{
    String path=""; 
      try{
       String user=(String)session.getAttribute("username");
       Student student=DAOFactory.getStudentDao().findById(user);
       session.setAttribute("student", student);
       path= "./jsps/SinhVien/SendComment.jsp";
       }catch(Exception ex){
           path= "./jsps/Message.jsp";
       }
        response.sendRedirect(path);

}    
private void sendComment(HttpServletRequest request,HttpServletResponse response) throws IOException{
     String path=""; 
      try{
          Date todayD = new Date(System.currentTimeMillis());
          SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
          String createDate = dayFormat.format(todayD.getTime());
          String content=request.getParameter("content");
          String author=request.getParameter("txtEmail");
          int id=DAOFactory.getCommentDao().getMaxID()+1;
          Comment coment=new Comment(id, content, author, createDate, 0);
          DAOFactory.getCommentDao().add(coment);
         path= "./jsps/SinhVien/SuccessResult.jsp";
       }catch(Exception ex){
           path= "./jsps/Message.jsp";
      }
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
