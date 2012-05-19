package uit.cnpm02.dkhp.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcDAO;
import uit.cnpm02.dkhp.model.Lecturer;

/**
 *
 * @author thanh
 */
public class LecturerDAO extends AbstractJdbcDAO<Lecturer, String>{

    public Object findByIdentifier(String cmnd) throws Exception {
        checkModelWellDefined();

        if ((cmnd == null) || (cmnd.isEmpty())) {
            throw new NullPointerException("cmnd is null.");
        }
        
        Lecturer t = new Lecturer();
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            String selectQuery = "Select * from " + t.getTableName() + " where CMND = ?";
            
            con = getConnection();
            statement = con.prepareStatement(selectQuery);
            statement.setObject(1, cmnd);
            rs = statement.executeQuery();
            if (rs.next()) {
                Object[] obj = new Object[t.getColumnNames().length];
                for (int j = 0; j < obj.length; j++) {
                    obj[j] = rs.getObject(t.getColumnNames()[j]);
                }
                Lecturer ti = new Lecturer();
                String id = (String) rs.getString(ti.getIdColumnName());

                ti.setId(id);
                ti.setColumnValues(obj);
                return ti;
            }
            return null;
        } catch (SQLException e) {
            throw new Exception(e);
        } finally {
            close(rs, statement);
            close(con);
        }
    }
    
}
