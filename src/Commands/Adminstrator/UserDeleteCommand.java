package Commands.Adminstrator;

import Application.Reader;
import Application.Writer;
import Business.User;
import Commands.AbstractCommand;
import DataAccess.IUserDao;
import DataAccess.UserDao;
import Exceptions.DaoException;

/**
 * @date 18/10/2013
 * @author Paul Millar <D00152098>
 */
public class UserDeleteCommand extends AbstractCommand{

    public UserDeleteCommand(String prompt) {
        super(prompt);
    }

    @Override
    public void execute() {
    
        try{
            // First see if the user exists
            IUserDao uso = new UserDao();
            User userToDelete = uso.getUserByEmail(Reader.readString("Please Enter Your Email Address"));

            // attempt delete
            if(uso.delete(userToDelete)){
                Writer.printMessage("USER WAS REMOVED SUCCESSFULLY !!!");
            } else {
                Writer.printMessage("USER CANNOT BE REMOVED !!!");
            }                
        }
        catch(DaoException daoe){
            Writer.printMessage(daoe.getMessage());
        }          
    }
    
}
