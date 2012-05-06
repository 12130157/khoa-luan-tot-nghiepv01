package uit.cnpm02.dkhp.service;

import java.util.List;
import uit.cnpm02.dkhp.model.PreSubject;
import uit.cnpm02.dkhp.utilities.ExecuteResult;

/**
 *
 * @author LocNguyen
 */
public interface IPreSubject {
    PreSubject addPreSubject(PreSubject preSub) throws Exception;
    /**
     * Delete a pre subject for a client request
     * 
     * @param sessionId session id
     * @param subId subject's id
     * @param preSubId preSubject's id
     * @return true if delete successful; false if not
     * @throws Exception 
     */
    boolean deletePreSubject(String sessionId, String subId, String preSubId)
                                                            throws Exception;
    
    List<PreSubject> getAll(String sessionId, boolean fullInfo) throws Exception;
    List<PreSubject> sort(String sessionId, final String by, final String type);
    List<PreSubject> findSubjects(String sessionId, int currentPage);
    
    int getNumberPage();

    public List<PreSubject> search(String sessionId, String key);
    public List<PreSubject> getCurrentPreSubjects(String sessionId);
    
    ExecuteResult isExisted(String subjectId, String preSubjectId) throws Exception;
}
