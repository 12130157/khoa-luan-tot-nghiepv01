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
import uit.cnpm02.dkhp.DAO.StudentDAO;
import uit.cnpm02.dkhp.model.Student;
import uit.cnpm02.dkhp.utilities.Constants;
import uit.cnpm02.dkhp.utilities.FileUtils;

/**
 *
 * @author LocNguyen
 */
@WebServlet(name = "ManageStudentController", urlPatterns = {"/ManageStudentController"})
public class ManageStudentController extends HttpServlet {

    private StudentDAO studentDao = DAOFactory.getStudentDao();
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
    protected void processRequest(HttpServletRequest request,
                                            HttpServletResponse response)
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
            if (action.equalsIgnoreCase("liststudent")) {
                listStudent(request, response);
                String path = "./jsps/PDT/ListStudent.jsp";
                response.sendRedirect(path);
            } else if (action.equalsIgnoreCase("editstudent")) {
                editStudent(request, response);
                String path = "./jsps/PDT/EditStudent.jsp";
                response.sendRedirect(path);
            } else if (action.equalsIgnoreCase("import")) {
                //Data input in format: student1; student2; student 3 ...
                //Student: mssv, hoten, ...
                String result = importStudentFromDataString(datas);
                session.setAttribute("error", result);
                String path = "./jsps/PDT/ImportStudent.jsp";
                response.sendRedirect(path);
            } else if (action.equalsIgnoreCase("importfromfile")) {
                String result = importStudentFromFile(request, response);
                session.setAttribute("error", result);
                String path = "./jsps/PDT/ImportStudent.jsp";
                response.sendRedirect(path);
            } else if (action.equalsIgnoreCase("delete")) {
                String result = deleteStudent(request, response);
                session.setAttribute("error", result);
                listStudent(request, response);
                String path = "./jsps/PDT/ListStudent.jsp";
                response.sendRedirect(path);
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
     * 
     * @param students
     * @throws Exception 
     */
    private void updateStudent(List<Student> students) throws Exception {
        if (studentDao != null) {
            studentDao.update(students);
        } else {
            throw new Exception("Couldn't create Student DAO");
        }
    }

    /**
     * Delete list student.
     * @param students
     * @throws Exception 
     */
    private String deleteStudent(HttpServletRequest req,
                        HttpServletResponse resp) throws Exception {
        String result = "";
        String data = (String) req.getParameter("data");

        if ((data == null) || data.isEmpty()) {
            return "Xóa không thành công.";
        }
        String[] mssv = data.split("-");
        if (studentDao == null) {
            studentDao = DAOFactory.getStudentDao();
        }

        List<Student> students = studentDao.findByIds(mssv);
        if (!students.isEmpty()) {
            studentDao.delete(students);
            result = "Đã xóa " + students.size() + " sinh viên";
            return result;
        }
        result = "Chưa xóa SV nào.";
        return result;
    }

    /**
     * Import data form a String
     * @param datas data
     * @return
     * @throws Exception 
     */
    private String importStudentFromDataString(String datas) throws Exception {
        if ((datas == null) || (datas.length() < 1)) {
            return "Vui lòng kiểm tra lại dữ liệu nhập";
        }

        List<Student> students = new ArrayList<Student>(10);
        String[] studentsStr = datas.split(";");
        if (studentsStr != null) {
            for (int i = 0; i < studentsStr.length; i++) {
                String[] studentDetail = studentsStr[i].split(",");
                Student s = new Student();
                s.setId(studentDetail[0]);                        //MSSV        0
                s.setFullName(studentDetail[1]);                  //Ho Ten      1
                Date startDate = dateFormat.parse(studentDetail[2]);
                s.setBirthday(startDate);                         //Ngay Sinh   2 
                s.setGender(studentDetail[3]);                    //Gioi Tinh      3
                s.setIdentityNumber(studentDetail[4]);            //CMND   4
                s.setHomeAddr(studentDetail[5]);                  //Que quan       5
                s.setAddress(studentDetail[6]);                   //Dia chi        6
                s.setPhone(studentDetail[7]);                     //Dien thoai     7
                s.setEmail(studentDetail[8]);                     //Email          8
                s.setClassCode(studentDetail[9]);                 //Lop           9
                s.setFacultyCode(studentDetail[10]);              //Khoa          10
                s.setCourseCode(studentDetail[11]);               //KhoaHoc       11
                s.setStatus(studentDetail[12]);                   //TinhTrang     12
                s.setStudyLevel(studentDetail[13]);               //Bac hoc       13
                Date birthDay = dateFormat.parse(studentDetail[14]);
                s.setDateStart(birthDay);                         //Ngay Nhap Hoc 14
                s.setStudyType(studentDetail[15]);                //Loai hinh hoc 15
                s.setNote(studentDetail[16]);                     //Ghi chu       16

                students.add(s);
            }
        }

        Collection<String> id_Students = DAOFactory.getStudentDao().addAll(students);

        if (id_Students == null) {
            return "Thêm không thành công";
        }

        return "Thêm thành công.";
    }

    /**
     * Import data from file
     * Data format must follow a specified rule.
     * @param request request object
     * @param response respone object
     * @return string
     * @throws Exception 
     */
    private String importStudentFromFile(HttpServletRequest request,
                            HttpServletResponse response) throws Exception {
        List<Student> students = new ArrayList<Student>();
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
                    Student s = initStudentFromHSSFRow(rowTemp);
                    students.add(s);
                }
            }

            Collection<String> id_Students = DAOFactory.getStudentDao()
                                                        .addAll(students);
            if (id_Students == null) {
                return "Thêm không thành công";
            }
            return "Thêm thành công.";

        } catch (Exception ex) {
            throw ex;
        }
    }

    private Student initStudentFromHSSFRow(HSSFRow rowTemp) {
        try {
            Student s = new Student();
            //STT-MSSV-Họ và tên-Ngày sinh-Giới tính-CMND-Quê quán
            //-Địa chỉ-Điện thoại-Email-Mã lớp-Mã khoa-Mã khóa học-Tình trạng
            //-Bậc học-Ngày nhập học-Loại hình học-Ghi chú
            HSSFCell cellTemp = null;

            cellTemp = rowTemp.getCell(1);
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
            s.setStatus(status);

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
     * This function will be called at the first time go
     * to manager student page.
     */
    private void listStudent(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        List<Student> students;
        String searchType = (String) request.getParameter("searchtype");
        String searchValue = (String) request.getParameter("searchvalue");
        String ajaxRespone = (String) request.getParameter("ajax");
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
            students = studentDao.findAll(rowPerPage, currentPage, "HoTen", null);
        } else {
            String searchTypeStr = "";
            if (searchType.equals("name")) {
                searchTypeStr = "HoTen";
            } else if (searchType.equals("clazz")) {
                searchTypeStr = "MaLop";
            } else if (searchType.equals("course")) {
                searchTypeStr = "MaKhoa";
            }
            students = studentDao.findAll(rowPerPage, currentPage,
                    searchTypeStr, "'%" + searchValue + "%'", "HoTen", null);
        }
        if ((ajaxRespone == null)
                || ajaxRespone.isEmpty()
                || ajaxRespone.equals("false")) {
            HttpSession session = request.getSession();
            session.setAttribute("liststudent", students);
        } else {
            PrintWriter out = response.getWriter();
            String respStr = "<tr id=\"tableliststudent-th\">"
                    + "<td><INPUT type=\"checkbox\" name=\"chkAll\""
                    + " onclick=\"selectAll('tableliststudent')\" /></td>"
                    + "<td> STT </td>"
                    + "<td> MSSV </td>"
                    + "<td> Họ Tên </td>"
                    + "<td> Lớp </td>"
                    + "<td> Khoa </td>"
                    + "<td> Ngày sinh </td>"
                    + "<td> Giới tính </td>"
                    + "<td> Loại </td>"
                    + "<td> Sửa </td>"
                    + "<td> Xóa </td>"
                    + "</tr>";
            out.println(respStr);
            for (int i = 0; i < students.size(); i++) {
                respStr = "<tr>"
                        + "<td><INPUT type=\"checkbox\" name=\"chk" + i + "\"/></td>"
                        + "<td> " + (i + 1) + " </td>"
                        + "<td> " + students.get(i).getId() + "</td> "
                        + "<td> " + students.get(i).getFullName() + "</td>"
                        + "<td> " + students.get(i).getClassCode() + "</td>"
                        + "<td> " + students.get(i).getFacultyCode() + "</td>"
                        + "<td> " + students.get(i).getBirthday() + "</td>"
                        + "<td> " + students.get(i).getGender() + "</td>"
                        + "<td> " + students.get(i).getStudyType() + "</td>"
                        + "<td> <a href=\"../../ManageStudentController?function=editstudent&mssv=" 
                                                            + students.get(i).getId() 
                                                            + "\">Sửa</a></td>"
                        + "<td> <a href=\"../../ManageStudentController?function=delete&ajax=true&data="
                                                            + students.get(i).getId()
                                                            + "\"> Xóa</a></td>"
                        + "</tr>";
                out.println(respStr);
            }
            out.close();
        }
    }

    private void editStudent(HttpServletRequest request,
                        HttpServletResponse response) throws Exception {
        String mssv = request.getParameter("mssv");
        Student s = studentDao.findById(mssv);
        HttpSession session = request.getSession();
        session.setAttribute("student", s);
    }

    private int getNumberPage() throws Exception {
        int rows = studentDao.getRowsCount();

        int rowsPerPage = Constants.ELEMENT_PER_PAGE_DEFAULT;
        if (rows % rowsPerPage == 0) {
            numPage = rows / rowsPerPage;
        } else {
            numPage = rows / rowsPerPage + 1;
        }
        return numPage;
    }
}
