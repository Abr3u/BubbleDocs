package pt.tecnico.bubbledocs.domain;

public class ArgLit extends ArgLit_Base {
    
	public ArgLit(Integer value) {
        super();
        this.setValue(value);
    }
	
	  public String toString(){
	    	return ""+this.getValue();
	    }

	@Override
	public Integer compute() {
		return this.getValue();
	}
	  
	  
    
}
