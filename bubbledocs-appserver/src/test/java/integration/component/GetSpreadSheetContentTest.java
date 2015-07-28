package integration.component;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.tecnico.bubbledocs.domain.CellInteger;
import pt.tecnico.bubbledocs.domain.CellRef;
import pt.tecnico.bubbledocs.domain.Spreadsheet;
import pt.tecnico.bubbledocs.exception.InvalidSpreadIDException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;
import pt.tecnico.bubbledocs.service.GetSpreadSheetContent;

public class GetSpreadSheetContentTest extends BubbleDocsServiceTest {


	private String alice, abreu;
	private int sid, esid;

	private static final String WRONG_USERNAME = "Allll";
	private static final String USERNAME = "Alice";
	private static final String EMAIL ="alice@tenico.pt";
	private static final String SPREADSHEET_NAME = "spread";
	private static final Integer WRONG_SID = 999999;


	@Override
	public void populate4Test() {

		createUser(USERNAME,EMAIL, "AliceName");
		createUser(WRONG_USERNAME,EMAIL, "alllllname");
		
		alice = addUserToSession("Alice");
		abreu = addUserToSession("Allll");

		Spreadsheet s = createSpreadSheet(getUserFromUsername(USERNAME), SPREADSHEET_NAME, 10, 10);
		s.getCell(1, 1).setConteudo(new CellInteger(2));
		s.getCell(1, 2).setConteudo(new CellRef(1, 1));
		s.getCell(1, 3).setConteudo(new CellInteger(-1));
		sid = s.getSID();
		System.out.println("SID ->"+sid);
		Spreadsheet es = createSpreadSheet(getUserFromUsername(USERNAME), SPREADSHEET_NAME+1, 10, 10);
		
		esid = es.getSID();
		System.out.println("ESID ->"+esid);
	}

	@Test
	public void success(){

		String[][] spread;
		String[][] emptySpread;
		Integer l,c;

		//TEST spreadsheet with content
		GetSpreadSheetContent service = new GetSpreadSheetContent(alice, sid);
		service.execute();

		spread = service.getSpreadMatrix();

		for(l=0; l< 10; l++ ){

			for(c =0; c < 10 ; c++){

				if(l == 1 && c == 1){
					if(!(spread[l][c].equals(new String("2"))))
						fail();
				}
				else {
					if(l == 1 && c == 2){
						if(!spread[l][c].equals(new String("2")))
							fail();
					}
					else {
						if(l == 1 && c == 3){
							if(!spread[l][c].equals(new String("-1")))
								fail();
						}
						else{
							assertEquals(spread[l][c], "");
						}
					}
				}
			}
		}

		//TEST empty spreadsheet
		GetSpreadSheetContent service1 = new GetSpreadSheetContent(alice,esid);
		service1.execute();

		emptySpread = service1.getSpreadMatrix();

		for(l=0; l< 10; l++ ){
			for(c =0; c < 10 ; c++){
				assertEquals(emptySpread[l][c], "");
			}

		}

	}

	
	@Test(expected = UnauthorizedOperationException.class)
	public void canRead(){
		GetSpreadSheetContent service = new GetSpreadSheetContent(abreu,sid);
		service.execute();
	}

	@Test(expected = InvalidSpreadIDException.class)
	public void WrongSpreadID(){
		GetSpreadSheetContent service = new GetSpreadSheetContent(alice,WRONG_SID);
		service.execute();
	}

	@Test(expected = UserNotInSessionException.class)
	public void notInSession(){
		removeUserFromSession(alice);
		GetSpreadSheetContent service = new GetSpreadSheetContent(alice, sid);
		service.execute();
	}


}
