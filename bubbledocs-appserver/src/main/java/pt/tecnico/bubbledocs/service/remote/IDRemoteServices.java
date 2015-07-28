package pt.tecnico.bubbledocs.service.remote;


import pt.sd.client.BdClient;
import pt.tecnico.bubbledocs.exception.*;
import pt.tecnico.ulisboa.sdis.id.ws.*;
import pt.tecnico.ulisboa.sdis.id.cli.*;

public class IDRemoteServices {
	
	private BdClient client = null;
	
	
	public IDRemoteServices() throws Exception{
		client = BdClient.getInstance();
	}
	

	
	public void createUser(String username, String email)
			throws InvalidUser_Exception, UserAlreadyExists_Exception,
			EmailAlreadyExists_Exception, InvalidEmail_Exception,
			RemoteInvocationException {
		System.out.println("IDRemoteServices @ CreateUser ::"+username +"|"+email);
		client.createUser(username, email);
		
	}
	public void loginUser(String username, String password)
			throws LoginBubbleDocsException, RemoteInvocationException, AuthReqFailed_Exception {
		client.requestAuthentication(username, password.getBytes());
		System.out.println("IDRemoteServices @ loginUser ::"+username +"|"+password);
	}
	public void removeUser(String user)
			throws LoginBubbleDocsException, RemoteInvocationException {
		try {
			client.removeUser(user);
		} catch (UserDoesNotExist_Exception e) {
			throw new LoginBubbleDocsException();
		}
		System.out.println("IDRemoteServices @ removeUser ::"+user);
	}
	public void renewPassword(String username)
			throws LoginBubbleDocsException, RemoteInvocationException {

		try {
			client.renewPassword(username);
		} catch (UserDoesNotExist_Exception e) {
			throw new LoginBubbleDocsException();
		}
		System.out.println("IDRemoteServices @ renewPassword ::"+username);
	}

	public void restart() {
		client.restart();
	}
}
