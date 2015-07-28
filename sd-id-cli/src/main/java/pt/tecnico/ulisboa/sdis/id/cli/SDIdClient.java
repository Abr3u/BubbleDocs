	package pt.tecnico.ulisboa.sdis.id.cli;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;
import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import pt.tecnico.ulisboa.sdis.id.SymKey;
import pt.tecnico.ulisboa.sdis.id.ws.*;
// classes generated from WSDL
import pt.tecnico.ulisboa.sdis.id.ws.uddi.UDDINaming;



public class SDIdClient {

	private SDId_Service service = null;
	private SDId port = null;

	
	private static Key myKey = null;
	private static Key sessionKey = null;
	private static byte[] ticket = null;
	private static String userName = null;
	
	private String uddiURL;
	private String name;
	
	public SDIdClient(String url, String sname){
		uddiURL = url;
		name = sname;
	};

	public void execute() throws JAXRException{

		System.out.printf("Contacting UDDI at %s%n", uddiURL);
		UDDINaming uddiNaming = new UDDINaming(uddiURL);

		System.out.printf("Looking for '%s'%n", name);
		String endpointAddress = uddiNaming.lookup(name);

		if (endpointAddress == null) {
			System.out.println("Not found!");
			return;
		} else {
			System.out.printf("Found %s%n", endpointAddress);
		}

		System.out.println("Creating stub ...");
		service = new SDId_Service();
		port = service.getSDIdImplPort();

		System.out.println("Setting endpoint address ...");
		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);

		System.out.println("Endpoint address set succesfully!\n\n\n Ready to receive input");
	}
	public void createUser(String name, String email) throws EmailAlreadyExists_Exception, InvalidEmail_Exception, InvalidUser_Exception, UserAlreadyExists_Exception{
		port.createUser(name, email);
	}

	public void removeUser(String name) throws UserDoesNotExist_Exception{
		port.removeUser(name);
	}
	
	public void reset(){
		port.reset();
	}

	public void renewPassword(String name) throws UserDoesNotExist_Exception{
		port.renewPassword(name);
	}

	public byte[] requestAuthentication(String name, byte[] reserved) throws AuthReqFailed_Exception{
		if(name == null || reserved == null)
			throw new AuthReqFailed_Exception("Failed to Login, wrong password", new AuthReqFailed());
		
		String service = "SD-STORE";
		Date now = new Date();
		DateFormat dateF = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		service = service + "\n" + dateF.format(now);
		Key clientKey = new SecretKeySpec(SymKey.code(reserved), "AES");
		reserved = service.getBytes();
		byte[] res = port.requestAuthentication(name, reserved);
		
		userName = name;
		String resS = null;
		try {
			resS = new String(res,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String[] responseComps = resS.split("\n");
		System.out.println("Got : "+responseComps[0]+"\n\n "+responseComps[1]+"\n\n "+responseComps[2]+".");
		System.out.println("..................");
		
		byte[] ticket = parseBase64Binary(responseComps[0]);
		byte[] sessionKey = parseBase64Binary(responseComps[1]);
		byte[] N = parseBase64Binary(responseComps[2]);
		
		System.out.println("Decrypting using KEY : "+printBase64Binary(clientKey.getEncoded()));
		System.out.println("Bytes : "+ printBase64Binary(sessionKey));
		try {
			sessionKey = SymKey.decrypt(sessionKey, clientKey);
			N = SymKey.decrypt(N, clientKey);
		} catch (InvalidKeyException e) {
			throw new AuthReqFailed_Exception("Failed to Login, wrong password", new AuthReqFailed());
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			throw new AuthReqFailed_Exception("Failed to Login, wrong password", new AuthReqFailed());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		
		Key sKey = new SecretKeySpec(sessionKey, "AES");
		setSessionKey(sKey);
		setTicket(ticket);
		
		String Ns = new String(N);
		
		System.out.println("New session key -> "+printBase64Binary(sKey.getEncoded()));
		res = new byte[1];
		res[0] = 1;
		
		return res;
		
	}

	public static String getUserName() {
		return userName;
	}

	public static void setUserName(String userName) {
		SDIdClient.userName = userName;
	}

	public static void main(String[] args) throws Exception{
		
		SDIdClient client = new SDIdClient(args[0], args[1]);
		client.execute();
		System.out.println(new String(client.requestAuthentication("alice", new BufferedReader(new InputStreamReader(System.in)).readLine().getBytes())));
	
	}


	
	
	public static String stringKey(Key toPrint){
		
		return printBase64Binary(toPrint.getEncoded());
	}

	public static Key getMyKey() {
		return myKey;
	}

	public static void setMyKey(Key myKey) {
		SDIdClient.myKey = myKey;
	}

	public static Key getSessionKey() {
		return sessionKey;
	}

	public static void setSessionKey(Key sessionKey) {
		SDIdClient.sessionKey = sessionKey;
	}

	public static byte[] getTicket() {
		return ticket;
	}

	public static void setTicket(byte[] ticket) {
		SDIdClient.ticket = ticket;
	}
	
	
	

}
