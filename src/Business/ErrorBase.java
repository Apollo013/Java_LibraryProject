package Business;

import java.util.HashMap;

// GENERIC ERROR MODEL TO TRACK ANY ERRORS WITHIN AN OBJECT.
// ONLY IMPLEMENTED IN THE SETTERS OF ANY SUB CLASS.
// ISVALID() METHOD THEN CALLED PRIOR TO INSERT AND UPDATE IN COMMAND CLASSES.
// IF NOT VALID, DISPLAYERRORS() METHOD SHOULD BE CALLED TO INFORM USER OF ALL ERRORS.

/**
 *
 * @author Paul Millar <D00152098>
 */
public abstract class ErrorBase {
    
    private HashMap<String,String> errors = new HashMap<String,String>();
    
    /**
     * Adds an error
     * @param key key to be added to HashMap
     * @param value value to be added to HashMap
     */
    protected void addError(String key, String value){
        if(!errors.containsKey(key)){
            errors.put(key, value);
        }
    }
    
    /**
     * Removes an error
     * @param key The key to remove from the HashMap
     */
    protected void removeError(String key){
        if(errors.containsKey(key)){
            errors.remove(key);
        }
    }
    
    /**
     * Determines if this object has errors (is not valid)
     * @return
     */
    public boolean hasErrors(){
        return !errors.isEmpty();
    }
    
    /**
     * Determines if this object is valid.
     * @return
     */
    public boolean isValid(){
        return errors.isEmpty();
    }
    
    /**
     * Prints any errors to the screen.
     */
    public void displayErrors(){        
        if(hasErrors()){
            System.out.println("-----------------------------------------------------------");
            System.out.println("CANNOT PROCESS THIS REQUEST BECAUSE OF THE FOLLOWING ERRORS");
            System.out.println("-----------------------------------------------------------");
            for(String key : errors.keySet()){
                System.out.println(errors.get(key));
            }
        } else {
            System.out.println("THERE ARE NO ERRORS");
        }
    }
    
    /**
     * Prints any errors to the screen.
     */
    public void toArray(){ 
        
        String[] errorArray = new String[errors.size()];
        int counter = 0;
        if(hasErrors()){
            for(String key : errors.keySet()){
                System.out.println(errors.get(key));
                counter++;
            }
        }
    }    
}
