package uit.cnpm02.dkhp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.FacultyDAO;
import uit.cnpm02.dkhp.DAO.SubjectDAO;
import uit.cnpm02.dkhp.model.Faculty;
import uit.cnpm02.dkhp.model.Subject;
import uit.cnpm02.dkhp.service.IFacultyService;

/**
 *
 * @author LocNguyen
 */
public class FacultyServiceImpl implements IFacultyService {
    private FacultyDAO fDao;
    private SubjectDAO subjectDao;
    
    public FacultyServiceImpl() {
        fDao = DAOFactory.getFacultyDao();
        subjectDao = DAOFactory.getSubjectDao();
    }

    @Override
    public List<Faculty> getAllFaculty() {
        try {
            return fDao.findAll();
        } catch (Exception ex) {
            Logger.getLogger(FacultyServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Subject> getSubject(String faculty) {
        List<Subject> results = new ArrayList<Subject>(10);
        try {
            results = subjectDao.findAll();
            //results = subjectDao.findByColumName("MaKhoa", faculty);
        } catch (Exception ex) {
            Logger.getLogger(FacultyServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return results;
    }
    
}
