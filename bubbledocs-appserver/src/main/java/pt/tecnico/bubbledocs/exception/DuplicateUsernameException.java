package pt.tecnico.bubbledocs.exception;

public class DuplicateUsernameException extends BubbleDocsException {

		private static final long serialVersionUID = 1L;

		private String conflictingName;
		
		public DuplicateUsernameException(String conflictingName) {
			this.conflictingName = conflictingName;
		}
		
		public String getConflictingName() {
			return "O utilizador com username" + conflictingName +"ja existe\n";
		}
		
	}

