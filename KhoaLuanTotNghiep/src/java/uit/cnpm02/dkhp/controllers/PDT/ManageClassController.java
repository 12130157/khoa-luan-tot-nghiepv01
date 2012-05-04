package uit.cnpm02.dkhp.controllers.PDT;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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
import uit.cnpm02.dkhp.service.impl.TrainClassServiceImpl;
import uit.cnpm02.dkhp.utilities.Constants;
import uit.cnpm02.dkhp.utilities.DateTimeUtil;
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
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
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
            } else if (requestAction.equals(ClassFunctionSupported.PREUPDATE.getValue())) {
                // Prepare to update TrainClass
                preUpdateTrainClass(request, response);
            }else if (requestAction.equals(ClassFunctionSupported.UPDATE.getValue())) {
                // Update TrainClass
                 updateTrainClass(request, response);
            }else if (requestAction.equals(ClassFunctionSupported.DETAIL.getValue())) {
                // view detail train class
                viewDetailClass(request, response);
            }else if(requestAction.equalsIgnoreCase(ClassFunctionSupported.FILTER.getValue())){
                filterTrainClass(request, response);
            }else if(requestAction.equalsIgnoreCase(ClassFunctionSupported.SEARCH.getValue())){
             searchTrainClass(request, response);   
            }
            
        } finally {
            out.close();
        }
    }
    private void searchTrainClass(HttpServletRequest request, HttpServletResponse response){
        try {
        String value = request.getParameter("value");
         List<TrainClass> trainClassList = new ArrayList<TrainClass>(10);
          List<TrainClass> temp = classDAO.findByColumName("MaLopHoc", value);
            if(temp != null && !temp.isEmpty())
                trainClassList.addAll(temp);
            List<Subject> subjectList= subjectDAO.findByColumName("TenMH", value);
            for(int i=0; i<subjectList.size();i++){
               temp = classDAO.findByColumName("MaMH", subjectList.get(i).getId());
               trainClassList.addAll(temp);
            }
            List<Lecturer> lectturerList= lectureDAO.findByColumName("HoTen", value);
            for(int i=0; i<lectturerList.size();i++){
               temp = classDAO.findByColumName("MaGV", lectturerList.get(i).getId());
               trainClassList.addAll(temp);
            }
            for(int i =0; i< trainClassList.size();i++){
                trainClassList.get(i).setSubjectName(subjectDAO.findById(trainClassList.get(i).getSubjectCode()).getSubjectName());
                trainClassList.get(i).setLectturerName(lectureDAO.findById(trainClassList.get(i).getLecturerCode()).getFullName());
            }
            PrintWriter out = response.getWriter();
            writeFilterTrainClass(trainClassList, out);
        
       } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void filterTrainClass(HttpServletRequest request, HttpServletResponse response) throws IOException{
       String year = request.getParameter("year");
       int semester = Integer.parseInt(request.getParameter("semester")); 
       List<TrainClass> trainclasslist = trainClassService.getTrainClass(year, semester);
       PrintWriter out = response.getWriter();
       writeFilterTrainClass(trainclasslist, out);
    }
    private void writeFilterTrainClass(List<TrainClass> trainclasslist, PrintWriter out){
        out.println("<tr><th> STT</th><th> Lớp học </th><th> Môn học </th><th> Giảng viên </th><th> Thứ </th><th> Phòng </th><th> Đăng ký </th><th>Ngày thi</th><th>Hủy</th><th>Đóng</th></tr>");
        if(trainclasslist == null || trainclasslist.isEmpty())
            out.println("<tr>Không tìm thấy dữ liệu</tr>");
        else{
        for(int i=0; i < trainclasslist.size();i++){
            StringBuffer result = new StringBuffer();
            result.append("<tr><td>").append(i+1).append("</td>");
            result.append("<td><a href= '../../ManageClassController?action=detail&classID=").append(trainclasslist.get(i).getId().getClassCode()).append("&year=").append(trainclasslist.get(i).getId().getYear()).append("&semester=").append(trainclasslist.get(i).getId().getSemester()).append("'>").append(trainclasslist.get(i).getId().getClassCode()).append("</a></td>");
            result.append("<td> ").append(trainclasslist.get(i).getSubjectName()).append("</td>");
            result.append(" <td> ").append(trainclasslist.get(i).getLectturerName()).append(" </td>");
            result.append("<td> ").append(trainclasslist.get(i).getStudyDate()).append("</td>");
            result.append("<td> ").append(trainclasslist.get(i).getClassRoom()).append(" </td>");
            result.append("<td> ").append(trainclasslist.get(i).getNumOfStudentReg()).append("/").append(trainclasslist.get(i).getNumOfStudent()).append(" </td>");
            if(trainclasslist.get(i).getTestDate() == null) 
            result.append("<td>Chưa có</td>");
            else
            result.append("<td>").append(trainclasslist.get(i).getTestDate()).append("</td>");
            result.append("<td><a href='../../ManageClassController?action=cancel&classID=").append(trainclasslist.get(i).getId().getClassCode()).append("'>Hủy</a></td>");
            result.append("<td><a href='../../ManageClassController?action=close&classID=").append(trainclasslist.get(i).getId().getClassCode()).append("'>Đóng</a></td>");
            result.append("</tr>");
            out.println(result.toString());
        }
        }
    }
    private void preUpdateTrainClass(HttpServletRequest request, HttpServletResponse response) throws IOException{
       String path="";
        try{
        HttpSession session = request.getSession();
        String ClassCode = (String)request.getParameter("classId"); 
        String year =(String) request.getParameter("year");
        int semester = Integer.parseInt((String) request.getParameter("semester")); 
        TrainClassID classID = new TrainClassID(ClassCode, year, semester);
        TrainClass trainClass = trainClassService.getClassInfomation(classID);
        ArrayList<Lecturer> lecturers = (ArrayList<Lecturer>) lectureDAO.findByColumName("MaKhoa", subjectDAO.findById(classDAO.findById(classID).getSubjectCode()).getFacultyCode());
          if ((lecturers != null) && (!lecturers.isEmpty())) {
                ArrayList<LecturerWeb> lws = new ArrayList<LecturerWeb>(10);
                
                for (Lecturer l : lecturers) {
                    LecturerWeb lw = new LecturerWeb(l.getId(), l.getFullName());
                    lws.add(lw);
                }
                session.setAttribute("lecturers", lws);
            }
        session.setAttribute("trainclass", trainClass);
        path = "./jsps/PDT/UpdateTrainClass.jsp";
        }catch(Exception ex){
           path= "./jsps/Message.jsp";
       }
         response.sendRedirect(path);
    }
    /**
     * this function to update train class
     * @param request
     * @param response 
     */
    private void updateTrainClass(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String path="";
        try{
        HttpSession session = request.getSession();
        String ClassCode = (String)request.getParameter("classId"); 
        String lecturerCode = (String) request.getParameter("lecturer");
        int studyDate = Integer.parseInt((String) request.getParameter("Date"));
        int shift = Integer.parseInt((String) request.getParameter("Shift"));
        String room = (String) request.getParameter("room");
        Date testDate = DateTimeUtil.parse(request.getParameter("testDate"));
        String testRoom = (String) request.getParameter("testroom");
        String testTime = (String) request.getParameter("hh") + ":" + (String) request.getParameter("hh");
        String year =(String) request.getParameter("year");
        int semester = Integer.parseInt((String) request.getParameter("semester")); 
        
        TrainClassID classID = new TrainClassID(ClassCode, year, semester);
        TrainClass trainClass = trainClassService.getClassInfomation(classID);
        trainClass.setLecturerCode(lecturerCode);
        trainClass.setStudyDate(studyDate);
        trainClass.setShift(shift);
        trainClass.setClassRoom(room);
        trainClass.setTestDate(testDate);
        trainClass.setTestRoom(testRoom);
        trainClass.setTestHours(testTime);
        trainClass = classDAO.update(trainClass);        
        
       session.setAttribute("trainclass", trainClass);
        
        path = "./jsps/PDT/TrainClassDetail.jsp";
        }catch(Exception ex){
           path= "./jsps/Message.jsp";
       }
         response.sendRedirect(path);
    }
    /**
     * This function use to get class to use view detail
     * @param request
     * @param response 
     */
    private void viewDetailClass(HttpServletRequest request, HttpServletResponse response) throws IOException{
      String path="";
        try{
        String ClassCode = (String)request.getParameter("classID"); 
        String year =(String) request.getParameter("year");
        int semester = Integer.parseInt((String) request.getParameter("semester")); 
        TrainClassID classID = new TrainClassID(ClassCode, year, semester);
        TrainClass trainClass = trainClassService.getClassInfomation(classID);
        HttpSession session = request.getSession();
        session.setAttribute("trainclass", trainClass);
         path = "./jsps/PDT/TrainClassDetail.jsp";
        }catch(Exception ex){
           path= "./jsps/Message.jsp";
       }
         response.sendRedirect(path);
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
        
        List<TrainClass> trainClazzs = trainClassService.getTrainClass(Constants.CURRENT_YEAR, Constants.CURRENT_SEMESTER);
        List<String> yearList=getYear(trainClazzs);
        //
        // TODO: Should accept only TrainClass of current semeter.
        //
        if ((trainClazzs != null) && (!trainClazzs.isEmpty())) {
            HttpSession session = request.getSession();
            session.setAttribute("train-clazzs", trainClazzs);
            session.setAttribute("yearList", yearList);
        }
        
        String path = "./jsps/PDT/TrainClassManager.jsp";
        response.sendRedirect(path);
        
        return;
    }
    private List<String> getYear(List<TrainClass> trainClassList){
        ArrayList<String> yearList=new ArrayList<String>();
        for(int i=0; i<trainClassList.size();i++){
         if(checkStringExist(trainClassList.get(i).getId().getYear(), yearList)==false)
             yearList.add(trainClassList.get(i).getId().getYear());
        }

       return yearList;         
    }
    private boolean checkStringExist(String value, List<String> list){
        boolean result=false;
        for(int i=0;i<list.size();i++){
           if(value.equalsIgnoreCase(list.get(i)))
               result=true;
        }
        return result;
    }
    private void retrieveTrainClassForAjaxQuery(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        /*int currentPage = 1;
        try {
            currentPage = Integer.parseInt((String)request.getAttribute("current-page"));
        } catch (Exception ex) {
            currentPage = 1;
        }*/
        
        List<TrainClass> trainClazzs = trainClassService.getTrainClass(Constants.CURRENT_YEAR, Constants.CURRENT_SEMESTER);
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
        int semester = Constants.CURRENT_SEMESTER;
        String year = Constants.CURRENT_YEAR;
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
        DETAIL("detail"),   // view detail class
        PREUPDATE("pre_update"), // prepare update class
        FILTER("filter"),
        SEARCH("search"),
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
