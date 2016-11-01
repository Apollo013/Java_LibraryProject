package Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @date 16/10/2013
 * @author Paul Millar <D00152098>
 */
public class Reader {

    private static BufferedReader reader =  new BufferedReader(new InputStreamReader(System.in));
    
    /**
     * Returns a BufferedReader Object.
     * @return
     */
    public static BufferedReader reader(){
        return reader;
    }
        
    /**
     * Closes the current Buffered Reader Object.
     */
    public static void closeReader(){
        if(reader!=null){
            try {
                reader.close();
                reader = null;
            }
            //return 1;
            catch (IOException ex) {
                System.out.println(ex.getMessage());
            }        
        }
    }
    
    /**
     * Prompts the user for a string value, Reads it, then and returns the value.
     * @param prompt
     * @return
     */
    public static String readString(String prompt){
        String value = "";

        do{			
            try{
                System.out.print("\n" + prompt + ": ");
                value = reader.readLine();
            }
            catch(IOException ioe){
                System.out.println("eadString(String prompt) " + ioe.getMessage());
            }			
        }while(value.equals("")); // DO NOT ALLOW ANY EMPTY STRINGS.

        return value;
    }       
    
    /**
     * Prompts the user for an integer value, Reads it then and returns the value.
     * @param prompt What to ask the user for e.g. "Please enter a number"
     * @return
     */    
    public static int readInt(String prompt){
        int value       = 0;
        boolean isValid = false;

        do{
            try{
                System.out.print("\n" + prompt + ": ");
                value = Integer.parseInt(reader().readLine());
                isValid = true;
            }
            catch(NumberFormatException nfe){
                System.out.println("WARNING: THIS IS NOT NUMBER !!!");
            }
            catch(IOException ioe){
                System.out.println("ERROR: AN IO EXCEPTION OCCURED !!!" + ioe.getMessage());
            }
        }while(isValid == false);

        return value;	
    }
        
    /**
     * Prompts the user for an integer value, Reads it then and returns the value.
     * <br/>The value must be within a specified range.
     * @param prompt What to ask the user for e.g. "Please enter you name"
     * @param min Minimum range value.
     * @param max Maximum range value.
     * @return
     */
    public static int readInt(String prompt, int min, int max){
        int value 	= 0;
        boolean isValid = false;

        do{
            try{
                System.out.print("\n" + prompt + ": ");
                value = Integer.parseInt(reader().readLine());
                // ONLY VALIDATE IF THE VALUE IS WITHIN SPECIFIED RANGE.
                if (value >= min && value <= max){
                    isValid = true;
                } else {
                    isValid = false;
                    System.out.println("The value must be between " + min + " and " + max + ". Please try again.");
                }
            }
            catch(NumberFormatException nfe){
                System.out.println("WARNING: THIS IS NOT NUMBER !!!");
            }
            catch(IOException ioe){
                System.out.println("ERROR: AN IO EXCEPTION OCCURED !!!" + ioe.getMessage());
            }
        }while(isValid == false);

        return value;	
    }
    
}
