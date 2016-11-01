package Application;

// FORMATS OUTPUT TO THE SCREEN

/**
 *
 * @author Paul Millar <D00152098>
 */
public class Writer {
    
    private static final int LINEWIDTH = 170;
    
    /**
     * Prints a header for each DTO
     * @param hdr
     */
    public static void printHeader(String hdr){
        printDoubleWideLine();
        System.out.println(hdr);        
        printDoubleWideLine();         
    }
    
    /**
     * Prints A Double Line
     * @param size The Number Of Characters In Length.
     */		
    public static void printDoubleWideLine(){
        for(int i = 0 ; i <= LINEWIDTH ; i++){
            System.out.print("=");
        }
        System.out.println("");        
    } 
        
    /**
     * Prints A Single Line
     */
    public static void printSingleWideLine(){
        for(int i = 0 ; i <= LINEWIDTH ; i++){
            System.out.print("-");
        }
        System.out.println("");        
    } 
    
    /**
     * Prints A Message Surrounded By Double Lines
     * @param msg The message to print
     */
    public static void printMessage(String msg){
        printDoubleWideLine();
        System.out.println(msg.toUpperCase());
        printDoubleWideLine();         
    }
    
}
