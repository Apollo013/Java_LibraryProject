package Commands.Public;

import Application.CurrentUser;
import Application.Reader;
import Application.Writer;
import Business.User;
import Commands.AbstractCommand;
import DataAccess.IUserDao;
import DataAccess.UserDao;
import Exceptions.DaoException;

/**
 * @date 17/10/2013
 * @author Paul Millar <D00152098>
 */
public class LoginCommand extends AbstractCommand{

    public LoginCommand(String prompt) {
        super(prompt);
    }
  
    @Override
    public void execute() {
        
        try{                    
            // Prompt the user for their login details.
            String email = Reader.readString("Please enter your email address");
            String password = Reader.readString("Please enter your password");
        
            // Attempt to get matching login details
            IUserDao udo = new UserDao();
            User user = udo.getUserLogIn(email, password);

            // If login details could not be found, warn the user.
            if(user == null){
                Writer.printMessage("MEMBER CANNOT BE FOUND, PLEASE TRY AGAIN !!!");

            // Otherwise log the user in.
            } else {                
                CurrentUser.login(user);
            }
            
        } catch (DaoException daoe){
            Writer.printMessage(daoe.getMessage());
        }

    }
    
}
