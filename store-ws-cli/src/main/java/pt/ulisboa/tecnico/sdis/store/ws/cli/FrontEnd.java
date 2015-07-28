package pt.ulisboa.tecnico.sdis.store.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.store.ws.CapacityExceeded_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.CreateDocResponse;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists;
import pt.ulisboa.tecnico.sdis.store.ws.DocAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.DocUserPair;
import pt.ulisboa.tecnico.sdis.store.ws.ListDocsResponse;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore;
import pt.ulisboa.tecnico.sdis.store.ws.SDStore_Service;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist;
import pt.ulisboa.tecnico.sdis.store.ws.UserDoesNotExist_Exception;
import pt.ulisboa.tecnico.sdis.store.ws.uddi.UDDINaming;

public class FrontEnd {


	private int RAQ = 0;
	private int WAQ = 0;
	private boolean docExists = false;
	private boolean userDoesNotExist = false;
	private int WT = 2;
	private int RT = 2;
	private int N = 3;
	private String name = null;
	private String uddiURL = null;

	private SDStore_Service service = null;
	private SDStore port = null;

	private static HashMap<String,String> replicas = new HashMap<String,String>();

	public FrontEnd(String url,String name) throws JAXRException{
		this.name=name;
		this.uddiURL=url;
		execute(url,name);
	}




	public void execute(String uddiUrl,String name) throws JAXRException{

		System.out.printf("Contacting UDDI at %s%n", uddiURL);
		UDDINaming uddiNaming = new UDDINaming(uddiURL);

		String endpointAddress = uddiNaming.lookup(name);

		if (endpointAddress == null) {
			System.out.println("Not found!");
			return;
		} else {
			System.out.printf("Found %s%n", endpointAddress);
		}

		System.out.println("Creating stub ...");		
		service = new SDStore_Service();
		port = service.getSDStoreImplPort();

		System.out.println("Setting endpoint address ...");

		replicas.put(name, endpointAddress);


		//call in replicas
		for(int i = 1;i<=N;i++){

			endpointAddress = uddiNaming.lookup(name+new Integer(i).toString());

			replicas.put(name+new Integer(i).toString(), endpointAddress);
			System.out.println("Endpoint "+endpointAddress+ "criado");

		}

	}

	public void reset(){
		String endpointAddress = replicas.get(name);
		service = new SDStore_Service();
		port = service.getSDStoreImplPort();

		System.out.println("Setting endpoint address ...");
		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);

		port.reset();

		//call in replicas
		for(int i = 1;i<=N;i++){
			endpointAddress = replicas.get(name+new Integer(i).toString());

			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
			port.reset();
		}
	}


	public void createDoc(DocUserPair docUserPair)throws DocAlreadyExists_Exception, JAXRException{


		String endpointAddress = replicas.get(name);
		service = new SDStore_Service();
		port = service.getSDStoreImplPort();
		System.out.println("Setting endpoint address ...");
		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
		WAQ = 0;
		port.createDocAsync(docUserPair,new AsyncHandler<CreateDocResponse>() {
			public void handleResponse(Response<CreateDocResponse> response) {
				try{
					CreateDocResponse wait = response.get();
					WAQ++;
				
				} catch (InterruptedException e) {
					System.out.println("Caught execution exception.");
					System.out.print("Cause: ");
					System.out.println(e.getCause());
					Exception ex = (Exception) e.getCause();
				} catch (ExecutionException e) {
					System.out.println("Caught execution exception.");
					System.out.print("Cause: ");
					System.out.println(e.getCause());
					Exception ex = (Exception) e.getCause();
					if (e.getCause().toString().contains("DocAlreadyExists"))
						docExists = true;
				}
			}

		});

		//call in replicas
		for(int i = 1;i<=N;i++){
			endpointAddress = replicas.get(name+new Integer(i).toString());

			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
			port.createDocAsync(docUserPair,new AsyncHandler<CreateDocResponse>() {

				public void handleResponse(Response<CreateDocResponse> response){
					try{
						CreateDocResponse wait = response.get();
						WAQ++;
					}
					catch (InterruptedException e) {
						System.out.println("Caught execution exception.");
						System.out.print("Cause: ");
						System.out.println(e.getCause());
						Exception ex = (Exception) e.getCause();
					}catch (ExecutionException e) {
						System.out.println("Caught execution exception.");
						System.out.print("Cause: ");
						System.out.println(e.getCause());
						if (e.getCause().toString().contains("DocAlreadyExists"))
							docExists = true;
					}
				}



			});

		}	
		while(WAQ<WT){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (docExists){
				docExists = false;
				throw new DocAlreadyExists_Exception("Document already exists", new DocAlreadyExists());
			}
		}
	}

	public List<String> listDocs(String userId) throws UserDoesNotExist_Exception, JAXRException {

		final List<String>  res = new ArrayList<String>();


		System.out.printf("Contacting UDDI at %s%n", uddiURL);
		UDDINaming uddiNaming = new UDDINaming(uddiURL);

		service = new SDStore_Service();
		port = service.getSDStoreImplPort();

		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();

		String endpointAddress = uddiNaming.lookup(name);

		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
		res.addAll(port.listDocs(userId));
		RAQ = 0;
		port.listDocsAsync(userId,new AsyncHandler<ListDocsResponse>() {

			public void handleResponse(Response<ListDocsResponse> response) {
				try {
					RAQ++;
					List<String> list = response.get().getDocumentId();
					res.addAll(list);
				} catch (InterruptedException e) {
					throw new RuntimeException(e.getCause());
				} catch (ExecutionException e) {
					System.out.println("Caught execution exception.");
					System.out.print("Cause: ");
					System.out.println(e.getCause());
					if (e.getCause().toString().contains("UserDoesNotExist"))
						userDoesNotExist = true;
				}
			}
		});


		for(int i = 1;i<=N;i++){

			endpointAddress = uddiNaming.lookup(name+new Integer(i).toString());
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);

			port.listDocsAsync(userId, new AsyncHandler<ListDocsResponse>() {

				public void handleResponse(Response<ListDocsResponse> response) {
					try {
						
						RAQ++;
						List<String> list = response.get().getDocumentId();
						res.addAll(list);
						

					}  catch (InterruptedException e) {
						throw new RuntimeException(e.getCause());
					} catch (ExecutionException e) {
						System.out.println("Caught execution exception.");
						System.out.print("Cause: ");
						System.out.println(e.getCause());
						if (e.getCause().toString().contains("UserDoesNotExist"))
							userDoesNotExist = true;
					}
				}
			});
		}

		while(RAQ<RT){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (userDoesNotExist){
				userDoesNotExist = false;
				throw new UserDoesNotExist_Exception("User does not exist", new UserDoesNotExist());
			}
		}


		Set<String> hs = new HashSet<String>();
		hs.addAll(res);
		res.clear();
		res.addAll(hs);

		return res;

	}




	public void store(DocUserPair docUserPair, byte[] contents)
			throws CapacityExceeded_Exception, DocDoesNotExist_Exception,
			UserDoesNotExist_Exception {
		String endpointAddress = replicas.get(name);
		service = new SDStore_Service();
		port = service.getSDStoreImplPort();
		System.out.println("Setting endpoint address ...");
		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
		port.store(docUserPair,contents);
	}


	public byte[] load(DocUserPair docUserPair)
			throws UserDoesNotExist_Exception, DocDoesNotExist_Exception {
		String endpointAddress = replicas.get(name);
		service = new SDStore_Service();
		port = service.getSDStoreImplPort();
		System.out.println("Setting endpoint address ...");
		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
		
		return port.load(docUserPair);
	}


	public int getRAQ() {
		return RAQ;
	}


	public void setRAQ(int aQ) {
		RAQ = aQ;
	}

	public void incRAQ(){
		System.out.println("Chegou RAQ.....");
		setRAQ(getRAQ()+1);
	}

	public void incWAQ(){
		System.out.println("Chegou WAQ.....");
		setWAQ(getWAQ()+1);
	}


	public int getWAQ() {
		return WAQ;
	}


	public void setWAQ(int wAQ) {
		WAQ = wAQ;
	}

}
