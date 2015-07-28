package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.GetUsername4TokenService;
import pt.tecnico.bubbledocs.service.RenewPassword;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RenewPasswordIntegrator extends BubbleDocsIntegrator {

	
	private String _userToken = null;
	
	public RenewPasswordIntegrator(String token){
		_userToken = token;
	}
	
	
	@Override
	protected void dispatch() throws Exception {
		
		RenewPassword localService = new RenewPassword(_userToken);
		IDRemoteServices remoteService = new IDRemoteServices();
		GetUsername4TokenService auxService = new GetUsername4TokenService(_userToken);
		auxService.execute();
		
		try{
			
			localService.execute();
			remoteService.renewPassword(auxService.getUsername());
		}
		catch (RemoteInvocationException e){
			throw new UnavailableServiceException();
		}

	}

}
