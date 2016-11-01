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
 * @author Paul Millar <D00152098>
 */
public class TitleUpdateCommand extends AbstractCommand{

    public TitleUpdateCommand(String prompt) {
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
            
            // Check that the title actually exists.
            if(title != null){

                // Print out the current details
                Writer.printMessage("CURRENT TITLE DETAILS ARE ...");
                Writer.printHeader(Title.HEADER); 
                System.out.println(title.toString());
                
                // Prompt the user for title details
                title.setIsbn(Reader.readString("Please Enter the ISBN for this Title (1-13 digits)"));                
                title.setName(Reader.readString("Please Enter the NAME for this Title"));                
                title.setLocation(Reader.readString("Please Enter the LOCATION"));                
                title.setCopies(Reader.readInt("Please Enter the Stock On Hand Value"));                
                title.setLoanPeriod((byte)Reader.readInt("Please Enter the Maximum Loan Period for this Title"));
                title.setTitleAuthors(Reader.readString("Please Enter the Title's Author(s)"));
                                     

                if(tdo.update(TitleISBN,title)){
                    Writer.printMessage("TITLE WAS UPDATED SUCCESSFULLY !!!");
                } else {
                    Writer.printMessage("TITLE CANNOT BE UPDATED !!!");
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
