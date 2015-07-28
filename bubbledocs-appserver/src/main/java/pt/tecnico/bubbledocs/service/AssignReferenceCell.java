package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.CellRef;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;
import pt.tecnico.bubbledocs.exception.InvalidCellLocation;
import pt.tecnico.bubbledocs.exception.InvalidSpreadIDException;
import pt.tecnico.bubbledocs.exception.ProtectedCellException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

// add needed import declarations

public class AssignReferenceCell extends BubbleDocsService {
    private String _result;
    private int _docId;
    private String _tokenUser;
    private String _cellId;
    private String _reference;
    
    public AssignReferenceCell(String tokenUser, int docId, String cellId,
            String reference) {
    	_docId=docId;
    	_tokenUser=tokenUser;
    	_cellId = cellId;
    	_reference=reference;
    }
    
    

    @Override
    protected void dispatch() throws BubbleDocsException {
    	BubbleDocs bd = BubbleDocs.getInstance();
    	if(bd.searchSpreadBySID(_docId) == null){
    		throw new InvalidSpreadIDException();
    	}
    	else{//existe Spread
    		Spreadsheet spread = bd.searchSpreadBySID(_docId);
    		try{
    			Integer[] lc = spread.parseCellID(_cellId);
    			if(lc==null){
    				throw new InvalidArgumentsException();
    			}
    			if(!spread.checkCoordinates(lc[0], lc[1])){
    				throw new InvalidCellLocation(lc[0]+";"+lc[1]);
    			}
    			Integer[] rf = spread.parseCellID(_reference);
    			if(rf==null){
    				throw new InvalidArgumentsException();
    			}
    			if(!spread.checkCoordinates(rf[0], rf[1])){
    				throw new InvalidCellLocation(rf[0]+";"+rf[1]);
    			}
    			Cell cel = spread.getCell(lc[0], lc[1]);
    			if(cel.getProt()){
    				throw new ProtectedCellException();
    			}
    			if(!bd.getSession().isOnline(_tokenUser)){
    				throw new UserNotInSessionException(_tokenUser);
    			}
    			if(!spread.canWrite(bd.getSession().getUserFromSession(_tokenUser))){
    				throw new UnauthorizedOperationException();
    			}
    			cel.setConteudo(new CellRef(rf[0],rf[1]));
    			this.setResult(cel.toString());
    		}catch(NumberFormatException e){
        		throw new InvalidArgumentsException();
        	}
    		
    	}
    }

    public final String getResult() {
        return _result;
    }
    
    public void setResult(String res){
    	_result = res;
    }
}
