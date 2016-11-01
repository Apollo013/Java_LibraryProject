package Application;

import Business.User;

// This is the current user - YOU, whether you are a member of the general public, 
// a library member or an administrator, this is you, this is what decides what 
// options are at your disposel.

/**
 *
 * @author Paul Millar <D00152098>
 */
public class CurrentUser{
    
    private static User currentUser = new User();
    
    private CurrentUser(){}
    
    /**
     * Returns the same User instance every time.
     * @return
     */
    public static User getInstance(){
        return currentUser;
    }
    
    /**
     * Determines if the current user is logged in, i.e either a member or administrator.
     * @return
     */
    public static boolean isLoggedIn(){
        return ((currentUser != null) && (currentUser.getId() > 0));
    }
    
    /**
     * Determines if the current user is an administrator.
     * @return boolean true if this is an administrator, otherwise false.
     */
    public static boolean isAdministrator(){
        return currentUser.isAdmin();
    }
    
    /**
     * Logs in the current user
     * @param user
     */
    public static void login(User user){
        currentUser.setId(user.getId());
        currentUser.setName(user.getName());
        currentUser.setEmail(user.getEmail());
        currentUser.setPassword(user.getPassword());
        currentUser.setIsAdmin(user.isAdmin());
        String type = (user.isAdmin() ? "Administrator" : "Member" );
        Writer.printMessage("YOU HAVE SUCCESSFULLY LOGGED IN " + CurrentUser.getInstance().getName() + " (" + type + ") !!!");           
    }
    
    /**
     * Logs out the current user
     */
    public static void logout(){
        currentUser.setId(0);
        currentUser.setName("");
        currentUser.setEmail("");
        currentUser.setPassword("");
        currentUser.setIsAdmin(false);
        Writer.printMessage("YOU ARE NOW LOGGED OUT !");
    }
    
}
