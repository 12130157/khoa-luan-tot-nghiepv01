package uit.cnpm02.dkhp.DAO;

/**
 *
 * @author LocNguyen
 */
public class DAOFactory {

    private static AccountDAO accountDao = null;
    private static NewsDAO newsDao = null;
    private static StudentDAO studentDao = null;

    public static StudentDAO getStudentDao() {
        if (studentDao == null) {
            studentDao = new StudentDAO();
        }
            
        return studentDao;
    }

    public static AccountDAO getAccountDao() {
        if (accountDao == null) {
            accountDao = new AccountDAO();
        }
        return accountDao;
    }

    public static NewsDAO getNewsDao() {
        if (newsDao == null) {
            newsDao = new NewsDAO();
        }

        return newsDao;
    }
}
