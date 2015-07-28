package pt.tecnico.ulisboa.sdis.id.test;

import static org.junit.Assert.*;

import java.util.HashMap;

import mockit.Expectations;
import mockit.Mocked;
import mockit.StrictExpectations;

import org.junit.After;
import org.junit.Test;

import pt.tecnico.ulisboa.sdis.id.cli.SDIdClient;
import pt.tecnico.ulisboa.sdis.id.ws.*;
import pt.tecnico.ulisboa.sdis.id.ws.uddi.UDDINaming;

import javax.xml.registry.JAXRException;
import javax.xml.ws.WebServiceException;


public class IDClientTest {

	

private static String USERNAME = "abreu";
private static String EMAIL = "abreu@email.com";
private static String USERNAME2 = "alex";
private static String EMAIL2 = "abreu2@email.com";
/**
 *  In this test the server is mocked to
 *  simulate a communication exception.
 */

	@Test(expected=WebServiceException.class)
	public void testMockServerException(
			@Mocked final SDId_Service service,
			@Mocked final SDId port)
					throws Exception {

		// an "expectation block"
		// One or more invocations to mocked types, causing expectations to be recorded.
		new Expectations() {{
			new SDId_Service();
			service.getSDIdImplPort(); result = port;
			port.createUser(anyString,anyString);
			result = new WebServiceException("fabricated");
		}};


		// Unit under test is exercised.
		SDId_Service client = new SDId_Service();
		// call to mocked server
		SDId portest = client.getSDIdImplPort();
		portest.createUser(USERNAME, EMAIL);
	}
	

    /**
     *  In this test the server is mocked to
     *  simulate a communication exception on a second call.
     */
    @Test(expected=WebServiceException.class)
    public void testMockServerExceptionOnSecondCall(
			@Mocked final SDId_Service service,
			@Mocked final SDId port)
					throws Exception {

		// an "expectation block"
		// One or more invocations to mocked types, causing expectations to be recorded.
		new StrictExpectations() {{
			new SDId_Service();
			service.getSDIdImplPort(); result = port;
			port.createUser(anyString,anyString);
			
			port.createUser(anyString,anyString); result = new WebServiceException("fabricated");
		}};


		// Unit under test is exercised.
		SDId_Service client = new SDId_Service();
		SDId portest = client.getSDIdImplPort();
		
		
        portest.createUser(USERNAME, EMAIL);
        portest.createUser(USERNAME2, EMAIL2);
    }
	
    
    @Test(expected=JAXRException.class)
	public void testWrongLookup(@Mocked final UDDINaming uddiNaming)
					throws Exception {

		// an "expectation block"
		// One or more invocations to mocked types, causing expectations to be recorded.
		new Expectations() {{
			new UDDINaming(anyString);
			uddiNaming.lookup(anyString);
			result = new JAXRException("fabricated");
		}};


		SDIdClient client = new SDIdClient("http://localhost:8080/id-ws/notendpoint", "SDId");
		client.execute();
	}
    
    
}
