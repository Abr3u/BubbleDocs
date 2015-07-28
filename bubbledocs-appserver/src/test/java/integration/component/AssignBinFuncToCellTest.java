package integration.component;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.Cell;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;
import pt.tecnico.bubbledocs.exception.InvalidCellLocation;
import pt.tecnico.bubbledocs.exception.InvalidSpreadIDException;
import pt.tecnico.bubbledocs.exception.ProtectedCellException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.AssignBinaryFunctionToCell;
import integration.component.BubbleDocsServiceTest;

// add needed import declarations

public class AssignBinFuncToCellTest extends BubbleDocsServiceTest {

  
    private static final String ROOT_USERNAME = "root";
    private static final String SPREADSHEET_NAME = "spread";
    private static final String READ_USER = "abreu";
    private static final String NO_PERMISSION = "alex";
    private static final String BINFUNCLIT = "=ADD(2,3)";
    private static final String BINFUNCREF = "=ADD(2;5,3;4)";
    private static final String BINFUNC = "=ADD(2;3,4)";
    private static final String CELL = "1;1";

    

    // the tokens for user root
    private String _root;
    private int sid;
    private String _reader;
    private String _stranger;

    @Override
    public void populate4Test() {
        _root = addUserToSession(ROOT_USERNAME);
        User uroot = getUserFromUsername(ROOT_USERNAME);
        Spreadsheet s = createSpreadSheet(uroot, SPREADSHEET_NAME, 10, 10);
        
        sid = s.getSID();
        Cell cellProt = s.getCell(1,3);
        cellProt.setProt(true);
        
        createUser(READ_USER, "123", "ricardo");
        User uabreu = getUserFromUsername(READ_USER);
        _reader = addUserToSession(READ_USER);
        s.addRuser(uabreu);
        
        createUser(NO_PERMISSION, "456", "alex");
        _stranger = addUserToSession(NO_PERMISSION);
    };

    @Test
    public void success() {
    	//Test with 2 literals
        AssignBinaryFunctionToCell service = new AssignBinaryFunctionToCell(_root, sid, CELL, BINFUNCLIT);
        service.execute();
        
        boolean created = service.getResult() != null;
        assertTrue("Function not created", created);
        
        boolean valueset = service.getResult().equals(getSpreadSheet(SPREADSHEET_NAME).getCell(1,1).toString());
        assertTrue("Function not set correctly", valueset);
        
        //Test with 2 references
        service = new AssignBinaryFunctionToCell(_root, sid, CELL, BINFUNCREF);
        service.execute();
        
        created = service.getResult() != null;
        assertTrue("Function not created", created);
        
        valueset = service.getResult().equals(getSpreadSheet(SPREADSHEET_NAME).getCell(1,1).toString());
        assertTrue("Function not set correctly", valueset);
      
        //Test with 1 each
        service = new AssignBinaryFunctionToCell(_root, sid, CELL, BINFUNC);
        service.execute();
        
        created = service.getResult() != null;
        assertTrue("Function not created", created);
        
        valueset = service.getResult().equals(getSpreadSheet(SPREADSHEET_NAME).getCell(1,1).toString());
        assertTrue("Function not set correctly", valueset);
        
        
    }

    
    
    @Test(expected = InvalidSpreadIDException.class)
    public void WrongSpreadID(){
    	  AssignBinaryFunctionToCell service = new AssignBinaryFunctionToCell(_root, sid+1, CELL, BINFUNC);
          service.execute();
    }

    @Test(expected = UnauthorizedOperationException.class)
    public void canOnlyRead(){
    	  AssignBinaryFunctionToCell service = new AssignBinaryFunctionToCell(_reader, sid, CELL, BINFUNC);
          service.execute();
    }

    @Test(expected = UnauthorizedOperationException.class)
    public void notRelated(){
    	AssignBinaryFunctionToCell service = new AssignBinaryFunctionToCell(_stranger, sid, CELL, BINFUNC);
          service.execute();
    }
    
    @Test(expected = UserNotInSessionException.class)
    public void notInSession(){
    	removeUserFromSession(_root);
    	AssignBinaryFunctionToCell service = new AssignBinaryFunctionToCell(_root, sid, CELL, BINFUNC);
    	service.execute();
    }
    
    @Test(expected = InvalidArgumentsException.class)
    public void emptyValue(){
    	AssignBinaryFunctionToCell service = new AssignBinaryFunctionToCell(_root, sid, CELL, "");
          service.execute();
    }
    
    
    @Test(expected = InvalidArgumentsException.class)
    public void invalidValue(){
    	AssignBinaryFunctionToCell service = new AssignBinaryFunctionToCell(_root, sid, CELL, "ola");
          service.execute();
    }
    
    @Test(expected = InvalidArgumentsException.class)
    public void emptyCell(){
    	AssignBinaryFunctionToCell service = new AssignBinaryFunctionToCell(_root, sid, "", BINFUNC);
          service.execute();
    }
    
    
    @Test(expected = InvalidArgumentsException.class)
    public void invalidCellTypo(){
    	AssignBinaryFunctionToCell service = new AssignBinaryFunctionToCell(_root, sid, "1,1,1", BINFUNC);
          service.execute();
    }
    
    @Test(expected = InvalidArgumentsException.class)
    public void invalidCell(){
    	AssignBinaryFunctionToCell service = new AssignBinaryFunctionToCell(_root, sid, "ola", BINFUNC);
          service.execute();
    }
    
    @Test(expected = InvalidCellLocation.class)
    public void invalidCellLocation(){
    	AssignBinaryFunctionToCell service = new AssignBinaryFunctionToCell(_root, sid, "11;11", BINFUNC);
          service.execute();
    }
    
    @Test(expected = ProtectedCellException.class)
    public void protectedCell(){
    	AssignBinaryFunctionToCell service = new AssignBinaryFunctionToCell(_root, sid, "1;3", BINFUNC);
          service.execute();
    }
    
    
    
}
    
    
    