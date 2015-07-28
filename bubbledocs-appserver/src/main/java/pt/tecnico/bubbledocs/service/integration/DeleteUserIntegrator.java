package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.CreateUser;
import pt.tecnico.bubbledocs.service.DeleteUser;
import pt.tecnico.bubbledocs.service.GetUserInfoService;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class DeleteUserIntegrator extends BubbleDocsIntegrator {

	private String _userToken;
	private String _usertobedeleted;
	
	
	public DeleteUserIntegrator(String userToken, String toDeleteUsername) {
		_userToken = userToken;
		_usertobedeleted = toDeleteUsername;
	}

	
	@Override
	protected void dispatch() throws Exception{
		
		DeleteUser localService = new DeleteUser(_userToken,_usertobedeleted);
		
		IDRemoteServices remote  = new IDRemoteServices();	
		GetUserInfoService infoserv = new GetUserInfoService(_usertobedeleted);
		
		try{
		
			infoserv.execute();
			
			localService.execute();
			
			remote.removeUser(_usertobedeleted);

		}catch(RemoteInvocationException rie){
		
			new CreateUser(_userToken, _usertobedeleted, infoserv.get_email(), infoserv.get_name()).execute();
			
			throw new UnavailableServiceException();
		}
		
	}
		


}
