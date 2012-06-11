/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.DAO;

import uit.cnpm02.dkhp.access.advancedJDBC.AdvancedAbstractJdbcDAO;
import uit.cnpm02.dkhp.model.DetailTrain;
import uit.cnpm02.dkhp.model.DetailTrainID;

/**
 *
 * @author thanh
 */
public class DetailTrainDAO extends AdvancedAbstractJdbcDAO<DetailTrain, DetailTrainID> {

    @Override
    public DetailTrainID createID() {
        return new DetailTrainID();
    }
    
}
