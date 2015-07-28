	package pt.tecnico.bubbledocs;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.Config;
import pt.tecnico.bubbledocs.domain.*;
import pt.tecnico.bubbledocs.exception.InvalidCellLocation;
import pt.tecnico.bubbledocs.exception.UserAlreadyExistsException;
import pt.tecnico.bubbledocs.service.AssignLiteralCell;
import pt.tecnico.bubbledocs.service.AssignReferenceCell;
import pt.tecnico.bubbledocs.service.CreateSpreadSheet;
import pt.tecnico.bubbledocs.service.CreateUser;
import pt.tecnico.bubbledocs.service.LoginUser;

public class SetupDomain {

	@Atomic
	public static void main(String[] args) {
		populateDomain();
	}

	@Atomic
	public static void populateDomain() {
		
		BubbleDocs bd = BubbleDocs.getInstance();
		
		bd.setSession(new Session());
		userRoot root = new userRoot("Super User", "root", "rootroot");
    	bd.setRootUser(root);
    	bd.addUser(root);
    	//bd.setSpreadCount(0);
		
		
	}
}