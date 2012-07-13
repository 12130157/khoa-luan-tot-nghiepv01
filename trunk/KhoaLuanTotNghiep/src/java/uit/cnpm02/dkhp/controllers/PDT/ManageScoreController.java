package uit.cnpm02.dkhp.controllers.PDT;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.LecturerDAO;
import uit.cnpm02.dkhp.DAO.RegistrationDAO;
import uit.cnpm02.dkhp.DAO.StudentDAO;
import uit.cnpm02.dkhp.DAO.StudyResultDAO;
import uit.cnpm02.dkhp.DAO.SubjectDAO;
import uit.cnpm02.dkhp.DAO.TaskDAO;
import uit.cnpm02.dkhp.DAO.TrainClassDAO;
import uit.cnpm02.dkhp.model.FileInfo;
import uit.cnpm02.dkhp.model.Lecturer;
import uit.cnpm02.dkhp.model.Registration;
import uit.cnpm02.dkhp.model.Student;
import uit.cnpm02.dkhp.model.StudyResult;
import uit.cnpm02.dkhp.model.StudyResultID;
import uit.cnpm02.dkhp.model.Subject;
import uit.cnpm02.dkhp.model.Task;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.model.TrainClassID;
import uit.cnpm02.dkhp.model.type.TaskStatus;
import uit.cnpm02.dkhp.model.type.TaskType;
import uit.cnpm02.dkhp.service.IFileUploadService;
import uit.cnpm02.dkhp.service.TrainClassStatus;
import uit.cnpm02.dkhp.service.impl.FileUploadServiceImpl;
import uit.cnpm02.dkhp.service.impl.ScoreProcessUtil;
import uit.cnpm02.dkhp.utilities.Constants;
import uit.cnpm02.dkhp.utilities.ExecuteResult;
import uit.cnpm02.dkhp.utilities.MultipartMap;
import uit.cnpm02.dkhp.utilities.StringUtils;

/**
 *
 * @author LocNguyen
 */
@WebServlet(name = "ManageScoreController", urlPatterns = {"/ManageScoreController"})
public class ManageScoreController extends HttpServlet {

    private IFileUploadService fuService = new FileUploadServiceImpl();
    private ScoreProcessUtil scoreUtil = new ScoreProcessUtil();
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

        String path = "./jsps/PDT/ManageScore.jsp";
        try {
            String action = (String) request.getParameter("function");

            if (action.equals("manage")) {
                session.setAttribute("list-miss-core-class", getMissingScoreClass());
                response.sendRedirect(path);
            } else if (action.equals("get-files")) {
                doGetFileScoresFromLecturer(request, response);
            } else if (action.equals("import-score")) {
                /** Main processing... **/
                doImportSelectedFile(request, response);
            } else if (action.equals("get-trainclass-detail")) {
                doGetTrainClassDetail(request, response);
                //
            } else if (action.equals("load-lecturer-list")) {
                doLoadLecturerWithOpenTrainClass(request, response);
            } else if (action.equals("lecturer-change")) {
                updateListTrainClassOfLecturer(request, response);
            }  else if (action.equals("submit-resend-score")) {
                submitResendScore(request, response);
            } else if (action.equals("load-trainclass")) {
                loadOpenTrainClass(request, response);
            } else if (action.equals("initial-manul-input-form")) {
                initialManualInputForm(request, response);
            } else if (action.equals("import-manual")) {
                importScoreFromForm(request, response);
            } else if (action.equals("import-score-from-file")) {//
                ExecuteResult result = new ExecuteResult(false, null);
                result = importScoreFromFile(request, response);
                session.removeAttribute("error");
                if (result.getMessage() != null) {
                    session.setAttribute("error", result.getMessage());
                }
                response.sendRedirect(path);
            } else if (action.equals("search")) {
                //Ajax function
                String searchValue = request.getParameter("search_value");
                List<Student> listStudent = searchStudent(searchValue);
                writeListStringToClient(listStudent, out);
                return;
            }
        } catch (Exception ex) {
            Logger.getLogger(ManageScoreController.class.getName())
                    .log(Level.SEVERE, "Error occur inside ManageScoreController", ex);
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

    private List<String> getListRegisteredClass() {
        List<String> result = new ArrayList<String>(10);
        TrainClassDAO classDao = DAOFactory.getTrainClassDAO();
        SubjectDAO subDao = DAOFactory.getSubjectDao();

        try {
            List<TrainClass> clazz = classDao.findAll();
            if ((clazz != null) && !clazz.isEmpty()) {
                for (TrainClass s : clazz) {
                    String temp = s.getId().getClassCode()
                            + " - " + s.getSubjectCode()
                            + " - " + subDao.findById(s.getSubjectCode()).getSubjectName();
                    result.add(temp);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ManageScoreController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    private ExecuteResult importScoreFromFile(HttpServletRequest request, HttpServletResponse response) {
        List<StudyResult> scores = new ArrayList<StudyResult>();
        ExecuteResult result = new ExecuteResult(true, null);
        MultipartMap map = null;
        String clazz = "";
        String courseStr = "";
        String year = "";

        try {
            map = new MultipartMap(request, this);
            clazz = map.getParameter("select_class");
            courseStr = map.getParameter("select_course");
            year = map.getParameter("select_year");
        } catch (ServletException ex) {
            Logger.getLogger(ManageScoreController.class.getName()).log(Level.SEVERE, null, ex);
            result.setIsSucces(false);
            result.setMessage("Lỗi upload file.");
        } catch (IOException ex) {
            Logger.getLogger(ManageScoreController.class.getName()).log(Level.SEVERE, null, ex);
            result.setIsSucces(false);
            result.setMessage("Lỗi upload file.");
        }

        if (!result.isIsSucces()) {
            return result;
        }

        File file = (File) map.get("txtPath");
        clazz = clazz.split(" - ")[1];
        //TrainClass tc = DAOFactory.getTrainClassDAO().find


        int course = Integer.parseInt(courseStr.substring(courseStr.length() - 1));
        try {
            InputStream is = new FileInputStream(file);
            HSSFWorkbook wb = new HSSFWorkbook(is); // FileUtils.getWorkbook(request, response);

            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow rowTemp;
            HSSFCell cellTemp;

            int cellType;
            Iterator rowIter = sheet.rowIterator();
            while (rowIter.hasNext()) {
                rowTemp = (HSSFRow) rowIter.next();
                if (rowTemp == null) {
                    continue;
                }
                cellTemp = rowTemp.getCell(0);
                if (cellTemp == null) {
                    continue;
                }

                cellTemp = rowTemp.getCell(0);
                cellType = cellTemp.getCellType();
                //check the first cell of data must be a number
                if (cellType != HSSFCell.CELL_TYPE_NUMERIC) {
                    continue;
                } else {
                    //HSSFRow rowTemp, int course, String year, String subjectId) {
                    StudyResult s = initStudyResultFromHSSFRow(rowTemp, course, year, clazz);
                    scores.add(s);
                }
            }

            //
            // Check if the student already registered
            //

            //TODO: Check if the student already registered


            // Check if the student already get mark for subject
            // Check if the current one greater then the older
            // => just update.

            StudyResultDAO srDao = DAOFactory.getStudyResultDao();
            if ((scores != null) && !scores.isEmpty()) {
                List<StudyResult> removeItem = new ArrayList<StudyResult>(10);
                List<StudyResult> updateItem = new ArrayList<StudyResult>(10);
                for (StudyResult sr : scores) {
                    StudyResult existed = srDao.findById(sr.getId());

                    if (existed != null) {
                        if (existed.getMark() < sr.getMark()) {
                            updateItem.add(sr);
                        }

                        removeItem.add(sr);
                    }
                }

                if (!updateItem.isEmpty()) {
                    srDao.update(updateItem);
                }

                if (!removeItem.isEmpty()) {
                    scores.removeAll(removeItem);
                }
            }

            if ((scores != null) && !scores.isEmpty()) {
                DAOFactory.getStudyResultDao().addAll(scores);
            }

            result.setMessage("Update thành công.");
        } catch (Exception ex) {
            Logger.getLogger(ManageScoreController.class.getName()).log(Level.SEVERE, "Error while try to reading from file.", ex);

            result.setIsSucces(false);
            result.setMessage("Lỗi cập nhật điểm: " + ex.toString());
        }

        return result;
    }

    private StudyResult initStudyResultFromHSSFRow(HSSFRow rowTemp, int course, String year, String subjectId) {
        StudyResult result = new StudyResult(null, subjectId, year, course, 0);
        HSSFCell cellTemp = null;

        cellTemp = rowTemp.getCell(1);
        String mssv = cellTemp.getStringCellValue();

        StudyResultID id = new StudyResultID(mssv, subjectId);
        result.setId(id);

        cellTemp = rowTemp.getCell(3);
        //cellTemp.setCellType(HSSFCell.CELL_TYPE_STRING);
        //String fullName = cellTemp.getStringCellValue();
        float score = (float) cellTemp.getNumericCellValue();
        result.setMark(score);

        return result;
    }

    private List<Student> searchStudent(String searchValue) {
        StudentDAO studentDao = DAOFactory.getStudentDao();
        List<Student> result = new ArrayList<Student>(10);
        try {
            //Try find by ID (MSSV)
            result = studentDao.findByColumName("MSSV", searchValue);
            // Try find by name (HoTen)
            List<Student> temp = studentDao.findByColumName("HoTen", searchValue);
            if ((temp != null) && (!temp.isEmpty())) {
                for (Student s : temp) {
                    if (!result.contains(s)) {
                        result.add(s);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ManageScoreController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    private void writeListStringToClient(List<Student> listStudent, PrintWriter out) {
        String respStr = "<tr id=\"tableliststudent-th\">"
                + "<td></td>"
                + "<td> STT </td>"
                + "<td> MSSV </td>"
                + "<td> Họ Tên </td>"
                + "<td> Lớp </td>"
                + "<td> Khoa </td>"
                + "</tr>";
        out.println(respStr);
        for (int i = 0; i < listStudent.size(); i++) {
            respStr = "<tr>"
                    + "<td><INPUT type=\"radio\" name=\"radio1\"" + ((i == 0) ? " checked" : "") + "/></td>"
                    + "<td> " + (i + 1) + " </td>"
                    + "<td> " + listStudent.get(i).getId() + "</td> "
                    + "<td> " + listStudent.get(i).getFullName() + "</td>"
                    + "<td> " + listStudent.get(i).getClassCode() + "</td>"
                    + "<td> " + listStudent.get(i).getFacultyCode() + "</td>"
                    + "</tr>";
            out.println(respStr);
        }
    }

    private void doGetFileScoresFromLecturer(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        List<FileInfo> files = fuService.getSheetFileFromLecturer();
        writeOutListScoreSheet(files, response.getWriter());
    }
    
    /**
     * Handle respone when user request all score sheet
     * 
     * @param files score sheet (sent by lecturers)
     * @param out output stream
     */
    private void writeOutListScoreSheet(List<FileInfo> files, PrintWriter out) {
        if ((files == null) || files.isEmpty()) {
            out.println("Không tìm thấy file điểm");
            return;
        }
        
        for (int i = 0; i < files.size(); i++) {
            String trainClassId = files.get(i).getFileName();
            trainClassId = trainClassId.substring(0, trainClassId.length() - 4);
            out.println("<tr>");
            out.println("<td> <input type=\"checkbox\" value=\""
                    + trainClassId + "\" id=\"chbox-list" + i + "\" /> </td>");
            // files.get(i).getFileName ~ TrainClass ID
            out.println("<td><span class=\"atag\" value=\"" + trainClassId + "\" onclick=\"getClassDetail('" + trainClassId + "')\" >" + "Bảng điểm lớp: "
                    + files.get(i).getFileName()
                    + "</span></td>");
            out.println("</tr>");
        }
    }

    private void doGetTrainClassDetail(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String id = (String) request.getParameter("id");
        TrainClassDAO tcDao = DAOFactory.getTrainClassDAO();
        TrainClassID ID = new TrainClassID(id, Constants.CURRENT_YEAR, Constants.CURRENT_SEMESTER);
        TrainClass tc = null;
        try {
            tc = tcDao.findById(ID);
            if (tc != null) {
                String subjectName = DAOFactory.getSubjectDao()
                        .findById(tc.getSubjectCode()).getSubjectName();
                String lecturerName = DAOFactory.getLecturerDao()
                        .findById(tc.getLecturerCode()).getFullName();
                Subject sub = DAOFactory.getSubjectDao().findById(tc.getSubjectCode());
                if (sub != null) {
                    tc.setNumTC(sub.getnumTC());
                }

                tc.setSubjectName(subjectName);
                tc.setLectturerName(lecturerName);
            }
        } catch (Exception ex) {
            Logger.getLogger(ManageScoreController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        writeOutTrainClassDetail(tc, response.getWriter());
    }
    
    private void writeOutTrainClassDetail(TrainClass tc, PrintWriter out) {
        if (tc == null) {
            out.println("Không tìm thấy thông tin chi tiết");
        } else {
            out.println("<br />- Tên môn học: <b>" + tc.getSubjectName() + "</b>");
            out.println("<br />- Tên GV: <b>" + tc.getLectturerName() + "</b>");
            out.println("<br />- Tổng số SV ĐK: <b>" + tc.getNumOfStudentReg() + "</b>");
            out.println("<br />- Số TC: <b>" + tc.getNumTC() + "</b>");
            //
            String key = tc.getId().getClassCode();
            out.println("<br /><a href=\"../../DownloadController?action=class-list-student&key="
                    + key + "\"><img src=\"../../imgs/download.png\" title=\"download\"/>Download</a>");
        }
                
    }

    private void doImportSelectedFile(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String data = (String) request.getParameter("data");
        PrintWriter out = response.getWriter();
        if (StringUtils.isEmpty(data)) {
            out.println("Lỗi: vui lòng chọn lại lớp.");
            return;
        }
        
        String tobeValidateFile[] = data.split(";");
        ImportScoreResult er = new ImportScoreResult();
        
        for (int i = 0; i < tobeValidateFile.length; i++) {
            er = processImportScore(tobeValidateFile[i]);
            writeOutImportScore(out, tobeValidateFile[i], er);
        }
    }

    private ImportScoreResult processImportScore(String trainClassId) {
        TrainClassID id = new TrainClassID(trainClassId,
                Constants.CURRENT_YEAR, Constants.CURRENT_SEMESTER);
        
        return scoreUtil.importScore(id);
    }
    
    private void writeOutImportScore(PrintWriter out, String trainClass,
            ImportScoreResult result) {
        // Write out message
        out.println("<br /><b><u>Kết quả nhập điểm cho lớp: " + trainClass + "</u></b>");
        if (!StringUtils.isEmpty(result.getMsg())) {
            out.println(" - " + result.getMsg());
        }
        List<Student> students = result.getAdded();
        // Added result
        if (students != null) {
            out.println("<br /><b>- SV được thêm điểm: (" + students.size() + "):</b>");
            for (int i = 0; i < students.size(); i++) {
                out.println("<br /> \t" + (i +1) + " - " + students.get(i).getFullName());
            }
        }
        
        // Updated result
        students = result.getUpdated();
        if (students != null) {
            out.println("<br /><b>- SV được cập nhật lại điểm: (" + students.size() + "):</b>");
            for (int i = 0; i < students.size(); i++) {
                out.println("<br /> \t" + (i +1) + " - " + students.get(i).getFullName());
            }
        }
        // Inoged result
         students = result.getInoged();
        if (students != null) {
            out.println("<br /> <b>- SV không được cập nhật lại điểm"
                    + " (do điểm hiện tại > điểm mới): (" + students.size() + "):</b>");
            for (int i = 0; i < students.size(); i++) {
                out.println("<br /> \t" + (i +1) + " - " + students.get(i).getFullName());
            }
        }
    }

    /**
     *Load lecturer, just the man, who has open train class
     *
     * @param request
     * @param response 
     */
    private void doLoadLecturerWithOpenTrainClass(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        
        // Find all open train class
        TrainClassDAO tcDao = DAOFactory.getTrainClassDAO();
        if (tcDao == null) {
            out.println("- Lỗi server.");
            return;
        }
        try {
            // Get out list lecturer
            List<TrainClass> trainClasses = tcDao.findByColumName(
                    "TrangThai", TrainClassStatus.OPEN.getValue());
            if ((trainClasses == null) || trainClasses.isEmpty()) {
                out.println("- No opened class for request score sheet.");
                return;
            }
            // Group by lecturer
            LecturerDAO lDao = DAOFactory.getLecturerDao();
            List<TrainClass> trainClassesOfFirstLecturer
                    = new ArrayList<TrainClass>(10);
            Lecturer firstLecturer = lDao
                    .findById(trainClasses.get(0).getLecturerCode());
            if (firstLecturer == null) {
                out.println("- Lỗi server: (First lecturer not found).");
                return;
            }
            
            List<Lecturer> lecturers = new ArrayList<Lecturer>(10);
            lecturers.add(firstLecturer);
            
            SubjectDAO sDao = DAOFactory.getSubjectDao();
            for (TrainClass tc : trainClasses) {
                if (firstLecturer.getId().equals(tc.getLecturerCode())) {
                    Subject sub = sDao.findById(tc.getSubjectCode());
                    if (sub != null) {
                        tc.setSubjectName(sub.getSubjectName());
                    }
                    trainClassesOfFirstLecturer.add(tc);
                } else {
                    Lecturer lTemp = lDao.findById(tc.getLecturerCode());
                    if ((lTemp != null) && !lecturers.contains(lTemp)) {
                        lecturers.add(lTemp);
                    }
                }
            }
            // Write out respone
            writeOutListFirstLecturerAndClasses(out, lecturers,
                                                trainClassesOfFirstLecturer);
        } catch (Exception ex) {
            Logger.getLogger(ManageScoreController.class.getName())
                    .log(Level.SEVERE, null, ex);
            out.println("- Lỗi server: " + ex.toString());
        }
    }

    private void writeOutListFirstLecturerAndClasses(PrintWriter out,
            List<Lecturer> lecturer,
            List<TrainClass> trainClassesOfFirstLecturer) {
        out.print("Yêu cầu GV ");
        out.print("<select id=\"list-lecturer\" onchange=\"selectLecturer()\">");
        for (Lecturer l : lecturer) {
            out.print("<option value=\"" + l.getId() + "\">" + l.getFullName() + "</option>");
        }
        out.print("</select>");
        out.print("Gửi lại bảng điểm lớp ");
        out.print("<select id=\"list-class-of-lecturer\">");
        for (TrainClass tc : trainClassesOfFirstLecturer) {
            String key = tc.getId().getClassCode() + ";"
                    + tc.getId().getYear() + ";"
                    + tc.getId().getSemester();
            out.print("<option value=\"" + key + "\">"
                    + tc.getSubjectName() 
                    + " (" + tc.getId().getClassCode() + ")" + "</option>");
        }
        out.print("</select>");
    }

    private void updateListTrainClassOfLecturer(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String lecturerId = (String) request.getParameter("lecturer_id");
        PrintWriter out = response.getWriter();
        if (StringUtils.isEmpty(lecturerId)) {
            out.print("");
            return;
        }
        
        TrainClassDAO tDao = DAOFactory.getTrainClassDAO();
        SubjectDAO sDao = DAOFactory.getSubjectDao();
        try {
            List<TrainClass> trainClasses = tDao.findByColumNames(
                    new String[] {"MaGV", "TrangThai"},
                    new Object[] {lecturerId, TrainClassStatus.OPEN.getValue()});
            
            if ((trainClasses == null) || trainClasses.isEmpty()) {
                out.print("");
                return;
            }
            for (TrainClass tc : trainClasses) {
                Subject sub = sDao.findById(tc.getSubjectCode());
                if (sub != null) {
                    tc.setSubjectName(sub.getSubjectName());
                }
                String key = tc.getId().getClassCode() + ";"
                     + tc.getId().getYear() + ";"
                     + tc.getId().getSemester();
                out.print("<option value=\"" + key + "\">"
                    + tc.getSubjectName() 
                    + " (" + tc.getId().getClassCode() + ")" + "</option>");
            }
        } catch (Exception ex) {
            Logger.getLogger(ManageScoreController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    private void submitResendScore(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        
        /**Ma giang vien**/
        String lecturerId = (String) request.getParameter("lecturer_id");
        /**- In format: trainclassID;year;semeter**/
        String key = (String) request.getParameter("trainclass");
        
        if (StringUtils.isEmpty(lecturerId)
                || StringUtils.isEmpty(key)) {
            out.println("Không tìm thấy GV hay lớp tương ứng.");
            return;
        }
        String[] values = key.replace(" ", "").split(";");
        TrainClassID id = null;
        
        try {
            id = new TrainClassID(values[0], values[1],
                Integer.parseInt(values[2]));
        } catch (Exception ex) {
            out.println("Không tìm thấy GV hay lớp tương ứng.");
            return;
        }
        
        TrainClassDAO tcDao = DAOFactory.getTrainClassDAO();
        TrainClass tc = null;
        try {
            tc = tcDao.findById(id);
        } catch (Exception ex) {
            Logger.getLogger(ManageScoreController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
        if (tc == null) {
            out.println("Lỗi: không tìm thấy lớp tương ứng.");
            return;
        }
        
        Task task = new Task();
        task.setReciever(lecturerId);
        task.setSender("admin");
        task.setContent("Gui bang diem lop " + values[0]);
        task.setStatus(TaskStatus.TOBE_PROCESS);
        task.setTaskType(TaskType.REQUEST_LECTURER_SENT_SCORE);
        task.setCreated(new Date());
        
        TaskDAO taskDao = DAOFactory.getTaskDAO();
        try {
            taskDao.add(task);
        } catch (Exception ex) {
            Logger.getLogger(ManageScoreController.class.getName())
                    .log(Level.SEVERE, null, ex);
            out.println("Lỗi: " + ex.toString());
        }
        
        out.println("Yêu cầu được gửi thành công.");
    }

    //
    // INPUT MANUAL PROCESS
    //
    private void loadOpenTrainClass(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        TrainClassDAO tcDao = DAOFactory.getTrainClassDAO();
        PrintWriter out = response.getWriter();
        List<TrainClass> tcs = null;
        try {
            tcs = tcDao.findByColumNames(
                    new String[] {"HocKy", "NamHoc", "TrangThai"},
                    new Object[] {Constants.CURRENT_SEMESTER,
                        Constants.CURRENT_YEAR, TrainClassStatus.OPEN.getValue()});
        } catch (Exception ex) {
            Logger.getLogger(ManageScoreController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
        out.print("<option value=\"none\">");
        out.print("");
        out.print("</option>");
        if ((tcs == null) || tcs.isEmpty()) {
            return;
        }
        //
        SubjectDAO subDao = DAOFactory.getSubjectDao();
        for (TrainClass tc : tcs) {
            Subject subTemp = null;
            try {
                subTemp = subDao.findById(tc.getSubjectCode());
            } catch (Exception ex) {
                Logger.getLogger(ManageScoreController.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
            String key = tc.getId().getClassCode() + ";"
                     + tc.getId().getYear() + ";"
                     + tc.getId().getSemester();
            out.print("<option value=\"" + key + "\">");
            out.print((subTemp != null ? subTemp.getSubjectName() : "")
                    + " (" + tc.getId().getClassCode() + ")");
            out.print("</option>");
        }
        
    }

    private void initialManualInputForm(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        String key = (String) request.getParameter("key");
        
        if (StringUtils.isEmpty(key)) {
            out.println("Không tìm thấy thông tin");
            return;
        }
        // IDS format: trainclassID;semeter;year
        String ids[] = key.replace(" ", "").split(";");
        String trainClassId = ids[0];
        String year = ids[1];
        int semeter = Integer.parseInt(ids[2]);
        //TrainClassID tcID = new TrainClassID(trainClassId, year, semeter); 
        
        RegistrationDAO regDao = DAOFactory.getRegistrationDAO();
        List<Registration> regs = new ArrayList<Registration>(50);
        try {
            regs = regDao.findByColumNames(new String[] {"MaLopHoc", "NamHoc", "HocKy"},
                    new Object[]{trainClassId, year, semeter});
        } catch (Exception ex) {
            Logger.getLogger(ManageScoreController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
        TrainClassDAO tcDao = DAOFactory.getTrainClassDAO();
        TrainClass tc = null;
        try {
            tc = tcDao.findById(new TrainClassID(trainClassId, year, semeter));
        } catch (Exception ex) {
            Logger.getLogger(ManageScoreController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
        if ((regs == null) || regs.isEmpty()) {
            out.println("Không tìm thấy thông tin");
            return;
        }
        
        writeOutListStudyResult(out, regs, tc);
    }
    
    private void writeOutListStudyResult(PrintWriter out,
            List<Registration> regs, TrainClass tc) {
        StudentDAO studentDao = DAOFactory.getStudentDao();
        StudyResultDAO studyResultDao = DAOFactory.getStudyResultDao();
        //out.println("<input type=\"checkbox\"/> Ẩn SV đã có điểm");
        out.println();
        out.print("<table id=\"list-student-result\" class=\"general-table\" style=\"width:500px;\">");
        out.print("<tr>"
                    + "<th>STT</th>"
                    + "<th>MSSV</th>"
                    + "<th>Họ và tên</th>"
                    + "<th>Điểm</th>"
                + "</tr>");
        
        for (int i = 0; i < regs.size(); i++) {
            String studentId = regs.get(i).getId().getStudentCode();
            Student s = null;
            try {
                s = studentDao.findById(studentId);
            } catch (Exception ex) {
                Logger.getLogger(ManageScoreController.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
            
            StudyResult sr = null;
            StudyResultID id = new StudyResultID(studentId, tc.getSubjectCode());
            try {
                sr = studyResultDao.findById(id);
            } catch (Exception ex) {
                Logger.getLogger(ManageScoreController.class.getName())
                        .log(Level.SEVERE, null, ex);
                out.println("Lỗi server: " + ex.toString());
                return;
            }
            out.print("<tr>"
                    + "<td>" + (i+1) + "</td>"
                    + "<td>" + studentId + "</td>"
                    + "<td>" + (s == null ? studentId : s.getFullName()) + "</td>"
                    + "<td><input type=\"text\" value=\"" + (sr != null ? sr.getMark() : "")
                        + "\" id=\""+ studentId +"\" /></td>"
                + "</tr>");
        }
        out.print("</table>");
        out.print("<div class=\"button-1\">");
        out.print("<span class=\"atag\" onclick=\"submitManul()\"><img src=\"../../imgs/check.png\"/>Hoàn thành</span>");
        out.print("</div>");
    }

    //
    // Manual import
    // List studyResult give in data string (data)
    // data = key;data
    // key: trainclassId;year;semeter
    // data: studentId,score
    //
    private void importScoreFromForm(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String data = (String) request.getParameter("data");
        
        PrintWriter out = response.getWriter();
        if (StringUtils.isEmpty(data)) {
            out.println("Da co loi xay ra, vui long thu lai sau.");
            return;
        }
        
        String datas[] = data.replace(" ", "").split(";");
        String trainClassId = datas[0];
        String year = datas[1];
        String semeter = datas[2];
        
        TrainClassID id = new TrainClassID(trainClassId,
                year, Integer.parseInt(semeter));
        TrainClassDAO tcDao = DAOFactory.getTrainClassDAO();
        TrainClass tc = null;
        try {
            tc = tcDao.findById(id);
        } catch (Exception ex) {
            Logger.getLogger(ManageScoreController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        if (tc == null) {
            return;
        }
        
        List<StudyResult> studyResults;
        try {
            studyResults = parstData(datas, 3, datas.length,
                tc.getSubjectCode(), id);
        } catch (Exception ex) {
            out.println("Da co loi xay ra trong qua trinh nhap diem."
                    + "Vui long kiem tra lai du lieu nhap vao.");
            return;
        }
        ImportScoreResult importResult = scoreUtil.importScore(studyResults);
        
        writeOutImportScore(out, trainClassId, importResult);
    }

    private List<StudyResult> parstData(String[] datas,
                    int start, int end, String subjectID, TrainClassID tcId) {
        List<StudyResult> results = new ArrayList<StudyResult>(10);
        for (int i = start; i < end; i++) {
            String values[] = datas[i].split(",");
            StudyResult sr = new StudyResult();
            sr.setId(new StudyResultID(values[0], subjectID));
            sr.setMark(Float.parseFloat(values[1]));
            sr.setSemester(tcId.getSemester());
            sr.setYear(tcId.getYear());
            
            results.add(sr);
        }
        return results;
    }

    // Danh sach cac lop chua dc cap nhat diem
    private List<TrainClass> getMissingScoreClass() {
        TrainClassDAO tcDao = DAOFactory.getTrainClassDAO();
        List<TrainClass> tcs = new ArrayList<TrainClass>(10);
        try {
            tcs = tcDao.findByColumName("CapNhatDiem", TrainClass.SCORE_NOT_UPDATED);
            //return null;
        } catch (Exception ex) {
            Logger.getLogger(ManageScoreController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        if (!tcs.isEmpty()) {
            SubjectDAO subDao = DAOFactory.getSubjectDao();
            LecturerDAO lecturerDao = DAOFactory.getLecturerDao();
            for (TrainClass tc : tcs) {
                try {
                    tc.setSubjectName(subDao.findById(tc.getSubjectCode()).getSubjectName());
                } catch(Exception ex) {
                    //
                }
                
                try {
                    tc.setLectturerName(lecturerDao.findById(tc.getLecturerCode()).getFullName());
                } catch(Exception ex) {
                    //
                }
            }
        }
        return tcs;
    }
    
}
