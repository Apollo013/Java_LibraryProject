package Commands.Public;

import Application.Reader;
import Commands.AbstractCommand;

/**
 * @date 17/10/2013
 * @author Paul Millar <D00152098>
 */
public class ExitProgramCommand extends AbstractCommand{

    public ExitProgramCommand(String prompt){
        super(prompt);
    }
    
    @Override
    public void execute() {
        Reader.closeReader();
        System.exit(0);
    }
    
}
