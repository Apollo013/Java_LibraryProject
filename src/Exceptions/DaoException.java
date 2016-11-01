/**
 * @Date 16/10/2013
 * @author Paul Millar <D00152098>
 */

package Exceptions;

import java.sql.SQLException;

public class DaoException extends SQLException{
    
    public DaoException(String errMessage){
        super(errMessage);
    }
    
    @Override
    public String getMessage(){
        return "Data Access Exception\n" + super.getMessage();
    }
}
