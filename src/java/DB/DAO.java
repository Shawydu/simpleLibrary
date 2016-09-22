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
 * @author kezhang
 */
public class DAO extends DAOParent {

    private String defaultDbUrl;
    private String defaultUserName;
    private String defaultPassword;

    private DAO() {
    }
    ;
    private static DAO instance = new DAO();

    public static DAO getInstance() {
        ObjectLifetimeManager.at_exit(instance);//Register instance to ObjectLifetimeManager.
        return instance;
    }

    /**
     * For display DB.
     */
    public ResultSet getQueryResult(String sql) throws SQLException {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            return rs;
        } catch (NamingException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    /**
     * For update DB.
     */
    public int excuteQuery(String sql) throws SQLException {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            int i = stmt.executeUpdate();
            return i;
        } catch (NamingException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;

    }

    protected Connection getConnection() throws NamingException, SQLException {
        try {
            InitialContext initialContext = new InitialContext();
            Context context = (Context) initialContext.lookup("java:comp/env");
            //The JDBC Data source that we just created
            DataSource ds = (DataSource) context.lookup("SimpleLibrary");
            Connection connection = ds.getConnection();

            return connection;
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            if (!this.defaultDbUrl.isEmpty()) {
                Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);

                Properties connectionProps = new Properties();
                connectionProps.put("user", this.defaultUserName);
                connectionProps.put("password", this.defaultPassword);

                return DriverManager.getConnection(
                        this.defaultDbUrl,
                        connectionProps);
            }
        }

        return null;
    }

    public void setDefaultConnectionParams(String url, String username, String password) {
        this.defaultDbUrl = url;
        this.defaultUserName = username;
        this.defaultPassword = password;
    }
    @Override
    public void cleanup() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.      
    }

}
