package Commands.Member;

import Application.CurrentUser;
import Application.Writer;
import Business.Loan;
import Commands.AbstractCommand;
import DataAccess.ILoanDao;
import DataAccess.LoanDao;
import Exceptions.DaoException;
import java.util.List;

/**
 *
 * @author Paul Millar <D00152098>
 */
public class SearchCurrentLoansForCurrentUserCommand extends AbstractCommand{

    public SearchCurrentLoansForCurrentUserCommand(String prompt) {
        super(prompt);
    }

    @Override
    public void execute() {
        
        // Make sure we have someone logged in
        if(!CurrentUser.isLoggedIn()){
            return;
        }
                
        try{
            // Create a new data access object for titles            
            ILoanDao ldo = new LoanDao();

            // Search For An Exact Match Only.
            List<Loan> loans = ldo.getCurrentLoansByUserId(CurrentUser.getInstance().getId());
            
            if(loans.size() > 0){
                
                // Print header
                Writer.printHeader(Loan.NORMALHEADER); 
                
                // print each loan record                
                for(Loan l : loans){
                    System.out.println(l.toString());
                }
                
                // Print how many were found
                Writer.printMessage(loans.size() + " LOANS(S) FOUND FOR " + CurrentUser.getInstance().getName());
            } else {
                Writer.printMessage("NO LOANS FOUND");
            }          
        } catch (DaoException daoe){
            Writer.printMessage(daoe.getMessage());
        }
    }
    
}
