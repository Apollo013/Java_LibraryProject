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


/**
 *
 * @author Paul Millar <D00152098>
 */
public class BookInTitleForCurrentUserCommand  extends AbstractCommand{

    public BookInTitleForCurrentUserCommand(String prompt) {
        super(prompt);
    }

    @Override
    public void execute() {
        try {                        
            // Create a new data access object for titles
            ITitleDao tdo = new TitleDao();
       
            // Prompt the user for title isbn & search For An Exact Match Only.
            Title title = tdo.getTitleByISBN(Reader.readString("Please enter the ISBN of the Title To Be Returned (1-13 digits)"));
            
            // Check that we have indeed a title to book in
            if(title != null){      
                
                // Create a new data access object for loans
                LoanDao ldo = new LoanDao();
                
                // If successful (we got a positive value indicating how many days overdue or a zero/negative value indicating a timely return)
                int days = ldo.bookIn(title, CurrentUser.getInstance().getId());
                if(days > 0){
                    Writer.printMessage("THANK YOU FOR RETURNING THIS TITLE BACK IN TIMELY FASHION ");
                } else {
                    days *= -1;
                    Writer.printMessage("YOU ARE LATE RETURNING THIS TITLE BY " + days + " days");
                }
            } else {
                Writer.printMessage("NO TITLE WAS FOUND WITH THAT ISBN");
            }
        } catch (DaoException daoe) {
            Writer.printMessage(daoe.getMessage());        
        }
    }
    
}
