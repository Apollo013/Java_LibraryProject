package Business;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// REGEX ASSEMBLIES ABOVE ONLY FOR EMAIL

/**
 * @date 14/10/2013
 * @author Ehi Omorede, validation added by Paul Millar
 */
public class User extends ErrorBase implements Serializable {
    
    private int id;
    private String name;
    private String email;
    private String password;
    private boolean isAdmin;

    public static final String HEADER = String.format("%-4s\t%-20s\t%-50s\t%-15s", "ID", "Name", "Email", "Type");
    
    public User(){
        this.id = 0; 
        this.name = "";
        this.email = "";
        this.password = "";
        this.isAdmin = false;
    }

    public User(String name, String email, String password, boolean isadmin) {
        this.id = 0;
        this.name = name;
        this.email = email;
        this.password = password;
        this.isAdmin = isadmin;
    }

    public User(int idNo, String name,String email, String password, boolean isadmin){
        this.id = idNo;
        this.name = name;
        this.email = email;
        this.password = password;
        this.isAdmin = isadmin;
    }

    /**
     *
     * @return the unique id for this user.
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return the name for this user.
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return the unique email address for this user.
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @return the password for this user.
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @return true if this user is an administrator, false otherwise.
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Sets the unique id for the user.
     * @param idNo
     * @param name
     */
    public void setId(int idNo) {
        if(this.id != idNo){
            this.id = idNo;            
        }
    }

    /**
     * Sets the name for the user.
     * @param name
     */    
    public void setName(String name) {
        // Validate that something was specified
        if(!this.name.equals(name)){
            this.name = name; 
            if(this.name.isEmpty()){
                this.addError("NAME", "You must specify your name");
            } else {
                this.removeError("NAME");
            }            
        }  
    }

    /**
     * Sets the unique email for the user.
     * @param email
     */        
    public void setEmail(String email) {
        // Validate that a proper email was specified
        if(!this.email.equals(email)){
            this.email = email; 
            Matcher matcher = Pattern.compile("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$").matcher(this.email);
            if(this.email.isEmpty()){
                this.addError("EMAIL", "You must specify an email address");
            } else if(!matcher.find()){
                this.addError("EMAIL2", "You must specify a proper email address");
            } else {
                this.removeError("EMAIL");
                this.removeError("EMAIL2");
            }            
        }  
    }

    /**
     * Sets the password for the user.
     * @param password
     */
    public void setPassword(String password) {
        // Validate that something was specified
        if(!this.password.equals(password)){
            this.password = password; 
            if(this.password.isEmpty()){
                this.addError("PASSWORD", "You must specify your password");
            } else {
                this.removeError("PASSWORD");
            }            
        }  
    }
    
    /**
     * Sets whether this user is an administrator or not.
     * @param isAdmin
     */
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + this.id;
        hash = 23 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 23 * hash + (this.email != null ? this.email.hashCode() : 0);
        hash = 23 * hash + (this.password != null ? this.password.hashCode() : 0);
        hash = 23 * hash + (this.isAdmin ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {return false;}
        
        if(obj instanceof User){
            User user = (User) obj;
            if(this.name.equals(user.getName()) && this.email.equals(user.getEmail()) && this.password.equals(user.getPassword()) && this.id == user.getId() && this.isAdmin() == user.isAdmin()){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        // Password deliberately not included here
        return  String.format("%4d\t%-20s\t%-50s\t%-15s", this.id,this.name,this.email,(isAdmin() ? "Administrator" : "Member"));
    }

}

    

