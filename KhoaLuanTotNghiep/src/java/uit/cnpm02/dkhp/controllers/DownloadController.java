package uit.cnpm02.dkhp.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uit.cnpm02.dkhp.DAO.StudyResultDAO;
import java.util.*;
import uit.cnpm02.dkhp.model.Registration;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import uit.cnpm02.dkhp.DAO.ClassDAO;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.FacultyDAO;
import uit.cnpm02.dkhp.DAO.RegistrationDAO;
import uit.cnpm02.dkhp.DAO.StudentDAO;
import uit.cnpm02.dkhp.DAO.SubjectDAO;
import uit.cnpm02.dkhp.DAO.TrainClassDAO;
import uit.cnpm02.dkhp.model.Faculty;
import uit.cnpm02.dkhp.model.Student;
import uit.cnpm02.dkhp.model.Class;
import uit.cnpm02.dkhp.model.RegistrationID;
import uit.cnpm02.dkhp.model.StudyResult;
import uit.cnpm02.dkhp.model.Subject;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.model.TrainClassID;
import uit.cnpm02.dkhp.service.IFileUploadService;
import uit.cnpm02.dkhp.service.IStudentService;
import uit.cnpm02.dkhp.service.impl.FileUploadServiceImpl;
import uit.cnpm02.dkhp.service.impl.StudentServiceImpl;
import uit.cnpm02.dkhp.utilities.Constants;

/**
 *
 * @author thanh
 */
@WebServlet(name = "DownloadController", urlPatterns = {"/DownloadController"})
public class DownloadController extends HttpServlet {
    private IFileUploadService fuService = new FileUploadServiceImpl();
    
    private HSSFCellStyle style = null; //Normal style (No border)
    /*private HSSFCellStyle styleTLBR = null; //(Top Left Bottom Right border)
    private HSSFCellStyle styleTL = null; // (Top left border)
    private HSSFCellStyle styleTR = null; //
    private HSSFCellStyle styleL = null; //
    private HSSFCellStyle styleR = null; //
    private HSSFCellStyle styleB = null; //
    private HSSFCellStyle styleBL = null; //
    private HSSFCellStyle styleBR = null; //*/
    
    /** 
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        try {
            String action = request.getParameter("action");
            if (action.equalsIgnoreCase("studentresult")) {
                exportStudyResult(request, response);
            } else if (action.equalsIgnoreCase("studentRegistry")) {
                exportRegistration(request, response);
            } else if (action.equalsIgnoreCase("class-list-student")) {
                downloadScoreFileExample(request, response);
            } else if (action.equalsIgnoreCase("download-empty-file-score")) {
                downloadEmptyScoreFile(request, response);
            } else if (action.equalsIgnoreCase("download-student-trainclass-report")) {
                // Download file incoude all trainclass registered by
                // specified student.
                downloadStudentTrainClassReport(request, response);
            } else if (action.equalsIgnoreCase("download-list-student-in-trainclass")) {
                // Download file include list student in specified trainclass
                downloadListStudentInTrainClass(request, response);
            } else if (action.equalsIgnoreCase("download-news-attached-file")) {
                downloadNewsAttachedFile(request, response);
            }
        } finally {
        }
    }

    private void setSubjectAndLecturer(List<TrainClass> trainClass) throws Exception {
        for (int i = 0; i < trainClass.size(); i++) {
            trainClass.get(i).setSubjectName(DAOFactory.getSubjectDao().findById(trainClass.get(i).getSubjectCode()).getSubjectName());
            trainClass.get(i).setLectturerName(DAOFactory.getLecturerDao().findById(trainClass.get(i).getLecturerCode()).getFullName());
            trainClass.get(i).setNumTC(DAOFactory.getSubjectDao().findById(trainClass.get(i).getSubjectCode()).getnumTC());
        }
    }
    private void exportRegistration(HttpServletRequest req,
            HttpServletResponse resp) throws Exception {
        try {
            String user = req.getParameter("mssv");
            List<Registration> registration = DAOFactory.getRegistrationDAO().findAllByStudentCode(user);
            ArrayList<TrainClass> registried = new ArrayList<TrainClass>();
            for (int i = 0; i < registration.size(); i++) {
                String classCode = registration.get(i).getId().getClassCode();
                TrainClassID trainClassId = new TrainClassID(classCode,
                        Constants.CURRENT_YEAR, Constants.CURRENT_SEMESTER);
                TrainClass trainClass = DAOFactory.getTrainClassDAO().findById(trainClassId);
                registried.add(trainClass);
            }
            setSubjectAndLecturer(registried);
            Student student = DAOFactory.getStudentDao().findById(user);
            Class classes = DAOFactory.getClassDao().findById(student.getClassCode());
            Faculty faculty = DAOFactory.getFacultyDao().findById(student.getFacultyCode());

            HSSFWorkbook hwb = new HSSFWorkbook();
            HSSFSheet sheet = hwb.createSheet("Dang ky hoc phan " + user);
            HSSFCellStyle style = hwb.createCellStyle();
            HSSFCellStyle style1 = hwb.createCellStyle();
            HSSFCellStyle style2 = hwb.createCellStyle();
            HSSFCellStyle style3 = hwb.createCellStyle();

            HSSFFont font = hwb.createFont();
            initialFont(font, HSSFFont.FONT_ARIAL, 18,
                    HSSFFont.BOLDWEIGHT_BOLD, HSSFColor.RED.index);
            style2.setFont(font);

            int nrow = 1, i = 0;

            String[] infoStudent = {"Họ Và Tên: " + student.getFullName(),
                "MSSV: " + student.getId(),
                "Lớp: " + classes.getClassName(),
                "Khoa: " + faculty.getFacultyName()};
            HSSFRow row1 = null;
            row1 = sheet.createRow((short) +(nrow++));
            createCell(row1, 6, style2, HSSFCell.CELL_TYPE_STRING, "Bảng đăng ký môn học");
            row1 = sheet.createRow((short) +(nrow++));
            createCell(row1, 6, style2, HSSFCell.CELL_TYPE_STRING, "Học kỳ " 
                    + Constants.CURRENT_SEMESTER
                    + " năm học " + Constants.CURRENT_YEAR);

            row1 = sheet.createRow((short) +(nrow++));
            row1 = sheet.createRow((short) +(nrow++));
            HSSFFont font1 = hwb.createFont();
            initialFont(font1, HSSFFont.FONT_ARIAL, 12,
                    HSSFFont.BOLDWEIGHT_BOLD, HSSFColor.BLUE.index);
            style.setFont(font1);
            for (i = 0; i < infoStudent.length; i++) {
                row1 = sheet.createRow((short) +(nrow++));
                createCell(row1, 0, style, HSSFCell.CELL_TYPE_STRING, infoStudent[i]);
            }

            row1 = sheet.createRow((short) +(nrow++));
            row1 = sheet.createRow((short) +(nrow++));
            row1 = sheet.createRow((short) +(nrow++));

            String[] title = {"STT", "Mã lớp", "Môn học", "Giảng viên", "Tín chỉ"};
            row1 = sheet.createRow((short) +(nrow++));
            HSSFFont font2 = hwb.createFont();
            initialFont(font2, HSSFFont.FONT_ARIAL, 12,
                    HSSFFont.BOLDWEIGHT_BOLD, HSSFColor.GREEN.index);
            style1.setFont(font2);
            for (i = 0; i < title.length; i++) {
                createCell(row1, (i+1), style1, HSSFCell.CELL_TYPE_STRING, title[i]);
            }
            String classCode;
            String subName;
            String lecturer;
            int numTC;
            int sumTC = 0;
            for (i = 0; i < registried.size(); i++) {
                HSSFRow row = sheet.createRow((short) +(nrow++));

                classCode = registried.get(i).getId().getClassCode();
                subName = registried.get(i).getSubjectName();
                lecturer = registried.get(i).getLectturerName();
                numTC = registried.get(i).getNumTC();
                sumTC += numTC;
                String[] info = {Integer.toString(i), classCode,
                    subName, lecturer, Integer.toString(numTC)};

                for (int j = 0; j < info.length; j++) {
                    createCell(row, (j+1), style3, HSSFCell.CELL_TYPE_STRING, info[j]);
                    sheet.autoSizeColumn(j + 1);
                }
            }
            HSSFRow row = sheet.createRow((short) +(nrow++));
            createCell(row, 4, style3, HSSFCell.CELL_TYPE_STRING, "Tổng số tín chỉ");
            createCell(row, 5, style3, HSSFCell.CELL_TYPE_STRING, sumTC +"");

            FileOutputStream fileOut =
                    new FileOutputStream("dangkyhocphan" + user + ".xls");
            hwb.write(fileOut);
            fileOut.close();
            downloadFile("dangkyhocphan" + user + ".xls", resp);

        } catch (Exception ex) {
            String path = "./jsps/Message.jsp";
            resp.sendRedirect(path);
        }

    }
    private void exportStudyResult(HttpServletRequest req,
            HttpServletResponse resp) throws IOException {
        try {
            SubjectDAO subjectDao = DAOFactory.getSubjectDao();
            //get info
            StudyResultDAO studyResultDao = DAOFactory.getStudyResultDao();
            String user = req.getParameter("mssv");
            List<StudyResult> studyResult = studyResultDao
                    .findByOther("MSSV", user, "NamHoc, HocKy", "ASC");
            ClassDAO classDao = DAOFactory.getClassDao();
            FacultyDAO facultyDao = DAOFactory.getFacultyDao();
            StudentDAO studentDao = DAOFactory.getStudentDao();
            Student student = studentDao.findById(user);
            Class classes = classDao.findById(student.getClassCode());
            Faculty faculty = facultyDao.findById(student.getFacultyCode());
            setSubjectName(studyResult);
            int numTC = getNumTC(studyResult);
            float averageMark = getAverageMark(studyResult);

            //create file excel
            HSSFWorkbook hwb = new HSSFWorkbook();
            HSSFSheet sheet = hwb.createSheet("Bang Diem SV " + user);
            HSSFCellStyle style = hwb.createCellStyle();
            HSSFCellStyle style1 = hwb.createCellStyle();
            HSSFCellStyle style2 = hwb.createCellStyle();
            HSSFCellStyle style3 = hwb.createCellStyle();

            HSSFFont font = hwb.createFont();
            initialFont(font, HSSFFont.FONT_ARIAL, 20,
                    HSSFFont.BOLDWEIGHT_BOLD, HSSFColor.RED.index);
            style2.setFont(font);

            int nrow = 1, i = 0;

            HSSFRow row1 = null;
            row1 = sheet.createRow((short) +(nrow++));
            createCell(row1, 2, style2, HSSFCell.CELL_TYPE_STRING, "BẢNG ĐIỂM SINH VIÊN");

            row1 = sheet.createRow((short) +(nrow++));
            row1 = sheet.createRow((short) +(nrow++));
            font = hwb.createFont();
            initialFont(font, HSSFFont.FONT_ARIAL, 10,
                    HSSFFont.BOLDWEIGHT_BOLD, HSSFColor.BLACK.index);
            style.setFont(font);
            String[] infoStudent = {"Họ Và Tên: " + student.getFullName(),
                "MSSV: " + student.getId(),
                "Lớp: " + classes.getClassName(),
                "Khoa: " + faculty.getFacultyName(),
                "Số tín chỉ đã tích lũy: " + numTC,
                "Điểm trung bình: " + averageMark};
            for (i = 0; i < 3; i++) {
                row1 = sheet.createRow((short) +(nrow++));
                createCell(row1, 1, style, HSSFCell.CELL_TYPE_STRING, infoStudent[i]); //HoTen, MSSV, Lop
                createCell(row1, 4, style, HSSFCell.CELL_TYPE_STRING, infoStudent[i+3]);// Khoa, So TC, DTB
            }
            
            row1 = sheet.createRow((short) +(nrow++));
            row1 = sheet.createRow((short) +(nrow++));
            row1 = sheet.createRow((short) +(nrow++));
            row1 = sheet.createRow((short) +(nrow++));

            String[] title = {"Năm học", "Học kỳ", "Mã môn học", "Tên môn học", "Số tín chỉ", "Điểm"};
            int[] widths = {125*32, 90*32, 125*32, 250*32, 100*32, 100*32};
            for (i = 0; i < title.length; i++) {
                sheet.setColumnWidth(i, widths[i]);
            }
            //HSSFFont font2 = hwb.createFont();
            HSSFCellStyle headeStyle = hwb.createCellStyle();
            HSSFCellStyle thinBorderStyle = hwb.createCellStyle();
            font = hwb.createFont();
            initialFont(font, HSSFFont.FONT_ARIAL, 12,
                    HSSFFont.BOLDWEIGHT_BOLD, HSSFColor.BLACK.index);
            headeStyle.setFont(font);
            setBorder(headeStyle, CellStyle.BORDER_THICK,
                                    CellStyle.BORDER_THIN,
                                    CellStyle.BORDER_HAIR,
                                    CellStyle.BORDER_MEDIUM);
            setBorder(thinBorderStyle, CellStyle.BORDER_THIN,
                                    CellStyle.BORDER_THIN,
                                    CellStyle.BORDER_THIN,
                                    CellStyle.BORDER_THIN);
            
            for (i = 0; i < title.length; i++) {
                createCell(row1, i, headeStyle, HSSFCell.CELL_TYPE_STRING, title[i]);
            }
            String year;
            int semester;
            String subCode;
            String subName;
            int subTC;
            Float mark;
            for (i = 0; i < studyResult.size(); i++) {
                HSSFRow row = sheet.createRow((short) +(nrow++));

                subCode = studyResult.get(i).getId().getSubjectCode();
                subName = studyResult.get(i).getSubjectName();
                mark = studyResult.get(i).getMark();
                semester = studyResult.get(i).getSemester();
                year = studyResult.get(i).getYear();
                subTC = subjectDao.findById(
                        studyResult.get(i).getId().getSubjectCode()).getnumTC();
                String[] info = {   year,
                                    Integer.toString(semester),
                                    subCode,
                                    subName,
                                    Integer.toString(subTC),
                                    Float.toString(mark)};

                for (int j = 0; j < info.length; j++) {
                    createCell(row, j, style3, HSSFCell.CELL_TYPE_STRING, info[j]);
                    //sheet.autoSizeColumn(j);
                }
            }

            FileOutputStream fileOut = new FileOutputStream("Bangdiem" + user + ".xls");
            hwb.write(fileOut);
            fileOut.close();
            downloadFile("Bangdiem" + user + ".xls", resp);

        } catch (Exception ex) {
            String path = "./jsps/Message.jsp";
            resp.sendRedirect(path);
        }

    }

    private void downloadFile(String filename,
            HttpServletResponse resp) throws IOException {
        try {
            resp.reset();
            resp.setContentType("application/xls");
            resp.setHeader("Content-disposition", "attachment; filename=" + filename);

            FileInputStream in = new FileInputStream(filename);
            int i;
            while ((i = in.read()) != -1) {
                resp.getOutputStream().write(i);
            }
            in.close();
            resp.getOutputStream().flush();
        } catch (Exception e) {
            resp.getWriter().println(e.toString());
        }
    }
    
    private void downloadFile(File file,
            HttpServletResponse resp) throws IOException {
        try {
            resp.reset();
            resp.setContentType("application/xls");
            resp.setHeader("Content-disposition", "attachment; filename=" + file.getName());

            FileInputStream in = new FileInputStream(file);
            int i;
            while ((i = in.read()) != -1) {
                resp.getOutputStream().write(i);
            }
            in.close();
            resp.getOutputStream().flush();
        } catch (Exception e) {
            resp.getWriter().println(e.toString());
        }
    }
    
    private int getNumTC(List<StudyResult> studyResult) throws Exception {
        int numTC = 0;
        SubjectDAO subjectDao = new SubjectDAO();
        for (int i = 0; i < studyResult.size(); i++) {
            numTC += subjectDao.findById(
                    studyResult.get(i).getId().getSubjectCode()).getnumTC();
        }
        return numTC;
    }

    private float getAverageMark(List<StudyResult> studyResult) throws Exception {
        int numtc = getNumTC(studyResult);
        SubjectDAO subjectDao = new SubjectDAO();
        float summark = 0;
        float result = 0;
        for (int i = 0; i < studyResult.size(); i++) {
            summark += (subjectDao.findById(studyResult.get(i).getId()
                    .getSubjectCode()).getnumTC() * studyResult.get(i).getMark());
        }
        result = (float) Math.round(summark * 100 / numtc) / 100;
        return result;
    }
    
    private void setSubjectName(List<StudyResult> studyResult) throws Exception {
        SubjectDAO subjectDao = new SubjectDAO();
        for (int i = 0; i < studyResult.size(); i++) {
            studyResult.get(i).setSubjectName(subjectDao.findById(
                    studyResult.get(i).getId().getSubjectCode()).getSubjectName());
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
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(DownloadController.class.getName())
                    .log(Level.SEVERE, null, ex);
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
    protected void doPost(HttpServletRequest request,
    HttpServletResponse response) throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(DownloadController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    /** 
     * 
     * @return 
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private void downloadScoreFileExample(HttpServletRequest req,
            HttpServletResponse resp) {
        try {
            String trainClassId = req.getParameter("key");
            File file = fuService.getFile(trainClassId + ".xls");
            downloadFile(file, resp);
        } catch (Exception ex) {
            //String path = "./jsps/Message.jsp";
            //resp.sendRedirect(path);
        }
    }
    
    private void downloadEmptyScoreFile(HttpServletRequest req,
            HttpServletResponse resp) {
        String keys = req.getParameter("key");
        String[] values = keys.replace(" ", "").split(";");
        String classId = values[0];
        String year = values[1];
        int semeter = Integer.parseInt(values[2]);
        // if keys not validate
        try {
            RegistrationDAO regDao = DAOFactory.getRegistrationDAO();
            List<Registration> regs = regDao.findByColumNames(
                    new String[]{"MaLopHoc", "NamHoc", "HocKy"},
                    new Object[]{classId, year, semeter});
            
            HSSFWorkbook hwb = new HSSFWorkbook();
            HSSFSheet sheet = hwb.createSheet(classId);
            HSSFCellStyle style = hwb.createCellStyle();
            HSSFFont font = hwb.createFont();
            HSSFRow row = null;
            int currentRow = 1;
            
            // Main table header
            String[] title = {
                "STT", "MSSV", "Hoc va ten", "Diem GK",
                "Diem cuoi ky", "DTB", "Ghi chu"};
            // Inital columns width
            int[] widths = {45*32, 125*32, 150*32, 80*32, 110*32, 70*32, 180*32};
            for (int i = 0; i < title.length; i++) {
                sheet.setColumnWidth(i, widths[i]);
            }
            
            // File header
            
            initialFont(font, HSSFFont.FONT_ARIAL, 18,
                    HSSFFont.BOLDWEIGHT_BOLD, HSSFColor.RED.index);
            style.setFont(font);
            row = sheet.createRow((short) +(currentRow++));
            createCell(row, 3, style, HSSFCell.CELL_TYPE_STRING, "Bang diem");
            row = sheet.createRow((short) +(currentRow++));
            createCell(row, 3, style, HSSFCell.CELL_TYPE_STRING, "Học kỳ " + semeter
                    + " năm học " + year);
            row = sheet.createRow((short) +(currentRow++));
            //row = sheet.createRow((short) +(currentRow++));
            initialFont(font, HSSFFont.FONT_ARIAL, 12,
                    HSSFFont.BOLDWEIGHT_BOLD, HSSFColor.BLUE.index);
            style.setFont(font);
            String[] classInfo = {"Ma mon hoc: " + classId,
                                        "Nam hoc: " + year};
            for (int i = 0; i < classInfo.length; i++) {
                row = sheet.createRow((short) +(currentRow++));
                createCell(row, 4, style, HSSFCell.CELL_TYPE_STRING, classInfo[i]);
            }

            // Some empty row
            row = sheet.createRow((short) +(currentRow++));
            row = sheet.createRow((short) +(currentRow++));
            row = sheet.createRow((short) +(currentRow++));
            row = sheet.createRow((short) +(currentRow++));
            
            // Table's header
            HSSFCellStyle headeStyle = hwb.createCellStyle();
            HSSFCellStyle thinBorderStyle = hwb.createCellStyle();
            initialFont(font, HSSFFont.FONT_ARIAL, 12,
                    HSSFFont.BOLDWEIGHT_BOLD, HSSFColor.BLACK.index);
            headeStyle.setFont(font);
            setBorder(headeStyle, CellStyle.BORDER_THICK,
                                    CellStyle.BORDER_THIN,
                                    CellStyle.BORDER_HAIR,
                                    CellStyle.BORDER_MEDIUM);
            setBorder(thinBorderStyle, CellStyle.BORDER_THIN,
                                    CellStyle.BORDER_THIN,
                                    CellStyle.BORDER_THIN,
                                    CellStyle.BORDER_THIN);

            for (int i = 0; i < title.length; i++) {
                createCell(row, i, headeStyle, HSSFCell.CELL_TYPE_STRING, title[i]);
            }
            // Initial table body
            for (int i = 0; i < regs.size(); i++) {
                String datas[] = initialDataRow(regs.get(i));
                datas[0] = (i + 1) +"";
                row = sheet.createRow((short) +(currentRow++));
                for (int j = 0; j < datas.length; j++) {
                    createCell(row, j, thinBorderStyle, HSSFCell.CELL_TYPE_STRING, datas[j]);
                }
            }
            
            // Inital file for download...
            FileOutputStream fileOut =
                    new FileOutputStream(classId + ".xls");
            hwb.write(fileOut);
            fileOut.close();
            downloadFile(classId + ".xls", resp);
        } catch (Exception ex) {
            //String path = "./jsps/Message.jsp";
            //resp.sendRedirect(path);
        }
    }
    
    private String[] initialDataRow(Registration reg) {
        String[] datas = new String[7];
        StudentDAO sDao = DAOFactory.getStudentDao();
        Student s = null;
        try {
            s = sDao.findById(reg.getId().getStudentCode());
        } catch (Exception ex) {
            Logger.getLogger(DownloadController.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
        datas[1] = reg.getId().getStudentCode();
        datas[2] = (s == null ? "" : s.getFullName());
        datas[3] = "";
        datas[4] = "";
        datas[5] = "";
        datas[6] = "";
        return datas;
    }
    
    private void createCell(HSSFRow row, int index,
            HSSFCellStyle style, int cellType, String value) {
        HSSFCell cell = row.createCell((short) +index);
        cell.setCellStyle(style);
        cell.setCellType(cellType);
        cell.setCellValue(value);
    }
    
    private void initialFont(HSSFFont font, String fontName,
            int fontHeightInPoint, int boldWeight, int color) {
        if (font == null) {
            return;
        }
        font.setFontName(fontName);
        font.setFontHeightInPoints((short)fontHeightInPoint);
        font.setBoldweight((short)boldWeight);
        font.setColor((short)color);
            
    }
    
    private void setBorder(HSSFCellStyle style,
            int top, int bottom, int left, int right) {
        style.setBorderBottom((short)bottom);
        style.setBorderLeft((short)left);
        style.setBorderRight((short)right);
        style.setBorderTop((short)top);
    }

    /**
     * Download file include informtaion about student registration.
     * @param request
     * @param response 
     */
    
    private void downloadStudentTrainClassReport(HttpServletRequest req,
            HttpServletResponse resp) {
        String mssv = req.getParameter("mssv");
        /*if (StringUtils.isEmpty(mssv)) {
            
        }*/

        try {
            StudentDAO studentDao = DAOFactory.getStudentDao();
            Student student = studentDao.findById(mssv);
            RegistrationDAO regDao = DAOFactory.getRegistrationDAO();
            List<Registration> regs = regDao.findByColumName(
                    "MSSV", mssv);
            
            HSSFWorkbook hwb = new HSSFWorkbook();
            HSSFSheet sheet = hwb.createSheet(mssv +"");
            HSSFCellStyle style = hwb.createCellStyle();
            HSSFFont font = hwb.createFont();
            HSSFRow row = null;
            int currentRow = 1;
            
            // Main table header
            String[] title = {
                "STT", "Mã lớp", "Tên MH", "Học kỳ", "Năm học"};
            // Inital columns width
            int[] widths = {45*32, 120*32, 225*32, 80*32, 150*32};
            for (int i = 0; i < title.length; i++) {
                sheet.setColumnWidth(i, widths[i]);
            }
            
            // File header
            initialFont(font, HSSFFont.FONT_ARIAL, 18,
                    HSSFFont.BOLDWEIGHT_BOLD, HSSFColor.RED.index);
            style.setFont(font);
            row = sheet.createRow((short) +(currentRow++));
            row = sheet.createRow((short) +(currentRow++));
            initialFont(font, HSSFFont.FONT_ARIAL, 12,
                    HSSFFont.BOLDWEIGHT_BOLD, HSSFColor.BLUE.index);
            style.setFont(font);
            String[] classInfo = {"MSSV: " + mssv,
                                        "Họ và tên: " + student.getFullName()};
            for (int i = 0; i < classInfo.length; i++) {
                row = sheet.createRow((short) +(currentRow++));
                createCell(row, 2, style, HSSFCell.CELL_TYPE_STRING, classInfo[i]);
            }

            // Some empty row
            row = sheet.createRow((short) +(currentRow++));
            row = sheet.createRow((short) +(currentRow++));
            row = sheet.createRow((short) +(currentRow++));
            
            // Table's header
            HSSFCellStyle headeStyle = hwb.createCellStyle();
            HSSFCellStyle thinBorderStyle = hwb.createCellStyle();
            row = sheet.createRow((short) +(currentRow++));
            initialFont(font, HSSFFont.FONT_ARIAL, 12,
                    HSSFFont.BOLDWEIGHT_BOLD, HSSFColor.BLACK.index);
            headeStyle.setFont(font);
            setBorder(headeStyle, CellStyle.BORDER_THICK,
                                    CellStyle.BORDER_THIN,
                                    CellStyle.BORDER_HAIR,
                                    CellStyle.BORDER_MEDIUM);
            setBorder(thinBorderStyle, CellStyle.BORDER_THIN,
                                    CellStyle.BORDER_THIN,
                                    CellStyle.BORDER_THIN,
                                    CellStyle.BORDER_THIN);

            for (int i = 0; i < title.length; i++) {
                createCell(row, i, headeStyle, HSSFCell.CELL_TYPE_STRING, title[i]);
            }
            // Initial table body
            TrainClassDAO tcDao = DAOFactory.getTrainClassDAO();
            SubjectDAO subDao = DAOFactory.getSubjectDao();
            for (int i = 0; i < regs.size(); i++) {
                RegistrationID regId = regs.get(i).getId();
                TrainClassID tcId = new TrainClassID(regId.getClassCode(),
                        regId.getYear(), regId.getSemester());
                TrainClass tc = tcDao.findById(tcId);
                
                Subject sub = subDao.findById(tc.getSubjectCode());
                String[] datas = new String[5];
                datas[0] = (i+1) +"";// STT
                datas[1] = regId.getClassCode();//Ma lop
                datas[2] = sub.getSubjectName(); //Ten mon hoc
                datas[3] = tcId.getSemester() +""; // Hoc ky
                datas[4] = tcId.getYear(); // Nam hoc

                row = sheet.createRow((short) +(currentRow++));
                for (int j = 0; j < datas.length; j++) {
                    createCell(row, j, thinBorderStyle, HSSFCell.CELL_TYPE_STRING, datas[j]);
                }
            }
            
            // Inital file for download...
            FileOutputStream fileOut =
                    new FileOutputStream(mssv +"" + ".xls");
            hwb.write(fileOut);
            fileOut.close();
            downloadFile(mssv +"" + ".xls", resp);
        } catch (Exception ex) {
            //String path = "./jsps/Message.jsp";
            //resp.sendRedirect(path);
        }
    }

    /**
     * Input: Train class id: classid, year, semeter
     * Output: XSL file include list student in specified
     * TrainClass
     * @param req
     * @param resp 
     */
    private void downloadListStudentInTrainClass(HttpServletRequest req,
            HttpServletResponse resp) {
        String keys = req.getParameter("key");
        // Value: classID, year, semeter
        String[] values = keys.replace(" ", "").split(";");
        String classId = values[0];
        String year = values[1];
        int semeter = Integer.parseInt(values[2]);
        // if keys not validate
        try {
            
            HSSFWorkbook hwb = new HSSFWorkbook();
            HSSFSheet sheet = hwb.createSheet(classId);
            style = hwb.createCellStyle();
            HSSFFont font = hwb.createFont();
            HSSFRow row = null;
            int currentRow = 1;
            
            // Main table header
            String[] title = {
                "STT", "MSSV", "Họ và tên"};
            // Inital columns width
            int[] widths = {45*32, 125*32, 175*32};
            for (int i = 0; i < title.length; i++) {
                sheet.setColumnWidth(i, widths[i]);
            }
            
            // File header
            
            initialFont(font, HSSFFont.FONT_ARIAL, 18,
                    HSSFFont.BOLDWEIGHT_BOLD, HSSFColor.RED.index);
            style.setFont(font);
            row = sheet.createRow((short) +(currentRow++));
            createCell(row, 3, style, HSSFCell.CELL_TYPE_STRING, "Danh sách SV lớp: " + classId);
            row = sheet.createRow((short) +(currentRow++));
            createCell(row, 3, style, HSSFCell.CELL_TYPE_STRING, "Học kỳ " + semeter
                    + " năm học " + year);
            row = sheet.createRow((short) +(currentRow++));
            //row = sheet.createRow((short) +(currentRow++));
            initialFont(font, HSSFFont.FONT_ARIAL, 12,
                    HSSFFont.BOLDWEIGHT_BOLD, HSSFColor.BLUE.index);
            style.setFont(font);
            String[] classInfo = {"Mã môn học: " + classId,
                                        "Năm học: " + year};
            for (int i = 0; i < classInfo.length; i++) {
                row = sheet.createRow((short) +(currentRow++));
                createCell(row, 4, style, HSSFCell.CELL_TYPE_STRING, classInfo[i]);
            }

            // Some empty row
            row = sheet.createRow((short) +(currentRow++));
            row = sheet.createRow((short) +(currentRow++));
            row = sheet.createRow((short) +(currentRow++));
            
            // Table's header
            HSSFCellStyle headeStyle = hwb.createCellStyle();
            HSSFCellStyle thinBorderStyle = hwb.createCellStyle();
            row = sheet.createRow((short) +(currentRow++));
            initialFont(font, HSSFFont.FONT_ARIAL, 12,
                    HSSFFont.BOLDWEIGHT_BOLD, HSSFColor.BLACK.index);
            headeStyle.setFont(font);
            setBorder(headeStyle, CellStyle.BORDER_THICK,
                                    CellStyle.BORDER_THIN,
                                    CellStyle.BORDER_HAIR,
                                    CellStyle.BORDER_MEDIUM);
            setBorder(thinBorderStyle, CellStyle.BORDER_THIN,
                                    CellStyle.BORDER_THIN,
                                    CellStyle.BORDER_THIN,
                                    CellStyle.BORDER_THIN);

            for (int i = 0; i < title.length; i++) {
                createCell(row, i, headeStyle, HSSFCell.CELL_TYPE_STRING, title[i]);
            }
            
            IStudentService reportService = new StudentServiceImpl();
            List<Student> students = reportService
                .getStudent(classId, year, semeter);
            if (students != null) {
                // Initial table body
                String datas[] = new String[3];
                for (int i = 0; i < students.size(); i++) {
                    datas[0] = (i + 1) +"";
                    datas[1] = students.get(i).getId();
                    datas[2] = students.get(i).getFullName();
                    row = sheet.createRow((short) +(currentRow++));
                    for (int j = 0; j < datas.length; j++) {
                        createCell(row, j, thinBorderStyle, HSSFCell.CELL_TYPE_STRING, datas[j]);
                    }
                }
            }
            
            // Inital file for download...
            FileOutputStream fileOut =
                    new FileOutputStream("DSSV-" + classId + ".xls");
            hwb.write(fileOut);
            fileOut.close();
            downloadFile("DSSV-" + classId + ".xls", resp);
        } catch (Exception ex) {
            //String path = "./jsps/Message.jsp";
            //resp.sendRedirect(path);
        }
    }

    private void downloadNewsAttachedFile(HttpServletRequest request,
            HttpServletResponse response) {
        try {
            String filename = request.getParameter("filename");
            File file = fuService.getFile(filename);
            if (file != null)
                downloadFile(file, response);
        } catch (Exception ex) {
            //String path = "./jsps/Message.jsp";
            //resp.sendRedirect(path);
        }
    }
    
}
