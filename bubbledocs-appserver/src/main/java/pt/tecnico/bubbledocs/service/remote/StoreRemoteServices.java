package pt.tecnico.bubbledocs.service.remote;

import java.util.List;

import javax.xml.registry.JAXRException;

import pt.sd.client.BdClient;
import pt.tecnico.bubbledocs.exception.CannotStoreDocumentException;
import pt.tecnico.bubbledocs.exception.CannotLoadDocumentException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class StoreRemoteServices {
	
	
	private BdClient client = null;

	public StoreRemoteServices() {
		
			try {
				client = BdClient.getInstance();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	



	public void createDoc(DocUserPair docUserPair)
			throws DocAlreadyExists_Exception, JAXRException{
		client.createDoc(docUserPair);

	}

	public List<String> listDocs(String userId) throws UserDoesNotExist_Exception, JAXRException {
		return client.listDocs(userId);
	}
	
	
	public void storeDocument(String username, String docName, byte[] document) 
			throws CannotStoreDocumentException, RemoteInvocationException, 
			CapacityExceeded_Exception, DocDoesNotExist_Exception, UserDoesNotExist_Exception {
		DocUserPair dup = new DocUserPair();
		dup.setDocumentId(docName);
		dup.setUserId(username);
		client.store(dup, document); 
	
		}

	
	public byte[] loadDocument(String username, String docName)
			throws CannotLoadDocumentException, RemoteInvocationException,
			UserDoesNotExist_Exception, DocDoesNotExist_Exception {

		DocUserPair doc = new DocUserPair();
		doc.setDocumentId(docName);
		doc.setUserId(username);

		return 	client.load(doc);
	}

	public void restart() {
		client.restart();
		
	}
}

