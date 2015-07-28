package pt.tecnico.bubbledocs.exception;

public class EmptyUsernameException extends BubbleDocsException {

		private static final long serialVersionUID = 1L;

		
		public EmptyUsernameException() {}
		
		public String getConflictingName() {
			return "No username found\n";		
		}
		
}

