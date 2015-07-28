package pt.tecnico.bubbledocs.domain;
import org.joda.time.DateTime;
import java.util.Random;

public class LToken extends LToken_Base {
    
    public LToken(User u) {
    	super();
       this.setUser(u);
       this.setLogindate(new DateTime());
       if (u.getUsername().equals("root")){
    	   this.setIdentifier(u.getUsername());
       }
       else{
    	   int r = (new Random().nextInt(10));
    	   this.setIdentifier(u.getUsername()+r);
       }
    }
    
    
    
    public LToken(User u, int old) {
        super();
        this.setUser(u);
        this.setLogindate(new DateTime());
        if (u.getUsername().equals("root")){
     	   this.setIdentifier(u.getUsername());
        }
        else{
        	while(true){
        		int r = (new Random().nextInt(10));
        		if (r != old){
        			this.setIdentifier(u.getUsername()+r);
        			break;
        		}
        	}
        }
     }
    
    public void refreshToken(){
    	if(this.getIdentifier().equals("root")){
    		this.setLogindate(new DateTime());
    	}
    	else{	
	    	int r = (new Random().nextInt(9));
	    	if (this.getIdentifier().equals(this.getUser().getUsername()+r)){ //gerou mesmo random
	    		this.refreshToken();
	    	}
	    	else{
	    		this.setIdentifier(this.getUser().getUsername()+r);
	        	this.setLogindate(new DateTime());
	    	}
    }	
    }
    
    public boolean isDateValid(DateTime data){
    	double hours = (data.getMillis() - this.getLogindate().getMillis()) /1000/60/60;
		if(hours>2){
			return false;
		}
		return true;
    }
    
    public void delete(){
    	this.setUser(null);
    	this.setSession(null);
    	deleteDomainObject();
    }
}