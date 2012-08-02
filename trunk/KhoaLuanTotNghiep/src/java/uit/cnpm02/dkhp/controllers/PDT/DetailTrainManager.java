/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import uit.cnpm02.dkhp.DAO.DetailTrainDAO;
import uit.cnpm02.dkhp.DAO.LecturerDAO;
import uit.cnpm02.dkhp.DAO.SubjectDAO;
import uit.cnpm02.dkhp.model.DetailTrain;
import uit.cnpm02.dkhp.model.DetailTrainID;
import uit.cnpm02.dkhp.model.Lecturer;
import uit.cnpm02.dkhp.model.Subject;
import uit.cnpm02.dkhp.service.IReporter;
import uit.cnpm02.dkhp.service.impl.ReporterImpl;
import uit.cnpm02.dkhp.utilities.Message;

/**
 *
 * @author thanh
 */
@WebServlet(name = "DetailTrainManager", urlPatterns = {"/DetailTrainManager"})
public class DetailTrainManager extends HttpServlet {

    /** 
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
     private IReporter reportService = new ReporterImpl();
     private LecturerDAO lecturerDao= DAOFactory.getLecturerDao();
     private DetailTrainDAO detailDao= DAOFactory.getDetainTrainDAO();
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String requestAction;
        try {
          requestAction = (String) request.getParameter("action");
          if (requestAction.equals(ReportFunctionSupported.
                                                SEARCH_LECTURER.getValue())) {
              searchLecturer(request, response);
          }else if(requestAction.equals(ReportFunctionSupported.
                                                LECTURER_REPORT.getValue())) {
              String lecturerCode = request.getParameter("value");
              getDetailTrainByLecturer(lecturerCode,out);
          }else if(requestAction.equals(ReportFunctionSupported.
                                                PRE_UPDATE.getValue())){
              preUpdateTrainDetailByLecturer(request, response);
          }else if(requestAction.equals(ReportFunctionSupported.
                                                ADD_DETAIL.getValue())){
              addDetailTrain(request, response);
          }else if(requestAction.equals(ReportFunctionSupported.
                                                REMOVE_DETAIL.getValue())){
              removeDetailTrain(request, response);
          }else if(requestAction.equals(ReportFunctionSupported.
                                                UPDATE.getValue())){
             updateComplete(request, response);
          }
        } finally {           
            out.close();
        }
    }
    private void updateComplete(HttpServletRequest request, HttpServletResponse response){
        try {
            PrintWriter out = response.getWriter();
            out.println("Cập nhật thành công");
        } catch (IOException ex) {
            Logger.getLogger(DetailTrainManager.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    private void writeDetailTrainListAfterUpdate(PrintWriter out, List<DetailTrain> listDetailTrain){
        if(listDetailTrain!= null && !listDetailTrain.isEmpty()){
            for(int i=0; i<listDetailTrain.size(); i++){
                out.println("<option value='" + listDetailTrain.get(i).getId().getSubjectCode()+ "'>"+listDetailTrain.get(i).getSubjectName()+"</option>");
            }
        }
    }
    private void addDetailTrain(HttpServletRequest request, HttpServletResponse response){
        PrintWriter out;
        try {
            out = response.getWriter();
            String subjectCode = request.getParameter("subCode");
            String lecCode = request.getParameter("lecCode");
            DetailTrainID id = new DetailTrainID(lecCode, subjectCode);
            DetailTrain checkItem = detailDao.findById(id);
            if(checkItem == null){
               DetailTrain detailTrain = new DetailTrain(id);
               detailDao.add(detailTrain);
            }
            List<DetailTrain> listDetailTrain = getdetailTrainByLecturer(lecCode);
            writeDetailTrainListAfterUpdate(out, listDetailTrain);
        } catch (Exception ex) {
           Logger.getLogger(DetailTrainManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void removeDetailTrain(HttpServletRequest request, HttpServletResponse response){
       PrintWriter out;
        try {
            out = response.getWriter();
            String subjectCode = request.getParameter("subCode");
            String lecCode = request.getParameter("lecCode");
            DetailTrainID id = new DetailTrainID(lecCode, subjectCode);
            DetailTrain detailTrain = new DetailTrain(id);
            detailDao.delete(detailTrain);
            List<DetailTrain> listDetailTrain = getdetailTrainByLecturer(lecCode);
            writeDetailTrainListAfterUpdate(out, listDetailTrain);
        } catch (Exception ex) {
            Logger.getLogger(DetailTrainManager.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
    private void preUpdateTrainDetailByLecturer(HttpServletRequest request, HttpServletResponse response) throws IOException{
        try {
            HttpSession session = request.getSession();
            SubjectDAO subjectDao= DAOFactory.getSubjectDao();
            String lecturerCode = request.getParameter("lecturerCode");
            List<DetailTrain> detainTrainList = getdetailTrainByLecturer(lecturerCode);
            Lecturer lecturer= lecturerDao.findById(lecturerCode);
            List<Subject> subjectList = subjectDao.findByColumName("MaKhoa", lecturer.getFacultyCode());
            session.setAttribute("lecturer", lecturer);
            session.setAttribute("detainTrainList", detainTrainList);
            session.setAttribute("subjectList", subjectList);
        } catch (Exception ex) {
            Logger.getLogger(DetailTrainManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        String path = "./jsps/PDT/UpdateDetailTrain.jsp";
        response.sendRedirect(path);
    }
    private void writeLecturerReportDetail(String lecturerCode, List<DetailTrain> datas,
                                                        PrintWriter out) {
        try {
            String lecturerName = DAOFactory.getLecturerDao().findById(lecturerCode).getFullName();
           out.println("Danh sách các môn học học <b>" + lecturerName + "</b> đang phụ trách:");
           out.println("<table id = \"student-report\" name = \"student-report\" class=\"general-table\" style=\"left: right; width: 450; margin-left: 32px;\">");
           out.println("<tr><th>STT</th><th>Mã môn học</th><th>Tên môn học</th></tr>");
           for (int i = 0; i < datas.size(); i++) {
               out.println("<tr>");
               out.println("<td> " + (i + 1) + " </td>");
               out.println("<td>"+datas.get(i).getId().getSubjectCode() +"</td>");
               out.println("<td>"+datas.get(i).getSubjectName() +"</td>");
               out.println("</tr>");
           }
           out.println("</table>");
           out.println("<div class=\"button-1\">"
                    + "<span class=\"atag\" onclick=\"updateDetailTrain()\" ><img src=\"../../imgs/check.png\"/>Cập nhật</span>"
                    + "</div>");
           out.println("<form id=\"form-update\" name=\"form-update\" action=\"../../DetailTrainManager?action=pre-update-train-prog\" method=\"post\">"
                    + "<input type=\"hidden\" name=\"lecturerCode\" id=\"lecturerCode\" value=\"" + lecturerCode + "\" />"
                        /*+ "<div class=\"button-1\">"
                        + "<input type= \"submit\" ><img src=\"../../imgs/check.png\"/>Cập nhật</input>"
                        + "</div>"*/
                + "</form>");
        } catch (Exception ex) {
            Logger.getLogger(DetailTrainManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void getDetailTrainByLecturer(String lecturerCode, PrintWriter out){
        List<DetailTrain> detainTrainList = getdetailTrainByLecturer(lecturerCode);
        writeLecturerReportDetail(lecturerCode, detainTrainList, out);
    }
    private List<DetailTrain> getdetailTrainByLecturer(String lecturerCode){
        return reportService.getDetainTrainByLecturer(lecturerCode);
    }
    private void searchLecturer(HttpServletRequest request, HttpServletResponse response){
        PrintWriter out = null;
        try {
            out = response.getWriter();
            String value = request.getParameter("value");
            List<Lecturer> lecturerList= searchLecturer(value);
            if(lecturerList!= null && !lecturerList.isEmpty()){
                writeRespond(lecturerList, out);
            }else{
                out.println(Message.LECTURER_SEARCH_NOTFOUND);
            }
        } catch (Exception ex) {
            Logger.getLogger(DetailTrainManager.class.getName()).log(Level.SEVERE, null, ex);
            out.println(ex);
        } finally {
            out.close();
        }
        
    }
    private void writeRespond(List<Lecturer> datas, PrintWriter out) throws Exception {
        out.println("<tr>"
                + "<th> STT </th>"
                + "<th>Mã GV</th>"
                + "<th> Họ và tên </th>"
                + "<th>Mã khoa</th>"
                + "</tr>");
        for (int i = 0; i < datas.size(); i++) {
            out.println("<tr>");
            out.println("<td> " + (i + 1) + " </td>");
            out.println("<td> " + datas.get(i).getId() + " </td>");
            String method = String.format(" onclick=getDetailLecturerReport('%s')>",
                                                        datas.get(i).getId());
            out.println("<td> <span class=\"atag\"" + method
                    + datas.get(i).getFullName()
                    + "</span> </td>");
            out.println("<td> " + datas.get(i).getFacultyCode() + " </td>");
            out.println("</tr>");// <a hreft > abc </a>
        }
        out.println("</table>");
    }
    private List<Lecturer> searchLecturer(String key) {
        List<Lecturer> results = reportService.searchLecturer(key);
        return results;
    }
    public enum ReportFunctionSupported {
        DEFAULT("default"), // List first page of class opened.
        SEARCH_LECTURER("search_lecturer"),
        LECTURER_REPORT("lecturer-report"),
        PRE_UPDATE("pre-update-train-prog"),
        UPDATE("update"),
        ADD_DETAIL("AddDetailTrain"),
        REMOVE_DETAIL("RemoveDetailTrain"),
        CLASS_REPORT("class-report"),
        CLASS_DETAIL("class-detail");
        
        private String description;
        ReportFunctionSupported(String description) {
            this.description = description;
        }
        
        public String getValue() {
            return description;
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
}
