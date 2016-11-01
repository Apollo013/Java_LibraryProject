package Controllers;

import Application.CurrentUser;
import Application.Reader;
import Application.Writer;
import Commands.AbstractCommand;
import java.util.ArrayList;

/**
 * @date 17/10/2013
 * @author Paul Millar <D00152098>
 */
public abstract class AbstractController {
    
    protected ArrayList<AbstractCommand> commands;
    
    public AbstractController(){
        commands = new ArrayList<AbstractCommand>();
    }
            
    /**
     * Prompts the user for an option and executes the corresponding command.
     */
    public void run(){
        
        // This less 1, will be the index value of the command in the arraylist to execute
        int currentCommandOption = 0;
        
        // Continuosly prompt and wait for the user to enter an option.
        // The last option in any list will always force the loop to be broken.
        do{                    
            // Display the options/commands available.
            printMenu();
        
            // Prompt the user - The number the user picks must be in the range of 1 to the number of options in the arraylist.
            currentCommandOption = (Reader.readInt("Please enter your option", 1, commands.size())); 
            
            // Get the command from the hash map and execute it.
            commands.get(currentCommandOption - 1).execute() ;
            
            // Keep running until the user has selected the last option and/or 
            // by some other expression specified in the canRun() method.
        } while (currentCommandOption != commands.size() && !CurrentUser.isLoggedIn());     
    }
    
    /**
     * Display all options / commands.
     */
    public void printMenu(){
        int counter = 1;
        
        System.out.println("");
        Writer.printSingleWideLine();
        for(AbstractCommand cmd : this.commands){
            System.out.println(counter + ". " + cmd.toString());
            counter++;
        }    
        Writer.printSingleWideLine();           
    }

}
