package pt.tecnico.bubbledocs.service;

import pt.tecnico.bubbledocs.domain.*;
import pt.tecnico.bubbledocs.exception.*;

// add needed import declarations

public class CreateSpreadSheet extends BubbleDocsService {
    
	private int sheetId;  // id of the new sheet
    private String _token;
	private String _name;
    private int _rows;
    private int _columns;

    public CreateSpreadSheet(String userToken, String name, int rows,
            int columns) {
    	set_name(name);
    	set_token(userToken);
    	set_rows(rows);
    	set_columns(columns);
    }

    @Override
    protected void dispatch() throws BubbleDocsException {
    	BubbleDocs bd = BubbleDocs.getInstance();
    	if (_rows <1 || _columns <1)
    		throw new InvalidSpreadsheetArgumentsException(_rows, _columns);
    	if (_name.isEmpty())
    		throw new EmptyNameException();
    	if (!(bd.getSession().isOnline(_token)))
    		throw new UserNotInSessionException(_token);
    	User creator = bd.getSession().getUserFromSession(_token);
    	sheetId = bd.incSpreadCount();
    	bd.addSpreadsheets(new Spreadsheet(creator, _name, _rows, _columns, sheetId));
    	
    }
    
    
    
    
    
    public int getSheetId() {
        return sheetId;
    }
    
    public String get_token() {
		return _token;
	}

	public void set_token(String _token) {
		this._token = _token;
	}

	public String get_name() {
		return _name;
	}

	public void set_name(String _name) {
		this._name = _name;
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

}
