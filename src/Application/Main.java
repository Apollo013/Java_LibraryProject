package Application;

import Controllers.AbstractController;
import Controllers.ControllerFactory;

// There are 3 menus, one for a general user (who has not logged in), a members menu and an administrators menu.
// Each menu only displays the relevant options associated with each role.

/**
 *
 * @author Paul Millar <D00152098>
 */
public class Main {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        ControllerFactory controllerFactory = new ControllerFactory();
        AbstractController optionsContoller = null; 
     
        do{

            // User has not logged in/nor registered, so get the 'General' options to allow this.
            if(!CurrentUser.isLoggedIn()){
                optionsContoller = controllerFactory.getController("General");              
            } 

            // User has either logged in or registered at this point.
            
            // If the user is an administrator, get the options associated with that role.
            else if (CurrentUser.isAdministrator()){
                optionsContoller = controllerFactory.getController("Administrator");             
            } 

            // If the user is an ordinary member, get the options associated with that role.
            else if (!CurrentUser.isAdministrator()){               
                optionsContoller = controllerFactory.getController("Member");               
            }

            // Run the options associated with the current users role.
            optionsContoller.run();

        } while(true); // To exit the program altogether, choose the 'Exit Program' option from GeneralController list of options.
        
    }
    
}
