package Service;

import DAO.MessageDAO;
import Model.Message;
import java.util.*;


public class MessageService {
    private MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }
    public MessageService(MessageDAO mDAO) {
        messageDAO = mDAO;
    }
    public Message addMessage(Message m) {
        return messageDAO.insertMessage(m);
    }
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }
    public Message getMessageByID(int message_id) {
        return messageDAO.getMessage(message_id);
    }
    public Message deleteMessageById(int message_id) {
      return messageDAO.deleteMessageById(message_id);  
    }
    public Message updateMessageTextById(int message_id, String message_text){
        return messageDAO.updateMessageTextById(message_id, message_text);
    }
    public List<Message> getMessagesByUser(int posted_by) {
        return messageDAO.getMessageByUser(posted_by);
    }

    
}
