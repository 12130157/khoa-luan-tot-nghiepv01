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
import uit.cnpm02.dkhp.service.ITrainClassService;
import uit.cnpm02.dkhp.utilities.Constants;

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
    public List<TrainClass> getTrainClass(int currentPage) {
        List<TrainClass> trainClazzs = new ArrayList<TrainClass>(10);
        try {
            if (currentPage < 1) {
                currentPage = 1;
            }

            trainClazzs = classDAO.findAll(Constants.ELEMENT_PER_PAGE_DEFAULT,
                    currentPage,
                    null, null);
                   
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
    public TrainClass addNewTrainClass(TrainClass obj) {
        // TODO: Implement
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean updateTrainClass(TrainClass obj) {
        // TODO: Implement
        throw new UnsupportedOperationException("Not supported yet.");
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
}
