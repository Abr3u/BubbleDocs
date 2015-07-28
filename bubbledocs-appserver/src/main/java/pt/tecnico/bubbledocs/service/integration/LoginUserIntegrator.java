package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.LoginUser;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class LoginUserIntegrator extends BubbleDocsIntegrator {

	public LoginUserIntegrator(String _username, String _password) {
		this._username = _username;
		this._password = _password;
	}



	private String _username = "";
	private String _password = "";
	LoginUser localService = null;
	
	
	@Override
	protected void dispatch() throws Exception{
		
		localService = new LoginUser(_username,_password);
		IDRemoteServices remote  = new IDRemoteServices();
		
		try{
			localService.execute();
			remote.loginUser(_username, _password);

		}catch(RemoteInvocationException rie){

			throw new UnavailableServiceException();
		}
	}



	public String getUserToken() {
		if (localService != null)
			return localService.getUserToken();
	return null;
	}

}
