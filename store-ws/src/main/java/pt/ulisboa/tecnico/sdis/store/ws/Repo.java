package pt.ulisboa.tecnico.sdis.store.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Repo{

	private final int LIMIT = 10024;

	private int currentSize;
	HashMap<String, Doc> _documents = new HashMap<String, Doc>();

	public Repo(){
	}

	public HashMap<String, Doc> get_Documents(){

		return _documents;
	}

	public Doc get_Document(String docId){
		return _documents.get(docId);
	}

	public void set_Document(String docId){
		Doc doc = new Doc(null);
		_documents.put(docId, doc);
	}

	public boolean addNewDocument(String docId) {
		if (this._documents.get(docId) != null) 
			return false;
		_documents.put(docId, new Doc());
		return true;
	}


	public void store(String docId, byte[] contents) 
			throws CapacityExceeded_Exception, DocDoesNotExist_Exception {

		
		
		if(!this._documents.containsKey(docId)) {
			this._documents.put(docId,new Doc());
		}

		_documents.get(docId).set_Content(contents);
	}

	public ArrayList<String> listDocs(){
		return new ArrayList<String>(_documents.keySet());
	}

	public byte[] load(String docId) throws DocDoesNotExist_Exception {
		if(!this._documents.containsKey(docId)){
			DocDoesNotExist faultInfo = new DocDoesNotExist();
			faultInfo.docId = docId;
			throw new DocDoesNotExist_Exception("Document does not exist", faultInfo);
		}

		return this._documents.get(docId).get_Content();
	}

	public boolean hasDoc(String docId) {
		return this._documents.containsKey(docId);
	}
}