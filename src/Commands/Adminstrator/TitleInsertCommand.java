package Commands.Adminstrator;

import Application.Reader;
import Application.Writer;
import Business.Title;
import Commands.AbstractCommand;
import DataAccess.ITitleDao;
import DataAccess.TitleDao;
import Exceptions.DaoException;

/**
 *
 * EHI
 */
public class TitleInsertCommand extends AbstractCommand{

    public TitleInsertCommand(String prompt) {
        super(prompt);
    }
    
    @Override
    public void execute() {

        try{
            
            Title title = new Title();
            
            // Prompt the user for title details
            title.setIsbn(Reader.readString("Please Enter the ISBN for this Title (1-13 digits)"));                
            title.setName(Reader.readString("Please Enter the NAME for this Title"));                
            title.setLocation(Reader.readString("Please Enter the LOCATION"));                
            title.setCopies(Reader.readInt("Please Enter the Stock-On-Hand Value"));                
            title.setLoanPeriod((byte)Reader.readInt("Please Enter the Maximum Loan Period for this Title"));
            title.setTitleAuthors(Reader.readString("Please Enter the Title's Author(s)"));
            
            // Attempt an insert
            if(title.isValid()){
                // Create a new data access object for titles
                ITitleDao tdo = new TitleDao();

                if(tdo.insert(title)){
                    Writer.printMessage("NEW TITLE INSERTED SUCCESSFULLY !!!");
                } else {
                    Writer.printMessage("INSERT FAILED !!!");
                }
            // Title object has errors
            } else {
                title.displayErrors();
            }

        }
        catch(DaoException daoe){
            Writer.printMessage(daoe.getMessage());
        }
    }
    
}
