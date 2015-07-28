package integration.component;


import static org.junit.Assert.assertTrue;
import mockit.Mocked;
import mockit.StrictExpectations;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.*;
import pt.tecnico.bubbledocs.service.*;
import pt.tecnico.bubbledocs.service.integration.ImportDocumentIntegrator;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;

// add needed import declarations

public class ImportDocumentIntegratorTest extends BubbleDocsServiceTest {

  
    private static final String USERNAME = "alice";
    private static final String EMAIL = "spread";
    private static final String NAME = "NOME";

  
	private Integer _sid;
	private byte[] exportedBytes;
	private String _token;
	private User _aux;
	private String _username;

    @Override
    public void populate4Test() {
    	_aux = createUser(USERNAME,EMAIL, NAME);
    	_username = _aux.getUsername();
		
		Spreadsheet spreadaux = createSpreadSheet(_aux, "spreadAux", 2, 2);
		_sid = spreadaux.getSID();
		_token = addUserToSession(_aux.getUsername());
		ExportDocument expAux = new ExportDocument(_token, _sid);
		expAux.execute();
		exportedBytes = expAux.getDocXML();
    };

    @Test
    public void success(@Mocked final StoreRemoteServices service) throws UserDoesNotExist_Exception, DocDoesNotExist_Exception {
    	
    	new StrictExpectations() {{
    		new StoreRemoteServices();
    		service.loadDocument(_username, _sid.toString());
    		result = exportedBytes;
    		new ImportDocument(_aux,exportedBytes);
						
		}};
    	
		
    	ImportDocumentIntegrator test = new ImportDocumentIntegrator(_token, _sid);
    	try {
			test.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	assertTrue("A Spreadsheet importada está incorreta",test.get_docXML().equals(exportedBytes));
    	
    }   
}