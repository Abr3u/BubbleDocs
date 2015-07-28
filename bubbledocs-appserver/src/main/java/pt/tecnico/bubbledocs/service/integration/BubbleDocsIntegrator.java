package pt.tecnico.bubbledocs.service.integration;



public abstract class BubbleDocsIntegrator {

	
	 
	    public final void execute() throws Exception {
	    	
	    	dispatch();
	    }
	
	    protected abstract void dispatch() throws Exception;
}
