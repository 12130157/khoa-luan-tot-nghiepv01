package uit.cnpm02.dkhp.DAO;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcDAO;
import uit.cnpm02.dkhp.model.News;
/**
 *
 * @author LocNguyen
 */
public class NewsDAO extends AbstractJdbcDAO<News, Integer> {
    
    public void updateNewsStatus(int id) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("Update khoaluantotnghiep.bantin set Loai=(Loai+1)%2 where MaTin=").append(id).append("");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.execute();
            stmt.close();
        } catch (Exception ex) {
            throw ex;
        }
    }
    public void deleteNewsByID(int id) throws SQLException, Exception{
        StringBuffer sql = new StringBuffer();
            sql.append("Delete from khoaluantotnghiep.bantin where MaTin=").append(id).append("");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.execute();
            stmt.close();
            updateNewsId(id);
    }
   public void updateNewsId(int id) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("Update khoaluantotnghiep.bantin set MaTin=MaTin-1 where MaTin>").append(id).append("");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.execute();
            stmt.close();
        } catch (Exception ex) {
            throw ex;
        }
    }
    
}
