package uit.cnpm02.dkhp.access.advancedJDBC;

import java.io.Serializable;

/**
 *
 * @author LocNguyen
 */
public interface IID extends Serializable {
    /**
     * Get id names
     * @return ids of model.
     */
    String[] getIDNames();
    
    /**
     * Get id's values.
     * @return id's values.
     */
    Object[] getIDValues();
}
