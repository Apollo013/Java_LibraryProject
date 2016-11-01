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
 * @author Ehi
 */
public class SearchTitlesByNameCommand extends AbstractCommand{

    public SearchTitlesByNameCommand(String prompt) {
        super(prompt);
    }

    @Override
    public void execute() {
       
        try{            
            // Create a new data access object for titles
            ITitleDao tdo = new TitleDao();

            // Prompt the user for a title name
            String TitleName = Reader.readString("Please enter the name of the Title");
        
            // First Search For An Exact Match.
            Title title = tdo.getTitleByName(TitleName);
            if(title != null){                
                // Print header
                Writer.printHeader(Title.HEADER); 
                // Print Title
                System.out.println(title.toString());                
            } 
            
            // Now look for similar title names
            List<Title> titlesList = tdo.getSimilarTitlesByName(TitleName);
            if(titlesList.size() > 0){
                String out = (title == null) ? "NO TITLE WAS FOUND WITH THAT NAME BUT YOU MIGHT BE INTERESTED IN THESE" : "YOU MAY ALSO BE INTERESTED IN THESE";
                Writer.printMessage(out);
                
                // Print header
                Writer.printHeader(Title.HEADER); 
                
                // print each title
                for(Title t : titlesList){
                    System.out.println(t.toString());
                }            
                
                // Print how many were found
                Writer.printMessage(titlesList.size() + " SIMILAR TITLES FOUND");                
            }
        } catch (DaoException daoe){
            Writer.printMessage(daoe.getMessage());
        }
    }
}
