package pt.tecnico.bubbledocs.domain;

import java.util.ArrayList;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.EmptyUsernameException;
import pt.tecnico.bubbledocs.exception.InvalidArgumentsException;
import pt.tecnico.bubbledocs.exception.UserAlreadyExistsException;


public class User extends User_Base {
    
    public User(String name, String username,String email) {
        super();
        init(name,username,email); 
    }
    
    public User(){
    	super();
    }
    
    public void init(String name, String username,String email)throws BubbleDocsException{
   
    	if(username.equals("root")){
    		throw new UserAlreadyExistsException(username);
    	}
    	setName(name);
    	setUsername(username);
    	setEmail(email);
    }
    
    @Override
    public void setUsername(String u){
    	if(u.length()==1){
    		throw new EmptyUsernameException();
    	}
    		
    	if(u.length()<3 ||u.length()>8){
    		throw new InvalidArgumentsException();
    	}
    	super.setUsername(u);
    }
    
    public void delete() {
    	for (Spreadsheet u : getCspreadsheetSet())
    	    u.delete();
    	setLtoken(null);
    	setBubbledocs(null);
    	deleteDomainObject();
        }
    
    public ArrayList<Spreadsheet> getCSpreadsByName(String name){
    	ArrayList<Spreadsheet> res = new ArrayList<Spreadsheet>();
    	for(Spreadsheet sp: this.getCspreadsheetSet()){
    		if (sp.getName().equals(name)){
    			res.add(sp);
    		}
    	}
    	return res;
    }
    
    
    
  
}
