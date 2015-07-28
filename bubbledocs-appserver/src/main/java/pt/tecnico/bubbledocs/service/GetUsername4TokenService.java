package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.*;


// add needed import declarations

public class GetUsername4TokenService extends BubbleDocsService {

	
	private String _username;
	private String _token;
	private User _user;
	
	
    public GetUsername4TokenService(String tok) {
    	this.set_token(tok);
    	
    }

    @Override
    protected void dispatch() throws BubbleDocsException {
    	BubbleDocs bd = BubbleDocs.getInstance();
    	if(bd.getSession().getUserFromSession(_token)!=null){
    		User u= bd.getSession().getUserFromSession(_token);
    		this.set_username(u.getUsername());
    		this.set_user(u);
    	}
    	else{
    		throw new UserNotInSessionException(_token);
    	}
    }

	private void set_username(String username) {
		_username=username;
		
	}
	
	public String getUsername(){
		return this._username;
	}

	public String get_token() {
		return _token;
	}

	public void set_token(String _token) {
		this._token = _token;
	}

	public User get_user() {
		return _user;
	}

	public void set_user(User _user) {
		this._user = _user;
	}


	
	
}
