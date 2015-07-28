package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.InvalidUsernameException;
import pt.tecnico.bubbledocs.service.CreateUser;
import pt.tecnico.bubbledocs.service.DeleteUser;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class CreateUserIntegrator extends BubbleDocsIntegrator {

	private String _userToken = "";
	private String _newUsername = "";
	private String _name = "";
	private String _email = "";
	
    public CreateUserIntegrator(String userToken, String newUsername, String email, String name) {
    	this._userToken = userToken;
    	this._newUsername = newUsername;	
    	this._email = email;
    	this._name = name;
    	
    }
	
	@Override
	protected void dispatch() throws Exception{
		
		
		CreateUser localService = new CreateUser(_userToken,_newUsername,_email,_name);
		IDRemoteServices remote  = new IDRemoteServices();
		
		try{
			localService.execute();
			remote.createUser(_newUsername, _email);

		}catch(RemoteInvocationException rie){

			DeleteUser compensateService = new DeleteUser(_userToken, _newUsername);
			compensateService.execute();
			throw new UnavailableServiceException();
		}
		
	}

}
