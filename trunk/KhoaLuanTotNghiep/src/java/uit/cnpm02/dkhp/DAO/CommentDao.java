/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcDAO;
import uit.cnpm02.dkhp.model.Comment;

/**
 *
 * @author thanh
 */
public class CommentDao extends AbstractJdbcDAO<Comment, Integer>{
    public void updateCommentStatus(int id) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("Update khoaluantotnghiep.ykien set TinhTrang=(TinhTrang+1)%2 where MaYKien=").append(id).append("");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.execute();
        } catch (Exception ex) {
            throw ex;
        }
    }
    public void deleteCommentByID(int id) throws SQLException, Exception{
        StringBuffer sql = new StringBuffer();
            sql.append("Delete from khoaluantotnghiep.ykien where MaYKien=").append(id).append("");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.execute();
            updateCommentId(id);
    }
   private int getMaxNewsID() throws SQLException{
        String a = "";
        StringBuffer sql = new StringBuffer();
            sql.append("select max(MaYKien) from khoaluantotnghiep.ykien");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.execute();
            ResultSet rs = stmt.executeQuery();
        if ((rs != null) && rs.next()) {
            a = rs.getString("max(MaYKien))");
        }
        return Integer.parseInt(a);
   }
    public void updateCommentId(int id) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("Update khoaluantotnghiep.ykien set MaYKien=MaYKien-1 where MaYKien>").append(id).append("");
            PreparedStatement stmt = getConnection().prepareStatement(sql.toString());
            stmt.execute();
        } catch (Exception ex) {
            throw ex;
        }
    }
}
