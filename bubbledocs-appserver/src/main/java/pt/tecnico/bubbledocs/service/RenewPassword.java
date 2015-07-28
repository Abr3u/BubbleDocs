package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

public class RenewPassword extends BubbleDocsService {

	private String _userToken;

	public RenewPassword(String userToken){
		this._userToken = userToken;

	}

	@Override
	protected void dispatch() throws BubbleDocsException {

		BubbleDocs bd = BubbleDocs.getInstance();
		
		if(bd.isOnline(this._userToken)){
			bd.renewPass(_userToken);
		}else{
			throw new UserNotInSessionException(this._userToken);
		}

	}

}
