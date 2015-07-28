package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.service.AssignBinaryFunctionToCell;

public class AssignBinaryFunctionToCellIntegrator extends BubbleDocsIntegrator {
	
	private int _docId;
	private String _tokenUser;
	private String _cellId;
	private String _expression;

	 public AssignBinaryFunctionToCellIntegrator(String accessUsername, int docId, String cellId,
	            String exp) {
	    	this._docId=docId;
	    	this._tokenUser=accessUsername;
	    	this._cellId = cellId;
	    	this._expression=exp;    
	    	}
	
	@Override
	protected void dispatch() throws Exception {
		
		AssignBinaryFunctionToCell localService = new AssignBinaryFunctionToCell(_tokenUser,_docId, _cellId, _expression);
		
		localService.execute();

	}

}
