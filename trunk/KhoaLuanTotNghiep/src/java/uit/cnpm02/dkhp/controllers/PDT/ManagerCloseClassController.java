/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.controllers.PDT;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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
import uit.cnpm02.dkhp.model.Faculty;
import uit.cnpm02.dkhp.model.Lecturer;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.model.Subject;
import uit.cnpm02.dkhp.model.TrainClassID;
import uit.cnpm02.dkhp.service.ITrainClassService;
import uit.cnpm02.dkhp.service.TrainClassStatus;
import uit.cnpm02.dkhp.service.impl.TrainClassServiceImpl;
import uit.cnpm02.dkhp.utilities.Constants;

/**
 *
 * @author thanh
 */
@WebServlet(name = "ManagerCloseClassController", urlPatterns = {"/ManagerCloseClassController"})
public class ManagerCloseClassController extends HttpServlet {

    /** 
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
     private TrainClassDAO classDAO = DAOFactory.getTrainClassDAO();
    private SubjectDAO subjectDAO = DAOFactory.getSubjectDao();
    private LecturerDAO lectureDAO = DAOFactory.getLecturerDao();
    
    private ITrainClassService trainClassService = new TrainClassServiceImpl();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
    
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
            }else if(requestAction.equalsIgnoreCase(ClassFunctionSupported.SEARCH.getValue())){
             searchTrainClass(request, response);   
            }else if(requestAction.equalsIgnoreCase(ClassFunctionSupported.FILTER_BY_FACULTY.getValue())){
               FilterByFaculty(request, response);
            }else if(requestAction.equalsIgnoreCase(ClassFunctionSupported.FILTER_BY_YAS.getValue())){
                filterTrainClass(request, response);
            }else if(requestAction.equalsIgnoreCase(ClassFunctionSupported.FILTER_PAGE_BY_FACULTY.getValue())){
               FilterPageByFaculty(request, response);
            }else if(requestAction.equalsIgnoreCase(ClassFunctionSupported.FILTER_PAGE_BY_INPUT.getValue())){
               FilterPageByInputValue(request, response);
            }else if(requestAction.equalsIgnoreCase(ClassFunctionSupported.FILTER_PAGE_BY_YAS.getValue())){
               FilterPageByYAS(request, response);
            }else if(requestAction.equalsIgnoreCase(ClassFunctionSupported.OPEN_BY_INPUT.getValue())){
              openClassWithInputSearch(request, response);
            }else if(requestAction.equalsIgnoreCase(ClassFunctionSupported.OPEN_BY_FACULTY.getValue())){
              openClassWithFilterByFaculty(request, response);
            }else if(requestAction.equalsIgnoreCase(ClassFunctionSupported.OPEN_BY_YAS.getValue())){
               openClassWithFilterByYAS(request, response);
            }
        } finally {
            out.close();
        }
    }
     private void openClassWithFilterByYAS(HttpServletRequest request, HttpServletResponse response){
       try{
        int numPage=0;
        PrintWriter out = response.getWriter();
         //get info close class
           String classCode =  request.getParameter("classCode");
           String classYear =  request.getParameter("classYear");
           int classSemester = Integer.parseInt(request.getParameter("classSemester"));
           //Close train class
           TrainClassID id =new TrainClassID(classCode, classYear, classSemester);
           TrainClass updateClass= classDAO.findById(id);
           updateClass.setStatus(TrainClassStatus.OPEN);
           classDAO.update(updateClass);
           //get list class again
        int currentPage=Integer.parseInt(request.getParameter("curentPage")); 
        String year = request.getParameter("year");
       int semester = Integer.parseInt(request.getParameter("semester")); 
       List<TrainClass> trainclasslist = trainClassService.getAllClassCloseByYearAndSemester(year, semester);
       //set current page
         if(trainclasslist.size() %Constants.ELEMENT_PER_PAGE_DEFAULT==0){
               numPage= trainclasslist.size() /Constants.ELEMENT_PER_PAGE_DEFAULT;
         }else{ 
               numPage=trainclasslist.size() /Constants.ELEMENT_PER_PAGE_DEFAULT+1;
         }
        if(currentPage > numPage)
            currentPage = numPage;
        
       List<TrainClass> trainClazzs = getClassListByPage(trainclasslist, currentPage);
       writeFilterTrainClass(trainClazzs, out, currentPage);
       } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    /**
     * 
     * @param request
     * @param response 
     */
    private void openClassWithFilterByFaculty(HttpServletRequest request, HttpServletResponse response){
        try {
        int numpage =0;
        PrintWriter out = response.getWriter();
        //get info close class
           String classCode =  request.getParameter("classCode");
           String classYear =  request.getParameter("classYear");
           int classSemester = Integer.parseInt(request.getParameter("classSemester"));
           //Close train class
           TrainClassID id =new TrainClassID(classCode, classYear, classSemester);
           TrainClass updateClass= classDAO.findById(id);
           updateClass.setStatus(TrainClassStatus.OPEN);
           classDAO.update(updateClass);
           //get list class again
        List<TrainClass> trainClassList = new ArrayList<TrainClass>(10);
        int currentPage=Integer.parseInt(request.getParameter("curentPage"));
        String facultyCode = request.getParameter("value");
        if(facultyCode.equalsIgnoreCase("All")){
          trainClassList = getAllClassClose();  
        }else{
          trainClassList = classDAO.findCloseClassByFaculty(facultyCode);
        }
        // set current page
       if(trainClassList.size() %Constants.ELEMENT_PER_PAGE_DEFAULT==0)
        numpage= trainClassList.size() /Constants.ELEMENT_PER_PAGE_DEFAULT;
       else 
        numpage=trainClassList.size() /Constants.ELEMENT_PER_PAGE_DEFAULT+1;
       if(currentPage > numpage)
          currentPage = numpage;
       
        List<TrainClass> result = getClassListByPage(trainClassList, currentPage);
            writeFilterTrainClass(result, out, currentPage);
         } catch (Exception ex) {
                Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    /**
     * 
     * @param request
     * @param response 
     */
    private void openClassWithInputSearch(HttpServletRequest request, HttpServletResponse response){
         try {
           int numPage=0;
           PrintWriter out = response.getWriter();
           //get info close class
           String classCode =  request.getParameter("classCode");
           String classYear =  request.getParameter("classYear");
           int classSemester = Integer.parseInt(request.getParameter("classSemester"));
           //Close train class
           TrainClassID id =new TrainClassID(classCode, classYear, classSemester);
           TrainClass updateClass= classDAO.findById(id);
           updateClass.setStatus(TrainClassStatus.OPEN);
           classDAO.update(updateClass);
           //get list class again
           int currentPage=Integer.parseInt(request.getParameter("curentPage"));
           String value = request.getParameter("value");
            List<TrainClass> trainClassList = new ArrayList<TrainClass>(10);
            if(value== null || value.isEmpty()){
                 trainClassList = getAllClassClose();
            }else{
            List<TrainClass> temp = trainClassService.SearchCloseClassByColumName("MaLopHoc", value);
            if(temp != null && !temp.isEmpty())
                trainClassList.addAll(temp);
            List<Subject> subjectList= subjectDAO.findByColumName("TenMH", value);
            for(int i=0; i<subjectList.size();i++){
               temp = trainClassService.SearchCloseClassByColumName("MaMH", subjectList.get(i).getId());
               if(temp != null && !temp.isEmpty()){
               for(int j =0; j< temp.size(); j++){
                   TrainClass trainClass = temp.get(j);
                   if(!checkTrainClassInList(trainClassList, trainClass))
                       trainClassList.add(trainClass);
               }
            }
            }
            List<Lecturer> lectturerList= lectureDAO.findByColumName("HoTen", value);
            for(int i=0; i<lectturerList.size();i++){
               temp = trainClassService.SearchCloseClassByColumName("MaGV", lectturerList.get(i).getId());
                if(temp != null && !temp.isEmpty()){
               for(int j =0; j< temp.size(); j++){
                   TrainClass trainClass = temp.get(j);
                   if(!checkTrainClassInList(trainClassList, trainClass))
                       trainClassList.add(trainClass);
               }
            }
            }
            }
            for(int i =0; i< trainClassList.size();i++){
                trainClassList.get(i).setSubjectName(subjectDAO.findById(trainClassList.get(i).getSubjectCode()).getSubjectName());
                trainClassList.get(i).setLectturerName(lectureDAO.findById(trainClassList.get(i).getLecturerCode()).getFullName());
            }
            //set current page
         if(trainClassList.size() %Constants.ELEMENT_PER_PAGE_DEFAULT==0){
               numPage= trainClassList.size() /Constants.ELEMENT_PER_PAGE_DEFAULT;
         }else{ 
               numPage=trainClassList.size() /Constants.ELEMENT_PER_PAGE_DEFAULT+1;
         }
        if(currentPage > numPage)
            currentPage = numPage;
        
            List<TrainClass> result = getClassListByPage(trainClassList, currentPage);
            writeFilterTrainClass(result, out, currentPage);
        
       } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    /**
     * 
     * @param request
     * @param response 
     */
     private void FilterPageByYAS(HttpServletRequest request, HttpServletResponse response){
       try{
        int numPage=0;
        PrintWriter out = response.getWriter();
        int currentPage=Integer.parseInt(request.getParameter("curentPage")); 
        String year = request.getParameter("year");
       int semester = Integer.parseInt(request.getParameter("semester")); 
       List<TrainClass> trainclasslist = trainClassService.getAllClassCloseByYearAndSemester(year, semester);
       //set current page
         if(trainclasslist.size() %Constants.ELEMENT_PER_PAGE_DEFAULT==0){
               numPage= trainclasslist.size() /Constants.ELEMENT_PER_PAGE_DEFAULT;
         }else{ 
               numPage=trainclasslist.size() /Constants.ELEMENT_PER_PAGE_DEFAULT+1;
         }
        if(currentPage > numPage)
            currentPage = numPage;
        
       List<TrainClass> trainClazzs = getClassListByPage(trainclasslist, currentPage);
       writeFilterTrainClass(trainClazzs, out, currentPage);
       } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    /**
     * 
     * @param request
     * @param response 
     */
     private void FilterPageByInputValue(HttpServletRequest request, HttpServletResponse response){
       try {
           int numPage=0;
           PrintWriter out = response.getWriter();
           int currentPage=Integer.parseInt(request.getParameter("curentPage"));
           String value = request.getParameter("value");
            List<TrainClass> trainClassList = new ArrayList<TrainClass>(10);
            if(value== null || value.isEmpty()){
                 trainClassList = getAllClassClose();
            }else{
            List<TrainClass> temp = trainClassService.SearchCloseClassByColumName("MaLopHoc", value);
            if(temp != null && !temp.isEmpty())
                trainClassList.addAll(temp);
            List<Subject> subjectList= subjectDAO.findByColumName("TenMH", value);
            for(int i=0; i<subjectList.size();i++){
               temp = trainClassService.SearchCloseClassByColumName("MaMH", subjectList.get(i).getId());
               if(temp != null && !temp.isEmpty()){
               for(int j =0; j< temp.size(); j++){
                   TrainClass trainClass = temp.get(j);
                   if(!checkTrainClassInList(trainClassList, trainClass))
                       trainClassList.add(trainClass);
               }
            }
            }
            List<Lecturer> lectturerList= lectureDAO.findByColumName("HoTen", value);
            for(int i=0; i<lectturerList.size();i++){
               temp = trainClassService.SearchCloseClassByColumName("MaGV", lectturerList.get(i).getId());
                if(temp != null && !temp.isEmpty()){
               for(int j =0; j< temp.size(); j++){
                   TrainClass trainClass = temp.get(j);
                   if(!checkTrainClassInList(trainClassList, trainClass))
                       trainClassList.add(trainClass);
               }
            }
            }
            }
            for(int i =0; i< trainClassList.size();i++){
                trainClassList.get(i).setSubjectName(subjectDAO.findById(trainClassList.get(i).getSubjectCode()).getSubjectName());
                trainClassList.get(i).setLectturerName(lectureDAO.findById(trainClassList.get(i).getLecturerCode()).getFullName());
            }
            //set current page
         if(trainClassList.size() %Constants.ELEMENT_PER_PAGE_DEFAULT==0){
               numPage= trainClassList.size() /Constants.ELEMENT_PER_PAGE_DEFAULT;
         }else{ 
               numPage=trainClassList.size() /Constants.ELEMENT_PER_PAGE_DEFAULT+1;
         }
        if(currentPage > numPage)
            currentPage = numPage;
        
            List<TrainClass> result = getClassListByPage(trainClassList, currentPage);
            writeFilterTrainClass(result, out, currentPage);
        
       } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    /**
     * 
     * @param request
     * @param response 
     */
    private void FilterPageByFaculty(HttpServletRequest request, HttpServletResponse response){
       try {
        int numpage =0;
        PrintWriter out = response.getWriter();
        List<TrainClass> trainClassList = null;
        int currentPage=Integer.parseInt(request.getParameter("curentPage"));
        String facultyCode = request.getParameter("value");
        if(facultyCode.equalsIgnoreCase("All")){
          trainClassList = getAllClassClose();  
        }else{
          trainClassList = classDAO.findCloseClassByFaculty(facultyCode);
        }
        // set current page
       if(trainClassList.size() %Constants.ELEMENT_PER_PAGE_DEFAULT==0)
        numpage= trainClassList.size() /Constants.ELEMENT_PER_PAGE_DEFAULT;
       else 
        numpage=trainClassList.size() /Constants.ELEMENT_PER_PAGE_DEFAULT+1;
       if(currentPage > numpage)
          currentPage = numpage;
       
        List<TrainClass> result = getClassListByPage(trainClassList, currentPage);
            writeFilterTrainClass(result, out, currentPage);
         } catch (Exception ex) {
                Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    /**
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
     private void filterTrainClass(HttpServletRequest request, HttpServletResponse response) throws IOException{
       String year = request.getParameter("year");
       int semester = Integer.parseInt(request.getParameter("semester")); 
       List<TrainClass> trainclasslist = trainClassService.getAllClassCloseByYearAndSemester(year, semester);
       List<TrainClass> trainClazzs = getClassListByPage(trainclasslist, 1);
       PrintWriter out = response.getWriter();
       writeFilterTrainClass(trainClazzs, out, 1);
    }
    /**
     * 
     * @param request
     * @param response 
     */
     private void FilterByFaculty(HttpServletRequest request, HttpServletResponse response){
        PrintWriter out = null;
        List<TrainClass> trainClassList = null;
        try {
            String facultyCode = request.getParameter("value");
            if(facultyCode.equalsIgnoreCase("All")){
              trainClassList = getAllClassClose();  
            }else{
            trainClassList = classDAO.findCloseClassByFaculty(facultyCode);
            }
            out = response.getWriter();
            List<TrainClass> result = getClassListByPage(trainClassList, 1);
            writeFilterTrainClass(result, out,1);
        } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
       } finally {
            out.close();
        }
    }
    /**
     * 
     * @param request
     * @param response 
     */
    private void searchTrainClass(HttpServletRequest request, HttpServletResponse response){
        try {
        String value = request.getParameter("value");
            List<TrainClass> trainClassList = new ArrayList<TrainClass>(10);
            if(value== null || value.isEmpty()){
                 trainClassList = getAllClassClose();
            }else{
            List<TrainClass> temp = trainClassService.SearchCloseClassByColumName("MaLopHoc", value);
            if(temp != null && !temp.isEmpty())
                trainClassList.addAll(temp);
            List<Subject> subjectList= subjectDAO.findByColumName("TenMH", value);
            for(int i=0; i<subjectList.size();i++){
               temp = trainClassService.SearchCloseClassByColumName("MaMH", subjectList.get(i).getId());
                if(temp != null && !temp.isEmpty()){ 
               for(int j =0; j< temp.size(); j++){
                   TrainClass trainClass = temp.get(j);
                   if(!checkTrainClassInList(trainClassList, trainClass))
                       trainClassList.add(trainClass);
               }
            }
            }
            List<Lecturer> lectturerList= lectureDAO.findByColumName("HoTen", value);
            for(int i=0; i<lectturerList.size();i++){
               temp = trainClassService.SearchCloseClassByColumName("MaGV", lectturerList.get(i).getId());
               if(temp != null && !temp.isEmpty()){
                for(int j =0; j< temp.size(); j++){
                   TrainClass trainClass = temp.get(j);
                   if(!checkTrainClassInList(trainClassList, trainClass))
                       trainClassList.add(trainClass);
               }
            }
            }
            }
            for(int i =0; i< trainClassList.size();i++){
                trainClassList.get(i).setSubjectName(subjectDAO.findById(trainClassList.get(i).getSubjectCode()).getSubjectName());
                trainClassList.get(i).setLectturerName(lectureDAO.findById(trainClassList.get(i).getLecturerCode()).getFullName());
            }
            PrintWriter out = response.getWriter();
            List<TrainClass> result = getClassListByPage(trainClassList, 1);
            writeFilterTrainClass(result, out,1);
        
       } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * 
     * @param request
     * @param response
     * @throws IOException 
     */
private void defaultAction(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int numPage=0;
            List<TrainClass> classList = getAllClassClose();
            List<TrainClass> trainClazzs = getClassListByPage(classList, 1);
            List<String> yearList=getYear(trainClazzs);
            List<Faculty> facultyList= DAOFactory.getFacultyDao().findAll();
            if(classList.size() %Constants.ELEMENT_PER_PAGE_DEFAULT==0){
               numPage= classList.size() /Constants.ELEMENT_PER_PAGE_DEFAULT;
              }else{ 
               numPage=classList.size() /Constants.ELEMENT_PER_PAGE_DEFAULT+1;
             }
                HttpSession session = request.getSession();
                session.setAttribute("train-clazzs", trainClazzs);
                session.setAttribute("yearList", yearList);
                session.setAttribute("numpage", classList.size());
                session.setAttribute("facultyList", facultyList);
                session.setAttribute("numpage", numPage);
            String path = "./jsps/PDT/CloseTrainClassManager.jsp";
            response.sendRedirect(path);
            
            return;
        } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
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
     * 
     * @return 
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
    public enum ClassFunctionSupported {
        DEFAULT("default"), // List first page of class opened.
        PRECREATE("pre_create"),
        CHECK_CREATE("check_create"),
        CREATE("create"),   // Create new class form support
        DELETE("delete"),   // Remove class
        DETAIL("detail"),   // view detail class
        PREUPDATE("pre_update"), // prepare update class
        FILTER_BY_YAS("filterByYAS"),
        FILTER_BY_FACULTY("filterByFaculty"),
        FILTER_PAGE_BY_INPUT("FilterPageByInput"),
        FILTER_PAGE_BY_FACULTY("FilterPageByFaculty"),
        FILTER_PAGE_BY_YAS("FilterPageByYAS"),
        OPEN_BY_INPUT("OpenByInput"),
        OPEN_BY_FACULTY("OpenByFaculty"),
        OPEN_BY_YAS("OpenPageByYAS"),
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
    private void setSubjectAndLecturer(List<TrainClass> trainClass) throws Exception{
     SubjectDAO subjectDao=new SubjectDAO();
     LecturerDAO lecturerDao=new LecturerDAO();
     for(int i=0;i<trainClass.size();i++){
        trainClass.get(i).setSubjectName(subjectDao.findById(trainClass.get(i).getSubjectCode()).getSubjectName());
        trainClass.get(i).setLectturerName(lecturerDao.findById(trainClass.get(i).getLecturerCode()).getFullName());
        trainClass.get(i).setNumTC(subjectDao.findById(trainClass.get(i).getSubjectCode()).getnumTC() );
    }
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
     /**
     * Get all class is closing
     * @return 
     */
    private List<TrainClass> getAllClassClose(){
         List<TrainClass> trainClazzs = trainClassService.getAllClassClose();
        try {
            setSubjectAndLecturer(trainClazzs);
        } catch (Exception ex) {
            Logger.getLogger(ManageClassController.class.getName()).log(Level.SEVERE, null, ex);
        }
         return trainClazzs;
    }
   private List<TrainClass> getClassListByPage(List<TrainClass> classList, int currentPage){
       List<TrainClass> result = new ArrayList<TrainClass>(Constants.ELEMENT_PER_PAGE_DEFAULT);
       if(classList.size()<= Constants.ELEMENT_PER_PAGE_DEFAULT)
         result = classList;
       else{
            int beginIndex=(currentPage-1)*Constants.ELEMENT_PER_PAGE_DEFAULT;
            int endIndex = (currentPage-1)*Constants.ELEMENT_PER_PAGE_DEFAULT + Constants.ELEMENT_PER_PAGE_DEFAULT;
            for(int i=0; i < classList.size(); i++){
                if(i>= beginIndex && i < endIndex )
                result.add(classList.get(i));
             }
         }
       return result;
    }  
    private void writeFilterTrainClass(List<TrainClass> trainclasslist, PrintWriter out, int currentPgae){
        out.println("<tr>"
                    + "<th> STT</th>"
                    + "<th> Lớp học </th>"
                    + "<th> Môn học </th>"
                    + "<th> Giảng viên </th>"
                    + "<th> Thứ </th>"
                    + "<th> Phòng </th>"
                    + "<th> Đăng ký </th>"
                    + "<th> Học kỳ </th>"
                    + "<th> Năm học </th>"
                    + "<th> Mở </th>"
                + "</tr>");
        if((trainclasslist == null) || trainclasslist.isEmpty()) {
            out.println("<tr>Không tìm thấy dữ liệu</tr>");
        } else {
            for (int i = 0; i < trainclasslist.size(); i++) {
                StringBuffer result = new StringBuffer();
                result.append("<tr><td>").append(((currentPgae-1)*Constants.ELEMENT_PER_PAGE_DEFAULT + 1 + i)).append("</td>");
                result.append("<td>").append(trainclasslist.get(i).getId().getClassCode()).append("</a></td>");
                result.append("<td> ").append(trainclasslist.get(i).getSubjectName()).append("</td>");
                result.append(" <td> ").append(trainclasslist.get(i).getLectturerName()).append(" </td>");
                result.append("<td> ").append(trainclasslist.get(i).getStudyDate()).append("</td>");
                result.append("<td> ").append(trainclasslist.get(i).getClassRoom()).append(" </td>");
                result.append("<td> ").append(trainclasslist.get(i).getNumOfStudentReg()).append("/").append(trainclasslist.get(i).getNumOfStudent()).append(" </td>");
                result.append("<td> ").append(trainclasslist.get(i).getId().getSemester()).append(" </td>");
                result.append("<td> ").append(trainclasslist.get(i).getId().getYear()).append(" </td>");
                String method = String.format(" onclick=openClass('%s','%s','%s')",trainclasslist.get(i).getId().getClassCode(), trainclasslist.get(i).getId().getSemester(), trainclasslist.get(i).getId().getYear());
                result.append("<td><span class='atag'" +method+ ">Mở</span></td>");
                
                result.append("</tr>");
                out.println(result.toString());
            }
        }
    }
    private boolean checkTrainClassInList(List<TrainClass> trainclasslist, TrainClass trainClass){
        boolean result = false;
        for(int i = 0; i < trainclasslist.size(); i++){
            if(trainclasslist.get(i).getId().getClassCode().equalsIgnoreCase(trainClass.getId().getClassCode())
               &&trainclasslist.get(i).getId().getYear().equalsIgnoreCase(trainClass.getId().getYear())
               &&trainclasslist.get(i).getId().getSemester()== trainClass.getId().getSemester()){
              result = true;  
              break;
            }
       }
        return result;
    }
}
     
