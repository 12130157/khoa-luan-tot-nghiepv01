/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.model;

import java.util.Date;
import uit.cnpm02.dkhp.access.mapper.MapperConstant;
import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcModel;

/**
 *
 * @author thanh
 */
public class Comment extends  AbstractJdbcModel<String>{
    String commentCode;
    String content;
    String author;
    Date createDate;
    String status;
    public Comment(){
        
    }
    public Comment(String _commentCode, String _content, String _author, Date _createDate, String _status){
        this.commentCode=_commentCode;
        this.content=_content;
        this.author=_author;
        this.createDate=_createDate;
        this.status=_status;
    }
    //************
    //set parameter
    //************
    public void setCommentCode(String _commentCode){
        this.commentCode=_commentCode;
    }
    public void setContent(String _content){
        this.content=_content;
    }
    public void setAuthor(String _author){
        this.author=_author;
    }
    public void setCreateDate(Date _createDate){
        this.createDate=_createDate;
    }
    public void setStatus(String _status){
        this.status=_status;
    }
    //************
    //get parameter
    //************
    public String getCommentCode(){
        return this.commentCode;
    }
    public String getContent(){
        return this.content;
    }
    public String getAuthor(){
        return this.author;
    }
    public Date getCreateDate(){
        return this.createDate;
    }
    public String getStatus(){
        return this.status;
    }
     @Override
    public String getIdColumnName() {
        return "MaYKien";
    }
     @Override
    public String getTableName() {
        return MapperConstant.DB_NAME
                + ".YKien";
    }
    @Override
    public String[] getColumnNames() {
        return new String[]{
                    "NoiDung",
                    "TacGia",
                    "NgayGui",
                    "TinhTrang"
                };
    }

   @Override
    public void setColumnValues(Object[] values) {
        try {
            content = values[0].toString();
            author = values[1].toString();
            createDate = (Date)values[2];
            status = values[3].toString();

        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
     @Override
    public Object[] getColumnValues() {
        return new Object[]{
                    commentCode,
                    content,
                    author,
                    createDate,
                    status
                };
    }
}
