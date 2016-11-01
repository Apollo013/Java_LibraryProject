package Commands.Member;

import Application.CurrentUser;
import Application.Reader;
import Application.Writer;
import Business.User;
import Commands.AbstractCommand;
import DataAccess.IUserDao;
import DataAccess.UserDao;
import Exceptions.DaoException;

/**
 *
 * @author Paul Millar <D00152098>
 */
public class UpdateCurrentUserDetailsCommand extends AbstractCommand{

    public UpdateCurrentUserDetailsCommand(String prompt) {
        super(prompt);
    }

    @Override
    public void execute() {
        try{            
            
            // Print out the details of the current user
            Writer.printMessage("YOUR CURRENT DETAILS ARE ...");
            Writer.printHeader(User.HEADER); 
            System.out.println(CurrentUser.getInstance().toString());

            // Create an empty user
            User updateUser = new User();

            // Prompt the user for their details
            updateUser.setName(Reader.readString("Please Enter Your Name"));
            updateUser.setEmail(Reader.readString("Please Enter Your Email Address"));
            updateUser.setPassword(Reader.readString("Please Enter Your Password"));  
            updateUser.setIsAdmin(false); 
        
            // attempt an update
            IUserDao udo = new UserDao();
            if(udo.update(CurrentUser.getInstance().getId(),updateUser)){
                // Successful registration so log user in sutomatically
                Writer.printMessage("UPDATE WAS SUCCESSFUL !!!");
                CurrentUser.login(updateUser);                
            } else {
                Writer.printMessage("\n UPDATE WAS NOT SUCCESSFUL");
            } 
  
        }
        catch(DaoException daoe){
            Writer.printMessage(daoe.getMessage());
        } 
    }
    
}
