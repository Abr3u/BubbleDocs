package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.service.AssignReferenceCell;

public class AssignReferenceCellIntegrator extends BubbleDocsIntegrator {

	private int _docId;
	private String _tokenUser;
	private String _cellId;
	private String _reference;

	public AssignReferenceCellIntegrator(String tokenUser, int docId, String cellId,
			String reference) {
		this._docId=docId;
		this._tokenUser=tokenUser;
		this._cellId = cellId;
		this._reference=reference;
	}


	@Override
	protected void dispatch() throws Exception {
		
		AssignReferenceCell localService = new AssignReferenceCell(this._tokenUser, this._docId, this._cellId, this._reference);

		localService.execute();
		
	}

}
