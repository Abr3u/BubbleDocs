
package pt.tecnico.bubbledocs;


import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.transaction.SystemException;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import pt.tecnico.bubbledocs.domain.*;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.TransactionManager;
import pt.tecnico.bubbledocs.service.*;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;


public class BubbleDocsApplication {

	public static void main(String[] args) throws Exception{
		System.out.println("Welcome to the BubbleDocs application!");
		
		//Spreadsheet sp = null;	
		//org.jdom2.Document doc = null;
		TransactionManager tm = FenixFramework.getTransactionManager();
		boolean committed = false;

		try {
			tm.begin();
		
			BubbleDocs bd = BubbleDocs.getInstance();
			
			setupIfNeed(bd);
			
			
//			//PRINT USER INFO
//			System.out.println("\n\n\n\n\n\n\n WELCOME TO BUBBLEDOCS APPLICATION \n\n\n");
//			for (User u: bd.getUserSet()){
//				System.out.println("Username: "+u.getUsername()+"\nName: "+u.getName()+"\nPassword: "+u.getPassword()+"\n\n\n");
//			}
//			tm.commit();
//			tm.begin();
//			User u = bd.getUserByUsername("pf");
//			System.out.println("Spreads from user "+u.getUsername());
//			for (Spreadsheet s: u.getCspreadsheetSet()){
//				System.out.println(s.getName());
//			}
//			System.out.println("\n");
//			tm.commit();
//			tm.begin();
//			u = bd.getUserByUsername("ra");
//			System.out.println("Spreads from user "+u.getUsername());
//			for (Spreadsheet s: u.getCspreadsheetSet()){
//				System.out.println(s.getName());
//			}
//			System.out.println("\n\n\n");
//			tm.commit();
//			tm.begin();
//			u = bd.getUserByUsername("pf");
//			System.out.println("Deleting Spreadsheet from user pf");
//			
//			LoginUser loginservice = new LoginUser("pf", "DEFAULT");
//			loginservice.execute();
//			String pftoken = loginservice.getUserToken();
//			byte[] exported = null;
//			for (Spreadsheet s: u.getCspreadsheetSet()){
//				ExportDocument expservice = new ExportDocument(pftoken, s.getSID());
//				expservice.execute();
//				exported = expservice.getDocXML();
//				System.out.println(new String(expservice.getDocXML()));
//				bd.deleteSpread(s.getName(), u);
//			}
//			System.out.println("\n\n");
//			tm.commit();
//			tm.begin();
//			System.out.println("Folhas de calculo do user pf");
//			for (Spreadsheet s: u.getCspreadsheetSet()){
//				System.out.println(s.getName());
//			}
//			tm.commit();
//			tm.begin();
//			System.out.println("\n\n");
//			System.out.println("Antes da importacao");
//			convertFromXML(convertByteToXML(exported));
//			System.out.println("Depois da importacao");
//			System.out.println("Folhas de calculo do user pf");
//			for (Spreadsheet s: u.getCspreadsheetSet()){
//				System.out.println("Spread"+s.getName());
//			}
//			tm.commit();
//			tm.begin();
//			
//			for (Spreadsheet s: u.getCspreadsheetSet()){
//				ExportDocument expservice = new ExportDocument(pftoken, s.getSID());
//				expservice.execute();
//				exported = expservice.getDocXML();
//				System.out.println(new String(expservice.getDocXML()));
//			}
//			System.out.println("\n\n");
//			tm.commit();

			committed = true;

		}catch ( Exception ex) {
			System.err.println("Error in execution of transaction: " + ex);
		} finally {
			if (!committed) 
				try {
					tm.rollback();
				} catch (SystemException ex) {
					System.err.println("Error in roll back of transaction: " + ex);
				}
		}



	}

	
	// removes the first person of the PhoneBook application.
	@Atomic
	static void removeAllUsers() {
		BubbleDocs bd = BubbleDocs.getInstance();

		for (User u : bd.getUserSet()) {
			u.delete();
		}
	}

	// setup the initial state of bubbledocs is empty
	@Atomic
	private static void setupIfNeed(BubbleDocs bd) {
		if (bd.getUserByUsername("root") == null)
			SetupDomain.populateDomain();
	}
}