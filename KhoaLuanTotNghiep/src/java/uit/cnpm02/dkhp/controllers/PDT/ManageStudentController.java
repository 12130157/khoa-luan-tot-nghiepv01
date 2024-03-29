package uit.cnpm02.dkhp.controllers.PDT;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import uit.cnpm02.dkhp.model.Class;
import uit.cnpm02.dkhp.model.Course;
import uit.cnpm02.dkhp.model.Faculty;
import uit.cnpm02.dkhp.model.Student;
import uit.cnpm02.dkhp.model.type.StudentStatus;
import uit.cnpm02.dkhp.model.type.StudyLevel;
import uit.cnpm02.dkhp.model.type.StudyType;
import uit.cnpm02.dkhp.service.IPDTService;
import uit.cnpm02.dkhp.service.IStudentService;
import uit.cnpm02.dkhp.service.impl.PDTServiceImpl;
import uit.cnpm02.dkhp.service.impl.StudentServiceImpl;
import uit.cnpm02.dkhp.utilities.Constants;
import uit.cnpm02.dkhp.utilities.DateTimeUtil;
import uit.cnpm02.dkhp.utilities.ExecuteResult;
import uit.cnpm02.dkhp.utilities.FileUtils;
import uit.cnpm02.dkhp.utilities.StringUtils;

/**
 *
 * @author LocNguyen
 */
@WebServlet(name = "ManageStudentController", urlPatterns = {"/ManageStudentController"})
public class ManageStudentController extends HttpServlet {
    
    private IPDTService pdtService = new PDTServiceImpl();
    private IStudentService studentService = new StudentServiceImpl();

    private int numPage = 1;
    private int currentPage = 1;

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request,
                                            HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        session.removeAttribute("error");

        try {
            session.removeAttribute("error");
            session.removeAttribute("students_added");
            session.removeAttribute("import.from.file.response");
        } catch (Exception ex) {
            //
        }
        try {
            numPage = studentService.getNumberPage();
            session.setAttribute("numpage", numPage);
            String action = request.getParameter("function");

            if (action.equalsIgnoreCase("liststudent")) {
                doListStudent(request, response);
            } else if (action.equalsIgnoreCase(ManageStudentSupport
                                                    .PRE_IMPORT.getValue())) {
                doPrepareDataForImportStudent(session, response);
            } else if (action.equalsIgnoreCase(ManageStudentSupport
                                                    .FACULTY_CHANGED.getValue())) {
                facultyChangeFromImportPage(request, out);
            } else if (action.equalsIgnoreCase(ManageStudentSupport
                                                        .PRE_EDIT.getValue())) {
                doPrepareDataForEditStudent(request, response);                
            } else if (action.equalsIgnoreCase(ManageStudentSupport
                                                        .UPDATE.getValue())) {
                doUpdateStudent(request, response);                
            } else if (action.equalsIgnoreCase(ManageStudentSupport
                                                .VALIDATE_ADD_ONE.getValue())) {
                doValidateAddOne(out, request);
            } else if (action.equalsIgnoreCase(ManageStudentSupport
                                                .ADD_ONE.getValue())) {
                doAddOne(out, request);
            } else if (action.equalsIgnoreCase(ManageStudentSupport
                                                .CANCEL_ADD_ONE.getValue())) {
                doCancelOne(out, request);
            } else if (action.equalsIgnoreCase(ManageStudentSupport
                                                .IMPORT_FROM_FILE.getValue())) {
                doImportFromFile(request, response);
                
            } else if (action.equalsIgnoreCase(ManageStudentSupport
                                                .RETRY_IMPORT_FROM_FILE.getValue())) {
                doRetryImportFromFile(request, response);
            } else if (action.equalsIgnoreCase(ManageStudentSupport
                                                .SEARCH.getValue())) {
                doSearchStudent(request, response);
            } else if (action.equalsIgnoreCase(ManageStudentSupport
                                                .DELETE_ONE.getValue())) {
                doDeleteOne(request, response);
            } else if (action.equalsIgnoreCase(ManageStudentSupport
                                                .DELETE_MULTI.getValue())) {
                doDeleteMulti(request, response);
            } else if (action.equalsIgnoreCase(ManageStudentSupport
                                                .PAGGING.getValue())) {
                doPaggingData(request, response);
            }
        } catch (Exception ex) {
            out.println("Đã xảy ra sự cố: </br>" + ex);
        } finally {
            out.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
                                    HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                                    HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Import data from file
     * Data format must follow a specified rule.
     * @param request request object
     * @param response respone object
     * @return string
     * @throws Exception 
     */
    private ExecuteResult importStudentFromFile(HttpServletRequest request
                , boolean importAsPossible) throws Exception {
        List<Student> students = new ArrayList<Student>();

        HSSFWorkbook wb = FileUtils.getWorkbook(request);
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
                Student s = initStudentFromHSSFRow(rowTemp);
                if (s != null)
                    students.add(s);
            }
        }

        if (!students.isEmpty()) {
            return studentService.addStudents(students, importAsPossible,
                                                request.getSession().getId());
        }
        return new ExecuteResult(false, "Không thể đọc thông tin.");
    }

    private Student initStudentFromHSSFRow(HSSFRow rowTemp) {
        try {
            Student s = new Student();
            //STT-MSSV-Họ và tên-Ngày sinh-Giới tính-CMND-Quê quán
            //-Địa chỉ-Điện thoại-Email-Mã lớp-Mã khoa-Mã khóa học-Tình trạng
            //-Bậc học-Ngày nhập học-Loại hình học-Ghi chú
            HSSFCell cellTemp = null;

            cellTemp = rowTemp.getCell(1);
            //MSSV must be a string
            if(cellTemp.getCellType() != HSSFCell.CELL_TYPE_STRING){
                cellTemp.setCellType(HSSFCell.CELL_TYPE_STRING);
            }
            String mssv = cellTemp.getStringCellValue();
            s.setId(mssv);

            cellTemp = rowTemp.getCell(2);
            String fullName = cellTemp.getStringCellValue();
            s.setFullName(fullName);

            cellTemp = rowTemp.getCell(3);
            //String birthDay = cellTemp.getStringCellValue();
            s.setBirthday(new Date());

            cellTemp = rowTemp.getCell(4);
            String gender = cellTemp.getStringCellValue();
            s.setGender(gender);

            cellTemp = rowTemp.getCell(5);
            cellTemp.setCellType(HSSFCell.CELL_TYPE_STRING);
            String cmnd = cellTemp.getStringCellValue();
            s.setIdentityNumber(cmnd);

            cellTemp = rowTemp.getCell(6);
            String homeAddr = cellTemp.getStringCellValue();
            s.setHomeAddr(homeAddr);

            cellTemp = rowTemp.getCell(7);
            String address = cellTemp.getStringCellValue();
            s.setAddress(address);

            cellTemp = rowTemp.getCell(8);
            cellTemp.setCellType(HSSFCell.CELL_TYPE_STRING);
            String phone = cellTemp.getStringCellValue();
            s.setPhone(phone);

            cellTemp = rowTemp.getCell(9);
            String email = cellTemp.getStringCellValue();
            s.setEmail(email);

            cellTemp = rowTemp.getCell(10);
            String clazz = cellTemp.getStringCellValue();
            s.setClassCode(clazz);

            cellTemp = rowTemp.getCell(11);
            String facult = cellTemp.getStringCellValue();
            s.setFacultyCode(facult);

            cellTemp = rowTemp.getCell(12);
            String course = cellTemp.getStringCellValue();
            s.setCourseCode(course);

            cellTemp = rowTemp.getCell(13);
            String status = cellTemp.getStringCellValue();
            StudentStatus statusEnum = StudentStatus.NORMAL;
            try {
                statusEnum = StudentStatus.getStudyStatus(status);
            } catch(Exception ex) {
                //
            }
            if (statusEnum == null) {
                statusEnum = StudentStatus.NORMAL;
            }
            s.setStatus(statusEnum.value());

            cellTemp = rowTemp.getCell(14);
            String level = cellTemp.getStringCellValue();
            s.setStudyLevel(level);

            cellTemp = rowTemp.getCell(15);
            //String enterDate = cellTemp.getStringCellValue();
            s.setDateStart(new Date());

            cellTemp = rowTemp.getCell(16);
            String type = cellTemp.getStringCellValue();
            s.setStudyType(type);

            cellTemp = rowTemp.getCell(17);
            String note = cellTemp.getStringCellValue();
            s.setNote(note);

            return s;
        } catch (Exception ex) {
            return null;
        }
    }
    
    /**
     * Prepare data for Import student page usaged.
     * - List Classes
     * - List Faculties
     * - List course
     * @param session 
     */
    private void doPrepareDataForImportStudent(HttpSession session,
                           HttpServletResponse response) throws IOException {
        List<uit.cnpm02.dkhp.model.Class> clazzes = pdtService.getAllClass();
        List<Faculty> faculties = pdtService.getAllFaculty();
        List<Course> courses = pdtService.getAllCourse();
        
        if (clazzes != null) {
            session.setAttribute("clazzes", clazzes);
        }
        if (faculties != null) {
            session.setAttribute("faculties", faculties);
        }
        if (courses != null) {
            session.setAttribute("courses", courses);
        }
        
        String path = "./jsps/PDT/ImportStudent.jsp";
        response.sendRedirect(path);
    }

    /**
     * At import student page, when user change faculty
     * the class must be changed correspond with faculty
     * selected.
     * @param request
     * @param out 
     */
    private void facultyChangeFromImportPage(HttpServletRequest request,
                                                        PrintWriter out) {
        String facultyId = request.getParameter("facultyId");
        if (StringUtils.isEmpty(facultyId)) {
            return;
        }
        
        List<uit.cnpm02.dkhp.model.Class> clazzes = 
                pdtService.getClassByFaculty(facultyId);
        writeListClassWhenFacultyChanged(out, clazzes);
    }

    private void writeListClassWhenFacultyChanged(PrintWriter out,
                                            List<Class> clazzes) {
        if ((clazzes == null) || clazzes.isEmpty()) {
            return;
        }
        
        for (Class c : clazzes) {
            out.println("<option value=\"" 
                    + c.getId() + "\">" 
                    + c.getClassName() 
                    + "</option>");
        }
    }
    
    private Student getStudentFromRequest(HttpServletRequest request) {
        String mssv = request.getParameter("mssv");
        String fullname = request.getParameter("fullname");
        String birthDay = request.getParameter("birthDay");
        String cmnd = request.getParameter("cmnd");
        String homeAddress = request.getParameter("homeAddress");
        String gender = request.getParameter("gender");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String addsress = request.getParameter("addsress");
        String enterDate = request.getParameter("enterDate");
        String faculty = request.getParameter("faculty");
        String clazz = request.getParameter("clazz");
        String course = request.getParameter("course");
        String statusStr = request.getParameter("status");
        String level = request.getParameter("level");
        String studyType = request.getParameter("studyType");
        String note = request.getParameter("note");
        
        SimpleDateFormat sdf = new SimpleDateFormat(
                                        Constants.DATETIME_PARTERM_DEFAULT);
        int status = StudentStatus.NORMAL.value();
        try {
            status = Integer.parseInt(statusStr);
            int studyLevelInt = Integer.parseInt(level);
            int typeInt = Integer.parseInt(studyType);
            level = StudyLevel.getStudyLevel(studyLevelInt).description();
            studyType = StudyType.getStudyType(typeInt).description();
        } catch (Exception ex) {
            //
        }
        
        // Validate
        
        // Add
        Date birthDayD = null;
        Date dateStart = null;
        try {
            dateStart = sdf.parse(enterDate);
            birthDayD = sdf.parse(birthDay);
        } catch (Exception ex) {
            Logger.getLogger(ManageScoreController.class.getName())
                    .log(Level.WARNING, null, ex);
        }
        Student s = new Student(mssv, fullname, birthDayD, gender, cmnd, homeAddress,
                addsress, phone, email, clazz, faculty, course, status,
                level, dateStart, studyType, note);
        
        return s;
    }

    private void doValidateAddOne(PrintWriter out, HttpServletRequest request) {
         // Get data from request instance.
        Student s = getStudentFromRequest(request);
        ExecuteResult er = studentService.validateNewStudent(s);
        
        // Write out back result
        writeOutValidateAddOneResult(out, er);
    }
    
    /**
     * Do add one student from submit form
     * This include following actions:
     *  Validate student information
     *  Persistent to database
     *  Write out result respone to user
     *  (In result send back, user should has
     * a choice to cancel his/her action).
     * @param out out put stream
     * @param request request object.
     */
    private void doAddOne(PrintWriter out, HttpServletRequest request) {
        // Get data from request instance.
        Student s = getStudentFromRequest(request);
        ExecuteResult er = studentService.addStudent(s);
        
        // Write out back result
        writeOutAddOneResult(out, er);
    }
    
    private void writeOutValidateAddOneResult(PrintWriter out, ExecuteResult er) {
        if (!er.isIsSucces()) {
            out.println(er.getMessage());
        } else {
            out.println("Thông tin hợp lệ");
        }
    }
    
    private void writeOutAddOneResult(PrintWriter out, ExecuteResult er) {
        if (!er.isIsSucces()) {
            out.println("Thêm SV không thành công: " + er.getMessage());
        } else {
            Student s = (Student) er.getData();
            //../../ManageStudentController?function=cancel-add-one&mssv=" 
            String cancelFunction = "cancelAddOne('" + s.getId() + "')";
            out.println("Thêm SV: <b>" + s.getFullName() 
                    + "</b> thành công "
                    + "<span class=\"atag\" onclick=\"" + cancelFunction + "\"><b>Hủy</b></span>");
            out.println("<table class=\"general-table\">");
            out.println("<tr>"
                        + "<th> MSSV </th>"
                        + "<th> Họ và tên </th>"
                        + "<th> Giới tính </th>"
                        + "<th> Quê quán </th>"
                    + "</tr>");
            out.println("<tr>"
                    + "<td>" + s.getId() + "</td>"
                    + "<td>" + s.getFullName() + "</td>"
                    + "<td>" + s.getGender() + "</td>"
                    + "<td>" + s.getHomeAddr() + "</td>"
                    + "</tr>");
            out.println("</table>");
        }
    }

    private void doCancelOne(PrintWriter out, HttpServletRequest request) {
         String mssv = request.getParameter("mssv");
         
         ExecuteResult er = studentService.deleteStudent(
                 mssv, false, request.getSession().getId());
         writeOutCancelAddOneResult(out, er);
    }
    
    private void writeOutCancelAddOneResult(PrintWriter out, ExecuteResult er) {
        if (!er.isIsSucces()) {
            out.println("Không thể xóa SV. " + er.getMessage());
            out.println("Vui lòng truy cập trang </b>Quản lý Sinh viên</b> để thử lại");
        } else {
            Student s = (Student) er.getData();
            out.println("Xóa thành công sinh viên <b>" + s.getFullName() + "</b>");
        }
    }

    private void doImportFromFile(HttpServletRequest request,
                            HttpServletResponse response) throws Exception {
        boolean importAsPossible;
        try {
            importAsPossible = Boolean.parseBoolean(
                                request.getParameter("import-as-possible"));
        } catch (Exception ex) {
            importAsPossible = false;
        }
        ExecuteResult result = importStudentFromFile(request, importAsPossible);

        HttpSession session = request.getSession();
        session.setAttribute("import.from.file.response", true);
        session.setAttribute("import-from-file-result", result);
        String path = "./jsps/PDT/ImportStudent.jsp";
        response.sendRedirect(path);
    }

    private void doRetryImportFromFile(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            ExecuteResult er = studentService.addStudents(
                            null, true, request.getSession().getId());
            writeOutResponseForRetryImportFromFile(response.getWriter(), er);
        } catch (IOException ex) {
            Logger.getLogger(ManageStudentController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    
    private void writeOutResponseForRetryImportFromFile(PrintWriter out,
            ExecuteResult er) {
        if (!er.isIsSucces()) {
            out.println(er.getMessage());
        } else {
            List<Student> students = (List<Student>) er.getData();
            if ((students == null) || students.isEmpty()) {
                out.println("Không có SV nào được thêm");
            } else {
                out.println("<i>Thêm thành công các SV:</i>");
                out.println("<table class=\"general-table\" style=\"width: 450px;\">");
                out.println("<tr><th>STT</th><th>MSSV</th><th>Họ và tên</th><th>CMND</th></tr>");
                for (int i = 0; i < students.size(); i++) {
                    out.println("<tr>"
                            + "<td>" + (i+1) + "</td>"
                            + "<td>" + students.get(i).getId() + "</td>"
                            + "<td>" + students.get(i).getFullName() + "</td>"
                            + "<td>" + students.get(i).getIdentityNumber() + "</td>"
                            + "</tr>");
                }
                out.println("</table>");
            }
        }
    }

    private void doSearchStudent(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String key = "*";
        try {
            key = request.getParameter("key");
        } catch(Exception ex) {
            //
        }
        List<Student> students = studentService.search(
                key, request.getSession().getId());
        List<Student> result = new ArrayList<Student>(Constants.ELEMENT_PER_PAGE_DEFAULT);
        if(students.size()> Constants.ELEMENT_PER_PAGE_DEFAULT){
            for(int i=0; i < Constants.ELEMENT_PER_PAGE_DEFAULT; i++){
               result.add(students.get(i));
            }
            writeOutSearchResult(response.getWriter(), result, 1);
        }else{
            writeOutSearchResult(response.getWriter(), students, 1);
        }
        
    }
    
      private void writeOutSearchResult(PrintWriter out, List<Student> students, int currentPage) {
        if (students == null) {
            return;
        }
        out.println("<table id=\"tableliststudent\" name=\"tableliststudent\" class=\"general-table\">");
        out.println("<tr>"
                + "<th><INPUT type=\"checkbox\" name=\"chkAll\" onclick=\"selectAll('tableliststudent', 0)\" /></th>"
                + "<th> STT </th>"
                + "<th> MSSV </span></th>"
                + "<th> Họ tên </span> </th>"
                + "<th> Lớp </span> </th>"
                + "<th> Khoa </span> </th>"
                + "<th> Ngày sinh </span> </th>"
                + "<th> Giới tính </span> </th>"
                + "<th> Loại </span> </th>"
                + "<th> Sửa </th>"
                + "<th> Xóa </th>"
                + "</tr>");

        if (!students.isEmpty()) {
            for (int i = 0; i < students.size(); i++) {
                out.println("<tr>"
                        + "<td><INPUT type=\"checkbox\" name=\"chk" +  i + "\"/></td>"
                        + "<td>" + ((currentPage-1)*Constants.ELEMENT_PER_PAGE_DEFAULT + 1 + i) + "</td>"
                        + "<td>" + students.get(i).getId() + "</td>"
                        + "<td>" + students.get(i).getFullName() + "</td>"
                        + "<td>" + students.get(i).getClassCode() + "</td>"
                        + "<td>" + students.get(i).getFacultyCode() + "</td>"
                        + "<td>" + DateTimeUtil.format(students.get(i).getBirthday()) + "</td>"
                        + "<td>" + students.get(i).getGender() + "</td>"
                        + "<td>" + students.get(i).getStudyType() + "</td>"
                        + "<td>" + "<a href=\"../../ManageStudentController?function=editstudent&mssv=" + students.get(i).getId() + "\"><img src=\"../../imgs/icon/edit.png\" title=\"Sửa\" alt=\"Sửa\"/></a></td>"
                        + "<td><span class=\"atag\" onclick=\"deleteOneStudent('" + students.get(i).getId() + "')\"><img src=\"../../imgs/icon/delete.png\" title=\"Xóa\" alt=\"Xóa\"/></span></td>"
                        + "</tr>");
            }
        }
        out.println("</table>");
    }

    private void doListStudent(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        List<Student> students;
        try {
            currentPage = Integer.parseInt(request.getParameter("currentpage"));
            if (currentPage < 0) {
                currentPage = 0;
            } else if (currentPage > numPage) {
                currentPage = numPage;
            }
        } catch (Exception ex) {
            currentPage = 1;
        }

        //students = studentDao.findAll(rowPerPage, currentPage, "HoTen", null);
        students = studentService.getStudents(
                currentPage, request.getSession().getId());
        if ((students != null) && !students.isEmpty()) {
            HttpSession session = request.getSession();
            session.setAttribute("liststudent", students);
        }
        
        String path = "./jsps/PDT/ListStudent.jsp";
        response.sendRedirect(path);
    }

    private void doDeleteOne(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        String mssv = request.getParameter("mssv");
         try {
            String page = request.getParameter("currentPage");
            currentPage = Integer.parseInt(page);
        } catch(Exception ex) {
            currentPage = 1;
        }
        String sessionId = request.getSession().getId();
        ExecuteResult er = studentService.deleteStudent(
                            mssv, false, sessionId);
        
        if (!er.isIsSucces()) {
            out.append("error " + er.getMessage());
        } else {
            List<Student> students = studentService
                .getStudents(currentPage, sessionId);
            if ((students != null) && !students.isEmpty()) {
                writeOutSearchResult(out, students, currentPage);
            }
        }
    }
    
    private void doDeleteMulti(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        /** Data hold list of student tobe deleted
         in format: mssv1 - mssv2 - mssvn**/
        String data = request.getParameter("data");
        if (StringUtils.isEmpty(data)) {
            out.append("error " + "Không lấy được dữ liệu từ Client."
                    + " Vui lòng thử lại.");
            return;
        }
        
        List<Student> students = new ArrayList<Student>(10);
        List<String> mssv = Arrays.asList(data.split("-"));
        
        String sessionId = request.getSession().getId();
        ExecuteResult er = studentService.deleteStudents(
                            mssv, false, sessionId);
        
        if (!er.isIsSucces()) {
            out.append("error " + er.getMessage());
        } else {
            students = studentService.getStudents(sessionId);
            if ((students != null) && !students.isEmpty()) {
                writeOutSearchResult(out, students,1);
            }
        }
    }

    private void doPrepareDataForEditStudent(HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String mssv = request.getParameter("mssv");
        Student s = studentService.getStudent(mssv);
        
        HttpSession session = request.getSession();
        session.setAttribute("student", s);
        
        List<uit.cnpm02.dkhp.model.Class> clazzes = 
                (List<uit.cnpm02.dkhp.model.Class>) session.getAttribute("clazzes");
        List<Faculty> faculties = (List<Faculty>) session.getAttribute("faculties");
        List<Course> courses = (List<Course>) session.getAttribute("courses");
        
        if ((clazzes == null) || clazzes.isEmpty()) {
            clazzes = pdtService.getAllClass();
            session.setAttribute("clazzes", clazzes);
        }
        if ((faculties == null) || faculties.isEmpty()) {
            faculties = pdtService.getAllFaculty();
            session.setAttribute("faculties", faculties);
        }
        if ((courses == null) || courses.isEmpty()) {
            courses = pdtService.getAllCourse();
            session.setAttribute("courses", courses);
        }
        
        String path = "./jsps/PDT/EditStudent.jsp";
        response.sendRedirect(path);
    }
    
    private void doUpdateStudent(HttpServletRequest request,
                    HttpServletResponse response) throws IOException {
        // Get data from request instance.
        Student s = getStudentFromRequest(request);
        ExecuteResult er = studentService
                .addUpdateStudent(request.getSession().getId(), s);
        
        PrintWriter out = response.getWriter();
        out.println(er.getMessage());
    }

    private void doPaggingData(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        
        try {
            String page = request.getParameter("currentpage");
            currentPage = Integer.parseInt(page);
        } catch(Exception ex) {
            currentPage = 1;
        }
        
        List<Student> students = studentService
                .getStudents(currentPage, request.getSession().getId());
        if ((students != null) && !students.isEmpty()) {
            writeOutSearchResult(response.getWriter(), students,currentPage);
        }
    }

    //#############################################
    public enum ManageStudentSupport {
        DEFAULT("default"), // List first page of class opened.
        PRE_IMPORT("pre-import-student"),
        VALIDATE_ADD_ONE("validate-add-one"),
        ADD_ONE("add-one"),
        IMPORT_FROM_FILE("importfromfile"),
        RETRY_IMPORT_FROM_FILE("retry-import-from-file"),
        CANCEL_ADD_ONE("cancel-add-one"),
        FACULTY_CHANGED("faculty-change"),
        SEARCH("search-students"),
        SORT("sort"),
        DELETE_ONE("delete-one"),
        DELETE_MULTI("delete-multi"),
        PRE_EDIT("editstudent"),
        UPDATE("update"),
        PAGGING("pagging");
        
        private String description;
        ManageStudentSupport(String description) {
            this.description = description;
        }
        
        public String getValue() {
            return description;
        }
        
    }
}
