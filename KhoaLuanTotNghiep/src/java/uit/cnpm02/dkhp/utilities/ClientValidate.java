/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uit.cnpm02.dkhp.utilities;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uit.cnpm02.dkhp.DAO.AccountDAO;
import uit.cnpm02.dkhp.DAO.DAOFactory;
import uit.cnpm02.dkhp.model.Account;
import uit.cnpm02.dkhp.model.type.AccountType;

/**
 *
 * @author LocNguyen
 */
public class ClientValidate {
    public static void validateAcess(AccountType accountType, HttpSession session,
            HttpServletResponse response) {
        String invalidatePath = "../Message.jsp";
        String user = (String) session.getAttribute("username");
        boolean validated = false;
        if (!StringUtils.isEmpty(user)) {
            AccountDAO accDao = DAOFactory.getAccountDao();
            try {
                Account acc = accDao.findById(user);
                if ((acc != null) && (acc.getType() == accountType.value())) {
                    validated = true;
                }
            } catch (Exception ex) {
                Logger.getLogger(ClientValidate.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
        
        if (!validated) {
            try {
                response.sendRedirect(invalidatePath);
            } catch (IOException ex) {
                Logger.getLogger(ClientValidate.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }
}
