package decentchat.api;

public interface Contact {
	
	public String getPubKey();
	public Status getStatus();
	public String getStatusMessage();
	public void message(String message);
	
}
