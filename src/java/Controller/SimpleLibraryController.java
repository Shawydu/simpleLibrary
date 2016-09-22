/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import DB.DAO;
import DB.writeLog;
import Model.LoanInfo;
import Model.MonitorInfo;
import Model.TitleInfo;
import Model.User;
import Model.UserInfo;
import OLM.CleanUp;
import OLM.ObjectLifetimeManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;
import org.apache.catalina.Session;

/**
 *
 * @author kezhang
 */
public class SimpleLibraryController extends HttpServlet {

    static final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet SimpleLibraryController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SimpleLibraryController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        if (action.equalsIgnoreCase("booklist")) {

            if (session.getAttribute("username") == null) {
                session.setAttribute("warning", "Session timeout!");
                response.sendRedirect("index.jsp");
            } else {

                try {
                    int checkid = Integer.parseInt(session.getAttribute("checkid").toString());
                    String qqph = "SELECT * FROM loan WHERE UserID = %s AND LatePayment > 0";
                    String qq = String.format(qqph, checkid);
                    DAO dd = DAO.getInstance();  //singleten
                    ResultSet res = dd.getQueryResult(qq);
                    if (res.next()) {
                        response.sendRedirect("SimpleLibraryController?action=paymoney");
                    } else {

                        String query = "select title.`Isbn`, title.`Author`, title.`TitleName`, (select count(*) from item \n"
                                + "where item.`IsDiabled` = false and item.`Isbn` = title.`Isbn` and item.`ItemID` not in (select loan.`ItemID` from loan where loan.`ReturnTime` is NULL)) as AvailableCopies\n"
                                + "from title\n"
                                + "where title.`IsDiabled` = false";

                        DAO db = DAO.getInstance();
                        ResultSet rs = db.getQueryResult(query);

                        HashMap<TitleInfo, Integer> bookList = new HashMap<TitleInfo, Integer>();

                        while (rs.next()) {
                            TitleInfo title = new TitleInfo();
                            title.setIsbn(rs.getString("Isbn"));
                            title.setAuthor(rs.getString("Author"));
                            title.setTitleName(rs.getString("TitleName"));
                            bookList.put(title, rs.getInt("AvailableCopies"));
                        }

                        request.setAttribute("books", bookList);

                        RequestDispatcher disp = request.getRequestDispatcher("/jsp/booklist.jsp");
                        disp.forward(request, response);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(SimpleLibraryController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (action.equalsIgnoreCase("borrowedBooks")) {
            if (session.getAttribute("username") == null) {
                session.setAttribute("warning", "Session timeout!");
                response.sendRedirect("index.jsp");
            } else {
                try {
                    String query = "select loan.`LoanID`, loan.`ItemID`, loan.`UserID`, title.`Isbn`, title.`Author`, title.`TitleName`,\n"
                            + "loan.`StartTime`, loan.`DueTime`, loan.`RenewTimes`\n"
                            + "from loan\n"
                            + "inner join item\n"
                            + "on loan.`ItemID` = item.`ItemID`\n"
                            + "inner join title\n"
                            + "on item.`Isbn` = title.`Isbn`\n"
                            + "where loan.`ReturnTime` is NULL and loan.`UserID`=" + session.getAttribute("checkid");

                    DAO db = DAO.getInstance();
                    ResultSet rs = db.getQueryResult(query);

                    TreeMap<LoanInfo, TitleInfo> borrowedBooks = new TreeMap<LoanInfo, TitleInfo>();

                    while (rs.next()) {  //mark

                        TitleInfo title = new TitleInfo();
                        title.setIsbn(rs.getString("Isbn"));
                        title.setAuthor(rs.getString("Author"));
                        title.setTitleName(rs.getString("TitleName"));

                        LoanInfo loan = new LoanInfo();
                        loan.setLoanID(rs.getInt("LoanID"));
                        loan.setItemID(rs.getInt("ItemID"));
                        loan.setUserID(rs.getInt("UserID"));
                        loan.setStartTime(rs.getTime("StartTime"));
                        loan.setDueTime(rs.getTime("DueTime"));
                        loan.setRenewTimes(rs.getInt("RenewTimes"));

                        //mark
                        Date dNow = new Date();
                        Date due = new Date(dNow.getYear(),
                                dNow.getMonth(),
                                dNow.getDate(),
                                loan.getDueTime().getHours(),
                                loan.getDueTime().getMinutes(),
                                loan.getDueTime().getSeconds());
                        if (due.getTime() < dNow.getTime()) {
                            long latepay = (dNow.getTime() - due.getTime()) / 30000;
                            loan.setLatePayment(latepay);

                        } else {
                            long latepay = 0;
                            loan.setLatePayment(latepay);
                        }
                        //mark                    
                        borrowedBooks.put(loan, title);

                    }

                    request.setAttribute("borrowedBooks", borrowedBooks);

                    RequestDispatcher disp = request.getRequestDispatcher("/jsp/borrowedBooks.jsp");
                    disp.forward(request, response);

                } catch (SQLException ex) {
                    Logger.getLogger(SimpleLibraryController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (action.equalsIgnoreCase("renew")) {
            try {
                String loanId = request.getParameter("loanID");

                String queryPlaceholder = "update loan\n"
                        + "set DueTime = (select AddTime(DueTime,'0:2:0')), RenewTimes = RenewTimes + 1\n"
                        + "where loan.`LoanID` = %s and loan.`RenewTimes`<3";

                DAO db = DAO.getInstance();

                StringBuilder sb = new StringBuilder();
                String query = String.format(queryPlaceholder, loanId);
                int result = db.excuteQuery(query);

                if (result == 0) {
                    sb.append("Loan \"").append(loanId).append(" is not extended.\n");
                } else {
                }

                if (sb.length() > 0) {
                    request.setAttribute("borrowFails", sb.toString());
                }

                response.sendRedirect("SimpleLibraryController?action=borrowedBooks");
            } catch (SQLException ex) {
                Logger.getLogger(SimpleLibraryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (action.equals("paymoney")) {
            if (session.getAttribute("username") == null) {
                session.setAttribute("warning", "Session timeout!");
                response.sendRedirect("index.jsp");
            } else {
                String uname = session.getAttribute("username").toString();
               //String query2 = "“         

                request.setAttribute("checkname", uname);
                request.setAttribute("idd", session.getAttribute("checkid"));

                RequestDispatcher disp = request.getRequestDispatcher("/jsp/paymoney.jsp");
                disp.forward(request, response);
            }
        } else if (action.equalsIgnoreCase("return")) {
            try {

                String pay = String.valueOf(request.getParameter("pay"));
                String loanId = request.getParameter("loanID");

                Date dNow = new Date();
                SimpleDateFormat ft
                        = new SimpleDateFormat("kk:mm:ss");
                String returnTime = ft.format(dNow);

                String queryPlaceholder = "update loan\n"
                        + "set ReturnTime = '%s',\n"
                        + "LatePayment = %s\n"
                        + "where LoanID = %s";

                DAO db = DAO.getInstance();

                StringBuilder sb = new StringBuilder();
                String query = String.format(queryPlaceholder, returnTime, pay, loanId);
                int result = db.excuteQuery(query);
                if (result == 0) {
                    sb.append("Loan \"").append(loanId).append(" is not returned.\n");
                } else {
                    String insertlog = "INSERT INTO simplelibrary.monitorsystemlog (EventType, Message, EventTime) \n"
                            + "VALUES ('return', 'LoanID: " + loanId + " return book. Late payment generated: "
                            + pay + " dollars', CURRENT_TIMESTAMP)";
                    int j = db.excuteQuery(insertlog);
                }
                if (sb.length() > 0) {
                    request.setAttribute("borrowFails", sb.toString());
                }
                response.sendRedirect("SimpleLibraryController?action=borrowedBooks");
            } catch (SQLException ex) {
                Logger.getLogger(SimpleLibraryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (action.equals("signup")) {
            RequestDispatcher disp = request.getRequestDispatcher("/jsp/add_user.jsp");
            disp.forward(request, response);
        } else if (action.equals("backadmin")) {
            session.removeAttribute("info");
            session.removeAttribute("flag");
            session.removeAttribute("rtw");
            session.removeAttribute("warn");
            session.removeAttribute("ruf");
            RequestDispatcher disp = request.getRequestDispatcher("/jsp/admin_main.jsp");
            disp.forward(request, response);
        } else if (action.equals("back")) {
            RequestDispatcher disp = request.getRequestDispatcher("/jsp/user_main.jsp");
            disp.forward(request, response);
        } else if (action.equals("logout")) {
            request.getSession().invalidate();
            response.sendRedirect("index.jsp");
//             RequestDispatcher disp = request.getRequestDispatcher("/index.jsp");
//             disp.forward(request, response);       
        } else if (action.equals("inventory")) {
            try {

                DAO dao = DAO.getInstance();
                String firquery = "SELECT TitleName, Author, Isbn FROM title WHERE IsDiabled=false";
                ResultSet rs1 = dao.getQueryResult(firquery);
                rs1.last();
                int count = rs1.getRow();
                int numitem[] = new int[count];
                TitleInfo ti[] = new TitleInfo[count];
                rs1.beforeFirst();
                for (int i = 0; rs1.next(); i++) {
                    String secqueryph = "SELECT COUNT(ItemID) AS num FROM item where Isbn = '%s' and IsDiabled = false";
                    String secquery = String.format(secqueryph, rs1.getString("Isbn"));
                    ResultSet rs2 = dao.getQueryResult(secquery);
                    rs2.next();
                    numitem[i] = rs2.getInt("num");
                    ti[i] = new TitleInfo();
                    ti[i].setTitleName(rs1.getString("TitleName"));
                    ti[i].setAuthor(rs1.getString("Author"));
                    ti[i].setIsbn(rs1.getString("Isbn"));

                }

                request.setAttribute("three", ti);
                request.setAttribute("last", numitem);
                request.setAttribute("time", ti.length);
                RequestDispatcher disp = request.getRequestDispatcher("/jsp/inventory_main.jsp");
                disp.forward(request, response);

            } catch (SQLException ex) {
                Logger.getLogger(SimpleLibraryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (action.equals("addtitlepage")) {
            response.sendRedirect("jsp/add_title.jsp");
        } else if (action.equals("usermanage")) {
            try {
                String query = "select UserID, FirstName, LastName, IsDiabled from `user` where TypeID = 2;";
                DAO dao = DAO.getInstance();
                ResultSet rs = dao.getQueryResult(query);
                //Linklist ls = new LinkList;
                rs.last();
                int count = rs.getRow();
                rs.beforeFirst();
                UserInfo ui[] = new UserInfo[count];
                Boolean flag[] = new Boolean[count];
                for (int i = 0; rs.next(); i++) {

                    ui[i] = new UserInfo();
                    ui[i].setUserID(rs.getInt("UserID"));
                    ui[i].setFirstName(rs.getString("FirstName"));
                    ui[i].setLastName(rs.getString("LastName"));
                    flag[i] = rs.getBoolean("IsDiabled");
                }
                request.setAttribute("userlist", ui);
                request.setAttribute("flag", flag);
                RequestDispatcher disp = request.getRequestDispatcher("/jsp/manage_user.jsp");
                disp.forward(request, response);

            } catch (SQLException ex) {
                Logger.getLogger(SimpleLibraryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (action.equals("monitor")) {
            try {
                DAO dao = DAO.getInstance();
                String query = "select * from MonitorSystemLog";
                ResultSet rs = dao.getQueryResult(query);
                request.setAttribute("logrs", rs);
                RequestDispatcher disp = request.getRequestDispatcher("/jsp/moniter_system.jsp");
                disp.forward(request, response);

            } catch (SQLException ex) {
                Logger.getLogger(SimpleLibraryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (action.equals("export")) {
            try {
                DAO dao = DAO.getInstance();
                String query = "select * from MonitorSystemLog";
                ResultSet rs = dao.getQueryResult(query);
//                request.setAttribute("logrs", rs);
                writeLog wl = writeLog.getInstance();//Singleton             
                ObjectLifetimeManager.at_exit(wl);
                
                StringBuilder sb = new StringBuilder();
                sb.append("Monitor Log" + "\n");
                while (rs.next()) {
                    sb.append(rs.getInt("LogID") + ",");
                    sb.append(rs.getString("EventType") + ",");
                    sb.append(rs.getString("Message") + ",");
                    sb.append(rs.getTime("EventTime") + "\n");
                }
                wl.writeDown(sb.toString());
                ObjectLifetimeManager.fini();
                rs.beforeFirst();
                session.setAttribute("info", "Finished Export!");
                request.setAttribute("logrs", rs);
                RequestDispatcher disp = request.getRequestDispatcher("/jsp/moniter_system.jsp");
                disp.forward(request, response);
            } catch (SQLException ex) {
                Logger.getLogger(SimpleLibraryController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        processRequest(request, response);
        String action = request.getParameter("action");
        HttpSession session = request.getSession(true);
        //add user ----> register
        if (action.equals("register")) {
            try {
                UserInfo ui = new UserInfo();
                ui.setFirstName(request.getParameter("gname"));
                ui.setLastName(request.getParameter("sname"));
                ui.setUserID(Integer.parseInt(request.getParameter("snumber")));
                ui.setPassword(request.getParameter("pwd"));
                ui.setTypeID(2);
                //insert into user 
                String query1 = "INSERT INTO user (UserID, FirstName, LastName, Password, TypeID) VALUES" + "('" + ui.getUserID() + "', '"
                        + ui.getFirstName() + "', '" + ui.getLastName() + "', '" + ui.getPassword() + "', '"
                        + ui.getTypeID() + "')";
                String query2 = "SELECT * FROM user WHERE UserID = " + ui.getUserID();
                String insertlog = "INSERT INTO simplelibrary.monitorsystemlog (EventType, Message, EventTime) \n"
                        + "VALUES ('register', 'User: " + ui.getFirstName() + " register, id assigned: " + ui.getUserID() + "', CURRENT_TIMESTAMP)";

                DAO db = DAO.getInstance();
                ResultSet rs = db.getQueryResult(query2);

                if (!rs.next()) {
                    int i = db.excuteQuery(query1);
                    int j = db.excuteQuery(insertlog);
                    request.setAttribute("name", ui.getFirstName());

                    RequestDispatcher disp = request.getRequestDispatcher("/jsp/successfully_register.jsp");
                    disp.forward(request, response);
                } else {

                    request.setAttribute("warning", "This student ID already used to register, please consult our service!");
                    RequestDispatcher disp = request.getRequestDispatcher("/jsp/add_user.jsp");
                    disp.forward(request, response);
                }

            } catch (SQLException ex) {
                Logger.getLogger(SimpleLibraryController.class.getName()).log(Level.SEVERE, null, ex);

            } catch (NumberFormatException ex) {
                request.setAttribute("warn", "Invalid input!");
                RequestDispatcher disp = request.getRequestDispatcher("/jsp/add_user.jsp");
                disp.forward(request, response);
            }
        } //user log in (Check abstract method on my book)
        else if (action.equalsIgnoreCase("login")) {
            try {
                UserInfo ui = new UserInfo();
                ui.setUserID(Integer.parseInt(request.getParameter("uid")));
                ui.setPassword(request.getParameter("pwd"));

                String query = "SELECT FirstName, TypeID FROM user WHERE " + "UserID = " + ui.getUserID()
                        + " AND Password = '" + ui.getPassword() + "'" + " AND IsDiabled = false";
                DAO db = DAO.getInstance();
                ResultSet rs = db.getQueryResult(query);
                if (rs.next()) {
                    if (Integer.parseInt(rs.getString(2)) == 1) {
                        session.setAttribute("username", rs.getString(1));
                        session.setAttribute("checkid", ui.getUserID());//try
                        RequestDispatcher disp = request.getRequestDispatcher("/jsp/admin_main.jsp");
                        disp.forward(request, response);
                    } else {
                        session.setAttribute("username", rs.getString(1));
                        session.setAttribute("checkid", ui.getUserID());//try
                        RequestDispatcher disp = request.getRequestDispatcher("/jsp/user_main.jsp");
                        disp.forward(request, response);
                    }
                } else {
                    session.setAttribute("warning", "Incorrect username or password!");
                    RequestDispatcher disp = request.getRequestDispatcher("/index.jsp");
                    disp.forward(request, response);
                }
            } catch (SQLException ex) {
                Logger.getLogger(SimpleLibraryController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NumberFormatException ex) {
                session.setAttribute("warning", "invalid student number!");
                RequestDispatcher disp = request.getRequestDispatcher("/index.jsp");
                disp.forward(request, response);
            }

        } else if (action.equalsIgnoreCase("borrow")) {
            String[] selectedTitlesISBN = request.getParameterValues("selectedTitles");
            User user = new User(session.getAttribute("username").toString());
            String error = user.borrowItem(selectedTitlesISBN);

            if (!error.isEmpty()) {
                session.setAttribute("borrowFails", error);
            }
            response.sendRedirect("SimpleLibraryController?action=borrowedBooks");
        } else if (action.equals("makepay")) {
            try {
                LoanInfo li = new LoanInfo();
                li.setLoanID(Integer.parseInt(request.getParameter("id").toString()));
                String queryPlaceholder = "UPDATE loan SET LatePayment = 0.0 WHERE LoanID = %d";
                String mpquery = String.format(queryPlaceholder, li.getLoanID());
                DAO dao = DAO.getInstance();
                int i = dao.excuteQuery(mpquery);
                String insertlog = "INSERT INTO simplelibrary.monitorsystemlog (EventType, Message, EventTime) \n"
                        + "VALUES ('payment', 'LoanID: " + li.getLoanID() + " pay latepayment', CURRENT_TIMESTAMP)";
                int j = dao.excuteQuery(insertlog);
                response.sendRedirect("SimpleLibraryController?action=paymoney");
            } catch (SQLException ex) {
                Logger.getLogger(SimpleLibraryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (action.equals("additem")) {
            try {
                String isbncode = request.getParameter("isncode").toString();
                String queryholder = "insert into item(Isbn) values ('%s')";
                String query = String.format(queryholder, isbncode);
                DAO dao = DAO.getInstance();
                int i = dao.excuteQuery(query);
                response.sendRedirect("SimpleLibraryController?action=inventory");
            } catch (SQLException ex) {
                Logger.getLogger(SimpleLibraryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (action.equals("removeitem")) {
            try {
                String isbncode = request.getParameter("isncode").toString();
                String plusph = "select count(ItemID) as count from item where Isbn = '%s' and "
                        + "isDiabled = true";//不改动
                String compare1ph = "select count(ItemID) as whole from item where Isbn = '%s' and "
                        + "isDiabled = false";//all
                String compare2ph = "select count(item.ItemID) as com from item inner "
                        + "join loan on item.ItemID = loan.ItemID\n"
                        + "where loan.LatePayment is null and item.IsDiabled = false and item.Isbn = '%s'";           //Start here!!
                String plus = String.format(plusph, isbncode);//已经被diabled的
                String all = String.format(compare1ph, isbncode);//有的
                String compare = String.format(compare2ph, isbncode);//正在借的

                DAO fir = DAO.getInstance();

                ResultSet rs = fir.getQueryResult(plus);
                ResultSet rsall = fir.getQueryResult(all);
                ResultSet rs1 = fir.getQueryResult(compare);
                rs.next();
                rsall.next();
                rs1.next();

                int second = rs.getInt("count");//不参与比较
                int zong = rsall.getInt("whole");
                int third = rs1.getInt("com");
                int result = zong - third;

                if (result > 0) {
                    String queryholder = "update item set isDiabled = true where item.Isbn = '%s' and item.ItemID not in \n"
                            + "(select loan.ItemID from loan where loan.LatePayment is null) and item.IsDiabled = false limit 1";//改,删除除借走之外的
                    String query = String.format(queryholder, isbncode);
                    DAO dao = DAO.getInstance();
                    int i = dao.excuteQuery(query);
                    response.sendRedirect("SimpleLibraryController?action=inventory");

                } else {
                    session.setAttribute("flag", "All this kind of item are currently in loan and not be returned, or there is no item available!");
                    response.sendRedirect("SimpleLibraryController?action=inventory");
                }
            } catch (SQLException ex) {
                Logger.getLogger(SimpleLibraryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (action.equals("removetitle")) {
            try {
                String isbncode = request.getParameter("isncode").toString();
                String searchph = "select item.Isbn from item inner join loan on item.ItemID = loan.ItemID where loan.LatePayment is null and\n"
                        + "item.Isbn = '%s'";
                String queryph = "update title set IsDiabled = true where Isbn = '%s'";
                String itemqueryph = "update item set IsDiabled = true where Isbn = '%s'";

                String search = String.format(searchph, isbncode);
                String query = String.format(queryph, isbncode);
                String itemquery = String.format(itemqueryph, isbncode);

                DAO dao = DAO.getInstance();
                ResultSet rs = dao.getQueryResult(search);
                rs.last();
                int count = rs.getRow();
                if (count == 0) {
                    int i = dao.excuteQuery(query);
                    int j = dao.excuteQuery(itemquery);
                    response.sendRedirect("SimpleLibraryController?action=inventory");

                } else {
                    session.setAttribute("rtw", "Cannot remove the title since some items under this title may currently in loan.");//change here!
                    response.sendRedirect("SimpleLibraryController?action=inventory");
                }
            } catch (SQLException ex) {
                Logger.getLogger(SimpleLibraryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (action.equals("addtitle")) {
            try {
                TitleInfo ti = new TitleInfo();
                ti.setTitleName(request.getParameter("title").toString());
                ti.setAuthor(request.getParameter("author").toString());
                ti.setIsbn(request.getParameter("isbn").toString());
                String searchph = "select title.Isbn, title.IsDiabled from title where title.Isbn='%s'";
                String queryPlaceholder = "insert into title (Isbn, Author, TitleName)"
                        + " VALUES ('%s', '%s', '%s')";
                String search = String.format(searchph, ti.getIsbn());
                String query = String.format(queryPlaceholder, ti.getIsbn(), ti.getAuthor(), ti.getTitleName());

                DAO dao = DAO.getInstance();
                ResultSet rs = dao.getQueryResult(search);
                rs.last();
                int num = rs.getRow();
                if (num > 0) {
                    //What to do
                    if (rs.getBoolean("IsDiabled")) {
                        //re-enable this record in database
                        queryPlaceholder = "update title set IsDiabled = false\n"
                                + "Where title.Isbn='%s'";
                        String update = String.format(queryPlaceholder, ti.getIsbn());
                        int i = dao.excuteQuery(update);
                        response.sendRedirect("SimpleLibraryController?action=inventory");
                    } else {
                        session.setAttribute("warn", "This title already exists!");
                        response.sendRedirect("SimpleLibraryController?action=addtitlepage");
                    }
                } else {
                    session.removeAttribute("warn");
                    int i = dao.excuteQuery(query);
                    response.sendRedirect("SimpleLibraryController?action=inventory");
                }
            } catch (SQLException ex) {
                Logger.getLogger(SimpleLibraryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (action.equals("removeuser")) {
            try {
                DAO dao = DAO.getInstance();
                int userid = Integer.parseInt(request.getParameter("uid").toString());
                String checkph = "select * from loan where UserID = %d and "
                        + "(LatePayment is null or LatePayment > 0)";
                String check = String.format(checkph, userid);
                ResultSet rs = dao.getQueryResult(check);
                rs.last();
                int i = rs.getRow();
                rs.beforeFirst();
                if (i == 0) {

                    session.removeAttribute("ruf");
                    String deleteph = "update `user` set IsDiabled = true where UserID = %d";
                    String delete = String.format(deleteph, userid);
                    dao.excuteQuery(delete);
                    response.sendRedirect("SimpleLibraryController?action=usermanage");
                } else {
                    session.setAttribute("ruf", "This user currently has a loan record so cannot be removed!");
                    response.sendRedirect("SimpleLibraryController?action=usermanage");
                }
            } catch (SQLException ex) {
                Logger.getLogger(SimpleLibraryController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
