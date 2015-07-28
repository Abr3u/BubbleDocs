package pt.tecnico.bubbledocs.service;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.domain.*;

// add needed import declarations

public class LoginUser extends BubbleDocsService {

	private String userToken;
	private String _username;
	private String _password;

	public LoginUser(String username,String password){
		this._username=username;
		this._password=password;
	}


	@Override
	protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = BubbleDocs.getInstance();

		try{
			setUserToken(bd.tryLocalLogin(_username,_password));
			if(!bd.checkDados(_username, _password)){
				throw new UnavailableServiceException();
			}
		}catch(NullPointerException npe){
			throw new UnavailableServiceException();
		}
	}




	public final String getUserToken() {
		return userToken;
	}

	public void setUserToken(String tok){
		this.userToken=tok;
	}

	// returns the time of the last access for the user with token userToken.
	// It must get this data from the session object of the application
	public DateTime getLastAccessTimeInSession(User u) {

		return u.getLtoken().getLogindate();
	}

	public Integer getElapsedTime(User user, DateTime currentTime){

		return Seconds.secondsBetween(getLastAccessTimeInSession(user), currentTime).getSeconds();
	}

}
