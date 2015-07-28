package integration.system;

import org.junit.After;
import org.junit.Before;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.domain.*;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;

// add needed import declarations

public class BubbleDocsIT {

	@Before
	public void setUp() throws Exception {
		FenixFramework.getTransactionManager().begin(false);
		BubbleDocs.deleteAllUsers();
		BubbleDocs.start();
		populate4Test();
	}

	@After
	public void tearDown() throws Exception {
		BubbleDocs.deleteAllUsers(); 
		IDRemoteServices id = new IDRemoteServices();
		StoreRemoteServices st = new StoreRemoteServices();
		id.restart();
		st.restart();
	}

	// should redefine this method in the subclasses if it is needed to specify
	// some initial state
	public void populate4Test(){}

	String addUserToSession(String username){
		return BubbleDocs.getInstance().getSession().addUsertoSession(username);
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

	public Spreadsheet getSpreadSheet(String name) {
		return BubbleDocs.getInstance().searchSpreadByName(name);

	}
	public Spreadsheet createSpreadSheet(User user, String name, int row,int column) {

		Spreadsheet newSpread = new Spreadsheet(user, name, row, column, BubbleDocs.getInstance().incSpreadCount());
		BubbleDocs.getInstance().addSpreadsheets(newSpread);
		return newSpread;
	}
}