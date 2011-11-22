package uit.cnpm02.dkhp.DAO;

import java.sql.PreparedStatement;
import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcDAO;
import uit.cnpm02.dkhp.model.News;
/**
 *
 * @author LocNguyen
 */
public class NewsDAO extends AbstractJdbcDAO<News, String> {
    
    public void UpdateNewsStatus(String id) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("Update khoaluantotnghiep.bantin set Loai=(Loai+1)%2 where MaTin='").append(id).append("'");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.execute();
        } catch (Exception ex) {
            throw ex;
        }
    }
   
    
}
