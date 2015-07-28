package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.*;

// add needed import declarations

public class GetUserInfoService extends BubbleDocsService {


	private String _username;
	private String _name;
	private String _email;


	public GetUserInfoService(String u) {
		this._username=u;

	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = BubbleDocs.getInstance();
		if(bd.getUserByUsername(_username)!=null){
			this.set_name(bd.getUserByUsername(_username).getName());
			this.set_email(bd.getUserByUsername(_username).getEmail());
		}
		else{
			throw new LoginBubbleDocsException();
		}
	}


	public String get_username() {
		return _username;
	}
	
	public String get_name() {
		return _name;
	}

	public void set_name(String _name) {
		this._name = _name;
	}

	public String get_email() {
		return _email;
	}

	public void set_email(String _email) {
		this._email = _email;
	}

}
