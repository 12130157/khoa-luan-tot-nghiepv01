package uit.cnpm02.dkhp.model;

import java.util.Date;
import uit.cnpm02.dkhp.access.JDBC.AbstractJdbcModel;
import uit.cnpm02.dkhp.access.mapper.MapperConstant;
import uit.cnpm02.dkhp.model.type.FileType;
import uit.cnpm02.dkhp.model.type.FileUploadStatus;

/**
 *
 * @author LocNguyen
 */
public class FileInfo extends AbstractJdbcModel<Integer> {
    private String fileName;
    private String author;
    private String category;
    private Date created;
    private FileUploadStatus status;
    private FileType type;

    public FileInfo() {
    }

    public FileInfo(String fileName, String author, String category,
            Date created, FileType type) {
        this.fileName = fileName;
        this.author = author;
        this.category = category;
        this.created = created;
        this.status = FileUploadStatus.NOT_PROCESS;
        this.type = type;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }
    
    public FileUploadStatus getStatus() {
        return status;
    }

    public void setStatus(FileUploadStatus status) {
        this.status = status;
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

    public Date getCreatedTime() {
        return created;
    }

    public void setCreatedTime(Date created) {
        this.created = created;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String[] getColumnNames() {
        return new String[] {
            "TenFile",
            "NguoiTao",
            "ThuMuc",
            "NgayTao",
            "TrangThai",
            "Loai"
        };
    }

    @Override
    public Object[] getColumnValues() {
        return new Object[] {
            fileName,
            author,
            category,
            created,
            status == null ? FileUploadStatus.UNKNOWN.value() : status.value(),
            type == null ? FileType.UNKNOWN.value() : type.value()
        };
    }

    @Override
    public void setColumnValues(Object[] values) {
        try {
            fileName = values[0].toString();
            author = values[1].toString();
            category = values[2].toString();
            created = (Date) values[3];
            status = FileUploadStatus.getFileUploadStatus(
                Integer.parseInt(values[4].toString()));
            type = FileType.getFileType(Integer.parseInt(values[5].toString()));
        } catch (Exception ex) {
            type = FileType.UNKNOWN; // For debug purpose.
        }
    }

    @Override
    public String getTableName() {
        return MapperConstant.DB_NAME
                + ".FileInfo";
    }

    @Override
    public boolean isIdAutoIncrement() {
        return true;
    }
    
    
    
}
