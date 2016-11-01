package Controllers;

/**
 * @date 17/10/2013
 * @author Paul Millar <D00152098>
 */
public class ControllerFactory {
    
    public AbstractController getController(String userType){
        
        // Return the correct controller associated with the CurrentUser's role.
        
        if(userType.equalsIgnoreCase("General")){
            return new GeneralController();
        } else if(userType.equalsIgnoreCase("Administrator")){
            return new AdministratorController();
        } else if(userType.equalsIgnoreCase("Member")){
            return new MembersController();
        }
        
        return null;
    }
    
}
