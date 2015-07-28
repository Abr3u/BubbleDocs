package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

// add needed import declarations

public class DeleteUser extends BubbleDocsService {

	private String _userToken;
	private String _usertobedeleted;

	public DeleteUser(String userToken, String toDeleteUsername) {
		_userToken = userToken;
		_usertobedeleted = toDeleteUsername;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {


		BubbleDocs bd = BubbleDocs.getInstance();

		if(!bd.getSession().isOnline(_userToken)){ //nao online
			throw new UserNotInSessionException(_userToken); 
		}
		else{ //e root
			if(!(bd.getSession().isRootToken(_userToken))){//nao e root
				throw new UnauthorizedOperationException();
			}
			else{ //root online

			
				User u = bd.getUserByUsername(_usertobedeleted);
				
				if (u == null)
					throw new LoginBubbleDocsException();
				if(u.getLtoken()!=null){

					bd.getSession().removeUserFromSession(u.getLtoken().getIdentifier());

				}

				bd.removeUser(u);
				u.delete();
			
				
				}
		}
	}
}

