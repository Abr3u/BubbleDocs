package pt.tecnico.bubbledocs.exception;

public class UserAlreadyExistsException extends BubbleDocsException {

		private static final long serialVersionUID = 1L;

		private String conflictingName;
		
		public UserAlreadyExistsException(String conflictingName) {
			this.conflictingName = conflictingName;
		}
		
		public String getConflictingName() {
			return "O utilizador com username" + conflictingName +"ja existe\n";
		}
		
	}

