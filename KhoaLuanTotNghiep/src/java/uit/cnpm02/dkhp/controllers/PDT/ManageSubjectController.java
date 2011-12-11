package uit.cnpm02.dkhp.controllers.PDT;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
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
import uit.cnpm02.dkhp.DAO.StudentDAO;
import uit.cnpm02.dkhp.DAO.SubjectDAO;
import uit.cnpm02.dkhp.model.PreSubID;
import uit.cnpm02.dkhp.model.PreSubject;
import uit.cnpm02.dkhp.model.Student;
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

    private int currentPage = 1;

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
                // Not support functions.
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
                int numSubExisted = DAOFactory.getSubjectDao().getRowsCount();
                int numPage = getNumberPage(numSubExisted);
                session.setAttribute("numpage_sub", numPage);
            }

            if (action.equals("list_subject")) {
                listSubject(request, response);
                path = "./jsps/PDT/SubjectManager.jsp";
            //} else if (action.equals("insert_sub_from_table")) {
            //    insertSubFromTable(request, response);
                //return;
            } else if (action.equals("delete_single_subject")) {
                deleteSingle(request, response);
                //subject_code
            } else if (action.equals("add_subject")) {
                addSubject(request, response);
                //Id not ajax called
                path = "./jsps/PDT/AddSubject.jsp";
                response.sendRedirect(path);
                return;
            }

            if (!ajaxCalled) {
                response.sendRedirect(path);
            }

        } catch (Exception ex) {
            Logger.getLogger(ManageSubjectController.class.getName()).log(Level.SEVERE, null, ex);
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
    private void listSubject(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        boolean ajaxCalled = false;
        try {
            currentPage = Integer.parseInt(req.getParameter("currentpage"));
            ajaxCalled = Boolean.parseBoolean(req.getParameter("ajax"));
        } catch (Exception ex) {
            currentPage = 1;
        }

        SubjectDAO subDao = DAOFactory.getSubjectDao();
        List<Subject> subjects = subDao.findAll(Constants.ELEMENT_PER_PAGE_DEFAULT, currentPage, null, null);
        if (!ajaxCalled) {
            HttpSession session = req.getSession();
            session.setAttribute("list_subject", subjects);
        } else if ((subjects != null) && (!subjects.isEmpty())) {
            PrintWriter out = resp.getWriter();
            try {
                String tblHeader = "<tr>"
                        + "<th><INPUT type = \"checkbox\""
                        + "name = \"chkAll\""
                        + "onclick = \"selectAll('tablelistsubject')\"/></th>"
                        + "<th> STT </th>"
                        + "<th> Mã MH </th>"
                        + "<th> Tên Môn học </th>"
                        + "<th> Số TC </th>"
                        + "<th> Số TCLT </th>"
                        + "<th> Số TCTH </th>"
                        + "<th> Sửa </th>"
                        + "<th> Xóa </th>"
                        + "</tr>";
                out.println(tblHeader);
                for (int i = 0; i < subjects.size(); i++) {
                    String currentLine = "<tr>"
                            + "<td><INPUT type=\"checkbox\" name=\"chk<%= i%>\"/></td>"
                            + "<td>" + ((currentPage - 1) * Constants.ELEMENT_PER_PAGE_DEFAULT + i + 1) + "</td>"
                            + "<td>" + subjects.get(i).getId() + "</td> "
                            + "<td>" + subjects.get(i).getSubjectName() + "</td>"
                            + "<td>" + subjects.get(i).getnumTCLT() + "</td>"
                            + "<td>" + subjects.get(i).getnumTCLT() + "</td>"
                            + "<td>" + subjects.get(i).getnumTCTH() + "</td>"
                            + "<td><a href = \"../../ManageSubjectController?function=edit_subject&subject_code=" + subjects.get(i).getId() + "\">Sửa</a></td>"
                            + "<td><a href = \"../../ManageSubjectController?function=delete_single_subject&ajax=false&currentpage=" + currentPage + "&subject_code=" + subjects.get(i).getId() + "\">Xóa</a></td>"
                            + "</tr>";
                    out.println(currentLine);
                }
            } catch (Exception ex) {
                //
            } finally {
                out.close();
            }
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
            int numSubExisted = subDao.getRowsCount();
            int numPage = getNumberPage(numSubExisted);
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
     * Return number page of number subject.
     * This number maybe change when add or
     * remove some entity(ies).
     * 
     * @param existedRow number entites.
     * @return number page.
     */
    private int getNumberPage(int existedRow) {
        int rowsPerPage = Constants.ELEMENT_PER_PAGE_DEFAULT;

        if (existedRow % rowsPerPage == 0) {
            return existedRow / rowsPerPage;
        } else {
            return existedRow / rowsPerPage + 1;
        }
    }

    /**
     * Add subject from subject form.
     * 
     * @param request
     * @param response 
     */
    private void addSubject(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String data = (String) request.getParameter("data");

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
                    Subject sub = new Subject(infos[0], infos[1], tclt + tcth, tcth);
                    
                    String id = DAOFactory.getSubjectDao().add(sub);
                    if ((id != null) && (id.length() > 0)) {
                        //Add subject successful
                        //Init Pre subject.
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
                    
                    out.println("Thêm môn học thành công.");
                } else {
                    out.println("Thêm môn học không thành công.");
                }
            } catch (Exception ex) {
                out.println("Thêm môn học không thành công: " + ex.toString());
                Logger.getLogger(ManageSubjectController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                out.close();
            }
        }
    }
}
