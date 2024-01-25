package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import  Model.Account;
import Util.ConnectionUtil;


public class UserDAO {

//     ## 1: Our API should be able to process new User registrations.

// As a user, I should be able to create a new Account on the endpoint POST localhost:8080/register. 
//The body will contain a representation of a JSON Account, but will not contain an account_id.
// - The registration will be successful if and only if the username is not blank, 
// the password is at least 4 characters long, and an Account with that username does not already exist. 
// If all these conditions are met, the response body should contain a JSON of the Account, including its account_id. 
// The response status should be 200 OK, which is the default. The new account should be persisted to the database.
// - If the registration is not successful, the response status should be 400. (Client error)
    public List<Account> getAllAccounts() {
        Connection conn = ConnectionUtil.getConnection();
        List<Account> allAccounts = new ArrayList<>();
        try {
            String sqlStatement = "SELECT * FROM account";
          PreparedStatement preparedStatement = conn.prepareStatement(sqlStatement);
          ResultSet rs = preparedStatement.executeQuery();

          while (rs.next()) {
            Account temp = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            allAccounts.add(temp);

          }
 
        } catch (SQLException e) {
          System.out.println(e.getMessage());
        }
       
        return allAccounts;

    }
    public Account insertAccount(Account account) {
      Connection conn = ConnectionUtil.getConnection();
      try {
          String sql = "INSERT INTO account (username, password) VALUES(?, ?)";
          PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

          preparedStatement.setString(1, account.getUsername());
          preparedStatement.setString(2, account.getPassword());

          preparedStatement.executeUpdate();
          ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys(); // returns Acc from DB (Has Account_ID in it)
          if (pkeyResultSet.next()) {
            int generated_account_id = (int) pkeyResultSet.getLong(1); // returns 1st index from DB (acc_id)
            return new Account(generated_account_id, account.getUsername(), account.getPassword());
          }
      } catch(SQLException e) {
          System.out.println(e);
      }
      return null;


    }
}
