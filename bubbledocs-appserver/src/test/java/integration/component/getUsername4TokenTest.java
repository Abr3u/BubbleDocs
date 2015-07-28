package integration.component;

import static org.junit.Assert.assertEquals;
import mockit.*;

import org.junit.Test;

import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.GetUsername4TokenService;


// add needed import declarations

public class getUsername4TokenTest extends BubbleDocsServiceTest {

    private static final String USERNAME = "jpp";
    private static final String NOBODY = "eusounada";
    private static final String NAME = "nomeJoao";
    private static final String EMPTY = "";

    private static final String EMAIL = "email@host.com";
     
    private String token;
    

    @Override
    public void populate4Test() {
        createUser(USERNAME, EMAIL, NAME);
        token = addUserToSession(USERNAME);
    }

    @Test
    public void success() 
  		  throws Exception{
    	
    	new StrictExpectations() {{
			
    		new GetUsername4TokenService(USERNAME);	
    	    		
    	}};

    	GetUsername4TokenService client = new GetUsername4TokenService(token);

    	client.execute();
    	assertEquals(USERNAME, client.getUsername());

    
    }
    
    @Test(expected = UserNotInSessionException.class)
    public void invalidToken(){
    	
    		new StrictExpectations() {{
        		new GetUsername4TokenService(NOBODY);	
        		

    	}};
    	
    	GetUsername4TokenService client = new GetUsername4TokenService(NOBODY);

    	client.execute();
    }
    
    @Test(expected = UserNotInSessionException.class)
    public void emptyToken(){
    	
    		new StrictExpectations() {{
        		new GetUsername4TokenService(EMPTY);	
        		

    	}};
    	
    	GetUsername4TokenService client = new GetUsername4TokenService(EMPTY);

    	client.execute();
    }

}