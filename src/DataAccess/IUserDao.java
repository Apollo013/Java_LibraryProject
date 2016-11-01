package DataAccess;

import Business.User;
import Exceptions.DaoException;

/**
 * @date 16/10/2013
 * @author Paul Millar <D00152098>
 */
public interface IUserDao {
    
    public boolean insert(User user) throws DaoException;
    public boolean update(int currentId, User user) throws DaoException;
    public boolean delete(User user) throws DaoException;      
    public User getUserLogIn(String email, String password) throws DaoException;
    public User getUserByEmail(String email) throws DaoException;  
}
