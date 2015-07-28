package integration.system;


import mockit.Expectations;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.service.ExportDocument;
import pt.tecnico.bubbledocs.service.integration.AssignBinaryFunctionToCellIntegrator;
import pt.tecnico.bubbledocs.service.integration.AssignLiteralCellIntegrator;
import pt.tecnico.bubbledocs.service.integration.AssignReferenceCellIntegrator;
import pt.tecnico.bubbledocs.service.integration.CreateSpreadSheetIntegrator;
import pt.tecnico.bubbledocs.service.integration.ExportDocumentIntegrator;
import pt.tecnico.bubbledocs.service.integration.GetSpreadSheetContentIntegrator;
import pt.tecnico.bubbledocs.service.integration.ImportDocumentIntegrator;
import pt.tecnico.bubbledocs.service.integration.LoginUserIntegrator;


public class RemoteSystemIT extends BubbleDocsIT{

	private static final String USERNAME = "alice";
	private static final String PASS = "Aaa1";
	private static final String EMAIL = "alice@email.com";

	private static final String NAME = "AliceName";


	private String token;
	private int sid;
	private byte[] exportedBytes;

	public void populate4Test() {
		
		User aux = createUser(USERNAME,EMAIL, NAME);
		setPasswordForUser(USERNAME, PASS);
		
		Spreadsheet spreadaux = createSpreadSheet(aux, "spreadAux", 2, 2);
		String auxTok = addUserToSession(aux.getUsername());
		ExportDocument expAux = new ExportDocument(auxTok, spreadaux.getSID());
		expAux.execute();
		exportedBytes = expAux.getDocXML();
	}

	@Test
	public void success() throws Exception{
		
 new Expectations(){{
	 
	 new LoginUserIntegrator(USERNAME, PASS);
	 new CreateSpreadSheetIntegrator(token,"MinhaSpread",10,10); 
	 new AssignLiteralCellIntegrator(token,sid,"1;1","17");
	 new AssignReferenceCellIntegrator(token,sid,"1;2","1;1");
	 new AssignBinaryFunctionToCellIntegrator(token,sid,"1;3","=ADD(1;1,3)");
	 new GetSpreadSheetContentIntegrator(token, sid);
	 new ExportDocumentIntegrator(token, sid);
	 new ImportDocumentIntegrator(USERNAME,sid);
	 new GetSpreadSheetContentIntegrator(token, sid+1);
 }};
 
		LoginUserIntegrator login = new LoginUserIntegrator(USERNAME, PASS);
		login.execute();
		token = login.getUserToken();
		
		
		//criar nova spread com one each e exportá-la
		CreateSpreadSheetIntegrator spreadservice = new CreateSpreadSheetIntegrator(token,"MinhaSpread",10,10);
		spreadservice.execute();
		sid = spreadservice.getID();
		
		AssignLiteralCellIntegrator literal = new AssignLiteralCellIntegrator(token,sid,"1;1","17");
		literal.execute();
		
		AssignReferenceCellIntegrator reference = new AssignReferenceCellIntegrator(token,sid,"1;2","1;1");
		reference.execute();
		
		
		AssignBinaryFunctionToCellIntegrator function = new AssignBinaryFunctionToCellIntegrator(token,sid,"1;3","=ADD(1;1,3)");
		function.execute();
		
		GetSpreadSheetContentIntegrator content = new GetSpreadSheetContentIntegrator(token, sid);
		content.execute();
		
		
		ExportDocumentIntegrator export = new ExportDocumentIntegrator(token, sid);
		export.execute();
		
        
        ImportDocumentIntegrator impIntegrator = new ImportDocumentIntegrator(token, sid);
		impIntegrator.execute();
		
		
		GetSpreadSheetContentIntegrator ndcontent = new GetSpreadSheetContentIntegrator(token, sid+1);
		ndcontent.execute();
	
	}
}