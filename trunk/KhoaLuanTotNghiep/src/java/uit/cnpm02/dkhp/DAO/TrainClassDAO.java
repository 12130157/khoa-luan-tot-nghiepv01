/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.DAO;

import uit.cnpm02.dkhp.access.advancedJDBC.AdvancedAbstractJdbcDAO;
import uit.cnpm02.dkhp.model.TrainClass;
import uit.cnpm02.dkhp.model.TrainClassID;

/**
 *
 * @author thanh
 */
public class TrainClassDAO extends AdvancedAbstractJdbcDAO<TrainClass, TrainClassID>{

    @Override
    public TrainClassID createID() {
        return new TrainClassID();
    }
    
}
