package pt.tecnico.bubbledocs.service.integration;

import javax.xml.registry.JAXRException;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.CannotLoadDocumentException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.ImportDocument;
import pt.tecnico.bubbledocs.service.GetUsername4TokenService;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

public class ImportDocumentIntegrator extends BubbleDocsIntegrator {
	private byte[] _docXML;
	private String _userToken = "";
	private int _docID;
	
    public ImportDocumentIntegrator(String userToken, int id) {
    	this._userToken = userToken;
    	this._docID=id;
    	
    }
	
	@Override
	protected void dispatch() throws CannotLoadDocumentException, UserDoesNotExist_Exception, DocDoesNotExist_Exception{
		
	
		StoreRemoteServices remote  = new StoreRemoteServices();
		GetUsername4TokenService usernameService = new GetUsername4TokenService(_userToken);
		usernameService.execute();
		User u = usernameService.get_user();
		
		try{
			byte[] document = remote.loadDocument(usernameService.getUsername(), new Integer(_docID).toString());
			
			set_docXML(document);
			System.out.println("Doc : "+new String(document) );
			ImportDocument service = new ImportDocument(u,document);
			service.execute();
			
		}catch(RemoteInvocationException  rie){
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
