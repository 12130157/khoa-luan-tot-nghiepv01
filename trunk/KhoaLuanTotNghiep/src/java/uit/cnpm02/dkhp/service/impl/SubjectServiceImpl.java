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
import uit.cnpm02.dkhp.DAO.PreSubjectDAO;
import uit.cnpm02.dkhp.DAO.SubjectDAO;
import uit.cnpm02.dkhp.model.PreSubID;
import uit.cnpm02.dkhp.model.PreSubject;
import uit.cnpm02.dkhp.model.Subject;
import uit.cnpm02.dkhp.service.ISubjectService;
import uit.cnpm02.dkhp.utilities.Constants;
import uit.cnpm02.dkhp.utilities.ExecuteResult;

/**
 *
 * @author LocNguyen
 */
public class SubjectServiceImpl implements ISubjectService {
    /** SessionID - Current list subject **/
    private Map<String, List<Subject>> subjectMap
            = new HashMap<String, List<Subject>>();
    private SubjectDAO subjectDao;
    private PreSubjectDAO preSubDao;
    
    private final Object mutex = new Object();
    
    public SubjectServiceImpl() {
        subjectDao = DAOFactory.getSubjectDao();
        preSubDao = DAOFactory.getPreSubDao();
    }

    @Override
    public List<Subject> getAll() throws Exception{
        // TODO: fillter by faculty code.
        return subjectDao.findAll();
    }
    @Override
    public List<Subject> findAll(int recordPerPage, int currentPage, String orderBy, String order) {
        try {
            return subjectDao.findAll(recordPerPage, currentPage, orderBy, order);
        } catch (Exception ex) {
            Logger.getLogger(SubjectServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    @Override
    public Subject addSubject(Subject sub) throws Exception {
        // validate subject
        if (subjectDao.findById(sub.getId()) != null) {
            // Already existed
            throw new Exception("Môn học đã có");
        }
        synchronized (mutex) {
            try {
                // Add subject into database
                String subId = subjectDao.add(sub);

                // Add requried
                List<Subject> requiredSub = sub.getPreSub();
                if ((requiredSub != null) && !requiredSub.isEmpty()) {
                    List<PreSubject> preSubject = new ArrayList<PreSubject>(5);
                    for (Subject s : requiredSub) {
                        PreSubject pSub = new PreSubject(s.getId(), sub.getId());
                        PreSubID id = new PreSubID(sub.getId(), s.getId());
                        pSub.setId(id);

                        if (!preSubject.contains(pSub)) {
                            preSubject.add(pSub);
                        } else {
                            sub.getPreSub().remove(s);
                        }
                    }
                    if (!preSubject.isEmpty()) {
                        preSubDao.addAll(preSubject);
                    }
                }
                sub.setId(subId);

                return sub;
            } catch (Exception ex) {
                Logger.getLogger(SubjectServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                throw new Exception("Rất tiếc đã có lỗi xảy ra: " + ex.toString());
            } finally {
                // Roll back process if some error occur...
            }
        }
    }

    @Override
    public ExecuteResult updateSubject(Subject sub) {
        ExecuteResult er = new ExecuteResult(true, "");
        try {
            Subject s = subjectDao.update(sub);
            List<PreSubject> preSubExists = preSubDao.findByColumName("MaMH", s.getId());
            if ((preSubExists != null) && !preSubExists.isEmpty()) {
                preSubDao.delete(preSubExists);
            }
            
            List<Subject> preSub = s.getPreSub();
            if ((preSub != null) && !preSub.isEmpty()) {
                List<PreSubject> newPreSubs = new ArrayList<PreSubject>(10);
                for (int i = 0; i < preSub.size(); i++) {
                    Subject s_temp = preSub.get(i);
                    PreSubID id = new PreSubID(s.getId(), s_temp.getId());
                    PreSubject preSub_temp = new PreSubject();
                    preSub_temp.setId(id);
                    if (!newPreSubs.contains(preSub_temp)) {
                        newPreSubs.add(preSub_temp);
                    } else {
                        preSub.remove(s_temp);
                        i--;
                    }
                }
                s.setPreSub(preSub);
                preSubDao.addAll(newPreSubs);
                // Update full information
                for (Subject p : preSub) {
                    p.setSubjectName(subjectDao.findById(p.getId())
                            .getSubjectName());
                }
            }
            er.setData(s);
        } catch (Exception ex) {
            Logger.getLogger(SubjectServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            return new ExecuteResult(false, 
                    "Đã có lỗi trong quá trình cập nhật môn học. " + ex.toString());
        }
        return er;
    }

    @Override
    public boolean deleteSubject(String sessionId, String subId)
                                                throws Exception {
        boolean deletePreSub = false;
        List<PreSubject> preSubjects = new ArrayList<PreSubject>(10);
        String errorMsg = "";
        try {
            // Find Pre-Subject - Remove if existed
            errorMsg = "Không thể xóa môn học tiên quyết.";
            List<PreSubject> temp = preSubDao.findByColumName("MaMH", subId);
            if ((temp != null) && !temp.isEmpty()) {
                preSubjects.addAll(temp);
            }
            temp = preSubDao.findByColumName("MaMHTQ", subId);
            if ((temp != null) && !temp.isEmpty()) {
                preSubjects.addAll(temp);
            }
            
            if ((preSubjects != null) && !preSubjects.isEmpty()) {
                preSubDao.delete(preSubjects);
                deletePreSub = true;
            }
            
            // Remove Subject
            errorMsg = "Không thể xóa môn học.";
            Subject sub = subjectDao.findById(subId);
            if (sub != null) {
                subjectDao.delete(sub);
            } else {
                throw new Exception("Không tìm thấy môn học.");
            }

            // If remove unsuccess, Please restore deleted Pre-Subject
            List<Subject> subjectsOfSession = subjectMap.get(sessionId);
            if ((subjectsOfSession != null) && subjectsOfSession.contains(sub)) {
                subjectsOfSession.remove(sub);
                subjectMap.put(sessionId, subjectsOfSession);
            }

            return true;
        } catch (Exception ex) {
            throw new Exception(errorMsg, ex);
        } finally {
            if (deletePreSub && (!preSubjects.isEmpty())) {
                try {
                    // Rollback data.
                    errorMsg = "Đã xóa môn học tiên quyết, không thể xóa môn học.";
                    preSubDao.addAll(preSubjects);
                } catch (Exception ex) {
                    // Don't know what to do...
                }
            }
        }
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
            if (key.equals("*") || key.equalsIgnoreCase("All")) {
                results = subjectDao.findAll();
            } else {

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
            }
        } catch (Exception ex) {
            Logger.getLogger(SubjectServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        if ((results != null) && !results.isEmpty()) {
            subjectMap.put(sessionId, results);
        }
        return results;
    }
    
    public List<Subject> getCurrentSubjects(String sessionId) {
        return subjectMap.get(sessionId);
    }

    @Override
    public Subject findById(String id) {
        try {
            return subjectDao.findById(id);
        } catch (Exception ex) {
            Logger.getLogger(SubjectServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
