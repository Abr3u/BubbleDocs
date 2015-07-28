package pt.tecnico.bubbledocs.domain;


import java.util.Iterator;
import java.util.Set;

import org.jdom2.DataConversionException;
import org.jdom2.Element;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import pt.tecnico.bubbledocs.exception.ImportDocumentException;
import pt.tecnico.bubbledocs.exception.UnauthorizedOperationException;

public class Spreadsheet extends Spreadsheet_Base {


	public Spreadsheet(){
		super();
	}

	public Spreadsheet(User user, String name, int linhas, int colunas, Integer sID) {
		super();
		init(user, name, linhas, colunas, sID);
	}



	private void init(User user, String name, int linhas, int colunas, int sID){
		this.setName(name);
		this.setCuser(user);
		this.setColunastotais(colunas);
		this.setLinhastotais(linhas);
		this.setCreationDate(new DateTime());
		this.setSID(sID);
	}

	public String[][] getSpreadMatrix(){

		
		String[][] res = new String[getLinhastotais()][getColunastotais()];
		
		int i,j =0;
		
		//incializes the outuput string[][] with empty strings.
		for(i = 0; i < getLinhastotais(); i++){
			for(j=0; j < getColunastotais(); j++){
				res[i][j] = "";	
			}
		}
		
		for(Cell cell: this.getCellSet()){
			Conteudo cont = cell.getConteudo();
			res[cell.getLinha()][cell.getColuna()] = cell.compute().toString();
			cell.setConteudo(cont);
		}
	return res;
	}


	public boolean checkLine(Integer l){
		if(l<1 || l>this.getLinhastotais()){
			return false;
		}
		return true;
	}

	public boolean checkColumn(int c){
		if(c<1 || c>this.getColunastotais()){
			return false;
		}
		return true;
	}

	public void delete(){
		this.setBubbledocs(null);
		this.setCuser(null);
		for (User u: this.getRuserSet()){
			this.removeRuser(u);
		}
		for (User u: this.getWuserSet()){
			this.removeWuser(u);
		}
		for (Cell c: this.getCellSet()){
			c.delete();
		}
		deleteDomainObject();

	}

	public Element exportToXML() {

		Element element = new Element("spreadsheet");
		Element usr = new Element("user");
		element.setAttribute("name",getName());
		element.setAttribute("sid",""+getSID());
		usr.setAttribute("creator",getCuser().getUsername());
		element.setAttribute("data", getCreationDate().toString("dd/MM/yyyy HH:mm:ss"));
		element.setAttribute("linhas",""+getLinhastotais());
		element.setAttribute("colunas",""+getColunastotais());
		element.addContent(usr);
		Element cellElement = new Element("matrix");

		for (Cell c : getCellSet()) {
			Element cell = c.exportToXML();
			cellElement.addContent(cell);
		}
		element.addContent(cellElement);

		return element;
	}

	public Cell getCell(Integer linha, Integer coluna){
		System.out.println("LOOKING for "+linha+";"+coluna);
		Set<Cell> aux = this.getCellSet();
		Cell c = null;
		for (Cell cl: aux){
			if (cl.getLinha().equals(linha) && cl.getColuna().equals(coluna))
				return cl;
		}
		c = new Cell(linha, coluna);
		this.addCell(c);
		System.out.println("NEW ÇELL ............");
		return c;
	}

	public Integer[] parseCellID(String id){
		if(id.contains(";")){
			String[] linhaColuna=id.split(";");
			if(linhaColuna.length!=2){
				return null; // Argument isnt a reference
			}
			else{//sucesso
				Integer[] result = {null, null};
				Integer linhaaux = Integer.parseInt(linhaColuna[0]);
				Integer colunaaux = Integer.parseInt(linhaColuna[1]);
				result[0] = linhaaux;
				result[1] = colunaaux;
				return result;
			}
		}
		return null;
	}
	public boolean checkCoordinates(Integer l, Integer c){
		if(this.checkLine(l)){
			if(this.checkColumn(c)){
				return true;
			}
		}
		return false;
	}

	public boolean equals(Spreadsheet s){
		Cell c1 = null;
		Cell c2 = null;
		if (this.getColunastotais() != s.getColunastotais() || this.getLinhastotais() != s.getLinhastotais()){
			return false;
		}
		if (!this.getCreationDate().toString("dd/MM/yyyy HH:mm:ss").equals(s.getCreationDate().toString("dd/MM/yyyy HH:mm:ss"))){
			return false;
		}
		if (!this.getCuser().equals(s.getCuser())){
			return false;
		}
		if (this.getCellSet().isEmpty() && s.getCellSet().isEmpty()){
			return true;
		}
		if (this.getCellSet().isEmpty() || s.getCellSet().isEmpty()){
			return false;
		}
		Iterator<Cell> i1 = this.getCellSet().iterator();
		Iterator<Cell> i2 = s.getCellSet().iterator();
		while (true){
			c1 = (Cell) i1.next();
			c2 = (Cell) i2.next();
			if (!c1.toString().equals(c2.toString())){
				return false;
			}
			if (!i1.hasNext())
				break;
		}
		return true;
	}




	public boolean canWrite(User u){
		if(u.getUsername().equals("root"))
			return true;
		if (this.getCuser().equals(u))
			return true;
		if(this.getWuserSet().isEmpty()){
			return false;
		}
		for(User uw : this.getWuserSet()){
			if(uw.getUsername().equals(u.getUsername())){
				return true;
			}
		}
		return false;
	}

	public boolean canRead (User u){
		if (this.getRuserSet().isEmpty())
			return canWrite(u);
		for (User us: getRuserSet()){
			if (us.getUsername().equals(u.getUsername()))
				return true;
		}
		return canWrite(u);
	}
	
	public void importFromXML(Element spreadsheet) throws ImportDocumentException{


		try{		
			DateTime date;
			DateTimeFormatter dateObj;
			Element usr = spreadsheet.getChild("user");
			String username = usr.getAttributeValue("creator");
			BubbleDocs bd = BubbleDocs.getInstance();
			User user = bd.getUserByUsername(username);
			setName(spreadsheet.getAttribute("name").getValue());
			setCuser(user);
			setSID(bd.incSpreadCount());

			dateObj = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
			date = dateObj.parseDateTime(spreadsheet.getAttributeValue("data"));
			setCreationDate(date);
			setCuser(user);
			setLinhastotais(spreadsheet.getAttribute("linhas").getIntValue());

			setColunastotais(spreadsheet.getAttribute("colunas").getIntValue());

			setBubbledocs(bd);

		} catch (DataConversionException | ImportDocumentException e) { 
			System.err.println(e);
		}


		Element matrix = spreadsheet.getChild("matrix");
		for (Element cell : matrix.getChildren("cell")) {
			Cell c = new Cell();
			c.importFromXML(cell); //adiciona conteudo a celula importada
			addCell(c);
		}
	}

	public void addRuser(User u, User newuser)throws UnauthorizedOperationException{
		if(this.canWrite(u)){
			super.addRuser(newuser);
		}
		else{
			throw new UnauthorizedOperationException();
		}

	}


	public void addWuser(User u ,User newuser)throws UnauthorizedOperationException{
		if(this.canWrite(u)){
			super.addWuser(newuser);
		}
		else{
			throw new UnauthorizedOperationException();
		}
	}


	public void removeRuser(User u,User newuser)throws UnauthorizedOperationException{
		if(this.canWrite(u)){
			super.removeRuser(newuser);
		}
		else{
			throw new UnauthorizedOperationException();
		}
	}


	public void removeWuser(User u,User newuser)throws UnauthorizedOperationException{
		if(this.canWrite(u)){
			super.removeWuser(newuser);
		}
		else{
			throw new UnauthorizedOperationException();
		}
	}

}



