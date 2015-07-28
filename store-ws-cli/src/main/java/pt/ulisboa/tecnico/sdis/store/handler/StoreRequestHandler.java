package pt.ulisboa.tecnico.sdis.store.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringBufferInputStream;
import java.io.StringWriter;
import java.security.Key;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;




import org.w3c.dom.Document;

import pt.ulisboa.tecnico.sdis.store.ws.cli.*;
import pt.tecnico.ulisboa.sdis.id.SymKey;

/**
 *  This SOAPHandler shows how to set/get values from headers in
 *  inbound/outbound SOAP messages.
 *
 *  A header is created in an outbound message and is read on an
 *  inbound message.
 *
 *  The value that is read from the header
 *  is placed in a SOAP message context property
 *  that can be accessed by other handlers or by the application.
 */

public class StoreRequestHandler extends SDHandler {

	public static final String CONTEXT_PROPERTY = "my.property";

	//
	// Handler interface methods
	//
	public Set<QName> getHeaders() {
		return null;
	}

	public boolean handleMessage(SOAPMessageContext smc) {
		System.out.println("AddHeaderHandler: Handling message.");

		Boolean outboundElement = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		Boolean testing = SDStoreCli.isTesting();
		
		

		try {
			if (outboundElement.booleanValue() && !testing) {
				
				//  OUTBOUND  MESSAGE

				System.out.println("\n#######################\n\n   REQUEST TO SERVER\n\n#######################");
				System.out.println("My session key is "+printBase64Binary(SDStoreCli.getSessionKey().getEncoded()));
				// get SOAP envelope
				SOAPMessage msg = smc.getMessage();
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope se = sp.getEnvelope();
				SOAPBody sb = se.getBody();
				SOAPHeader header = se.getHeader();
				
				
				//Get header
				if (header == null)
					se.addHeader();
				
				//Add header elements
				Name ticket = se.createName("ticket", "prefix", "namespace");
				header.addChildElement(ticket);
				Name autenticator = se.createName("autenticator", "prefix", "namespace");
				header.addChildElement(autenticator);
				Name mac = se.createName("MAC", "prefix", "namespace");
				header.addChildElement(mac);
				
				Iterator elements = header.getChildElements();
				
				// Add value to ticket
				SOAPHeaderElement ele = (SOAPHeaderElement) elements.next();
				byte[] ticketB = SDStoreCli.getTicket();
				String ticketS = printBase64Binary(ticketB);
				ele.setValue(ticketS);
				
				
				// Add value to autenticator
				ele = (SOAPHeaderElement) elements.next();
				Date now = new Date();
				DateFormat dateF = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				// Generate autenticator string with current time and username
				// Then encrypt it with sessionKey
				String autentS = dateF.format(now)+"\n"+SDStoreCli.getUserName();
				byte[] autent = autentS.getBytes();
				Key sessionKey = SDStoreCli.getSessionKey();
				autent = SymKey.encrypt(autent, sessionKey);
				autentS = printBase64Binary(autent);
				ele.setValue(autentS);
				
				
				// Add value to MAC by hasing the message body and ciphering with session key
				ele = (SOAPHeaderElement) elements.next();
				
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				SOAPBody body = se.getBody();
//				Document bod = body.extractContentAsDocument();
//				TransformerFactory tf = TransformerFactory.newInstance();
//				Transformer transformer = tf.newTransformer();
//				transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
//				StringWriter writer = new StringWriter();
//				transformer.transform(new DOMSource(bod), new StreamResult(writer));
//				String toCiphe = writer.getBuffer().toString().replaceAll("\n|\r", "");
		        msg.writeTo(out);
		        String toCiphee = new String(out.toByteArray());
				String[] tmp = toCiphee.split("Header/>");
				toCiphee = tmp[1];
				byte[] toCipher = toCiphee.getBytes("UTF-8");
				toCipher = SymKey.code(toCipher);
				System.out.println("Sending "+printBase64Binary(toCipher));
				toCipher = SymKey.encrypt(toCipher, sessionKey);
				String MAC = printBase64Binary(toCipher);
				ele.setValue(MAC);
			}
			
		} catch (Exception e) {
			System.out.print("Caught exception in handleMessage: ");
			System.out.println(e);
			e.printStackTrace();
			System.out.println("Continue normal processing...");
		}
		
		return true;
	}

	public boolean handleFault(SOAPMessageContext smc) {
		System.out.println("Ignoring fault message...");
		return true;
	}

	public void close(MessageContext messageContext) {
	}

}