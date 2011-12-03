package uit.cnpm02.dkhp.utilities;

import java.sql.Time;
import java.util.Date;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.DAO.DiaryDAO;
import uit.cnpm02.dkhp.model.Diary;

/**
 *
 * @author LocNguyen
 */
public class Log {
    private DiaryDAO diaryDao = DAOFactory.getDiaryDao();
    private static Log INSTANCE = null;
    
    public Log() {
        
    }
    
    public static Log getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Log();
        }

        return new Log();
    }
    
    public void log(String userName, String content) {
        try {
            Diary d = new Diary(userName, content, new Date(), new Time(System.currentTimeMillis()));
            diaryDao.add(d);
        } catch (Exception ex) {
            //
        }
    }
}
