package pt.ulisboa.tecnico.sdis.store.handler;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.security.Key;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;
import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.Set;

import javax.xml.ws.BindingProvider;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPBody;
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

import org.w3c.dom.Attr;
import org.w3c.dom.Document;

import com.sun.xml.ws.developer.JAXWSProperties;

import pt.tecnico.ulisboa.sdis.id.SymKey;
import pt.ulisboa.tecnico.sdis.store.ws.Ticket;

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

public class StoreRequestHandler extends SDHandler{

	public static final String CONTEXT_PROPERTY = "my.property";

	//
	// Handler interface methods
	//
	public Set<QName> getHeaders() {
		return null;
	}

	public boolean handleMessage(SOAPMessageContext smc) {
		Boolean outboundElement = (Boolean) smc
				.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		try {
			// get SOAP envelope
			SOAPMessage msg = smc.getMessage();
			SOAPPart sp = msg.getSOAPPart();
			SOAPEnvelope se = sp.getEnvelope();


			if (!outboundElement.booleanValue()) {


				// Incoming message from client. Need to store N that comes in header
				// and associate client key with user

				System.out.println("\n#######################\nREQUEST FROM CLIENT\n#######################");
				SOAPHeader header = se.getHeader();
				if (header == null)
					se.addHeader();

				Iterator headerElements = header.getChildElements();
				if (headerElements == null)
					return false;
				// GET TICKET FROM HEADER
				SOAPHeaderElement ticket = (SOAPHeaderElement) headerElements.next();

				//Decipher and create ticket
				byte[] ticketB = parseBase64Binary(ticket.getValue());

				Key servKey = SymKey.read("SD-STORE.key");

				ticketB = SymKey.decrypt(ticketB, servKey);
				Ticket tick = new Ticket(ticketB);

				//GET AUTH FROM HEADER
				SOAPHeaderElement autenticator = (SOAPHeaderElement) headerElements.next(); 
				
				Key sessionKey = tick.get_sessionKey();
				byte[] aux = parseBase64Binary(autenticator.getValue());
				aux = SymKey.decrypt(aux, sessionKey);
				String auxS = new String(aux);
				String[] auxSA = auxS.split("\n");
				String data = auxSA[0];
				String user = auxSA[1];
				// GET MAC FROM HEADER
				SOAPHeaderElement MAC = (SOAPHeaderElement) headerElements.next(); 

				aux = parseBase64Binary(MAC.getValue());
				aux = SymKey.decrypt(aux, sessionKey);
				
				System.out.println("MAC received "+printBase64Binary(aux));
				String mac = printBase64Binary(aux);
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				msg.writeTo(out);
		        String toCiphee = new String(out.toByteArray());
				String[] tmp = toCiphee.split("/SOAP-ENV:Header>");
				toCiphee = tmp[1];
				byte[] toCipher = toCiphee.getBytes("UTF-8");
				toCipher = SymKey.code(toCipher);
				System.out.println("Hash got..."+printBase64Binary(toCipher));
				String got = printBase64Binary(toCipher);
				
			//	if (!tick.checkValidaty(user, data))
				//	return false;
				
				if (!mac.equals(got)){
					System.out.println("Inconsistent MAC");
					return false;
				}

				

				
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