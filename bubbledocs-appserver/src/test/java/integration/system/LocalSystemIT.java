package integration.system;


import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.service.ExportDocument;
import pt.tecnico.bubbledocs.service.ImportDocument;
import pt.tecnico.bubbledocs.service.integration.*;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;


public class LocalSystemIT extends BubbleDocsIT{

	private static final String USERNAME = "Abreu";
	private static final String PASS = "password";
	private static final String EMAIL = "abreu@email.com";
	private static final String NAME = "AbreuName";


	private String token;
	private Integer sid;
	private Spreadsheet _spread;
	private byte[] exportedBytes;
	private String auxTok;
	private Integer auxSID;
	private User aux;
	private Spreadsheet spreadaux;

	public void populate4Test() {
		aux = createUser(USERNAME,EMAIL, NAME);
		setPasswordForUser(USERNAME, PASS);

		spreadaux = createSpreadSheet(aux, "spreadAux", 2, 2);
		auxSID = spreadaux.getSID();
		auxTok = addUserToSession(aux.getUsername());
		ExportDocument expAux = new ExportDocument(auxTok, spreadaux.getSID());
		expAux.execute();
		exportedBytes = expAux.getDocXML();
		System.out.println("exported bytes "+new String(exportedBytes));
		
	}

	@Test
	public void success(@Mocked final IDRemoteServices idservice,@Mocked final StoreRemoteServices storeservice) throws Exception{

		new Expectations(){{
			
			new LoginUserIntegrator(USERNAME, PASS);
			new IDRemoteServices();
			idservice.loginUser(USERNAME, PASS);
			new CreateSpreadSheetIntegrator(token,"MinhaSpread",10,10);
			new AssignLiteralCellIntegrator(token,auxSID+1,"1;1","17");
			new AssignReferenceCellIntegrator(token,auxSID+1,"1;2","1;1");
			new AssignBinaryFunctionToCellIntegrator(token,auxSID+1,"1;3","=ADD(1;1,3)");
			new GetSpreadSheetContentIntegrator(token, auxSID+1);
			
			new ExportDocumentIntegrator(token, auxSID+1);
			new ExportDocument(token,auxSID+1);
			new StoreRemoteServices();
			
			new ImportDocumentIntegrator(token, auxSID+1);
			new StoreRemoteServices();
			storeservice.loadDocument(USERNAME, ""+(auxSID+1));
			result = exportedBytes;
			System.out.println("RESULT = "+new String(exportedBytes));
			new ImportDocument(aux,exportedBytes);
			
			new GetSpreadSheetContentIntegrator(token, auxSID+21);
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