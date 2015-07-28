package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.InvalidSpreadIDException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.CreateSpreadSheet;
import pt.tecnico.bubbledocs.service.GetSpreadSheetContent;

public class GetSpreadSheetContentIntegrator extends BubbleDocsIntegrator {

	private String userTok;
	private Integer spreadId;
	
	private String[][] resultMatrix;
	private GetSpreadSheetContent localService = null;
	public GetSpreadSheetContentIntegrator(String userTok, Integer spreadId) {
		this.userTok = userTok;
		this.spreadId = spreadId;
	}

	@Override
	protected void dispatch() throws Exception {
		
		GetSpreadSheetContent localService = new GetSpreadSheetContent(userTok, spreadId);
		localService.execute();
		resultMatrix=localService.getSpreadMatrix();
	
	}

	public String[][] getResultMatrix() {
		if (localService != null)
			return localService.getSpreadMatrix();
	return null;
	}
	
}