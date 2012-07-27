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
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.LecturerDAO;
import uit.cnpm02.dkhp.model.Faculty;
import uit.cnpm02.dkhp.model.Lecturer;
import uit.cnpm02.dkhp.model.type.HocHam;
import uit.cnpm02.dkhp.model.type.HocVi;
import uit.cnpm02.dkhp.service.ILecturerService;
import uit.cnpm02.dkhp.service.IPDTService;
import uit.cnpm02.dkhp.service.impl.LecturerServiceImpl;
import uit.cnpm02.dkhp.service.impl.PDTServiceImpl;
import uit.cnpm02.dkhp.utilities.Constants;
import uit.cnpm02.dkhp.utilities.DateTimeUtil;
import uit.cnpm02.dkhp.utilities.FileUtils;
import uit.cnpm02.dkhp.utilities.ExecuteResult;
import uit.cnpm02.dkhp.utilities.StringUtils;

/**
 *
 * @author LocNguyen
 */
@WebServlet(name = "ManageLecturerController", urlPatterns = {"/ManageLecturerController"})
public class ManageLecturerController extends HttpServlet {
    private ILecturerService lecturerService = new LecturerServiceImpl();
    private IPDTService pdtService = new PDTServiceImpl();
    
    private LecturerDAO lecturerDao = DAOFactory.getLecturerDao();
    private int rowPerPage = Constants.ELEMENT_PER_PAGE_DEFAULT;
    private int numPage = 1;
    private int currentPage = 1;


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

        try {
            numPage = getNumberPage();
            session.setAttribute("numpage", numPage);
            String action = request.getParameter("function");
            if (action.equalsIgnoreCase(ManageLecturerSupport
                    .SEARCH.getValue())) {
                doSearch(request, response);
            } else if (action.equalsIgnoreCase(ManageLecturerSupport
                    .PRE_IMPORT.getValue())) {
                doPrepareDataForImportLecturer(session, response);
            } else if (action.equalsIgnoreCase(ManageLecturerSupport
                    .DELETE_ONE.getValue())) {
                doDeleteOne(request, response);
            } else if (action.equalsIgnoreCase(ManageLecturerSupport
                    .DELETE_MULTI.getValue())) {
                doDeleteMulti(request, response);
            } else if (action.equalsIgnoreCase("listlecturer")) {
                listLecturer(request, response);
                String path = "./jsps/PDT/ListLecturer.jsp";
                response.sendRedirect(path);
            } else if (action.equalsIgnoreCase(ManageLecturerSupport
                    .PRE_EDIT.getValue())) {
                doPrepareDataForEditLecturer(request, response);
            } else if (action.equalsIgnoreCase(ManageLecturerSupport
                    .UPDATE.getValue())) {
                doUpdateLecturer(request, response);
            } else if (action.equalsIgnoreCase(ManageLecturerSupport
                    .VALIDATE_ADD_ONE.getValue())) {
                doValidateAddOne(out, request);
            } else if (action.equalsIgnoreCase(ManageLecturerSupport
                    .ADD_ONE.getValue())) {
                doAddOne(out, request);
            } else if (action.equalsIgnoreCase(ManageLecturerSupport
                    .CANCEL_ADD_ONE.getValue())) {
                doCancelOne(out, request);
            } else if (action.equalsIgnoreCase(ManageLecturerSupport
                    .IMPORT_FROM_FILE.getValue())) {
                doImportFromFile(request, response);
                
            } else if (action.equalsIgnoreCase(ManageLecturerSupport
                    .RETRY_IMPORT_FROM_FILE.getValue())) {
                doRetryImportFromFile(request, response);
            }  else if (action.equalsIgnoreCase(ManageLecturerSupport
                    .PAGGING.getValue())) {
                doPaggingData(request, response);
            }
        } catch (Exception ex) {
            out.println("Đã xảy ra sự cố: </br>" + ex);
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

    private int getNumberPage() throws Exception {
        int rows = lecturerDao.getRowsCount();

        int rowsPerPage = Constants.ELEMENT_PER_PAGE_DEFAULT;
        if (rows % rowsPerPage == 0) {
            numPage = rows / rowsPerPage;
        } else {
            numPage = rows / rowsPerPage + 1;
        }
        return numPage;
    }

    private void listLecturer(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Lecturer> lectureres;
        String searchType = (String) request.getParameter("searchtype");
        String searchValue = (String) request.getParameter("searchvalue");
        String ajaxRespone = (String) request.getParameter("ajax");
        HttpSession session = request.getSession();
        session.removeAttribute("ajax");
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

        if ((searchType == null) || searchType.isEmpty()
                || (searchValue == null) || searchValue.isEmpty()
                || searchValue.equalsIgnoreCase("*")
                || searchValue.equalsIgnoreCase("all")) {
            lectureres = lecturerDao.findAll(rowPerPage, currentPage, "HoTen", null);
        } else {
            String searchTypeStr = "";
            if (searchType.equals("name")) {
                searchTypeStr = "HoTen";
            }/* else if (searchType.equals("clazz")) {
            searchTypeStr = "MaLop";
            } else if (searchType.equals("course")) {
            searchTypeStr = "MaKhoa";
            }*/
            lectureres = lecturerDao.findAll(rowPerPage, currentPage, searchTypeStr, "'%" + searchValue + "%'", "HoTen", null);
        }
        if ((ajaxRespone == null)
                || ajaxRespone.isEmpty()
                || ajaxRespone.equals("false")) {
            session = request.getSession();
            session.setAttribute("listlecturer", lectureres);
        } else {

            PrintWriter out = response.getWriter();
            String respStr = "<tr id=\"tablelistlecturer-th\">"
                    + "<td><INPUT type=\"checkbox\" name=\"chkAll\" onclick=\"selectAll('tablelistlecturer', 0)\" /></td>"
                    + "<td> STT </td>"
                    + "<td> Mã GV </td>"
                    + "<td> Họ Tên </td>"
                    + "<td> Khoa </td>"
                    + "<td> Địa chỉ </td>"
                    + "<td> Ngày sinh </td>"
                    + "<td> Giới tính </td>"
                    + "<td> Email </td>"
                    + "<td> Học Hàm </td>"
                    + "<td> Học Vị </td>"
                    + "<td> Sửa </td>"
                    + "<td> Xóa </td>"
                    + "</tr>";
            out.println(respStr);
            for (int i = 0; i < lectureres.size(); i++) {
                respStr = "<tr>"
                        + "<td><INPUT type=\"checkbox\" name=\"chk" + i + "\"/></td>"
                        + "<td> " + (i + 1) + " </td>"
                        + "<td> " + lectureres.get(i).getId() + "</td> "
                        + "<td> " + lectureres.get(i).getFullName() + "</td>"
                        + "<td> " + lectureres.get(i).getFacultyCode() + "</td>"
                        + "<td> " + lectureres.get(i).getAddress() + "</td>"
                        + "<td> " + lectureres.get(i).getBirthday() + "</td>"
                        + "<td> " + lectureres.get(i).getGender() + "</td>"
                        + "<td> " + lectureres.get(i).getEmail() + "</td>"
                        + "<td> " + lectureres.get(i).getHocHam() + "</td>"
                        + "<td> " + lectureres.get(i).getHocVi() + "</td>"
                        + "<td> <a href=\"../../ManageLecturerController?function=editlecturer&mgv=" + lectureres.get(i).getId() + "\">Sửa</a></td>"
                        + "<td> <a href=\"../../ManageLecturerController?function=delete&ajax=false&data=" + lectureres.get(i).getId() + "\"> Xóa</a></td>"
                        + "</tr>";
                out.println(respStr);
            }
            out.close();
        }
    }

    private ExecuteResult deleteLecturer(HttpServletRequest request,
            HttpServletResponse response) {
        ExecuteResult result = new ExecuteResult(true, null);
        String data = (String) request.getParameter("data");

        if ((data == null) || data.isEmpty()) {
            result.setMessage("Xóa không thành công.");
            return result;
        }
        String[] ids = data.split("-");
        if (lecturerDao == null) {
            lecturerDao = DAOFactory.getLecturerDao();
        }

        try {
            List<Lecturer> lecturers = lecturerDao.findByIds(ids);
            if (!lecturers.isEmpty()) {
                lecturerDao.delete(lecturers);
                result.setMessage("Đã xóa " + lecturers.size() + " giảng viên viên");
                return result;
            }
        } catch (Exception ex) {
            result.setIsSucces(false);
            result.setMessage("Lỗi: " + ex.toString());
        }
        return result;
    }

    //
    //
    //
    /**
     * Search
     * @param request
     * @param response 
     */
    private void doSearch(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String key = "*";
        try {
            key = request.getParameter("key");
        } catch(Exception ex) {
            //
        }
        List<Lecturer> lecturers = lecturerService.search(
                key, request.getSession().getId());
        List<Lecturer> result = new ArrayList<Lecturer>(Constants.ELEMENT_PER_PAGE_DEFAULT);
        if(lecturers.size()> Constants.ELEMENT_PER_PAGE_DEFAULT){
            for(int i=0; i < Constants.ELEMENT_PER_PAGE_DEFAULT; i++){
               result.add(lecturers.get(i));
            }
            writeOutListLecturers(response.getWriter(), result, 1);
        }else{
            writeOutListLecturers(response.getWriter(), lecturers, 1);
        }
        
        
        
    }

    /**
     * Write out list lecterers for ajax calling...
     * 
     */
     private void writeOutListLecturers(PrintWriter out,
            List<Lecturer> lecturers, int currentPage) {
        if (lecturers == null) {
            return;
        }
       out.println("<table id=\"tablelistlecturer\" name=\"tablelistlecturer\" class=\"general-table\">");
        out.println("<tr>"
                + "<th><INPUT type=\"checkbox\" name=\"chkAll\" onclick=\"selectAll('tablelistlecturer', 0)\" /></th>"
                + "<th> STT </th>"
                + "<th> Mã GV </span></th>"
                + "<th> Họ tên </span> </th>"
                + "<th> Khoa </span> </th>"
                + "<th> Địa chỉ </span> </th>"
                + "<th> Ngày sinh </span> </th>"
                + "<th> Giới tính </span> </th>"
                /*+ "<th> <span class=\"atag\" onclick=\"sort('Email')\" >  Email </span> </th>"*/
                + "<th> Học Hàm </span> </th>"
                + "<th> Học Vị </span> </th>"
                + "<th> Sửa </th>"
                + "<th> Xóa </th>"
                + "</tr>");
        if (!lecturers.isEmpty()) {
            for (int i = 0; i < lecturers.size(); i++) {
                out.println("<tr>"
                        + "<td><INPUT type=\"checkbox\" name=\"chk" + i + "\"/></td>"
                        + "<td>" + ((currentPage-1)*Constants.ELEMENT_PER_PAGE_DEFAULT + 1 + i) + "</td>"
                        + "<td>" + lecturers.get(i).getId() + "</td>"
                        + "<td>" + lecturers.get(i).getFullName() + "</td>"
                        + "<td>" + lecturers.get(i).getFacultyCode() + "</td>"
                        + "<td>" + lecturers.get(i).getAddress() + "</td>"
                        + "<td>" + DateTimeUtil.format(lecturers.get(i).getBirthday()) + "</td>"
                        + "<td>" + lecturers.get(i).getGender() + "</td>"
                        /*+ "<td>" + lecturers.get(i).getEmail() + "</td>"*/
                        + "<td>" + lecturers.get(i).getHocHam() + "</td>"
                        + "<td>" + lecturers.get(i).getHocVi() + "</td>"
                        + "<td>" + "<a href=\"../../ManageLecturerController?function=edit&magv=" + lecturers.get(i).getId() + "\"><img src=\"../../imgs/icon/edit.png\" title=\"Sửa\" alt=\"Sửa\"/></a></td>"
                        + "<td><span class=\"atag\" onclick=\"deleteOneLecturer('" + lecturers.get(i).getId() + "')\"><img src=\"../../imgs/icon/delete.png\" title=\"Xóa\" alt=\"Xóa\"/></span></td>"
                        + "</tr>");
            }
        }
        out.println("</table>");
       
    }

    private void doPrepareDataForImportLecturer(HttpSession session,
            HttpServletResponse response) throws IOException {
        List<Faculty> faculties = pdtService.getAllFaculty();
        
        if (faculties != null) {
            session.setAttribute("faculties", faculties);
        }
        String path = "./jsps/PDT/ImportLecturer.jsp";
        response.sendRedirect(path);
    }

    private void doDeleteOne(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        String magv = request.getParameter("magv");
        String sessionId = request.getSession().getId();
        ExecuteResult er = lecturerService.deleteLecturer(magv, false, sessionId);
         try {
            String page = request.getParameter("currentPage");
            currentPage = Integer.parseInt(page);
        } catch(Exception ex) {
            currentPage = 1;
        }
        if (!er.isIsSucces()) {
            out.append("error " + er.getMessage());
        } else {
            List<Lecturer> lecturers = lecturerService
                .getLecturers(currentPage, sessionId);
            if ((lecturers != null) && !lecturers.isEmpty()) {
                writeOutListLecturers(out, lecturers, currentPage);
            }
        }
    }

    private void doValidateAddOne(PrintWriter out, HttpServletRequest request) {
        // Get data from request instance.
        Lecturer l = getLecturerFromRequest(request);
        ExecuteResult er = lecturerService.validateNewLecturer(l);
        
        // Write out back result
        writeOutValidateAddOneResult(out, er);
    }

    private Lecturer getLecturerFromRequest(HttpServletRequest request) {
        String magv = request.getParameter("magv");
        String fullname = request.getParameter("fullname");
        String birthDay = request.getParameter("birthDay");
        String cmnd = request.getParameter("cmnd");
        String address = request.getParameter("address");
        String gender = request.getParameter("gender");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String faculty = request.getParameter("faculty");
        String hocham = request.getParameter("hocham");
        String hocvi = request.getParameter("hocvi");
        String note = request.getParameter("note");
        
        SimpleDateFormat sdf = new SimpleDateFormat(
                                        Constants.DATETIME_PARTERM_DEFAULT);
        try {
            int hocHamInt = Integer.parseInt(hocham);
            int hocViInt = Integer.parseInt(hocvi);
            hocham = HocHam.getHocHam(hocHamInt).description();
            hocvi = HocVi.getHocVi(hocViInt).description();
        } catch (Exception ex) {
            //
        }
        
        // Validate
        
        // Add
        Date birthDayD = null;
        try {
            birthDayD = sdf.parse(birthDay);
        } catch (Exception ex) {
            Logger.getLogger(ManageLecturerController.class.getName())
                    .log(Level.WARNING, null, ex);
        }
        Lecturer l = new Lecturer(magv, fullname, faculty, birthDayD, address,
                cmnd, phone, email, gender, hocham, hocvi);
        if (!StringUtils.isEmpty(note)) {
            l.setNote(note);
        }
        
        return l;
    }

    private void writeOutValidateAddOneResult(PrintWriter out, ExecuteResult er) {
        if (er.isIsSucces())
            out.println("Thông tin hợp lệ.");
        else
            out.append(er.getMessage());
    }

    //
    // ADD ONE
    //
    private void doAddOne(PrintWriter out, HttpServletRequest request) {
        // Get data from request instance.
        Lecturer l = getLecturerFromRequest(request);
        ExecuteResult er = lecturerService.addLecturer(l);
        
        // Write out back result
        writeOutAddOneResult(out, er);
    }

    private void writeOutAddOneResult(PrintWriter out, ExecuteResult er) {
        if (!er.isIsSucces()) {
            out.println("Thêm GV không thành công: " + er.getMessage());
        } else {
            Lecturer l = (Lecturer) er.getData();
            String cancelFunction = "cancelAddOne('" + l.getId() + "')";
            out.println("Thêm GV: <b>" + l.getFullName() 
                    + "</b> thành công "
                    + "<span class=\"atag\" onclick=\"" + cancelFunction + "\"><b>Hủy</b></span>");
            out.println("<table class=\"general-table\">");
            out.println("<tr>"
                        + "<th> Ma GV </th>"
                        + "<th> Họ và tên </th>"
                        + "<th> Giới tính </th>"
                        + "<th> Quê quán </th>"
                    + "</tr>");
            out.println("<tr>"
                    + "<td>" + l.getId() + "</td>"
                    + "<td>" + l.getFullName() + "</td>"
                    + "<td>" + l.getGender() + "</td>"
                    + "<td>" + l.getAddress() + "</td>"
                    + "</tr>");
            out.println("</table>");
        }
    }

    private void doCancelOne(PrintWriter out, HttpServletRequest request) {
        String magv = request.getParameter("magv");

        ExecuteResult er = lecturerService.deleteLecturer(
                magv, false, request.getSession().getId());
        writeOutCancelAddOneResult(out, er);
    }

    private void writeOutCancelAddOneResult(PrintWriter out, ExecuteResult er) {
        if (!er.isIsSucces()) {
            out.println("Không thể xóa GV. " + er.getMessage());
            out.println("Vui lòng truy cập trang </b>Quản lý Giảng viên</b> để thử lại");
        } else {
            Lecturer l = (Lecturer) er.getData();
            out.println("Xóa thành công giảng viên <b>" + l.getFullName() + "</b>");
        }
    }

    //
    // IMPORT FROM FILE
    //
    private void doImportFromFile(HttpServletRequest request,
                        HttpServletResponse response) throws Exception {
        boolean importAsPossible;
        try {
            importAsPossible = Boolean.parseBoolean(
                                request.getParameter("import-as-possible"));
        } catch (Exception ex) {
            importAsPossible = false;
        }
        ExecuteResult result = importLecturerFromFile(request, importAsPossible);

        HttpSession session = request.getSession();
        session.setAttribute("import.from.file.response", true);
        session.setAttribute("import-from-file-result", result);
        String path = "./jsps/PDT/ImportLecturer.jsp";
        response.sendRedirect(path);
    }

    private ExecuteResult importLecturerFromFile(
            HttpServletRequest request, boolean importAsPossible) {
        List<Lecturer> lecturers = new ArrayList<Lecturer>();

        HSSFWorkbook wb = null;
        try {
            wb = FileUtils.getWorkbook(request);
        } catch (Exception ex) {
            Logger.getLogger(ManageLecturerController.class.getName())
                    .log(Level.SEVERE, null, ex);
            return new ExecuteResult(false,
                    "Lỗi trong quá trình import từ file: " + ex.toString());
        }
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
                Lecturer l = initLecturerFromHSSFRow(rowTemp);
                lecturers.add(l);
            }
        }

        if (!lecturers.isEmpty()) {
            return lecturerService.addLecturers(lecturers, importAsPossible,
                                                request.getSession().getId());
        }
        return new ExecuteResult(false, "Không thể đọc thông tin.");
    }
    
    private Lecturer initLecturerFromHSSFRow(HSSFRow rowTemp) {
        try {
            Lecturer l = new Lecturer();
            //STT	Mã GV	Họ và tên	Mã Khoa	Ngày sinh	Quê quán
            //CMND	Điện thoại	Email	Giới tính	Học Hàm	Học Vị	Ghi chú

            HSSFCell cellTemp = null;

            cellTemp = rowTemp.getCell(1);
            String id = cellTemp.getStringCellValue();
            l.setId(id);

            cellTemp = rowTemp.getCell(2);
            String fullName = cellTemp.getStringCellValue();
            l.setFullName(fullName);

            cellTemp = rowTemp.getCell(3);
            String facultyId = cellTemp.getStringCellValue();
            l.setFacultyCode(facultyId);

            cellTemp = rowTemp.getCell(4);
            //String birthDay = cellTemp.getStringCellValue();
            l.setBirthday(new Date());

            cellTemp = rowTemp.getCell(5);
            String address = cellTemp.getStringCellValue();
            l.setAddress(address);

            cellTemp = rowTemp.getCell(6);
            cellTemp.setCellType(HSSFCell.CELL_TYPE_STRING);
            String cmnd = cellTemp.getStringCellValue();
            l.setIdentityCard(cmnd);

            cellTemp = rowTemp.getCell(7);
            cellTemp.setCellType(HSSFCell.CELL_TYPE_STRING);
            String phone = cellTemp.getStringCellValue();
            l.setPhone(phone);

            cellTemp = rowTemp.getCell(8);
            String email = cellTemp.getStringCellValue();
            l.setEmail(email);

            //Email	Giới tính	Học Hàm	Học Vị	Ghi chú
            cellTemp = rowTemp.getCell(9);
            String gender = cellTemp.getStringCellValue();
            l.setGender(gender);

            cellTemp = rowTemp.getCell(10);
            String hocHam = cellTemp.getStringCellValue();
            l.setHocHam(hocHam);

            cellTemp = rowTemp.getCell(11);
            String hocVi = cellTemp.getStringCellValue();
            l.setHocVi(hocVi);

            cellTemp = rowTemp.getCell(12);
            String note = cellTemp.getStringCellValue();
            l.setNote(note);

            return l;
        } catch (Exception ex) {
            return null;
        }
    }

    private void doRetryImportFromFile(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            ExecuteResult er = lecturerService.addLecturers(
                            null, true, request.getSession().getId());
            writeOutResponseForRetryImportFromFile(response.getWriter(), er);
        } catch (IOException ex) {
            Logger.getLogger(ManageLecturerController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    private void writeOutResponseForRetryImportFromFile(
            PrintWriter out, ExecuteResult er) {
        if (!er.isIsSucces()) {
            out.println(er.getMessage());
        } else {
            List<Lecturer> lecturers = (List<Lecturer>) er.getData();
            if ((lecturers == null) || lecturers.isEmpty()) {
                out.println("Không có GV nào được thêm");
            } else {
                out.println("<i>Thêm thành công các GV:</i>");
                out.println("<table class=\"general-table\" style=\"width: 450px;\">");
                out.println("<tr><th>STT</th><th>Mã GV</th><th>Họ và tên</th><th>CMND</th></tr>");
                for (int i = 0; i < lecturers.size(); i++) {
                    out.println("<tr>"
                            + "<td>" + (i+1) + "</td>"
                            + "<td>" + lecturers.get(i).getId() + "</td>"
                            + "<td>" + lecturers.get(i).getFullName() + "</td>"
                            + "<td>" + lecturers.get(i).getIdentityCard() + "</td>"
                            + "</tr>");
                }
                out.println("</table>");
            }
        }
    }

    //
    // EDIT LECTURER
    //
    private void doPrepareDataForEditLecturer(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String magv = request.getParameter("magv");
        Lecturer s = lecturerService.getLecturer(magv);
        
        HttpSession session = request.getSession();
        session.setAttribute("lecturer", s);
        
        List<Faculty> faculties = (List<Faculty>) session.getAttribute("faculties");
        if ((faculties == null) || faculties.isEmpty()) {
            faculties = pdtService.getAllFaculty();
            session.setAttribute("faculties", faculties);
        }
        
        String path = "./jsps/PDT/EditLecturer.jsp";
        response.sendRedirect(path);
    }

    private void doUpdateLecturer(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        // Get data from request instance.
        Lecturer s = getLecturerFromRequest(request);
        ExecuteResult er = lecturerService
                .updateLecturer(request.getSession().getId(), s);
        
        PrintWriter out = response.getWriter();
        out.println(er.getMessage());
    }

    private void doDeleteMulti(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        /** Data hold list of student tobe deleted
         in format: magv1 - magv2 - magvn**/
        String data = request.getParameter("data");
        if (StringUtils.isEmpty(data)) {
            out.append("error " + "Không lấy được dữ liệu từ Client."
                    + " Vui lòng thử lại.");
            return;
        }
        
        List<Lecturer> lecturers = new ArrayList<Lecturer>(10);
        List<String> magv = Arrays.asList(data.split("-"));
        
        String sessionId = request.getSession().getId();
        ExecuteResult er = lecturerService.deleteLecturers(
                            magv, false, sessionId);
        
        if (!er.isIsSucces()) {
            out.append("error " + er.getMessage());
        } else {
            lecturers = lecturerService.getLecturers(sessionId);
            if ((lecturers != null) && !lecturers.isEmpty()) {
                writeOutListLecturers(out, lecturers,1);
            }
        }
    }
 private void doPaggingData(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        try {
            String page = request.getParameter("currentpage");
            currentPage = Integer.parseInt(page);
        } catch(Exception ex) {
            currentPage = 1;
        }
        
        List<Lecturer> lecturers = lecturerService
                .getLecturers(currentPage, request.getSession().getId());
        if ((lecturers != null) && !lecturers.isEmpty()) {
            writeOutListLecturers(response.getWriter(), lecturers, currentPage);
        }
    }
    
    //#############################################
    public enum ManageLecturerSupport {
        DEFAULT("default"), // List first page of class opened.
        SEARCH("search"),
        PRE_IMPORT("pre-import-lecturer"),
        DELETE_ONE("delete-one"),
        DELETE_MULTI("delete-multi"),
        VALIDATE_ADD_ONE("validate-add-one"),
        ADD_ONE("add-one"),
        CANCEL_ADD_ONE("cancel-add-one"),
        IMPORT_FROM_FILE("importfromfile"),
        RETRY_IMPORT_FROM_FILE("retry-import-from-file"),
        PRE_EDIT("editlecturer"),
        UPDATE("update"),
        SORT("sort"),
        PAGGING("pagging");
        
        private String description;
        ManageLecturerSupport(String description) {
            this.description = description;
        }
        
        public String getValue() {
            return description;
        }
    }
}
