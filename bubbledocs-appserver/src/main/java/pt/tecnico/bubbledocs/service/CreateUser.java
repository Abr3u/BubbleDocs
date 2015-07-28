package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.*;

// add needed import declarations

public class CreateUser extends BubbleDocsService {

	
	private String _userToken = "";
	private String _newUsername = "";
	private String _name = "";
	private String _email = "";
	
	
    public CreateUser(String userToken, String newUsername, String email, String name) {
    	this._userToken = userToken;
    	this._newUsername = newUsername;	
    	this._email = email;
    	this._name = name;
    	
    }

    @Override
    protected void dispatch() throws BubbleDocsException {
    	BubbleDocs bd = BubbleDocs.getInstance();
    	if(bd.isOnline(_userToken)){

    		if(!(bd.getSession().isRootToken(_userToken))){
    			throw new UnauthorizedOperationException();
    		}																																																															

    		if(_name.isEmpty()) {
    			throw new  EmptyNameException();
    		}
    		if(_newUsername.isEmpty()) {
    			throw new EmptyUsernameException();
    		}

    	
    		User u = new User(this._name, this._newUsername, this._email);
    		
    		bd.addUser(u);	

    	}else throw new UserNotInSessionException(_newUsername);    
    }
}
