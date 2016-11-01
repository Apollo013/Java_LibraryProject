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
public class RegisterCommand extends AbstractCommand{

    public RegisterCommand(String prompt) {
        super(prompt);
    }
    
    @Override
    public void execute() {

        try{            
            // Create an empty user
            User newUser = new User();

            // Prompt the user for their details
            newUser.setName(Reader.readString("Please Enter Your Name"));
            newUser.setEmail(Reader.readString("Please Enter Your Email Address"));
            newUser.setPassword(Reader.readString("Please Enter Your Password"));  
            newUser.setIsAdmin(false); 
        
            // attempt an insert
            IUserDao udo = new UserDao();
            if(udo.insert(newUser)){
                // Successful registration so log user in automatically
                Writer.printMessage("YOUR REGISTRATION WAS SUCCESSFUL !!!");
                CurrentUser.login(newUser);                
            } else {
                System.out.println("\n THIS EMAIL IS ALREADY IN USE !");
            } 
  
        }
        catch(DaoException daoe){
            Writer.printMessage(daoe.getMessage());
        }                        
    }
    
}
