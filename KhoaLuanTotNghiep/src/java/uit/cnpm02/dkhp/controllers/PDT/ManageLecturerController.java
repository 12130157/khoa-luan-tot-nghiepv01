package uit.cnpm02.dkhp.controllers.PDT;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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
import uit.cnpm02.dkhp.model.Lecturer;
import uit.cnpm02.dkhp.utilities.Constants;
import uit.cnpm02.dkhp.utilities.FileUtils;
import uit.cnpm02.dkhp.utilities.ExecuteResult;

/**
 *
 * @author LocNguyen
 */
@WebServlet(name = "ManageLecturerController", urlPatterns = {"/ManageLecturerController"})
public class ManageLecturerController extends HttpServlet {

    private LecturerDAO lecturerDao = DAOFactory.getLecturerDao();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
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
            String datas = request.getParameter("data");
            if (action.equalsIgnoreCase("listlecturer")) {
                listLecturer(request, response);
                String path = "./jsps/PDT/ListLecturer.jsp";
                response.sendRedirect(path);
            } else if (action.equalsIgnoreCase("editlecturer")) {
                editLecturer(request, response);
                String path = "./jsps/PDT/EditLecturer.jsp";
                response.sendRedirect(path);
            } else if (action.equalsIgnoreCase("import")) {
                //Data input in format: lecturer1; lecturer2; lecturer 3 ...
                //lecturer: maGV, hoten, ...
                ExecuteResult result = importLecturerFromDataString(datas);
                session.setAttribute("error", result.getMessage());
                String path = "./jsps/PDT/ImportLecturer.jsp";
                response.sendRedirect(path);
            } else if (action.equalsIgnoreCase("importfromfile")) {
                String result = importLectuererFromFile(request, response);
                session.setAttribute("error", result);
                String path = "./jsps/PDT/ImportLecturer.jsp";
                response.sendRedirect(path);
            } else if (action.equalsIgnoreCase("delete")) {
                ExecuteResult result = deleteLecturer(request, response);
                session.setAttribute("error", result.getMessage());
                if (result.isIsSucces() == true) {
                    listLecturer(request, response);
                }
                String path = "./jsps/PDT/ListLecturer.jsp";
                response.sendRedirect(path);
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

    private void editLecturer(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("mgv");
        Lecturer s = lecturerDao.findById(id);
        HttpSession session = request.getSession();
        session.setAttribute("lecturer", s);
    }

    private ExecuteResult importLecturerFromDataString(String datas) {
        ExecuteResult result = new ExecuteResult(true, datas);

        if ((datas == null) || (datas.length() < 1)) {
            return new ExecuteResult(false, "Vui lòng kiểm tra lại dữ liệu nhập");
        }
        try {
            List<Lecturer> lecturers = new ArrayList<Lecturer>(10);
            String[] lecturerStr = datas.split(";");
            if (lecturerStr != null) {
                for (int i = 0; i < lecturerStr.length; i++) {
                    String[] lecturerDetail = lecturerStr[i].split(",");
                    Lecturer l = new Lecturer();
                    l.setId(lecturerDetail[0]);                        //MSSV        0
                    l.setFullName(lecturerDetail[1]);                  //Ho Ten      1
                    l.setFacultyCode(lecturerDetail[2]);
                    Date startDate = dateFormat.parse(lecturerDetail[3]);
                    l.setBirthday(startDate);                         //Ngay Sinh   2 
                    l.setAddress(lecturerDetail[4]);
                    l.setIdentityCard(lecturerDetail[5]);
                    l.setPhone(lecturerDetail[6]);
                    l.setEmail(lecturerDetail[7]);
                    l.setGender(lecturerDetail[8]);
                    l.setHocHam(lecturerDetail[9]);
                    l.setHocVi(lecturerDetail[10]);
                    l.setNote(lecturerDetail[11]);

                    lecturers.add(l);
                }
            }

            Collection<String> id_Lecturers = DAOFactory.getLecturerDao().addAll(lecturers);
            if (id_Lecturers == null) {
                return new ExecuteResult(false, "Thêm không thành công");
            }

            result.setMessage("Thêm thành công.");
        } catch (Exception ex) {
            return new ExecuteResult(false, "Lỗi: " + ex.toString());
        }

        return result;
    }

    private String importLectuererFromFile(HttpServletRequest request, HttpServletResponse response) {
        List<Lecturer> lecturers = new ArrayList<Lecturer>();
        try {
            HSSFWorkbook wb = FileUtils.getWorkbook(request, response);
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

            //Collection<String> id_Students = DAOFactory.getStudentDao().addAll(students);
            //if (id_Students == null) {
            //    return "Thêm không thành công";
            //}
            return "Thêm thành công.";

        } catch (Exception ex) {
            return "Đã có lỗi xảy ra.";
        }
    }

    private ExecuteResult deleteLecturer(HttpServletRequest request, HttpServletResponse response) {
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
}
