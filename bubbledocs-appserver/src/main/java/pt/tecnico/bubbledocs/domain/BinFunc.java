package pt.tecnico.bubbledocs.domain;

public abstract class BinFunc extends BinFunc_Base {
    
    public BinFunc(){
    	super();
    }
    
    @Override
    public void delete(){
    	this.getParametro1().delete();
    	this.getParametro2().delete();
    	super.delete();
    }
   
}
