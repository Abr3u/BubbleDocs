package pt.tecnico.bubbledocs.domain;

public abstract class Parametro extends Parametro_Base {
    
    public Parametro() {
        super();
    }
    
    public void delete(){
    	this.setBinfunc1(null);
    	this.setBinfunc2(null);
    	deleteDomainObject();
    }
    
    public abstract String toString();
    
    public abstract Integer compute();
    
}
