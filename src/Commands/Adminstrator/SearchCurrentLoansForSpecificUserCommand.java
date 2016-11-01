package Commands.Adminstrator;

import Application.Reader;
import Application.Writer;
import Business.Loan;
import Business.User;
import Commands.AbstractCommand;
import DataAccess.ILoanDao;
import DataAccess.IUserDao;
import DataAccess.LoanDao;
import DataAccess.UserDao;
import Exceptions.DaoException;
import java.util.List;

/**
 *
 * @author Paul Millar <D00152098>
 */
public class SearchCurrentLoansForSpecificUserCommand extends AbstractCommand{

    public SearchCurrentLoansForSpecificUserCommand(String prompt) {
        super(prompt);
    }

    @Override
    public void execute() {
                
        try{                      
            // Create a new data access object for users
            IUserDao udo = new UserDao();
            
            // Get the users details
            User user = udo.getUserByEmail(Reader.readString("Please Enter the Persons Email Address"));
            
            if(user != null){                                
                // Create a new data access object for loans            
                ILoanDao ldo = new LoanDao();
                
                // Search For Exact Matches Only.
                List<Loan> loans = ldo.getCurrentLoansByUserId(user.getId());

                if(loans.size() > 0){
                    // Print header
                    Writer.printHeader(Loan.NORMALHEADER); 

                    // print each loan record                
                    for(Loan l : loans){
                        System.out.println(l.toString());
                    }

                    // Print how many were found
                    Writer.printMessage(loans.size() + " TITLE(S) ARE CURRENTLY ON LOAN FOR " + user.getName());
                } else {
                    Writer.printMessage("NO TITLES ARE CURRENTLY ON LOAN FOR " + user.getName());
                }             
            }
         
        } catch (DaoException daoe){
            Writer.printMessage(daoe.getMessage());
        }
    }
}
