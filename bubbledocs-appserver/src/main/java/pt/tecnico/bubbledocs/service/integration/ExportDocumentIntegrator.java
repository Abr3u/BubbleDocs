package pt.tecnico.bubbledocs.service.integration;

import javax.xml.registry.JAXRException;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.ExportDocument;
import pt.tecnico.bubbledocs.service.GetUsername4TokenService;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;
import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class ExportDocumentIntegrator extends BubbleDocsIntegrator {
	private byte[] _docXML;
	private String _userToken = "";
	private int _docID;
	
    public ExportDocumentIntegrator(String userToken, int id) {
    	this._userToken = userToken;
    	this._docID=id;
    	
    }
	
	@Override

	protected void dispatch() throws BubbleDocsException, JAXRException, CapacityExceeded_Exception, DocDoesNotExist_Exception, UserDoesNotExist_Exception{

		
		
		ExportDocument localService = new ExportDocument(_userToken,_docID);
		StoreRemoteServices remote  = new StoreRemoteServices();
		
		try{
			
			localService.execute();
			byte[] document=localService.getDocXML();
			this.set_docXML(document);
			
			GetUsername4TokenService aux = new GetUsername4TokenService(_userToken);
			aux.execute();
			String username = aux.getUsername();
			remote.storeDocument(username, ""+_docID, document);

		}catch(RemoteInvocationException rie){
			throw new UnavailableServiceException();
		}
		
	}

	public byte[] get_docXML() {
		return _docXML;
	}

	public void set_docXML(byte[] _docXML) {
		this._docXML = _docXML;
	}

}
