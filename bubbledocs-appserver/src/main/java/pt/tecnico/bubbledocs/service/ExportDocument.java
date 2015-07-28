package pt.tecnico.bubbledocs.service;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import pt.tecnico.bubbledocs.domain.*;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;
import pt.tecnico.bubbledocs.exception.InvalidSpreadIDException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;
import pt.tecnico.bubbledocs.exception.UserNotInSessionException;

// add needed import declarations

public class ExportDocument extends BubbleDocsService {
    private byte[] _docXML;
    private String _userToken;
    private int _docId;


    public ExportDocument(String userToken, int docId) {
    	this._userToken = userToken;
    	this._docId = docId;
    }

    @Override
    protected void dispatch() throws BubbleDocsException{
    		try{
    			
    			BubbleDocs bd = BubbleDocs.getInstance();
    			
    			Spreadsheet spreadToExport = bd.searchSpreadBySID(_docId);
    			if (spreadToExport == null)
    				throw new InvalidSpreadIDException();
    			
    			if (_userToken == null || _userToken=="")
    				throw new InvalidArgumentsException();
    			
    			if (!(bd.getSession().isOnline(_userToken)))
    				throw new UserNotInSessionException(_userToken);
    			
    			if (!spreadToExport.canRead(bd.getSession().getUserFromSession(_userToken)))
    				throw new UnauthorizedOperationException();

    			
    			Element ele = spreadToExport.exportToXML();
    			org.jdom2.Document doc = BubbleDocs.convertToXML(ele);
    			ByteArrayOutputStream btstream = new ByteArrayOutputStream();
    			XMLOutputter xmlo = new XMLOutputter();
    			xmlo.setFormat(Format.getPrettyFormat());
    			xmlo.output(doc, btstream);
    			setDocXML(btstream.toByteArray());	
    			
    			
    			
    		}
    		catch (IOException e){
    			e.printStackTrace();
    		}
    }
    
    public byte[] getDocXML(){
    	return _docXML;
    }
    
    public void setDocXML(byte[] doc){
    	_docXML=doc;
    }
}
