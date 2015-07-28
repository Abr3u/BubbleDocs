package integration.component;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.junit.After;
import org.junit.Before;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.core.WriteOnReadError;
import pt.tecnico.bubbledocs.domain.*;

// add needed import declarations

public class BubbleDocsServiceTest {

    @Before
    public void setUp() throws Exception {

        try {
            FenixFramework.getTransactionManager().begin(false);
			BubbleDocs.start();
            populate4Test();
        } catch (WriteOnReadError | NotSupportedException | SystemException e1) {
            e1.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        try {
            FenixFramework.getTransactionManager().rollback();
        } catch (IllegalStateException | SecurityException | SystemException e) {
            e.printStackTrace();
        }
    }

    // should redefine this method in the subclasses if it is needed to specify
    // some initial state
    public void populate4Test() {
    	
    }
    
    // auxiliary methods that access the domain layer and are needed in the test classes
    // for defining the inital state and checking that the service has the expected behavior
    
    User createRootUser(String username, String email, String name) {
    	BubbleDocs bd = BubbleDocs.getInstance();
    	userRoot newUser = new userRoot(name,username,email);
    	bd.addUser(newUser);
    	bd.setRootUser(newUser);
    	return newUser;
    }
    
    User createUser(String username, String email, String name) {
    	BubbleDocs bd = BubbleDocs.getInstance();
    	User newUser = new User(name,username,email);
    	bd.addUser(newUser);
    	return newUser;
    }
    
    public void setPasswordForUser(String username, String password){
    	BubbleDocs bd = BubbleDocs.getInstance();
    	User u = bd.getUserByUsername(username);
    	u.setPassword(password);	
    }

    public Spreadsheet createSpreadSheet(User user, String name, int row,int column) {
    	
    	Spreadsheet newSpread = new Spreadsheet(user, name, row, column, BubbleDocs.getInstance().incSpreadCount());
    	BubbleDocs.getInstance().addSpreadsheets(newSpread);
    	return newSpread;
    }

    // returns a spreadsheet whose name is equal to name
    public Spreadsheet getSpreadSheet(String name) {
    	return BubbleDocs.getInstance().searchSpreadByName(name);
    	
    }

    // returns the user registered in the application whose username is equal to username
    User getUserFromUsername(String username) {
	 return BubbleDocs.getInstance().getUserByUsername(username);
    }

    // put a user into session and returns the token associated to it
    String addUserToSession(String username){
    	return BubbleDocs.getInstance().getSession().addUsertoSession(username);
    }

    // remove a user from session given its token
    void removeUserFromSession(String token) {
    	BubbleDocs.getInstance().getSession().removeUserFromSession(token);
    }

    // return the user registered in session whose token is equal to token
    User getUserFromSession(String token) {
    	return BubbleDocs.getInstance().getSession().getUserFromSession(token);
    }

}