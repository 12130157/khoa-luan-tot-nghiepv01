package uit.cnpm02.dkhp.controllers.SV;

import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.PreSubjectDAO;
import uit.cnpm02.dkhp.DAO.SubjectDAO;
import uit.cnpm02.dkhp.model.PreSubID;
import uit.cnpm02.dkhp.model.PreSubject;
import uit.cnpm02.dkhp.model.Subject;

/**
 *
 * @author thanh
 */
@WebServlet(name = "PreSubjectController", urlPatterns = {"/PreSubjectController"})
public class PreSubjectController extends HttpServlet {

    private PreSubjectDAO preDao = new PreSubjectDAO();

    /** 
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        try {
            String action = request.getParameter("action");
            if (action.equalsIgnoreCase("view")) {
                viewPreSubject(response, session);
            } else if (action.equalsIgnoreCase("manage")) {
                loadDataForManage(request, response);
            } else if (action.equalsIgnoreCase("check-existed")) {
                doCheckExisted(request, response);
            } else if (action.equalsIgnoreCase("add-pre-sub")) {
                addSub(request, response);
            } else if (action.equalsIgnoreCase("delete")) {
                String message = deleteSub(request, response);
                if (!message.isEmpty()) {
                    session.setAttribute("error", message);
                }
                
                loadDataForManage(request, response);
            }
        } finally {
            out.close();
        }
    }

    private void viewPreSubject(HttpServletResponse response, HttpSession session) throws IOException {
        String path = "";
        try {
            List<PreSubject> preSub = preDao.findAll();
            setSubjectName(preSub);
            session.setAttribute("preSub", preSub);
            path = "./jsps/SinhVien/PreSubject.jsp";
        } catch (Exception ex) {
            path = "./jsps/Message.jsp";
        }
        response.sendRedirect(path);
    }

    private void setSubjectName(List<PreSubject> preSub) throws Exception {
        SubjectDAO subjectDao = new SubjectDAO();
        for (int i = 0; i < preSub.size(); i++) {
            preSub.get(i).setPreSubjectName(subjectDao.findById(preSub.get(i).getId().getPreSudId()).getSubjectName());
            preSub.get(i).setSubjectName(subjectDao.findById(preSub.get(i).getId().getSudId()).getSubjectName());
        }
    }
    //
    // For admin functional.
    //
    private Map<String, String> subNameMapping = new HashMap<String, String>(10);

    private void loadDataForManage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SubjectDAO subDao = DAOFactory.getSubjectDao();
        List<String> data = new ArrayList<String>(10);
        List<Subject> subs = null;
        List<PreSubject> preSubs = null;

        try {
            HttpSession session = request.getSession();
            subs = subDao.findAll();

            if ((subs != null) && (!subs.isEmpty())) {
                for (Subject sub : subs) {
                    String temp = sub.getSubjectName() + " - " + sub.getId();
                    data.add(temp);
                    subNameMapping.put(sub.getId(), sub.getSubjectName());
                }
            }

            //preSubs = preDao.findAll(Constants.ELEMENT_PER_PAGE_DEFAULT, 1, null, null);
            preSubs = preDao.findAll();

            Collections.sort(data);

            for (PreSubject preSub : preSubs) {
                String subName = subNameMapping.get(preSub.getId().getSudId());
                String preName = subNameMapping.get(preSub.getId().getPreSudId());

                preSub.setSubjectName(subName);
                preSub.setPreSubjectName(preName);
            }

            session.setAttribute("list_sub", data);
            session.setAttribute("list_pre_sub", preSubs);
        } catch (Exception ex) {
            log("Error while try to get all subject", ex);
        }

        String path = "./jsps/PDT/PreSubjectManager.jsp";
        response.sendRedirect(path);

    }

    private CheckResult checkPreSubjectExisted(String subjectId, String preSubjectId) {
        String message = null;
        boolean value = true;
        try {
            if (subjectId.equals(preSubjectId)) {
                message = "Giá trị không hợp lệ"; //Mon hoc tien quyet khong the la chinh no
                value = false;
            } else {
                PreSubID id = new PreSubID(subjectId, preSubjectId);
                if (DAOFactory.getPreSubDao().findById(id) != null) {
                    message = "Đã tồn tại";
                    value = false;
                } else {
                    message = "Giá trị hợp lệ";
                }
            }
        } catch (Exception ex) {
            log("Error occur while do checking the pre subject", ex);
        }
        return new CheckResult(value, message);
    }

    /**
     * 
     * @param preSub
     * @return 
     */
    private void doCheckExisted(HttpServletRequest req, HttpServletResponse resp) {
        PrintWriter out = null;

        try {
            String subjectNameStr = req.getParameter("sub");
            String preSubjectNameStr = req.getParameter("pre-sub");
            out = resp.getWriter();

            if (subjectNameStr == null
                    || preSubjectNameStr == null
                    || subjectNameStr.isEmpty()
                    || preSubjectNameStr.isEmpty()) {
                out.println("Đã có lỗi xảy ra.");
                return;
            }

            String[] subjectData = subjectNameStr.split("-");
            String[] preSubjectData = preSubjectNameStr.split("-");

            String message = "";
            CheckResult checkResult = null;
            if (subjectData.length < 2
                    || preSubjectData.length < 2) {
                message = "Đã có lỗi xảy ra";
            } else {
                checkResult = checkPreSubjectExisted(subjectData[1].trim(), preSubjectData[1].trim());
                message = checkResult.getMessage();
            }

            out.println(message);
        } catch (Exception ex) {
            log("Error occur while do checking the pre subject", ex);
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                //
            }
        }
    }

    private String deleteSub(HttpServletRequest req, HttpServletResponse resp) {
        String message = "";
        try {
            String subId = req.getParameter("sub-id");
            String preSubId = req.getParameter("pre-sub-id");

            if (subId == null
                    || preSubId == null
                    || subId.isEmpty()
                    || preSubId.isEmpty()) {
                return "Đã có lỗi xảy ra.";
            }

            PreSubID id = new PreSubID(subId, preSubId);
            PreSubject preSub = preDao.findById(id);
            
            if (preSub != null) {
                preDao.delete(preSub);
            }
        } catch (Exception ex) {
            log("Error occur while do checking the pre subject", ex);
        }
        
        return message;
    }

    private void addSub(HttpServletRequest req, HttpServletResponse resp) {
        PrintWriter out = null;

        try {
            String subjectNameStr = req.getParameter("sub");
            String preSubjectNameStr = req.getParameter("pre-sub");
            out = resp.getWriter();

            if (subjectNameStr == null
                    || preSubjectNameStr == null
                    || subjectNameStr.isEmpty()
                    || preSubjectNameStr.isEmpty()) {
                out.println("Đã có lỗi xảy ra.");
                return;
            }

            String[] subjectData = subjectNameStr.split("-");
            String[] preSubjectData = preSubjectNameStr.split("-");

            String message = "Thêm thành công";
            if (subjectData.length < 2
                    || preSubjectData.length < 2) {
                message = "Đã có lỗi xảy ra";
            } else {
                PreSubID id = new PreSubID(subjectData[1].trim(), preSubjectData[1].trim());
                CheckResult check = checkPreSubjectExisted(id.getSudId(), id.getPreSudId());

                if (check.isOK) {
                    PreSubject preSub = new PreSubject();
                    preSub.setId(id);
                    id = preDao.add(preSub);
                    if (id == null) {
                        message = "Đã có lỗi xảy ra.";
                    }
                } else {
                    message = check.getMessage();
                }
            }

            out.println(message);
        } catch (Exception ex) {
            log("Error occur while do checking the pre subject", ex);
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                //
            }
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

    private class CheckResult {

        private boolean isOK;
        private String message;

        public CheckResult() {
        }

        public CheckResult(boolean isOK, String message) {
            this.isOK = isOK;
            this.message = message;
        }

        public boolean isIsOK() {
            return isOK;
        }

        public void setIsOK(boolean isOK) {
            this.isOK = isOK;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
