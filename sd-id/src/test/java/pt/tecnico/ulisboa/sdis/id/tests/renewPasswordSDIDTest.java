package pt.tecnico.ulisboa.sdis.id.tests;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import pt.tecnico.ulisboa.sdis.id.SDIdImpl;
import pt.tecnico.ulisboa.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.tecnico.ulisboa.sdis.id.ws.InvalidEmail_Exception;
import pt.tecnico.ulisboa.sdis.id.ws.InvalidUser_Exception;
import pt.tecnico.ulisboa.sdis.id.ws.UserAlreadyExists_Exception;
import pt.tecnico.ulisboa.sdis.id.ws.UserDoesNotExist_Exception;
import mockit.*;



public class renewPasswordSDIDTest extends SDIDTest {

	private static String USERNAME = "abreu";
	private static String USERNAME2 = "alex";
	private static String EMAIL = "abreu@email.com";
	private static byte[] PASSWORD = "PASSWORD".getBytes();
	
	@Override
	public void populate4Test() {
		SDIdImpl sdAux = new SDIdImpl(wsUrl);
		HashMap<String,String> userm = sdAux.getUserManager();
		
		userm.put(USERNAME, EMAIL);

	};
	
	
	@Test
	public void success() throws UserDoesNotExist_Exception {
		
		SDIdImpl service = new SDIdImpl(wsUrl);
		service.renewPassword(USERNAME);

		
	}

	@Test(expected = UserDoesNotExist_Exception .class)
	public void userNotExists() throws UserDoesNotExist_Exception {
		SDIdImpl service = new SDIdImpl(wsUrl);
		service.renewPassword(USERNAME2);
	}
	
	
}