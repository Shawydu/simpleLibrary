/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TestPackage;

import DB.DAO;
import java.sql.SQLException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import net.sourceforge.jwebunit.api.IElement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import net.sourceforge.jwebunit.junit.WebTester;

/**
 *
 * @author yeshang
 */
public class WebApplicationTest {

    static final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs
    static final String BASE_URL = "http://localhost:8084/SimpleLibrary";
    static final String USER_ID = "1";
    static final String USER_PASSWORD = "123456";
    static final String RESET_DB = "CALL `simplelibrary`.`ResetTestData`();";
    Lock sequential = new ReentrantLock();

    public WebApplicationTest() {
    }

    @Before
    public void setUp() {
        try {
            DAO db;
            db = DAO.getInstance();
            db.setDefaultConnectionParams("jdbc:mysql://localhost:3306/SimpleLibrary", "root", "123456");
            int result = db.excuteQuery(RESET_DB);

            setBaseUrl(BASE_URL);
            beginAt("index.jsp");
            setTextField("uid", USER_ID);
            setTextField("pwd", USER_PASSWORD);
            submit();
            assertTitleEquals("User Main");
        } catch (SQLException ex) {
            Logger.getLogger(WebApplicationTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        sequential.lock();
    }

    @Test
    public void test1() {
        beginAt("index.jsp"); //Open the browser on http://localhost:8084/SimpleLibrary/index.jsp
        assertTitleEquals("Welcome Page");
    }

    //TC1	Add	a	user	
    @Test
    public void AddUserTest() {
        //Logout first
        clickLinkWithExactText("Log Out");

        clickLinkWithExactText("Haven't register yet?");
        assertTitleEquals("Register");
        setTextField("gname", "New User 111");
        setTextField("sname", "New User 111");
        setTextField("snumber", "1234567890");
        setTextField("pwd", "123456");
        submit();
        assertTitleEquals("Congratulations");
        assertTextPresent("Congratulations, you have successfully registered on our Simple Library System!");
    }

    //TC2	Add	two	users	 	 
    //TC2a	Add	2	users	using	2	librarians
    //We let user to self-register
    //TC3	Add	new	title	
    //UC2:	Administrator	adds	Title
    @Test
    public void AddTitleTest() {
        //Admin login
        WebTester testerAdmin = new WebTester();
        testerAdmin.setBaseUrl("http://localhost:8084/SimpleLibrary");
        testerAdmin.beginAt("index.jsp");
        testerAdmin.assertTitleEquals("Welcome Page");
        testerAdmin.setTextField("uid", "2");
        testerAdmin.setTextField("pwd", "123456");
        testerAdmin.submit();
        testerAdmin.assertTitleEquals("User Main");
        testerAdmin.clickLinkWithExactText("Inventory Management");
        testerAdmin.assertTitleEquals("Inventory Management");

        testerAdmin.clickLinkWithExactText("Add Title");
        testerAdmin.assertTitleEquals("Add Title");
        testerAdmin.setTextField("title", "New book 111");
        testerAdmin.setTextField("author", "Author 111");
        testerAdmin.setTextField("isbn", "1234567890");
        testerAdmin.submit();
        testerAdmin.assertTitleEquals("Inventory Management");

        testerAdmin.assertTextPresent("1234567890");
    }

    //TC4	Add	two	titles	 
    @Test
    public void AddTitle_TwoTitlesTest() {
        //Admin login
        WebTester testerAdmin = new WebTester();
        testerAdmin.setBaseUrl("http://localhost:8084/SimpleLibrary");
        testerAdmin.beginAt("index.jsp");
        testerAdmin.assertTitleEquals("Welcome Page");
        testerAdmin.setTextField("uid", "2");
        testerAdmin.setTextField("pwd", "123456");
        testerAdmin.submit();
        testerAdmin.assertTitleEquals("User Main");
        testerAdmin.clickLinkWithExactText("Inventory Management");
        testerAdmin.assertTitleEquals("Inventory Management");

        testerAdmin.clickLinkWithExactText("Add Title");
        testerAdmin.assertTitleEquals("Add Title");
        testerAdmin.setTextField("title", "New book 111");
        testerAdmin.setTextField("author", "Author 111");
        testerAdmin.setTextField("isbn", "1234567890");
        testerAdmin.submit();
        testerAdmin.assertTitleEquals("Inventory Management");
        testerAdmin.assertTextPresent("1234567890");

        testerAdmin.clickLinkWithExactText("Add Title");
        testerAdmin.assertTitleEquals("Add Title");
        testerAdmin.setTextField("title", "New book 222");
        testerAdmin.setTextField("author", "Author 222");
        testerAdmin.setTextField("isbn", "0123456789");
        testerAdmin.submit();
        testerAdmin.assertTitleEquals("Inventory Management");
        testerAdmin.assertTextPresent("0123456789");
    }

    //TC4a	Add	two	titles	using	2	librarians
    @Test
    public void AddTitle_TwoTitlesUsingTwoAdminsTest() {
        //Admin1 login
        WebTester testerAdmin1 = new WebTester();
        testerAdmin1.setBaseUrl("http://localhost:8084/SimpleLibrary");
        testerAdmin1.beginAt("index.jsp");
        testerAdmin1.assertTitleEquals("Welcome Page");
        testerAdmin1.setTextField("uid", "2");
        testerAdmin1.setTextField("pwd", "123456");
        testerAdmin1.submit();
        testerAdmin1.assertTitleEquals("User Main");
        testerAdmin1.clickLinkWithExactText("Inventory Management");
        testerAdmin1.assertTitleEquals("Inventory Management");

        //Admin2 login
        WebTester testerAdmin2 = new WebTester();
        testerAdmin2.setBaseUrl("http://localhost:8084/SimpleLibrary");
        testerAdmin2.beginAt("index.jsp");
        testerAdmin2.assertTitleEquals("Welcome Page");
        testerAdmin2.setTextField("uid", "6");
        testerAdmin2.setTextField("pwd", "123456");
        testerAdmin2.submit();
        testerAdmin2.assertTitleEquals("User Main");
        testerAdmin2.clickLinkWithExactText("Inventory Management");
        testerAdmin2.assertTitleEquals("Inventory Management");

        //Admin1 add one title
        testerAdmin1.clickLinkWithExactText("Add Title");
        testerAdmin1.assertTitleEquals("Add Title");
        testerAdmin1.setTextField("title", "New book 111");
        testerAdmin1.setTextField("author", "Author 111");
        testerAdmin1.setTextField("isbn", "1234567890");
        testerAdmin1.submit();
        testerAdmin1.assertTitleEquals("Inventory Management");
        testerAdmin1.assertTextPresent("1234567890");

        //Admin2 add the other title
        testerAdmin2.clickLinkWithExactText("Add Title");
        testerAdmin2.assertTitleEquals("Add Title");
        testerAdmin2.setTextField("title", "New book 222");
        testerAdmin2.setTextField("author", "Author 222");
        testerAdmin2.setTextField("isbn", "0123456789");
        testerAdmin2.submit();
        testerAdmin2.assertTitleEquals("Inventory Management");
        testerAdmin2.assertTextPresent("0123456789");
    }

    //TC5	Add	new	copy	of	title
    @Test
    public void AddItemTest() {
        //Admin login
        WebTester testerAdmin = new WebTester();
        testerAdmin.setBaseUrl("http://localhost:8084/SimpleLibrary");
        testerAdmin.beginAt("index.jsp");
        testerAdmin.assertTitleEquals("Welcome Page");
        testerAdmin.setTextField("uid", "2");
        testerAdmin.setTextField("pwd", "123456");
        testerAdmin.submit();
        testerAdmin.assertTitleEquals("User Main");
        testerAdmin.clickLinkWithExactText("Inventory Management");
        testerAdmin.assertTitleEquals("Inventory Management");

        List<IElement> addItemForms = testerAdmin.getElementsByXPath("//form[@action='SimpleLibraryController?action=additem' and @method='POST']");
        if (addItemForms.size() > 0) {
            IElement firstAddItemForm = addItemForms.get(0);
            int currentNumOfCopies = Integer.parseInt(firstAddItemForm.getParent().getParent().getChildren().get(3).getTextContent());
            String isbn = firstAddItemForm.getParent().getParent().getChildren().get(2).getTextContent();

            IElement firstAddItemButton = firstAddItemForm.getChildren().get(1);
            testerAdmin.clickButton(firstAddItemButton.getAttribute("id"));

            //Admin check the number of item to see if it increase 1
            IElement isbnTD = testerAdmin.getElementByXPath("//td[text()='" + isbn + "']");
            if (isbnTD != null) {
                assertEquals(currentNumOfCopies + 1, Integer.parseInt(isbnTD.getParent().getChildren().get(3).getTextContent()));
            }
        }
    }

    //TC6	Add	multiples	of	several	titles
    @Test
    public void AddItem_MultipleCopiesOfSeveralTitlesTest() {
        //Admin login
        WebTester testerAdmin = new WebTester();
        testerAdmin.setBaseUrl("http://localhost:8084/SimpleLibrary");
        testerAdmin.beginAt("index.jsp");
        testerAdmin.assertTitleEquals("Welcome Page");
        testerAdmin.setTextField("uid", "2");
        testerAdmin.setTextField("pwd", "123456");
        testerAdmin.submit();
        testerAdmin.assertTitleEquals("User Main");
        testerAdmin.clickLinkWithExactText("Inventory Management");
        testerAdmin.assertTitleEquals("Inventory Management");

        List<IElement> addItemForms = testerAdmin.getElementsByXPath("//form[@action='SimpleLibraryController?action=additem' and @method='POST']");
        if (addItemForms.size() >= 3) {
            //Add 3 copies for title1
            IElement firstAddItemForm = addItemForms.get(0);
            int firstCurrentNumOfCopies = Integer.parseInt(firstAddItemForm.getParent().getParent().getChildren().get(3).getTextContent());
            String firstIsbn = firstAddItemForm.getParent().getParent().getChildren().get(2).getTextContent();

            IElement firstAddItemButton = firstAddItemForm.getChildren().get(1);
            testerAdmin.clickButton(firstAddItemButton.getAttribute("id"));
            testerAdmin.clickButton(firstAddItemButton.getAttribute("id"));
            testerAdmin.clickButton(firstAddItemButton.getAttribute("id"));

            //Admin check the number of item to see if it increase 3
            IElement firstIsbnTD = testerAdmin.getElementByXPath("//td[text()='" + firstIsbn + "']");
            if (firstIsbnTD != null) {
                assertEquals(firstCurrentNumOfCopies + 3, Integer.parseInt(firstIsbnTD.getParent().getChildren().get(3).getTextContent()));
            }


            //Add 2 copies for title2
            IElement secondAddItemForm = addItemForms.get(1);
            int secondCurrentNumOfCopies = Integer.parseInt(secondAddItemForm.getParent().getParent().getChildren().get(3).getTextContent());
            String secondIsbn = secondAddItemForm.getParent().getParent().getChildren().get(2).getTextContent();

            IElement secondAddItemButton = secondAddItemForm.getChildren().get(1);
            testerAdmin.clickButton(secondAddItemButton.getAttribute("id"));
            testerAdmin.clickButton(secondAddItemButton.getAttribute("id"));

            //Admin check the number of item to see if it increase 2
            IElement secondIsbnTD = testerAdmin.getElementByXPath("//td[text()='" + secondIsbn + "']");
            if (secondIsbnTD != null) {
                assertEquals(secondCurrentNumOfCopies + 2, Integer.parseInt(secondIsbnTD.getParent().getChildren().get(3).getTextContent()));
            }


            //Add 1 copy for title3
            IElement thirdAddItemForm = addItemForms.get(2);
            int thirdCurrentNumOfCopies = Integer.parseInt(thirdAddItemForm.getParent().getParent().getChildren().get(3).getTextContent());
            String thirdIsbn = thirdAddItemForm.getParent().getParent().getChildren().get(2).getTextContent();

            IElement thirdAddItemButton = thirdAddItemForm.getChildren().get(1);
            testerAdmin.clickButton(thirdAddItemButton.getAttribute("id"));

            //Admin check the number of item to see if it increase 1
            IElement thirdIsbnTD = testerAdmin.getElementByXPath("//td[text()='" + thirdIsbn + "']");
            if (thirdIsbnTD != null) {
                assertEquals(thirdCurrentNumOfCopies + 1, Integer.parseInt(thirdIsbnTD.getParent().getChildren().get(3).getTextContent()));
            }
        }
    }

    //UC4:	User	borrows	title
    //TC7	User	borrows	item	
    @Test
    public void BorrowLoanCopy_SelectOneBookTest() {
        clickLinkWithExactText("Books Available");

        List<IElement> checkBoxElements = getElementsByXPath("//input[@type=\"checkbox\" and @name=\"selectedTitles\"]");
        if (checkBoxElements.size() > 0) {
            IElement firstCheckbox = checkBoxElements.get(0);

            checkCheckbox("selectedTitles", firstCheckbox.getAttribute("value"));
            submit();
            assertTitleEquals("Your borrowed books");

            String isbn = firstCheckbox.getAttribute("value").split("\\|")[0];
            assertTextPresent(isbn);
            assertTextNotPresent("ISBN:");
        }
    }

    //TC8	Three	users	borrow	same	title
    @Test
    public void BorrowLoanCopy_ThreeUsersBorrowSameTitleTest() {
        WebTester tester1 = new WebTester();
        tester1.setBaseUrl("http://localhost:8084/SimpleLibrary");
        tester1.beginAt("index.jsp");
        tester1.assertTitleEquals("Welcome Page");
        tester1.setTextField("uid", "1");
        tester1.setTextField("pwd", "123456");
        tester1.submit();
        tester1.assertTitleEquals("User Main");
        tester1.clickLinkWithExactText("Books Available");
        List<IElement> trElements = tester1.getElementsByXPath("//tbody/tr");
        String selectedBookCheckBoxValue = "";
        String isbn = "";
        for (IElement tr : trElements) {
            if (Integer.parseInt(tr.getChildren().get(4).getTextContent()) >= 3) {
                selectedBookCheckBoxValue = tr.getChildren().get(0).getChildren().get(0).getAttribute("value");
                break;
            }
        }
        if (!selectedBookCheckBoxValue.isEmpty()) {
            isbn = selectedBookCheckBoxValue.split("\\|")[0];
            tester1.checkCheckbox("selectedTitles", selectedBookCheckBoxValue);
            tester1.submit();
            tester1.assertTitleEquals("Your borrowed books");

            tester1.assertTextPresent(isbn);
        }


        WebTester tester2 = new WebTester();
        tester2.setBaseUrl("http://localhost:8084/SimpleLibrary");
        tester2.beginAt("index.jsp");
        tester2.assertTitleEquals("Welcome Page");
        tester2.setTextField("uid", "3");
        tester2.setTextField("pwd", "123456");
        tester2.submit();
        tester2.assertTitleEquals("User Main");
        tester2.clickLinkWithExactText("Books Available");
        if (!selectedBookCheckBoxValue.isEmpty()) {
            tester2.checkCheckbox("selectedTitles", selectedBookCheckBoxValue);
            tester2.submit();
            tester2.assertTitleEquals("Your borrowed books");

            tester2.assertTextPresent(isbn);
        }

        WebTester tester3 = new WebTester();
        tester3.setBaseUrl("http://localhost:8084/SimpleLibrary");
        tester3.beginAt("index.jsp");
        tester3.assertTitleEquals("Welcome Page");
        tester3.setTextField("uid", "4");
        tester3.setTextField("pwd", "123456");
        tester3.submit();
        tester3.assertTitleEquals("User Main");
        tester3.clickLinkWithExactText("Books Available");
        if (!selectedBookCheckBoxValue.isEmpty()) {
            tester3.checkCheckbox("selectedTitles", selectedBookCheckBoxValue);
            tester3.submit();
            tester3.assertTitleEquals("Your borrowed books");

            tester3.assertTextPresent(isbn);
        }
    }

    //T8a	One	user	borrows	2	copies	of	the	same	title
    @Test
    public void BorrowLoanCopy_OneUserBorrowsTwoCopiesOfOneBookTest() {
        clickLinkWithExactText("Books Available");

        List<IElement> trElements = getElementsByXPath("//tbody/tr");
        String selectedBookCheckBoxValue = "";
        String isbn = "";
        for (IElement tr : trElements) {
            if (Integer.parseInt(tr.getChildren().get(4).getTextContent()) >= 2) {
                selectedBookCheckBoxValue = tr.getChildren().get(0).getChildren().get(0).getAttribute("value");
                break;
            }
        }
        if (!selectedBookCheckBoxValue.isEmpty()) {
            isbn = selectedBookCheckBoxValue.split("\\|")[0];

            //Borrow first copy
            checkCheckbox("selectedTitles", selectedBookCheckBoxValue);
            submit();
            assertTitleEquals("Your borrowed books");
            assertTextPresent(isbn);

            clickLinkWithExactText("Back");
            clickLinkWithExactText("Books Available");

            //Borrow second copy
            checkCheckbox("selectedTitles", selectedBookCheckBoxValue);
            submit();
            assertTitleEquals("Your borrowed books");
            assertTextPresent(isbn);

            //Check if two borrow records with same ISBN
            List<IElement> isbnTDElements = getElementsByXPath("//tbody/tr/td[3][text()='" + isbn + "']");
            assertEquals(2, isbnTDElements.size());
        }
    }

    //TC9	Three	users	borrow	same	title	(only	two	copies)
    @Test
    public void BorrowLoanCopy_ThreeUsersBorrowSameTitleButOnlyTwoCopiesTest() {
        WebTester tester1 = new WebTester();
        tester1.setBaseUrl("http://localhost:8084/SimpleLibrary");
        tester1.beginAt("index.jsp");
        tester1.assertTitleEquals("Welcome Page");
        tester1.setTextField("uid", "1");
        tester1.setTextField("pwd", "123456");
        tester1.submit();
        tester1.assertTitleEquals("User Main");
        tester1.clickLinkWithExactText("Books Available");



        WebTester tester2 = new WebTester();
        tester2.setBaseUrl("http://localhost:8084/SimpleLibrary");
        tester2.beginAt("index.jsp");
        tester2.assertTitleEquals("Welcome Page");
        tester2.setTextField("uid", "3");
        tester2.setTextField("pwd", "123456");
        tester2.submit();
        tester2.assertTitleEquals("User Main");
        tester2.clickLinkWithExactText("Books Available");



        WebTester tester3 = new WebTester();
        tester3.setBaseUrl("http://localhost:8084/SimpleLibrary");
        tester3.beginAt("index.jsp");
        tester3.assertTitleEquals("Welcome Page");
        tester3.setTextField("uid", "4");
        tester3.setTextField("pwd", "123456");
        tester3.submit();
        tester3.assertTitleEquals("User Main");
        tester3.clickLinkWithExactText("Books Available");

        List<IElement> trElements = tester1.getElementsByXPath("//tbody/tr");
        String selectedBookCheckBoxValue = "";
        String isbn = "";
        for (IElement tr : trElements) {
            if (Integer.parseInt(tr.getChildren().get(4).getTextContent()) == 2) {
                selectedBookCheckBoxValue = tr.getChildren().get(0).getChildren().get(0).getAttribute("value");
                break;
            }
        }

        if (!selectedBookCheckBoxValue.isEmpty()) {
            isbn = selectedBookCheckBoxValue.split("\\|")[0];
            tester1.checkCheckbox("selectedTitles", selectedBookCheckBoxValue);
            tester1.submit();
            tester1.assertTitleEquals("Your borrowed books");

            tester1.assertTextPresent(isbn);
        }

        if (!selectedBookCheckBoxValue.isEmpty()) {
            tester2.checkCheckbox("selectedTitles", selectedBookCheckBoxValue);
            tester2.submit();
            tester2.assertTitleEquals("Your borrowed books");

            tester2.assertTextPresent(isbn);
        }

        if (!selectedBookCheckBoxValue.isEmpty()) {
            tester3.checkCheckbox("selectedTitles", selectedBookCheckBoxValue);
            tester3.submit();
            tester3.assertTitleEquals("Your borrowed books");

            tester3.assertTextNotPresent(isbn);
        }
    }

//    //@Test
//    public void BorrowLoanCopy_SelectMultipleBooksTest() {
//        clickLinkWithExactText("Books Available");
//
//        List<IElement> checkBoxElements = getElementsByXPath("//input[@type=\"checkbox\" and @name=\"selectedTitles\"]");
//        if (checkBoxElements.size() > 1) {
//            IElement firstCheckbox = checkBoxElements.get(0);
//            IElement secondCheckbox = checkBoxElements.get(1);
//
//            checkCheckbox("selectedTitles", firstCheckbox.getAttribute("value"));
//            checkCheckbox("selectedTitles", secondCheckbox.getAttribute("value"));
//            submit();
//            assertTitleEquals("Your borrowed books");
//
//            String firstIsbn = firstCheckbox.getAttribute("value").split("\\|")[0];
//            String secondIsbn = secondCheckbox.getAttribute("value").split("\\|")[0];
//            assertTextPresent(firstIsbn);
//            assertTextPresent(secondIsbn);
//            assertTextNotPresent("ISBN:");
//        }
//    }
//
//    //@Test
//    public void BorrowLoanCopy_NoCopyLeftTest() {
//        clickLinkWithExactText("Books Available");
//
//        List<IElement> trElements = getElementsByXPath("//tbody/tr");
//
//        for (IElement tr : trElements) {
//            if ("0".equals(tr.getChildren().get(4).getTextContent())) {
//                if (tr.getChildren().get(0).getChildren().size() != 0) {
//                    fail("Still contain checkbox");
//                }
//            }
//        }
//    }
    //UC5:	User	renews	loan
    //TC10	User	renews	loan
    @Test
    public void RenewLoanTest() {
        clickLinkWithExactText("Books Available");

        List<IElement> checkBoxElements = getElementsByXPath("//input[@type=\"checkbox\" and @name=\"selectedTitles\"]");
        if (checkBoxElements.size() > 0) {
            IElement firstCheckbox = checkBoxElements.get(0);

            checkCheckbox("selectedTitles", firstCheckbox.getAttribute("value"));
            submit();
            assertTitleEquals("Your borrowed books");

            String isbn = firstCheckbox.getAttribute("value").split("\\|")[0];
            assertTextPresent(isbn);
            assertTextNotPresent("ISBN:");

            List<IElement> elements = getElementsByXPath("//a[@class='renew_loan']");
            if (elements.size() > 0) {
                IElement renewLink = elements.get(0);
                IElement dueTimeTD = renewLink.getParent().getParent().getChildren().get(4);
                String dueTime = dueTimeTD.getTextContent();
                //"21:53:13";
                SimpleDateFormat ft = new SimpleDateFormat("kk:mm:ss");
                Date beforeRenewDueTime = ft.parse(dueTime, new ParsePosition(0));

                long t = beforeRenewDueTime.getTime();
                Date afterAddingTwoMins = new Date(t + (2 * ONE_MINUTE_IN_MILLIS));

                clickLink(renewLink.getAttribute("id"));

                assertTextInElement(dueTimeTD.getAttribute("id"), ft.format(afterAddingTwoMins));
            }
        }
    }

    //TC11	User	renews	loan	as	loan	expires	
    @Test
    public void RenewLoan_AsCloseAsLoanExpiresTest() {
        clickLinkWithExactText("Books Available");

        List<IElement> checkBoxElements = getElementsByXPath("//input[@type=\"checkbox\" and @name=\"selectedTitles\"]");
        if (checkBoxElements.size() > 0) {
            try {
                IElement firstCheckbox = checkBoxElements.get(0);

                checkCheckbox("selectedTitles", firstCheckbox.getAttribute("value"));
                submit();
                assertTitleEquals("Your borrowed books");

                String isbn = firstCheckbox.getAttribute("value").split("\\|")[0];
                assertTextPresent(isbn);
                assertTextNotPresent("ISBN:");

                IElement isbnTD = getElementByXPath("//tbody/tr/td[text()='" + isbn + "']");
                String renewLinkID = isbnTD.getParent().getChildren().get(5).getChildren().get(0).getAttribute("id");
                String loanID = renewLinkID.split("_")[2];

                //Change the duet time is 1 second after start time
                String queryPlaceholder = "update loan\n"
                        + "set DueTime = (select AddTime(StartTime,'0:1:30'))\n"
                        + "where loan.`LoanID` = %s";

                DAO db;
                db = DAO.getInstance();
                String query = String.format(queryPlaceholder, loanID);
                int result = db.excuteQuery(query);

                //Refresh to see the changes DueTime value
                clickLinkWithExactText("Back");
                clickLinkWithExactText("My Borrowed");
                assertTitleEquals("Your borrowed books");

                List<IElement> elements = getElementsByXPath("//a[@class='renew_loan']");
                if (elements.size() > 0) {
                    IElement renewLink = elements.get(0);
                    IElement dueTimeTD = renewLink.getParent().getParent().getChildren().get(4);
                    String dueTime = dueTimeTD.getTextContent();

                    SimpleDateFormat ft = new SimpleDateFormat("kk:mm:ss");
                    Date beforeRenewDueTime = ft.parse(dueTime, new ParsePosition(0));

                    long t = beforeRenewDueTime.getTime();
                    Date afterAddingTwoMins = new Date(t + (2 * ONE_MINUTE_IN_MILLIS));

                    //Wait 1 minute to let loan close to expire
                    Thread.sleep(ONE_MINUTE_IN_MILLIS);

                    clickLink(renewLink.getAttribute("id"));

                    assertTextInElement(dueTimeTD.getAttribute("id"), ft.format(afterAddingTwoMins));
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(WebApplicationTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(WebApplicationTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //TC12	User	renews	after	max	renewals
    @Test
    public void RenewLoan_MaximumRenewalsTest() {
        clickLinkWithExactText("Books Available");

        List<IElement> checkBoxElements = getElementsByXPath("//input[@type=\"checkbox\" and @name=\"selectedTitles\"]");
        if (checkBoxElements.size() > 0) {
            IElement firstCheckbox = checkBoxElements.get(0);

            checkCheckbox("selectedTitles", firstCheckbox.getAttribute("value"));
            submit();
            assertTitleEquals("Your borrowed books");

            String isbn = firstCheckbox.getAttribute("value").split("\\|")[0];
            assertTextPresent(isbn);
            assertTextNotPresent("ISBN:");

            List<IElement> elements = getElementsByXPath("//a[@class='renew_loan']");
            if (elements.size() > 0) {
                String renewLinkId = elements.get(0).getAttribute("id");
                IElement renewTD = elements.get(0).getParent();

                clickLink(renewLinkId);
                clickLink(renewLinkId);
                clickLink(renewLinkId);

                assertTextInElement(renewTD.getAttribute("id"), "Cannot renew more than 3 times");
            }
        }
    }

    //TC13	User	attempts	renewal	after	loan	expires	
    @Test
    public void RenewLoan_ExpiredTest() {
        clickLinkWithExactText("Books Available");

        List<IElement> checkBoxElements = getElementsByXPath("//input[@type=\"checkbox\" and @name=\"selectedTitles\"]");
        if (checkBoxElements.size() > 0) {
            try {
                IElement firstCheckbox = checkBoxElements.get(0);

                checkCheckbox("selectedTitles", firstCheckbox.getAttribute("value"));
                submit();
                assertTitleEquals("Your borrowed books");

                String isbn = firstCheckbox.getAttribute("value").split("\\|")[0];
                assertTextPresent(isbn);
                assertTextNotPresent("ISBN:");

                IElement isbnTD = getElementByXPath("//tbody/tr/td[text()='" + isbn + "']");
                String renewLinkID = isbnTD.getParent().getChildren().get(5).getChildren().get(0).getAttribute("id");
                String loanID = renewLinkID.split("_")[2];

                //Change the duet time is 1 second after start time
                String queryPlaceholder = "update loan\n"
                        + "set DueTime = StartTime\n"
                        + "where loan.`LoanID` = %s";

                DAO db;
                db = DAO.getInstance();
                String query = String.format(queryPlaceholder, loanID);
                int result = db.excuteQuery(query);

                //Wait 1 minute to let loan expire
                Thread.sleep(ONE_MINUTE_IN_MILLIS);

                clickLinkWithExactText("Back");
                clickLinkWithExactText("My Borrowed");
                assertTitleEquals("Your borrowed books");
                List<IElement> dueTimeTDElements = getElementsByXPath("//tbody/tr/td[5]");
                if (dueTimeTDElements.size() > 0) {
                    Date dNow = new Date();
                    for (IElement dueTimeTD : dueTimeTDElements) {
                        String dueTimeText = dueTimeTD.getTextContent();
                        SimpleDateFormat ft = new SimpleDateFormat("kk:mm:ss");
                        Date tempDueTime = ft.parse(dueTimeText, new ParsePosition(0));
                        Date realDueTime = new Date(dNow.getYear(), dNow.getMonth(), dNow.getDate(), tempDueTime.getHours(), tempDueTime.getMinutes(), tempDueTime.getSeconds());
                        if (realDueTime.getTime() < dNow.getTime()) {
                            IElement renewTD = dueTimeTD.getParent().getChildren().get(5);
                            Assert.assertTrue(renewTD.getTextContent().trim().contains("This loan has expired"));
                        }
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(WebApplicationTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(WebApplicationTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //TC14{a,b,c}	User	returns	item
    //UC9:	User	Returns	Item	
    @Test
    public void ReturnLoanCopyTest() {
        clickLinkWithExactText("Books Available");

        List<IElement> checkBoxElements = getElementsByXPath("//input[@type=\"checkbox\" and @name=\"selectedTitles\"]");
        if (checkBoxElements.size() > 0) {

            IElement firstCheckbox = checkBoxElements.get(0);

            checkCheckbox("selectedTitles", firstCheckbox.getAttribute("value"));
            submit();
            assertTitleEquals("Your borrowed books");

            String isbn = firstCheckbox.getAttribute("value").split("\\|")[0];
            assertTextPresent(isbn);
            assertTextNotPresent("ISBN:");

            List<IElement> elements = getElementsByXPath("//a[@class='return_loan']");
            if (elements.size() > 0) {
                IElement borrowedIsbnTD = getElementByXPath("//tbody/tr/td[text()='" + isbn + "']");
                IElement returnLink = borrowedIsbnTD.getParent().getChildren().get(6).getChildren().get(0);
                clickLink(returnLink.getAttribute("id"));

                assertTextNotPresent(isbn);
            }
        }
    }

    //T14d:	an	extension	of	T14a:	u1	borrows	the	sole	copy	of	a	title,	returns	it	in	time	and	user	u2	borrows	 t1…		
    @Test
    public void ReturnLoanCopy_User1ReturnSoleCopyAndThenUser2LoanTest() {
        WebTester tester1 = new WebTester();
        tester1.setBaseUrl("http://localhost:8084/SimpleLibrary");
        tester1.beginAt("index.jsp");
        tester1.assertTitleEquals("Welcome Page");
        tester1.setTextField("uid", "1");
        tester1.setTextField("pwd", "123456");
        tester1.submit();
        tester1.assertTitleEquals("User Main");
        tester1.clickLinkWithExactText("Books Available");

        List<IElement> trElements = tester1.getElementsByXPath("//tbody/tr");
        String selectedBookCheckBoxValue = "";
        String isbn = "";
        for (IElement tr : trElements) {
            if (Integer.parseInt(tr.getChildren().get(4).getTextContent()) == 1) {
                selectedBookCheckBoxValue = tr.getChildren().get(0).getChildren().get(0).getAttribute("value");
                break;
            }
        }
        if (!selectedBookCheckBoxValue.isEmpty()) {
            isbn = selectedBookCheckBoxValue.split("\\|")[0];
            tester1.checkCheckbox("selectedTitles", selectedBookCheckBoxValue);
            tester1.submit();
            tester1.assertTitleEquals("Your borrowed books");
            //User 1 borrowed the sole copy.
            tester1.assertTextPresent(isbn);

            WebTester tester2 = new WebTester();
            tester2.setBaseUrl("http://localhost:8084/SimpleLibrary");
            tester2.beginAt("index.jsp");
            tester2.assertTitleEquals("Welcome Page");
            tester2.setTextField("uid", "3");
            tester2.setTextField("pwd", "123456");
            tester2.submit();
            tester2.assertTitleEquals("User Main");
            tester2.clickLinkWithExactText("Books Available");
            IElement isbnTD = tester2.getElementByXPath("//tbody/tr/td[text()='" + isbn + "']");
            //User 2 will see 0 in "Availiable Copies" cell.
            assertEquals(0, Integer.parseInt(isbnTD.getParent().getChildren().get(4).getTextContent()));

            IElement borrowedIsbnTD = tester1.getElementByXPath("//tbody/tr/td[text()='" + isbn + "']");
            IElement returnLink = borrowedIsbnTD.getParent().getChildren().get(6).getChildren().get(0);
            tester1.clickLink(returnLink.getAttribute("id"));
            //User 1 returns the copy.
            tester1.assertTextNotPresent(isbn);

            tester2.clickLinkWithText("Back");
            tester2.clickLinkWithExactText("Books Available");
            IElement isbnTDAfterReturn = tester2.getElementByXPath("//tbody/tr/td[text()='" + isbn + "']");
            //User 2 is able to see 1 in "Available Copies" cell.
            assertEquals(1, Integer.parseInt(isbnTDAfterReturn.getParent().getChildren().get(4).getTextContent()));
            tester2.checkCheckbox("selectedTitles", selectedBookCheckBoxValue);
            tester2.submit();
            tester2.assertTitleEquals("Your borrowed books");
            //User 2 borrow this copy.
            tester2.assertTextPresent(isbn);
        }
    }

    //UC10:	User	Pays	late	fee	
    @Test
    public void LatePaymentTest() {
        clickLinkWithExactText("Books Available");

        List<IElement> checkBoxElements = getElementsByXPath("//input[@type=\"checkbox\" and @name=\"selectedTitles\"]");
        if (checkBoxElements.size() > 0) {
            try {
                IElement firstCheckbox = checkBoxElements.get(0);

                checkCheckbox("selectedTitles", firstCheckbox.getAttribute("value"));
                submit();
                assertTitleEquals("Your borrowed books");

                String isbn = firstCheckbox.getAttribute("value").split("\\|")[0];
                assertTextPresent(isbn);
                assertTextNotPresent("ISBN:");

                IElement isbnTD = getElementByXPath("//tbody/tr/td[text()='" + isbn + "']");
                String renewLinkID = isbnTD.getParent().getChildren().get(5).getChildren().get(0).getAttribute("id");
                String loanID = renewLinkID.split("_")[2];

                //Change the duet time is 1 second after start time
                String queryPlaceholder = "update loan\n"
                        + "set DueTime = StartTime\n"
                        + "where loan.`LoanID` = %s";

                DAO db;
                db = DAO.getInstance();
                String query = String.format(queryPlaceholder, loanID);
                int result = db.excuteQuery(query);

                //Wait 1 minute to let loan expire
                Thread.sleep(ONE_MINUTE_IN_MILLIS);

                clickLinkWithExactText("Back");
                clickLinkWithExactText("My Borrowed");
                assertTitleEquals("Your borrowed books");

                IElement borrowedIsbnTD = getElementByXPath("//tbody/tr/td[text()='" + isbn + "']");
                IElement returnLink = borrowedIsbnTD.getParent().getChildren().get(6).getChildren().get(0);
                clickLink(returnLink.getAttribute("id"));

                assertTextNotPresent(isbn);

                clickLinkWithExactText("Back");
                clickLinkWithExactText("Late Payment");
                assertTitleEquals("Late Payment");

                List<IElement> elements;
                elements = getElementsByXPath("//tbody/form/tr");
                if (!elements.isEmpty()) {
                    String firstLoanId = getElementByXPath("//tbody/form/tr[1]/td[1]").getTextContent();
                    clickElementByXPath("//input[@type=\"submit\" and @value=\"Make Payment\"][1]");

                    List<IElement> loanIdTDs = getElementsByXPath("//tbody/form/tr/td[1][text()='" + firstLoanId + "']");
                    assertEquals(0, loanIdTDs.size());
                }
            } catch (SQLException ex) {
                Logger.getLogger(WebApplicationTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(WebApplicationTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //TC15	User	attempts	to	borrow	while	still	having	an	unpaid	late	fee	
    //UC10:	User	Pays	late	fee	
    @Test
    public void LatePayment_CannotSeeBooklistTest() {
        clickLinkWithExactText("Books Available");

        List<IElement> checkBoxElements = getElementsByXPath("//input[@type=\"checkbox\" and @name=\"selectedTitles\"]");
        if (checkBoxElements.size() > 0) {
            try {
                IElement firstCheckbox = checkBoxElements.get(0);

                checkCheckbox("selectedTitles", firstCheckbox.getAttribute("value"));
                submit();
                assertTitleEquals("Your borrowed books");

                String isbn = firstCheckbox.getAttribute("value").split("\\|")[0];
                assertTextPresent(isbn);
                assertTextNotPresent("ISBN:");

                IElement isbnTD = getElementByXPath("//tbody/tr/td[text()='" + isbn + "']");
                String renewLinkID = isbnTD.getParent().getChildren().get(5).getChildren().get(0).getAttribute("id");
                String loanID = renewLinkID.split("_")[2];

                //Change the duet time is 1 second after start time
                String queryPlaceholder = "update loan\n"
                        + "set DueTime = StartTime\n"
                        + "where loan.`LoanID` = %s";

                DAO db;
                db = DAO.getInstance();
                String query = String.format(queryPlaceholder, loanID);
                int result = db.excuteQuery(query);

                //Wait 1 minute to let loan expire
                Thread.sleep(ONE_MINUTE_IN_MILLIS);

                clickLinkWithExactText("Back");
                clickLinkWithExactText("Late Payment");
                assertTitleEquals("Late Payment");

                List<IElement> elements = getElementsByXPath("//tbody/form/tr");
                if (!elements.isEmpty()) {
                    clickLinkWithExactText("Back");

                    clickLinkWithExactText("Books Available");
                    assertTitleEquals("Late Payment");
                }
            } catch (SQLException ex) {
                Logger.getLogger(WebApplicationTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(WebApplicationTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
//
//    //@Test
//    public void LatePayment_CanSeeBooklistTest() {
//        clickLinkWithExactText("Late Payment");
//        assertTitleEquals("Late Payment");
//
//        List<IElement> elements = getElementsByXPath("//tbody/form/tr");
//        if (elements.isEmpty()) {
//            clickLinkWithExactText("Back");
//
//            clickLinkWithExactText("Books Available");
//            assertTitleEquals("List of books");
//        }
//    }

    //TC16	Remove	a	user
    @Test
    public void RemoveUserTest() {
        //Admin login
        WebTester testerAdmin = new WebTester();
        testerAdmin.setBaseUrl("http://localhost:8084/SimpleLibrary");
        testerAdmin.beginAt("index.jsp");
        testerAdmin.assertTitleEquals("Welcome Page");
        testerAdmin.setTextField("uid", "2");
        testerAdmin.setTextField("pwd", "123456");
        testerAdmin.submit();
        testerAdmin.assertTitleEquals("User Main");
        testerAdmin.clickLinkWithExactText("User Management");
        testerAdmin.assertTitleEquals("User Management");

        List<IElement> removeUserButtonElements = testerAdmin.getElementsByXPath("//input[@type=\"submit\" and @value=\"Remove\"]");
        if (removeUserButtonElements.size() > 0) {
            String firstRemoveUserButtonID = removeUserButtonElements.get(0).getAttribute("id");
            String userID = firstRemoveUserButtonID.split("_")[2];
            testerAdmin.clickButton(firstRemoveUserButtonID);
            testerAdmin.assertTitleEquals("User Management");

            List<IElement> userIdTDs = testerAdmin.getElementsByXPath("//tr/td[1][text()=\"" + userID + "\"]");
            if (userIdTDs.size() > 0) {
                assertEquals("No", userIdTDs.get(0).getParent().getChildren().get(3).getTextContent().trim());
            }
        }
    }

    //TC17	Remove	a	user	with	outstanding	fee
    @Test
    public void RemoveUser_WithOutstandingFeeTest() {
        clickLinkWithExactText("Books Available");

        List<IElement> checkBoxElements = getElementsByXPath("//input[@type=\"checkbox\" and @name=\"selectedTitles\"]");
        if (checkBoxElements.size() > 0) {
            try {
                IElement firstCheckbox = checkBoxElements.get(0);

                checkCheckbox("selectedTitles", firstCheckbox.getAttribute("value"));
                submit();
                assertTitleEquals("Your borrowed books");

                String isbn = firstCheckbox.getAttribute("value").split("\\|")[0];
                assertTextPresent(isbn);
                assertTextNotPresent("ISBN:");

                clickLinkWithExactText("Back");
                clickLinkWithExactText("My Borrowed");
                assertTitleEquals("Your borrowed books");

                IElement isbnTD = getElementByXPath("//tbody/tr/td[text()='" + isbn + "']");
                String returnLinkID = isbnTD.getParent().getChildren().get(6).getChildren().get(0).getAttribute("id");
                String loanID = returnLinkID.split("_")[2];

                //Change the duet time is 1 second after start time
                String queryPlaceholder = "update loan\n"
                        + "set DueTime = StartTime\n"
                        + "where loan.`LoanID` = %s";

                DAO db;
                db = DAO.getInstance();
                String query = String.format(queryPlaceholder, loanID);
                int result = db.excuteQuery(query);

                //Wait 1 minute to let loan expire
                Thread.sleep(ONE_MINUTE_IN_MILLIS);

                clickLinkWithExactText("Back");
                clickLinkWithExactText("My Borrowed");
                assertTitleEquals("Your borrowed books");
                IElement expiredIsbnTD = getElementByXPath("//tbody/tr/td[text()='" + isbn + "']");
                IElement expiredRenewTD = expiredIsbnTD.getParent().getChildren().get(5);
                Assert.assertTrue(expiredRenewTD.getTextContent().trim().contains("This loan has expired"));

                //Return
                clickLink(returnLinkID);
                assertTextNotPresent(isbn);


                //Admin login
                WebTester testerAdmin = new WebTester();
                testerAdmin.setBaseUrl("http://localhost:8084/SimpleLibrary");
                testerAdmin.beginAt("index.jsp");
                testerAdmin.assertTitleEquals("Welcome Page");
                testerAdmin.setTextField("uid", "2");
                testerAdmin.setTextField("pwd", "123456");
                testerAdmin.submit();
                testerAdmin.assertTitleEquals("User Main");
                testerAdmin.clickLinkWithExactText("User Management");
                testerAdmin.assertTitleEquals("User Management");

                List<IElement> removeUserButtonElements = testerAdmin.getElementsByXPath("//input[@type=\"submit\" and @value=\"Remove\" and @id=\"remove_user_1\"]");
                if (removeUserButtonElements.size() > 0) {
                    String userID = "1";
                    testerAdmin.clickButton("remove_user_1");
                    testerAdmin.assertTitleEquals("User Management");

                    List<IElement> userIdTDs = testerAdmin.getElementsByXPath("//tr/td[1][text()=\"" + userID + "\"]");
                    if (userIdTDs.size() > 0) {
                        assertEquals("Yes", userIdTDs.get(0).getParent().getChildren().get(3).getTextContent().trim());
                        testerAdmin.assertTextPresent("This user currently has a loan record so cannot be removed!");
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(WebApplicationTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(WebApplicationTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //TC18      Remove	a	user	with	item	on	loan
    @Test
    public void RemoveUser_WithItemOnLoanTest() {
        clickLinkWithExactText("Books Available");

        List<IElement> checkBoxElements = getElementsByXPath("//input[@type=\"checkbox\" and @name=\"selectedTitles\"]");
        if (checkBoxElements.size() > 0) {
            try {
                IElement firstCheckbox = checkBoxElements.get(0);

                checkCheckbox("selectedTitles", firstCheckbox.getAttribute("value"));
                submit();
                assertTitleEquals("Your borrowed books");

                String isbn = firstCheckbox.getAttribute("value").split("\\|")[0];
                assertTextPresent(isbn);
                assertTextNotPresent("ISBN:");

                clickLinkWithExactText("Back");
                clickLinkWithExactText("My Borrowed");
                assertTitleEquals("Your borrowed books");

                IElement isbnTD = getElementByXPath("//tbody/tr/td[text()='" + isbn + "']");
                String returnLinkID = isbnTD.getParent().getChildren().get(6).getChildren().get(0).getAttribute("id");
                String loanID = returnLinkID.split("_")[2];

                //Change the duet time is 1 second after start time
                String queryPlaceholder = "update loan\n"
                        + "set DueTime = StartTime\n"
                        + "where loan.`LoanID` = %s";

                DAO db;
                db = DAO.getInstance();
                String query = String.format(queryPlaceholder, loanID);
                int result = db.excuteQuery(query);

                //Wait 1 minute to let loan expire
                Thread.sleep(ONE_MINUTE_IN_MILLIS);

                clickLinkWithExactText("Back");
                clickLinkWithExactText("My Borrowed");
                assertTitleEquals("Your borrowed books");
                IElement expiredIsbnTD = getElementByXPath("//tbody/tr/td[text()='" + isbn + "']");
                IElement expiredRenewTD = expiredIsbnTD.getParent().getChildren().get(5);
                Assert.assertTrue(expiredRenewTD.getTextContent().trim().contains("This loan has expired"));

                //Admin login
                WebTester testerAdmin = new WebTester();
                testerAdmin.setBaseUrl("http://localhost:8084/SimpleLibrary");
                testerAdmin.beginAt("index.jsp");
                testerAdmin.assertTitleEquals("Welcome Page");
                testerAdmin.setTextField("uid", "2");
                testerAdmin.setTextField("pwd", "123456");
                testerAdmin.submit();
                testerAdmin.assertTitleEquals("User Main");
                testerAdmin.clickLinkWithExactText("User Management");
                testerAdmin.assertTitleEquals("User Management");

                List<IElement> removeUserButtonElements = testerAdmin.getElementsByXPath("//input[@type=\"submit\" and @value=\"Remove\" and @id=\"remove_user_1\"]");
                if (removeUserButtonElements.size() > 0) {
                    String userID = "1";
                    testerAdmin.clickButton("remove_user_1");
                    testerAdmin.assertTitleEquals("User Management");

                    List<IElement> userIdTDs = testerAdmin.getElementsByXPath("//tr/td[1][text()=\"" + userID + "\"]");
                    if (userIdTDs.size() > 0) {
                        assertEquals("Yes", userIdTDs.get(0).getParent().getChildren().get(3).getTextContent().trim());
                        testerAdmin.assertTextPresent("This user currently has a loan record so cannot be removed!");
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(WebApplicationTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(WebApplicationTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //TC19	User	pays	fine	after	loan	expires	 
    @Test
    public void LatePayment_PayAfterLoanExpiresTest() {
        clickLinkWithExactText("Books Available");

        List<IElement> checkBoxElements = getElementsByXPath("//input[@type=\"checkbox\" and @name=\"selectedTitles\"]");
        if (checkBoxElements.size() > 0) {
            try {
                IElement firstCheckbox = checkBoxElements.get(0);

                checkCheckbox("selectedTitles", firstCheckbox.getAttribute("value"));
                submit();
                assertTitleEquals("Your borrowed books");

                String isbn = firstCheckbox.getAttribute("value").split("\\|")[0];
                assertTextPresent(isbn);
                assertTextNotPresent("ISBN:");

                clickLinkWithExactText("Back");
                clickLinkWithExactText("My Borrowed");
                assertTitleEquals("Your borrowed books");

                IElement isbnTD = getElementByXPath("//tbody/tr/td[text()='" + isbn + "']");
                String returnLinkID = isbnTD.getParent().getChildren().get(6).getChildren().get(0).getAttribute("id");
                String loanID = returnLinkID.split("_")[2];

                //Change the duet time is 1 second after start time
                String queryPlaceholder = "update loan\n"
                        + "set DueTime = StartTime\n"
                        + "where loan.`LoanID` = %s";

                DAO db;
                db = DAO.getInstance();
                String query = String.format(queryPlaceholder, loanID);
                int result = db.excuteQuery(query);

                //Wait 1 minute to let loan expire
                Thread.sleep(ONE_MINUTE_IN_MILLIS);

                clickLinkWithExactText("Back");
                clickLinkWithExactText("My Borrowed");
                assertTitleEquals("Your borrowed books");
                IElement expiredIsbnTD = getElementByXPath("//tbody/tr/td[text()='" + isbn + "']");
                IElement expiredRenewTD = expiredIsbnTD.getParent().getChildren().get(5);
                Assert.assertTrue(expiredRenewTD.getTextContent().trim().contains("This loan has expired"));

                //Return
                clickLink(returnLinkID);
                assertTextNotPresent(isbn);

                clickLinkWithExactText("Back");
                clickLinkWithExactText("Late Payment");
                assertTitleEquals("Late Payment");

                IElement loanIDHiddenField = getElementByXPath("//input[@name='id' and @type='hidden' and @value='" + loanID + "']");
                if (loanIDHiddenField != null) {
                    clickButton("makepay_loanId_" + loanID);
                    List<IElement> afterPaymentLoanIDHiddenFields = getElementsByXPath("//tbody/tr/td/input[@name='id' and @type='hidden' and @value='" + loanID + "']");
                    assertEquals(0, afterPaymentLoanIDHiddenFields.size());
                }
            } catch (SQLException ex) {
                Logger.getLogger(WebApplicationTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(WebApplicationTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //TC20	User	pays	fine	after	renewal	expires
    @Test
    public void LatePayment_PayAfterRenewExpiresTest() {
        clickLinkWithExactText("Books Available");

        List<IElement> checkBoxElements = getElementsByXPath("//input[@type=\"checkbox\" and @name=\"selectedTitles\"]");
        if (checkBoxElements.size() > 0) {
            try {
                IElement firstCheckbox = checkBoxElements.get(0);

                checkCheckbox("selectedTitles", firstCheckbox.getAttribute("value"));
                submit();
                assertTitleEquals("Your borrowed books");

                String isbn = firstCheckbox.getAttribute("value").split("\\|")[0];
                assertTextPresent(isbn);
                assertTextNotPresent("ISBN:");

                clickLinkWithExactText("Back");
                clickLinkWithExactText("My Borrowed");
                assertTitleEquals("Your borrowed books");

                IElement isbnTD = getElementByXPath("//tbody/tr/td[text()='" + isbn + "']");
                String renewLinkID = isbnTD.getParent().getChildren().get(5).getChildren().get(0).getAttribute("id");
                String returnLinkID = isbnTD.getParent().getChildren().get(6).getChildren().get(0).getAttribute("id");
                String loanID = returnLinkID.split("_")[2];

                //Renew
                IElement dueTimeTD = isbnTD.getParent().getChildren().get(4);
                String dueTime = dueTimeTD.getTextContent();
                SimpleDateFormat ft = new SimpleDateFormat("kk:mm:ss");
                Date beforeRenewDueTime = ft.parse(dueTime, new ParsePosition(0));
                long t = beforeRenewDueTime.getTime();
                Date afterAddingTwoMins = new Date(t + (2 * ONE_MINUTE_IN_MILLIS));

                clickLink(renewLinkID);
                assertTextInElement(dueTimeTD.getAttribute("id"), ft.format(afterAddingTwoMins));

                //Change the due time is 1 second after start time
                String queryPlaceholder = "update loan\n"
                        + "set DueTime = StartTime\n"
                        + "where loan.`LoanID` = %s";

                DAO db;
                db = DAO.getInstance();
                String query = String.format(queryPlaceholder, loanID);
                int result = db.excuteQuery(query);

                //Wait 1 minute to let loan expire
                Thread.sleep(ONE_MINUTE_IN_MILLIS);

                clickLinkWithExactText("Back");
                clickLinkWithExactText("My Borrowed");
                assertTitleEquals("Your borrowed books");
                IElement expiredIsbnTD = getElementByXPath("//tbody/tr/td[text()='" + isbn + "']");
                IElement expiredRenewTD = expiredIsbnTD.getParent().getChildren().get(5);
                Assert.assertTrue(expiredRenewTD.getTextContent().trim().contains("This loan has expired"));

                //Return
                clickLink(returnLinkID);
                assertTextNotPresent(isbn);

                clickLinkWithExactText("Back");
                clickLinkWithExactText("Late Payment");
                assertTitleEquals("Late Payment");

                IElement loanIDHiddenField = getElementByXPath("//input[@name='id' and @type='hidden' and @value='" + loanID + "']");
                if (loanIDHiddenField != null) {
                    clickButton("makepay_loanId_" + loanID);
                    List<IElement> afterPaymentLoanIDHiddenFields = getElementsByXPath("//tbody/tr/td/input[@name='id' and @type='hidden' and @value='" + loanID + "']");
                    assertEquals(0, afterPaymentLoanIDHiddenFields.size());
                }
            } catch (SQLException ex) {
                Logger.getLogger(WebApplicationTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(WebApplicationTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //TC21	User	pays	fine	without	returning	book
    @Test
    public void LatePayment_PayingWithoutReturninBookTest() {
        clickLinkWithExactText("Books Available");

        List<IElement> checkBoxElements = getElementsByXPath("//input[@type=\"checkbox\" and @name=\"selectedTitles\"]");
        if (checkBoxElements.size() > 0) {
            try {
                IElement firstCheckbox = checkBoxElements.get(0);

                checkCheckbox("selectedTitles", firstCheckbox.getAttribute("value"));
                submit();
                assertTitleEquals("Your borrowed books");

                String isbn = firstCheckbox.getAttribute("value").split("\\|")[0];
                assertTextPresent(isbn);
                assertTextNotPresent("ISBN:");

                clickLinkWithExactText("Back");
                clickLinkWithExactText("My Borrowed");
                assertTitleEquals("Your borrowed books");

                IElement isbnTD = getElementByXPath("//tbody/tr/td[text()='" + isbn + "']");
                String returnLinkID = isbnTD.getParent().getChildren().get(6).getChildren().get(0).getAttribute("id");
                String loanID = returnLinkID.split("_")[2];

                //Change the duet time is 1 second after start time
                String queryPlaceholder = "update loan\n"
                        + "set DueTime = StartTime\n"
                        + "where loan.`LoanID` = %s";

                DAO db;
                db = DAO.getInstance();
                String query = String.format(queryPlaceholder, loanID);
                int result = db.excuteQuery(query);

                //Wait 1 second to let loan expire
                Thread.sleep(ONE_MINUTE_IN_MILLIS);

                clickLinkWithExactText("Back");
                clickLinkWithExactText("My Borrowed");
                assertTitleEquals("Your borrowed books");
                IElement expiredIsbnTD = getElementByXPath("//tbody/tr/td[text()='" + isbn + "']");
                IElement expiredRenewTD = expiredIsbnTD.getParent().getChildren().get(5);
                Assert.assertTrue(expiredRenewTD.getTextContent().trim().contains("This loan has expired"));

                //Return
                clickLink(returnLinkID);
                assertTextNotPresent(isbn);

                clickLinkWithExactText("Back");
                clickLinkWithExactText("Late Payment");
                assertTitleEquals("Late Payment");

                IElement loanIDHiddenField = getElementByXPath("//input[@name='id' and @type='hidden' and @value='" + loanID + "']");
                if (loanIDHiddenField != null) {
                    //Pay late fine
                    clickButton("makepay_loanId_" + loanID);
                    List<IElement> afterPaymentLoanIDHiddenFields = getElementsByXPath("//tbody/tr/td/input[@name='id' and @type='hidden' and @value='" + loanID + "']");
                    assertEquals(0, afterPaymentLoanIDHiddenFields.size());
                }

                clickLinkWithExactText("Back");
                clickLinkWithExactText("Books Available");

                //Borrow after paying fine.
                checkCheckbox("selectedTitles", firstCheckbox.getAttribute("value"));
                submit();
                assertTitleEquals("Your borrowed books");
                assertTextPresent(isbn);
                assertTextNotPresent("ISBN:");
            } catch (SQLException ex) {
                Logger.getLogger(WebApplicationTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(WebApplicationTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //TC22	User	borrows	book	after	paying	fine
    @Test
    public void LatePayment_BorrowBookAfterPayingTest() {
        clickLinkWithExactText("Books Available");

        List<IElement> checkBoxElements = getElementsByXPath("//input[@type=\"checkbox\" and @name=\"selectedTitles\"]");
        if (checkBoxElements.size() > 0) {
            try {
                IElement firstCheckbox = checkBoxElements.get(0);

                checkCheckbox("selectedTitles", firstCheckbox.getAttribute("value"));
                submit();
                assertTitleEquals("Your borrowed books");

                String isbn = firstCheckbox.getAttribute("value").split("\\|")[0];
                assertTextPresent(isbn);
                assertTextNotPresent("ISBN:");

                clickLinkWithExactText("Back");
                clickLinkWithExactText("My Borrowed");
                assertTitleEquals("Your borrowed books");

                IElement isbnTD = getElementByXPath("//tbody/tr/td[text()='" + isbn + "']");
                String returnLinkID = isbnTD.getParent().getChildren().get(6).getChildren().get(0).getAttribute("id");
                String loanID = returnLinkID.split("_")[2];

                //Change the duet time is 1 second after start time
                String queryPlaceholder = "update loan\n"
                        + "set DueTime = StartTime\n"
                        + "where loan.`LoanID` = %s";

                DAO db;
                db = DAO.getInstance();
                String query = String.format(queryPlaceholder, loanID);
                int result = db.excuteQuery(query);

                //Wait 1 second to let loan expire
                Thread.sleep(ONE_MINUTE_IN_MILLIS);

                clickLinkWithExactText("Back");
                clickLinkWithExactText("My Borrowed");
                assertTitleEquals("Your borrowed books");
                IElement expiredIsbnTD = getElementByXPath("//tbody/tr/td[text()='" + isbn + "']");
                IElement expiredRenewTD = expiredIsbnTD.getParent().getChildren().get(5);
                Assert.assertTrue(expiredRenewTD.getTextContent().trim().contains("This loan has expired"));

                //Return
                clickLink(returnLinkID);
                assertTextNotPresent(isbn);

                clickLinkWithExactText("Back");
                clickLinkWithExactText("Late Payment");
                assertTitleEquals("Late Payment");

                IElement loanIDHiddenField = getElementByXPath("//input[@name='id' and @type='hidden' and @value='" + loanID + "']");
                if (loanIDHiddenField != null) {
                    //Pay late fine
                    clickButton("makepay_loanId_" + loanID);
                    List<IElement> afterPaymentLoanIDHiddenFields = getElementsByXPath("//tbody/tr/td/input[@name='id' and @type='hidden' and @value='" + loanID + "']");
                    assertEquals(0, afterPaymentLoanIDHiddenFields.size());
                }

                clickLinkWithExactText("Back");
                clickLinkWithExactText("Books Available");

                //Borrow after paying fine.
                checkCheckbox("selectedTitles", firstCheckbox.getAttribute("value"));
                submit();
                assertTitleEquals("Your borrowed books");
                assertTextPresent(isbn);
                assertTextNotPresent("ISBN:");
            } catch (SQLException ex) {
                Logger.getLogger(WebApplicationTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(WebApplicationTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //TC23	Remove	Item	 	 	 	 
    //TC23a	before	Remove Item,	borrow	and	return	1
    @Test
    public void RemoveItem_BorrowAndReturnTest() {
        clickLinkWithExactText("Books Available");

        List<IElement> trElements = getElementsByXPath("//tbody/tr");
        String selectedBookCheckBoxValue = "";
        String isbn = "";
        for (IElement tr : trElements) {
            if (Integer.parseInt(tr.getChildren().get(4).getTextContent()) == 1) {
                selectedBookCheckBoxValue = tr.getChildren().get(0).getChildren().get(0).getAttribute("value");
                break;
            }
        }
        if (!selectedBookCheckBoxValue.isEmpty()) {
            isbn = selectedBookCheckBoxValue.split("\\|")[0];
            checkCheckbox("selectedTitles", selectedBookCheckBoxValue);
            submit();
            assertTitleEquals("Your borrowed books");

            assertTextPresent(isbn);

            IElement borrowedIsbnTD = getElementByXPath("//tbody/tr/td[text()='" + isbn + "']");
            IElement returnLink = borrowedIsbnTD.getParent().getChildren().get(6).getChildren().get(0);
            clickLink(returnLink.getAttribute("id"));
            //User 1 returns the copy.
            assertTextNotPresent(isbn);

            //Admin login
            WebTester testerAdmin = new WebTester();
            testerAdmin.setBaseUrl("http://localhost:8084/SimpleLibrary");
            testerAdmin.beginAt("index.jsp");
            testerAdmin.assertTitleEquals("Welcome Page");
            testerAdmin.setTextField("uid", "2");
            testerAdmin.setTextField("pwd", "123456");
            testerAdmin.submit();
            testerAdmin.assertTitleEquals("User Main");
            testerAdmin.clickLinkWithExactText("Inventory Management");

            //Admin find the item to remove
            testerAdmin.clickButton("remove_item_" + isbn);
            //Admin check the number of item to see if it equals 0
            IElement afterRemoveIsbnTD = testerAdmin.getElementByXPath("//td[text()='" + isbn + "']");
            if (afterRemoveIsbnTD != null) {
                assertEquals(0, Integer.parseInt(afterRemoveIsbnTD.getParent().getChildren().get(3).getTextContent()));
            }
        }
    }

    //TC23a	before	Remove Item,	borrow	but	do	not	return	1	
    @Test
    public void RemoveItem_BorrowAndNotReturnTest() {
        clickLinkWithExactText("Books Available");

        List<IElement> trElements = getElementsByXPath("//tbody/tr");
        String selectedBookCheckBoxValue = "";
        String isbn = "";
        for (IElement tr : trElements) {
            if (Integer.parseInt(tr.getChildren().get(4).getTextContent()) == 1) {
                selectedBookCheckBoxValue = tr.getChildren().get(0).getChildren().get(0).getAttribute("value");
                break;
            }
        }
        if (!selectedBookCheckBoxValue.isEmpty()) {
            isbn = selectedBookCheckBoxValue.split("\\|")[0];
            checkCheckbox("selectedTitles", selectedBookCheckBoxValue);
            submit();
            assertTitleEquals("Your borrowed books");

            assertTextPresent(isbn);

            //Admin login
            WebTester testerAdmin = new WebTester();
            testerAdmin.setBaseUrl("http://localhost:8084/SimpleLibrary");
            testerAdmin.beginAt("index.jsp");
            testerAdmin.assertTitleEquals("Welcome Page");
            testerAdmin.setTextField("uid", "2");
            testerAdmin.setTextField("pwd", "123456");
            testerAdmin.submit();
            testerAdmin.assertTitleEquals("User Main");
            testerAdmin.clickLinkWithExactText("Inventory Management");

            //Admin find the item to remove
            testerAdmin.clickButton("remove_item_" + isbn);
            testerAdmin.assertTextPresent("All this kind of item are currently in loan and not be returned or there is no item available!");
        }
    }

    //TC24	Remove	Title	
    @Test
    public void RemoveTitleTest() {
        //Admin login
        WebTester testerAdmin = new WebTester();
        testerAdmin.setBaseUrl("http://localhost:8084/SimpleLibrary");
        testerAdmin.beginAt("index.jsp");
        testerAdmin.assertTitleEquals("Welcome Page");
        testerAdmin.setTextField("uid", "2");
        testerAdmin.setTextField("pwd", "123456");
        testerAdmin.submit();
        testerAdmin.assertTitleEquals("User Main");
        testerAdmin.clickLinkWithExactText("Inventory Management");

        List<IElement> trElements = testerAdmin.getElementsByXPath("//tbody/tr");
        String isbn = "";
        for (IElement tr : trElements) {
            if (Integer.parseInt(tr.getChildren().get(3).getTextContent()) == 1) {
                isbn = tr.getChildren().get(2).getTextContent();
                break;
            }
        }
        //Admin find the item to remove
        testerAdmin.clickButton("remove_title_" + isbn);
        testerAdmin.assertTextNotPresent(isbn);
    }

    //TC25	Remove	Title	with	Multiple	Copies	 
    //T25a      before	remove title,	borrow	and	return	2	copies
    @Test
    public void RemoveTitle_BorrowAndReturnTwoCopiesTest() {
        clickLinkWithExactText("Books Available");

        List<IElement> trElements = getElementsByXPath("//tbody/tr");
        String selectedBookCheckBoxValue = "";
        String isbn = "";
        for (IElement tr : trElements) {
            if (Integer.parseInt(tr.getChildren().get(4).getTextContent()) == 2) {
                selectedBookCheckBoxValue = tr.getChildren().get(0).getChildren().get(0).getAttribute("value");
                break;
            }
        }
        if (!selectedBookCheckBoxValue.isEmpty()) {
            isbn = selectedBookCheckBoxValue.split("\\|")[0];
            checkCheckbox("selectedTitles", selectedBookCheckBoxValue);
            submit();
            assertTitleEquals("Your borrowed books");
            //User borrow the first copy
            assertTextPresent(isbn);

            clickLinkWithExactText("Back");
            clickLinkWithExactText("Books Available");
            checkCheckbox("selectedTitles", selectedBookCheckBoxValue);
            submit();
            assertTitleEquals("Your borrowed books");
            //User borrow the second copy.
            List<IElement> borrowedIsbnTDs = getElementsByXPath("//tbody/tr/td[text()='" + isbn + "']");
            assertEquals(2, borrowedIsbnTDs.size());

            for (IElement td : borrowedIsbnTDs) {
                IElement returnLink = td.getParent().getChildren().get(6).getChildren().get(0);
                clickLink(returnLink.getAttribute("id"));
            }
            //User returns 2 copies.
            assertTextNotPresent(isbn);

            //Admin login
            WebTester testerAdmin = new WebTester();
            testerAdmin.setBaseUrl("http://localhost:8084/SimpleLibrary");
            testerAdmin.beginAt("index.jsp");
            testerAdmin.assertTitleEquals("Welcome Page");
            testerAdmin.setTextField("uid", "2");
            testerAdmin.setTextField("pwd", "123456");
            testerAdmin.submit();
            testerAdmin.assertTitleEquals("User Main");
            testerAdmin.clickLinkWithExactText("Inventory Management");

            //Admin find the title to remove
            testerAdmin.clickButton("remove_title_" + isbn);
            assertTextNotPresent(isbn);
        }
    }

    //T25b      before	remove title,	borrow	2	copies	but	do	not	return	1	
    @Test
    public void RemoveTitle_BorrowTwoCopiesButDoNotReturnOneTest() {
        clickLinkWithExactText("Books Available");

        List<IElement> trElements = getElementsByXPath("//tbody/tr");
        String selectedBookCheckBoxValue = "";
        String isbn = "";
        for (IElement tr : trElements) {
            if (Integer.parseInt(tr.getChildren().get(4).getTextContent()) == 2) {
                selectedBookCheckBoxValue = tr.getChildren().get(0).getChildren().get(0).getAttribute("value");
                break;
            }
        }
        if (!selectedBookCheckBoxValue.isEmpty()) {
            isbn = selectedBookCheckBoxValue.split("\\|")[0];
            checkCheckbox("selectedTitles", selectedBookCheckBoxValue);
            submit();
            assertTitleEquals("Your borrowed books");
            //User borrow the first copy
            assertTextPresent(isbn);

            clickLinkWithExactText("Back");
            clickLinkWithExactText("Books Available");
            checkCheckbox("selectedTitles", selectedBookCheckBoxValue);
            submit();
            assertTitleEquals("Your borrowed books");
            //User borrow the second copy.
            List<IElement> borrowedIsbnTDs = getElementsByXPath("//tbody/tr/td[text()='" + isbn + "']");
            assertEquals(2, borrowedIsbnTDs.size());

            for (IElement td : borrowedIsbnTDs) {
                IElement returnLink = td.getParent().getChildren().get(6).getChildren().get(0);
                clickLink(returnLink.getAttribute("id"));
                break;
            }
            //User returns ONLY 1 copy.
            List<IElement> afterReturnBorrowedIsbnTDs = getElementsByXPath("//tbody/tr/td[text()='" + isbn + "']");
            assertEquals(1, afterReturnBorrowedIsbnTDs.size());

            //Admin login
            WebTester testerAdmin = new WebTester();
            testerAdmin.setBaseUrl("http://localhost:8084/SimpleLibrary");
            testerAdmin.beginAt("index.jsp");
            testerAdmin.assertTitleEquals("Welcome Page");
            testerAdmin.setTextField("uid", "2");
            testerAdmin.setTextField("pwd", "123456");
            testerAdmin.submit();
            testerAdmin.assertTitleEquals("User Main");
            testerAdmin.clickLinkWithExactText("Inventory Management");

            //Admin find the title to remove
            testerAdmin.clickButton("remove_title_" + isbn);
            testerAdmin.assertTextPresent("Cannot remove the title since some items under this title may currently in loan.");
        }
    }

    @After
    public void tearDown() {
        sequential.unlock();
    }
}