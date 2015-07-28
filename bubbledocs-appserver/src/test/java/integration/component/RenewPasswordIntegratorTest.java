package integration.component;

import static org.junit.Assert.*;
import mockit.Mocked;
import mockit.StrictExpectations;

import org.junit.Test;

import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.RenewPassword;
import pt.tecnico.bubbledocs.service.integration.RenewPasswordIntegrator;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RenewPasswordIntegratorTest extends BubbleDocsServiceTest {


	private static final String NAME = "AliceName";
	private static final String USERNAME = "Alice";
	private static final String USERNAME_DOES_NOT_EXIST = " ";
	private static final String EMAIL ="alice@tenico.pt";

	private String user;

	@Override
	public void populate4Test() {
		createUser(USERNAME,EMAIL,NAME);
		user = addUserToSession(USERNAME);
	}

	@Test
	public void success(@Mocked final IDRemoteServices service) 
			throws Exception{

		new StrictExpectations(){{
			new RenewPassword(user);
			new IDRemoteServices();
			service.renewPassword(USERNAME);
		}};

		RenewPasswordIntegrator remoteClient = new RenewPasswordIntegrator(this.user);

		remoteClient.execute();

		assertNull(getUserFromUsername(USERNAME).getPassword());


	}


	@Test(expected = UserNotInSessionException.class)
	public void notInSession(@Mocked final IDRemoteServices service) 
			throws Exception {

		new StrictExpectations(){{
			new RenewPassword(USERNAME_DOES_NOT_EXIST);
			new IDRemoteServices();
		}};


		// Unit under test is now exercised
		RenewPasswordIntegrator remoteClient = new RenewPasswordIntegrator(USERNAME_DOES_NOT_EXIST);

		remoteClient.execute();

	}


	@Test(expected = UnavailableServiceException.class)
	public void serviceUnavailable(
			@Mocked final IDRemoteServices service) 
					throws Exception {

		new StrictExpectations() {{

			new RenewPassword(user);
			new IDRemoteServices();
			service.renewPassword(USERNAME); 
			result = new RemoteInvocationException();
		}};

		// Unit under test is now exercised
		RenewPasswordIntegrator remoteClient = new RenewPasswordIntegrator(this.user);

		remoteClient.execute();

	}




}
