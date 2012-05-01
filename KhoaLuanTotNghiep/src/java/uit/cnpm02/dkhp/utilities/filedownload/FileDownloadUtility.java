package uit.cnpm02.dkhp.utilities.filedownload;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.StudentDAO;
import uit.cnpm02.dkhp.model.Student;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.utilities.Message;

/**
 *
 * @author LocNguyen
 */
public class FileDownloadUtility {
    private static StudentDAO studentDao;
    
    public FileDownloadUtility() {
        studentDao = DAOFactory.getStudentDao();
    }
    
    public static void downloadFile(String filename,
                            HttpServletResponse resp) throws IOException {
        try {
            resp.reset();
            resp.setContentType("application/xls");
            resp.setHeader("Content-disposition", "attachment; filename="
                    + filename);

            FileInputStream in = new FileInputStream(filename);
            int i;
            while ((i = in.read()) != -1) {
                resp.getOutputStream().write(i);
            }
            in.close();
            resp.getOutputStream().flush();
        } catch (Exception e) {
            resp.getWriter().println(Message.DOWNLOAD_FILE_ERROR 
                    + " \n" + e.toString());
        }
    }
    
    public static String exportStudentReportFile(String mssv,
                                                List<TrainClass> trainClasses) {
        String fileName = "" + mssv + ".xls";
        
        try {
            HSSFWorkbook hwb = new HSSFWorkbook();
            HSSFSheet sheet = hwb.createSheet("" + mssv);
            HSSFCellStyle style = hwb.createCellStyle();

            int nrow = 0, i = 0;
            int n = trainClasses.size();

            Student student = studentDao.findById(mssv);
            String[] infoStudent = {"Họ Và Tên: " + student.getFullName(),
                "MSSV: " + mssv};//,
                //"Số TC Da tich luy: " + numtc,
                //"Diểm TB: " + DTB};

            //Write student's information into file
            HSSFRow row1 = null;
            HSSFCell cell1 = null;

            row1 = sheet.createRow((short) +(nrow++));
            cell1 = row1.createCell((short) +4);
            cell1.setCellStyle(style);
            cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell1.setCellValue("BẢNG ĐIỂM SINH VIÊN");

            row1 = sheet.createRow((short) +(nrow++));
            row1 = sheet.createRow((short) +(nrow++));

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

            String[] title = {"Năm học", "Học kỳ", "Mã môn học", "Tên môn học", "Điểm"};
            row1 = sheet.createRow((short) +(nrow++));
            for (i = 0; i < title.length; i++) {
                cell1 = row1.createCell((short) +i);
                cell1.setCellStyle(style);
                cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell1.setCellValue(title[i]);
            }

            //write score of student into file
            String subCode;
            String subName;
            Float mark;
            int semester;
            String year;
            for (i = 0; i < n; i++) {
                /*clsDetailResult stTemp = resultstudy.get(i);
                HSSFRow row = sheet.createRow((short) +(nrow++));

                subCode = stTemp.getSubCode();
                subName = stTemp.getSubName();
                mark = stTemp.getMark();
                semester = stTemp.getSemester();
                year = stTemp.getYear();
                String[] info = {year, Integer.toString(semester), subCode, subName, Float.toString(mark)};

                HSSFCell cell = null;
                for (int j = 0; j < info.length; j++) {
                    cell = row.createCell((short) +j);
                    cell.setCellStyle(style);
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(info[j]);
                }*/
            }

            
            FileOutputStream fileOut = new FileOutputStream(fileName);
            hwb.write(fileOut);
            fileOut.close();
            //DownloadFile("Bangdiem" + mssv + ".xls", resp);
            //result = "Tao file thanh cong";
        } catch (Exception ex) {
            return "";
        }
        
        return fileName;
    }
    
}
