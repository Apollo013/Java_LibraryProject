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
public class EmailOverDueUsersCommand extends AbstractCommand{

    public EmailOverDueUsersCommand(String prompt) {
        super(prompt);
    }

    @Override
    public void execute() {
        try{
            // Create a new data access object for loans            
            ILoanDao ldo = new LoanDao();

            // Search For overdue loans.
            List<Loan> loans = ldo.getAllOverdueLoans();

            int numberOfLoans = loans.size();
            int counter = 0;
            
            if(numberOfLoans > 0){

                String emailAddress = "";

                do{
                    
                 // Print an email for every user
                    if(!emailAddress.equals(loans.get(counter).getUserEmail())){
                        // New email address, start a new email
                        Writer.printSingleWideLine();
                        System.out.println("To: " + loans.get(counter).getUserEmail());
                        System.out.println("Subject: Overdue Titles");
                        System.out.println("Message: According to our records the following titles are overdue.");
                        Writer.printSingleWideLine();
                        Writer.printHeader(Loan.LONGHEADER);
                        emailAddress = loans.get(counter).getUserName();
                        
                        // print all the overdue titles for this email
                        do{
                            System.out.println(loans.get(counter).toLongString());
                            ++counter;
                        } while (counter < numberOfLoans && emailAddress.equals(loans.get(counter).getUserEmail()));
                        
                        // close off email
                        Writer.printDoubleWideLine();                    
                        System.out.println("\n");
                    }

                } while (counter < numberOfLoans);
    

                // Print how many were found
                Writer.printMessage("THERE ARE " + loans.size() + " TITLE(S) CURRENTLY OVERDUE");
            } else {
                Writer.printMessage("NO TITLES ARE CURRENTLY OVERDUE");
            } 
        }
        catch(DaoException daoe){
            Writer.printMessage(daoe.getMessage());
        }
    }
    
}
