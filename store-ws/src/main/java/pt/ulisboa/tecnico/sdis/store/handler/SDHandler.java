package pt.ulisboa.tecnico.sdis.store.handler;

import java.security.Key;
import java.util.HashMap;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import pt.tecnico.ulisboa.sdis.id.SymKey;

public abstract class SDHandler implements SOAPHandler<SOAPMessageContext> {
	
	private static HashMap<String, String> Ns = new HashMap<String, String>();
	private HashMap<String, String> Clients;
	private static Key serviceKey = null;
	
	public String getClientFromKey(String key){
		return Clients.get(key);
	}
	
	public void putClient(String key, String user){
		Clients.put(key, user);
	}
	
	public static Key getServiceKey(){
		try {
			return SymKey.read("SD-STORE.key");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void setServiceKey(Key key){
		serviceKey = key;
	}

	public HashMap<String, String> getNs() {
		return Ns;
	}

	public void setNs(HashMap<String, String> ns) {
		Ns = ns;
	}
	
	public String getN(String user){
		return Ns.get(user);
	}

	public abstract void close(MessageContext arg0);

	public abstract boolean handleFault(SOAPMessageContext arg0);

	public abstract boolean handleMessage(SOAPMessageContext arg0);

	public abstract Set<QName> getHeaders();

}
