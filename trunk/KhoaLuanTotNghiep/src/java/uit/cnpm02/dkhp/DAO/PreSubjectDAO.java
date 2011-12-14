package uit.cnpm02.dkhp.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import uit.cnpm02.dkhp.access.advancedJDBC.AdvancedAbstractJdbcDAO;
import uit.cnpm02.dkhp.model.PreSubID;
import uit.cnpm02.dkhp.model.PreSubject;
import java.util.List;
import uit.cnpm02.dkhp.utilities.Constants;

/**
 *
 * @author LocNguyen
 */
public class PreSubjectDAO extends AdvancedAbstractJdbcDAO<PreSubject, PreSubID> {
    
    @Override
    public PreSubID createID() {
        return new PreSubID();
    }
    public List<String> findAllPreSubBySubCode(String subjectCode) throws Exception {
        checkModelWellDefined();
        PreSubject t = new PreSubject();
        if (t == null) {
            throw new Exception("Cannot initialize the " + PreSubject.class.getName()
                    + " class");
        }
        List<String> results = new ArrayList<String>();
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        String selectQuery = "Select * from "+t.getTableName()+" where MaMH = ? ";
        try {
            con = getConnection();
            statement = con.prepareStatement(selectQuery);
            statement.setObject(1, subjectCode);
            rs = statement.executeQuery();

            while (rs.next()) {
                results.add(rs.getString("MaMHTQ"));
            }
        } catch (SQLException ex) {
            throw new Exception(ex);
        } finally {
            close(rs, statement);
            close(con);
        }
        return results;
    }
}
