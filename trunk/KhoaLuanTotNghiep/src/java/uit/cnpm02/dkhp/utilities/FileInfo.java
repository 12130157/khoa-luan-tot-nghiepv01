package uit.cnpm02.dkhp.utilities;

import java.util.Date;

/**
 *
 * @author LocNguyen
 */
public class FileInfo {
    private String fileName;
    private String author;
    private String category;
    private Date lastModified;

    public FileInfo() {
    }

    public FileInfo(String fileName, String author, String category, Date lastModified) {
        this.fileName = fileName;
        this.author = author;
        this.category = category;
        this.lastModified = lastModified;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
}
