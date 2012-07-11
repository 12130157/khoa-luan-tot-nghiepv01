package uit.cnpm02.dkhp.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.StudentDAO;
import uit.cnpm02.dkhp.DAO.StudyResultDAO;
import uit.cnpm02.dkhp.DAO.TrainClassDAO;
import uit.cnpm02.dkhp.controllers.PDT.ImportScoreResult;
import uit.cnpm02.dkhp.model.Student;
import uit.cnpm02.dkhp.model.StudyResult;
import uit.cnpm02.dkhp.model.StudyResultID;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.model.TrainClassID;
import uit.cnpm02.dkhp.service.IFileUploadService;
/**
 *
 * @author LocNguyen
 */
public class ScoreProcessUtil {
    private TrainClassDAO classDao;
    private IFileUploadService fuService;
    private StudyResultDAO srDao = null;
    private StudentDAO studentDao = null;
    
    /**
     * Default constructor.
     */
    public ScoreProcessUtil () {
        classDao = DAOFactory.getTrainClassDAO();
        fuService = new FileUploadServiceImpl();
        srDao = DAOFactory.getStudyResultDao();
        studentDao = DAOFactory.getStudentDao();
    }
    
    public ImportScoreResult importScore(List<StudyResult> studyResults) {
        ImportScoreResult isr = new ImportScoreResult();
        try {
            List<Student> addedS = new ArrayList<Student>(10);
            List<Student> updatedS = new ArrayList<Student>(10);
            //List<Student> errorS = new ArrayList<Student>(10);
            List<Student> inogedS = new ArrayList<Student>(10);

            List<StudyResult> processedList = new ArrayList<StudyResult>(10);
            for (StudyResult sr : studyResults) {
                StudyResult temp = srDao.findById(sr.getId());
                if (temp != null) {
                    processedList.add(temp);
                    Student s = getStudent(sr);
                    if (temp.getMark() < sr.getMark()) {
                        srDao.update(sr);
                        updatedS.add(s);
                    } else {
                        inogedS.add(s);
                    }
                }
            }
            if (!processedList.isEmpty()) {
                studyResults.removeAll(processedList);
            }

            if (!studyResults.isEmpty()) {
                srDao.addAll(studyResults);
                for (StudyResult sr : studyResults) {
                    Student s = getStudent(sr);
                    if (s != null) {
                        addedS.add(s);
                    }
                }
            }
            
            if (!updatedS.isEmpty()) {
                isr.setUpdated(updatedS);
            }
            if (!addedS.isEmpty()) {
                isr.setAdded(addedS);
            }
            if (!inogedS.isEmpty()) {
                isr.setInoged(inogedS);
            }
        } catch (Exception ex) {
            Logger.getLogger(ScoreProcessUtil.class.getName()).log(Level.SEVERE, null, ex);
            return new ImportScoreResult("Lỗi server: " + ex.toString());
        }
        return isr;
    }
    
    /**
     * Import score
     * In return result for update fail case,
     * should have a map include:
     * - 
     * @param tcId
     * @return 
     */
    public ImportScoreResult importScore(TrainClassID tcId) {
        ImportScoreResult isr = new ImportScoreResult();
        try {
            // Step 1: Validate file
            TrainClass clazz = classDao.findById(tcId);
            if (clazz == null) {
                return new ImportScoreResult("Không tìm thấy lớp.");
            }
            
            File file = fuService.getFile(tcId.getClassCode() + ".xls");
            if ((file == null) || !file.exists()) {
                return new ImportScoreResult("Không tìm thấy file.");
            }
            // Step 2: Validate & load data
            List<StudyResult> results = loadData(file, clazz);
            // Step 3: Update
            isr = importScore(results);
            /*List<Student> addedS = new ArrayList<Student>(10);
            List<Student> updatedS = new ArrayList<Student>(10);
            //List<Student> errorS = new ArrayList<Student>(10);
            List<Student> inogedS = new ArrayList<Student>(10);
            
            List<StudyResult> processedList = new ArrayList<StudyResult>(10);
            for (StudyResult sr : results) {
                StudyResult temp = srDao.findById(sr.getId());
                if (temp != null) {
                    processedList.add(temp);
                    Student s = getStudent(sr);
                    if (temp.getMark() < sr.getMark()) {
                        srDao.update(sr);
                        updatedS.add(s);
                    } else {
                        inogedS.add(s);
                    }
                }
            }
            if (!processedList.isEmpty()) {
                results.removeAll(processedList);
            }
            
            if (!results.isEmpty()) {
                srDao.addAll(results);
                for (StudyResult sr : results) {
                    Student s = getStudent(sr);
                    if (s != null) {
                        addedS.add(s);
                    }
                }
            }*/
            
            // Step 4: Turn on flag indicate the class all ready update score
            // O: not updated yet - other: updated
            
            //
            // set flag only all studyresult filled TODO:
            //
            clazz.setUpdateScore(1);
            classDao.update(clazz);
            
            // Step 5: Move processed file to backup folder
            File backupDir = new File("bk");
            if (!backupDir.exists() || !backupDir.isDirectory()) {
                backupDir.mkdir();
            }
            copyfile(file.getPath(), "bk" + File.separator + file.getName() 
                    + "_" + System.currentTimeMillis());
            file.delete();
            
        } catch (Exception ex) {
            Logger.getLogger(ScoreProcessUtil.class.getName()).log(Level.SEVERE, null, ex);
            return new ImportScoreResult("Lỗi server: " + ex.toString());
        }
        
        return isr;
    }
    
    private Student getStudent(StudyResult sr) {
        String studentId = sr.getId().getStudentCode();
        Student student = null;
        try {
            student = studentDao.findById(studentId);
        } catch (Exception ex) {
            Logger.getLogger(ScoreProcessUtil.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
        return student;
    }
    
    private List<StudyResult> loadData(File file, TrainClass clazz)  {
        List<StudyResult> sResult = new ArrayList<StudyResult>(10);
        
        HSSFWorkbook wb = null;
        try {
            wb = new HSSFWorkbook(new FileInputStream(file));
        } catch (IOException ex) {
            Logger.getLogger(ScoreProcessUtil.class.getName())
                    .log(Level.SEVERE, null, ex);
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
            String firstValue = cellTemp.getStringCellValue();
            boolean firstLine = false;
            try {
                int value = Integer.parseInt(firstValue);
                firstLine = true;
            } catch (Exception ex) {
                firstLine = false;
            }
            //check the first cell of data must be a number
            //if (cellType != HSSFCell.CELL_TYPE_NUMERIC) {
            if (!firstLine) {
                continue;
            } else {
                //MSSV	MaMH	Diem	HocKy	NamHoc
                cellTemp = rowTemp.getCell(1);
                String mssv = cellTemp.getStringCellValue();

                cellTemp = rowTemp.getCell(5);
                float finalScore = (float) cellTemp.getNumericCellValue();
            
                StudyResult sr = new StudyResult();
                StudyResultID srId = new StudyResultID(mssv, clazz.getSubjectCode());
                sr.setId(srId);
                sr.setMark(finalScore);
                sr.setSemester(clazz.getId().getSemester());
                sr.setYear(clazz.getId().getYear());
                
                sResult.add(sr);
            }
        }
        
        return sResult;
    }
    
    /**
     * Copy file
     * @param srFile source file
     * @param dtFile dest file
     */
    private static void copyfile(String srFile, String dtFile) {
        try {
            File f1 = new File(srFile);
            File f2 = new File(dtFile);
            InputStream in = new FileInputStream(f1);

            //For Overwrite the file.
            OutputStream out = new FileOutputStream(f2);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            System.out.println("File copied.");
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage() + " in the specified directory.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
