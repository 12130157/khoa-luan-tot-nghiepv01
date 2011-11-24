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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uit.cnpm02.dkhp.DAO.NewsDAO;
import uit.cnpm02.dkhp.model.News;

/**
 *
 * @author thanh
 */
@WebServlet(name = "NewsController", urlPatterns = {"/NewsController"})
public class NewsController extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
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
                NewsManeger(response, session);
            else if(action.equalsIgnoreCase("update"))
                updateNews(response, request, session);
            else if(action.equalsIgnoreCase("delete"))
                deleteNews(response, request, session);
            else if(action.equalsIgnoreCase("insert"))
                insertNew(response, request, session);
        } finally {            
            out.close();
        }
    }
    private void insertNew(HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception{
        NewsDAO newsDao=new NewsDAO();
        int id=newsDao.getMaxID()+1;
        String tille=request.getParameter("NewsTiltle");
        String content=request.getParameter("newscontent");
        int type=Integer.parseInt(request.getParameter("Type"));
        String author = (String)session.getAttribute("username");
        Date todayD = new Date(System.currentTimeMillis());
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
        String createDate = dayFormat.format(todayD.getTime());
        News news=new News(id, tille, content, author, createDate, type);
        newsDao.add(news);
        NewsManeger(response, session);
    }
    /**
     * 
     * @param response
     * @param request
     * @param session
     * @throws Exception 
     */
    private void deleteNews(HttpServletResponse response, HttpServletRequest request, HttpSession session) throws Exception{
        int id= Integer.parseInt(request.getParameter("Id"));
        NewsDAO newsDao=new NewsDAO();
        try {
            newsDao.deleteNewsByID(id);
            NewsManeger(response, session);
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
        NewsDAO newsDao=new NewsDAO();
        try {
            newsDao.updateNewsStatus(id);
            NewsManeger(response, session);
        } catch (Exception ex) {
            Logger.getLogger(NewsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    /**
     * 
     * @param response
     * @param session 
     */
    private void NewsManeger(HttpServletResponse response, HttpSession session){
        NewsDAO newsDao=new NewsDAO();
        String path="./jsps/PDT/NewsManager.jsp";
        try {
            List<News> newsList=newsDao.findAll();
            session.setAttribute("newsList", newsList);
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
    NewsDAO newsDao=new NewsDAO();
    News news=newsDao.findById(id);
    session.setAttribute("newsdetail", news);
    if(actor.equalsIgnoreCase("normal"))
         path = "./jsps/NewsDetail.jsp";
    else if(actor.equalsIgnoreCase("Student"))
        path = "./jsps/SinhVien/NewsDetail.jsp";
    else if(actor.equalsIgnoreCase("Lecturer"))
        path = "./jsps/GiangVien/NewsDetail.jsp";
    else if(actor.equalsIgnoreCase("PDT"))
        path = "./jsps/PDT/NewsDetail.jsp";
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
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(NewsController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(NewsController.class.getName()).log(Level.SEVERE, null, ex);
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
