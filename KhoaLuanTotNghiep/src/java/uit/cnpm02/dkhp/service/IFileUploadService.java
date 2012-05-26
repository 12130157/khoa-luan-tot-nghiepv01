package uit.cnpm02.dkhp.service;

import java.util.List;
import uit.cnpm02.dkhp.model.FileInfo;

/**
 *
 * @author LocNguyen
 */
public interface IFileUploadService {
    /**
     * Get all file and sub file of specified category
     * (just manage file in upload files folder).
     * @param parentCategory category
     * @return list file found.
     */
    void getFile(String parentCategory, List<FileInfo> results, final String author);
}
