package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;
import pt.tecnico.bubbledocs.exception.InvalidSpreadIDException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

public class GetSpreadSheetContent extends BubbleDocsService {

	private String userTok;
	private Integer spreadId;

	private String[][] resultMatrix;

	public GetSpreadSheetContent(String userTok, Integer spreadId) {
		this.userTok = userTok;
		this.spreadId = spreadId;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = BubbleDocs.getInstance();

		//Check if user is in session
		if(!bd.isOnline(userTok)){
			throw new UserNotInSessionException(userTok);
		}

		//Check if spreadsheet exists
		if(bd.searchSpreadBySID(spreadId) == null){
			throw new InvalidSpreadIDException();
		}else{//Spreadshee exists

			Spreadsheet spread = bd.searchSpreadBySID(spreadId);

			GetUsername4TokenService service = new GetUsername4TokenService(userTok);
			service.execute();

			if(!spread.canRead(bd.getUserByUsername(service.getUsername()))){

				throw new UnauthorizedOperationException();
			}

			if(spread.getLinhastotais() == null || spread.getColunastotais() == null){
				throw new InvalidArgumentsException();
			}else {

				resultMatrix = spread.getSpreadMatrix();

			}

		}

	}


	public String[][] getSpreadMatrix(){

		return this.resultMatrix;
	}

}
