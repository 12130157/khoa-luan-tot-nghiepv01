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
import uit.cnpm02.dkhp.model.PreSubID;
import uit.cnpm02.dkhp.model.PreSubject;
import uit.cnpm02.dkhp.model.Subject;
import uit.cnpm02.dkhp.service.IFacultyService;
import uit.cnpm02.dkhp.service.ISubjectService;
import uit.cnpm02.dkhp.service.impl.FacultyServiceImpl;
import uit.cnpm02.dkhp.service.impl.SubjectServiceImpl;
import uit.cnpm02.dkhp.utilities.Constants;
import uit.cnpm02.dkhp.utilities.ExecuteResult;
import uit.cnpm02.dkhp.utilities.Message;

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
    private IFacultyService facultyService = new FacultyServiceImpl();
    private int currentPage = 1;
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

            boolean pageAlreadySet = true;
            int numpage = -1;
            try {
                numpage = ((Integer) session.getAttribute("numpage_sub")).intValue();
            } catch (Exception ex) {
                pageAlreadySet = false;
            }
            if (!pageAlreadySet || (numpage < 0)) {
                int numPage = subjectService.getNumberPage();
                session.setAttribute("numpage_sub", numPage);
            }

            if (action.equals(SubjectManageFunction.LIST_SUBJECT.value())) {
                listSubject(request, response);
                path = "./jsps/PDT/SubjectManager.jsp";
            } else if (action.equals(SubjectManageFunction.SEARCH.value())) {
                String key = request.getParameter("key");
                List<Subject> subject = searchSubject(session.getId(),key);
                if ((subject != null) && !subject.isEmpty()) {
                    writeOutListSubject(out, subject);
                }
                return;
            } else if (action.equals(SubjectManageFunction.SORT.value())) {
                String by = request.getParameter("by");
                String type = request.getParameter("type");
                List<Subject> subjects = sort(session.getId(), by, type);
                writeOutListSubject(out, subjects);
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
            } else if (action.equals("delete_single_subject")) {
                deleteSingle(request, response);
            } else if (action.equals("edit_subject")) {
                editSubject(request, response);
                path = "./jsps/PDT/EditSubject.jsp";
                response.sendRedirect(path);
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

    /**
     * Get list student, send it to client.
     * @param req
     * @param resp
     * @throws Exception 
     */
    private void listSubject(HttpServletRequest req
            , HttpServletResponse resp) throws Exception {

        try {
            currentPage = Integer.parseInt(req.getParameter("currentpage"));
        } catch (Exception ex) {
            currentPage = 1;
        }

        List<Subject> subjects = subjectService
                .findSubjects(req.getSession().getId(), currentPage);
        
            HttpSession session = req.getSession();
            session.setAttribute("list_subject", subjects);
        /*} else if ((subjects != null) && (!subjects.isEmpty())) {
            PrintWriter out = resp.getWriter();
            writeOutListSubject(out, subjects);
        }*/
    }
    
    private List<Subject> searchSubject(String sessionId, String key) {
        return subjectService.search(sessionId, key);
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
        
    private void writeOutListSubject(PrintWriter out, List<Subject> subjects) {
        try {
            // Search(by, type)
            String method = "sort('%s','%s')";
            String tblHeader = "<tr>"
                    + "<th> STT </th>"
                    + "<th> <a href='#' onclick=\""
                        + String.format(method, "MaMH", "ASC") +"\"> Mã MH </a></th>"
                    + "<th> <a href='#' onclick=\""
                        + String.format(method, "TenMH", "ASC") +"\"> Tên Môn học </a></th>"
                    + "<th> <a href='#' onclick=\""
                        + String.format(method, "SoTC", "ASC") +"\"> Số TC </a></th>"
                    + "<th> <a href='#' onclick=\""
                        + String.format(method, "SoTCLT", "ASC") +"\"> Số TCLT </a></th>"
                    + "<th> <a href='#' onclick=\""
                        + String.format(method, "SoTCTH", "ASC") +"\"> Số TCTH </a></th>"
                    + "<th> Sửa </th>"
                    + "<th> Xóa </th>"
                    + "</tr>";
            out.println(tblHeader);
            for (int i = 0; i < subjects.size(); i++) {
                String currentLine = "<tr>"
                        + "<td>" + ((currentPage - 1) * Constants.ELEMENT_PER_PAGE_DEFAULT + i + 1) + "</td>"
                        + "<td>" + subjects.get(i).getId() + "</td> "
                        + "<td>" + subjects.get(i).getSubjectName() + "</td>"
                        + "<td>" + subjects.get(i).getnumTC() + "</td>"
                        + "<td>" + subjects.get(i).getnumTCLT() + "</td>"
                        + "<td>" + subjects.get(i).getnumTCTH() + "</td>"
                        + "<td><a href = \"../../ManageSubjectController?function=edit_subject&subject_code=" + subjects.get(i).getId() + "\">Sửa</a></td>"
                        + "<td><a href = \"../../ManageSubjectController?function=delete_single_subject&ajax=false&currentpage=" + currentPage + "&subject_code=" + subjects.get(i).getId() + "\">Xóa</a></td>"
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
            Logger.getLogger(ManageSubjectController.class.getName()).log(Level.SEVERE, null, ex);
            out.println("Đã có lỗi xảy ra. " + ex.toString());
        }
    }

    /**
     * Delete single subject.
     * The infomation is attached in session
     * by its id.
     * 
     * @param request HttpServletRequest object
     * @param response HttpServletResponse object
     */
    private void deleteSingle(HttpServletRequest request, HttpServletResponse response) {
        String subId = request.getParameter("subject_code");
        if ((subId == null) || (subId.isEmpty())) {
            return;
        }

        HttpSession session = request.getSession();
        try {
            SubjectDAO subDao = DAOFactory.getSubjectDao();
            Subject s = subDao.findById(subId);
            subDao.delete(s);

            //Delete success
            
            int numPage = subjectService.getNumberPage();
            session.setAttribute("numpage_sub", numPage);

        } catch (Exception ex) {
            Logger.getLogger(ManageSubjectController.class.getName()).log(Level.SEVERE, null, ex);
            session.setAttribute("error", "Xóa không thành công: " + ex.toString());
        }

        try {
            listSubject(request, response);
        } catch (Exception ex) {
            Logger.getLogger(ManageSubjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                result.setMessage(Message.ADD_SUBJECT_ERROR + " -- " + ex.toString());
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
            if (infos.length >= 7) {
                String preSubId[] = infos[6].split("-");
                List<Subject> preSubs = new ArrayList<Subject>(3);
                for (int i = 0; i < preSubId.length; i++) {
                    Subject preSub = new Subject();
                    preSub.setId(subjectId);
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
    
    private void editSubject(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String data = (String) request.getParameter("data");
        String subId = (String) request.getParameter("subject_code");

        SubjectDAO subDao = DAOFactory.getSubjectDao();
        if (data == null) {
            try {
                //Just repare data
                //
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
                    }
                }

                //return;
            } catch (Exception ex) {
                Logger.getLogger(ManageSubjectController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            // Process action add subject
            // Data get from AddSubject.jsp form
            // and it's saved in data string.
            // in format: ...
            PrintWriter out = null;
            try {
                out = response.getWriter();

                String[] infos = data.split("-");
                if ((infos != null) && (infos.length > 0)) {
                    int tclt = Integer.parseInt(infos[2]);
                    int tcth = Integer.parseInt(infos[3]);
                    Subject sub = null; //new Subject(infos[0], infos[1], tclt + tcth, tcth);

                    Subject updatedSub = DAOFactory.getSubjectDao().update(sub);
                    if (updatedSub != null) {
                        //Add subject successful
                        //Init Pre subject.
                        List<PreSubject> preSubjectsExisted = DAOFactory.getPreSubDao().findByColumName("MaMH", updatedSub.getId());
                        if ((preSubjectsExisted != null) && (!preSubjectsExisted.isEmpty())) {
                            DAOFactory.getPreSubDao().delete(preSubjectsExisted);
                        }
                        
                        List<PreSubject> preSubjects = new ArrayList<PreSubject>(10);
                        for (int i = 4; i < infos.length; i++) {
                            PreSubID preId = new PreSubID(infos[0], infos[i]);
                            PreSubject ps = new PreSubject();
                            ps.setId(preId);
                            preSubjects.add(ps);
                        }

                        if (!preSubjects.isEmpty()) {
                            DAOFactory.getPreSubDao().addAll(preSubjects);
                        }
                    }

                    out.println("Cập nhật môn học thành công.");
                } else {
                    out.println("Cập nhật môn học không thành công.");
                }
            } catch (Exception ex) {
                out.println("Cập nhật môn học không thành công: " + ex.toString());
                Logger.getLogger(ManageSubjectController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                out.close();
            }
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
            out.println("Print out error please...");
        } else {
            Subject s = (Subject) er.getData();
            out.println("Đã thêm thành công môn học mới:");
            out.println("<table>");
            // Print header of table...
            out.println("<tr>"
                    + "<td> Mã MH </td>"
                    + "<td> Tên MH </td>"
                    + "<td> Something else... </td>"
                    + "</tr>");
            // Print out content
            out.println("<tr>"
                    + "<td><a href=''>" + s.getId() + "</a></td>"
                    + "<td>" + s.getSubjectName() + "</td>"
                    + "<td> ... </td>"
                    + "</tr>");
            out.println("</table>");
        }
    }

    public enum SubjectManageFunction {
        LIST_SUBJECT("list_subject"),
        DELETE_SINGLE("delete_single_subject"),
        PRE_ADD_SUBJECT("pre_add_subject"),
        ADD_SUBJECT("add_subject"),
        SEARCH("search"),
        SORT("sort"),
        EDIT_SUBJECT("edit_subject");

        private String function;
        private SubjectManageFunction(String function) {
            this.function = function;
        }
        
        public String value() {
            return function;
        }
    }
}
