package integration.component;

import static org.junit.Assert.*;
import mockit.*;

import org.junit.Test;

import pt.tecnico.bubbledocs.exception.DuplicateEmailException;
import pt.tecnico.bubbledocs.exception.EmptyUsernameException;
import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UserAlreadyExistsException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.exception.InvalidEmailException;
import pt.tecnico.bubbledocs.service.CreateUser;
import pt.tecnico.bubbledocs.service.integration.CreateUserIntegrator;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;


// add needed import declarations

public class CreateUserIntegratorTest extends BubbleDocsServiceTest {



	// the tokens
	private String root;
	private String alice;

	private static final String NAME = "AliceName";
	private static final String USERNAME = "Alice";
	private static final String USERNAME2 = "Artur";
	private static final String MAIOR8 = "QueGrandeNome";
	private static final String MENOR3 = "eu";
	private static final String USERNAME_DOES_NOT_EXIST = " ";
	private static final String EMAIL ="alice@tenico.pt";
	private static final String NOTEMAIL = "alice#tecnico";


	@Override
	public void populate4Test() {
		createUser(USERNAME,EMAIL, "AliceName");
		setPasswordForUser(USERNAME, "password");
		root = addUserToSession("root");
		alice = addUserToSession("Alice");
	}


	@Test
	public void success(@Mocked final IDRemoteServices service) 
			throws Exception{

		new StrictExpectations() {{
			
				new CreateUser(root, USERNAME2, EMAIL, NAME);
				new IDRemoteServices();
				service.createUser(USERNAME2, EMAIL); 
				
			}};
	
		CreateUserIntegrator remoteClient = new CreateUserIntegrator(root,USERNAME2, EMAIL, NAME);

		remoteClient.execute();

		assertEquals(USERNAME2, getUserFromUsername(USERNAME2).getUsername());
		assertEquals(NAME, getUserFromUsername(USERNAME2).getName());
		assertEquals(EMAIL, getUserFromUsername(USERNAME2).getEmail());


	}

	@Test(expected = UserAlreadyExistsException.class)
	public void usernameExists() 
					throws Exception {

		{
			new StrictExpectations() {{

				new CreateUser(root,USERNAME, EMAIL, NAME);

			}};

		
			// Unit under test is now exercised
			CreateUserIntegrator remoteClient = new CreateUserIntegrator(root,USERNAME, EMAIL, NAME);

			remoteClient.execute();

		}
	}

	@Test(expected = InvalidArgumentsException.class)
	public void smallerUsername()
					throws Exception {

		new StrictExpectations() {{
			
			
			new CreateUser(root, MENOR3, EMAIL, NAME);
		}};

		
		
		
		// Unit under test is now exercised
		CreateUserIntegrator remoteClient = new CreateUserIntegrator(root, MENOR3, EMAIL, NAME);

		remoteClient.execute();

	}

	
	@Test(expected = InvalidArgumentsException.class)
	public void biggerUsername()
					throws Exception {

		new StrictExpectations() {{

			new CreateUser(root, MAIOR8, EMAIL, NAME);
		}};
		


		// Unit under test is now exercised
		CreateUserIntegrator remoteClient = new CreateUserIntegrator(root, MAIOR8, EMAIL, NAME);

		remoteClient.execute();

	}
	
	@Test(expected = EmptyUsernameException.class)
	public void emptyUsername()
					throws Exception {

		new StrictExpectations() {{
			
			new CreateUser(root, USERNAME_DOES_NOT_EXIST, EMAIL, NAME);
		}};

		

		// Unit under test is now exercised
		CreateUserIntegrator remoteClient = new CreateUserIntegrator(root, USERNAME_DOES_NOT_EXIST, EMAIL, NAME);

		remoteClient.execute();

	}

	@Test(expected = UnauthorizedOperationException.class)
	public void unauthorizedUserCreation() throws Exception {

		
		CreateUser remoteClient = new CreateUser(this.alice,USERNAME, EMAIL, NAME);

		remoteClient.execute();

	}

	@Test(expected = UserNotInSessionException.class)
	public void accessUsernameNotExist() 
			throws Exception {


		CreateUser remoteClient = new CreateUser("arturius" ,USERNAME2, EMAIL, NAME);

		remoteClient.execute();

	}


	@Test(expected = InvalidEmailException.class)
	public void invalidEmail(
			@Mocked final IDRemoteServices remoteService)
					throws Exception {

		new StrictExpectations() {{
			
			new CreateUser(root, USERNAME2, NOTEMAIL, NAME); 
			new IDRemoteServices();
			
			remoteService.createUser(USERNAME2, NOTEMAIL); 

			result = new InvalidEmailException();
		}};

		
		// Unit under test is now exercised
		CreateUserIntegrator remoteClient = new CreateUserIntegrator(root, USERNAME2, NOTEMAIL, NAME);

		remoteClient.execute();

	}

	@Test(expected = DuplicateEmailException.class)
	public void duplicateEmail(
			@Mocked final IDRemoteServices remoteService)
					throws Exception {

		new StrictExpectations() {{
			
			new CreateUser(root, USERNAME2, EMAIL, NAME);
			new IDRemoteServices();
			
			remoteService.createUser(USERNAME2, EMAIL); 

			result = new DuplicateEmailException();
		}};

		
		// Unit under test is now exercised
		CreateUserIntegrator remoteClient = new CreateUserIntegrator(root, USERNAME2, EMAIL,NAME);

		remoteClient.execute();

	}


	@Test(expected = UnavailableServiceException.class)
	public void unavailableService(
			@Mocked final IDRemoteServices remoteService)
					throws Exception {

		new Expectations() {{

			new CreateUser(root, USERNAME2, EMAIL, NAME);
			new IDRemoteServices();
			
			
			remoteService.createUser(USERNAME2, EMAIL); 

			result = new RemoteInvocationException();
		}};
		
		// Unit under test is now exercised
		CreateUserIntegrator remoteClient = new CreateUserIntegrator(root, USERNAME2, EMAIL, NAME);
		
		remoteClient.execute();
		assertNull(getUserFromUsername(USERNAME2));
	}

}