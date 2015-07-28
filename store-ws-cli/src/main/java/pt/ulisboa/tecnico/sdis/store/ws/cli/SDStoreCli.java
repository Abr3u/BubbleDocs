package pt.ulisboa.tecnico.sdis.store.ws.cli;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.Key;
import java.util.List;

import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;
import pt.ulisboa.tecnico.sdis.store.ws.uddi.UDDINaming;

import javax.xml.registry.JAXRException;
import javax.xml.ws.*;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

public class SDStoreCli {

	private static Key myKey = null;
	private static boolean testing = false;
	
	private static int request = 0;

	private static Key sessionKey = null;
	private static byte[] ticket = null;
	private static String userName = null;
	public static Key getMyKey() {
		return myKey;
	}

	public static void setMyKey(Key myKey) {
		SDStoreCli.myKey = myKey;
	}

	public static Key getSessionKey() {
		return sessionKey;
	}

	public static void setSessionKey(Key sessionKey) {
		SDStoreCli.sessionKey = sessionKey;
	}

	
	
	public static int getRequest() {
		return request;
	}

	public static void setRequest(int request) {
		SDStoreCli.request = request;
	}

	public static byte[] getTicket() {
		return ticket;
	}

	public static void setTicket(byte[] ticket) {
		SDStoreCli.ticket = ticket;
	}

	public static String getUserName() {
		return userName;
	}

	public static void setUserName(String userName) {
		SDStoreCli.userName = userName;
	}
	
	public static boolean isTesting() {
		return testing;
	}

	public static void setTesting(boolean testing) {
		SDStoreCli.testing = testing;
	}

	
	private FrontEnd fe = null;

	private static String uddiURL;
	private static String name;
	
	public SDStoreCli(String url, String sname){
		uddiURL = url;
		name = sname;
	};


	public static void main(String[] args) throws Exception{
		SDStoreCli client = new SDStoreCli(args[0], args[1]);
		client.execute();
		
		DocUserPair dup = new DocUserPair();
		dup.setDocumentId("NOME");
		dup.setUserId("alice");
		client.createDoc(dup);
		List<String> aux = client.listDocs("alice");
		
		for(String s: aux){
			System.out.println("UM textinho "+s);
		}
	}



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

		System.out.println("Creating FE ...");
		fe = new FrontEnd(uddiURL,name);

		

		System.out.println("Endpoint address set succesfully!\n\n\n Ready to receive input");
	}




	public void createDoc(DocUserPair docUserPair)
			throws DocAlreadyExists_Exception, JAXRException{
		setRequest(0);
		fe.createDoc(docUserPair);

	}

	public List<String> listDocs(String userId) throws UserDoesNotExist_Exception, JAXRException {
		setRequest(0);
		return fe.listDocs(userId);
	}

	
	public void store(DocUserPair docUserPair, byte[] contents)
			throws CapacityExceeded_Exception, DocDoesNotExist_Exception,
			UserDoesNotExist_Exception {
		setRequest(0);
		fe.store(docUserPair, contents);
	}

	public void reset(){
		setRequest(0);
		fe.reset();
	}
	
	public byte[] load(DocUserPair docUserPair)
			throws UserDoesNotExist_Exception, DocDoesNotExist_Exception {
		setRequest(0);
		return fe.load(docUserPair);

	}

	public static void incRequest() {
		request++;
	}
}
