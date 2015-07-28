package pt.tecnico.bubbledocs.domain;


import org.jdom2.Element;

import pt.tecnico.bubbledocs.exception.ImportDocumentException;

public class Cell extends Cell_Base {
    
    public Cell(Integer linha, Integer coluna, Conteudo cont) {
        super();
        init(linha, coluna,cont);
    }
    
    public Cell(Integer linha, Integer coluna) {
        super();
        init(linha, coluna);
    }
    
    public Cell(){
    	super();
    }
    
    private void init(Integer linha, Integer coluna, Conteudo cont){
    	this.setLinha(linha);
    	this.setColuna(coluna);
    	this.setProt(false);
    	this.setConteudo(cont);
    }
    
    public Integer compute(){
    	Conteudo c = getConteudo();
    	if (c == null){
    		return new Integer(0);
    	}
    	return c.compute();
    }
    
    private void init(Integer linha, Integer coluna){
    	this.setLinha(linha);
    	this.setColuna(coluna);
    	this.setProt(false);
    }
    
    public void delete(){
    	this.setSpreadsheet(null);
    	this.getConteudo().delete();
    	deleteDomainObject();
    }
    
    public Element exportToXML() {
    	Conteudo c = this.getConteudo();
    	if(c==null)
    		System.out.println("ESTA A NULL");
    	Element element = new Element("cell");
    	Element conteudo = new Element ("conteudo");
    	element.setAttribute("linha", ""+getLinha());
    	element.setAttribute("coluna", ""+getColuna());
    	conteudo.setAttribute("c", getConteudo().toString());
    	element.setAttribute("protected",( getProt()==false) ? "false" : "true");
    	element.addContent(conteudo);
    	return element;
        }
    
    public String toString(){
    	return "[" + this.getLinha() + "][" + this.getColuna() + "]->" + this.getConteudo().toString(); 
    	
    }
    
    public void importFromXML(Element info) throws ImportDocumentException{
    	try{
    	Element cont = info.getChild("conteudo");
    	setLinha(new Integer(info.getAttributeValue("linha")));
    	setColuna(new Integer(info.getAttributeValue("coluna")));
    	setProt(new Boolean(info.getAttributeValue("protected")));
    	setConteudo(parseContent(cont.getAttributeValue("c")));
    }catch (ImportDocumentException e){
    	System.err.println(e);
    }
    	
    }
    
    public  Conteudo parseContent(String content){ 
    	
    	if (content.contains("(")){     //Function
    		String[] parts = content.split("\\(");
    		String[] aux;
    		Parametro param1;
    		Parametro param2;
    		
    		parts = parts[1].split("\\)");
    		parts = parts[0].split("\\,");   //vetor com arg1 na posicao 0 e arg 2 na posicao 1
			    	//analise de argumento 1
			    		if (parts[0].contains(";")){     //referencia
			    			aux = parts[0].split("\\;");
			    			param1 = new ArgRef(new Integer(aux[0]), new Integer(aux[1]));
			    		}else param1 = new ArgLit(new Integer(parts[0]));
			    		
			    	//analise de argumento 2
			    		if (parts[1].contains(";")){     //referencia
			    			aux = parts[1].split("\\;");
			    			param2 = new ArgRef(new Integer(aux[0]), new Integer(aux[1]));
			    		}else param2 = new ArgLit(new Integer(parts[1]));
			    		
			  if (content.contains("ADD")){
				  FuncADD add = new FuncADD(param1, param2);
				  return add;
			  }
			  else if (content.contains("SUB")){
				  FuncSUB sub = new FuncSUB(param1, param2);
				  return sub;
			  }
			  else if (content.contains("DIV")){
				  FuncDIV div = new FuncDIV(param1, param2);
				  return div;
			  }
			  else if (content.contains("MUL")){
				  FuncMUL mul = new FuncMUL(param1, param2);
				  return mul;
			  }
			    		
    	}
    	
    	
    	else if(content.contains(";")){    //Reference  
    		String[] aux;
    		aux = content.split("\\;");
    		CellRef ref = new CellRef(new Integer(aux[0]), new Integer(aux[1]));
    		return ref;
    	}
    	
    	else{
    		CellInteger inte = new CellInteger(new Integer(content));
    		return inte;
    	}
    	return null;
    }
    
}
