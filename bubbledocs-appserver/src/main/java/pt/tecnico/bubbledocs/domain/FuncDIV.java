package pt.tecnico.bubbledocs.domain;

public class FuncDIV extends FuncDIV_Base {
    
	public FuncDIV(Parametro p1,Parametro p2) {
        super();
        init(p1,p2);
    }
    
    public void init(Parametro p1,Parametro p2){
    	this.setParametro1(p1);
    	this.setParametro2(p2);
    }
    
    @Override
    public String toString(){
    	return "=DIV("+this.getParametro1().toString()+","+this.getParametro2().toString()+")";
    }

	@Override
	public Integer compute() {
		return this.getParametro1().compute() / this.getParametro2().compute();
		
	}
}
