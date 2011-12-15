/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.controllers;

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
import javax.servlet.http.HttpSession;
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
import uit.cnpm02.dkhp.DAO.ClassDAO;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.FacultyDAO;
import uit.cnpm02.dkhp.DAO.StudentDAO;
import uit.cnpm02.dkhp.DAO.SubjectDAO;
import uit.cnpm02.dkhp.model.Faculty;
import uit.cnpm02.dkhp.model.Student;
import uit.cnpm02.dkhp.model.Class;
import uit.cnpm02.dkhp.model.StudyResult;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.model.TrainClassID;
import uit.cnpm02.dkhp.utilities.Constants;

/**
 *
 * @author thanh
 */
@WebServlet(name = "DownloadController", urlPatterns = {"/DownloadController"})
public class DownloadController extends HttpServlet {

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
        HttpSession session = request.getSession();
        try {
          String action=request.getParameter("action");
          if(action.equalsIgnoreCase("studentresult"))
              exportStudyResult(request, response);
          else if(action.equalsIgnoreCase("studentRegistry"))
              exportRegistration(request, response);
        } finally {            
            
        }
    }
    private void setSubjectAndLecturer(List<TrainClass> trainClass) throws Exception{
    for(int i=0;i<trainClass.size();i++){
        trainClass.get(i).setSubjectName(DAOFactory.getSubjectDao().findById(trainClass.get(i).getSubjectCode()).getSubjectName());
        trainClass.get(i).setLectturerName(DAOFactory.getLecturerDao().findById(trainClass.get(i).getLecturerCode()).getFullName());
        trainClass.get(i).setNumTC(DAOFactory.getSubjectDao().findById(trainClass.get(i).getSubjectCode()).getnumTC() );
    }
}
    private void exportRegistration(HttpServletRequest req, HttpServletResponse resp) throws Exception{
     try{
         String user=req.getParameter("mssv");
         List<Registration> registration=DAOFactory.getRegistrationDAO().findAllByStudentCode(user);
         ArrayList<TrainClass> registried=new ArrayList<TrainClass>();
         for(int i=0; i<registration.size();i++){
          String classCode=registration.get(i).getId().getClassCode();
          TrainClassID trainClassId=new TrainClassID(classCode, Constants.CURRENT_YEAR, Constants.CURRENT_SEMESTER);
          TrainClass trainClass=DAOFactory.getTrainClassDAO().findById(trainClassId);
          registried.add(trainClass);
       }
        setSubjectAndLecturer(registried);
         Student student =DAOFactory.getStudentDao().findById(user);
         Class classes = DAOFactory.getClassDao().findById(student.getClassCode());
         Faculty faculty =DAOFactory.getFacultyDao().findById(student.getFacultyCode());
         
          HSSFWorkbook hwb = new HSSFWorkbook();
           HSSFSheet sheet = hwb.createSheet("Dang ky hoc phan " + user);
           HSSFCellStyle style = hwb.createCellStyle();
           HSSFCellStyle style1 = hwb.createCellStyle();
           HSSFCellStyle style2 = hwb.createCellStyle();
           HSSFCellStyle style3 = hwb.createCellStyle();
           
            HSSFFont font = hwb.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setFontHeightInPoints((short) 18);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font.setColor(HSSFColor.RED.index);
            style2.setFont(font);
            
           int nrow = 1, i = 0;
                      
           String[] infoStudent = {"Họ Và Tên: " + student.getFullName(),
                "MSSV: " + student.getId(),
                "Lớp: " +classes.getClassName(),
                "Khoa: " +faculty.getFacultyName()};
            HSSFRow row1 = null;
            HSSFCell cell1 = null;
            
            row1 = sheet.createRow((short) +(nrow++));
            cell1 = row1.createCell((short) +6);
            cell1.setCellStyle(style2);
            cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell1.setCellValue("Bảng đăng ký môn học");
            row1 = sheet.createRow((short) +(nrow++));
            cell1 = row1.createCell((short) +6);
            cell1.setCellStyle(style2);
            cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell1.setCellValue("Học kỳ "+Constants.CURRENT_SEMESTER+" năm học "+Constants.CURRENT_YEAR);

            row1 = sheet.createRow((short) +(nrow++));
            row1 = sheet.createRow((short) +(nrow++));
            HSSFFont font1 = hwb.createFont();
            font1.setFontName(HSSFFont.FONT_ARIAL);
            font1.setFontHeightInPoints((short) 12);
            font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font1.setColor(HSSFColor.BLUE.index);
            style.setFont(font1);
            for (i = 0; i < infoStudent.length; i++) {
                row1 = sheet.createRow((short) +(nrow++));
                cell1 = row1.createCell((short) +0);
                cell1.setCellStyle(style);
                cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell1.setCellValue(infoStudent[i]);
            }

            row1 = sheet.createRow((short) +(nrow++));
            row1 = sheet.createRow((short) +(nrow++));
            row1 = sheet.createRow((short) +(nrow++));
            
            String[] title = {"STT", "Mã lớp", "Môn học", "Giảng viên", "Tín chỉ"};
            row1 = sheet.createRow((short) +(nrow++));
            HSSFFont font2 = hwb.createFont();
            font2.setFontName(HSSFFont.FONT_ARIAL);
            font2.setFontHeightInPoints((short) 12);
            font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font2.setColor(HSSFColor.GREEN.index);
            style1.setFont(font2);
            for (i = 0; i < title.length; i++) {
                cell1 = row1.createCell((short) +(i+1));
                cell1.setCellStyle(style1);
                cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell1.setCellValue(title[i]);
            }
            String classCode;
            String subName;
            String lecturer;
            int numTC;
            int sumTC=0;
            for (i = 0; i < registried.size(); i++) {
                HSSFRow row = sheet.createRow((short) +(nrow++));

                classCode = registried.get(i).getId().getClassCode();
                subName = registried.get(i).getSubjectName();
                lecturer = registried.get(i).getLectturerName();
                numTC=registried.get(i).getNumTC();
                sumTC+=numTC;
                String[] info = {Integer.toString(i), classCode, subName, lecturer, Integer.toString(numTC)};

                HSSFCell cell = null;
                for (int j = 0; j < info.length; j++) {
                    cell = row.createCell((short) +(j+1));
                    cell.setCellStyle(style3);
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(info[j]);
                    sheet.autoSizeColumn(j+1);
               }
            }
            HSSFRow row = sheet.createRow((short) +(nrow++));
            cell1 = row.createCell((short) +(4));
            cell1.setCellStyle(style3);
            cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell1.setCellValue("Tổng số tín chỉ");
            cell1 = row.createCell((short) +(5));
            cell1.setCellStyle(style3);
            cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell1.setCellValue(Integer.toString(sumTC));
            
            FileOutputStream fileOut = new FileOutputStream("dangkyhocphan" + user + ".xls");
            hwb.write(fileOut);
            fileOut.close();
            downloadFile("dangkyhocphan" + user + ".xls", resp);
            
       }catch(Exception ex){
           String path= "./jsps/Message.jsp";
           resp.sendRedirect(path);
       }
           
    }
private void exportStudyResult(HttpServletRequest req, HttpServletResponse resp) throws IOException{
   try{
          SubjectDAO subjectDao=new SubjectDAO();
          //get info
           StudyResultDAO studyResultDao=new StudyResultDAO();
           String user=req.getParameter("mssv");
           List<StudyResult> studyResult=studyResultDao.findByOther("MSSV", user, "NamHoc, HocKy", "ASC");
           ClassDAO classDao = DAOFactory.getClassDao();
           FacultyDAO facultyDao = DAOFactory.getFacultyDao();
           StudentDAO studentDao=DAOFactory.getStudentDao();
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
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setFontHeightInPoints((short) 20);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font.setColor(HSSFColor.RED.index);
            style2.setFont(font);
            
           boolean done = false;
           int nrow = 1, i = 0;
                      
           String[] infoStudent = {"Họ Và Tên: " + student.getFullName(),
                "MSSV: " + student.getId(),
                "Lớp: " +classes.getClassName(),
                "Khoa: " +faculty.getFacultyName(),
                "Số tín chỉ đã tích lũy: " + numTC,
                "Điểm trung bình: " + averageMark};
            HSSFRow row1 = null;
            HSSFCell cell1 = null;
            
            
            
            row1 = sheet.createRow((short) +(nrow++));
            cell1 = row1.createCell((short) +4);
            cell1.setCellStyle(style2);
            cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell1.setCellValue("BẢNG ĐIỂM SINH VIÊN");

            row1 = sheet.createRow((short) +(nrow++));
            row1 = sheet.createRow((short) +(nrow++));
            HSSFFont font1 = hwb.createFont();
            font1.setFontName(HSSFFont.FONT_ARIAL);
            font1.setFontHeightInPoints((short) 12);
            font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font1.setColor(HSSFColor.BLUE.index);
            style.setFont(font1);
            for (i = 0; i < infoStudent.length; i++) {
                row1 = sheet.createRow((short) +(nrow++));
                cell1 = row1.createCell((short) +0);
                cell1.setCellStyle(style);
                cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell1.setCellValue(infoStudent[i]);
            }

            row1 = sheet.createRow((short) +(nrow++));
            row1 = sheet.createRow((short) +(nrow++));
            row1 = sheet.createRow((short) +(nrow++));
            
            String[] title = {"Năm học", "Học kỳ", "Mã môn học", "Tên môn học", "Số tín chỉ", "Điểm"};
            row1 = sheet.createRow((short) +(nrow++));
            HSSFFont font2 = hwb.createFont();
            font2.setFontName(HSSFFont.FONT_ARIAL);
            font2.setFontHeightInPoints((short) 12);
            font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font2.setColor(HSSFColor.GREEN.index);
            style1.setFont(font2);
            for (i = 0; i < title.length; i++) {
                cell1 = row1.createCell((short) +(i+1));
                cell1.setCellStyle(style1);
                cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell1.setCellValue(title[i]);
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
                subTC=subjectDao.findById(studyResult.get(i).getId().getSubjectCode()).getnumTC();
                String[] info = {year, Integer.toString(semester), subCode, subName,Integer.toString(subTC), Float.toString(mark)};

                HSSFCell cell = null;
                for (int j = 0; j < info.length; j++) {
                    cell = row.createCell((short) +(j+1));
                    cell.setCellStyle(style3);
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(info[j]);
                    sheet.autoSizeColumn(j+1);
                }
            }
            
            FileOutputStream fileOut = new FileOutputStream("Bangdiem" + user + ".xls");
            hwb.write(fileOut);
            fileOut.close();
            downloadFile("Bangdiem" + user + ".xls", resp);
            
       }catch(Exception ex){
           String path= "./jsps/Message.jsp";
           resp.sendRedirect(path);
       }
        
}
 private void downloadFile(String filename, HttpServletResponse resp) throws IOException {
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
private int getNumTC(List<StudyResult> studyResult) throws Exception {
        int numTC = 0;
        SubjectDAO subjectDao=new SubjectDAO();
        for (int i = 0; i < studyResult.size(); i++) {
            numTC += subjectDao.findById(studyResult.get(i).getId().getSubjectCode()).getnumTC();
        }
        return numTC;
    }

    private float getAverageMark(List<StudyResult> studyResult) throws Exception {
        int numtc = getNumTC(studyResult);
        SubjectDAO subjectDao=new SubjectDAO();
        float summark = 0;
        float result = 0;
        for (int i = 0; i < studyResult.size(); i++) {
            summark += (subjectDao.findById(studyResult.get(i).getId().getSubjectCode()).getnumTC() * studyResult.get(i).getMark());
        }
        result = (float) Math.round(summark * 100 / numtc) / 100;
        return result;
    }
private void setSubjectName(List<StudyResult> studyResult) throws Exception{
    SubjectDAO subjectDao=new SubjectDAO();
    for(int i=0;i<studyResult.size();i++){
        studyResult.get(i).setSubjectName(subjectDao.findById(studyResult.get(i).getId().getSubjectCode()).getSubjectName());
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
            Logger.getLogger(DownloadController.class.getName()).log(Level.SEVERE, null, ex);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(DownloadController.class.getName()).log(Level.SEVERE, null, ex);
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
    
}
