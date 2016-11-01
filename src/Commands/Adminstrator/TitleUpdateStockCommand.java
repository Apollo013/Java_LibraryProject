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
public class TitleUpdateStockCommand extends AbstractCommand{

    public TitleUpdateStockCommand(String prompt) {
        super(prompt);
    }

    @Override
    public void execute() {
        try{            
            // Create a new data access object for titles
            ITitleDao tdo = new TitleDao();

            // Prompt the user for the titles' ISBN
            String TitleISBN = Reader.readString("Please Enter the ISBN of the Title to update (1-13 digits)");
                                                            
            // Get the Title Details First.
            Title title = tdo.getTitleByISBN(TitleISBN);
            
            // Check that the title exists.
            if(title != null){

                // Prompt the user for title details               
                title.setCopies(Reader.readInt("Please Enter the Stock On Hand Value"));                
            
                // Attempt an update
                if(title.isValid()){
                    if(tdo.update(TitleISBN,title)){
                        Writer.printMessage("TITLE STOCK WAS UPDATED SUCCESSFULLY !!!");
                    } else {
                        Writer.printMessage("TITLE STOCK CANNOT BE UPDATED !!!");
                    }
                // Title object has errors
                } else {
                    title.displayErrors();
                }
                 
            // Title could not be found
            } else {
                Writer.printMessage("NO TITLE WAS FOUND WITH THAT ISBN !!!");
            }            
        } catch (DaoException daoe){
            Writer.printMessage(daoe.getMessage());
        } 
    }
    
}
