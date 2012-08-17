package uit.cnpm02.dkhp.service;

import java.io.File;
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
    
    /**
     * All score sheet sent from lecturer will be
     * lacate at upload folder
     * 
     * This function will retrive all score sheet
     * for update score process
     * 
     * @return all score sheet sent by lecturers
     */
    List<FileInfo> getSheetFileFromLecturer();

    public File getFile(String fileName);
    public File getNewsAttachedFile(String fileName);
}
