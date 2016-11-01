package Controllers;

import Commands.Public.*;

/**
 * @date 17/10/2013
 * @author Paul Millar <D00152098>
 */
public class GeneralController extends AbstractController{

    // Add Data Access Commands Associated With The General Public Role.
    
    public GeneralController() {
        super();
        commands.add(new LoginCommand("Log In"));
        commands.add(new RegisterCommand("Register"));
        commands.add(new ViewAllTitlesCommand("View All Titles"));      
        commands.add(new ExitProgramCommand("Exit Program"));
    }

}
