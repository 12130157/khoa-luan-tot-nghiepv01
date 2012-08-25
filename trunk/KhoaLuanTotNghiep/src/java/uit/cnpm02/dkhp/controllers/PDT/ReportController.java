package uit.cnpm02.dkhp.controllers.PDT;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.LecturerDAO;
import uit.cnpm02.dkhp.DAO.RegistrationDAO;
import uit.cnpm02.dkhp.DAO.SubjectDAO;
import uit.cnpm02.dkhp.DAO.TrainClassDAO;
import uit.cnpm02.dkhp.model.Faculty;
import uit.cnpm02.dkhp.model.Registration;
import uit.cnpm02.dkhp.model.ReportBySemester;
import uit.cnpm02.dkhp.model.Rule;
import uit.cnpm02.dkhp.model.Student;
import uit.cnpm02.dkhp.model.Subject;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.service.IReporter;
import uit.cnpm02.dkhp.service.IStudentService;
import uit.cnpm02.dkhp.service.TrainClassStatus;
import uit.cnpm02.dkhp.service.impl.ReporterImpl;
import uit.cnpm02.dkhp.service.impl.StudentServiceImpl;
import uit.cnpm02.dkhp.utilities.BOUtils;
import uit.cnpm02.dkhp.utilities.Constants;
import uit.cnpm02.dkhp.utilities.Message;
import uit.cnpm02.dkhp.utilities.filedownload.FileDownloadUtility;

/**
 *
 * @author LocNguyen
 */
@WebServlet(name = "ReportController", urlPatterns = {"/ReportController"})
public class ReportController extends HttpServlet {
    
    private IReporter reportService = new ReporterImpl();
    private IStudentService studentService = new StudentServiceImpl();
    
    private String sortType = "ASC";
    /**Store curren process student**/
    private String mssv = "";

    /** 
     * Processes requests for both HTTP <code>GET</code> and
     * <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request,
                                        HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String requestAction;
        try {
            requestAction = (String) request.getParameter("action");
        } catch (Exception ex) {
             requestAction = ReportFunctionSupported.DEFAULT.getValue();
        }
        try {
            if (requestAction.equals(ReportFunctionSupported.
                                                DEFAULT.getValue())) {
                getDefaultReport(request, response);
            } else if (requestAction.equals(ReportFunctionSupported.
                                                SEARCH_STUDENT.getValue())) {
                // List existed TrainClass, Pagging setup
                /**The key should be student's name or student's id**/
                String key = request.getParameter("value");
                if (key != null) {
                    List<Student> students = searchStudent(key);
                    if ((students != null) && !students.isEmpty()) {
                        writeRespond(students, out);
                    } else {
                        out.println(Message.STUDENT_SEARCH_NOTFOUND);
                    }
                } else {
                    out.println(Message.STUDENT_SEARCH_NOTFOUND);
                }
                return;
            } else if (requestAction.equals(ReportFunctionSupported.
                                                STUDENT_REPORT_SORT.getValue())) {
                // List existed TrainClass, Pagging setup
                String by = request.getParameter("by");
                String type = request.getParameter("type");
                if (type != null) {
                    if (!type.equalsIgnoreCase("ASC")
                            && !type.equalsIgnoreCase("DES")) {
                        type = "ASC";
                    }
                    List<TrainClass> trainClassReg = reportService.sort(session.getId(), by, type);
                    if ((trainClassReg != null) && !trainClassReg.isEmpty()) {
                        writeStudentReportDetail(mssv, trainClassReg, out);
                    } else {
                        out.println(Message.STUDENT_REPORT_NO_REPORT);
                    }
                    
                    if (sortType.equalsIgnoreCase("ASC"))
                        sortType = "DES";
                    else
                        sortType = "ASC";
                    return;
                } else {
                    out.println(Message.STUDENT_SEARCH_NOTFOUND);
                }
                return;
            } else if (requestAction.equals(ReportFunctionSupported.
                                                STUDENT_REPORT.getValue())) {
                mssv = request.getParameter("value");
                List<TrainClass> trainClassReg = getStudentReport(mssv, session.getId());
                if ((trainClassReg != null) && !trainClassReg.isEmpty()) {
                    writeStudentReportDetail(mssv, trainClassReg, out);
                } else {
                    out.println(Message.STUDENT_REPORT_NO_REPORT);
                }
                return;
            } else if (requestAction.equals(ReportFunctionSupported.
                                                CLASS_REPORT.getValue())) {
                String year = request.getParameter("year");
                String s = request.getParameter("semeter");
                int semeter = -1;
                if ((s != null) || !s.isEmpty()) {
                    if (s.equals("1"))
                        semeter = 1;
                    else if (s.equals("2"))
                        semeter = 2;
                }
                if ((year == null)
                        || year.isEmpty()
                        || year.equalsIgnoreCase("All")
                        || year.equals("*")) {
                    
                    year = "";
                }
                
                List<TrainClass> trainClassReg = getTrainClassByYearAndSemeter(
                                                    year, semeter);
                
                if ((trainClassReg == null) || trainClassReg.isEmpty()) {
                    out.println(Message.CLASS_REPORT_NO_REPORT);
                } else {
                    writeTrainClassReport(trainClassReg, out);
                }
                return;
            } else if (requestAction.equals(ReportFunctionSupported.
                                                CLASS_DETAIL.getValue())) {
                String classId = request.getParameter("classid");
                updateClassDetailReport(classId, session);
                String path = "./jsps/PDT/TrainClassReport.jsp";
                response.sendRedirect(path);
                return;
            } else if (requestAction.equals("trainclass-report")) {
                fillTrainClassReport(request, response);
                return;
            } else if (requestAction.equals("doGetReportPassFailData")) {
                loadPassFailData(request, response);
                //loadVirtualPassFailData(request, response);
            }
        } catch(Exception ex) {
            Logger.getLogger(ReportController.class.getName()).
                                                log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }
    }
    private void getDefaultReport(HttpServletRequest request, HttpServletResponse response){
        try {
            List<TrainClass> classList = DAOFactory.getTrainClassDAO().findAll();
            List<String> yearList = getYear(classList);
            ReportBySemester report = getReportBySemesterAndYear(1, yearList.get(0));
            HttpSession session = request.getSession();
            String path = "./jsps/PDT/Report.jsp";
            session.setAttribute("yearList", yearList);
            session.setAttribute("report", report);
            response.sendRedirect(path);
            return;
        } catch (Exception ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private List<String> getYear(List<TrainClass> trainClassList) {
        ArrayList<String> yearList = new ArrayList<String>();
        for (int i = 0; i < trainClassList.size(); i++) {
            if (checkStringExist(trainClassList.get(i).getId().getYear(), yearList) == false) {
                yearList.add(trainClassList.get(i).getId().getYear());
            }
        }

        return yearList;
    }
     private boolean checkStringExist(String value, List<String> list) {
        boolean result = false;
        for (int i = 0; i < list.size(); i++) {
            if (value.equalsIgnoreCase(list.get(i))) {
                result = true;
            }
        }
        return result;
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

    private List<Faculty> getAllFaculty() {
        try {
            return DAOFactory.getFacultyDao().findAll();
        } catch (Exception ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<Faculty>(1);
    }
    
    private List<Student> searchStudent(String key) {
        List<Student> results = reportService.searchStudent(key);
        return results;
    }
    
    /**
     * Data respone throught ajax when call search student from page
     * @param datas list student found
     * @param out output stream
     */
    private void writeRespond(List<Student> datas, PrintWriter out) {
        out.println("<table class=\"general-table\" style=\"width: 350px;\">");
        out.println("<tr>"
                + "<th> STT </th>"
                + "<th width = 200px> Họ và tên </th>"
                + "</tr>");
        for (int i = 0; i < datas.size(); i++) {
            out.println("<tr>");
            out.println("<td> " + (i + 1) + " </td>");
            String method = String.format(" onclick=getDetailStudentReport('%s')>",
                                                        datas.get(i).getId());
            out.println("<td> <span class=\"atag\"" + method
                    + datas.get(i).getFullName()
                    + "</span> </td>");
            out.println("</tr>");// <a hreft > abc </a>
        }
        out.println("</table>");
    }

    private List<TrainClass> getStudentReport(String mssv, String sessionId) {
        return reportService.getTrainClassRegistered(sessionId, mssv, true);
    }
    
    /**
     * Write out list trainclass student registered.
     * @param mssv student id
     * @param datas
     * @param out 
     */
    private void writeStudentReportDetail(String mssv, List<TrainClass> datas,
                                                        PrintWriter out) {
        String studentName = studentService.getStudent(mssv).getFullName();
        out.println("Danh sách các lớp học <b>" + studentName + "</b> đã đăng ký:");
        out.println("<table class=\"general-table\" style=\"width: 550px;\">");

        out.println("<tr>"
                + "<th> STT </th>"
                + "<th> <span class=\"atag\""
                    + " onclick=\"sortTrainClass('MaLopHoc', '" + sortType + "')\">"
                    + " Mã lớp </a></th>"
                + "<th> <a href='#'"
                    + " onclick=\"sortTrainClass('MonHoc', '" + sortType + "')\">"
                    + " Môn học </a></th>"
                + "<th> <a href='#'"
                    + " onclick=\"sortTrainClass('NamHoc', '" + sortType + "')\">"
                    + " Năm học </a></th>"
                + "<th> <a href = '#'"
                    + " onclick=\"sortTrainClass('HocKy', '" + sortType + "')\">"
                    + "Học kỳ </a></th>"
                + "</tr>");
        for (int i = 0; i < datas.size(); i++) {
            out.println("<tr>");
            out.println("<td> " + (i + 1) + " </td>");
            String classId = datas.get(i).getId().getClassCode();
            out.println("<td> <a href=\"../../ReportController?action=class-detail&classid=" 
                    + classId + "\">" 
                    + classId
                    + "</a></td>");
            out.println("<td> " + datas.get(i).getSubjectName() + " </td>");
            out.println("<td> " + datas.get(i).getId().getYear() + " </td>");
            out.println("<td> " + datas.get(i).getId().getSemester() + " </td>");
            out.println("</tr>");// <a hreft > abc </a>
        }
        out.println("</table>");
        out.println("<a href=\"../../DownloadController?action=download-student-trainclass-report&mssv="
                    + mssv + "\" class=\"atag\" style=\"margin-left: 80%;\">"
                + "<img src=\"../../imgs/download.png\" title=\"download\"/>Download</a>");
    }

    private List<TrainClass> getTrainClassByYearAndSemeter(
            String year, int semeter) {
        return reportService.getTrainClass(year, semeter, TrainClassStatus.ALL);
    }

    private void writeTrainClassReport(List<TrainClass> trainClassReg,
                                                        PrintWriter out) {
        out.println("<table>");
        /**
            Tổng số lớp đã tạo: 	120
            Tổng số lớp hủy do không đủ điều kiện: 	100
            Tổng số lớp chưa kết thúc: 	20
         */
        int total = trainClassReg.size();
        int classClosed = 0;
        int classCanceled = 0;
        int classInProcess = 0;
        for (TrainClass t : trainClassReg) {
            
            if (t.getStatus() == TrainClassStatus.OPEN) {
                classInProcess ++;
            } else if (t.getStatus() == TrainClassStatus.CLOSE) {
                classClosed ++;
            } 
        }
        
        String[][] map = new String[][] {
            {Message.REPORT_TRAINCLASS_TOTAL, Integer.toString(total)},
            {Message.REPORT_TRAINCLASS_CLOSED, Integer.toString(classClosed)},
            {Message.REPORT_TRAINCLASS_CANCELED, Integer.toString(classCanceled)},
            {Message.REPORT_TRAINCLASS_IN_PROGRESS, Integer.toString(classInProcess)},
        };     
        
        for (int i = 0; i < map.length; i++) {
            out.println("<tr>");
            out.println("<td> " + map[i][0] + " </td>");
            out.println("<td> " + map[i][1] + " </td>");
            out.println("</tr>");
        }
        
        out.println("</table>");
        out.println("<a href='#'>Tải file excel</a>");
    }
    
    private void downloadStudentReport(HttpServletResponse resp, String sessionId) {
        List<TrainClass> trainClasses = reportService
                                  .getTrainClassRegistered(sessionId, mssv, true);
        String fileName = 
                FileDownloadUtility.exportStudentReportFile(mssv, trainClasses);
        if (!fileName.isEmpty()) {
            try {
                FileDownloadUtility.downloadFile(fileName, resp);
            } catch (IOException ex) {
                Logger.getLogger(ReportController.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
        
    }

    /**
     * User, after search some student, the click handler for 
     * link at each student include detail class
     * but, we need detail for each class, so this function
     * will update nedded data.
     * @param classId
     * @param session 
     */
    private void updateClassDetailReport(String classId, HttpSession session) {
        TrainClass clazz = reportService.getTrainClass(classId);
        String clazzId = clazz.getId().getClassCode();
        String year = Constants.CURRENT_YEAR;
        int semeter = Constants.CURRENT_SEMESTER;
        
        List<Student> students = studentService
                .getStudent(clazzId, year, semeter);
        
        session.setAttribute("trainclass", clazz);
        session.setAttribute("students", students);
    }
    private ReportBySemester getReportBySemesterAndYear(int semester, String year){
        try {
                List<Rule> rule = DAOFactory.getRuleDao().findAll();
                 float markPass = rule.get(0).getValue();
                 int numOfPassReg=0;
                 int numOfNotPassReg=0;
                 int numOfReg=0;
                 TrainClassDAO classDao = DAOFactory.getTrainClassDAO();
                 List<TrainClass> listClass = classDao.findAllBySemesterAndYear(semester, year);
                 if(listClass!= null && !listClass.isEmpty()){
                 RegistrationDAO regDao = DAOFactory.getRegistrationDAO();
                 String[] columnName = new String[]{"HocKy", "NamHoc"};
                 Object[] values = new Object[]{semester, year}; 
                 List<Registration> regList = regDao.findByColumNames(columnName, values);
                 TrainClass maxRegClass = listClass.get(0);
                 TrainClass minRegClass = listClass.get(0);
                 for(int i =1; i< listClass.size(); i++){
                     if(listClass.get(i).getNumOfStudentReg()>maxRegClass.getNumOfStudentReg()){
                         maxRegClass=listClass.get(i);
                     }
                     if(listClass.get(i).getNumOfStudentReg()< maxRegClass.getNumOfStudentReg()){
                         minRegClass=listClass.get(i);
                     }
                 }
                 if(regList!= null && !regList.isEmpty()){
                     for(int j =0; j<regList.size(); j++){
                         if(regList.get(j).getMark()>= markPass){
                             numOfPassReg++;
                         }else{
                             numOfNotPassReg++;
                         }
                     }
                     numOfReg= regList.size();
                 }
                 ReportBySemester report=new ReportBySemester(listClass.size(), numOfReg, maxRegClass, minRegClass, numOfPassReg, numOfNotPassReg);
                 return report;
                 }else{
                     return null;
                 }
        } catch (Exception ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    private void fillTrainClassReport(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
            PrintWriter out = response.getWriter(); 
            String yearStr = request.getParameter("year");
            String semeterStr = request.getParameter("semeter");
            int semester = Integer.parseInt(semeterStr);
            ReportBySemester report=getReportBySemesterAndYear(semester, yearStr);
            writeOutTrainClassReport(out, report);
            
   }
    
    private void writeOutTrainClassReport(PrintWriter out, ReportBySemester report) {
        if(report!=null){
            DecimalFormat format = new DecimalFormat("#.##");
            float ever=  (float)report.getNumOfReg()/report.getNumOfClassOpen();
            String everstr= format.format(ever);
            float percentPass = (float)report.getNumOfPassReg()/report.getNumOfReg() *100;
            String percentPassSrt= format.format(percentPass);
            float percentNotPass = (float)report.getNumOfNotPassReg()/report.getNumOfReg() *100;
            String percentNotPassSrt= format.format(percentNotPass);
            out.println("<tr>");
            out.println("<td>Số lớp đã mở: </td>");
            out.println("<td>"+report.getNumOfClassOpen()+"</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td>Số đăng ký: </td>");
            out.println("<td>"+report.getNumOfReg()+"</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td>Trung bình: </td>");
            out.println("<td>"+everstr+" (ĐK/Lớp)</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td>Lớp có đăng ký nhiều nhất</td>");
            out.println("<td>"+report.getMaxRegClass().getId().getClassCode()+"  ("+report.getMaxRegClass().getNumOfStudentReg()+" ĐK)</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td>Lớp có đăng ký ít nhất</td>");
            out.println("<td>"+report.getMinRegClass().getId().getClassCode()+"  ("+report.getMinRegClass().getNumOfStudentReg()+" ĐK)</td>");           
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td>Số sinh viên đạt</td>");
            out.println("<td>"+report.getNumOfPassReg()+"  ("+percentPassSrt+"%)</td>");            
            out.println("</tr>");
            out.println("<td>Số sinh viên không đạt</td>");
            out.println("<td>"+report.getNumOfNotPassReg()+"  ("+percentNotPassSrt+"%)</td>");            
            out.println("</tr>");
        }else{
            out.println("<p>Chưa có thông tin</p>");
        }
        
    }

    private void loadPassFailData(HttpServletRequest request,
            HttpServletResponse response) {
        JSONArray jsons = new JSONArray();
        String startYear = (String) request.getParameter("startYear");
        String endYear = (String) request.getParameter("endYear");
        String time = (String) request.getParameter("time");
        int course = Integer.parseInt(time);
        int start = Integer.parseInt(startYear);
        int end = Integer.parseInt(endYear);
        if (end < start) {
            return;
        }
        
        TrainClassDAO tcDao = DAOFactory.getTrainClassDAO();
        SubjectDAO subDao = DAOFactory.getSubjectDao();
        //JSONObject[] json = new JSONObject[5];
        int CNPM = 0, KTMT = 1, MMT = 2, HTTT = 3, KHMT = 4;
        /*for (int i = 0; i < 5; i++) {
            json[i] = new JSONObject();
        }*/
        for (int i = 0; i <= (end-start); i++) { // for each year
            String year = (start+i)+"-"+(start+i+1);
            List<TrainClass> trainClass = null;
            
            try {
                if (course == 0) { // all semeter
                    trainClass = tcDao.findByColumName("NamHoc", year);
                } else {
                    trainClass = tcDao.findAllBySemesterAndYear(course, year);
                }
                if ((trainClass != null) && !trainClass.isEmpty()) {
                    JSONObject json = new JSONObject();
                    json.put("year", year);
                    int pass[] = new int[5];
                    int fail[] = new int[5];
                    for (TrainClass tc : trainClass) { // for each subject of year
                        String subjectId = tc.getSubjectCode();
                        Subject sub = subDao.findById(subjectId);
                        String facultyId = sub.getFacultyCode();
                        
                        int currentPass = tcDao.getNumberOfRegByClassAndType(
                                    tc.getId().getClassCode(), tc.getId().getSemester(), year, Constants.PASS);
                        int currentFail = tcDao.getNumberOfRegByClassAndType(
                                    tc.getId().getClassCode(), tc.getId().getSemester(), year, Constants.FAIL);
                        if (facultyId.equals("CNPM")) {
                            pass[CNPM] += currentPass;
                            fail[CNPM] += currentFail;
                        } else if (facultyId.equals("KHMT")) {
                            pass[KHMT] += currentPass;
                            fail[KHMT] += currentFail;
                        } else if (facultyId.equals("MMT")) {
                            pass[MMT] += currentPass;
                            fail[MMT] += currentFail;
                        } else if (facultyId.equals("KTMT")) {
                            pass[KTMT] += currentPass;
                            fail[KTMT] += currentFail;
                        } else if (facultyId.equals("HTTT")) {
                            pass[HTTT] += currentPass;
                            fail[HTTT] += currentFail;
                        }
                    }
                    int cnpm_pass = (pass[CNPM] + fail[CNPM]) > 0 ? (100*pass[CNPM]) / (pass[CNPM] + fail[CNPM]) : 0;
                    int mmt_pass = (pass[MMT] + fail[MMT]) > 0 ? (100*pass[MMT]) / (pass[MMT] + fail[MMT]) : 0;
                    int ktmt_pass = (pass[KTMT] + fail[KTMT]) > 0 ? (100*pass[KTMT]) / (pass[KTMT] + fail[KTMT]) : 0;
                    int httt_pass = (pass[HTTT] + fail[HTTT]) > 0 ? (100*pass[HTTT]) / (pass[HTTT] + fail[HTTT]) : 0;
                    int khmt_pass = (pass[KHMT] + fail[KHMT]) > 0 ? (100*pass[KHMT]) / (pass[KHMT] + fail[KHMT]) : 0;
                    json.put("cnpm_pass", cnpm_pass);
                    json.put("mmt_pass", mmt_pass);
                    json.put("ktmt_pass", ktmt_pass);
                    json.put("httt_pass", httt_pass);
                    json.put("khmt_pass", khmt_pass);
                    
                    jsons.add(json);
                }
            } catch (Exception ex) {
                Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        BOUtils.writeResponse(jsons.toString(), request, response);
    }
    
    private void loadVirtualPassFailData(HttpServletRequest request,
            HttpServletResponse response) {
        JSONArray jsons = new JSONArray();
        String startYear = (String) request.getParameter("startYear");
        String endYear = (String) request.getParameter("endYear");
        String time = (String) request.getParameter("time");
        int course = Integer.parseInt(time);
        int start = Integer.parseInt(startYear);
        int end = Integer.parseInt(endYear);
        if (end < start) {
            return;
        }
        
        int CNPM = 0, KTMT = 1, MMT = 2, HTTT = 3, KHMT = 4;
        Random r = new Random();
        for (int i = 0; i <= (end-start); i++) { // for each year
            String year = (start+i)+"-"+(start+i+1);
            JSONObject json = new JSONObject();
            json.put("year", year);
            int pass[] = new int[5];
            int fail[] = new int[5];
            
            pass[CNPM] += r.nextInt(25);
            fail[CNPM] += r.nextInt(25);

            pass[KHMT] += r.nextInt(25);
            fail[KHMT] += r.nextInt(25);

            pass[MMT] += r.nextInt(25);
            fail[MMT] += r.nextInt(25);

            pass[KTMT] += r.nextInt(25);
            fail[KTMT] += r.nextInt(25);

            pass[HTTT] += r.nextInt(25);
            fail[HTTT] += r.nextInt(25);
                        
                    
            int cnpm_pass = (pass[CNPM] + fail[CNPM]) > 0 ? (100*pass[CNPM]) / (pass[CNPM] + fail[CNPM]) : 0;
            int mmt_pass = (pass[MMT] + fail[MMT]) > 0 ? (100*pass[MMT]) / (pass[MMT] + fail[MMT]) : 0;
            int ktmt_pass = (pass[KTMT] + fail[KTMT]) > 0 ? (100*pass[KTMT]) / (pass[KTMT] + fail[KTMT]) : 0;
            int httt_pass = (pass[HTTT] + fail[HTTT]) > 0 ? (100*pass[HTTT]) / (pass[HTTT] + fail[HTTT]) : 0;
            int khmt_pass = (pass[KHMT] + fail[KHMT]) > 0 ? (100*pass[KHMT]) / (pass[KHMT] + fail[KHMT]) : 0;
            json.put("cnpm_pass", cnpm_pass);
            json.put("mmt_pass", mmt_pass);
            json.put("ktmt_pass", ktmt_pass);
            json.put("httt_pass", httt_pass);
            json.put("khmt_pass", khmt_pass);

            jsons.add(json);
            
        }
        
        BOUtils.writeResponse(jsons.toString(), request, response);
    }
    
     /**
     * An enum define all supported function of serverlet
     * .
     */
    public enum ReportFunctionSupported {
        DEFAULT("default"), // List first page of class opened.
        SEARCH_STUDENT("search_student"),
        STUDENT_REPORT("student-report"),
        STUDENT_REPORT_SORT("sort-student-report"),
        CLASS_REPORT("class-report"),
        CLASS_DETAIL("class-detail"),
        DOWNLOAD_STUDENT_REPORT("download-student-report");
        
        private String description;
        ReportFunctionSupported(String description) {
            this.description = description;
        }
        
        public String getValue() {
            return description;
        }
        
    }
}
