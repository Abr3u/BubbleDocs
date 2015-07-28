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
import pt.tecnico.bubbledocs.service.AssignLiteralCell;
import integration.component.BubbleDocsServiceTest;

// add needed import declarations

public class AssignLiteralCellTest extends BubbleDocsServiceTest {

  
    private static final String ROOT_USERNAME = "root";
    private static final String SPREADSHEET_NAME = "spread";
    private static final String READ_USER = "abreu";
    private static final String NO_PERMISSION = "alex";

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
        AssignLiteralCell service = new AssignLiteralCell(_root, sid, "1,1", "7");
        service.execute();

        
        boolean created = service.getResult() != null;
        assertTrue("Literal not created", created);
        
        boolean valueset = service.getResult().equals(getSpreadSheet(SPREADSHEET_NAME).getCell(1,1).toString());
        assertTrue("Literal not set correctly", valueset);
    }

    
    
    @Test(expected = InvalidSpreadIDException.class)
    public void WrongSpreadID(){
    	  AssignLiteralCell service = new AssignLiteralCell(_root, sid+1, "1,1", "7");
          service.execute();
    }

    @Test(expected = UnauthorizedOperationException.class)
    public void canOnlyRead(){
    	  AssignLiteralCell service = new AssignLiteralCell(_reader, sid, "1,1", "7");
          service.execute();
    }

    @Test(expected = UnauthorizedOperationException.class)
    public void notRelated(){
    	  AssignLiteralCell service = new AssignLiteralCell(_stranger, sid, "1,1", "7");
          service.execute();
    }
    
    @Test(expected = UserNotInSessionException.class)
    public void notInSession(){
    	removeUserFromSession(_root);
    	AssignLiteralCell service = new AssignLiteralCell(_root, sid, "1,1", "7");
    	service.execute();
    }
    
    @Test(expected = InvalidArgumentsException.class)
    public void emptyValue(){
    	  AssignLiteralCell service = new AssignLiteralCell(_root, sid, "1,1", "");
          service.execute();
    }
    
    @Test(expected = InvalidArgumentsException.class)
    public void invalidValueTypo(){
    	  AssignLiteralCell service = new AssignLiteralCell(_root, sid, "1,1", "7,");
          service.execute();
    }
    
    @Test(expected = InvalidArgumentsException.class)
    public void invalidValue(){
    	  AssignLiteralCell service = new AssignLiteralCell(_root, sid, "1,1", "ola");
          service.execute();
    }
    
    @Test(expected = InvalidArgumentsException.class)
    public void emptyCell(){
    	  AssignLiteralCell service = new AssignLiteralCell(_root, sid, "", "7");
          service.execute();
    }
    
    
    @Test(expected = InvalidArgumentsException.class)
    public void invalidCellTypo(){
    	  AssignLiteralCell service = new AssignLiteralCell(_root, sid, "1,1,1", "7");
          service.execute();
    }
    
    @Test(expected = InvalidArgumentsException.class)
    public void invalidCell(){
    	  AssignLiteralCell service = new AssignLiteralCell(_root, sid, "ola", "7");
          service.execute();
    }
    
    @Test(expected = InvalidCellLocation.class)
    public void invalidCellLocation(){
    	  AssignLiteralCell service = new AssignLiteralCell(_root, sid, "11,11", "7");
          service.execute();
    }
    
    @Test(expected = ProtectedCellException.class)
    public void protectedCell(){
    	  AssignLiteralCell service = new AssignLiteralCell(_root, sid, "1,3", "7");
          service.execute();
    }
    
    
    
}
    
    
    