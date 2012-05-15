package uit.cnpm02.dkhp.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import uit.cnpm02.dkhp.DAO.AccountDAO;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.model.Account;
import uit.cnpm02.dkhp.service.IAccountService;
import uit.cnpm02.dkhp.utilities.Constants;
import uit.cnpm02.dkhp.utilities.ExecuteResult;
import uit.cnpm02.dkhp.utilities.StringUtils;
import uit.cnpm02.dkhp.utilities.password.PasswordProtector;

/**
 *
 * @author LocNguyen
 */
public class AccountServiceImpl implements IAccountService {
    private AccountDAO accountDao;
    
    private int numsPerPage = Constants.ELEMENT_PER_PAGE_DEFAULT;
    
    /**SessionID - List account.**/
    private Map<String, List<Account>> accountMap
            = new HashMap<String, List<Account>>();
    
    /**SessionID - List account.**/
    private Map<String, String> sortTypeMap
            = new HashMap<String, String>();
    //private List<Account> accounts = new ArrayList<Account>(10);
    
    public AccountServiceImpl() {
        accountDao = DAOFactory.getAccountDao();
    }

    @Override
    public Account addAccount(Account acc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ExecuteResult deleteAccount(String username, String sessionId) {
        ExecuteResult result = new ExecuteResult(true, "");
        try {
            Account acc = accountDao.findById(username);
            if (acc == null) {
                return new ExecuteResult(false, "Account không tồn tại.");
            } else {
                accountDao.delete(acc);
                accountMap.remove(sessionId);
                getAccount(1, sessionId);
            }
        } catch (Exception ex) {
            Logger.getLogger(AccountServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            result.setIsSucces(false);
            result.setMessage("Error: " + ex.toString());
        }
        
        return result;
    }

    @Override
    public boolean changePassword(String username, String newPwd) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Account> getAccount(int page, String sessionId) {
        List<Account> accounts = new ArrayList<Account>(10);
        
        //accounts = accountMap.get(sessionId);
        try {
            accounts = accountDao.findAll(numsPerPage,
                                page, "TenDangNhap", "DESC");
            //accountMap.remove(sessionId);
            accountMap.put(sessionId, accounts);
        } catch (Exception ex) {
            Logger.getLogger(AccountServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return accounts;
    }
    
    @Override
    public List<Account> getAccount(String sessionId) {
        return accountMap.get(sessionId);
    }

    @Override
    public List<Account> search(String key, String sessionId) {
        
        List<Account> accounts = new ArrayList<Account>(10);
        try {
            if ((key == null)
                    || (key.isEmpty())
                    || (key.equals("*"))
                    || (key.equalsIgnoreCase("all"))) {
                accounts = accountDao.findAll();
            } else {
                // Search by username
                accounts = accountDao.findByColumName("TenDangNhap", key);
            
                // Search by full name
                List<Account> acc_temps = accountDao.findByColumName("HoTen", key);
                if ((acc_temps != null) && !acc_temps.isEmpty()) {
                    for (Account acc : acc_temps) {
                        if (!accounts.contains(acc)) {
                            accounts.add(acc);
                        }
                    }
                }
            }
            
            if (!accounts.isEmpty()) {
                //accountMap.remove(sessionId);
                accountMap.put(sessionId, accounts);
            }
            
        } catch (Exception ex) {
            Logger.getLogger(AccountServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return accounts;
    }

    @Override
    public List<Account> sort(final String by, String sessionId) {
        String type = sortTypeMap.get(sessionId);
        if (StringUtils.isEmpty(type)) {
            type = "ASC";
        }
        
        if (type.equals("ASC")) {
            sortTypeMap.put(sessionId, "DES");
        } else {
            sortTypeMap.put(sessionId, "ASC");
        }
        
        List<Account> accounts = accountMap.get(sessionId);
        if ((accounts == null) || accounts.isEmpty()) {
            try {
                accounts = accountDao.findAll(numsPerPage,
                        1, "TenDangNhap", "DESC");
            } catch (Exception ex) {
                Logger.getLogger(AccountServiceImpl.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
        
        if ((accounts != null) && (!accounts.isEmpty())) {
            final String sortType_final = type;
            Collections.sort(accounts, new Comparator<Account>() {
                @Override
                public int compare(Account o1, Account o2) {
                    if (sortType_final.equals("ASC")) {
                        return o1.compare(o2, by);
                    } else {
                        return o2.compare(o1, by);
                    }
                }
            });
        }
        accountMap.put(sessionId, accounts);

        return accounts;
    }
    
    @Override
    public Account findAccount(String userName) {
        try {
            return accountDao.findById(userName);
        } catch (Exception ex) {
            Logger.getLogger(AccountServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public ExecuteResult update(Account account, String sessionId) {
        ExecuteResult result = new ExecuteResult(true, "");
        try {
            if (accountDao.findById(account.getId()) == null) {
                return new ExecuteResult(
                        false, "Không tìm thấy tài khoản: " + account.getId());
            }
            String pass = PasswordProtector.getMD5(account.getPassword());
            account.setPassword(pass);
            accountDao.update(account);
            getAccount(1, sessionId);
        } catch (Exception ex) {
            Logger.getLogger(AccountServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            return new ExecuteResult(
                        false, "Lỗi từ server: " + ex.toString());
        }
        
        return result;
    }

    @Override
    public ExecuteResult createNew(Account account, String sessionId) {
        ExecuteResult result = new ExecuteResult(true, "");
        try {
            if (accountDao.findById(account.getId()) != null) {
                return new ExecuteResult(
                        false, "Tài khoản đã có (" + account.getId() + ")");
            }
            String pass = PasswordProtector.getMD5(account.getPassword());
            account.setPassword(pass);
            accountDao.add(account);
            getAccount(1, sessionId);
        } catch (Exception ex) {
            Logger.getLogger(AccountServiceImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            return new ExecuteResult(
                        false, "Lỗi từ server: " + ex.toString());
        }
        
        return result;
    }
}
