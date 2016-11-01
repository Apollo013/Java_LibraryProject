package DataAccess;

import Business.Loan;
import Business.Title;
import Exceptions.DaoException;
import java.util.Date;
import java.util.List;

/**
 *
 * EHI
 */
public interface ILoanDao {
    
    Date bookOut(Title title, int userId) throws DaoException;
    int bookIn(Title title, int userId) throws DaoException;    
    List<Loan> getAllCurrentLoans() throws DaoException; 
    List<Loan> getAllOverdueLoans() throws DaoException;
    List<Loan> getCurrentLoansByUserId(int userId) throws DaoException;     
    Loan matchingLoanExists(String isbn, int userId) throws DaoException;
    
}
    
