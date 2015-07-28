package pt.tecnico.bubbledocs.domain;

public class userRoot extends userRoot_Base {
    
	 public userRoot(String name, String username, String email) {
	        super();
	        init(name,username,email); 
	    }
	    
	    public userRoot(){
	    	super();
	    }
	    
	    public void init(String name, String username, String email){
	    	setName(name);
	    	setUsername(username);
	    	setEmail(email);
	    	setLtoken(new LToken(this));
	    }
    
}
