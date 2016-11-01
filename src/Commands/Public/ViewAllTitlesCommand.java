package Commands.Public;

import Application.Writer;
import Business.Title;
import Commands.AbstractCommand;
import DataAccess.ITitleDao;
import DataAccess.TitleDao;
import Exceptions.DaoException;
import java.util.List;

/**
 * @date 18/10/2013
 * @author Paul Millar <D00152098>
 */
public class ViewAllTitlesCommand extends AbstractCommand{

    public ViewAllTitlesCommand(String prompt) {
        super(prompt);
    }
    
    @Override
    public void execute() {
        try{
            
            // Create a new data access object for titles
            ITitleDao tdo = new TitleDao();
            
            // Get all titles
            List<Title> titlesList = tdo.getAllTitles();

            // Print header
            Writer.printHeader(Title.HEADER); 
            
            // Display Them
            for(Title t : titlesList){
                System.out.println(t.toString());
            }
            
            // Display how many titles there are
            Writer.printMessage(titlesList.size() + " title(s) in total");
            
        } catch (DaoException daoe){
            Writer.printMessage(daoe.getMessage());
        }        

    }
}
