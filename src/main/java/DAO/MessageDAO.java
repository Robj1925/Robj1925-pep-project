package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    public List<Message> getMessageByUser(int posted_by) {
        Connection conn = ConnectionUtil.getConnection();
        List<Message> messagesByUser = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, posted_by);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Message temp = new Message(rs.getInt("message_id"), posted_by , rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messagesByUser.add(temp);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return messagesByUser;
    }
    public List<Message> getAllMessages() {
        Connection conn = ConnectionUtil.getConnection();
        List<Message> allMessages = new ArrayList<>();
        try {
           String sql = "SELECT * FROM message"; 
           PreparedStatement preparedStatement = conn.prepareStatement(sql);
           ResultSet rs = preparedStatement.executeQuery();

           while (rs.next()) {
            Message temp = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            allMessages.add(temp);
           }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return allMessages;
                
    }
    public Message insertMessage(Message m) {
        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(2, m.getMessage_text());
            ps.setInt(1, m.getPosted_by());
            ps.setLong(3, m.getTime_posted_epoch());
            ps.executeUpdate();

             ResultSet pkeyResultSet = ps.getGeneratedKeys();
             if (pkeyResultSet.next()) {
                int generated_message_id = (int) pkeyResultSet.getLong(1); // returns 1st index from DB (acc_id)
                return new Message(generated_message_id, m.getPosted_by(), m.getMessage_text(), m.getTime_posted_epoch());
              }
              
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }
    public Message updateMessageTextById(int message_id, String message_text) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(2, message_id);
            ps.setString(1, message_text);

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated == 1) {
                return getMessage(message_id);
            }          
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }
    public Message deleteMessageById(int message_id) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, message_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message temp = new Message(message_id, rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch")) ;
                return temp;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }
    public Message getMessage(int message_id) {
        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, message_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message temp = new Message(message_id, rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch")) ;
                return temp;
            }
            
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }
    
}
