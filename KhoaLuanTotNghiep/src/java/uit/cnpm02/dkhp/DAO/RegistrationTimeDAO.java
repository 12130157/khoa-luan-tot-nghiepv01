/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.DAO;

import uit.cnpm02.dkhp.access.advancedJDBC.AdvancedAbstractJdbcDAO;
import uit.cnpm02.dkhp.model.RegistrationTime;
import uit.cnpm02.dkhp.model.RegistrationTimeID;

/**
 *
 * @author thanh
 */
public class RegistrationTimeDAO extends AdvancedAbstractJdbcDAO<RegistrationTime, RegistrationTimeID>{

    @Override
    public RegistrationTimeID createID() {
        return new RegistrationTimeID();
    }
    
}
