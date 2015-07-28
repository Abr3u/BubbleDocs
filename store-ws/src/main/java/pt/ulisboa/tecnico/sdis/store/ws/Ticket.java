package pt.ulisboa.tecnico.sdis.store.ws;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Key;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.time.DateUtils;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pt.tecnico.ulisboa.sdis.id.SymKey;
import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

public class Ticket {

	private Date date1;
	private Date date2;

	private String _userId;
	private String _service;

	private Key _sessionKey;

	public Key get_sessionKey() {
		return _sessionKey;
	}


	public Ticket(byte[] toGenerate){

		Document doc = convertByteToXML(toGenerate);

		Element ele = doc.getRootElement();
		_userId = ele.getChild("UserID").getValue();

		String keyTemp = ele.getChild("SessionKey").getValue(); 
		byte[] aux = parseBase64Binary(keyTemp);
		
		String servTemp = ele.getChild("Service").getValue();
		_service = servTemp;

		_sessionKey = new SecretKeySpec(aux, "AES");

		String dateI = ele.getChild("dateI").getValue(); 
		String dateF = ele.getChild("dateF").getValue();


		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dateStr = null;
		try {
			dateStr = formatter.parse(dateI);

			String formattedDate = formatter.format(dateStr);
		
			this.date1 = formatter.parse(formattedDate);
			this.date2 =  formatter.parse(formattedDate);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public boolean checkValidaty(String user, String toCheck){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dateStr = null;
		try {
			dateStr = formatter.parse(toCheck);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(dateStr.before(date1) || dateStr.after(date2)) return false;
		
		if(user.equals(_userId)) return true;
		
		return false;
		
	}
	
	@SuppressWarnings("restriction")
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
		sessionKey.addContent(printBase64Binary(_sessionKey.getEncoded()));
		ticket.addContent(sessionKey);

		org.jdom2.Document jdomDoc = new org.jdom2.Document();
		jdomDoc.setRootElement(ticket);

		XMLOutputter xml = new XMLOutputter();
		xml.setFormat(Format.getPrettyFormat());
		String res =  xml.outputString(jdomDoc);
		return res.getBytes();

	}


	public static String printDomainInXML(org.jdom2.Document jdomDoc) {
		XMLOutputter xml = new XMLOutputter();
		xml.setFormat(Format.getPrettyFormat());
		return xml.outputString(jdomDoc);
	}


	public static org.jdom2.Document convertByteToXML(byte[] bytes){
		SAXBuilder sb = new SAXBuilder();
		ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
		Document doc = null;
		try {
			doc = sb.build(stream);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
}
