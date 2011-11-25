/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uit.cnpm02.dkhp.DAO.CommentDao;
import uit.cnpm02.dkhp.model.Comment;
import java.util.List;

/**
 *
 * @author thanh
 */
@WebServlet(name = "CommentController", urlPatterns = {"/CommentController"})
public class CommentController extends HttpServlet {

    /** 
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        try {
            String action = request.getParameter("action");
            if(action.equalsIgnoreCase("detail"))
                NewsDetail(response, request, session);
            else if(action.equalsIgnoreCase("manager"))
                CommentManeger(response, session);
            else if(action.equalsIgnoreCase("update"))
                updateNews(response, request, session);
            else if(action.equalsIgnoreCase("delete"))
                deleteCommnet(response, request, session);
            else if(action.equalsIgnoreCase("insert"))
                insertNew(response, request, session);
            else if(action.equalsIgnoreCase("Filter"))
                filterNews(request, response);
         } finally {            
            out.close();
        }
    }
 private void filterNews(HttpServletRequest request, HttpServletResponse response) throws Exception{
        PrintWriter out = response.getWriter();
        int currentPage=Integer.parseInt(request.getParameter("curentPage"));
        CommentDao commentDao=new CommentDao();
        List<Comment> commentList=commentDao.findAll(10, currentPage, "NgayGui", "DESC");
        if (commentList.isEmpty()==false) {
            out.println("<tr><th>STT</th><th>Người gửi</th><th>Nội dung</th><th>Ngày gửi</th><th>Tình trạng</th><th>Xem</th><th>Xóa</th></tr>");
            for (int i = 0; i < commentList.size(); i++) {
                StringBuffer str = new StringBuffer();
                str.append("<tr><td>").append((currentPage-1)*10 + 1 + i).append("</td>");
                str.append("<td>").append(commentList.get(i).getAuthor()).append("</td>");
                str.append("<td>").append(commentList.get(i).getContent()).append("</td>");
                str.append("<td>").append(commentList.get(i).getCreateDate()).append("</td>");
                if(commentList.get(i).getStatus()==1)
                    str.append("<td>Đã xem</td>");   
                else 
                    str.append("<td>Chưa xem</td>"); 
                
                str.append("<td><a href='../../CommentController?action=detail&Id=").append(commentList.get(i) .getId()).append("'>Xem</a></td>");
                str.append("<td><a href='../../CommentController?action=delete&Id=").append(commentList.get(i) .getId()).append("'>Xóa</a></td>");
                out.println(str.toString());
            }
        }
    }
    /**
     * 
     * @param response
     * @param request
     * @param session
     * @throws Exception 
     */
    private void insertNew(HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception{
        CommentDao commentDao=new CommentDao();
        int id=commentDao.getMaxID()+1;
        String tille=request.getParameter("NewsTiltle");
        String content=request.getParameter("newscontent");
        int type=Integer.parseInt(request.getParameter("Type"));
        String author = (String)session.getAttribute("username");
        Date todayD = new Date(System.currentTimeMillis());
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
        String createDate = dayFormat.format(todayD.getTime());
        
        
        CommentManeger(response, session);
    }
    /**
     * 
     * @param response
     * @param request
     * @param session
     * @throws Exception 
     */
    private void deleteCommnet(HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception{
        int id= Integer.parseInt(request.getParameter("Id"));
        CommentDao commentDao=new CommentDao();
        try {
            commentDao.deleteCommentByID(id);
            CommentManeger(response, session);
        } catch (SQLException ex) {
            Logger.getLogger(NewsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * 
     * @param response
     * @param request
     * @param session 
     */
    private void updateNews(HttpServletResponse response, HttpServletRequest request, HttpSession session){
        int id= Integer.parseInt(request.getParameter("Id"));
        CommentDao commentDao=new CommentDao();
        try {
            commentDao.updateCommentStatus(id);
            CommentManeger(response, session);
        } catch (Exception ex) {
            Logger.getLogger(NewsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    /**
     * 
     * @param response
     * @param session 
     */
    private void CommentManeger(HttpServletResponse response, HttpSession session) throws Exception{
        CommentDao commentDao= new CommentDao();
        int rows=commentDao.getRowsCount();
        int numpage=0;
        if(rows%10==0) numpage=rows/10;
        else numpage=rows/10+1;
        String path="./jsps/PDT/CommentManager.jsp";
        try {
            List<Comment> commnetList=commentDao.findAll(10, 1, "NgayGui", "DESC");
            session.setAttribute("commentList", commnetList);
            session.setAttribute("numpage", numpage);
            response.sendRedirect(path);
        } catch (Exception ex) {
            Logger.getLogger(NewsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * 
     * @param response
     * @param request
     * @param session
     * @throws Exception 
     */
private void NewsDetail(HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception{
     int id= Integer.parseInt(request.getParameter("Id"));
    String actor=request.getParameter("Actor");
    String path="";
    CommentDao commentDao=new CommentDao();
    Comment comment=commentDao.findById(id);
    commentDao.updateCommentStatus(id);
    session.setAttribute("commentdetail", comment);
     path = "./jsps/PDT/CommentDetail.jsp";
     response.sendRedirect(path);
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
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(CommentController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(CommentController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
