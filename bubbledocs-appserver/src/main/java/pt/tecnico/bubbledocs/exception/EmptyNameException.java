package pt.tecnico.bubbledocs.exception;

public class EmptyNameException extends BubbleDocsException {

		private static final long serialVersionUID = 1L;

		
		public EmptyNameException() {}
		
		public String getConflictingName() {
			return "No name found\n";		
		}
		
}

