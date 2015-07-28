package pt.tecnico.bubbledocs.service;


import pt.tecnico.bubbledocs.domain.*;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;

// add needed import declarations

public class ImportDocument extends BubbleDocsService {
    private byte[] _docXML;
    private User _user;


    public ImportDocument(User u,byte[] contents) {
    	_docXML = contents;
    	_user = u;
    }

    @Override
    protected void dispatch() throws BubbleDocsException{
    		
    	BubbleDocs.getInstance();
    	
    	org.jdom2.Document doc = BubbleDocs.convertByteToXML(_docXML);
    	BubbleDocs.convertFromXML(_user, doc);
    	
    	
    }
    
    public byte[] getDocXML(){
    	return _docXML;
    }
    
    public void setDocXML(byte[] doc){
    	_docXML=doc;
    }
}
