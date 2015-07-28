package pt.tecnico.ulisboa.sdis.id;


import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.tecnico.ulisboa.sdis.id.SymKey;
import pt.tecnico.ulisboa.sdis.id.ws.*;
import pt.tecnico.ulisboa.sdis.id.ws.uddi.UDDINaming;

import org.apache.commons.lang3.RandomStringUtils;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;



@WebService(
		endpointInterface="pt.tecnico.ulisboa.sdis.id.ws.SDId", 
		wsdlLocation="SD-ID.1_1.wsdl",
		name="SD-ID.1_1",
		portName="SDIdImplPort",
		targetNamespace= "urn:pt:tecnico:ulisboa:sdis:id:ws",
		serviceName="SDId"
		)
public class SDIdImpl implements SDId{
	
    // Endpoint management

    private String uddiURL = null;
    private String wsName = null;
    private String wsURL = null;

    private Endpoint endpoint = null;
    private UDDINaming uddiNaming = null;

    /** output option **/
    private boolean verbose = true;

    public boolean isVerbose() {
        return verbose;
    }
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    /** constructor with provided UDDI location, WS name, and WS URL */
    public SDIdImpl(String uddiURL, String wsName, String wsURL) {
        this.uddiURL = uddiURL;
        this.wsName = wsName;
        this.wsURL = wsURL;
    }

    /** constructor with provided web service URL */
    public SDIdImpl(String wsURL) {
        if (wsURL == null)
            throw new NullPointerException("Web Service URL cannot be null!");
        this.wsURL = wsURL;
    }
    
    public void start() throws Exception {
        try {
            // publish endpoint
            endpoint = Endpoint.create(this);
            if (verbose) {
                System.out.printf("Starting %s%n", wsURL);
            }
            endpoint.publish(wsURL);
        } catch(Exception e) {
            endpoint = null;
            if (verbose) {
                System.out.printf("Caught exception when starting: %s%n", e);
                e.printStackTrace();
            }
            throw e;
        }
        try {
			// publish to UDDI
			if (uddiURL != null) {
			    if (verbose) {
				    System.out.printf("Publishing '%s' to UDDI at %s%n", wsName, uddiURL);
				}
				uddiNaming = new UDDINaming(uddiURL);
				uddiNaming.rebind(wsName, wsURL);
			}
        } catch(Exception e) {
            uddiNaming = null;
            if (verbose) {
                System.out.printf("Caught exception when binding to UDDI: %s%n", e);
                e.printStackTrace();
            }
            throw e;
        }
    }

    public void awaitConnections() {
        if (verbose) {
            System.out.println("Awaiting connections");
            System.out.println("Press enter to shutdown");
        }
        try {
            System.in.read();
        } catch(IOException e) {
            if (verbose) {
                System.out.printf("Caught i/o exception when awaiting requests: %s%n", e);
            }
        }
    }

    public void stop() {
        try {
            if (endpoint != null) {
                // stop endpoint
                endpoint.stop();
                if (verbose) {
                    System.out.printf("Stopped %s%n", wsURL);
                }
            }
        } catch(Exception e) {
            if (verbose) {
                System.out.printf("Caught exception when stopping: %s%n", e);
            }
        }
		try {
			if (uddiNaming != null) {
				// delete from UDDI
				uddiNaming.unbind(wsName);
				if (verbose) {
				    System.out.printf("Deleted '%s' from UDDI%n", wsName);
				}
			}
		} catch (Exception e) {
		    if (verbose) {
			    System.out.printf("Caught exception when unbinding: %s%n", e);
			}
		}
    }


	
	// SYSTEM IMPLEMENTATION
	


	//DATA structures to mantain users and passwords.
	private static HashMap<String, String> userManager = new HashMap<String,String>(); //username;email
	private static List<String> services = new ArrayList<String>();

	public static void populate() throws Exception{
		userManager.put("alice", "alice@tecnico.pt");
		userManager.put("bruno", "bruno@tecnico.pt");
		userManager.put("carla", "carla@tecnico.pt");
		userManager.put("duarte", "duarte@tecnico.pt");
		userManager.put("eduardo", "alice@tecnico.pt");
		SymKey.write("alice.key", "Aaa1".getBytes());
		SymKey.write("bruno.key", "Bbb2".getBytes());
		SymKey.write("carla.key", "Ccc3".getBytes());
		SymKey.write("duarte.key", "Ddd4".getBytes());
		SymKey.write("eduardo.key", "Eee5".getBytes());
		services.add("SD-STORE");
		
		generateServiceKeys();
		
	}

	private static void generateServiceKeys() throws Exception {
		for (String s: services){
			System.out.println("Generating private key for service -> "+s);
			SymKey.write(s+".key", SymKey.code("SD-STORE".getBytes()));
		}
	}

	public static Key getServiceKey(String service) throws Exception{
		return SymKey.read(service+".key");
	}


	public void createUser(String userId, String emailAddress)
			throws EmailAlreadyExists_Exception, InvalidEmail_Exception,
			InvalidUser_Exception, UserAlreadyExists_Exception {


		System.out.println("@ CREATE USER: "+ userId);

		if (userId == null)
			throw new InvalidUser_Exception("Username invalid", new InvalidUser());
		
		if (emailAddress == null)
			throw new InvalidEmail_Exception("Email is null", new InvalidEmail());

		if(userId.length() < 3 || userId.length() > 8){
			throw new InvalidUser_Exception("Username must have between 3 and 8 characters", new InvalidUser());
		}
		if(!(isValidEmailAddress(emailAddress))){
			throw new InvalidEmail_Exception("Email is not in form: aa@bb.com", new InvalidEmail());
		}

		if(userManager.containsKey(userId)){
			throw new UserAlreadyExists_Exception(userId,new UserAlreadyExists());
		}
		if(userManager.containsValue(emailAddress)){
			throw new EmailAlreadyExists_Exception(emailAddress,new EmailAlreadyExists());
		}

		System.out.println("@WebService::SDId @CreateUser:");

		userManager.put(userId, emailAddress);

		System.out.println("SD-ID-WS@CreateUser::: User:"+userManager.get(userId));

		//Gerar password - getRandomPassword;

		final byte[] pass =  generateRandomPass();
		try {
			System.out.println("Generating random password: "+new String(pass, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			SymKey.write(userId+".key", pass);
		} catch (Exception e) {
			e.printStackTrace();
		}



	}

	public static byte[] generateRandomPass() {

		String s = RandomStringUtils.randomAlphanumeric(8);
		return s.getBytes();
	}

	public void renewPassword(String userId) throws UserDoesNotExist_Exception {

		if(userManager.containsKey(userId)){

			final byte[] pass =  generateRandomPass();
			try {
				System.out.println("Generating random password (renew): "+new String(pass , "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			try {
				SymKey.write(userId+".key", pass);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}else throw new UserDoesNotExist_Exception("Utilizador nao existe", new UserDoesNotExist());
	}

	public void removeUser(String userId) throws UserDoesNotExist_Exception {

		System.out.println("@ REMOVE USER: "+ userId);

		if(userManager.containsKey(userId)){

			userManager.remove(userId);

			File file = new File(userId+".key");

			if(file.delete()){
				System.out.println(file.getName() + " is deleted!");
			}else{
				System.out.println("Delete operation is failed.");
			}


		}else throw new UserDoesNotExist_Exception("Utilizador nao existe", new UserDoesNotExist());

	}

	public byte[] requestAuthentication(String userId, byte[] reserved)
			throws AuthReqFailed_Exception {

		String service = null;
		byte[] saved = null;
		String givenPass = null;
		String readPass = null;
		String N = null;


		System.out.println("@ AUTENTICATION: "+ userId);

		String[] contents = new String(reserved).split("\n");
		service = contents[0];
		N = contents[1];




		if(!userManager.containsKey(userId)){
			throw new AuthReqFailed_Exception("Failed to Login, user does not exist", new AuthReqFailed());
		}
		
		if (!services.contains(service)){
			throw new AuthReqFailed_Exception("Failed to Login, unknown service", new AuthReqFailed());
		}




		try {
			// Fetch key stored from user IDs
			FileInputStream fis = new FileInputStream(userId+".key");
			saved = new byte[fis.available()];
			fis.read(saved);
			fis.close();

			// Create Session Ticket
			Ticket newT = new Ticket (userId, service);	
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] ticketBytes = newT.toByteArray();
			
			Key sessionKey = newT.get_sessionKey();
			Key clientKey = SymKey.read(userId+".key");
			System.out.println("SESSION KEY -> "+printBase64Binary(sessionKey.getEncoded()));
			
			// Have everything necessary for Kerberos, need to encrypt

			//Encrypt ticket with service key
			ticketBytes = SymKey.encrypt(ticketBytes, getServiceKey(service));

			//Encrypt N and sessionKey with ClientKey
			byte[] sKey = SymKey.encrypt(sessionKey.getEncoded(), clientKey);
			byte[] Nbytes = SymKey.encrypt(N.getBytes(), clientKey);

			sKey = printBase64Binary(sKey).getBytes();
			Nbytes = printBase64Binary(Nbytes).getBytes();
			ticketBytes = printBase64Binary(ticketBytes).getBytes();

			outputStream.write(ticketBytes);
			outputStream.write("\n".getBytes());
			outputStream.write(sKey);
			outputStream.write("\n".getBytes());
			outputStream.write(Nbytes);
			outputStream.write("\n".getBytes());
			outputStream.write(ticketBytes);

			return outputStream.toByteArray();
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new AuthReqFailed_Exception("Failed to Login, unknown exception", new AuthReqFailed());
		}


	}


	public String getUserEmail(String userId){
		return this.userManager.get(userId);
	}


	public boolean isValidEmailAddress(String email) {
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		Pattern p = java.util.regex.Pattern.compile(ePattern);
		Matcher m = p.matcher(email);
		return m.matches();
	}
	public String byteArrayToString(byte[] toConvert){

		return new String(toConvert);
	}

	public static HashMap<String, String> getUserManager(){
		return userManager;
	}

	public static String stringKey(Key toPrint){
	
		return printBase64Binary(toPrint.getEncoded());
	}

	
	public void reset(){
		userManager = new HashMap<String,String>();
		try {
			populate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
