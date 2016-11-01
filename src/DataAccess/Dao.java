/**
 * @Date 15/10/2013 - 16/10/2013
 * @author Ehi Omorede & Paul Millar <D00152098>
 */
package DataAccess;

import Exceptions.DaoException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Dao {
        
    /**
     * Creates a new connection object for the database
     * @return a new connection object for the database
     * @throws DaoException
     */
    public Connection getConnection() throws DaoException {

        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/library";
        String username = "root";
        String password = ""; //apollo013
        
        Connection conn = null;
        
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
        } 
        catch (ClassNotFoundException cnfe) {
            System.out.println("Failed To Find Driver Class:\n" + cnfe.getMessage());
            System.exit(1);
        } 
        catch (SQLException sqle) {
            System.out.println("Connection Failed:\n" + sqle.getMessage());
            System.exit(2);
        }
        return conn;
    }
    
    /**
     * Closes & Frees a Connection object
     * @param conn Connection object to free.
     * @throws DaoException
     */
    public void freeConnection(Connection conn) throws DaoException {
        try {
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (SQLException sqle) {
            System.out.println("Failed To Free Connection:\n" + sqle.getMessage());
            System.exit(2);
        }
    }
    
    /**
     * Closes & Frees a Statement object.
     * @param stmt Statement object to free
     */
    public void freePreparedStatement(PreparedStatement ps) throws DaoException {
        try{
            if(ps != null){
                ps.close();
                ps = null;
            }
        } 
        catch(SQLException sqle){
            System.out.println("Failed To Free Statement:\n" + sqle.getMessage());
            System.exit(2);
        }
    } 
    
    /**
     * Closes & Frees a ResultSet object.
     * @param stmt ResultSet object to free
     */    
    public void freeResultSet(ResultSet rs) throws DaoException {
        try{
            if(rs != null){
                rs.close();
                rs = null;
            }            
        }
        catch(SQLException sqle){
            System.out.println("Failed To Free Result Set:\n" + sqle.getMessage());
            System.exit(2);
        }        
    }
       
    /**
     * Closes & Frees all connection resource object.
     * @param errMessageTitle Title of the error message to be displayed (if one occurs)
     * @param conn Connection
     * @param ps PreparedStatement
     * @param rs ResultSet
     * @throws DaoException
     */
    public void freeResources(String errMessageTitle,Connection conn,PreparedStatement ps,ResultSet rs) throws DaoException{
        try {
            if (rs != null) {
                freeResultSet(rs);
            }
            if (ps != null) {
                freePreparedStatement(ps);
            }
            if (conn != null) {
                freeConnection(conn);
            }
        } catch (SQLException e) {
            throw new DaoException(errMessageTitle + e.getMessage());
        }        
    }
    
}
    
