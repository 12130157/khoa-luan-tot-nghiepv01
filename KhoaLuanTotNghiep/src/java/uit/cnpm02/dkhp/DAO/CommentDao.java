/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcDAO;
import uit.cnpm02.dkhp.model.Comment;

/**
 *
 * @author thanh
 */
public class CommentDao extends AbstractJdbcDAO<Comment, Integer>{
    public void updateCommentStatus(int id) throws Exception {
        checkModelWellDefined();
        Comment t = new Comment();
        if (t == null) {
            throw new Exception("Cannot initialize the " + Comment.class.getName()
                    + " class");
        }
        Connection con = null;
        PreparedStatement statement = null;
        String selectQuery = "Update "+t.getTableName()+" set TinhTrang=(TinhTrang+1)%2 where MaYKien= ? ";
        try {
            con = getConnection();
            statement = con.prepareStatement(selectQuery);
            statement.setObject(1, id);
            statement.execute();

        } catch (SQLException ex) {
            throw new Exception(ex);
        } finally {
           close(con);
        }
      }
   public void deleteCommentByID(int id) throws Exception {
        checkModelWellDefined();
        Comment t = new Comment();
        if (t == null) {
            throw new Exception("Cannot initialize the " + Comment.class.getName()
                    + " class");
        }
        Connection con = null;
        PreparedStatement statement = null;
        String selectQuery = "Delete from "+t.getTableName()+" where MaYKien= ? ";
        try {
            con = getConnection();
            statement = con.prepareStatement(selectQuery);
            statement.setObject(1, id);
            statement.execute();

        } catch (SQLException ex) {
            throw new Exception(ex);
        } finally {
           close(con);
        }
        updateCommentId(id);
    }
   public void updateCommentId(int id) throws Exception {
       checkModelWellDefined();
        Comment t = new Comment();
        if (t == null) {
            throw new Exception("Cannot initialize the " + Comment.class.getName()
                    + " class");
        }
        Connection con = null;
        PreparedStatement statement = null;
        String selectQuery = "Update "+t.getTableName()+" set MaYKien=MaYKien-1 where MaYKien > ? ";
        try {
            con = getConnection();
            statement = con.prepareStatement(selectQuery);
            statement.setObject(1, id);
            statement.execute();
         } catch (SQLException ex) {
            throw new Exception(ex);
        } finally {
           close(con);
        }
      }
}
