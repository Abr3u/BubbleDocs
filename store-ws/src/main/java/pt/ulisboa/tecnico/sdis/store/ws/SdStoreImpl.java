package pt.ulisboa.tecnico.sdis.store.ws;

import java.util.HashMap;
import java.util.List;

import javax.jws.HandlerChain;
import javax.jws.WebService;

import pt.ulisboa.tecnico.sdis.store.ws.*;


@WebService(
		endpointInterface="pt.ulisboa.tecnico.sdis.store.ws.SDStore", 
		wsdlLocation="SD-STORE.1_1.wsdl",
		name="SD-STORE.1_1",
		portName="SDStoreImplPort",
		targetNamespace= "urn:pt:ulisboa:tecnico:sdis:store:ws",
		serviceName="SD-Store"
		)
@HandlerChain(file="/handler-chain.xml")
public class SdStoreImpl implements SDStore {


	private static int nr = 0;
	private int myNr;
	public HashMap<String, Repo> _repositories = new HashMap<String, Repo>();

	public SdStoreImpl(){
		myNr = nr;
		nr++;
		System.out.println("My name is STORE "+myNr);
		_repositories.put("alice", new Repo());
		_repositories.put("bruno", new Repo());
		_repositories.put("carla", new Repo());
		_repositories.put("duarte", new Repo());
		_repositories.put("eduardo", new Repo());
		try {
			DocUserPair aux = new DocUserPair();
			aux.setDocumentId("a1");
			aux.setUserId("alice");
			store(aux, "AAAAAAAAAA".getBytes());
			aux.setDocumentId("a2");
			aux.setUserId("alice");
			store(aux, "aaaaaaaaaa".getBytes());
			aux.setDocumentId("b1");
			aux.setUserId("bruno");
			store(aux, "BBBBBBBBBBBBBBBBBBBB".getBytes());


		} catch (CapacityExceeded_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocDoesNotExist_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserDoesNotExist_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


	public void createDoc(DocUserPair docUserPair)
			throws DocAlreadyExists_Exception {

		if(docUserPair.getDocumentId() != null){
			if(docUserPair.getDocumentId() != ""){
				if(docUserPair.getUserId() != null){ 
					if(docUserPair.getUserId() != "") {

						System.out.println("Creating Document........: "+docUserPair.documentId+" ON MACHINE  "+myNr+"\n");

						Repo rep = _repositories.get(docUserPair.getUserId());

						if (rep == null){
							rep = new Repo();
							_repositories.put(docUserPair.getUserId(), rep);
						}

						if (rep.addNewDocument(docUserPair.getDocumentId()) == false) {
							// fi.setMessage("Document already exists");
							System.out.println("Duplicate document\n");
							throw new DocAlreadyExists_Exception("Document already exists", new DocAlreadyExists());
						}
					}
				}
			}
		}
	}

	public List<String> listDocs(String userId) throws UserDoesNotExist_Exception {
		System.out.println("LIST DOCUMENT");

		if(userId == null || userId.equalsIgnoreCase("") || _repositories.get(userId) == null) {    	
			UserDoesNotExist faultInfo = new UserDoesNotExist();
			faultInfo.setUserId(userId);
			throw new UserDoesNotExist_Exception("User does not exist **", faultInfo);    	
		}     	

		Repo rep = _repositories.get(userId); 
		return rep.listDocs();

	}

	public void store(DocUserPair docUserPair, byte[] contents)
			throws CapacityExceeded_Exception, DocDoesNotExist_Exception,
			UserDoesNotExist_Exception {

		System.out.println("Eu sou o store......");
		String user = docUserPair.getUserId();
		String name = docUserPair.getDocumentId();

		if (!_repositories.containsKey(user)){
			throw new UserDoesNotExist_Exception(user, new UserDoesNotExist());
		}
		this._repositories.get(user).store(name, contents);

	}

	public byte[] load(DocUserPair docUserPair)
			throws UserDoesNotExist_Exception, DocDoesNotExist_Exception {
		System.out.println("Eu sou o LOAD");

		String user = docUserPair.getUserId();
		String name = docUserPair.getDocumentId();

		if (!_repositories.containsKey(user)){
			throw new UserDoesNotExist_Exception("User does not exist", new UserDoesNotExist());
		}
		return _repositories.get(user).load(name);

	}

	public void reset() {
		System.out.println("Clearing........at....."+myNr);
		_repositories.clear();
		// as specified in:
		// http://disciplinas.tecnico.ulisboa.pt/leic-sod/2014-2015/labs/proj/test.html
		{
			Repo rep = new Repo();
			_repositories.put("alice", rep);
		}
		{
			Repo rep = new Repo();
			_repositories.put("bruno", rep);
		}
		{
			Repo rep = new Repo();
			_repositories.put("carla", rep);
		}
		{
			Repo rep = new Repo();
			_repositories.put("duarte", rep);
		}
		{
			Repo rep = new Repo();
			_repositories.put("eduardo", rep);
		}
	}

}
