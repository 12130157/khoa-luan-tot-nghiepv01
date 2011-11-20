package uit.cnpm02.dkhp.DAO;

import java.lang.reflect.ParameterizedType;
import uit.cnpm02.dkhp.access.advancedJDBC.AdvancedAbstractJdbcDAO;
import uit.cnpm02.dkhp.model.PreSubID;
import uit.cnpm02.dkhp.model.PreSubject;

/**
 *
 * @author LocNguyen
 */
public class PreSubjectDAO extends AdvancedAbstractJdbcDAO<PreSubject, PreSubID> {
    
    @Override
    public PreSubID createID() {
        return new PreSubID();
    }
    
}
