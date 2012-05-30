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
import uit.cnpm02.dkhp.DAO.SubjectDAO;
import uit.cnpm02.dkhp.model.Faculty;
import uit.cnpm02.dkhp.model.PreSubject;
import uit.cnpm02.dkhp.model.Subject;
import uit.cnpm02.dkhp.service.IFacultyService;
import uit.cnpm02.dkhp.service.ISubjectService;
import uit.cnpm02.dkhp.service.impl.FacultyServiceImpl;
import uit.cnpm02.dkhp.service.impl.SubjectServiceImpl;
import uit.cnpm02.dkhp.utilities.Constants;
import uit.cnpm02.dkhp.utilities.ExecuteResult;
import uit.cnpm02.dkhp.utilities.Message;
import uit.cnpm02.dkhp.utilities.StringUtils;

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

    private ISubjectService subjectService = new SubjectServiceImpl();
    //private IPreSubject preSubService = new PreSubjectImpl();
    private IFacultyService facultyService = new FacultyServiceImpl();
  
    public ManageSubjectController() {
        super();
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

    /** 
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        session.removeAttribute("error");
        String path = "./jsps/PDT/SubjectManager.jsp";
        boolean ajaxCalled = false;
        try {
            ajaxCalled = Boolean.parseBoolean(request.getParameter("ajax"));
        } catch (Exception ex) {
            //
        }
        try {
            String action = request.getParameter("function");
            if ((action == null) || action.isEmpty()) {
                return;
            }

           if (action.equals(SubjectManageFunction.LIST_SUBJECT.value())) {
                listSubject(request, response);
           } else if (action.equals(SubjectManageFunction.SEARCH.value())) {
                String key = request.getParameter("key");
                List<Subject> subject = searchSubject(session.getId(),key);
                if ((subject != null) && !subject.isEmpty()) {
                    writeOutListSubject(out, subject,1);
                }
                return;
            } else if (action.equals(SubjectManageFunction.SORT.value())) {
                String by = request.getParameter("by");
                String type = request.getParameter("type");
                List<Subject> subjects = sort(session.getId(), by, type);
                writeOutListSubject(out, subjects,1);
                return;
            } else if (action.equals(SubjectManageFunction.PRE_ADD_SUBJECT.value())) {
                preDataForAddSubject(session);
                path = "./jsps/PDT/AddSubject.jsp";
                response.sendRedirect(path);
                return;
            } else if (action.equals(SubjectManageFunction.ADD_SUBJECT.value())) {
                ExecuteResult er = addSubject(request);
                writeResponeAddSubject(er, out);
                return;
            } else if (action.equals(SubjectManageFunction.DELETE.value())) {
                String subId = request.getParameter("subject_code");
                int currentPage=Integer.parseInt(request.getParameter("curentPage"));
                String key = request.getParameter("key");
                deleteSubjectBy(out, session.getId(), key, subId, currentPage);
                return;
            } else if (action.equals(SubjectManageFunction.EDIT_SUBJECT.value())) {
                editSubject(request, response);
                path = "./jsps/PDT/EditSubject.jsp";
                response.sendRedirect(path);
                return;
            }
           else if (action.equals(SubjectManageFunction.FILTER.value())) {
               int currentPage=Integer.parseInt(request.getParameter("curentPage"));
               String key = request.getParameter("key");
               filterSubjectList(out, session.getId(),currentPage, key);
               return;
            }


            if (!ajaxCalled) {
                response.sendRedirect(path);
            }

        } catch (Exception ex) {
            Logger.getLogger(ManageSubjectController.class.getName())
                    .log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }

    }
private void deleteSubjectBy(PrintWriter out, String sessionId, String key, String subId, int currentPage){
    List<Subject> result = new ArrayList<Subject>(Constants.ELEMENT_PER_PAGE_DEFAULT);  
    int numpage=0;
    try {
         if(subjectService.deleteSubjectByID(subId)){
               if(key.isEmpty())
                 result = subjectService.findAll(Constants.ELEMENT_PER_PAGE_DEFAULT, currentPage, "MaMH", "ASC");
               else{
                   List<Subject> listSubject = subjectService.search(sessionId, key);
                   if(listSubject.size() %Constants.ELEMENT_PER_PAGE_DEFAULT==0)
                   numpage= listSubject.size() /Constants.ELEMENT_PER_PAGE_DEFAULT;
                   else 
                   numpage=listSubject.size() /Constants.ELEMENT_PER_PAGE_DEFAULT+1;
                   if(currentPage>numpage)
                    currentPage=numpage;
                   if(listSubject.size()<= Constants.ELEMENT_PER_PAGE_DEFAULT)
                   result = listSubject;
                   else{
                   int beginIndex=(currentPage-1)*Constants.ELEMENT_PER_PAGE_DEFAULT;
                   int endIndex = (currentPage-1)*Constants.ELEMENT_PER_PAGE_DEFAULT + Constants.ELEMENT_PER_PAGE_DEFAULT;
                    for(int i=0; i < listSubject.size(); i++){
                    if(i>= beginIndex && i < endIndex )
                   result.add(listSubject.get(i));
                         }
                       }
                    }
             }
        } catch (Exception ex) {
            result = null;
        }
        if ((result != null) && !result.isEmpty()) {
                    writeOutListSubject(out, result, currentPage);
        }else {
            out.println("Không thể xóa môn học vì môn học đang được sử dụng");
        }
}   
private void filterSubjectList(PrintWriter out ,String sessionId, int currentPage, String key){
    int numpage=0;
    List<Subject> result = new ArrayList<Subject>(Constants.ELEMENT_PER_PAGE_DEFAULT);
    if(key.isEmpty())
     result= subjectService.findAll(Constants.ELEMENT_PER_PAGE_DEFAULT, currentPage, "MaMH", "ASC");
    else{
        List<Subject> listSubject = subjectService.search(sessionId, key);
        if(listSubject.size() %Constants.ELEMENT_PER_PAGE_DEFAULT==0)
            numpage= listSubject.size() /Constants.ELEMENT_PER_PAGE_DEFAULT;
            else 
            numpage=listSubject.size() /Constants.ELEMENT_PER_PAGE_DEFAULT+1;
            if(currentPage>numpage)
                currentPage=numpage;
        if(listSubject.size()<= Constants.ELEMENT_PER_PAGE_DEFAULT)
        result = listSubject;
        else{
            int beginIndex=(currentPage-1)*Constants.ELEMENT_PER_PAGE_DEFAULT;
            int endIndex = (currentPage-1)*Constants.ELEMENT_PER_PAGE_DEFAULT + Constants.ELEMENT_PER_PAGE_DEFAULT;
            for(int i=0; i < listSubject.size(); i++){
                if(i>= beginIndex && i < endIndex )
               result.add(listSubject.get(i));
            }
          }
    } 
    if ((result != null) && !result.isEmpty()) {
                    writeOutListSubject(out, result, currentPage);
                }
     
}
   /**
     * Get list student, send it to client.
     * @param req
     * @param resp
     * @throws Exception 
     */
    private void listSubject(HttpServletRequest req
            , HttpServletResponse resp) throws Exception {
       String path="";
        int numpage = subjectService.getNumberPage();
        try {
        List<Subject> subjects= subjectService.findAll(Constants.ELEMENT_PER_PAGE_DEFAULT, 1, "MaMH", "ASC");
        HttpSession session = req.getSession();
            session.setAttribute("list_subject", subjects);
            path = "./jsps/PDT/SubjectManager.jsp";
            session.setAttribute("numpage", numpage);
       } catch (Exception ex) {
            path= "./jsps/Message.jsp";
        }
        resp.sendRedirect(path);
      
    }
    
    private List<Subject> searchSubject(String sessionId, String key) {
        List<Subject> listSubject = subjectService.search(sessionId, key);
        List<Subject> result = new ArrayList<Subject>(Constants.ELEMENT_PER_PAGE_DEFAULT);
        if(listSubject.size()> Constants.ELEMENT_PER_PAGE_DEFAULT){
            for(int i=0; i < Constants.ELEMENT_PER_PAGE_DEFAULT; i++){
               result.add(listSubject.get(i));
            }
            return result;
        }else
        return listSubject;
        
    }
    
    private List<Subject> sort(String sessionId, String by, String type) {
        return subjectService.sort(sessionId, by, type);
    }
    
    private void preDataForAddSubject(HttpSession session) {
        // List al faculties
        List<Faculty> faculties = facultyService.getAllFaculty();
        if ((faculties != null) && !faculties.isEmpty()) {
            session.setAttribute("faculty", faculties);
        }
        
        List<Subject> subjects = facultyService.getSubject("");
        if ((subjects != null) && !subjects.isEmpty()) {
            session.setAttribute("subjects", subjects);
        }
    }
        
    private void writeOutListSubject(PrintWriter out, List<Subject> subjects, int currentPage) {
       try {
            // Search(by, type)
             String tblHeader = "<tr>"
                    + "<th> STT </th>"
                    + "<th>  Mã MH </th>"
                    + "<th> Tên Môn học</th>"
                    + "<th>  Khoa </th>"
                    + "<th> Số TC </th>"
                    + "<th> Số TCLT </th>"
                    + "<th>  Số TCTH </th>"
                    + "<th>  Loai </th>"
                    + "<th> Sửa </th>"
                    + "<th> Xóa </th>"
                    + "</tr>";
            out.println(tblHeader);
            for (int i = 0; i < subjects.size(); i++) {
                String type="Bắt buộc";
                if(subjects.get(i).getType()==1)
                    type="Tự chọn";
                String method = String.format(" onclick=deleteSub('%s')",subjects.get(i).getId());
                String currentLine = "<tr>"
                        + "<td>" +  ((currentPage-1)*Constants.ELEMENT_PER_PAGE_DEFAULT + 1 + i) + "</td>"
                        + "<td>" + subjects.get(i).getId() + "</td> "
                        + "<td>" + subjects.get(i).getSubjectName() + "</td>"
                        + "<td>" + subjects.get(i).getFacultyCode() + "</td>"
                        + "<td>" + subjects.get(i).getnumTC() + "</td>"
                        + "<td>" + subjects.get(i).getnumTCLT() + "</td>"
                        + "<td>" + subjects.get(i).getnumTCTH() + "</td>"
                        + "<td>" + type + "</td>"
                        + "<td><a href = \"../../ManageSubjectController?function=edit_subject&subject_code=" + subjects.get(i).getId() + "\">Sửa</a></td>"
                        + "<td><span class='atag' "+method+">Xóa</span></td></tr>"
                        + "</tr>";
                out.println(currentLine);
            }
        } catch (Exception ex) {
        }
    }

    /**
     * Insert subject(s) to database
     * The data is read from request object as a String
     * The string hold data in format:
     * SUB1;SUB2;SUB3; ...
     * SUB1: MaMH, TenMH, SoTCLT, SoTCTH
     * 
     * @param request request object
     * @param response respont object
     */
    private void insertSubFromTable(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        String data = request.getParameter("data");

        if ((data == null) || (data.isEmpty())) {
            out.println("Lỗi: Server không nhận được dữ liệu.");
            return;
        }

        String[] subjectsStr = data.split(";");
        List<Subject> subjects = new ArrayList<Subject>();
        try {
            for (int i = 0; i < subjectsStr.length; i++) {
                String[] subStr = subjectsStr[i].split(",");
                int numTCLT = Integer.parseInt(subStr[2]);
                int numTCTH = Integer.parseInt(subStr[3]);
                Subject sub = new Subject();

                sub.setId(subStr[0]);
                sub.setSubjectName(subStr[1]);
                sub.setnumTCLT(numTCLT);
                sub.setnumTCTH(numTCTH);
                sub.setnumTC(numTCTH + numTCLT);

                subjects.add(sub);
            }
            // Every prepared OK
            SubjectDAO subDao = DAOFactory.getSubjectDao();
            subDao.addAll(subjects);
        } catch (Exception ex) {
            Logger.getLogger(ManageSubjectController.class.getName())
                    .log(Level.SEVERE, null, ex);
            out.println("Đã có lỗi xảy ra. " + ex.toString());
        }
    }

    /**
     * Delete single subject.
     * The infomation is attached in session
     * by its id.
     * 
     * @param sessionId
     * @param subId
     */
    private ExecuteResult deleteSingle(String sessionId, String subId) {
        ExecuteResult result = new ExecuteResult(true, "");
        if (StringUtils.isEmpty(subId)) {
            result.setIsSucces(false);
            result.setMessage("Không tìm thấy môn học.");
            return result;
        }
        try {
            boolean r= subjectService.deleteSubject(sessionId, subId);
            result.setIsSucces(r);
        } catch (Exception ex) {
            result.setIsSucces(false);
            result.setMessage("Đã có lỗi xảy ra: " + ex.toString());
            Logger.getLogger(ManageSubjectController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        List<Subject> subjects = subjectService.getCurrentSubjects(sessionId);
        result.setData(subjects);
        return result;
    }

    /**
     * Add subject from subject form.
     * 
     * @param request
     * @param response 
     */
    private ExecuteResult addSubject(HttpServletRequest request) {
        ExecuteResult result = new ExecuteResult(true, "");
        String data = (String) request.getParameter("data");

        //
        // Parsing data, get result with full information
        // include pre-required subject
        //
        Subject sub = initialSubjectFromRequest(data);
        if(sub == null) {
            result.setIsSucces(false);
            result.setMessage(Message.ADD_SUBJECT_ERROR);
        } else {
            try {
                sub = subjectService.addSubject(sub);
                result.setData(sub);
            } catch (Exception ex) {
                result.setIsSucces(false);
                result.setMessage(Message.ADD_SUBJECT_ERROR + " -- " 
                        + ex.toString());
                Logger.getLogger(ManageSubjectController.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
            
        return result;
    }

    /*+ subjectName + ";"
     * + tclt + ";"
     * + tcth + ";"
     * + faculty + ";"
     * + type + ";"
     * + preSubject;
     */
    private Subject initialSubjectFromRequest(String data) {
        Subject sub = null;
         try {
            String[] infos = data.split(";");
            if ((infos == null) && (infos.length < 1)) {
                return null;
            }

            String subjectId = infos[0];
            String subjectName = infos[1];
            int tclt = Integer.parseInt(infos[2]);
            int tcth = Integer.parseInt(infos[3]);
            String faculty = infos[4];
            int type = Integer.parseInt(infos[5]);
            sub = new Subject(subjectId, subjectName, tclt + tcth,
                    tclt, faculty, type);
            
            // Check if existed some required subject
            // Then initial required subject.
            //
            if ((infos.length >= 7) && !infos[6].equals("X")) {
                String preSubId[] = infos[6].split("-");
                List<Subject> preSubs = new ArrayList<Subject>(3);
                for (int i = 0; i < preSubId.length; i++) {
                    Subject preSub = new Subject();
                    preSub.setId(preSubId[i]);
                    //preSub.sets
                    preSubs.add(preSub);
                }
                sub.setPreSub(preSubs);
            }
        } catch (Exception ex) {
            Logger.getLogger(ManageSubjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sub;
    }
    
    private void editSubject(HttpServletRequest request
                                , HttpServletResponse response) {
        HttpSession session = request.getSession();
        String data = (String) request.getParameter("data");
        String subId = (String) request.getParameter("subject_code");

        if (data == null) {
            prepareEditedData(session, subId);
        } else {
            updateSubject(request, response, data);
        }
    }

    private void prepareEditedData(HttpSession session, String subId) {
           try {
            SubjectDAO subDao = DAOFactory.getSubjectDao();
            List<Subject> subs = DAOFactory.getSubjectDao().findAll();
            if ((subs != null) && (!subs.isEmpty())) {
                List<String> subjectsStr = new ArrayList<String>();
                for (int i = 0; i < subs.size(); i++) {
                    subjectsStr.add(subs.get(i).getId() + "-" + subs.get(i).getSubjectName());
                }
                session.setAttribute("subjects", subjectsStr);
            }

            Subject sub = subDao.findById(subId);

            if (sub != null) {
                session.setAttribute("subject", sub);
                List<PreSubject> preSubs = DAOFactory.getPreSubDao().findByColumName("MaMH", subId);
                if (!preSubs.isEmpty()) {
                    List<String> preSubStr = new ArrayList<String>(10);
                    for (int i = 0; i < preSubs.size(); i++) {
                        preSubStr.add(preSubs.get(i).getId().getPreSudId());
                    }

                    session.setAttribute("pre_subjects", preSubStr);
                } else {
                    session.removeAttribute("pre_subjects");
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ManageSubjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateSubject(HttpServletRequest resquest,
                                HttpServletResponse respone, String data) {
        PrintWriter out = null;
        try {
            out = respone.getWriter();
        } catch (IOException ex) {
            Logger.getLogger(ManageSubjectController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        try {
            // Data format: ID - Name - TCLT - TCTH - TQ1 - TQ2 ... - TQn
            String[] infos = data.split("-");
            if ((infos != null) && (infos.length > 0)) {
                int tclt = Integer.parseInt(infos[2]);
                int tcth = Integer.parseInt(infos[3]);
                Subject sub = subjectService.findById(infos[0]);
                sub.setSubjectName(infos[1]);
                sub.setnumTCLT(tclt);
                sub.setnumTCTH(tcth);
                sub.setnumTC(tcth + tclt);
                
                List<Subject> preSubjects = new ArrayList<Subject>(10);
                for (int i = 4; i < infos.length; i++) {
                    Subject s = new Subject();
                    s.setId(infos[i]);
                    preSubjects.add(s);
                }

                if (!preSubjects.isEmpty()) {
                    sub.setPreSub(preSubjects);
                }

                ExecuteResult er = subjectService.updateSubject(sub);
                
                if (!er.isIsSucces()) {
                    out.println("Lỗi: " + er.getMessage());
                } else {
                    Subject s_temp = (Subject) er.getData();
                    List<Subject> preSub_temp = s_temp.getPreSub();
                    out.println("<b>Cập nhật thành công</b></br>");
                    out.println("- Môn học: " + s_temp.getSubjectName() + " (" + s_temp.getId() + ").</br>");
                    out.println("- Số TCLT: " + s_temp.getnumTCLT() + "</br>");
                    out.println("- Số TCTH: " + s_temp.getnumTCTH() + "</br>");
                    List<String> preData = new ArrayList<String>(10);
                    if ((preSub_temp != null) && !preSub_temp.isEmpty()) {
                        out.println("<u>Các môn học tiên quyết:</u></br>");
                        for (int i = 0; i < preSub_temp.size(); i++) {
                            out.println("- " + (i + 1) + ": " + preSub_temp.get(i).getSubjectName()
                                    + " (" + preSub_temp.get(i).getId() + ")</br>");
                            String temp = preSub_temp.get(i).getId() + "-" +
                                    preSub_temp.get(i).getSubjectName();
                            preData.add(temp);
                        }
                    }
                    //HttpSession session = resquest.getSession();
                    //session.setAttribute("pre_subjects", preData);
                }
            } else {
                out.println("Cập nhật môn học không thành công. - Infos null");
            }
        } catch (Exception ex) {
            out.println("Cập nhật môn học không thành công: " + ex.toString());
            Logger.getLogger(ManageSubjectController.class.getName())
                    .log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }
    }
   
    
    /**
     * After add subject submited, please send back information
     * notify user...
     * @param er data include subject's information
     * @param out user output stream
     */
    private void writeResponeAddSubject(ExecuteResult er, PrintWriter out) {
        if (!er.isIsSucces()) {
            out.println("Lỗi: " + er.getMessage());
        } else {
            Subject s = (Subject) er.getData();
            out.println("Đã thêm thành công môn học mới:");
            out.println("<table class='general-table'>");
            // Print header of table...
            out.println("<tr>"
                    + "<th> Mã MH </th>"
                    + "<th> Tên MH </th>"
                    + "<th> Số TCLT </th>"
                    + "<th> Số TCTH </th>"
                    + "</tr>");
            // Print out content
            out.println("<tr>"
                    + "<td>" + s.getId() + "</td>"
                    + "<td>" + s.getSubjectName() + "</td>"
                    + "<td>" + s.getnumTCLT() + "</td>"
                    + "<td>" + s.getnumTCTH() + "</td>"
                    + "</tr>");
            out.println("</table>");
        }
    }

    private void writeResponeDeleteSubject(ExecuteResult er, PrintWriter out) {
            List<Subject> subjects= subjectService.findAll(Constants.ELEMENT_PER_PAGE_DEFAULT, 1, "MaMH", "DESC");
            if ((subjects != null) && !subjects.isEmpty()) {
                writeOutListSubject(out, subjects,1);
            }
        
    }

    public enum SubjectManageFunction {
        LIST_SUBJECT("list_subject"),
        DELETE("delete"),
        PRE_ADD_SUBJECT("pre_add_subject"),
        ADD_SUBJECT("add_subject"),
        SEARCH("search"),
        SORT("sort"),
        EDIT_SUBJECT("edit_subject"),
        FILTER("Filter"),
        DELETE_SUBJECT("delete_subject");

        private String function;
        private SubjectManageFunction(String function) {
            this.function = function;
        }
        
        public String value() {
            return function;
        }
    }
}
