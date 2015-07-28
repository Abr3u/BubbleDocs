package pt.tecnico.bubbledocs.domain;

public class CellInteger extends CellInteger_Base {
    
    public CellInteger(Integer value) {
        super();
        this.setValue(value);
    }
    
    
    @Override
    public String toString(){
    	return ""+this.getValue();
    }

	@Override
	public Integer compute() {
		return this.getValue();
		
	}
    
}
