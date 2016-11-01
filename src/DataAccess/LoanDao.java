package DataAccess;

import Application.CurrentUser;
import Business.Loan;
import Business.Title;
import Exceptions.DaoException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 *
 */
public class LoanDao extends Dao implements ILoanDao{

    /**
     *
     * @param title
     * @param userId
     * @return due date if successful, otherwise null
     * @throws DaoException
     */
    @Override
    public Date bookOut(Title title, int userId) throws DaoException {
 
        // Make sure we have copies to book out, warn and exit
        if(title.availableQuantity() == 0){
            throw new DaoException ("SORRY BUT THERE ARE NO MORE COPIES AVAILABLE");                  
        }         
                
        // Check that the user does not already have this on loan
        // This should return a loan object if it exists in the DB (bad), null otherwise (good)
        if(matchingLoanExists(title.getIsbn(), userId) != null){
            throw new DaoException ("SORRY BUT YOU ALREADY HAVE THIS TITLE ON LOAN - " + title.getName());                    
        }    

        // Good to go
        String updateTitlesSQL  = "UPDATE titles SET quantity_on_loan = ? WHERE isbn = ?";
        String insertLoanSQL    = "INSERT INTO loans (date_borrowed,date_due,isbn,user_id) VALUES (?,?,?,?)";
                
        Connection          conn            = null;
        PreparedStatement   psUpdateTitles  = null; 
        PreparedStatement   psInsertLoan    = null; 
        Date                borrowedDate    = new Date();
        
        // Calculate the due date
        Date dueDate    = new Date();
        Calendar c      = Calendar.getInstance();
        c.add(Calendar.DATE, title.getLoanPeriod());
        dueDate = c.getTime();
    
        try{
            // Create a new connection
            conn = getConnection();
            
            // Start a transaction to update both the title & loan tables
            // Turn AutoCommit Off, we'll call it explicitly at the end.
            conn.setAutoCommit(false);
            
            // Increase the quantity on loan by 1
            title.increaseQuantityOnLoan();
            
            // Set up the transcation to update the title table
            psUpdateTitles = conn.prepareStatement(updateTitlesSQL);
            psUpdateTitles.setInt(1, title.getQuantityOnLoan());
            psUpdateTitles.setString(2, title.getIsbn());           
            
            // Set up the transcation to update the loan table
            psInsertLoan = conn.prepareStatement(insertLoanSQL);
            psInsertLoan.setDate(1,new java.sql.Date(borrowedDate.getTime()));
            psInsertLoan.setDate(2,new java.sql.Date(dueDate.getTime()));
            psInsertLoan.setString(3,title.getIsbn());  
            psInsertLoan.setInt(4,userId);
            
            // Execute
            psInsertLoan.executeUpdate();
            psUpdateTitles.executeUpdate();
            
            // Explicitly commit the transaction
            conn.commit();
        } 
        catch (SQLException e){
            // Set to null, will signal to caller that this action was not successful
            dueDate = null;
            // Rollback transaction
            if(conn != null){
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new DaoException("LoanDao.bookOut(): " + ex.getMessage());
                }
            }
            throw new DaoException("LoanDao.bookOut(): " + e.getMessage());
        }
        finally{
            freePreparedStatement(psInsertLoan);
            freePreparedStatement(psUpdateTitles);
            freeConnection(conn);
        }     
        
        // Return the due date to the caller - could be null
        return (dueDate);
    }

    /**
     * Books a title back into stock
     * @param title
     * @param userId
     * @return Return the numbers of days overdue or not overdue
     * @throws DaoException
     */
    @Override
    public int bookIn(Title title, int userId) throws DaoException {
                        
        // Check that the user does have this title on loan
        // This should return a loan object if it exists in the loans table (good), null otherwise (bad)
        Loan loan = matchingLoanExists(title.getIsbn(), CurrentUser.getInstance().getId());

        if( loan == null){
            throw new DaoException("SORRY BUT YOU SHOULD NOT HAVE THIS TITLE ON LOAN - " + title.getName());                  
        }

        // Good to go
        String updateTitlesSQL  = "UPDATE titles SET quantity_on_loan = ? WHERE isbn = ?";
        String updateLoanSQL    = "UPDATE loans SET date_returned = ? WHERE id = ?";
                
        Connection          conn            = null;
        PreparedStatement   psUpdateTitles  = null; 
        PreparedStatement   psInsertLoan    = null; 
        Date                dateReturned    = new Date();
        
        try{
            // Create a new connection
            conn = getConnection();
            
            // Start a transaction to update both the title & loan tables
            // Turn AutoCommit Off, we'll call it explicitly at the end.            
            conn.setAutoCommit(false);
            
            // Decrease the quantity on loan by 1
            title.decreaseQuantityOnLoan();
            
            // Set up the transcation to update the title table
            psUpdateTitles = conn.prepareStatement(updateTitlesSQL);
            psUpdateTitles.setInt(1, title.getQuantityOnLoan());
            psUpdateTitles.setString(2, title.getIsbn());           
            
            // Set up the transcation to update the loan table
            psInsertLoan = conn.prepareStatement(updateLoanSQL);
            psInsertLoan.setDate(1,new java.sql.Date(dateReturned.getTime()));
            psInsertLoan.setInt(2, loan.getId());   
            
            // Execute
            psInsertLoan.executeUpdate();
            psUpdateTitles.executeUpdate();
            
            // Explicitly commit the transaction
            conn.commit();
        } 
        catch (SQLException e){
            // Rollback transaction
            if(conn != null){
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new DaoException("LoanDao.bookIn(): " + ex.getMessage());
                }
            }
            throw new DaoException("LoanDao.bookIn(): " + e.getMessage());
        }
        finally{
            freePreparedStatement(psInsertLoan);
            freePreparedStatement(psUpdateTitles);
            freeConnection(conn);
        }     
        
        // Return the numbers of days overdue or not overdue
        return (int)(loan.getDateDue().getTime() - dateReturned.getTime()) / (1000 * 60 * 60 * 24);
    }

    /**
     * Searches for and returns any titles currently on loan for a user
     * @param userId
     * @return List of any titles currently on loan for a user
     * @throws DaoException
     */
    @Override
    public List<Loan> getCurrentLoansByUserId(int userId) throws DaoException {
        
        Connection          conn        = null;
        PreparedStatement   ps          = null;
        ResultSet           rs          = null;    
        List<Loan>          loansList   = new ArrayList<Loan>();
        
        try{
            conn = getConnection();
            // Get all loan details, user's name + email & title name for current loans for a particular user
            String query =  "SELECT loans.id, loans.user_id, loans.isbn, loans.date_borrowed, loans.date_due, loans.date_returned, " +
                            "titles.name AS tname, users.name AS uname, users.email FROM loans " +
                            "JOIN titles on loans.isbn = titles.isbn " +
                            "JOIN users on loans.user_id = users.id " +
                            "WHERE loans.user_id = ? AND loans.date_returned IS NULL ORDER BY loans.date_borrowed";
            
            ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            
            while(rs.next()){
                loansList.add(new Loan (    rs.getInt("id"),
                                            rs.getInt("user_id"),
                                            rs.getString("isbn"),
                                            rs.getDate("date_borrowed"),
                                            rs.getDate("date_due"),
                                            rs.getDate("date_returned"),
                                            rs.getString("tname"),
                                            rs.getString("uname"),
                                            rs.getString("email")));                  
            }
        } catch (SQLException ex) {
            
            throw new DaoException("LoanDao.getCurrentLoansByUserId(): " + ex.getMessage()); 
        }
        finally{
            freeResources("LoanDao.getCurrentLoansByUserId(): ",conn,ps,rs);
        }
    
        return loansList;
    }

    /**
     * Searches for and returns any titles currently on loan
     * @return List of any titles currently on loan
     * @throws DaoException
     */
    @Override
    public List<Loan> getAllCurrentLoans() throws DaoException {
        
        Connection          conn    = null;
        PreparedStatement   ps      = null;
        ResultSet           rs      = null;    
        List<Loan>         loansList  = new ArrayList<Loan>();
        
        try{
            conn = getConnection();
            // Get all loan details, user's name + email & title name for current loans
            String query =  "SELECT loans.id, loans.user_id, loans.isbn, loans.date_borrowed, loans.date_due, loans.date_returned, " +
                            "titles.name AS tname, users.name AS uname, users.email FROM loans " +
                            "JOIN titles on loans.isbn = titles.isbn " +
                            "JOIN users on loans.user_id = users.id " +
                            "WHERE loans.date_returned IS NULL ORDER BY loans.date_borrowed";
            ps = conn.prepareStatement(query);            
            rs = ps.executeQuery();
            
            while(rs.next()){
                loansList.add(new Loan (    rs.getInt("id"),
                                            rs.getInt("user_id"),
                                            rs.getString("isbn"),
                                            rs.getDate("date_borrowed"),
                                            rs.getDate("date_due"),
                                            rs.getDate("date_returned"),
                                            rs.getString("tname"),
                                            rs.getString("uname"),
                                            rs.getString("email")));                  
            }
              
            
        } catch (SQLException ex) {
            
            throw new DaoException("LoanDao.getAllCurrentLoans(): " + ex.getMessage()); 
        }
        finally{
            freeResources("LoanDao.getAllCurrentLoans(): ",conn,ps,rs);
        }
    
        return loansList;
        
        
        
    }

    /**
     * Searches for and returns any titles currently on loan and overdue
     * @return List of any titles currently on loan and overdue
     * @throws DaoException
     */
    @Override
    public List<Loan> getAllOverdueLoans() throws DaoException {
        
        Connection          conn    = null;
        PreparedStatement   ps      = null;
        ResultSet           rs      = null;    
        List<Loan>         loansList  = new ArrayList<Loan>();
        
        try{
            conn = getConnection();
            // Get all loan details, user's name + email & title name for overdue loans
            String query =  "SELECT loans.id, loans.user_id, loans.isbn, loans.date_borrowed, loans.date_due, loans.date_returned, " +
                            "titles.name AS tname, users.name AS uname, users.email FROM loans " +
                            "JOIN titles on loans.isbn = titles.isbn " +
                            "JOIN users on loans.user_id = users.id " +
                            "WHERE loans.date_returned IS NULL AND loans.date_due < DATE(NOW()) ORDER BY users.email, loans.date_borrowed";
            ps = conn.prepareStatement(query);            
            rs = ps.executeQuery();
            
            while(rs.next()){
                loansList.add(new Loan (    rs.getInt("id"),
                                            rs.getInt("user_id"),
                                            rs.getString("isbn"),
                                            rs.getDate("date_borrowed"),
                                            rs.getDate("date_due"),
                                            rs.getDate("date_returned"),
                                            rs.getString("tname"),
                                            rs.getString("uname"),
                                            rs.getString("email")));                  
            }
              
            
        } catch (SQLException ex) {            
            throw new DaoException("LoanDao.getAllCurrentLoans(): " + ex.getMessage()); 
        }
        finally{
            freeResources("LoanDao.getAllCurrentLoans(): ",conn,ps,rs);
        }
    
        return loansList;
        
        
        

    }

    /**
     * Checks to see if a title is currently on,loan by a specific user
     * @param isbn
     * @param userId
     * @return a Loan object if it exists, otherwise null
     * @throws DaoException
     */
    @Override
    public Loan matchingLoanExists(String isbn, int userId) throws DaoException {
        
        String sql = "SELECT * FROM loans WHERE isbn = ? AND user_id = ? AND date_returned IS NULL";
        
        Connection          conn        = null;
        PreparedStatement   ps          = null;
        ResultSet           rs          = null;  
        Loan                loan        = null;
        
        try{
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1,isbn);
            ps.setInt(2, userId);
            
            rs = ps.executeQuery();
            
            // Create a Loan object only if we found a match
            if(rs.next()){
                loan = new Loan(    rs.getInt("id"),
                                    rs.getInt("user_id"),
                                    rs.getString("isbn"),
                                    rs.getDate("date_borrowed"),
                                    rs.getDate("date_due"),
                                    rs.getDate("date_returned"));
            }            
        }
        catch (SQLException e){
            throw new DaoException("LoanDao.checkExistingLoan(): " + e.getMessage());
        }
        finally{
            freeResources("LoanDao.checkExistingLoan()",conn,ps,rs);
        }       
        
        return loan;    // could be null
    }
    
}
