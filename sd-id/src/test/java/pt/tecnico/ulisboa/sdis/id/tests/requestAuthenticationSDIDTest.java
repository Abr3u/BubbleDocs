package pt.tecnico.ulisboa.sdis.id.tests;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.*;

import pt.tecnico.ulisboa.sdis.id.SDIdImpl;
import pt.tecnico.ulisboa.sdis.id.ws.AuthReqFailed_Exception;
import pt.tecnico.ulisboa.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.tecnico.ulisboa.sdis.id.ws.InvalidEmail_Exception;
import pt.tecnico.ulisboa.sdis.id.ws.InvalidUser_Exception;
import pt.tecnico.ulisboa.sdis.id.ws.UserAlreadyExists_Exception;
import pt.tecnico.ulisboa.sdis.id.ws.UserDoesNotExist_Exception;
import mockit.*;



public class requestAuthenticationSDIDTest extends SDIDTest {

	private static String USERNAME = "alice";
	private static String USERNAME2 = "abreu";
	private static String EMAIL = "abreu@email.com";
	private static Date now = new Date();
	private static DateFormat dateF = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
	private static String SERVICE = "SD-STORE" + "\n" + dateF.format(now);
	private static String SERVICE2 = "SD-STORA" + "\n" + dateF.format(now);
	
	@Override
	public void populate4Test() {
		SDIdImpl sdAux = new SDIdImpl(wsUrl);
		HashMap<String,String> userm = sdAux.getUserManager();
		
		userm.put(USERNAME, EMAIL);

	};
	
	
	@Test
	public void success() throws AuthReqFailed_Exception {
		
		SDIdImpl service = new SDIdImpl(wsUrl);
		service.requestAuthentication(USERNAME, SERVICE.getBytes());

	}

	@Test(expected = AuthReqFailed_Exception .class)
	public void unknownService() throws AuthReqFailed_Exception {
		SDIdImpl service = new SDIdImpl(wsUrl);
		service.requestAuthentication(USERNAME, SERVICE2.getBytes());
	}
	
	@Test(expected = AuthReqFailed_Exception .class)
	public void userNotExists() throws AuthReqFailed_Exception {
		SDIdImpl service = new SDIdImpl(wsUrl);
		service.requestAuthentication(USERNAME2, SERVICE.getBytes());
	}
}