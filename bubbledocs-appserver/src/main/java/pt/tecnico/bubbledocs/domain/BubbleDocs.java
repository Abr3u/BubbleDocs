package pt.tecnico.bubbledocs.domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.exception.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exception.UserAlreadyExistsException;

import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.Document;
import org.jdom2.JDOMException;

public class BubbleDocs extends BubbleDocs_Base {
    
    public BubbleDocs() {
    	FenixFramework.getDomainRoot().setBubbledocs(this);
    }
   

    public static BubbleDocs getInstance(){
    	BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
    	if (bd == null){
    		bd = new BubbleDocs();
    		bd.setSession(new Session());
    		bd.setSpreadCount(new Integer(0));
    	}
    	return bd;
    }
    
    public static void deleteAllUsers(){
    	BubbleDocs bd = BubbleDocs.getInstance();
    	bd.setRootUser(null);
    	for (User u: bd.getUserSet()){
    		u.delete();
    	}
    	
    }
    
    public static userRoot getuserroot(){
    	BubbleDocs bd = getInstance();
    	userRoot root = bd.getRootUser();
    	if (root == null){
    		root = new userRoot("Super User", "root", "rootroot");
    		bd.setRootUser(root);
    		bd.addUser(root);
    	}
    	return bd.getRootUser();
    		
    }
    
    public static void start(){
    	getInstance();getuserroot();
    }
    
    public void deleteSpread(String name, User creator){
    	ArrayList<Spreadsheet> sps = creator.getCSpreadsByName(name);
    	for (Spreadsheet sp: sps){
    		sp.delete();
    	}
    }
    
    public Spreadsheet searchSpreadByName(String name){
    	Set<Spreadsheet> sps = getSpreadsheetsSet();
    	
    	for(Spreadsheet sp : sps){
    		if(sp.getName().equals(name)){
    			return sp;
    		}
    	}
    	return null;
    	
    }
    
    public String[][] getSpreadMatrix(Integer spreadId){
    	
    	Spreadsheet aux = searchSpreadBySID(spreadId);
    	
    	return aux.getSpreadMatrix();
    	
    }
    
    
    @Atomic
	public static org.jdom2.Document convertByteToXML(byte[] bytes){
		SAXBuilder sb = new SAXBuilder();
		ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
		Document doc = null;
		try {
			doc = sb.build(stream);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
    public Spreadsheet searchSpreadBySID(Integer sid){
    	Set<Spreadsheet> sps = getSpreadsheetsSet();
    	
    	for(Spreadsheet sp : sps){
    		if(sp.getSID() == sid){
    			return sp;
    		}
    	}
    	return null;
    	
    }
    
  
    public User getUserByUsername(String username){
    	for (User u: this.getUserSet()){
    		if (u.getUsername().equals(username))
    			return u;
    	}
    	return null;
    }

    public boolean hasUser(String userName) {
    	return getUserByUsername(userName) != null;
    }

    @Override
    public void addUser(User userToBeAdded) throws UserAlreadyExistsException{
    	if (this.getUserByUsername(userToBeAdded.getUsername()) == null){

    		super.addUser(userToBeAdded);
    	}
    	else throw new UserAlreadyExistsException(userToBeAdded.getUsername());
    }


    public Integer incSpreadCount(){
    	Integer res = this.getSpreadCount();
    	if (res == null){
    		res = new Integer(0);
    		System.out.println("New SID");
    		this.setSpreadCount(res);
    		return res;
    	}
    	this.setSpreadCount(res+1);
    	return res;
    }

    public boolean checkDados(String username,String password){


    	if(this.getUserByUsername(username)!=null){
    		if(this.getUserByUsername(username).getPassword().equals(password)){
    			return true;
    		}

    	}
    	return false;

    }


    
    @Atomic
	public
	static void convertFromXML(User u, org.jdom2.Document jdomDoc) {

		Spreadsheet spread = new Spreadsheet();
		spread.setCuser(u);
		System.out.println("Creator "+u.getUsername());
		spread.importFromXML(jdomDoc.getRootElement());
	}


	@Atomic
	public static org.jdom2.Document convertToXML(org.jdom2.Element ele) {

		org.jdom2.Document jdomDoc = new org.jdom2.Document();
		jdomDoc.setRootElement(ele);

		return jdomDoc;
	}
	
	

	@Atomic
	public static String printDomainInXML(org.jdom2.Document jdomDoc) {
		XMLOutputter xml = new XMLOutputter();
		xml.setFormat(Format.getPrettyFormat());
		return xml.outputString(jdomDoc);
	}


	public String tryLocalLogin(String username,String password) throws NullPointerException{
		
		if(getUserByUsername(username)==null){
    		throw new LoginBubbleDocsException();
		}
		
		if(getUserByUsername(username).getPassword().isEmpty() || 
				!getUserByUsername(username).getPassword().equals(password)){
			getUserByUsername(username).setPassword(password);
		}
		if(getSession().getTokensSet().isEmpty()){//se nao ha tokens online
			getSession().addUsertoSession(username);
			
			return getUserByUsername(username).getLtoken().getIdentifier();
		}
		else{ //ha token online
			if(getSession().hasUsernameInSession(username)){
				String old = getSession().getTokenFromUser(username).getIdentifier();
				getSession().removeUserFromSession(old);
				getSession().addUsertoSession(username,old);
				return getUserByUsername(username).getLtoken().getIdentifier();
			}
			else{
				getSession().addUsertoSession(username);
				return getUserByUsername(username).getLtoken().getIdentifier();
			}
		}
		
	};
	
	
	public boolean isOnline(String userToken){
		
		 return getSession().isOnline(userToken);
			 
		
	}


	public void renewPass(String _userToken) {
		User u = this.getSession().getUserFromSession(_userToken);
		u.setPassword(null);
	}
	
public static Conteudo parseContent(String content){ 
    	
    	if (content.contains("(")){     //Function
    		String[] parts = content.split("\\(");
    		String[] aux;
    		Parametro param1;
    		Parametro param2;
    		
    		parts = parts[1].split("\\)");
    		parts = parts[0].split("\\,");   //vetor com arg1 na posicao 0 e arg 2 na posicao 1
			    	//analise de argumento 1
			    		if (parts[0].contains(";")){     //referencia
			    			aux = parts[0].split(";");
			    			param1 = new ArgRef(new Integer(aux[0]), new Integer(aux[1]));
			    		}else param1 = new ArgLit(new Integer(parts[0]));
			    		
			    	//analise de argumento 2
			    		if (parts[1].contains(";")){     //referencia
			    			aux = parts[1].split("\\;");
			    			param2 = new ArgRef(new Integer(aux[0]), new Integer(aux[1]));
			    		}else param2 = new ArgLit(new Integer(parts[1]));
			    		
			  if (content.contains("ADD")){
				  FuncADD add = new FuncADD(param1, param2);
				  return add;
			  }
			  else if (content.contains("SUB")){
				  FuncSUB sub = new FuncSUB(param1, param2);
				  return sub;
			  }
			  else if (content.contains("DIV")){
				  FuncDIV div = new FuncDIV(param1, param2);
				  return div;
			  }
			  else if (content.contains("MUL")){
				  FuncMUL mul = new FuncMUL(param1, param2);
				  return mul;
			  }
			    		
    	}
    	
    	
    	else if(content.contains(";")){    //Reference  
    		String[] aux;
    		aux = content.split("\\;");
    		CellRef ref = new CellRef(new Integer(aux[0]), new Integer(aux[1]));
    		return ref;
    	}
    	
    	else{
    		CellInteger inte = new CellInteger(new Integer(content));
    		return inte;
    	}
    	return null;
    }
	

}
