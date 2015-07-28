package pt.tecnico.ulisboa.sdis.id.handler;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;

import java.security.Key;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public abstract class SDHandler  implements SOAPHandler<SOAPMessageContext> {

	private static byte[] Ticket;
	
	private static Key sessionKey;
	
	private static byte[] N;
	
	public static byte[] getN(){
		return N;
	}
	
	public static void setN(byte[] NN){
		N = NN;
	}
	
	public static Key getSessionKey() {
		return sessionKey;
	}

	public static void setSessionKey(Key sessionKey) {
		SDHandler.sessionKey = sessionKey;
	}

	private static Key clientKey;
	
	
	
	public static Key getClientKey() {
		return clientKey;
	}

	public static void setClientKey(Key clientKey) {
		SDHandler.clientKey = clientKey;
	}

	
	
	public static byte[] getTicket() {
		return Ticket;
	}

	public static void setTicket(byte[] ticket) {
		Ticket = ticket;
	}
	
	

	public abstract void close(MessageContext arg0);

	public abstract boolean handleFault(SOAPMessageContext arg0);

	public abstract boolean handleMessage(SOAPMessageContext arg0);

	public abstract Set<QName> getHeaders();

}
