package Commands.Member;

import Application.CurrentUser;
import Application.Reader;
import Application.Writer;
import Business.Title;
import Commands.AbstractCommand;
import DataAccess.ITitleDao;
import DataAccess.LoanDao;
import DataAccess.TitleDao;
import Exceptions.DaoException;
import java.util.Date;

/**
 *
 * @author Paul Millar <D00152098>
 */
public class BookOutTitleForCurrentUserCommand extends AbstractCommand{

    public BookOutTitleForCurrentUserCommand(String prompt) {
        super(prompt);
    }

    @Override
    public void execute() {
        
        try {                        
            // Create a new data access object for titles
            ITitleDao tdo = new TitleDao();
       
            // Prompt the user for title isbn & search For An Exact Match Only.
            Title title = tdo.getTitleByISBN(Reader.readString("Please enter the ISBN of the Title (1-13 digits)"));
            
            // Check that we have indeed a title to book out
            if(title != null){

                // Create a new data access object for loans
                LoanDao ldo = new LoanDao();
 
                // attempt to book the title out
                Date dueDate = ldo.bookOut(title, CurrentUser.getInstance().getId());
                
                // If successful (we got a due date back from the update)
                if(dueDate != null){
                    String df = String.format("%tB %<td, %<tY", dueDate);
                    Writer.printMessage("THANK YOU " + CurrentUser.getInstance().getName() + ", THIS TITLE IS DUE BACK ON " + df);
                } else {
                    Writer.printMessage("SORRY PLEASE CHOOSE ANOTHER TITLE");
                }
            } else {
                Writer.printMessage("NO TITLE WAS FOUND WITH THAT ISBN");
            }
        } catch (DaoException daoe) {
            Writer.printMessage(daoe.getMessage());        
        }
                    
    }
    
}
