package uit.cnpm02.dkhp.DAO;

/**
 *
 * @author LocNguyen
 */
public class DAOFactory {

    private static AccountDAO accountDao;
    private static NewsDAO newsDao;

    public static AccountDAO getAccountDao() {
        if (accountDao != null) {
            accountDao = new AccountDAO();
        }
        return accountDao;
    }

    public static NewsDAO getNewsDao() {
        if (newsDao != null) {
            newsDao = new NewsDAO();
        }

        return newsDao;
    }
}
