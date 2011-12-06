/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.DAO;

import uit.cnpm02.dkhp.access.advancedJDBC.AdvancedAbstractJdbcDAO;
import uit.cnpm02.dkhp.model.StudyResult;
import uit.cnpm02.dkhp.model.StudyResultID;

/**
 *
 * @author thanh
 */
public class StudyResultDAO extends AdvancedAbstractJdbcDAO<StudyResult, StudyResultID>{

    @Override
    public StudyResultID createID() {
        return new StudyResultID();
    }
    
}
