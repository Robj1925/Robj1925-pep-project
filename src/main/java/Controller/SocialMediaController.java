package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postAccountHandler);
        app.get("/accounts", this::getAllAccountsHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("messages", this::getAllMessagesHandler);
        app.get("messages/{message_id}", this::getMessageByIdHandler);
        app.delete("messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("messages/{message_id}", this::updateMessageTextByIdHandler);
        app.get("accounts/{account_id}/messages", this::getMessagesByUser);


        return app;
    }
    private void getMessagesByUser(Context context) {
        int account_id = Integer.parseInt(context.pathParam("account_id"));
        List<Message> allMessagesByUser = messageService.getMessagesByUser(account_id);
        context.json(allMessagesByUser);

    }
    private void updateMessageTextByIdHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message newMsg = mapper.readValue(context.body(), Message.class);

        int message_id = Integer.parseInt(context.pathParam("message_id"));
        if (messageService.getMessageByID(message_id) != null) {

          if (!newMsg.getMessage_text().isEmpty() && newMsg.getMessage_text().length() <= 255) {
                 Message temp = messageService.updateMessageTextById(message_id, newMsg.getMessage_text());
                context.json(mapper.writeValueAsString(temp));
            } else {
                context.status(400);
            }        
        } else {
            context.status(400);
        }
    }
    private void deleteMessageByIdHandler(Context context) {
      int message_id = Integer.parseInt(context.pathParam("message_id"));
      Message temp = messageService.getMessageByID(message_id);
      if (temp != null) {
        context.json(temp);
      } else {
        context.result("");
      }
    }
    private void getMessageByIdHandler(Context context) {
        try {
            int messageId = Integer.parseInt(context.pathParam("message_id"));
            Message temp = messageService.getMessageByID(messageId);
            if (temp != null) {
                context.json(temp);
            } else {
                context.result("");
            }
            
        } catch (NumberFormatException e) {
            context.status(400);
        }
    }
    private void getAllMessagesHandler(Context context) {
        List<Message> allMessages = messageService.getAllMessages();
        context.json(allMessages);
    }
    private void getAllAccountsHandler(Context context) {
        List<Account> accounts = accountService.getAllAccounts();
        context.json(accounts);
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postAccountHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class); //json we receive gets turned into acc obj
        Account addedAccount = null;
        for (Account acc : accountService.getAllAccounts()) {
            if (acc.getUsername().toLowerCase().equals(account.getUsername().toLowerCase())) { // check 4 dup username
                context.status(400);
            }
        }
        if (!account.getUsername().isEmpty() && account.getPassword().length() > 4) { //chek 4 empty username & pw > 4
            addedAccount = accountService.addAccount(account);
        }
        

        if (addedAccount != null) {
            context.json(mapper.writeValueAsString(addedAccount));
        } else {
            context.status(400);
        }
    }
    private void postLoginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class); //json we receive gets turned into acc obj
        for (Account acc : accountService.getAllAccounts()) {
            if (account.getPassword().equals(acc.getPassword()) && account.getUsername().equals(acc.getUsername())) {
                context.json(mapper.writeValueAsString(acc));
                return;
            }
        }
        context.status(401);
    }
    private void postMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message addedMessage = null;    
        boolean isExisting = false;

        for (Account acc: accountService.getAllAccounts()) {
            if (acc.getAccount_id() == message.posted_by) {
                isExisting = true;
            }
        }
        if (isExisting && !message.getMessage_text().isEmpty() && message.getMessage_text().length() <= 255) {
            addedMessage = messageService.addMessage(message);
            context.json(mapper.writeValueAsString(addedMessage));
        } else {
            context.status(400);
        }
         
    }


}