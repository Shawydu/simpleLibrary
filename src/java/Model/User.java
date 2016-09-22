/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Controller.SimpleLibraryController;
import DB.DAO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author yeshang
 */
public class User {

    static final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs
    private UserInfo ui;

    public User(String fname) {
        try {
            DAO db = DAO.getInstance();
            this.ui = new UserInfo();
            String myquery = "select UserID from user where FirstName = '" + fname + "'";

            ResultSet rs = db.getQueryResult(myquery);
            while (rs.next()) {
                this.ui.setFirstName(fname);
                this.ui.setUserID(rs.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public User(String firstName, String lastName, String password) {
        this.ui = new UserInfo();
        this.ui.setFirstName(firstName);
        this.ui.setLastName(lastName);
        this.ui.setPassword(password);
    }

    public int Login() {
        try {
            String query = "SELECT FirstName, TypeID FROM user WHERE " + "UserID = " + ui.getUserID()
                    + " AND Password = '" + ui.getPassword() + "'" + " AND IsDiabled = false";
            DAO db = DAO.getInstance();
            ResultSet rs = db.getQueryResult(query);
            if (rs.next()) {
                return Integer.parseInt(rs.getString(2));
            } else {
                return -1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SimpleLibraryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public String borrowItem(String[] selectedTitlesISBN) {
        try {
            if (selectedTitlesISBN.length == 0) {
                //Confirm with no selection
            }

            int userid = this.ui.getUserID();
            String queryPlaceholder = "insert into loan(ItemID, UserID, StartTime, DueTime)\n"
                    + "select item.`ItemID`, %d, '%s', '%s'\n"
                    + "from item\n"
                    + "where item.`Isbn`='%s' and item.`IsDiabled`=false \n"
                    + "and item.`ItemID` not in (select ItemID from loan where loan.`ReturnTime` is null)\n"
                    + "order by item.`ItemID` asc limit 1;";

            Date dNow = new Date();
            SimpleDateFormat ft =
                    new SimpleDateFormat("kk:mm:ss");
            String startTime = ft.format(dNow);

            long t = dNow.getTime();
            Date due = new Date(t + (5 * ONE_MINUTE_IN_MILLIS));
            String dueTime = ft.format(due);

            StringBuilder sb = new StringBuilder();
            for (String selectedString : selectedTitlesISBN) {
                String isbn = selectedString.split("\\|")[0];
                String titleName = selectedString.split("\\|")[1];
                DAO dba = DAO.getInstance();
                String query = String.format(queryPlaceholder, userid, startTime, dueTime, isbn);
                int result = dba.excuteQuery(query);

                if (result == 0) {
                    sb.append("Book \"").append(titleName).append("\", (ISBN:  ").append(isbn).append(") is not borrowed, no copies left.\n");
                } else {
                    String insertlog = "INSERT INTO simplelibrary.monitorsystemlog (EventType, Message, EventTime) \n"
                            + "VALUES ('borrow', 'User: " + userid + " borrow book: " + titleName + ", ISBN: "
                            + isbn + "', CURRENT_TIMESTAMP)";
                    int j = dba.excuteQuery(insertlog);
                }
            }
            return sb.toString();
        } catch (SQLException ex) {
            Logger.getLogger(SimpleLibraryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
}
