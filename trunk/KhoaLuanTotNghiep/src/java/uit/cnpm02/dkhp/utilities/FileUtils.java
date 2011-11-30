package uit.cnpm02.dkhp.utilities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    public static HSSFWorkbook getWorkbook(HttpServletRequest req, HttpServletResponse response) throws IOException, FileUploadException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(req);
        if (isMultipart) {
            ServletFileUpload upload = new ServletFileUpload();
            FileItemIterator iter;
            String fileNamSource = req.getParameter("txtPath");
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
                            String fileName = new File(item.getName()).getName();
                            POIFSFileSystem fs = new POIFSFileSystem(stream);
                            HSSFWorkbook wb = new HSSFWorkbook(fs);
                            return wb;
                        }
                    }
                }
            } catch (FileUploadException ex) {
                response.getWriter().println(ex.toString());
            } catch (IOException ex) {
                response.getWriter().println(ex.toString());
            }
        }
        return null;
    }
}
