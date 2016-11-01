package Controllers;

import Commands.Member.*;
import Commands.Public.*;

/**
 *
 * @author Paul Millar <D00152098>
 */
public class MembersController extends AbstractController{

    // Add Data Access Commands Associated With General Public & Member Roles
        
    public MembersController() {
        super();
        commands.add(new BookOutTitleForCurrentUserCommand("Book Out A Title"));           
        commands.add(new BookInTitleForCurrentUserCommand("Book In A Title"));
        commands.add(new ViewAllTitlesCommand("View All Titles")); 
        commands.add(new SearchTitlesByNameCommand("Search Title By Name (searches similar titles also)"));               
        commands.add(new SearchTitleByISBNCommand("Search Title By ISBN"));
        commands.add(new SearchTitlesByAuthorCommand("Search For Titles By Author"));        
        commands.add(new SearchTitlesByLocationCommand("Search For Titles By Location"));        
        commands.add(new SearchCurrentLoansForCurrentUserCommand("View Your Current Loans"));
        commands.add(new UpdateCurrentUserDetailsCommand("Update Your Details"));        
        commands.add(new LogoutCommand("Log Out As A Member"));                 
    } 

}
