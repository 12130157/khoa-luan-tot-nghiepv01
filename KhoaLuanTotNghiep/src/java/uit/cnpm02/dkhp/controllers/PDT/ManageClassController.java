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
import uit.cnpm02.dkhp.DAO.LecturerDAO;
import uit.cnpm02.dkhp.DAO.SubjectDAO;
import uit.cnpm02.dkhp.DAO.TrainClassDAO;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.model.TrainClassID;
import uit.cnpm02.dkhp.service.ITrainClassService;
import uit.cnpm02.dkhp.service.TrainClassStatus;
import uit.cnpm02.dkhp.service.impl.TrainClassServiceImpl;

/**
 * Manage Class
 * + Open, set up new class
 * + Edit class information
 * + Cacel/delete class
 * 
 * @author LocNguyen
 */
@WebServlet(name = "ManageClassController", urlPatterns = {"/ManageClassController"})
public class ManageClassController extends HttpServlet {
    
    // DAO definition ///
    private TrainClassDAO classDAO = DAOFactory.getTrainClassDAO();
    private SubjectDAO subjectDAO = DAOFactory.getSubjectDao();
    private LecturerDAO lectureDAO = DAOFactory.getLecturerDao();
    
    private ITrainClassService trainClassService = new TrainClassServiceImpl();

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
        
        String requestAction;
        try {
            requestAction = (String) request.getParameter("action");
        } catch (Exception ex) {
             requestAction = ClassFunctionSupported.DEFAULT.getValue();
        }
        
        try {
            if (requestAction.equals(ClassFunctionSupported.DEFAULT.getValue())) {
                // List existed TrainClass, Pagging setup
                defaultAction(request, response);
            } else if (requestAction.equals(ClassFunctionSupported.CREATE.getValue())) {
                // Create new TrainClass
                // Setup data, send to create page
            } else if (requestAction.equals(ClassFunctionSupported.DELETE.getValue())) {
                // Delete a TrainClass specified
            } else if (requestAction.equals(ClassFunctionSupported.UPDATE.getValue())) {
                // Update TrainClass
            }
            
        } finally {
            out.close();
        }
    }
    
    /**
     * Default action
     * This just list trainclass with pagging
     * and send it back to callback function.
     * 
     * @param request request object
     * @param response respone object
     */
    private void defaultAction(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*int currentPage = 1;
        try {
            currentPage = Integer.parseInt((String)request.getAttribute("current-page"));
        } catch (Exception ex) {
            currentPage = 1;
        }*/
        
        List<TrainClass> trainClazzs = trainClassService.getTrainClass(TrainClassStatus.OPEN.getValue());
        //
        // TODO: Should accept only TrainClass of current semeter.
        //
        if ((trainClazzs != null) && (!trainClazzs.isEmpty())) {
            HttpSession session = request.getSession();
            session.setAttribute("train-clazzs", trainClazzs);
        }
        
        String path = "./jsps/PDT/TrainClassManager.jsp";
        response.sendRedirect(path);
        
        return;
    }

    private void retrieveTrainClassForAjaxQuery(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        /*int currentPage = 1;
        try {
            currentPage = Integer.parseInt((String)request.getAttribute("current-page"));
        } catch (Exception ex) {
            currentPage = 1;
        }*/
        
        List<TrainClass> trainClazzs = trainClassService.getTrainClass(TrainClassStatus.OPEN.getValue());
        //
        // TODO: Should accept only TrainClass of current semeter.
        //
        if ((trainClazzs != null) && (!trainClazzs.isEmpty())) {
            HttpSession session = request.getSession();
            session.setAttribute("train-clazzs", trainClazzs);
        }
        
        String path = "./jsps/PDT/TrainClassManager.jsp";
        response.sendRedirect(path);
        
        return;
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
        return "This Serverlet support functionality of Class controller.";
    }// </editor-fold>
    
    /**
     * Create new Class
     * Information about new one get from Request object.
     */
    private TrainClass createNewClass(HttpServletRequest req) {
        // Information need:
        // MaLopHoc	HocKy	NamHoc	MaMH	MaGV	SLSV	SLDK	NgayHoc	CaHoc	PhongHoc
        String id = req.getParameter("classID");
        int semester = Integer.parseInt(req.getParameter("semester"));
        String year = req.getParameter("year");
        String subjectName = req.getParameter("subject");
        String lectureName = req.getParameter("lecturer");
        int SLSV = Integer.parseInt(req.getParameter("slsv"));
        int SLDK = Integer.parseInt(req.getParameter("sldk"));
        String date = req.getParameter("date");
        int shift = Integer.parseInt(req.getParameter("shift"));
        String room = req.getParameter("rool");
        // [Option] NgayThi	CaThi	PhongThi

        TrainClass clazz = null;
        try {
            TrainClassID classID = new TrainClassID(id, year, semester);

            String subjectId = subjectDAO.findByColumName("MaMH", subjectName).get(0).getId();
            String lectureId = lectureDAO.findByColumName("MaGV", lectureName).get(0).getId();

            // Checking pre-condition
            // + 
            
            if (classDAO.findUnique(semester, year, lectureId, SLDK, shift, room) != null) {
                return null; 
                //TODO: Should give a message.
            }
            
            clazz = new TrainClass(id, year, semester, subjectId, lectureId,
                    room, SLSV, SLDK, date, shift, null, "", "");
            clazz.setId(classID);

            classDAO.add(clazz);
        } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return clazz;
    }
    
    private void createNewClassRespond(HttpServletResponse resp, TrainClass clazz) {
        // Just write the result
        // If clzz is NUll, just notify an error message.
    }
    
    /**
     * An enum define all supported function of serverlet
     * .
     */
    public enum ClassFunctionSupported {
        DEFAULT("default"), // List first page of class opened.
        CREATE("create"),   // Create new class form support
        DELETE("delete"),   // Remove class
        UPDATE("update");   // Update
        
        private String description;
        ClassFunctionSupported(String description) {
            this.description = description;
        }
        
        public String getValue() {
            return description;
        }
        
    }
}
