package uit.cnpm02.dkhp.controllers.PDT;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.SubjectDAO;
import uit.cnpm02.dkhp.model.Subject;
import uit.cnpm02.dkhp.utilities.Constants;

/**
 * This controller support function to manage subject include
 *  + ListSubject
 *  + InsertNewSubject
 *  + DeleteSubject
 *  + SearchSubject
 * 
 * @author LocNguyen
 */
@WebServlet(name = "ManageSubjectController", urlPatterns = {"/ManageSubjectController"})
public class ManageSubjectController extends HttpServlet {

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
        session.removeAttribute("error");
        String path = "./jsps/PDT/SubjectManager.jsp";

        try {
            String action = request.getParameter("function");
            if ((action == null) || action.isEmpty()) {
                // Not support functions.
                return;
            }
            
            if (action.equals("list_subject")) {
                listSubject(request, response);
                path = "./jsps/PDT/SubjectManager.jsp";
            } else if (action.equals("add_subject")) {
                out.println("Add subject.");
            } else if (action.equals("delete_subject")) {
                out.println("Delete subject.");
            } else if (action.equals("edit_subject")) {
                out.println("Edit subject.");
            }
            
            response.sendRedirect(path);
        }catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            out.close();
        }
    }
    
    private void listSubject(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        SubjectDAO subDao = DAOFactory.getSubjectDao();
        List<Subject> subjects = subDao.findAll(Constants.ELEMENT_PER_PAGE_DEFAULT, 1, null, null);
        
        HttpSession session = req.getSession();
        session.setAttribute("list_subject", subjects);
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
