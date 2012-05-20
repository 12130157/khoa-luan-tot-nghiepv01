package uit.cnpm02.dkhp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.LecturerDAO;
import uit.cnpm02.dkhp.DAO.RuleDAO;
import uit.cnpm02.dkhp.DAO.SubjectDAO;
import uit.cnpm02.dkhp.DAO.TrainClassDAO;
import uit.cnpm02.dkhp.model.Rule;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.model.TrainClassID;
import uit.cnpm02.dkhp.service.ITrainClassService;
import uit.cnpm02.dkhp.service.TrainClassStatus;
import uit.cnpm02.dkhp.utilities.ExecuteResult;
import uit.cnpm02.dkhp.utilities.Message;

/**
 * This utility class support functions for TrainClass
 * Servlet
 * @author LocNguyen
 */
public class TrainClassServiceImpl implements ITrainClassService {
    // DAO definition ///
    private TrainClassDAO classDAO = DAOFactory.getTrainClassDAO();
    private SubjectDAO subjectDAO = DAOFactory.getSubjectDao();
    private LecturerDAO lectureDAO = DAOFactory.getLecturerDao();
    private RuleDAO ruleDao = DAOFactory.getRuleDao();
    
    
    private static Object mutex = new Object();
    
    @Override
    public List<TrainClass> getTrainClass(String year, int semester) {
        List<TrainClass> results = new ArrayList<TrainClass>(10);
        try {
            //if (currentPage < 1) {
            //    currentPage = 1;
            //}
            /*trainClazzs = classDAO.findAll(Constants.ELEMENT_PER_PAGE_DEFAULT,
            currentPage,
            null, null);
             */
            // trainClazzs = classDAO.findByColumNames(new String[]{"NamHoc", "HocKy"}, new Object[]{year,semester});
           
            try {
                List<String> columnName = new ArrayList<String>(10);
                List<Object> values = new ArrayList<Object>(10);

                if ((year != null)
                        && !year.isEmpty()
                        && !year.equalsIgnoreCase("all")) {
                    columnName.add("NamHoc");
                    values.add(year);
                }
                if (semester > 0) {
                    columnName.add("HocKy");
                    values.add(semester);
                }

                String[] strColumnNames = (String[]) columnName.toArray(
                        new String[columnName.size()]);

                if ((strColumnNames == null) || (strColumnNames.length <= 0)) {
                    results = classDAO.findAll();
                } else {
                    results = classDAO.findByColumNames(
                            strColumnNames, values.toArray());
                }
            } catch (Exception ex) {
                Logger.getLogger(
                        ReporterImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Update External Information
            if ((results != null) && !results.isEmpty()) {
                try {
                    for (TrainClass t : results) {
                        String subName = subjectDAO.findById(t.getSubjectCode()).getSubjectName();
                        String lecturerName = lectureDAO.findById(t.getLecturerCode()).getFullName();

                        t.setSubjectName(subName);
                        t.setLectturerName(lecturerName);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }

    @Override
    public List<TrainClass> getTrainClass() {
        List<TrainClass> trainClazzs = new ArrayList<TrainClass>(10);
        try {
            trainClazzs = classDAO.findAll();
            // Update External Information
            if ((trainClazzs != null) && !trainClazzs.isEmpty()) {
                try {
                    for (TrainClass t : trainClazzs) {
                        String subName = subjectDAO.findById(t.getSubjectCode()).getSubjectName();
                        String lecturerName = lectureDAO.findById(t.getLecturerCode()).getFullName();

                        t.setSubjectName(subName);
                        t.setLectturerName(lecturerName);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return trainClazzs;
    }

    @Override
    public ExecuteResult addNewTrainClass(TrainClass obj) {
        ExecuteResult result = null;
        try {
            result = internal_checkCreateNewTrainClass(obj);
            
            if (!result.isIsSucces()) {
                return result;
            }

            classDAO.add(obj);
            String className = subjectDAO.findById(obj.getSubjectCode()).getSubjectName();
            String lectureName = lectureDAO.findById(obj.getLecturerCode()).getFullName();
            obj.setSubjectName(className);
            obj.setLectturerName(lectureName);
            result.setData(obj);
        } catch (Exception ex) {
            Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            result.setIsSucces(false);
            result.setMessage(Message.ADD_TRAINCLASS_ERROR);
        }
        
        return result;
    }
    
    @Override
    public ExecuteResult checkOpenClassCondition(TrainClass obj) {
        try {
            ExecuteResult er = internal_checkCreateNewTrainClass(obj);
            if (er.isIsSucces()) {
                er.setMessage("Lớp hợp lệ.");
            }
            return er;
        } catch (Exception ex) {
            Logger.getLogger(TrainClassServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            return new ExecuteResult(false, "Lỗi server: " + ex.toString());
        }
    }

    private ExecuteResult internal_checkCreateNewTrainClass(TrainClass obj) throws Exception {
        ExecuteResult result = new ExecuteResult(true, Message.ADD_TRAINCLASS_SUCCSESS);
        boolean checkPass = true;
            TrainClass classExisted;
            classExisted = classDAO.findById(obj.getId());
            if (classExisted != null) {
                result.setMessage(Message.ADD_TRAINCLASS_ERROR_CLASS_EXISTED);
                checkPass = false;
            }
            
            //
            //Check so SV toi da, toi thieu
            List<Rule> rules = ruleDao.findAll();
            int numMax = 120;
            int numMin = 30;
            if ((rules != null) && !rules.isEmpty()) {
                for (Rule r : rules) {
                    try {
                        if (r.getId().equalsIgnoreCase("SoSinhVienToiDa")) {
                            numMax = (int) r.getValue();
                        } else if (r.getId().equalsIgnoreCase("SoSinhVienToiThieu")) {
                            numMin = (int) r.getValue();
                        }
                    } catch (Exception ex) {
                    }
                }
            }
            
            if ((obj.getNumOfStudent() < numMin)
                ||(obj.getNumOfStudent() > numMax)) {
                String msg = "Số SV không thỏa mãn (" + numMin + " < SLSV < " + numMax +").";
                return new ExecuteResult(false, msg);
            }
                        
            //////
            // Room - In a time --> 1 train class
            //
            List<TrainClass> existedClasses = classDAO.findByClassRoomAndTime(
                    obj.getClassRoom(), obj.getStudyDate(),
                    obj.getShift(), TrainClassStatus.OPEN.getValue());
            if ((existedClasses != null) && (!existedClasses.isEmpty())) {
                String message = "<b>Lớp không hợp lệ: Tại phòng " + obj.getClassRoom()
                        + ", ca " + obj.getShift() + ", ngày thứ " + obj.getStudyDate()
                        + " có lớp " + existedClasses.get(0).getId().getClassCode() + "</b>";
                        //result.setMessage(Message.ADD_TRAINCLASS_ERROR_ROOM_DUPLECATE);
                result.setMessage(message);
                checkPass = false;
            }
            //////
            // Lecturer - In a time --> 1 train class
            //
            existedClasses = classDAO.findByLecturerAndTime(obj.getLecturerCode(),
                    obj.getStudyDate(), obj.getShift(),
                    TrainClassStatus.OPEN.getValue());
            if ((existedClasses != null) && (!existedClasses.isEmpty())) {
                String message = "<b>Lớp không hợp lệ: Giảng viên " + obj.getLecturerCode()
                        + " bị trùng giờ tại lớp: " + existedClasses.get(0).getId().getClassCode() + "</b>";
                        //result.setMessage(Message.ADD_TRAINCLASS_ERROR_ROOM_DUPLECATE);
                result.setMessage(message);
                result.setMessage(Message.ADD_TRAINCLASS_ERROR_LECTURER_DUPLECATE);
                checkPass = false;
            }

            if (!checkPass) {
                result.setIsSucces(false);
                return result;
            }
            
            return result;
    }
    
    @Override
    public ExecuteResult updateTrainClass(TrainClass obj) {
        //TODO: implementation.
        ExecuteResult result = new ExecuteResult();
        try {
            //
            // Query out real object from database
            //
            TrainClass trainClass = classDAO.findById(obj.getId());

            
            //
            // Check if object changed or not
            //
            
            //
            // Update to database
            //
            
            
        } catch (Exception ex) {
            Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        return result;
    }

    @Override
    public boolean isExisted(TrainClass obj) {
        // TODO: Implement
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public boolean validate(TrainClass obj) {
        if ((obj == null) || (obj.getId() == null)) // Not instance
            return false;
        try {
            if (classDAO.findById(obj.getId()) != null) // Class existed
                return false;
        } catch (Exception ex) {
            Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true;
    }

    @Override
    public TrainClass getClassInfomation(TrainClassID obj) {
        try {
            TrainClass trainClass = classDAO.findById(obj);
            trainClass.setLectturerName(lectureDAO.findById(trainClass.getLecturerCode()).getFullName());
            trainClass.setSubjectName(subjectDAO.findById(trainClass.getSubjectCode()).getSubjectName());
            return trainClass;
        } catch (Exception ex) {
            Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
       
    }

    @Override
    public List<TrainClass> getCurrentTrainClass(String lecturer) {
        try {
            List<TrainClass> results = classDAO.findByColumName("MaGV", lecturer);
            if ((results != null) && !results.isEmpty()) {
                for (int i = 0; i < results.size(); i++) {
                    if (results.get(i).getStatus().getValue() 
                            != TrainClassStatus.OPEN.getValue()) {
                        results.remove(i);
                        i--;
                    }
                }
            }
            
            if ((results != null) && !results.isEmpty()) {
            try {
                    for (TrainClass t : results) {
                        String subName = subjectDAO.findById(t.getSubjectCode()).getSubjectName();
                        //String lecturerName = lectureDAO.findById(t.getLecturerCode()).getFullName();

                        t.setSubjectName(subName);
                        t.setLectturerName(lecturer);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(TrainClassServiceImpl.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            }
            return results;
        } catch (Exception ex) {
            Logger.getLogger(TrainClassServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
