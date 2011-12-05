/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.controllers.SV;

import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uit.cnpm02.dkhp.DAO.PreSubjectDAO;
import uit.cnpm02.dkhp.DAO.SubjectDAO;
import uit.cnpm02.dkhp.model.PreSubject;

/**
 *
 * @author thanh
 */
@WebServlet(name = "PreSubjectController", urlPatterns = {"/PreSubjectController"})
public class PreSubjectController extends HttpServlet {
PreSubjectDAO preDao=new PreSubjectDAO();
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
            if(action.equalsIgnoreCase("view"))
            viewPreSubject(response, session);
        } finally {            
            out.close();
        }
    }
private void viewPreSubject(HttpServletResponse response, HttpSession session) throws IOException{
    String path="";  
       try{
           List<PreSubject> preSub=preDao.findAll();
           setSubjectName(preSub);
           session.setAttribute("preSub", preSub);
         path = "./jsps/SinhVien/PreSubject.jsp";
       }catch(Exception ex){
           path= "./jsps/Message.jsp";
       }
        response.sendRedirect(path);
}
private void setSubjectName(List<PreSubject> preSub) throws Exception{
    SubjectDAO subjectDao=new SubjectDAO();
    for(int i=0;i<preSub.size();i++){
        preSub.get(i).setPreSubjectName(subjectDao.findById(preSub.get(i).getId().getPreSudId()).getSubjectName());
        preSub.get(i).setSubjectName(subjectDao.findById(preSub.get(i).getId().getSudId()).getSubjectName());
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
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
