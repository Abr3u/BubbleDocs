package pt.sd.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.Key;
import java.util.List;

import javax.xml.registry.JAXRException;

import pt.tecnico.ulisboa.sdis.id.cli.SDIdClient;
import pt.tecnico.ulisboa.sdis.id.ws.AuthReqFailed_Exception;
import pt.tecnico.ulisboa.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.tecnico.ulisboa.sdis.id.ws.InvalidEmail_Exception;
import pt.tecnico.ulisboa.sdis.id.ws.InvalidUser_Exception;
import pt.tecnico.ulisboa.sdis.id.ws.UserAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.cli.SDStoreCli;



public class BdClient{
	
	private static BdClient instance = null;
	
	private static final String uddiURL = "http://localhost:8081";
	private static String idName = "SDId";
	private static String stName = "SD-Store";

	
	
	private static SDIdClient id = null;
	
	private static SDStoreCli st = null;
	
	
	
	
	protected BdClient () throws Exception{ 
		id = new SDIdClient(uddiURL, idName);
		st = new SDStoreCli(uddiURL, stName);
		id.execute();
		st.execute();
	}
	
	public static BdClient getInstance() throws Exception{
		if (instance == null)
			instance = new BdClient();
		return instance;
	}
	
	public static void main(String[] args) throws Exception{
		BdClient cl = getInstance();
		System.out.println("Introduza password....");
		cl.requestAuthentication("alice", new BufferedReader(new InputStreamReader(System.in)).readLine().getBytes());
		System.out.println("Tentando criar doc.....");
		
		DocUserPair doc = new DocUserPair();
		doc.setUserId("Abreu");
		doc.setDocumentId("1");
		cl.createDoc(doc);
		System.out.println("done ");
	}
	
	
	public void createUser(String name, String email) throws EmailAlreadyExists_Exception, InvalidEmail_Exception, InvalidUser_Exception, UserAlreadyExists_Exception{
		id.createUser(name, email);
	}
	
	public void removeUser(String name) throws pt.tecnico.ulisboa.sdis.id.ws.UserDoesNotExist_Exception{
		id.removeUser(name);
	}
	
	public void renewPassword(String name) throws pt.tecnico.ulisboa.sdis.id.ws.UserDoesNotExist_Exception{
		id.renewPassword(name);
	}
	
	public byte[] requestAuthentication(String name, byte[] pass) throws AuthReqFailed_Exception{
		byte[] res = id.requestAuthentication(name, pass);
		st.setMyKey(id.getMyKey());
		st.setSessionKey(id.getSessionKey());
		st.setTicket(id.getTicket());
		st.setUserName(id.getUserName());
		return res;
	}
	
	public void createDoc(DocUserPair docUserPair) throws DocAlreadyExists_Exception, JAXRException{
		st.createDoc(docUserPair);
	}

	
	public List<String> listDocs(String userId) throws UserDoesNotExist_Exception, JAXRException{
		return st.listDocs(userId);
	}
	
	public void restart(){
		st.reset();
		id.reset();
	}
	
	
	public void store(DocUserPair docUserPair, byte[] contents) throws CapacityExceeded_Exception, DocDoesNotExist_Exception, UserDoesNotExist_Exception{
		st.store(docUserPair, contents);
		}


	public byte[] load(DocUserPair docUserPair) throws UserDoesNotExist_Exception, DocDoesNotExist_Exception{
		return st.load(docUserPair);
	}
		
	
}
