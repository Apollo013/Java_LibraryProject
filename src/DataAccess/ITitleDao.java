package DataAccess;

import Business.Title;
import Exceptions.DaoException;
import java.util.List;

/**
 * 
 * EHI
 */
public interface ITitleDao {
    public boolean insert(Title title) throws DaoException;
    public boolean update(String currentISBN, Title title) throws DaoException;
    public boolean delete(String isbn) throws DaoException;     
    public Title getTitleByISBN(String isbn) throws DaoException; 
    public Title getTitleByName(String name) throws DaoException;
    public List<Title> getAllTitles() throws DaoException;
    public List<Title> getSimilarTitlesByName(String isbn) throws DaoException;      
    public List<Title> getTitlesByAuthor(String authorName) throws DaoException;
    public List<Title> getTitlesByLocation(String location) throws DaoException;    
}
