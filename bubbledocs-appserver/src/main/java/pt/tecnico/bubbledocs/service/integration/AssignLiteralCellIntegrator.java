package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.service.AssignLiteralCell;


public class AssignLiteralCellIntegrator extends BubbleDocsIntegrator {

	private int _docId;
	private String _tokenUser;
	private String _cellId;
	private String _literal;

	public AssignLiteralCellIntegrator(String accessUsername, int docId, String cellId,
            String literal) {
    	this._docId=docId;
    	this._tokenUser=accessUsername;
    	this._cellId = cellId;
    	this._literal=literal;    
    	}
	
	
	@Override
	protected void dispatch() throws Exception {
		
		AssignLiteralCell localService = new AssignLiteralCell(this._tokenUser, this._docId, this._cellId, this._literal);

		localService.execute();

	}

}
