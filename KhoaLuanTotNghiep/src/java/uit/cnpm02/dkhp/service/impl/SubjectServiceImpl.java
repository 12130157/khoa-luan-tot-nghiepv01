package uit.cnpm02.dkhp.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.SubjectDAO;
import uit.cnpm02.dkhp.model.Subject;
import uit.cnpm02.dkhp.service.ISubjectService;
import uit.cnpm02.dkhp.utilities.Constants;

/**
 *
 * @author LocNguyen
 */
public class SubjectServiceImpl implements ISubjectService {
    /** SessionID - Current list subject **/
    private Map<String, List<Subject>> subjectMap
            = new HashMap<String, List<Subject>>();
    private SubjectDAO subjectDao;
    
    private Object mutex = new Object();
    
    public SubjectServiceImpl() {
        subjectDao = DAOFactory.getSubjectDao();
    }

    @Override
    public Subject addSubject(Subject sub) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Subject updateSubject(Subject sub) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean deleteSubject(Subject sub) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Subject> sort(String sessionId, final String by, final String type) {
        List<Subject> subject = subjectMap.get(sessionId);

        if ((subject != null) && (!subject.isEmpty())) {
            Collections.sort(subject, new Comparator<Subject>() {
            @Override
            public int compare(Subject o1, Subject o2) {
                if (type.equals("ASC"))
                    return o1.compare(o2, by);
                else
                    return o2.compare(o1, by);
            }

        });
        }
        subject.get(0).getId();
        return subject;
    }

    @Override
    public List<Subject> findByFaculty(String facultyCode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public List<Subject> findSubjects(String sessionId, int currentPage) {
        List<Subject> subjects = new ArrayList<Subject>(10);
        try {
            subjects = subjectDao.findAll(Constants.ELEMENT_PER_PAGE_DEFAULT,
                                                    currentPage, null, null);
            if ((subjects != null) && !subjects.isEmpty()) {
                subjectMap.put(sessionId, subjects);
            }
        } catch (Exception ex) {
            Logger.getLogger(SubjectServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return subjects;
    }
    
    @Override
    public int getNumberPage() {
        try {
            int existedRow = subjectDao.getRowsCount();
            int rowsPerPage = Constants.ELEMENT_PER_PAGE_DEFAULT;

            if (existedRow % rowsPerPage == 0) {
                return existedRow / rowsPerPage;
            } else {
                return existedRow / rowsPerPage + 1;
            }
        } catch (Exception ex) {
            Logger.getLogger(SubjectServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 1;
    }

    @Override
    public List<Subject> search(String sessionId, String key) {
        List<Subject> results = new ArrayList<Subject>(10);
        try {
            // Search by Subject ID
            results = subjectDao.findByColumName("MaMH", key);
            
            // Search by subject name
            List<Subject> temp = subjectDao.findByColumName("TenMH", key);
            if ((temp != null) && !temp.isEmpty()) {
                for (Subject s : temp) {
                    if (!results.contains(s)) {
                        results.add(s);
                    }
                }
            }
            if ((results != null) && !results.isEmpty()) {
                subjectMap.put(sessionId, results);
            }
        } catch (Exception ex) {
            Logger.getLogger(SubjectServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return results;
    }
    
}