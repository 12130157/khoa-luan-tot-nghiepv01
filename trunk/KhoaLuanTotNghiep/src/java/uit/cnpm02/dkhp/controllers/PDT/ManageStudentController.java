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
import uit.cnpm02.dkhp.utilities.FileUtils;

/**
 *
 * @author LocNguyen
 */
@WebServlet(name = "ManageStudentController", urlPatterns = {"/ManageStudentController"})
public class ManageStudentController extends HttpServlet {
    private StudentDAO studentDao = DAOFactory.getStudentDao();

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");

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
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        try {
            //addStudent(null, out);
            String action = request.getParameter("function");
            String datas = request.getParameter("data");
            if (action.equalsIgnoreCase("liststudent")) {
                listStudent(request, response);
     
                String path="./jsps/PDT/ListStudent.jsp";
                response.sendRedirect(path);
            } else if (action.equalsIgnoreCase("editstudent")) {
                editStudent(request, response);
     
                String path="./jsps/PDT/EditStudent.jsp";
                response.sendRedirect(path);
            } else if (action.equalsIgnoreCase("import")) {
                //Data input in format: student1; student2; student 3 ...
                //Student: mssv, hoten, ...
                String result = importStudentFromDataString(datas);
                out.println(result);
            } else if (action.equalsIgnoreCase("importfromfile")) {
                String result = importStudentFromFile(request, response);
                out.println(result);
            } else {
                // Unknown function request.
            }
        } catch (Exception ex) {
            out.println("Đã xảy ra sự cố: </br>" + ex);
        } finally {
            out.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * 
     * @param students
     * @throws Exception 
     */
    private void updateStudent(List<Student> students) throws Exception {
        StudentDAO studentDao = DAOFactory.getStudentDao();

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
    private void deleteStudent(List<Student> students) throws Exception {
        StudentDAO studentDao = DAOFactory.getStudentDao();

        if (studentDao != null) {

            // Xoa account


            // Xoa cac ban DKHP cua cac SV nay


            //

            studentDao.delete(students);
        } else {
            throw new Exception("Couldn't create Student DAO");
        }
    }

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

    private String importStudentFromFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            HttpSession session = request.getSession();
            int numerror = 0;
            HSSFWorkbook wb = FileUtils.getWorkbook(request, response);
            HSSFSheet sheet = wb.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();
            HSSFRow rowTemp;
            HSSFCell cellTemp;

            String datas = "";
            int cellType;
            String strValue = "";

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
                    //STT-MSSV-Họ và tên-Ngày sinh-Giới tính-CMND-Quê quán
                    //-Địa chỉ-Điện thoại-Email-Mã lớp-Mã khoa-Mã khóa học-Tình trạng
                    //-Bậc học-Ngày nhập học-Loại hình học-Ghi chú
                    String currentData = "";
                    String date = "";
                    for (int j = 1; j < 18; j++) {
                        if (j > 1) {
                            currentData += ",";
                        }
                        cellTemp = rowTemp.getCell(j);
                        if (cellTemp == null) {
                            return "Lỗi đọc file";
                        }

                        if (j == 3 || j == 15) {
                            date = cellTemp.getDateCellValue().toString();
                            currentData += "10/04/1987";//dateFormat.parse(date).toString();
                        } else {
                            cellTemp.setCellType(HSSFCell.CELL_TYPE_STRING);
                            currentData += cellTemp.getStringCellValue();
                        }
                    }

                    datas += currentData + ";";
                }
            }

            return importStudentFromDataString(datas.substring(0, datas.lastIndexOf(';')));
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * This function will be called at the first time go
     * to manager student page.
     */
    private void listStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        List<Student> students = studentDao.findAll();
        
        HttpSession session = request.getSession();
        session.setAttribute("liststudent", students);
    }

    private void editStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String mssv = request.getParameter("mssv");
        Student s = studentDao.findById(mssv);
        HttpSession session = request.getSession();
        session.setAttribute("student", s);
    }
}
