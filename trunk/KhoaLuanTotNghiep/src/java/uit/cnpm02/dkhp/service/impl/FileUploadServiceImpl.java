package uit.cnpm02.dkhp.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import uit.cnpm02.dkhp.service.IFileUploadService;
import uit.cnpm02.dkhp.utilities.Constants;
import uit.cnpm02.dkhp.utilities.FileInfo;

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
    
    private FileInfo getFileInfo(File file, String author, String category) {
        FileInfo fi = new FileInfo();
        fi.setFileName(file.getName());
        fi.setAuthor(author);
        fi.setCategory(category);
        fi.setLastModified(new Date(file.lastModified()));
        
        return fi;
    }
    
}
