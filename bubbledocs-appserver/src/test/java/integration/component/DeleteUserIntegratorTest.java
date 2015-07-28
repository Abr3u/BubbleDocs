package integration.component;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import mockit.Mocked;
import mockit.StrictExpectations;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.DeleteUser;
import pt.tecnico.bubbledocs.service.integration.DeleteUserIntegrator;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

// add needed import declarations

public class DeleteUserIntegratorTest extends BubbleDocsServiceTest {

    private static final String USERNAME_TO_DELETE = "smf";
    private static final String USERNAME = "Allice";
    private static final String NAME = "Alice";
    private static final String ROOT_USERNAME = "root";
    private static final String USERNAME_DOES_NOT_EXIST = "no-one";
    private static final String SPREADSHEET_NAME = "spread";
    private static final String EMAIL ="Alice@tecnico.pt";
    // the tokens for user root
    private String root;
    private String testuser;

    @Override
    public void populate4Test() {
        createUser(USERNAME, EMAIL, NAME);
      
        User smf = createUser(USERNAME_TO_DELETE, EMAIL, NAME);
        createSpreadSheet(smf, USERNAME_TO_DELETE, 20, 20);

        root = addUserToSession(ROOT_USERNAME);
        testuser = addUserToSession(USERNAME);
    };

    @Test
    public void success(@Mocked final IDRemoteServices service) throws Exception{
    	
    	new StrictExpectations() {{

    		new DeleteUser(root,USERNAME_TO_DELETE);
			new IDRemoteServices();
			service.removeUser(USERNAME_TO_DELETE);
    	}};
		
        DeleteUserIntegrator remoteClient = new DeleteUserIntegrator(root, USERNAME_TO_DELETE);
       
        remoteClient.execute();

        boolean deleted = getUserFromUsername(USERNAME_TO_DELETE) == null;

        assertTrue("user was not deleted", deleted);

        assertNull("Spreadsheet was not deleted",
                getSpreadSheet(SPREADSHEET_NAME));
    }

    /*
     * accessUsername exists, is in session and is root, toDeleteUsername exists
     * and is not in session
     */
    
    @Test
    public void successToDeleteIsNotInSession(@Mocked final IDRemoteServices service) throws Exception {
    	success(service);
    }

    /*
     * accessUsername exists, is in session and is root toDeleteUsername exists
     * and is in session Test if user and session are both deleted
     */
    
    
    @Test
    public void successToDeleteIsInSession(@Mocked final IDRemoteServices service)  throws Exception{
        String token = addUserToSession(USERNAME_TO_DELETE);
        success(service);
        assertNull("Removed user but not removed from session", getUserFromSession(token));
    }

    @Test(expected = LoginBubbleDocsException.class)
    public void userToDeleteDoesNotExist(
    		@Mocked final IDRemoteServices service) 
    				throws Exception {
    	
    	new StrictExpectations() {{

			new IDRemoteServices();
			
		}};
		
        new DeleteUserIntegrator(root, USERNAME_DOES_NOT_EXIST).execute();
    }

    @Test(expected = UnauthorizedOperationException.class)
    public void notRootUser() throws Exception {
    	
    	new StrictExpectations() {{

			new IDRemoteServices();
			
		}};

    	new DeleteUserIntegrator(testuser, USERNAME).execute();

    }

    @Test(expected = UserNotInSessionException.class)
    public void rootNotInSession() throws Exception {
    	
    	new StrictExpectations() {{

			new IDRemoteServices();
			
		}};
       
    	removeUserFromSession(root);

        new DeleteUserIntegrator(root, USERNAME_TO_DELETE).execute();
    }

    @Test(expected = UserNotInSessionException.class)
    public void notInSessionAndNotRoot() throws Exception{
       
    	new StrictExpectations() {{

			new IDRemoteServices();
			
		}};
    	
        removeUserFromSession(testuser);

        new DeleteUserIntegrator(testuser, USERNAME_TO_DELETE).execute();
    }

    @Test(expected = UserNotInSessionException.class)
    public void accessUserDoesNotExist() throws Exception {
    	
    	new StrictExpectations() {{

			new IDRemoteServices();
			
		}};
    
        new DeleteUserIntegrator(USERNAME_DOES_NOT_EXIST, USERNAME_TO_DELETE).execute();
    }
    
    
    @Test(expected = UnavailableServiceException.class)
    public void unavailableService(
    		@Mocked final IDRemoteServices service)
    			throws Exception {
    	
    	new StrictExpectations() {{

    		new DeleteUserIntegrator(ROOT_USERNAME,USERNAME);
    		
			new IDRemoteServices();
    			    			
    		service.removeUser(USERNAME); 
    			
    		result = new RemoteInvocationException();
    	}};
    	
    	// Unit under test is now exercised
    	DeleteUserIntegrator remoteClient = new DeleteUserIntegrator(ROOT_USERNAME,USERNAME);

    	remoteClient.execute();

    	}
    
}
