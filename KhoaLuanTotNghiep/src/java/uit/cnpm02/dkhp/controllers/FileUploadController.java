package uit.cnpm02.dkhp.controllers;

import java.io.File;
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
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.service.IFileUploadService;
import uit.cnpm02.dkhp.service.ITrainClassService;
import uit.cnpm02.dkhp.service.impl.FileUploadServiceImpl;
import uit.cnpm02.dkhp.service.impl.TrainClassServiceImpl;
import uit.cnpm02.dkhp.utilities.FileInfo;
import uit.cnpm02.dkhp.utilities.MultipartMap;
import uit.cnpm02.dkhp.utilities.StringUtils;

/**
 *
 * @author LocNguyen
 */
@WebServlet(name = "FileUploadController", urlPatterns = {"/FileUploadController"})
public class FileUploadController extends HttpServlet {

    private IFileUploadService fuService = new FileUploadServiceImpl();
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
        HttpSession session = request.getSession();
        session.removeAttribute("msg");

        try {
            String action = request.getParameter("function");
            if (action.equalsIgnoreCase(FileUploadSupport
                    .DEFAULT.getValue())) {
                doDefaultAction(request, response);
            } else if (action.equalsIgnoreCase(FileUploadSupport
                    .VALIDATE_FILENAME.getValue())) {
                doValidateFileName(request, response);
            } else if (action.equalsIgnoreCase(FileUploadSupport
                    .UPLOAD_FILE.getValue())) {
                doUploadFile(request, response);
            }
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

    private void doUploadFile(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        try {
            
            MultipartMap multiPart = new MultipartMap(request, this);
            doDefaultAction(request, response);
        } catch (ServletException ex) {
            Logger.getLogger(FileUploadController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    private void doDefaultAction(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        if (!StringUtils.isEmpty(username)) {
            List<FileInfo> files = new ArrayList<FileInfo>(10);
            fuService.getFile(username, files, username);
            if (!files.isEmpty()) {
                session.setAttribute("files", files);
            }
        }        
        String path = "./jsps/GiangVien/FileManager.jsp";
        response.sendRedirect(path);
    }

    private void doValidateFileName(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        // Validate file name
        // Lecturer upload file score must in specified format
        // The name of file must be class code

        String fileName = (String) request.getParameter("txtPath");
        String lecturerId = (String) request.getSession().getAttribute("username");
        
        // C:/CAR.001.xls ==> CAR.001
        fileName = fileName.substring(0, fileName.length() - 4);
        List<TrainClass> trainClass = trainClassService.getCurrentTrainClass(lecturerId);
        if ((trainClass == null) || trainClass.isEmpty()) {
            out.append("error - GV hiện không dạy lớp nào.");
            return;
        }
        
        for (TrainClass tc : trainClass) {
            if (fileName.equalsIgnoreCase(tc.getId().getClassCode())) {
                out.append("Oh, Success.");
                return;
            }
        }
        
        out.append("error - Tên file không đúng định dạng.");
    }

    //#############################################
    public enum FileUploadSupport {
        DEFAULT("default"),
        VALIDATE_FILENAME("validate-filename"),
        UPLOAD_FILE("upload");
        
        private String description;
        FileUploadSupport(String description) {
            this.description = description;
        }
        
        public String getValue() {
            return description;
        }
    }

}
