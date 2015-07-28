package pt.tecnico.bubbledocs.exception;

public class UserNotInSessionException extends BubbleDocsException  {

	private String _username;
	private static final long serialVersionUID = 1L;

	public UserNotInSessionException(String username){
		this._username = username;
	}

	public String getConflictingName() {
		return "User " + _username + "not in session\n";
	}
	
}
