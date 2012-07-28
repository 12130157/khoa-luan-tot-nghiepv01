/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.controllers.PDT;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import uit.cnpm02.dkhp.DAO.AccountDAO;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.DiaryDAO;
import uit.cnpm02.dkhp.model.Account;
import uit.cnpm02.dkhp.model.Diary;
import uit.cnpm02.dkhp.model.type.AccountStatus;
import uit.cnpm02.dkhp.model.type.AccountType;
import uit.cnpm02.dkhp.utilities.DateTimeUtil;
import uit.cnpm02.dkhp.utilities.StringUtils;

/**
 *
 * @author LocNguyen
 */
@WebServlet(name = "DiaryController", urlPatterns = {"/DiaryController"})
public class DiaryController extends HttpServlet {

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
            } else if (action.equalsIgnoreCase("get-user-detail")) {
                getUserDetail(request, response);
            } else if(action.equalsIgnoreCase("get-content-detail")) {
                getContentDetail(request, response);
            } else if (action.equalsIgnoreCase("search-log")) {
                searchLogs(request, response);
            } else if (action.equalsIgnoreCase("search-log-by-date")) {
                searchLogByDate(request, response);
            }/* else if (action.equalsIgnoreCase("remove-subject-to-traing-prog")) {
                deleteSubFromTrainProg(request, response);
            } else if (action.equals("delete-train-prog")) {
                deleteTrainProgram(request, response);
            }*/
        } catch (Exception ex) {
            //
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
        DiaryDAO diaryDao = DAOFactory.getDiaryDao();
        
        int recordPerPage = 50;
        try {
            List<Diary> diaries = diaryDao.findAll(recordPerPage, 1, "Ngay", null);
            if (diaries != null && !diaries.isEmpty()) {
                session.setAttribute("diaries", diaries);
            }
        } catch (Exception ex) {
            Logger.getLogger(DiaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        String path = "./jsps/PDT/ManageDiary.jsp";
        response.sendRedirect(path);
    }

    private void getUserDetail(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String username = request.getParameter("user");
        AccountDAO accDao = DAOFactory.getAccountDao();
        PrintWriter out = response.getWriter();
        out.println("<div id=\"popup-title\">Thông tin user</div>");
        Account acc;
        try {
            acc = accDao.findById(username);
            if (acc == null) {
                return;
            }
            
            out.println("<div id=\"popup-content\">");
            out.println("<br />- Usernam: " + username);
            out.println("<br />- Họ và tên: " + acc.getFullName());
            out.println("<br />- Strạng thái: " + AccountStatus.getDescription(acc.getStatus()));
            out.println("<br />- Loại: " + AccountType.getDescription(acc.getType()));
            out.println("<hr />");
            out.println("</div>");
            
        } catch (Exception ex) {
            Logger.getLogger(DiaryController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
    }

    private void getContentDetail(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String diaryId = request.getParameter("id");
        DiaryDAO diaryDao = DAOFactory.getDiaryDao();
        PrintWriter out = response.getWriter();
        out.println("<div id=\"popup-title\">Nội dung</div>");
        try {
            int idInt = Integer.parseInt(diaryId);
            Diary d = diaryDao.findById(idInt);
            if (d == null) {
                return;
            }
            out.println("<div id=\"popup-content\">");
            out.println("<br /><i>- " + d.getContent() + "</i>");
            out.println("<hr />");
            out.println("</div>");
            
        } catch (Exception ex) {
            Logger.getLogger(DiaryController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    private void searchLogs(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String key = request.getParameter("key");
        if (StringUtils.isEmpty(key)) {
            return;
        }
        
        PrintWriter out = response.getWriter();
        
        List<Diary> diaries = new ArrayList<Diary>(10);
        try {
            List<Diary> temp = new ArrayList<Diary>(10);
            DiaryDAO diaryDao = DAOFactory.getDiaryDao();
            temp = diaryDao.findByColumName("TaiKhoan", key);
            if (temp != null && !temp.isEmpty()) {
                diaries.addAll(temp);
            }
            temp = diaryDao.findByColumName("NoiDung", key);
            if (temp != null && !temp.isEmpty()) {
                diaries.addAll(temp);
            }
            temp = diaryDao.findByColumName("Ngay", key);
            if (temp != null && !temp.isEmpty()) {
                diaries.addAll(temp);
            }
        } catch (Exception ex) {
            Logger.getLogger(DiaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (!diaries.isEmpty()) {
            writeOutListLogs(out, diaries);
        }
        
    }

    private void writeOutListLogs(PrintWriter out, List<Diary> diaries) {
        out.println("<tr><th>STT</th><th>User</th><th>Nội dung</th><th>Thời gian</th></tr>");
        
        for (int i = 0; i < diaries.size(); i ++) {
            Diary d = diaries.get(i);
            out.println("<tr>");
            
            out.println("<td>" + (i + 1) + "</td>");
            out.println("<td> <span class=\"atag\" onclick=\"loadUserDetail('" + d.getUserName() + "')\">" +d.getUserName() + "</span> </td>");
            out.println("<td> <span class=\"atag\" onclick=\"loadContent(" +d.getId()+ ")\">" + d.getContent() +"</span></td>");
            out.println("<td>" + d.getDate() +" </td>");
            out.println("</tr>");
        }
    }

    private void searchLogByDate(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String fromDateStr = request.getParameter("fromdate");
        String toDateStr = request.getParameter("todate");
        if (StringUtils.isEmpty(fromDateStr)
                || StringUtils.isEmpty(toDateStr)) {
            return;
        }
        Date fromDate = null;
        Date toDate = null;
        try {
            fromDate = DateTimeUtil.parse(fromDateStr);
            toDate = DateTimeUtil.parse(toDateStr);
            
            if (fromDate == null || toDate == null || fromDate.after(toDate)) {
                return;
            }
            fromDate = new Date(fromDate.getTime() - 24*60*60);
            toDate = new Date(toDate.getTime() + 24*60*60);
            
        } catch (Exception ex) {
            //Do nothing...
        }
        
        PrintWriter out = response.getWriter();
        
        List<Diary> diaries = new ArrayList<Diary>(10);
        try {
            List<Diary> temp = new ArrayList<Diary>(10);
            DiaryDAO diaryDao = DAOFactory.getDiaryDao();
            temp = diaryDao.findAll();
            if (temp != null && !temp.isEmpty()) {
                //diaries.addAll(temp);
                
                for (Diary d : temp) {
                    if ((d.getDate() != null)
                            && d.getDate().after(fromDate)
                            && d.getDate().before(toDate)) {
                        
                        diaries.add(d);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(DiaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (!diaries.isEmpty()) {
            writeOutListLogs(out, diaries);
        }
    }
}
