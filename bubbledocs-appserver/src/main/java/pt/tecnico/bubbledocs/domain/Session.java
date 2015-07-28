package pt.tecnico.bubbledocs.domain;
import java.util.Set;

import org.joda.time.DateTime;

import pt.tecnico.bubbledocs.exception.UnknownBubbleDocsUserException;

public class Session extends Session_Base {
    
    public Session() {
        super();
    }
    
    public boolean hasOnlineUsers(){
    	if(this.getTokensSet().isEmpty()){
    		return false;
    	}
    	else{
    		return true;
    	}
    }
    
    public void maintenance(){
    	if (this.hasOnlineUsers()){
    		for (LToken tok : getTokensSet()){
    			if (!tok.isDateValid(new DateTime())){
    				this.removeUserFromSession(tok.getIdentifier());
    				System.out.println("User removido da Sessao: "+tok.getIdentifier());
    			}
    				
    		}
    	}
    }
    
    
    public boolean isRootToken(String tok){
    	for(User u : this.getBubbledocs().getUserSet()){
    		if(u.getUsername().equals("root") && u.getLtoken().getIdentifier().equals(tok)){
    				return true;
    			}
    		}
    	return false;
    }
    
    public boolean hasUsernameInSession(String username){
    	if(this.hasOnlineUsers()){
    		for(LToken tok: this.getTokensSet()){
    			if(tok.getUser().getUsername().equals(username)){
    				return true;
    			}
    		}//nao encontrou
    		return false;
    	}
    	return false;
    }
    
    public boolean isOnline(String token){
    	if(this.hasOnlineUsers()){
    		for(LToken t : this.getTokensSet()){
    			if(t.getIdentifier().equals(token)){
    					return true;
    			}
    		}
    	}
		return false;
    }
    
    public LToken getTokenFromUser(String username){
    	Set<LToken> users = this.getTokensSet();
    	if (users.isEmpty()){
    		return null;
    	}
    	for (LToken tk: users){
    		if (tk.getUser().getUsername().equals(username))
    			return tk;
    	}
    	return null;
    }
    
    public String addUsertoSession(String username) throws UnknownBubbleDocsUserException{
    	User u = this.getBubbledocs().getUserByUsername(username);
    	if(u!=null){//existe user
    		LToken tok = new LToken(u);
    		this.addTokens(tok);
    		return tok.getIdentifier();
    	}
    	else{
    		throw new UnknownBubbleDocsUserException();
    	}
    }
    
    public String addUsertoSession(String username,String old) throws UnknownBubbleDocsUserException{
    	User u = this.getBubbledocs().getUserByUsername(username);
    	if(u!=null){//existe user
    		Integer randomOld = Integer.parseInt(old.substring(old.length()-1)); 
    		LToken tok = new LToken(u,randomOld);
    		this.addTokens(tok);
    		return tok.getIdentifier();
    	}
    	else{
    		throw new UnknownBubbleDocsUserException();
    	}
    }
    
    public void removeUserFromSession(String usertoken){
    	if(this.hasOnlineUsers()){
    		if(this.isOnline(usertoken)){
    			for(LToken tok : this.getTokensSet()){
    				if(tok.getIdentifier().equals(usertoken)){
    					this.removeTokens(tok);
    				}
    			}
    		}
    	}	
    }
    
    public User getUserFromSession(String usertoken){
    	if(this.hasOnlineUsers()){
    		for(LToken tok: this.getTokensSet()){
    			if(tok.getIdentifier().equals(usertoken)){
    				return tok.getUser();
    			}
    		}
    	}
    	return null;
    }
    

    	
   
    	

    
}
