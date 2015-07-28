package pt.tecnico.bubbledocs.domain;

public class ArgRef extends ArgRef_Base {
    
    public ArgRef(Integer reflinha,Integer refcoluna) {
        super();
        this.setReflinha(reflinha);
        this.setRefcoluna(refcoluna);
    }
    @Override
    public void delete(){
    	this.setCellra(null);
    	super.delete();
    }
    public String toString(){
    	return this.getReflinha()+";"+this.getRefcoluna();
    }

    @Override
	public Integer compute() {
		Cell cellr = getCellra();
		if (cellr == null){
			BinFunc bf1 = getBinfunc1();
			if (bf1 != null)
				setCellra(bf1.getCell().getSpreadsheet().getCell(getReflinha(),getRefcoluna()));
			else
				setCellra(getBinfunc2().getCell().getSpreadsheet().getCell(getReflinha(),getRefcoluna()));
		}
		return getCellra().compute();
	}
      
    
    
    
}
