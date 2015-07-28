package pt.tecnico.bubbledocs.exception;

public class UserDoesNotExistException extends BubbleDocsException{


		private static final long serialVersionUID = 1L;

		private String contactName;

		public UserDoesNotExistException(String contactName) {
			this.contactName = contactName;
		}
		
		public String getContactName() {
			return this.contactName+"\n";
		}
	}