package integration.component;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.tecnico.bubbledocs.exception.EmptyNameException;
import pt.tecnico.bubbledocs.exception.InvalidSpreadsheetArgumentsException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.CreateSpreadSheet;
// add needed import declarations

public class CreateSpreadSheetTest extends BubbleDocsServiceTest {

  
    private static final String ROOT_USERNAME = "root";
    private static final String SPREADSHEET_NAME = "spread";

    // the tokens for user root
    private String root;

    @Override
    public void populate4Test() {
        root = addUserToSession(ROOT_USERNAME);
    };

    public void success() {
        CreateSpreadSheet service = new CreateSpreadSheet(root, SPREADSHEET_NAME, 10, 10);
        service.execute();

        
        boolean created = getSpreadSheet(SPREADSHEET_NAME) != null;
        boolean creatorSet = getSpreadSheet(SPREADSHEET_NAME).getCuser().getUsername().equals( "root");
        assertTrue("Spreadsheet not created", created);
        
        //Asserts para dimensoes da spreadsheet
        
        assertTrue("Creator not set correctly", creatorSet);
    }

    
    
    @Test(expected = InvalidSpreadsheetArgumentsException.class)
    public void rowNegative(){
    	new CreateSpreadSheet(ROOT_USERNAME, SPREADSHEET_NAME, -1, 10).execute();
    }
    
    @Test(expected = InvalidSpreadsheetArgumentsException.class)
    public void rowZero(){
    	new CreateSpreadSheet(ROOT_USERNAME, SPREADSHEET_NAME, 0, 10).execute();
    }
    
    @Test(expected = InvalidSpreadsheetArgumentsException.class)
    public void columnNegative(){
    	new CreateSpreadSheet(ROOT_USERNAME, SPREADSHEET_NAME, 10, -1).execute();
    }
    
    @Test(expected = InvalidSpreadsheetArgumentsException.class)
    public void columnZero(){
    	new CreateSpreadSheet(ROOT_USERNAME, SPREADSHEET_NAME, 10, 0).execute();
    }
    
    @Test(expected = EmptyNameException.class)
    public void noName(){
    	new CreateSpreadSheet(ROOT_USERNAME, "", 10, 10).execute();
    }
    
    @Test(expected = UserNotInSessionException.class)
    public void noUserInSession(){
    	new CreateSpreadSheet("jp", SPREADSHEET_NAME, 10, 10).execute();
    }
    
    
}
