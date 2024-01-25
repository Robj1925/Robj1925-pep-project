package Service;

import java.util.List;



import DAO.UserDAO;
import Model.Account;

public class AccountService {
    private UserDAO userDAO;

    public AccountService() {
        userDAO = new UserDAO();      
    }
    public AccountService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    public List<Account> getAllAccounts() {
        return userDAO.getAllAccounts();
    }
    public Account addAccount(Account account) {
        return userDAO.insertAccount(account);
        
    }

    
}
