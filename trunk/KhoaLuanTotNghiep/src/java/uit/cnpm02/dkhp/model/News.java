package uit.cnpm02.dkhp.model;

import java.util.Date;
import uit.cnpm02.dkhp.access.mapper.MapperConstant;
import uit.cnpm02.dkhp.access.util.AbstractJdbcModel;

/**
 *
 * @author LocNguyen
 */
public class News extends AbstractJdbcModel<String> {

    private String idNews;
    private String title;
    private String content;
    private String author;
    private Date createdDate;
    private int type;

    public News() {
        super();
    }

    public News(String id, String title, String content, String author, Date createdDate, int type) {
        this.idNews = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdDate = createdDate;
        this.type = type;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getIdNews() {
        return idNews;
    }

    public void setIdNews(String id) {
        this.idNews = id;
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
    public boolean isIdAutoIncrement() {
        return false;
    }

    @Override
    public String[] getColumnNames() {
        return new String[]{
                    "TieuDe",
                    "NoiDung",
                    "NguoiDang",
                    "NgayTao",
                    "Loai"
                };
    }

    @Override
    public Object[] getColumnValues() {
        return new Object[]{
                    title,
                    content,
                    author,
                    createdDate,
                    type
                };
    }

    @Override
    public void setColumnValues(Object[] values) {
        //Date date = results.getDate(column);
        //DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        //System.out.println("Formatted date: " + df.format(date));
        try {
            title = values[0].toString();
            content = values[1].toString();
            author = values[2].toString();
            createdDate = (Date)values[3];
            type = Integer.parseInt(values[4].toString());

        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
