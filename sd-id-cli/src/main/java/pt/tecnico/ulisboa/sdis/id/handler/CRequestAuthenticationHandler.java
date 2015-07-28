package pt.tecnico.ulisboa.sdis.id.handler;

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
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

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

public class CRequestAuthenticationHandler extends SDHandler {

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
		Boolean requestAuthOperation = smc.get(MessageContext.WSDL_OPERATION).toString().contains("requestAuthentication");

		outboundElement = requestAuthOperation && outboundElement;


		try {
			if (!outboundElement.booleanValue()) {


				System.out.println("\n#######################\n\n   REPLY FROM SERVER\n\n#######################");
				// get SOAP envelope header
				SOAPMessage msg = smc.getMessage();
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope se = sp.getEnvelope();
				SOAPHeader sh = se.getHeader();

				// check header
				if (sh == null) {
					System.out.println("Header not found.");
					return true;
				}

				//Parse received encrypted bytes
				byte[] ticket = parseBase64Binary(sh.getAttribute("ticket"));
				byte[] sessionKey = parseBase64Binary(sh.getAttribute("sessionKey"));
				byte[] N = parseBase64Binary(sh.getAttribute("N"));
				
				
				//Decrypt N and sessionKey
				System.out.println("Decrypting...");
				System.out.println("Using key -> "+printBase64Binary(getClientKey().getEncoded()));
				N = SymKey.decrypt(N, getClientKey());
				sessionKey = SymKey.decrypt(sessionKey, getClientKey());
				

				System.out.println("Ticket = "+new String(ticket));
				System.out.println("clientBytes = "+new String(sessionKey));
				System.out.println("N = "+new String(N));
				setTicket(ticket);
				
				
				SecretKeySpec spec = new SecretKeySpec(sessionKey, "AES");
				setSessionKey(spec);
				
				

			}
			else{
				
				//  OUTBOUND  MESSAGE

				System.out.println("\n#######################\n\n   REQUEST TO SERVER\n\n#######################");
				byte[] bytearray = null;
				Date now = new Date();
				DateFormat dateF = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
				bytearray = dateF.format(now).getBytes();
				
				// get SOAP envelope
				SOAPMessage msg = smc.getMessage();
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope se = sp.getEnvelope();
				SOAPBody sb = se.getBody();
				SOAPHeader header = se.getHeader();
				
				
				//Get header
				if (header == null)
					se.addHeader();
				
				
				Name n = se.createName("N", "prefix", "namespace");
				header.addChildElement(n);
				
				Iterator elements = header.getChildElements();
				
				
				SOAPHeaderElement ele = (SOAPHeaderElement) elements.next();
				System.out.println("Found child "+ele.getLocalName()); //Found N
				System.out.println("Putting "+new String(bytearray));
				ele.setValue(new String(bytearray));
				
				
				Iterator bodyIt = sb.getChildElements();
				SOAPBodyElement element = (SOAPBodyElement) bodyIt.next();
				bodyIt = element.getChildElements();
				element = (SOAPBodyElement) bodyIt.next();
				element = (SOAPBodyElement) bodyIt.next();
				
				byte[] pass = parseBase64Binary(element.getValue());
				SecretKeySpec spec = new SecretKeySpec(SymKey.code(pass), "AES");
				setClientKey(spec);
				
			}
			
		} catch (Exception e) {
			System.out.print("Caught exception in handleMessage: ");
			System.out.println(e);
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