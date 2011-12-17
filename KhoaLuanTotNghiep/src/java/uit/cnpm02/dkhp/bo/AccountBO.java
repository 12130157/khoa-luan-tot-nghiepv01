package uit.cnpm02.dkhp.bo;

import java.util.logging.Level;
import java.util.logging.Logger;
import uit.cnpm02.dkhp.DAO.AccountDAO;
import uit.cnpm02.dkhp.model.Account;

/**
 *
 * @author LocNguyen
 */
public class AccountBO {

    private AccountDAO accDao = new AccountDAO();

    public boolean Login(String userName, String password) {
        try {

            Account acc = accDao.findById(userName);

            if ((acc != null)
                    && (acc.getUserName().equals(userName))
                    && (acc.getPassword().equals(password))) {
                return true;
            }
        } catch (Exception ex) {
            Logger.getLogger(AccountBO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    public boolean  isLogined(String user) {
        try {
            Account acc = accDao.findById(user);
            if(acc != null) {
                if(acc.getIsLogined())
                    return true;
            }
        } catch (Exception ex) {
            Logger.getLogger(AccountBO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean isLock(String user) {
        try {
            Account acc = accDao.findById(user);
            if(acc != null) {
                if(acc.getStatus()==1)
                return true;
            }
        } catch (Exception ex) {
            Logger.getLogger(AccountBO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean Logout(String userName, String password) {
        return false;
    }

    /**
     *
     * @param user
     * @param newpass
     * @throws Exception
     */
    public void changePassWord(String user, String newpass) throws Exception {
        Account acc = accDao.findById(user);
        acc.setPassword(newpass);
        accDao.update(acc);
    }
}
