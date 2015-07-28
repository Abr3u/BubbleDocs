package integration.component;


import static org.junit.Assert.assertTrue;
import mockit.Mocked;
import mockit.StrictExpectations;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.*;
import pt.tecnico.bubbledocs.exception.*;
import pt.tecnico.bubbledocs.service.*;
import pt.tecnico.bubbledocs.service.integration.ExportDocumentIntegrator;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;

// add needed import declarations

public class ExportDocumentIntegratorTest extends BubbleDocsServiceTest {

  
    private static final String ROOT_USERNAME = "root";
    private static final String SPREADSHEET_NAME = "spread";
    private static final String READ_USER = "abreu";
    private static final String NO_PERMISSION = "alex";

    // the tokens for user root
    private String _root;
    private int sid;
    private String _reader;
    private String _stranger;
    private Spreadsheet _spread;

    @Override
    public void populate4Test() {
        _root = addUserToSession(ROOT_USERNAME);
        User uroot = getUserFromUsername(ROOT_USERNAME);
        
        
        Spreadsheet s = createSpreadSheet(uroot, SPREADSHEET_NAME, 10, 10);
        s.getCell(1, 1).setConteudo(new CellInteger(2));
        s.getCell(1, 2).setConteudo(new CellRef(1, 1));
        s.getCell(1, 3).setConteudo(new CellInteger(-1));
        sid = s.getSID();
        _spread = s;
        
        
        createUser(READ_USER, "123", "ricardo");
        User uabreu = getUserFromUsername(READ_USER);
        _reader = addUserToSession(READ_USER);
        s.addRuser(uabreu);
        
        createUser(NO_PERMISSION, "456", "alex");
        _stranger = addUserToSession(NO_PERMISSION);
    };

    @Test
    public void success(@Mocked final StoreRemoteServices service) throws Exception{
    	
    	new StrictExpectations() {{

    		new ExportDocument(_reader,sid);
			new StoreRemoteServices();

			service.storeDocument(READ_USER, ""+sid , (byte[]) any); 
			
			new ExportDocument(_root, sid);
			new StoreRemoteServices();

			service.storeDocument(ROOT_USERNAME, ""+sid , (byte[]) any); 			
		}};
    	
		
    	// fazer casso teste para NORMAL
        ExportDocumentIntegrator remoteClient = new ExportDocumentIntegrator(_reader, sid);
        remoteClient.execute();
        byte[] testbytes = remoteClient.get_docXML();

        Spreadsheet imported = new Spreadsheet();
        imported.importFromXML(BubbleDocs.convertByteToXML(testbytes).getRootElement());
        assertTrue("A Spreadsheet exportada está incorreta", _spread.equals(imported));
        
        //caso teste utilizador DONO
        remoteClient = new ExportDocumentIntegrator(_root, sid);
        remoteClient.execute();
        testbytes = remoteClient.get_docXML();
        
        imported = new Spreadsheet();
        imported.importFromXML(BubbleDocs.convertByteToXML(testbytes).getRootElement());
        assertTrue("A Spreadsheet exportada está incorreta", _spread.equals(imported));
        
        
    }

    
    
    @Test(expected = InvalidSpreadIDException.class)
    public void WrongSpreadID() throws Exception{
    	new StrictExpectations() {{

    		new ExportDocument(_root,sid);
					
		}};
		
    	  ExportDocumentIntegrator service = new ExportDocumentIntegrator(_root, sid+1);
          service.execute();
    }


    @Test(expected = UnauthorizedOperationException.class)
    public void notRelated() throws Exception{
    	new StrictExpectations() {{

    		new ExportDocument(_stranger,sid);
					
		}};
  
    	  ExportDocumentIntegrator service = new ExportDocumentIntegrator(_stranger, sid);
          service.execute();
    }
    
    @Test(expected = UserNotInSessionException.class)
    public void notInSession() throws Exception{
    	new StrictExpectations() {{

    		new ExportDocument(_root,sid);
					
		}};
    	removeUserFromSession(_root);
    	ExportDocumentIntegrator service = new ExportDocumentIntegrator(_root, sid);
    	service.execute();
    }
    
    @Test(expected = InvalidArgumentsException.class)
    public void noToken() throws Exception{
    	new StrictExpectations() {{

    		new ExportDocument("",sid);
					
		}};
    	ExportDocumentIntegrator service = new ExportDocumentIntegrator("", sid);
    	service.execute();
    }
    
    @Test(expected = InvalidArgumentsException.class)
    public void nullToken() throws Exception{
    	new StrictExpectations() {{

    		new ExportDocument(null,sid);
					
		}};
    	ExportDocumentIntegrator service = new ExportDocumentIntegrator(null, sid);
    	service.execute();
    }
    
    @Test(expected = UnavailableServiceException.class)
	public void unavailableService(
			@Mocked final StoreRemoteServices service)
					throws Exception {

		new StrictExpectations() {{

			
			new ExportDocument(_root, sid);
			new StoreRemoteServices();

			service.storeDocument(_root, ""+sid , (byte[]) any); 

			result = new RemoteInvocationException();
		}};

		// Unit under test is now exercised
		ExportDocumentIntegrator remoteClient = new ExportDocumentIntegrator(_root, sid);

		remoteClient.execute();

	}
}
    
    
    