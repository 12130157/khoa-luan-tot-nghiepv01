package uit.cnpm02.dkhp.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import uit.cnpm02.dkhp.service.IFileUploadService;
import uit.cnpm02.dkhp.utilities.Constants;
import uit.cnpm02.dkhp.model.FileInfo;

/**
 *
 * @author LocNguyen
 */
public class FileUploadServiceImpl implements IFileUploadService {
    
    @Override
    public void getFile(String parentCategory, List<FileInfo> results, final String author) {
        if (results == null) {
            results = new ArrayList<FileInfo>(10);
        }
        
        String path = Constants.FILEUPLOAD_DIR + File.separator + parentCategory;
        File root = new File(path);
        if (!root.exists()) {
            return;
        }
        
        File[] files = root.listFiles();
        if (files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    getFile(parentCategory + File.separator + files[i].getName(),
                            results,
                            author);
                } else {
                    FileInfo fi = getFileInfo(files[i],
                            parentCategory, author);
                    results.add(fi);
                }
            }
        }
    }
    
    private void internal_getFiles(String parentCategory, List<FileInfo> results, final String author) {
        if (results == null) {
            results = new ArrayList<FileInfo>(10);
        }
        
        String path = Constants.FILEUPLOAD_DIR + File.separator + parentCategory;
        File root = new File(path);
        if (!root.exists()) {
            return;
        }
        
        File[] files = root.listFiles();
        if (files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    getFile(parentCategory + File.separator + files[i].getName(),
                            results,
                            author);
                } else {
                    FileInfo fi = getFileInfo(files[i],
                            parentCategory, author);
                    results.add(fi);
                }
            }
        }
    }
    
    private FileInfo getFileInfo(File file, String author, String category) {
        FileInfo fi = new FileInfo();
        fi.setFileName(file.getName());
        fi.setAuthor(author);
        fi.setCategory(category);
        fi.setCreatedTime(new Date(file.lastModified()));
        
        return fi;
    }

    @Override
    public List<FileInfo> getSheetFileFromLecturer() {
        List<FileInfo> fileInfos = new ArrayList<FileInfo>(10);
        
        String path = Constants.FILEUPLOAD_DIR;
        File root = new File(path);
        if (!root.exists()) {
            return null;
        }
        
        File[] files = root.listFiles();
        if (files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                if (!files[i].isDirectory()) {
                    continue;
                }
                
                List<FileInfo> temps = new ArrayList<FileInfo>(10);
                getFile(files[i].getName(), temps, files[i].getName());
                
                if (!temps.isEmpty()) {
                    fileInfos.addAll(temps);
                }
            }
        }
        
        return fileInfos;
    }

    @Override
    public File getFile(String fileName) {
        List<FileInfo> filesInfos = getSheetFileFromLecturer();
        
        if ((filesInfos == null) || filesInfos.isEmpty()) {
            return null;
        }
        
        for (FileInfo fi : filesInfos) {
            if (fi.getFileName().equals(fileName)) {
                // Retrieve file
                File f = new File(Constants.FILEUPLOAD_DIR 
                        + File.separator + fi.getCategory() + File.separator + fileName);
                if (!f.exists())
                    return null;
                else
                    return f;
            }
        }
        // File not found.
        return null;
        
    }
    
}
