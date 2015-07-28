package integration.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import mockit.*;

import org.junit.Test;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.integration.LoginUserIntegrator;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

// add needed import declarations

public class LoginUserIntegratorTest extends BubbleDocsServiceTest {

    private static final String USERNAME = "jpp";
    private static final String NOBODY = "eusounada";
    private static final String PASSWORD = "jp#";
    private static final String WRONGPASSWORD = "jp!!!";
    private static final String EMAIL = "email@host.com";
    

    @Override
    public void populate4Test() {
        createUser(USERNAME, EMAIL, "João Pereira");
        setPasswordForUser(USERNAME,PASSWORD);
    }

    // returns the time of the last access for the user with token userToken.
    // It must get this data from the session object of the application
    private DateTime getLastAccessTimeInSession(String userToken) {
    	User u = getUserFromSession(userToken);
    	return u.getLtoken().getLogindate();
    }

    @Test
    public void success(@Mocked IDRemoteServices service) 
  		  throws Exception{
    	
    	new StrictExpectations() {{
			
    		new IDRemoteServices();	
    		service.loginUser(USERNAME, PASSWORD);
    	    		
    	}};

    	LoginUserIntegrator remoteClient = new LoginUserIntegrator(USERNAME,PASSWORD);

    	remoteClient.execute();
    	
    	DateTime currentTime = new DateTime();
    	String token = remoteClient.getUserToken();

        User user = getUserFromSession(remoteClient.getUserToken());
        assertEquals(USERNAME, user.getUsername());

	int difference = Seconds.secondsBetween(getLastAccessTimeInSession(token), currentTime).getSeconds();

	assertTrue("Access time in session not correctly set", difference >= 0);
	assertTrue("diference in seconds greater than expected", difference < 2);
    }
    
    @Test
    public void successLoginTwice(@Mocked IDRemoteServices service) throws Exception{
    	
    		new StrictExpectations() {{
    		new IDRemoteServices();	
    		service.loginUser(USERNAME, PASSWORD);
    		new IDRemoteServices();
    		service.loginUser(USERNAME, PASSWORD); 
    	    		
    	}};
    	
        LoginUserIntegrator remoteClient = new LoginUserIntegrator(USERNAME, PASSWORD);

        remoteClient.execute();
        String token1 = remoteClient.getUserToken();

        remoteClient.execute();
        String token2 = remoteClient.getUserToken();

        User user = getUserFromSession(token1);
        assertNull(user);
      
        user = getUserFromSession(token2);
        assertEquals(USERNAME, user.getUsername());
    }


    
    @Test(expected = LoginBubbleDocsException.class)
    public void unknownUser(@Mocked IDRemoteServices service	) throws Exception {
    	
    	{
    		new StrictExpectations() {{

        		new IDRemoteServices();
        		
    	}};
    	
    	// Unit under test is now exercised
    	LoginUserIntegrator remoteClient = new LoginUserIntegrator(NOBODY,PASSWORD);

		remoteClient.execute();
    	
    	}
    }
    
    @Test(expected = LoginBubbleDocsException.class)
    public void wrongPassword(@Mocked IDRemoteServices service	) throws Exception {
    	
    	{
    		new StrictExpectations() {{
    			
    		new IDRemoteServices();
    		service.loginUser(USERNAME, WRONGPASSWORD); 
    	    		
    		result = new LoginBubbleDocsException();
    	}};
    	
    	// Unit under test is now exercised
    	LoginUserIntegrator remoteClient = new LoginUserIntegrator(USERNAME,WRONGPASSWORD);

		remoteClient.execute();
    	
    	}
    }
    
    @Test(expected = UnavailableServiceException.class)
    public void unavailableService(
    		@Mocked final IDRemoteServices service)
    			throws Exception {
    	
    	new StrictExpectations() {{
    		
    		new IDRemoteServices();
    			    			
    		service.loginUser(USERNAME, PASSWORD); 
    			
    		result = new UnavailableServiceException();
    	}};
    	
    	// Unit under test is now exercised
    	LoginUserIntegrator remoteClient = new LoginUserIntegrator(USERNAME,PASSWORD);

    	remoteClient.execute();

    	}
   }
