package pt.tecnico.bubbledocs.domain;

public abstract class Conteudo extends Conteudo_Base {
    
    public Conteudo() {
        super();
    }

	public void delete() {
		this.setCell(null);
		deleteDomainObject();
	}
	
	public abstract Integer compute();
	
	public String toString(){
		return "#VALUE";
	}
    
}
