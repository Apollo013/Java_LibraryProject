package Commands.Member;

import Application.Reader;
import Application.Writer;
import Business.Title;
import Commands.AbstractCommand;
import DataAccess.ITitleDao;
import DataAccess.TitleDao;
import Exceptions.DaoException;
import java.util.List;

/**
 *
 * @author Paul Millar <D00152098>
 */
public class SearchTitlesByAuthorCommand extends AbstractCommand{

    public SearchTitlesByAuthorCommand(String prompt) {
        super(prompt);
    }
    
    @Override
    public void execute() {

        try{
            
            // Create a new data access object for titles
            ITitleDao tdo = new TitleDao();

            // Prompt the user for an authors' name
            String AuthorName = Reader.readString("Please enter the name of the Author");
        
            // Perform the Search
            List<Title> titlesList = tdo.getTitlesByAuthor(AuthorName);
            
            if(titlesList.size() > 0){
                
                // Print header
                Writer.printHeader(Title.HEADER); 
                
                // print each title                
                for(Title t : titlesList){
                    System.out.println(t.toString());
                }
                
                // Print how many were found
                Writer.printMessage(titlesList.size() + " TITLE(S) FOUND.");
            } else {
                Writer.printMessage("NO TITLES WERE FOUND FOR THAT AUTHOR");
            }
            
        } catch (DaoException daoe){
            Writer.printMessage(daoe.getMessage());
        }
                
    }
    
}
