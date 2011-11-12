package uit.cnpm02.dkhp.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uit.cnpm02.dkhp.DAO.AccountDAO;
import uit.cnpm02.dkhp.DAO.NewsDAO;
import uit.cnpm02.dkhp.model.Account;
import uit.cnpm02.dkhp.model.News;
/**
 *
 * @author LocNguyen
 */
@WebServlet(name = "Test1", urlPatterns = {"/Test1"})
public class Test1 extends HttpServlet {
    private AccountDAO accountDao = new AccountDAO();
    private NewsDAO newsDao = new NewsDAO();

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
        PrintWriter out = response.getWriter();
        
        testAddNews(out);
        
        try {
            List<News> news = newsDao.findAll();
            
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Test</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Test at " + request.getContextPath() + "</h1>");

            if ((news != null) && (news.size() > 0)) {
                out.println("<table>");
                for (int i = 0; i < news.size(); i++) {
                    out.println("<tr>");
                    out.println("<td>");
                    out.println(news.get(i).getTitle());
                    out.println("</td>");
                    out.println("<td>");
                    out.println(news.get(i).getContent());
                    out.println("</td>");

                    out.println("</tr>");
                }
                out.println("</table>");
            }
            out.println("</body>");
            out.println("</html>");

        } catch (Exception ex) {
            Logger.getLogger(Test1.class.getName()).log(Level.SEVERE, null, ex);
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

    private void testAddNews(PrintWriter out) {
        News news = new News("BT06", "Nghi hoc chieu thu 7", "Thong bao cho sinh vien CNPM 02, tat ca dc nghi ngay chu nhat.", "Loc Nguyen", new Date(), 0);
        int id = 0;
        try {
            id = newsDao.add(news);
        } catch (Exception ex) {
            Logger.getLogger(Test1.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            News result = newsDao.findById(id);
            
            if(result != null) {
                out.println(result.getTitle() + " --- " + result.getContent());
            }
        } catch (Exception ex) {
            Logger.getLogger(Test1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void testAdd(PrintWriter out) {
        Account acc = new Account("locnv", "123", "Nguyen Van Loc", 0, "Normal", 1);
        int id = -1;
        try {
            id = accountDao.add(acc);
        } catch (Exception ex) {
            Logger.getLogger(Test1.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Account result = accountDao.findById(id);
            
            if(result != null) {
                out.println(result.getUserName() + " --- " + result.getPassword());
            }
        } catch (Exception ex) {
            Logger.getLogger(Test1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}