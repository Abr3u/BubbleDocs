package pt.ulisboa.tecnico.sdis.store.ws;

public class Doc {

	private byte[] _byte;
	
	public Doc() {
		this._byte = new byte[0];
	}
	
	public Doc(byte[] bytes){
		
		_byte = bytes;
	}
	
	public byte[] get_Content(){		
		return _byte;
	}
	
	public void set_Content(byte[] _byte){
		this._byte = _byte;
	}
	
	public int getSize() {
		return this._byte.length;
	}
	
}
