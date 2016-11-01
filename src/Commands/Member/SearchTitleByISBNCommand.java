package Commands.Member;

import Application.Reader;
import Application.Writer;
import Business.Title;
import Commands.AbstractCommand;
import DataAccess.ITitleDao;
import DataAccess.TitleDao;
import Exceptions.DaoException;

/**
 * @date 21/10/2013
 * @author Paul Millar <D00152098>
 */
public class SearchTitleByISBNCommand extends AbstractCommand{

    public SearchTitleByISBNCommand(String prompt) {
        super(prompt);
    }
    
    @Override
    public void execute() {
        
        try{
            // Create a new data access object for titles
            ITitleDao tdo = new TitleDao();

            // Prompt the user for title isbn
            String TitleISBN = Reader.readString("Please enter the ISBN of the Title (1-13 digits)");
        
            // Search For An Exact Match Only.
            Title title = tdo.getTitleByISBN(TitleISBN);
            
            if(title != null){
                
                // Print header
                Writer.printHeader(Title.HEADER); 
                
                // Print the title
                System.out.println(title.toString());
                
                // print an underline
                Writer.printDoubleWideLine();
                
            } else {
                Writer.printMessage("NO TITLE WAS FOUND WITH THAT ISBN");
            }
            
        } catch (DaoException daoe){
            Writer.printMessage(daoe.getMessage());
        }
    }
    
}
