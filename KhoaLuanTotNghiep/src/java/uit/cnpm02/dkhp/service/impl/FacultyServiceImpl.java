package uit.cnpm02.dkhp.service.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.FacultyDAO;
import uit.cnpm02.dkhp.model.Faculty;
import uit.cnpm02.dkhp.service.IFacultyService;

/**
 *
 * @author LocNguyen
 */
public class FacultyServiceImpl implements IFacultyService {
    private FacultyDAO fDao;
    
    public FacultyServiceImpl() {
        fDao = DAOFactory.getFacultyDao();
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
    
}
