package Commands;

// ABSTRACT DATA ACCESS COMMAND CLASS

/**
 *
 * @author Paul Millar <D00152098>
 */
public abstract class AbstractCommand {
    
    private String prompt;

    public AbstractCommand(String prompt){
        this.prompt = prompt;
    }
    
    public abstract void execute();
    
    @Override
    public String toString() {
        return prompt ;
    }
   
}
