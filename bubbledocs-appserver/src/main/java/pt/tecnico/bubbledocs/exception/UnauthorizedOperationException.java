package pt.tecnico.bubbledocs.exception;

public class UnauthorizedOperationException extends BubbleDocsException  {


	private static final long serialVersionUID = 1L;

	public UnauthorizedOperationException(){}

	public String getConflictingName() {
		return "Não tem permissões\n";
	}
	
}
