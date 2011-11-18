package uit.cnpm02.dkhp.access.advancedJDBC;

import java.io.Serializable;

/**
 *
 * @author LocNguyen
 */
public interface IAdvancedModel <ID extends IID> extends Serializable{
    
    /**
     * @return the Id
     */
    ID getId();

    /**
     * @param id
     */
    void setId(ID id);

    /**
     * 
     * @return true if the model is validated, otherwise false.
     */
    boolean validate();
}
