package pt.tecnico.ulisboa.sdis.id;

import java.security.Key;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.jdom2.*;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.apache.commons.lang3.time.DateUtils;

public class Ticket {
	
	private Date date1;
	private Date date2;
	
	private String _userId;
	private String _service;
	
	private Key _sessionKey;
	
	public Key get_sessionKey() {
		return _sessionKey;
	}


	public Ticket(String userId, String service){
		_userId = userId;
		_service = service;
		date1 = new Date();
		date2 = DateUtils.addHours(date1, 1);
		
		byte[] auxKey = SDIdImpl.generateRandomPass();
		try {
			_sessionKey = new SecretKeySpec(SymKey.code(auxKey), "AES");
			} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public byte[] toByteArray(){
		DateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Element ticket = new Element("ticket");
		
		Element dateI = new Element("dateI");
		dateI.addContent(dateForm.format(date1));
		ticket.addContent(dateI);
		
		Element dateF = new Element("dateF");
		dateF.addContent(dateForm.format(date2));
		ticket.addContent(dateF);
		
		
		Element userID = new Element("UserID");
		userID.addContent(_userId);
		ticket.addContent(userID);
		

		Element service = new Element("Service");
		service.addContent(_service);
		ticket.addContent(service);
		

		Element sessionKey = new Element("SessionKey");
		sessionKey.addContent(SDIdImpl.stringKey(_sessionKey));
		ticket.addContent(sessionKey);
		
		org.jdom2.Document jdomDoc = new org.jdom2.Document();
		jdomDoc.setRootElement(ticket);
		
		XMLOutputter xml = new XMLOutputter();
		xml.setFormat(Format.getRawFormat());
		String res = xml.outputString(jdomDoc);
		System.out.println(res);
		return res.getBytes();
		
	}

}
