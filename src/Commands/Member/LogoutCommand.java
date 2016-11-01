/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Commands.Member;

import Application.CurrentUser;
import Commands.AbstractCommand;

/**
 *
 * @author Paul Millar <D00152098>
 */
public class LogoutCommand extends AbstractCommand{

    public LogoutCommand(String prompt) {
        super(prompt);
    }
    
    @Override
    public void execute() {
        CurrentUser.logout();
    }
    
}
