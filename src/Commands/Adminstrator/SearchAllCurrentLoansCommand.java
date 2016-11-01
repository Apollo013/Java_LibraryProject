package Commands.Adminstrator;

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
public class SearchAllCurrentLoansCommand extends AbstractCommand{

    public SearchAllCurrentLoansCommand(String prompt) {
        super(prompt);
    }

    @Override
    public void execute() {
        try{
            // Create a new data access object for loans            
            ILoanDao ldo = new LoanDao();

            // get all current loans
            List<Loan> loans = ldo.getAllCurrentLoans();

            if(loans.size() > 0){
                
                // We are printing the long version of the toString() method.
                
                // Print header
                Writer.printHeader(Loan.LONGHEADER); 

                // print each loan record                
                for(Loan l : loans){
                    System.out.println(l.toLongString());
                }

                // Print how many were found
                Writer.printMessage(loans.size() + " TITLE(S) CURRENTLY ON LOAN");
            } else {
                Writer.printMessage("NO TITLES ARE CURRENTLY ON LOAN");
            } 
        }
        catch(DaoException daoe){
            Writer.printMessage(daoe.getMessage());
        }
    }
    
}
