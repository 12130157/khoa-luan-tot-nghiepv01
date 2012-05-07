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
        ExecuteResult result = new ExecuteResult(true, Message.ADD_TRAINCLASS_SUCCSESS);
        try {
            //////
            // TrainClass hasn't existed yet.
            //
            boolean checkPass = true;
            TrainClass classExisted;

            classExisted = classDAO.findById(obj.getId());
            if (classExisted != null) {
                result.setMessage(Message.ADD_TRAINCLASS_ERROR_CLASS_EXISTED);
                checkPass = false;
            }
            
            //
            // Check max and min number of student for open class
            //
            
            
            //////
            // Room - In a time --> 1 train class
            //
            List<TrainClass> existedClasses = classDAO.findByClassRoomAndTime(
                    obj.getClassRoom(), obj.getStudyDate(),
                    obj.getShift(), TrainClassStatus.OPEN.getValue());
            if ((existedClasses != null) && (!existedClasses.isEmpty())) {
                result.setMessage(Message.ADD_TRAINCLASS_ERROR_ROOM_DUPLECATE);
                checkPass = false;
            }
            //////
            // Lecturer - In a time --> 1 train class
            //
            existedClasses = classDAO.findByLecturerAndTime(obj.getLecturerCode(),
                    obj.getStudyDate(), obj.getShift(),
                    TrainClassStatus.OPEN.getValue());
            if ((existedClasses != null) && (!existedClasses.isEmpty())) {
                result.setMessage(Message.ADD_TRAINCLASS_ERROR_LECTURER_DUPLECATE);
                checkPass = false;
            }

            if (!checkPass) {
                result.setIsSucces(false);
                return result;
            }

            classDAO.add(obj);
            result.setData(obj);
        } catch (Exception ex) {
            Logger.getLogger(TrainClassServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            result.setIsSucces(false);
            result.setMessage(Message.ADD_TRAINCLASS_ERROR);
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
}