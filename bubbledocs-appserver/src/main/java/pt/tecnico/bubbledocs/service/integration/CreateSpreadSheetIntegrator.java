package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.service.CreateSpreadSheet;

public class CreateSpreadSheetIntegrator extends BubbleDocsIntegrator {

	
    private String _token;
	private String _name;
    private int _rows;
    

	private int _columns;
    private int ID;
    
    CreateSpreadSheet localService = null;


	public CreateSpreadSheetIntegrator(String userToken, String name, int rows,
            int columns) {
    	this._name = name;
    	this._token = userToken;
    	this._rows = rows;
    	this._columns = columns;
    }
    
	
	@Override
	protected void dispatch() throws Exception {
		
		CreateSpreadSheet localService = new CreateSpreadSheet(_token,_name,_rows,_columns);
	
		localService.execute();
		this.setID(localService.getSheetId());
	
	}
	
	public int get_rows() {
		return _rows;
	}


	public void set_rows(int _rows) {
		this._rows = _rows;
	}


	public int get_columns() {
		return _columns;
	}


	public void set_columns(int _columns) {
		this._columns = _columns;
	}
	
	
	private void setID(int sheetId) {
		ID=sheetId;
	}
	
	public int getID(){
		return ID;
	}

}
