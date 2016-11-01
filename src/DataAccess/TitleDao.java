package DataAccess;

import Application.CurrentUser;
import Business.Title;
import Exceptions.DaoException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * EHI
 */
public class TitleDao extends Dao implements ITitleDao{

    /**
     * Inserts a new Title in the database
     * @param title
     * @return true if the insert succeeded, false otherwise.
     * @throws DaoException
     */
    @Override
    public boolean insert(Title title) throws DaoException {
        
        // We have to be an administrator to perform this operation.
        if(!CurrentUser.isAdministrator()){
            System.out.println("YOU MUST BE AN ADMINISTRATOR TO PERFORM THIS ACTION");
            return false;
        }
        
        // Make sure title details are valid
        if(!title.isValid()){
            title.displayErrors();  
            throw new DaoException("THIE TITLE HAS ERRORS, PLEASE TRY AGAIN.");            
        }    
        
        Connection          conn            = null;
        PreparedStatement   ps              = null;
        int                 rowsAffected    = 0;
        
        try{
            conn = getConnection();   

            String sql = "INSERT INTO titles (isbn, name, location, total_stock, quantity_on_loan, loan_period, authors) VALUES (?,?,?,?,?,?,?)";
        
            ps = conn.prepareStatement(sql);
            ps.setString(1, title.getIsbn()); 
            ps.setString(2, title.getName()); 
            ps.setString(3, title.getLocation()); 
            ps.setInt(4, title.getCopies());
            ps.setInt(5, title.getQuantityOnLoan());
            ps.setByte(6, title.getLoanPeriod());
            ps.setString(7, title.getTitleAuthors());
            
            rowsAffected = ps.executeUpdate();     

        }
        catch (SQLException e) {
            throw new DaoException("TitleDao.insert(): " + e.getMessage()); 
        }
        finally{
            freeConnection(conn);
            freePreparedStatement(ps);
        }
        
        // Return true or false based on wheteher there was an affected row.
        return (rowsAffected == 1 ? true : false);        
    }

    /**
     * Updates the details of a title in the system
     * @param currentISBN The current ISBN of the title prior to any changes.
     * @param title The Title object with the details to be updated
     * @return true if the update was successful, false otherwise.
     * @throws DaoException
     */
    @Override
    public boolean update(String currentISBN, Title title) throws DaoException {
        
        // Note: We are going to allow the user to change the ISBN Number even though it is the primary key.
        // The reason for this is that because it is entered by the user, mistakes could be made in doing so.
        // Therefore, we must allow them the chance to correct any errors. This change will propagate down into
        // the 'loans' table.
        
        // We have to be an administrator to perform this operation.
        if(!CurrentUser.isAdministrator()){
            System.out.println("YOU MUST BE AN ADMINISTRATOR TO PERFORM THIS ACTION");
            return false;
        }
                        
        // Make sure title details are valid
        if(!title.isValid()){
            title.displayErrors();  
            throw new DaoException("THIE TITLE HAS ERRORS, PLEASE TRY AGAIN.");            
        } 
        
        String sql = "UPDATE titles SET isbn = ?, name = ?, location = ?, total_stock = ?, quantity_on_loan = ?, loan_period = ? , authors = ? WHERE isbn = ?";
        Connection          conn            = null;
        PreparedStatement   ps              = null; 
        int                 rowsAffected    = 0;

        try{
            conn = getConnection();

            ps = conn.prepareStatement(sql);
            ps.setString(1, title.getIsbn());            
            ps.setString(2, title.getName()); 
            ps.setString(3, title.getLocation()); 
            ps.setInt(4, title.getCopies());
            ps.setInt(5, title.getQuantityOnLoan());
            ps.setByte(6, title.getLoanPeriod());
            ps.setString(7,title.getTitleAuthors());
            ps.setString(8, currentISBN);
            
            rowsAffected = ps.executeUpdate();              
        }
         catch (SQLException e) {
            throw new DaoException("TitleDao.update(): " + e.getMessage()); 
        }
        finally{
            freeConnection(conn);
            freePreparedStatement(ps);
        }

        // Return true or false based on wheteher there was an affected row.
        return (rowsAffected == 1 ? true : false);        
    }

    /**
     * Removes a title from the system
     * @param titleISBN The current ISBN in the system.
     * @return true if the delete was successful, false otherwise.
     * @throws DaoException
     */
    @Override
    public boolean delete(String isbn) throws DaoException {
        
        // Note - Deletes will not be permitted if there are related
        //        rows in the loans table, due to foreign key constraints in the DBMS.
        
        // We have to be an administrator to perform this operation.
        if(!CurrentUser.isAdministrator()){
            System.out.println("YOU MUST BE AN ADMINISTRATOR TO PERFORM THIS ACTION");
            return false;
        }
        
        String sql = "DELETE FROM titles WHERE isbn = ?";        
        Connection          conn            = null;
        PreparedStatement   ps              = null;
        int                 rowsAffected    = 0;

        try{
            conn = getConnection();

            ps = conn.prepareStatement(sql);
            ps.setString(1, isbn); 
           
            rowsAffected = ps.executeUpdate();              
        }
         catch (SQLException e) {
            throw new DaoException("TitleDao.delete(): " + e.getMessage()); 
        }
        finally{
            freeConnection(conn);
            freePreparedStatement(ps);
        }

        // Return true or false based on wheteher there was an affected row.
        return (rowsAffected == 1 ? true : false); 
    }

    /**
     * Searches for and returns any title(s) that match the ISBN.
     * @param isbn a 13 character unique identifier for a title
     * @return A title that exactly matches the provided ISBN.  (or null if not found)
     * @throws DaoException
     */
    @Override
    public Title getTitleByISBN(String isbn) throws DaoException {

        Connection          conn    = null;
        PreparedStatement   ps      = null;
        ResultSet           rs      = null;  
        Title               title   = null;
        
        try{
            conn = getConnection();
            
            String query = "SELECT * FROM titles WHERE isbn = ? LIMIT 1";
            ps = conn.prepareStatement(query);
            ps.setString(1, isbn);
            
            rs = ps.executeQuery();
            
            // Only create a new title object if we found a match.
            if(rs.next()){
                title =  new Title( rs.getString("isbn"),
                                    rs.getString("name"),
                                    rs.getString("location"),
                                    rs.getInt("total_stock"),
                                    rs.getInt("quantity_on_loan"),
                                    rs.getByte("loan_period"),
                                    rs.getString("authors"));                       
            }
        }
        catch (SQLException e) {
            throw new DaoException("TitleDao.getTitleByISBN(): " + e.getMessage()); 
        }
        finally{
            freeResources("TitleDao.getTitleByISBN(): ",conn,ps,rs);
        }
      
        return title;   // This could be null
    }

    /**
     * Searches for and returns a title that exactly matches the provided title name.
     * @param name The title name
     * @return A title that exactly matches the provided title name. (or null if not found)
     * @throws DaoException
     */
    @Override
    public Title getTitleByName(String name) throws DaoException {

        Connection          conn    = null;
        PreparedStatement   ps      = null;
        ResultSet           rs      = null;  
        Title               title   = null;
        
        try{
            conn = getConnection();
            
            // Search for name using uppercase values
            String query = "SELECT * FROM titles WHERE UPPER(name) = ? LIMIT 1";
            ps = conn.prepareStatement(query);
            ps.setString(1, name.toUpperCase());
            
            rs = ps.executeQuery();
            
            // Only create a new title object if we found a match.
            if(rs.next()){
                title =  new Title( rs.getString("isbn"),
                                    rs.getString("name"),
                                    rs.getString("location"),
                                    rs.getInt("total_stock"),
                                    rs.getInt("quantity_on_loan"),
                                    rs.getByte("loan_period"),
                                    rs.getString("authors"));                       
            }  
        }
        catch (SQLException e) {
            throw new DaoException("TitleDao.getTitleByName(): " + e.getMessage()); 
        }
        finally{
            freeResources("TitleDao.getTitleByName(): ",conn,ps,rs);
        }
      
        return title;   // This could be null
    }

    /**
     * Retrieves all titles in the database
     * @return An array list of titles
     * @throws DaoException
     */
    @Override
    public List<Title> getAllTitles() throws DaoException {
        
        Connection          conn    = null;
        PreparedStatement   ps      = null;
        ResultSet           rs      = null;    
        List<Title>         titlesList  = new ArrayList<Title>();
        
        try{
            conn = getConnection();
            
            String query = "SELECT * FROM titles ORDER BY name";
            ps = conn.prepareStatement(query);            
            rs = ps.executeQuery();
            
            while(rs.next()){
                titlesList.add(new Title(   rs.getString("isbn"),
                                            rs.getString("name"),
                                            rs.getString("location"),
                                            rs.getInt("total_stock"),
                                            rs.getInt("quantity_on_loan"),
                                            rs.getByte("loan_period"),
                                            rs.getString("authors")));                        
            } 
        }
        catch (SQLException e) {
            throw new DaoException("TitleDao.getAllTitles(): " + e.getMessage()); 
        }
        finally{
            freeResources("TitleDao.getAllTitles(): ",conn,ps,rs);
        }
      
        return titlesList;  
    }
    
    /**
     * Searches for and returns a list of similar titles by name only.
     * <br/>Note this will split the String parameter (by empty space) and search for each word individually.
     * @param name
     * @param loadRelatedAuthors
     * @return An array list of titles
     * @throws DaoException
     */
    @Override
    public List<Title> getSimilarTitlesByName(String name) throws DaoException {

        Connection          conn        = null;
        PreparedStatement   ps          = null;
        ResultSet           rs          = null;  
        ArrayList<Title>    titlesList  = new ArrayList<Title>();
        
        try{
            conn = getConnection();
            
            String query = "SELECT * FROM titles WHERE UPPER(name) LIKE ?";

            // Split the string so we can search for all possible values
            String[] searchNames = name.split(" ");
            
            // Iterate through the searchNames array and search the DB for each value
            for(int i = 0 ; i < searchNames.length ; i ++){

                ps = conn.prepareStatement(query); 
                
                // Our parameter contains the '%' symbol to be used with the LIKE keyword
                ps.setString(1, "%" + searchNames[i].toUpperCase() + "%");

                rs = ps.executeQuery();     
                
                // Only add a title if it does not already exist in the list & 
                // it does not match the title name exactly (we're only looking for similar titles)
                while(rs.next()){
                    Title t =  new Title(   rs.getString("isbn"),
                                            rs.getString("name"),
                                            rs.getString("location"),
                                            rs.getInt("total_stock"),
                                            rs.getInt("quantity_on_loan"),
                                            rs.getByte("loan_period"),
                                            rs.getString("authors"));
                    if(!titlesList.contains(t) && !t.getName().equals(name)){
                        titlesList.add(t);
                    }
                }                
            }
        }
        catch (SQLException sqe) {
            throw new DaoException("TitleDao.getSimilarTitlesByName(): " + sqe.getMessage()); 
        }
        finally{
            freeResources("TitleDao.getSimilarTitlesByName(): ",conn,ps,rs);
        }
      
        return titlesList;
    }

    /**
     * Searches for and returns any title(s) that match the authors name.
     * @param authorName The authors name
     * @return An array list of titles.
     * @throws DaoException
     */
    @Override
    public List<Title> getTitlesByAuthor(String authorName) throws DaoException {
        
        Connection          conn        = null;
        PreparedStatement   ps          = null;
        ResultSet           rs          = null;  
        ArrayList<Title>    titlesList  = new ArrayList<Title>();
        
        try{
            
            conn = getConnection();
            
            String query = "SELECT * FROM titles WHERE UPPER(authors) LIKE ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, "%" + authorName.toUpperCase() + "%");
            
            rs = ps.executeQuery();
            
            // add new title objects if matches were found.
            while(rs.next()){
                titlesList.add(new Title(   rs.getString("isbn"),
                                            rs.getString("name"),
                                            rs.getString("location"),
                                            rs.getInt("total_stock"),
                                            rs.getInt("quantity_on_loan"),
                                            rs.getByte("loan_period"),
                                            rs.getString("authors")));                        
            } 
            
        } catch (SQLException sqe){
            throw new DaoException("TitleDao.getTitlesByAuthor(): " + sqe.getMessage());
        }
        finally{
            freeResources("TitleDao.getTitlesByAuthor(): ",conn,ps,rs);
        }
        
        return titlesList;
    }

    /**
     * Searches for and returns any title(s) that match the location.
     * @param location
     * @return An array list of titles.
     * @throws DaoException
     */
    @Override
    public List<Title> getTitlesByLocation(String location) throws DaoException {
        
        Connection          conn        = null;
        PreparedStatement   ps          = null;
        ResultSet           rs          = null;  
        ArrayList<Title>    titlesList  = new ArrayList<Title>();
        
        try{
            
            conn = getConnection();
            
            String query = "SELECT * FROM titles WHERE UPPER(location) LIKE ?";
            ps = conn.prepareStatement(query);
            ps.setString(1, "%" + location.toUpperCase() + "%");
            
            rs = ps.executeQuery();
            
            // add new title objects if matches were found.
            while(rs.next()){
                titlesList.add(new Title(   rs.getString("isbn"),
                                            rs.getString("name"),
                                            rs.getString("location"),
                                            rs.getInt("total_stock"),
                                            rs.getInt("quantity_on_loan"),
                                            rs.getByte("loan_period"),
                                            rs.getString("authors")));                        
            } 
            
        } 
        catch (SQLException sqe){
            throw new DaoException("TitleDao.getTitlesByLocation(): " + sqe.getMessage());
        }
        finally{
            freeResources("TitleDao.getTitlesByLocation(): ",conn,ps,rs);
        }
        
        return titlesList;
    }
     
}
