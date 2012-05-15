package uit.cnpm02.dkhp.service;

import java.util.List;
import uit.cnpm02.dkhp.model.Account;
import uit.cnpm02.dkhp.utilities.ExecuteResult;

/**
 *
 * @author LocNguyen
 */
public interface IAccountService {
    
    Account addAccount(Account acc);
    ExecuteResult deleteAccount(String username, String sessionId);
    /**
     * Update password for specified user
     * @param username specify user
     * @param newPwd new password (in text plain)
     * @return true if update successfull; false if not
     */
    boolean changePassword(String username, String newPwd);
    
    /**
     * List All account
     * @return 
     */
    List<Account> getAccount(int page, String sessionId);
    List<Account> getAccount(String sessionId);
    List<Account> search(String key, String sessionId);
    List<Account> sort(final String by, String sessionId);

    public Account findAccount(String userName);

    public ExecuteResult update(Account account, String sessionId);

    public ExecuteResult createNew(Account account, String sessionId);
    
}
