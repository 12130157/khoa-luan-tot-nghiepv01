package uit.cnpm02.dkhp.controllers.PDT;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import uit.cnpm02.dkhp.DAO.StudentDAO;
import uit.cnpm02.dkhp.DAO.StudyResultDAO;
import uit.cnpm02.dkhp.DAO.SubjectDAO;
import uit.cnpm02.dkhp.DAO.TrainClassDAO;
import uit.cnpm02.dkhp.model.Student;
import uit.cnpm02.dkhp.model.StudyResult;
import uit.cnpm02.dkhp.model.StudyResultID;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.utilities.ExecuteResult;
import uit.cnpm02.dkhp.utilities.MultipartMap;

/**
 *
 * @author LocNguyen
 */
@WebServlet(name = "ManageScoreController", urlPatterns = {"/ManageScoreController"})
public class ManageScoreController extends HttpServlet {

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
                List<String> classDetail = getListRegisteredClass();
                session.setAttribute("list-subject", classDetail);

                response.sendRedirect(path);
            } else if (action.equals("import-score-from-file")) {
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
            Logger.getLogger(ManageScoreController.class.getName()).log(Level.SEVERE, "Error occur inside ManageScoreController", ex);
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
}
