package integration.component;

import static org.junit.Assert.assertEquals;
import mockit.*;

import org.junit.Test;

import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.service.GetUserInfoService;


// add needed import declarations

public class getUserInfoTest extends BubbleDocsServiceTest {

    private static final String USERNAME = "jpp";
    private static final String NOBODY = "eusounada";
    private static final String NAME = "nomeJoao";
    private static final String EMPTY = "";

    private static final String EMAIL = "email@host.com";
    

    @Override
    public void populate4Test() {
        createUser(USERNAME, EMAIL, NAME);
    }

    @Test
    public void success() 
  		  throws Exception{
    	
    	new StrictExpectations() {{
			
    		new GetUserInfoService(USERNAME);	
    	    		
    	}};

    	GetUserInfoService client = new GetUserInfoService(USERNAME);

    	client.execute();
    	assertEquals(USERNAME, client.get_username());
    	assertEquals(EMAIL, client.get_email());
    	assertEquals(NAME, client.get_name());

    
    }
    
    @Test(expected = LoginBubbleDocsException.class)
    public void invalidUsername(){
    	
    		new StrictExpectations() {{
        		new GetUserInfoService(NOBODY);	
        		

    	}};
    	
    	GetUserInfoService client = new GetUserInfoService(NOBODY);

    	client.execute();
    }
    
    @Test(expected = LoginBubbleDocsException.class)
    public void emptyUsername(){
    	
    		new StrictExpectations() {{
        		new GetUserInfoService(EMPTY);	
        		

    	}};
    	
    	GetUserInfoService client = new GetUserInfoService(EMPTY);

    	client.execute();
    }

}