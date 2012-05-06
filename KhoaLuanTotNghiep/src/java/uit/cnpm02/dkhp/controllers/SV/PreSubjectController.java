package uit.cnpm02.dkhp.controllers.SV;

import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uit.cnpm02.dkhp.model.PreSubID;
import uit.cnpm02.dkhp.model.PreSubject;
import uit.cnpm02.dkhp.model.Subject;
import uit.cnpm02.dkhp.service.IPreSubject;
import uit.cnpm02.dkhp.service.ISubjectService;
import uit.cnpm02.dkhp.service.impl.PreSubjectImpl;
import uit.cnpm02.dkhp.service.impl.SubjectServiceImpl;
import uit.cnpm02.dkhp.utilities.ExecuteResult;

/**
 *
 * @author thanh
 */
@WebServlet(name = "PreSubjectController", urlPatterns = {"/PreSubjectController"})
public class PreSubjectController extends HttpServlet {

    //private PreSubjectDAO preDao = new PreSubjectDAO();
    private IPreSubject preSubService = new PreSubjectImpl();
    private ISubjectService subjectService = new SubjectServiceImpl();

    /** 
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        try {
            String action = request.getParameter("action");
            if (action.equalsIgnoreCase(PreSubManageFunction.DEFAULT.value())) {
                loadDefault(session, response);
                return;
            }else if (action.equalsIgnoreCase("view")) { // Student only
                List<PreSubject> preSubs = getAll(session.getId());
                session.setAttribute("preSub", preSubs);
                String path = "./jsps/SinhVien/PreSubject.jsp";
                response.sendRedirect(path);
                return;
            }/* else if (action.equalsIgnoreCase("manage")) {
                loadDataForManage(request, response);
            }*/else if (action.equalsIgnoreCase(PreSubManageFunction.CHECK_EXIST.value())) {
                doCheckExisted(request, response);
            } else if (action.equalsIgnoreCase(PreSubManageFunction.SEARCH.value())) {
                String key = request.getParameter("key");
                search(session.getId(), key, out);
            } else if (action.equalsIgnoreCase(PreSubManageFunction.ADD_PRESUB.value())) {
                addSub(request, out);
            } else if (action.equalsIgnoreCase(PreSubManageFunction.DELETE_PRESUB.value())) {
                String subId = request.getParameter("subid");
                String preSubId = request.getParameter("presubid");
                doDeletePreSub(out, session, subId, preSubId);
            } else if (action.equalsIgnoreCase(PreSubManageFunction.SORT.value())) {
                String by = request.getParameter("by");
                String type = request.getParameter("type");
                doSort(out, session.getId(), by, type);
                
            }
        } finally {
            out.close();
        }
    }
    
    private void loadDefault(HttpSession session, HttpServletResponse response) {
        try {
            List<PreSubject> preSubs = getAll(session.getId());
            List<Subject> subjects = subjectService.getAll("");

            if ((preSubs != null) && !preSubs.isEmpty()) {
                session.setAttribute("list_pre_sub", preSubs);
            }
            
            if ((subjects != null) && !subjects.isEmpty()) {
                session.setAttribute("list_sub", subjects);
            }
            String path = "./jsps/PDT/PreSubjectManager.jsp";
            response.sendRedirect(path);
        } catch (Exception ex) {
            Logger.getLogger(PreSubjectController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private List<PreSubject> getAll(String sessionId) throws IOException {
        List<PreSubject> results = new ArrayList<PreSubject>(10);
        try {
            results = preSubService.getAll(sessionId, true);
        } catch (Exception ex) {
            //
        }
        return results;
    }

    /**
     * 
     * @param preSub
     * @return 
     */
    private void doCheckExisted(HttpServletRequest req,
                                                HttpServletResponse resp) {
        PrintWriter out = null;

        try {
            String subjectID = req.getParameter("sub");
            String preSubjectID = req.getParameter("pre-sub");
            out = resp.getWriter();
            ExecuteResult er = preSubService.isExisted(subjectID, preSubjectID);

            //CheckResult checkResult = checkPreSubjectExisted(subjectID,
            //        preSubjectID);
            out.println(er.getMessage());

        } catch (Exception ex) {
            log("Có lỗi xảy ra trong quá trình kiểm tra. ", ex);
            out.println("Có lỗi xảy ra trong quá trình kiểm tra. " + ex.toString());
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                //
            }
        }
    }

    private void doDeletePreSub(PrintWriter out, HttpSession session,
                                        String subId, String preSubId) {
        try {
            if (subId == null
                    || preSubId == null
                    || subId.isEmpty()
                    || preSubId.isEmpty()) {
                return;
            }

            preSubService.deletePreSubject(session.getId(), subId, preSubId);
            
            List<PreSubject> preSubjects = preSubService
                                .getCurrentPreSubjects(session.getId());
            
            writeOutListPreSubject(out, preSubjects, true, true);
        } catch (Exception ex) {
            log("Error occur while do checking the pre subject", ex);
        }
    }

    private void addSub(HttpServletRequest req, PrintWriter out) {
        try {
            String subjectId = req.getParameter("sub");
            String preSubjectId = req.getParameter("pre-sub");

            ExecuteResult er = preSubService.isExisted(subjectId, preSubjectId);
            if (er.isIsSucces() == false) {
                out.println(er.getMessage());
                return;
            }

            PreSubID id = new PreSubID(subjectId, preSubjectId);
            PreSubject preSub = new PreSubject();
            preSub.setId(id);
            preSub = preSubService.addPreSubject(preSub);

            if (preSub == null) {
                out.println("Đã có lỗi xảy ra.");
            } else {
                List<PreSubject> data = new ArrayList<PreSubject>(5);
                data.add(preSub);
                out.println("Thêm thành công.");
                writeOutListPreSubject(out, data, false, false);
            }
        } catch (Exception ex) {
            log("Error occur while do checking the pre subject", ex);
        }
    }
    
    private void writeOutListPreSubject(PrintWriter out, List<PreSubject> preSubjects,
            boolean includeLinkSort, boolean includeBtnDelete) {
        try {
            out.println("<table class='general-table'" + (!includeBtnDelete ? " style=\"width:547px !important\"" : "") + ">");
            String tblHeader = "<tr>"
                    + "<th> STT </th>"
                    + "<th> " + (includeLinkSort ? "<a href='#' onclick =\"sort('TenMH')\">" : "") + " Môn học " + (includeLinkSort ? "</a>" : "") + " </th>"
                    + "<th> " + (includeLinkSort ? "<a href='#' onclick =\"sort('TenMHTQ')\">" : "") + "Môn học tiên quyết " + (includeLinkSort ? "</a>" : "") + " </th>"
                    + ((includeBtnDelete == true) ? "<th></th>" : "")
                      + "</tr>";
            out.println(tblHeader);
            //<a href="deletePreSub(<%=preSubjects.get(j).getId().getSudId()%>, <%=preSubjects.get(j).getId().getPreSudId() %>)"></a>
            String btnOk = "<a href='#' onclick=\"deletePreSub('%s', '%s')\"> Xóa</a>";
            for (int i = 0; i < preSubjects.size(); i++) {
                PreSubject ps = preSubjects.get(i);
                String currentLine = "<tr>"
                        + "<td> " + (i + 1) + " </td>"
                        + "<td> " + ps.getSubjectName() + " </td>"
                        + "<td> " + ps.getPreSubjectName() + " </td> "
                        + "<td> " + ((includeBtnDelete == true) ? String.format(btnOk, ps.getId().getSudId(), ps.getId().getPreSudId()) : "") + "</td>"
                        + "</tr>";
                out.println(currentLine);
            }
            out.println("</table>");
        } catch (Exception ex) {
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
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void search(String sessionId, String key, PrintWriter out) {
        List<PreSubject> preSubjects = preSubService.search(sessionId, key);
        
        if(preSubjects == null) {
            out.print("Không tìm thấy dữ liệu");
        } else {
            out.print("<b>Danh sách môn học tiên quyết:</b>");
            writeOutListPreSubject(out, preSubjects, true, true);
        }
    }

    private void doSort(PrintWriter out, String sessionId, String by, String type) {
        preSubService.sort(sessionId, by, type);
        List<PreSubject> preSubjects = preSubService.getCurrentPreSubjects(sessionId);
        
        if ((preSubjects != null) && !preSubjects.isEmpty()) {
            String newType = (type.equalsIgnoreCase("ASC") ? "DES" : "ASC");
            out.println("<input type=\"hidden\" id=\"sort-type\" value=\"" + newType + "\" />");
            writeOutListPreSubject(out, preSubjects, true, true);
        }
    }

    public enum PreSubManageFunction {
        DEFAULT("manage"),
        VIEW("view"),
        LIST_PRESUB("list_presubject"),
        DELETE_PRESUB("delete"),
        ADD_PRESUB("add-pre-sub"),
        CHECK_EXIST("check-existed"),
        SEARCH("search"),
        SORT("sort");

        private String function;
        private PreSubManageFunction(String function) {
            this.function = function;
        }
        
        public String value() {
            return function;
        }
    }
}
