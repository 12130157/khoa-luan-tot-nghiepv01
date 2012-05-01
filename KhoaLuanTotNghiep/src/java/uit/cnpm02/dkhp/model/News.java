package uit.cnpm02.dkhp.model;

import uit.cnpm02.dkhp.access.mapper.MapperConstant;
import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcModel;

/**
 *
 * @author LocNguyen
 */
public class News extends AbstractJdbcModel<Integer> {

    private String title;
    private String content;
    private String author;
    private String createdDate;
    private int type;
    private String imgPath;

    public News() {
        super();
    }

    public News(int id, String title, String content, String author, String createdDate, int type) {
        setId(id);
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdDate = createdDate;
        this.type = type;
        imgPath = "";
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
    
    public String getImgPath() {
        return imgPath;
    }
    
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String getIdColumnName() {
        return "MaTin";
    }

    @Override
    public String getTableName() {
        return MapperConstant.DB_NAME
                + ".BanTin";
    }

    @Override
    public String[] getColumnNames() {
        return new String[]{
                    "TieuDe",
                    "NoiDung",
                    "NguoiDang",
                    "NgayTao",
                    "Loai",
                    "HinhDaiDien"
                };
    }

    @Override
    public Object[] getColumnValues() {
        return new Object[]{
                    title,
                    content,
                    author,
                    createdDate,
                    type,
                    imgPath
                };
    }

    @Override
    public void setColumnValues(Object[] values) {
        try {
            title = values[0].toString();
            content = values[1].toString();
            author = values[2].toString();
            createdDate = values[3].toString();
            type = Integer.parseInt(values[4].toString());
            imgPath = (values[5] == null) ? null : values[5].toString();

        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
