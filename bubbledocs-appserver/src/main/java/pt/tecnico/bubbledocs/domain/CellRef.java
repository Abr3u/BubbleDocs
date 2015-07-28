package pt.tecnico.bubbledocs.domain;

public class CellRef extends CellRef_Base {
    
    public CellRef(Integer l, Integer c) {
        super();
        this.setRefLine(l);
        this.setRefCol(c);
    }
    
    public void delete(){
    	this.setCellr(null);
    	super.delete();
    }
    
    @Override
    public String toString(){
    	return this.getRefLine()+";"+this.getRefCol();
    }

	@Override
	public Integer compute() {
		Cell cellr = getCellr();
		if (cellr == null){
			setCellr(getCell().getSpreadsheet().getCell(this.getRefLine(),this.getRefCol()));
		}
		return getCellr().compute();
	}
    
}
