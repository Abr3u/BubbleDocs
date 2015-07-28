package pt.tecnico.ulisboa.sdis.id.tests;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import pt.tecnico.ulisboa.sdis.id.SDIdImpl;
import pt.tecnico.ulisboa.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.tecnico.ulisboa.sdis.id.ws.InvalidEmail_Exception;
import pt.tecnico.ulisboa.sdis.id.ws.InvalidUser_Exception;
import pt.tecnico.ulisboa.sdis.id.ws.UserAlreadyExists_Exception;
import mockit.*;



public class createUserSDIDTest extends SDIDTest {

	private static String MORE8 = "ricardoabreu";
	private static String LESS3 = "ab";
	private static String USERNAME = "abreu";
	private static String USERNAME2 = "alex";
	private static String EMAIL = "abreu@email.com";
	private static String EMAIL2 = "abreu2@email.com";
	private static String NOTEMAIL = "abreu#email.com";
	private static byte[] PASSWORD = "PASSWORD".getBytes();
	
	@Override
	public void populate4Test() {
		SDIdImpl sdAux = new SDIdImpl(wsUrl);
		HashMap<String,String> userm = sdAux.getUserManager();
		
		userm.put(USERNAME, EMAIL);
		

	};
	
	
	@Test
	public void success() throws EmailAlreadyExists_Exception, InvalidEmail_Exception, InvalidUser_Exception, UserAlreadyExists_Exception {
		
		SDIdImpl service = new SDIdImpl(wsUrl);
		service.createUser(USERNAME2, EMAIL2);

		assertEquals(service.getUserEmail(USERNAME2),EMAIL2);
		
	}



	
	@Test(expected = EmailAlreadyExists_Exception.class)
	public void duplicateEmail() throws EmailAlreadyExists_Exception, InvalidEmail_Exception, InvalidUser_Exception, UserAlreadyExists_Exception{
		SDIdImpl service = new SDIdImpl(wsUrl);
		service.createUser(USERNAME2, EMAIL);
	}

	@Test(expected = InvalidEmail_Exception.class)
	public void invalidEmail() throws EmailAlreadyExists_Exception, InvalidEmail_Exception, InvalidUser_Exception, UserAlreadyExists_Exception{
		SDIdImpl service = new SDIdImpl(wsUrl);
		service.createUser(USERNAME2, NOTEMAIL);
	}
	
	@Test(expected = InvalidUser_Exception.class)
	public void less3username() throws EmailAlreadyExists_Exception, InvalidEmail_Exception, InvalidUser_Exception, UserAlreadyExists_Exception{
		SDIdImpl service = new SDIdImpl(wsUrl);
		service.createUser(LESS3, EMAIL2);
	}
	
	@Test(expected = InvalidUser_Exception.class)
	public void more8username() throws EmailAlreadyExists_Exception, InvalidEmail_Exception, InvalidUser_Exception, UserAlreadyExists_Exception{
		SDIdImpl service = new SDIdImpl(wsUrl);
		service.createUser(MORE8, EMAIL2);
	}
	
	@Test(expected = UserAlreadyExists_Exception .class)
	public void duplicateUser() throws EmailAlreadyExists_Exception, InvalidEmail_Exception, InvalidUser_Exception, UserAlreadyExists_Exception{
		SDIdImpl service = new SDIdImpl(wsUrl);
		service.createUser(USERNAME, EMAIL2);
	}
}