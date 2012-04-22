package uit.cnpm02.dkhp.controllers.PDT;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import uit.cnpm02.dkhp.model.Lecturer;
import uit.cnpm02.dkhp.model.Subject;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.model.TrainClassID;
import uit.cnpm02.dkhp.model.web.LecturerWeb;
import uit.cnpm02.dkhp.model.web.SubjectWeb;
import uit.cnpm02.dkhp.service.ITrainClassService;
import uit.cnpm02.dkhp.service.TrainClassStatus;
import uit.cnpm02.dkhp.service.impl.TrainClassServiceImpl;
import uit.cnpm02.dkhp.utilities.BOUtils;
import uit.cnpm02.dkhp.utilities.ExecuteResult;

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
            } else if (requestAction.equals(ClassFunctionSupported.PRECREATE.getValue())) {
                preCreateNewTrainClass(request);
                String path = "./jsps/PDT/AddTrainClass.jsp";
                response.sendRedirect(path);
                return;
            } else if (requestAction.equals(ClassFunctionSupported.CREATE.getValue())) {
                ExecuteResult result = createNewTrainClass(request);
                writeRespondErrorMessage(result, out);
                return;
                
            } else if (requestAction.equals(ClassFunctionSupported.DELETE.getValue())) {
                // Delete a TrainClass specified
            } else if (requestAction.equals(ClassFunctionSupported.UPDATE.getValue())) {
                // Update TrainClass
                updateTrainClass(request, response);
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
    private ExecuteResult createNewTrainClass(HttpServletRequest req) {
        // Information need:
        // MaLopHoc	HocKy	NamHoc	MaMH	MaGV	SLSV	SLDK	NgayHoc	CaHoc	PhongHoc
        String id = req.getParameter("classcode");
        //int semester = Constants.CURRENT_SEMESTER;
        int semester = BOUtils.getCurrentSemeter(1);
        //String year = Constants.CURRENT_YEAR;
        String year = BOUtils.getCurrentYear("2011-2012");
        String subjectCode = req.getParameter("subject");
        String lectureCode = req.getParameter("lecturer");
        int SLSV = Integer.parseInt(req.getParameter("slsv"));
        int SLDK = 0;
        int date = Integer.parseInt(req.getParameter("Date"));
        int shift = Integer.parseInt(req.getParameter("Shift"));
        String room = req.getParameter("room");

        TrainClass clazz = null;
        try {
            TrainClassID classID = new TrainClassID(id, year, semester);
            clazz = new TrainClass(id, year, semester, subjectCode, lectureCode,
                    room, SLSV, SLDK, date, shift, null, "", "");
            clazz.setId(classID);
            
            return trainClassService.addNewTrainClass(clazz);
        } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private void createNewClassRespond(HttpServletResponse resp, TrainClass clazz) {
        // Just write the result
        // If clzz is NUll, just notify an error message.
    }

    private void preCreateNewTrainClass(HttpServletRequest req) {
        HttpSession session = req.getSession();
        if (session == null) {
            //
        }
        
        try {
            ArrayList<Subject> subjects = (ArrayList<Subject>) subjectDAO.findAll();
            ArrayList<Lecturer> lecturers = (ArrayList<Lecturer>) lectureDAO.findAll();

            if ((subjects != null) && (!subjects.isEmpty())) {
                ArrayList<SubjectWeb> sws = new ArrayList<SubjectWeb>(10);
                for (Subject s : subjects) {
                    SubjectWeb sw = new SubjectWeb(s.getId(), s.getSubjectName());
                    sws.add(sw);
                }
                session.setAttribute("subjects", sws);
            }

            if ((lecturers != null) && (!lecturers.isEmpty())) {
                ArrayList<LecturerWeb> lws = new ArrayList<LecturerWeb>(10);
                
                for (Lecturer l : lecturers) {
                    LecturerWeb lw = new LecturerWeb(l.getId(), l.getFullName());
                    lws.add(lw);
                }
                session.setAttribute("lecturers", lws);
            }
        } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateTrainClass(HttpServletRequest request, HttpServletResponse response) {
        //
        // Retrieve TrainClass's name
        //
        
        //
        // Query out in database
        //
        
        //
        // S
        //
    }

    private void writeRespondErrorMessage(ExecuteResult result, PrintWriter out) {
        out.println(result.getMessage());
        if (result.isIsSucces()) {
            TrainClass data = (TrainClass) result.getData();
            out.println("<table id = \"table-list-train-class\" name = \"table-list-train-class\">");
            
            out.println("<tr>" +
                        //"<th> STT </th>" +
                        "<th> Lớp học </th>" +
                        "<th> Môn học </th>" +
                        "<th> Giảng viên </th>" +
                        "<th> Thứ </th>" +
                        "<th> Phòng </th>" +
                        "<th> Số lượng </th>" +
                        "</tr>");
            out.println("<td> " + data.getId().getClassCode() + " </td>");
            out.println("<td> " + data.getSubjectName() + " </td>");
            out.println("<td> " + data.getLectturerName() + " </td>");
            out.println("<td> " + data.getStudyDate() + " </td>");
            out.println("<td> " + data.getClassRoom() + " </td>");
            out.println("<td> " + data.getNumOfStudent() + " </td>");
            out.println("</tr>");
            out.println("</table>");
        }
    }

    /**
     * An enum define all supported function of serverlet
     * .
     */
    public enum ClassFunctionSupported {
        DEFAULT("default"), // List first page of class opened.
        PRECREATE("pre_create"),
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
