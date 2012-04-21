package uit.cnpm02.dkhp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.LecturerDAO;
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
    
    private static Object mutex = new Object();
    
    @Override
    public List<TrainClass> getTrainClass(int status) {
        List<TrainClass> trainClazzs = new ArrayList<TrainClass>(10);
        try {
            //if (currentPage < 1) {
            //    currentPage = 1;
            //}
            /*trainClazzs = classDAO.findAll(Constants.ELEMENT_PER_PAGE_DEFAULT,
                    currentPage,
                    null, null);
            */
            trainClazzs = classDAO.findByStatus(status);
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
            existedClasses = classDAO.findByLecturerAndTime(obj.getClassRoom(),
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
