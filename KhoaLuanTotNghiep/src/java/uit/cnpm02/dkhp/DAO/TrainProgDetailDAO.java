/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.DAO;

import uit.cnpm02.dkhp.access.advancedJDBC.AdvancedAbstractJdbcDAO;
import uit.cnpm02.dkhp.model.TrainProDetail;
import uit.cnpm02.dkhp.model.TrainProDetailID;

/**
 *
 * @author LocNguyen
 */
public class TrainProgDetailDAO extends AdvancedAbstractJdbcDAO<TrainProDetail, TrainProDetailID>{

    @Override
    public TrainProDetailID createID() {
        return new TrainProDetailID();
    }
    
}
