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
import uit.cnpm02.dkhp.service.IPreSubject;
import uit.cnpm02.dkhp.utilities.ExecuteResult;

/**
 *
 * @author LocNguyen
 */
public class PreSubjectImpl implements IPreSubject {
    /** SessionID - Current list subject **/
    private Map<String, List<PreSubject>> preSubjectMap
            = new HashMap<String, List<PreSubject>>();
    
    /**SubjectId - SubjectName**/
    private Map<String, String> subNameMapping = new HashMap<String, String>(10);
    private boolean loaded = false;
    private SubjectDAO subjectDao;
    private PreSubjectDAO preSubDao;
    
    private final Object mutex = new Object();
    
    public PreSubjectImpl() {
        subjectDao = DAOFactory.getSubjectDao();
        preSubDao = DAOFactory.getPreSubDao();
        
        loadSubjectName();
    }

    @Override
    public PreSubject addPreSubject(PreSubject preSub) throws Exception {
        // Validate
        if (preSub == null) {
            return null;
        }
        // Add
        PreSubID id  = preSubDao.add(preSub);
        preSub.setId(id);
        
        // Update information
        preSub.setSubjectName(subNameMapping.get(id.getSudId()));
        preSub.setPreSubjectName(subNameMapping.get(id.getPreSudId()));
        
        return preSub;
        
    }

    @Override
    public boolean deletePreSubject(String sessionId, String subId, String preSubId)
                                                            throws Exception {
        // Validaste
        if (subId == null
                || preSubId == null
                || subId.isEmpty()
                || preSubId.isEmpty()) {
            throw new Exception("Mã môn học không hợp lệ.");
        }
        
        PreSubject obj = preSubDao.findById(new PreSubID(subId, preSubId));
        if (obj == null) {
            throw new Exception("Khong tìm thấy dữ liệu.");
        }
        
        // Delete
        preSubDao.delete(obj);
        
        // Update
        List<PreSubject> preSubjects = preSubjectMap.get(sessionId);
        if ((preSubjects != null) 
            && !preSubjects.isEmpty() 
            && preSubjects.contains(obj)) {
          
            preSubjects.remove(obj);
        }
        
        return true;
    }

    @Override
    public List<PreSubject> sort(String sessionId, final String by, final String type) {
        List<PreSubject> preSubjects = preSubjectMap.get(sessionId);
        
        if ((preSubjects == null) || preSubjects.isEmpty()){
            return null;
        }
        
        Collections.sort(preSubjects, new Comparator<PreSubject>() {
            @Override
            public int compare(PreSubject o1, PreSubject o2) {
                if (type.equals("ASC"))
                    return o1.compare(o2, by);
                else
                    return o2.compare(o1, by);
            }

        });
        
        return preSubjects;
    }

    @Override
    public List<PreSubject> getAll(String sessionId, boolean fullInfo) throws Exception {
        List<PreSubject> results = new ArrayList<PreSubject>(10);
        results = preSubDao.findAll();
        if (fullInfo) {
            setSubjectName(results);
        }
        preSubjectMap.put(sessionId, results);
        return results;
    }

    @Override
    public List<PreSubject> findSubjects(String sessionId, int currentPage) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getNumberPage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<PreSubject> search(String sessionId, String key) {
        List<PreSubject> results = new ArrayList<PreSubject>(10);
        try {
            if (key.equals("*") || key.equalsIgnoreCase("All")) {
                results = preSubDao.findAll();
            } else {
                // Search by Subject name
                List<Subject> subjects = subjectDao.findByColumName("TenMH", key);
                List<String> subjectIds = new ArrayList<String>(10);
                if ((subjects != null) && !subjects.isEmpty()) {
                    for (Subject s : subjects) {
                        subjectIds.add(s.getId());
                    }
                }

                // Search by subject's id
                subjects = subjectDao.findByColumName("MaMH", key);
                if ((subjects != null) && !subjects.isEmpty()) {
                    for (Subject s : subjects) {
                        if (!subjectIds.contains(s.getId())) {
                            subjectIds.add(s.getId());
                        }
                    }
                }

                if ((subjectIds != null) && !subjectIds.isEmpty()) {
                    List<PreSubID> ids = new ArrayList<PreSubID>(10);
                    List<Subject> allSubject = subjectDao.findAll();
                    for (int i = 0; i < subjectIds.size(); i++) {
                        for (Subject s : allSubject) {
                            PreSubID id = new PreSubID(subjectIds.get(i), s.getId());
                            ids.add(id);
                            id = new PreSubID(s.getId(), subjectIds.get(i));
                            ids.add(id);
                        }
                    }
                    if (!ids.isEmpty()) {
                        results = preSubDao.findByIds(ids);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(PreSubjectImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        //
        // Save data for each session
        //
        if ((results != null) && !results.isEmpty()) {
            setSubjectName(results);
            preSubjectMap.put(sessionId, results);
        }
        return results;
    }

    @Override
    public List<PreSubject> getCurrentPreSubjects(String sessionId) {
        return preSubjectMap.get(sessionId);
    }

    @Override
    public ExecuteResult isExisted(String subjectId, String preSubjectId)
                                                    throws Exception {
         if (subjectId == null
                    || preSubjectId == null
                    || subjectId.isEmpty()
                    || preSubjectId.isEmpty()) {
                return new ExecuteResult(false, "Mã môn học hoặc mã môn học tiên quyết không hợp lệ.");
            }
         
        try {
            if (subjectId.equals(preSubjectId)) {
                //Mon hoc tien quyet khong the la chinh no
                return new ExecuteResult(false, "Chọn môn học trùng nhau.");
            } else {
                PreSubID id = new PreSubID(subjectId, preSubjectId);
                if (DAOFactory.getPreSubDao().findById(id) != null) {
                    return new ExecuteResult(false, "Key đã tồn tại.");
                } else {
                    return new ExecuteResult(true, "Hợp lệ.");
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(PreSubjectImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            return new ExecuteResult(false, "Đã có lỗi xảy ra" + ex);

        }
    }
    
    private void setSubjectName(List<PreSubject> preSub){
        if ((preSub == null) || preSub.isEmpty()) {
            return;
        }
        for (PreSubject ps : preSub) {
            ps.setSubjectName(subNameMapping.get(ps.getId().getSudId()));
            ps.setPreSubjectName(subNameMapping.get(ps.getId().getPreSudId()));
        }
    }
    
    private void loadSubjectName() {
        try {
            List<Subject> subs = subjectDao.findAll();

            if ((subs != null) && (!subs.isEmpty())) {
                for (Subject sub : subs) {
                    subNameMapping.put(sub.getId(), sub.getSubjectName());
                }
                loaded = true;
            }
        } catch (Exception ex) {
            Logger.getLogger(PreSubjectImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            loaded = false;
        }
    }
}
