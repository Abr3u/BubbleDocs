package pt.tecnico.bubbledocs.exception;




/**
 * Launched when an invalid cell location is used.
 */
public class InvalidCellLocation extends BubbleDocsException {
	
	private static final long serialVersionUID = 1L;

	private String endereco;

	public InvalidCellLocation(String endereco) {
		this.endereco = endereco;
	}
	
	public String getCellLocation() {
		return this.endereco;
	}
	
}

