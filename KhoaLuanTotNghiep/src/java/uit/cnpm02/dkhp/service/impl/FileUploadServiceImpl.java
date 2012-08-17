package uit.cnpm02.dkhp.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uit.cnpm02.dkhp.DAO.AccountDAO;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.model.Account;
import uit.cnpm02.dkhp.service.IFileUploadService;
import uit.cnpm02.dkhp.utilities.Constants;
import uit.cnpm02.dkhp.model.FileInfo;
import uit.cnpm02.dkhp.model.type.AccountType;
import uit.cnpm02.dkhp.utilities.StringUtils;

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
                // File not by lecturer
                String folderName = files[i].getName(); // this username
                AccountDAO accDao = DAOFactory.getAccountDao();
                try {
                    Account acc = accDao.findById(folderName);
                    if ((acc == null) || (acc.getType() != AccountType.LECTUTER.value())) {
                        continue;
                    }
                } catch (Exception ex) {
                    Logger.getLogger(FileUploadServiceImpl.class.getName())
                            .log(Level.SEVERE, null, ex);
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
    
    @Override
    public File getNewsAttachedFile(String fileName) {
        // Should check to find file from all admin account
        List<FileInfo> fileAttached = getFileByUserName("admin");
        if (fileAttached != null && !fileAttached.isEmpty()) {
            for (FileInfo fi : fileAttached) {
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
        }
        return null;
    }
    
    private List<FileInfo> getFileByUserName(String username) {
        if(StringUtils.isEmpty(username)) {
            return null;
        }
        List<FileInfo> fileInfos = new ArrayList<FileInfo>(10);
        
        String path = Constants.FILEUPLOAD_DIR;
        File root = new File(path);
        if (!root.exists()) {
            return null;
        }
        
        File[] files = root.listFiles();
        if (files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                if (!files[i].isDirectory() || !files[i].getName().equals(username)) {
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
    
}
