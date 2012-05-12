package uit.cnpm02.dkhp.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 *
 * @author LocNguyen
 */
public class FileUtils {

    public static HSSFWorkbook getWorkbook(HttpServletRequest req)
                            throws IOException, FileUploadException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(req);
        if (isMultipart) {
            ServletFileUpload upload = new ServletFileUpload();
            FileItemIterator iter;
            try {
                iter = upload.getItemIterator(req);
                FileItemStream item = null;
                String name = "";
                InputStream stream = null;
                while (iter.hasNext()) {
                    item = iter.next();
                    name = item.getFieldName();
                    stream = item.openStream();
                    if (item.isFormField()) {
                        continue;
                    } else {
                        name = item.getName();
                        if (name != null && !"".equals(name)) {
                            //String fileName = new File(item.getName()).getName();
                            POIFSFileSystem fs = new POIFSFileSystem(stream);
                            HSSFWorkbook wb = new HSSFWorkbook(fs);
                            return wb;
                        }
                    }
                }
            } catch (FileUploadException ex) {
                 Logger.getLogger(FileUtils.class.getName())
                        .log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FileUtils.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
