/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.mapper.MapperConstant;
import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcModel;

/**
 *
 * @author thanh
 */
public class Comment extends  AbstractJdbcModel<Integer>{
    String content;
    String author;
    String createDate;
    int status;
    public Comment(){
        
    }
    public Comment(int id, String _content, String _author, String _createDate, int _status){
       setId(id);
        this.content=_content;
        this.author=_author;
        this.createDate=_createDate;
        this.status=_status;
    }
    //************
    //set parameter
    //************
     public void setContent(String _content){
        this.content=_content;
    }
    public void setAuthor(String _author){
        this.author=_author;
    }
    public void setCreateDate(String _createDate){
        this.createDate=_createDate;
    }
    public void setStatus(int _status){
        this.status=_status;
    }
    //************
    //get parameter
    //************
     public String getContent(){
        return this.content;
    }
    public String getAuthor(){
        return this.author;
    }
    public String getCreateDate(){
        return this.createDate;
    }
    public int getStatus(){
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
            createDate = values[2].toString();
            status =Integer.parseInt(values[3].toString());

        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
     @Override
    public Object[] getColumnValues() {
        return new Object[]{
                    content,
                    author,
                    createDate,
                    status
                };
    }
}
