package Commands.Adminstrator;

import Application.Reader;
import Application.Writer;
import Commands.AbstractCommand;
import DataAccess.ITitleDao;
import DataAccess.TitleDao;
import Exceptions.DaoException;

/**
 *
 * @author Paul Millar <D00152098>
 */
public class TitleDeleteCommand extends AbstractCommand{

    public TitleDeleteCommand(String prompt) {
        super(prompt);
    }
   
    @Override
    public void execute() {
        
        try{            
            // Create a new data access object for titles
            ITitleDao tdo = new TitleDao();

            // Prompt for ISBN & Check if delete was successful or not.
            if(tdo.delete(Reader.readString("Please Enter the ISBN of the Title to remove (1-13 digits)"))){
                Writer.printMessage("TITLE WAS REMOVED SUCCESSFULLY !!!");
            } else {
                Writer.printMessage("TITLE CANNOT BE FOUND !!!");
            }
            
        } catch (DaoException daoe){
            Writer.printMessage(daoe.getMessage());
        }            
    }   
    
}
