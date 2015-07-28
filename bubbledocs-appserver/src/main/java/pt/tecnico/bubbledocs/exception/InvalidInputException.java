package pt.tecnico.bubbledocs.exception;

public class InvalidInputException extends BubbleDocsException {

	private String invalidInput;
	
	private static final long serialVersionUID = 1L;
	
	public  InvalidInputException(String conflictingName) {
		this.invalidInput = conflictingName;
	}
	
	public String getConflictingName() {
		return this.invalidInput+"\n";
	}

}
