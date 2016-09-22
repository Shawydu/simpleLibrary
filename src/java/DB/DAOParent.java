/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DB;
import OLM.CleanUp;
import OLM.ObjectLifetimeManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
/**
 *
 * @author DamoN
 */
public abstract class DAOParent extends CleanUp{
    
    
    public abstract ResultSet getQueryResult(String type) throws Exception;
    
    public abstract int excuteQuery(String sql) throws SQLException;
    
    protected abstract Connection getConnection() throws Exception;
    
    public abstract void cleanup();
    
}
