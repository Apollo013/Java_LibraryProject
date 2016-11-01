package Controllers;

import Commands.Adminstrator.*;
import Commands.Member.*;
import Commands.Public.*;

/**
 * @date 17/10/2013
 * @author Paul Millar <D00152098>
 */
public class AdministratorController extends AbstractController{

    // Administrators Are Permitted Access To All Data Access Commands. 
        
    public AdministratorController() {
        super();       
        commands.add(new BookOutTitleForCurrentUserCommand("Book Out A Title (for you)"));
        commands.add(new BookInTitleForCurrentUserCommand("Book In A Title (for you)"));
        commands.add(new ViewAllTitlesCommand("View All Titles"));
        commands.add(new SearchTitlesByNameCommand("Search For Title By Name (searches similar titles also)"));        
        commands.add(new SearchTitleByISBNCommand("Search For Title By ISBN"));        
        commands.add(new SearchTitlesByLocationCommand("Search For Titles By Location"));
        commands.add(new SearchTitlesByAuthorCommand("Search For Titles By Author"));        
        commands.add(new TitleUpdateStockCommand("Update Stock For An Existing Title"));                              
        commands.add(new SearchCurrentLoansForCurrentUserCommand("View Your Current Loans"));        
        commands.add(new SearchCurrentLoansForSpecificUserCommand("View Current Loans For Specific User"));
        commands.add(new TitleInsertCommand("Insert A New Title"));        
        commands.add(new TitleUpdateCommand("Update An Existing Title"));     
        commands.add(new TitleDeleteCommand("Remove A Title"));        
        commands.add(new SearchAllCurrentLoansCommand("View All Titles Currently On Loan"));
        commands.add(new EmailOverDueUsersCommand("Email Overdue Members"));
        commands.add(new UpdateCurrentUserDetailsCommand("Update Your Details"));    
        commands.add(new UserDeleteCommand("Remove A User"));        
        commands.add(new LogoutCommand("Log Out As An Administrator"));   
    }
        
}
